using System;
using System.Diagnostics;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using RemindClock.Utils;

namespace RemindClock
{
    public partial class AlertForm : Form
    {
        [DllImport("user32")]
        private static extern bool AnimateWindow(IntPtr hwnd, int dwTime, int dwFlags);

        private const int AW_HIDE = 0x10000; //隐藏窗口
        private const int AW_ACTIVE = 0x20000; //激活窗口，在使用了AW_HIDE标志后不要使用这个标志
        private const int AW_BLEND = 0x80000; //使用淡入淡出效果

        public AlertForm(int noteId)
        {
            InitializeComponent();
            this.label1.Text = noteId.ToString();
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
        }

        private void Button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}