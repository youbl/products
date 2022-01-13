using System;
using System.Collections.Generic;
using Beinet.Feign;
using System.Windows.Forms;
using MstscIps.Feign;
using System.Diagnostics;
using System.Text.RegularExpressions;
using MstscIps.Feign.Dto;

namespace MstscIps
{
    public partial class MainForm : Form
    {
        private readonly IFeignIP _ipFeign = ProxyLoader.GetProxy<IFeignIP>();

        private List<VpsMachineDto> _ipList;

        // ListView排序标识，true为顺序，false为倒序
        private bool _isAsc = true;

        // 配置里的IP获取地址
        private string _url;

        public MainForm()
        {
            InitializeComponent();
            LoadConfig();

            toolStripLabel1.Text = "启动时间:" + DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss 有问题请联系北亮");
        }

        private void listView1_SelectedIndexChanged(object sender, EventArgs e)
        {
            try
            {
                if (listView1.SelectedItems.Count < 1 || listView1.SelectedItems[0].SubItems.Count < 2)
                    return;

                var ip = listView1.SelectedItems[0].SubItems[1].Text;
                txtIp.Text = ip;
            }
            catch (Exception exp)
            {
                MessageBox.Show("出错了:" + exp.Message);
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            StartMstsc(txtIp.Text);
        }

        private string FindPwd(string ip)
        {
            if (string.IsNullOrEmpty(ip) || _ipList == null || _ipList.Count == 0)
                return txtPwd.Text;

            string ret = null;
            foreach (var machineDto in _ipList)
            {
                if (ip == machineDto.VpsIp)
                {
                    ret = machineDto.VpsPwd;
                    break;
                }
            }

            if (string.IsNullOrEmpty(ret))
                return txtPwd.Text;
            return ret;
        }


        private void StartMstsc(string ip)
        {
            var pwd = FindPwd(ip);
            StartMstscReal(ip, pwd);
        }


        private static void StartMstscReal(string ip, string pwd)
        {
            try
            {
                ip = (ip ?? "").Trim();
                pwd = (pwd ?? "").Trim();
                if (ip.Length == 0 || pwd.Length == 0)
                {
                    MessageBox.Show("IP或远程密码不能为空");
                    return;
                }

                if (!Regex.IsMatch(ip, @"^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$"))
                {
                    MessageBox.Show("无效的IP格式");
                    return;
                }

                var savePwd = "/generic:" + ip + " /user:administrator /pass:\"" + pwd + "\" ";
                Process.Start("cmdkey", savePwd);
                System.Threading.Thread.Sleep(1000);
                Process.Start("mstsc", "/f /v:" + ip); // 全屏
            }
            catch (Exception exp)
            {
                MessageBox.Show("开远程出错:" + exp.Message);
            }
        }

        private void listView1_DoubleClick(object sender, EventArgs e)
        {
            try
            {
                if (listView1.SelectedItems.Count < 1 || listView1.SelectedItems[0].SubItems.Count < 2)
                    return;

                var ip = listView1.SelectedItems[0].SubItems[1].Text;
                StartMstsc(ip);
            }
            catch (Exception exp)
            {
                MessageBox.Show("出错了:" + exp.Message);
            }
        }

        private void listView1_ColumnClick(object sender, ColumnClickEventArgs e)
        {
            if (e.Column < 1 || e.Column > 3)
                return;

            BindListView(_ipList, e.Column, _isAsc);
            _isAsc = !_isAsc;
        }

        private void BindListView(List<VpsMachineDto> ips, int sortCol, bool asc)
        {
            if (ips == null || ips.Count == 0)
                return;

            ips.Sort((item1, item2) =>
                asc
                    ? string.Compare(GetDtoCol(item1, sortCol), GetDtoCol(item2, sortCol), StringComparison.Ordinal)
                    : string.Compare(GetDtoCol(item2, sortCol), GetDtoCol(item1, sortCol), StringComparison.Ordinal));

            listView1.Items.Clear();
            var idx = 0;
            foreach (var ip in ips)
            {
                idx++;
                var item = new ListViewItem(new[]
                {
                    idx.ToString(),
                    ip.VpsIp,
                    ip.GroupCode,
                    ip.CreateDate
                });
                listView1.Items.Add(item);
            }
        }

        private string GetDtoCol(VpsMachineDto dto, int col)
        {
            switch (col)
            {
                default:
                    return dto.VpsIp;
                case 2:
                    return dto.GroupCode;
                case 3:
                    return dto.CreateDate;
            }
        }

        private void txtIp_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter && !e.Control && !e.Alt)
                StartMstsc(txtIp.Text);
        }

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            new EditForm().ShowDialog();
            LoadConfig();
        }

        private void LoadConfig()
        {
            lstUrls.Items.Clear();
            var items = ConfigUtil.Default.ReadConfigList();
            if (items == null || items.Length <= 0)
                return;
            foreach (var item in items)
            {
                lstUrls.Items.Add(item);
            }

            lstUrls.SelectedIndex = 0;
            ParseUrlAndPwd();
        }

        private void ParseUrlAndPwd()
        {
            var urlAndPwd = ConfigUtil.Default.SplitConfig(lstUrls.Text);
            if (urlAndPwd == null)
            {
                MessageBox.Show("配置有误，请检查并重新保存配置");
                return;
            }

            txtPwd.Text = urlAndPwd[2];
            _url = urlAndPwd[1];
        }

        private void button2_Click(object sender, EventArgs e)
        {
            var begin = DateTime.Now;
            try
            {
                ParseUrlAndPwd();
                var url = _url;
                if (string.IsNullOrEmpty(url))
                {
                    MessageBox.Show("配置有误，请检查并重新保存配置");
                    return;
                }

                _ipList = _ipFeign.GetIP(new Uri(url));
                BindListView(_ipList, 1, true);
            }
            catch (Exception exp)
            {
                MessageBox.Show(exp + "");
            }
            finally
            {
                var costTime = (int) (DateTime.Now - begin).TotalMilliseconds;
                toolStripLabel2.Text = "刷新耗时:" + costTime.ToString() + "ms";
            }
        }
    }
}