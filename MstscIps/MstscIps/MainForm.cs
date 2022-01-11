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
        private IFeignIP ipFeign = ProxyLoader.GetProxy<IFeignIP>();

        private List<VpsMachineDto> ipList;
        private bool isAsc = true;
        private string _pwd;
        private string _url;

        public MainForm()
        {
            InitializeComponent();
            LoadConfig();
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
            StartMstsc(txtIp.Text, _pwd);
        }

        private static void StartMstsc(string ip, string pwd)
        {
            try
            {
                if (string.IsNullOrEmpty(ip))
                {
                    MessageBox.Show("IP不能为空");
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
                StartMstsc(ip, _pwd);
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

            BindListView(ipList, e.Column, isAsc);
            isAsc = !isAsc;
        }

        private void BindListView(List<VpsMachineDto> ips, int sortCol, bool asc)
        {
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
            StartMstsc(txtIp.Text, _pwd);
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

            _pwd = urlAndPwd[2];
            _url = urlAndPwd[1];
        }

        private void button2_Click(object sender, EventArgs e)
        {
            try
            {
                ParseUrlAndPwd();
                var url = _url;
                if (string.IsNullOrEmpty(url))
                {
                    MessageBox.Show("配置有误，请检查并重新保存配置");
                    return;
                }

                ipList = ipFeign.GetIP(new Uri(url));
                BindListView(ipList, 1, true);
            }
            catch (Exception exp)
            {
                MessageBox.Show(exp + "");
            }
        }
    }
}