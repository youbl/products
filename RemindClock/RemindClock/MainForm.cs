using System;
using System.Windows.Forms;
using RemindClock.Repository.Model;
using RemindClock.Utils;

namespace RemindClock
{
    public partial class MainForm : Form
    {
        private static MainForm _default;
        private bool realClose = false;

        public static MainForm Default
        {
            get
            {
                if (_default == null)
                {
                    _default = new MainForm();
                }

                return _default;
            }
        }

        public MainForm()
        {
            InitializeComponent();
        }

        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);
            CheckAutoStart();
        }

        /// <summary>
        /// 最小化到托盘的双击事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void notifyIcon1_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            this.ShowInTaskbar = true; // 任务栏显示
            this.Visible = true; // 设置窗体可见
            this.WindowState = FormWindowState.Normal; // 设置窗体为默认大小
            this.Activate(); // 激活当前窗口，避免不可见
            notifyIcon1.Visible = false;
        }

        /// <summary>
        /// 窗体大小变更事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void MainForm_SizeChanged(object sender, System.EventArgs e)
        {
            if (this.WindowState == FormWindowState.Minimized)
            {
                // 进入最小化状态时, 任务栏隐藏
                this.ShowInTaskbar = false;
                // 托盘可见
                notifyIcon1.Visible = true;
                // 隐藏窗体
                this.Visible = false;
            }
        }

        private void 打开提醒机ToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            notifyIcon1_MouseDoubleClick(sender, null);
        }

        private void 退出ToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            this.realClose = true;
            //this.Dispose(true);
            this.Close();
        }

        private void 开机启动ToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            var checkedStatus = !开机启动ToolStripMenuItem.Checked;
            try
            {
                AutoStartHelper.AutoStart(this.Text, checkedStatus);
                开机启动ToolStripMenuItem.Checked = checkedStatus;
            }
            catch (Exception exp)
            {
                MessageBox.Show("请先获取管理员权限:" + exp.Message);
            }
        }

        /// <summary>
        /// 已设置开机启动时，自动最小化
        /// </summary>
        private void CheckAutoStart()
        {
            if (AutoStartHelper.IsAutoStart(this.Text))
            {
                开机启动ToolStripMenuItem.Checked = true;
                this.WindowState = FormWindowState.Minimized;
            }
        }

        private void BtnNew_Click(object sender, EventArgs e)
        {
            new NoteForm(new Notes()).ShowDialog(this);
        }

        // 不让关闭，改成最小化
        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (this.realClose)
            {
                return; // 菜单退出，要真实的退出
            }

            // 点x，最小化，而不是退出
            e.Cancel = true;
            this.WindowState = FormWindowState.Minimized;
        }
    }
}