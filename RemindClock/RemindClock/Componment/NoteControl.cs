using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using RemindClock.Repository.Model;

namespace RemindClock.Componment
{
    public partial class NoteControl : UserControl
    {
        private const int FIRST_X = 3;
        private const int FIRST_Y = 11;
        private List<Notes.NoteDetail> details;

        public NoteControl(List<Notes.NoteDetail> details)
        {
            InitializeComponent();
            this.details = details;
        }

        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);
            RebindDetails();
        }

        private void LnkPlusOnLinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            var newItem = new Notes.NoteDetail
            {
                EventTime = DateTime.Now,
                EventType = "单次",
            };
            details.Add(newItem);
            AddItemControl(newItem, details.Count - 1);

            // 手工触发事件
            this.OnVisibleChanged(e);
        }

        private void LnkMinusOnLinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            var idx = int.Parse(((Control) sender).Name.Replace("lnkMinus", ""));
            details.RemoveAt(idx); // 删了，别的控件错乱了，重新绑定一遍
            RebindDetails();

            // 手工触发事件
            this.OnVisibleChanged(e);
        }

        void AddItemControl(Notes.NoteDetail detail, int idx)
        {
            var pointY = (FIRST_Y + 20) * idx + FIRST_Y;

            var labTime = new System.Windows.Forms.Label();
            var txtNoteTime = new System.Windows.Forms.DateTimePicker();
            var labRepeat = new System.Windows.Forms.Label();
            var lstRepeat = new System.Windows.Forms.ComboBox();
            var lnkMinus = new System.Windows.Forms.LinkLabel();
            var lnkPlus = new System.Windows.Forms.LinkLabel();
            this.panel1.Controls.Add(labTime);
            this.panel1.Controls.Add(txtNoteTime);
            this.panel1.Controls.Add(labRepeat);
            this.panel1.Controls.Add(lstRepeat);
            this.panel1.Controls.Add(lnkMinus);
            this.panel1.Controls.Add(lnkPlus);

            // 
            // labTime
            // 
            labTime.AutoSize = true;
            labTime.Location = new System.Drawing.Point(FIRST_X, pointY);
            labTime.Name = "labTime" + idx;
            labTime.Text = "提醒时间:";
            // 
            // txtNoteTime
            // 
            txtNoteTime.Location = new System.Drawing.Point(65, pointY);
            txtNoteTime.MinDate = new System.DateTime(2020, 5, 5);
            txtNoteTime.Name = "txtNoteTime" + idx;
            txtNoteTime.Size = new System.Drawing.Size(150, 21);
            txtNoteTime.Value = detail.EventTime;
            txtNoteTime.Format = DateTimePickerFormat.Custom;
            txtNoteTime.CustomFormat = "yyyy-MM-dd HH:mm:ss";
            txtNoteTime.ValueChanged += (sender, args) =>
            {
                detail.EventTime = txtNoteTime.Value;
                this.OnVisibleChanged(null);
            };

            // 
            // labRepeat
            // 
            labRepeat.AutoSize = true;
            labRepeat.Location = new System.Drawing.Point(220, pointY);
            labRepeat.Name = "labRepeat" + idx;
            labRepeat.Text = "重复方式:";
            // 
            // lstRepeat
            // 
            lstRepeat.FormattingEnabled = true;
            lstRepeat.DropDownStyle = ComboBoxStyle.DropDownList;
            lstRepeat.Items.AddRange(new object[]
            {
                "单次",
                "每分钟",
                "每小时",
                "每天",
                "周一~周五每天",
                "周六~周日每天",
                "每周",
                "每月",
                "每年"
            });
            lstRepeat.Location = new System.Drawing.Point(280, pointY);
            lstRepeat.Name = "lstRepeat" + idx;
            lstRepeat.Size = new System.Drawing.Size(121, 20);
            lstRepeat.Text = detail.EventType;
            lstRepeat.SelectedIndexChanged += (sender, args) =>
            {
                detail.EventType = lstRepeat.Text;
                this.OnVisibleChanged(null);
            };
            // 
            // lnkMinus
            // 
            lnkMinus.AutoSize = true;
            lnkMinus.LinkBehavior = System.Windows.Forms.LinkBehavior.NeverUnderline;
            lnkMinus.Location = new System.Drawing.Point(410, pointY);
            lnkMinus.Name = "lnkMinus" + idx;
            lnkMinus.Text = "删除";
            //lnkMinus.Font = new System.Drawing.Font("宋体", 20, System.Drawing.FontStyle.Bold);
            lnkMinus.LinkClicked += LnkMinusOnLinkClicked;
            // 
            // lnkPlus
            // 
            lnkPlus.AutoSize = true;
            lnkPlus.LinkBehavior = System.Windows.Forms.LinkBehavior.NeverUnderline;
            lnkPlus.Location = new System.Drawing.Point(440, pointY);
            lnkPlus.Name = "lnkPlus" + idx;
            lnkPlus.Text = "添加";
            //lnkPlus.Font = new System.Drawing.Font("宋体", 20, System.Drawing.FontStyle.Bold);
            lnkPlus.LinkClicked += LnkPlusOnLinkClicked;
        }

        void RebindDetails()
        {
            panel1.Controls.Clear();
            if (details.Count <= 0)
            {
                LnkPlusOnLinkClicked(null, null);
            }
            else
            {
                for (int i = 0, j = details.Count; i < j; i++)
                {
                    AddItemControl(details[i], i);
                }
            }
        }
    }
}