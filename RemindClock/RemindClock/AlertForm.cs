using System;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using RemindClock.Utils;

namespace RemindClock
{
    public sealed partial class AlertForm : Form
    {
        #region 用于实现按下鼠标，拖动窗体的引入代码

        [DllImport("user32.dll")]
        public static extern bool ReleaseCapture();

        [DllImport("user32.dll")]
        public static extern bool SendMessage(IntPtr hwnd, int wMsg, int wParam, int lParam);

        private const int VM_NCLBUTTONDOWN = 0XA1; //定义鼠标左键按下
        private const int HTCAPTION = 2;

        #endregion

        #region 用于实现右下角弹窗的引入代码

        [DllImport("user32")]
        private static extern bool AnimateWindow(IntPtr hwnd, int dwTime, int dwFlags);

        private const int AW_HIDE = 0x10000; //隐藏窗口
        private const int AW_ACTIVE = 0x20000; //激活窗口，在使用了AW_HIDE标志后不要使用这个标志
        private const int AW_BLEND = 0x80000; //使用淡入淡出效果

        #endregion

        public AlertForm(string title, string content)
        {
            InitializeComponent();
            this.Icon = RemindClock.Properties.Resources.clock;

            this.labTime.Text = GetTimeTxt();
            this.labNote.Text = FormatNote(content);
            if (!string.IsNullOrEmpty(title))
            {
                this.Text = title;
                this.labTitle.Text = title;
            }
        }

        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);

            // 设置在右下角弹窗
            int x = Screen.PrimaryScreen.WorkingArea.Right - this.Width;
            int y = Screen.PrimaryScreen.WorkingArea.Bottom - this.Height;
            this.Location = new Point(x, y);
            AnimateWindow(this.Handle, 1000, AW_ACTIVE);
            this.FormClosing += (a, b) => { AnimateWindow(this.Handle, 1000, AW_BLEND | AW_HIDE); };

            // label控件也支持拖动
            foreach (var item in this.Controls)
            {
                if (item is Label label)
                    label.MouseDown += new MouseEventHandler(AlertForm_MouseDown);
            }
        }

        private void Button1_Click(object sender, EventArgs e)
        {
            var result = MessageBox.Show("请先确认完成，再关闭，以免忘记！\r\n确认要关闭吗？",
                "关了就没法提醒了！",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning,
                MessageBoxDefaultButton.Button2);
            if (result == DialogResult.Yes)
                this.Close();
        }

        private void AlertForm_MouseDown(object sender, MouseEventArgs e)
        {
            //为当前应用程序释放鼠标捕获
            ReleaseCapture();
            //发送消息 让系统误以为在标题栏上按下鼠标
            SendMessage((IntPtr) this.Handle, VM_NCLBUTTONDOWN, HTCAPTION, 0);
        }

        /// <summary>
        /// 字太长了，要加入换行符
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        private string FormatNote(string note)
        {
            var lineLen = 30;
            if (string.IsNullOrEmpty(note) || note.Length <= lineLen)
                return note;

            var sb = new StringBuilder(note.Length);
            int i = lineLen, j = note.Length;
            for (; i < j; i += lineLen)
            {
                sb.Append(note.Substring(i - lineLen, lineLen)).Append("\r\n");
            }

            if (i - lineLen < j)
            {
                sb.Append(note.Substring(i - lineLen));
            }

            return sb.ToString();
        }

        private string GetTimeTxt()
        {
            var now = DateTime.Now;
            return now.ToString("yyyy-MM-dd HH:mm:ss") + " " + now.DayOfWeek;
        }

        public static void Show(string title, string content)
        {
            ThreadPool.UnsafeQueueUserWorkItem(
                state => { FormHelper.Invoke(MainForm.Default, () => { new AlertForm(title, content).Show(); }); },
                null);
        }
    }
}