using System;
using System.Text;
using System.Windows.Forms;
using PopupTool;
using RemindClock.Componment;
using RemindClock.Repository.Model;
using RemindClock.Services;

namespace RemindClock
{
    public partial class NoteForm : Form
    {
        private readonly Notes notes;
        private readonly NotesService notesService = new NotesService();

        public NoteForm(Notes notes)
        {
            this.notes = notes ?? throw new ArgumentException("提醒不能为空");
            InitializeComponent();
            this.Icon = RemindClock.Properties.Resources.clock;

            SetTextByNote();
            ShowDeails();
        }

        private void SetTextByNote()
        {
            this.txtTitle.Text = this.notes.Title;
            this.txtContent.Text = this.notes.Content;
            if (!string.IsNullOrEmpty(this.notes.DingDingToken))
            {
                this.chkDingDing.Checked = true;
                this.txtDingDingToken.Text = this.notes.DingDingToken;
            }
        }

        private void NoteForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            // this.Hide();
            // e.Cancel = true;
        }

        private void BtnSave_Click(object sender, EventArgs e)
        {
            this.notes.Title = txtTitle.Text.Trim();
            if (this.notes.Title.Length <= 0)
            {
                MessageBox.Show("标题必填");
                return;
            }

            this.notes.Content = txtContent.Text.Trim();
            if (chkDingDing.Checked)
            {
                this.notes.DingDingToken = txtDingDingToken.Text.Trim();
                if (this.notes.DingDingToken.Length <= 0)
                {
                    MessageBox.Show("钉钉Token必填");
                    return;
                }
            }
            else
            {
                this.notes.DingDingToken = "";
            }

            notesService.Save(this.notes);
            MessageBox.Show("保存成功");
        }

        private NoteControl noteControl;

        // 点击提醒文本框，弹浮层选择时间
        private void TxtNoteTime_MouseClick(object sender, EventArgs e)
        {
            noteControl = new NoteControl(this.notes.Details);
            noteControl.VisibleChanged += NoteControl_SizeChanged; // 这个不能正常工作

            Popup popup = new Popup(noteControl);
            popup.Show(txtNoteTime, false);
        }

        private void NoteControl_SizeChanged(object sender, EventArgs e)
        {
            ShowDeails();
        }

        private void ShowDeails()
        {
            var sb = new StringBuilder();
            foreach (var detail in notes.Details)
            {
                sb.Append("[").Append(detail).Append("];");
            }

            txtNoteTime.Text = sb.ToString();
        }

        private void chkDingDing_CheckedChanged(object sender, EventArgs e)
        {
            txtDingDingToken.Visible = chkDingDing.Checked;
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        //        protected override bool ProcessCmdKey(ref Message msg, Keys keyData)
        //        {
        //            int WM_KEYDOWN = 256;
        //            int WM_SYSKEYDOWN = 260;
        //            if (msg.Msg == WM_KEYDOWN | msg.Msg == WM_SYSKEYDOWN)
        //
        //            {
        //                switch (keyData)
        //
        //                {
        //                    case Keys.Escape:
        //
        //                        this.Close(); //esc关闭窗体
        //
        //                        break;
        //                }
        //            }
        //
        //            return false;
        //        }


        //        protected override void OnKeyPress(KeyPressEventArgs e)
        //        {
        //            if (e.KeyChar == (char) Keys.Escape)
        //            {
        //                this.Close();
        //                return;
        //            }
        //
        //            base.OnKeyPress(e);
        //        }
    }
}