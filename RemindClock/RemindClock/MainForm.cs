using System.Windows.Forms;
using RemindClock.Properties;

namespace RemindClock
{
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
            // 设置窗体icon
            this.Icon = Resources.clock;
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
    }
}