using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
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

            this.txtTitle.Text = this.notes.Title;
            this.txtContent.Text = this.notes.Content;
            ShowDeails();
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

            this.notes.Title = txtContent.Text.Trim();

            notesService.Save(this.notes);
            MessageBox.Show("保存成功");
        }

        private NoteControl noteControl;

        // 点击提醒文本框，弹浮层选择时间
        private void TxtNoteTime_MouseClick(object sender, MouseEventArgs e)
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
    }
}