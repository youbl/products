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

namespace RemindClock
{
    public partial class NoteForm : Form
    {
        private Notes notes;

        public NoteForm(Notes notes)
        {
            this.notes = notes ?? throw new ArgumentException("提醒不能为空");
            InitializeComponent();
        }

        private void NoteForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            // this.Hide();
            // e.Cancel = true;
        }
    }
}