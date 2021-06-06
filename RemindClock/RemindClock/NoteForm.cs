using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
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
        }

        private void NoteForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            // this.Hide();
            // e.Cancel = true;
        }

        private void BtnSave_Click(object sender, EventArgs e)
        {
            notesService.Save(this.notes);
            MessageBox.Show("保存成功");
        }
    }
}