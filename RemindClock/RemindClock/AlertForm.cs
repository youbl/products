using System;
using System.Diagnostics;
using System.Windows.Forms;
using RemindClock.Utils;

namespace RemindClock
{
    public partial class AlertForm : Form
    {
        public int NoteId { get; set; }

        public AlertForm()
        {
            InitializeComponent();
        }

        public void ShowMe()
        {
//            var handle = Process.GetCurrentProcess().MainWindowHandle;
//            var form = Form.FromHandle(handle);
//            form.Invoke((EventHandler) delegate
//            {
//                this.label1.Text = NoteId.ToString();
//                this.ShowDialog(MainForm.Default);
//            });

            // 无法完成模态功能，先忽略
            FormHelper.Invoke(MainForm.Default, () =>
            {
                this.label1.Text = NoteId.ToString();
                this.TopMost = true;
                this.ShowDialog(MainForm.Default);
            });
        }
    }
}