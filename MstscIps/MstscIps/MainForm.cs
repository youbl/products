using System;
using System.Collections.Generic;
using Beinet.Feign;
using System.Windows.Forms;
using MstscIps.Feign;
using System.Diagnostics;
using System.Text;
using System.Text.RegularExpressions;
using MstscIps.Feign.Dto;
using MstscIps.Utils;

namespace MstscIps
{
    public partial class MainForm : Form
    {
        private readonly IFeignIP _ipFeign = ProxyLoader.GetProxy<IFeignIP>();

        private List<VpsMachineDto> _ipList;

        // ListView排序标识，true为顺序，false为倒序
        private bool _isAsc = true;
        private int _sortCol = 1;
        private string _oldSearchTxt = "";

        // 选中的配置项
        private ConfigItem _configItem;

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

        private VpsMachineDto FindPwd(string ip)
        {
            if (string.IsNullOrEmpty(ip) || _ipList == null || _ipList.Count == 0)
                return GetDefault();

            foreach (var machineDto in _ipList)
            {
                if (ip == machineDto.VpsIp)
                {
                    if (string.IsNullOrEmpty(machineDto.VpsPwd))
                    {
                        machineDto.VpsPwd = txtPwd.Text;

                        if (machineDto.ImageName != null &&
                            machineDto.ImageName.IndexOf("tencent", StringComparison.OrdinalIgnoreCase) >= 0 &&
                            machineDto.VpsPwd.Length > 30
                           )
                        {
                            machineDto.VpsPwd = machineDto.VpsPwd.Substring(0, 30); // 腾讯云只支持30位密码
                        }
                    }

                    return machineDto;
                }
            }

            return GetDefault();
        }

        private VpsMachineDto GetDefault()
        {
            var ret = new VpsMachineDto();
            ret.VpsPwd = txtPwd.Text;
            return ret;
        }


        private void StartMstsc(string ip)
        {
            var pwd = FindPwd(ip);
            StartMstscReal(ip, pwd.User, pwd.VpsPwd);
        }


        private static void StartMstscReal(string ip, string user, string pwd)
        {
            try
            {
                if (string.IsNullOrEmpty(user))
                    user = "administrator";
                ip = (ip ?? "").Trim();
                pwd = (pwd ?? "").Trim();
                if (ip.Length == 0 || pwd.Length == 0)
                {
                    MessageBox.Show("IP或远程密码不能为空");
                    return;
                }

                if (!StrHelper.IsIp(ip))
                {
                    MessageBox.Show("无效的IP格式");
                    return;
                }

                if (!string.IsNullOrEmpty(pwd))
                {
                    var savePwd = "/generic:" + ip + " /user:" + user + " /pass:\"" + pwd + "\" ";
                    Process.Start("cmdkey", savePwd);
                    System.Threading.Thread.Sleep(100);
                }

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
            if (e.Column < 1)
                return;

            BindListView(_ipList, e.Column, _isAsc);
            _isAsc = !_isAsc;
        }

        private void BindListView(List<VpsMachineDto> ips, int sortCol, bool asc)
        {
            listView1.Items.Clear();
            if (ips == null || ips.Count == 0)
                return;

            this._sortCol = sortCol;
            ips.Sort((item1, item2) =>
                asc
                    ? string.Compare(GetDtoCol(item1, sortCol), GetDtoCol(item2, sortCol), StringComparison.Ordinal)
                    : string.Compare(GetDtoCol(item2, sortCol), GetDtoCol(item1, sortCol), StringComparison.Ordinal));

            var idx = 0;
            foreach (var ip in ips)
            {
                idx++;
                var item = new ListViewItem(new[]
                {
                    idx.ToString(),
                    ip.VpsIp,
                    ip.GroupCode,
                    ip.CreateDate,
                    ip.InstanceId,
                    ip.ImageName
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
                case 4:
                    return dto.InstanceId;
                case 5:
                    return dto.ImageName;
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

            txtPwd.Text = urlAndPwd.Pwd;
            _configItem = urlAndPwd;
        }

        private void btnResreshIpList_Click(object sender, EventArgs e)
        {
            BindListView(null, 0, false); // 清空
            var begin = DateTime.Now;
            try
            {
                ParseUrlAndPwd();
                var url = _configItem.Url;
                if (string.IsNullOrEmpty(url))
                {
                    MessageBox.Show("配置有误，请检查并重新保存配置");
                    return;
                }

                if (_configItem.Type == ConfigType.Url)
                {
                    _ipList = _ipFeign.GetIP(new Uri(url));
                }
                else if (_configItem.Type == ConfigType.File)
                {
                    _ipList = ConfigUtil.Default.ReadIpFile(url);
                }

                txtIpFilter_KeyUp(null, null);
            }
            catch (Exception exp)
            {
                MessageBox.Show(exp + "");
            }
            finally
            {
                var costTime = (int) (DateTime.Now - begin).TotalMilliseconds;
                toolStripLabel2.Text =
                    "刷新耗时:" + costTime.ToString() + "ms, 行数:" + (_ipList?.Count ?? 0);
            }
        }

        private void txtIpFilter_KeyUp(object sender, KeyEventArgs e)
        {
            if (e != null)
            {
                if (e.KeyCode == Keys.Escape)
                {
                    txtIpFilter.Text = "";
                }
                else
                {
                    // 只允许数字和小数点
                    var newTxt = Regex.Replace(txtIpFilter.Text, @"[^\d\.]", "");
                    if (txtIpFilter.Text != newTxt)
                    {
                        txtIpFilter.Text = newTxt;
                        txtIpFilter.SelectionStart = newTxt.Length;
                        // txtIpFilter.Select(newTxt.Length, 0);
                    }
                }
            }

            if (_ipList == null || _ipList.Count <= 0)
                return;

            List<VpsMachineDto> showIpList;
            var filter = txtIpFilter.Text.Trim();
            if (filter.Length > 0 && filter == _oldSearchTxt)
            {
                return;
            }

            _oldSearchTxt = filter;
            if (filter.Length <= 0)
            {
                showIpList = _ipList;
            }
            else
            {
                showIpList = new List<VpsMachineDto>();
                foreach (var dto in _ipList)
                {
                    if (!string.IsNullOrEmpty(dto.VpsIp) && dto.VpsIp.Contains(filter))
                    {
                        showIpList.Add(dto);
                    }
                }
            }

            if (showIpList.Count == 0)
            {
                MessageBox.Show("无结果，请修改IP检索内容");
                txtIpFilter.SelectAll();
                txtIpFilter.Focus();
                return;
            }

            BindListView(showIpList, _sortCol, _isAsc);
        }

        private void 立即远程ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            listView1_DoubleClick(sender, e);
        }

        private void 复制ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (listView1.SelectedItems.Count < 1)
            {
                MessageBox.Show("请先选中行");
                return;
            }

            var sb = new StringBuilder();
            foreach (ListViewItem item in listView1.SelectedItems)
            {
                foreach (ListViewItem.ListViewSubItem subItem in item.SubItems)
                {
                    sb.Append(subItem.Text).Append(", ");
                }

                sb.AppendLine();
            }

            Clipboard.SetText(sb.ToString());
        }

        private void lvContextMenu_Opening(object sender, System.ComponentModel.CancelEventArgs e)
        {
            var enabled = listView1.SelectedItems.Count >= 1;
            foreach (ToolStripItem item in lvContextMenu.Items)
            {
                item.Enabled = enabled;
            }
        }
    }
}