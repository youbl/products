﻿namespace RemindClock
{
    partial class NoteForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.txtTitle = new System.Windows.Forms.TextBox();
            this.txtContent = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.btnSave = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.txtNoteTime = new System.Windows.Forms.TextBox();
            this.chkDingDing = new System.Windows.Forms.CheckBox();
            this.txtDingDingToken = new System.Windows.Forms.TextBox();
            this.btnCancel = new System.Windows.Forms.Button();
            this.chkEnabled = new System.Windows.Forms.CheckBox();
            this.txtUrl = new System.Windows.Forms.TextBox();
            this.chkUrl = new System.Windows.Forms.CheckBox();
            this.txtPhone = new System.Windows.Forms.TextBox();
            this.chkPhone = new System.Windows.Forms.CheckBox();
            this.label4 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(13, 31);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(65, 12);
            this.label1.TabIndex = 0;
            this.label1.Text = "记事标题：";
            // 
            // txtTitle
            // 
            this.txtTitle.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtTitle.Location = new System.Drawing.Point(69, 28);
            this.txtTitle.Name = "txtTitle";
            this.txtTitle.Size = new System.Drawing.Size(626, 21);
            this.txtTitle.TabIndex = 2;
            // 
            // txtContent
            // 
            this.txtContent.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtContent.Location = new System.Drawing.Point(69, 170);
            this.txtContent.Multiline = true;
            this.txtContent.Name = "txtContent";
            this.txtContent.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.txtContent.Size = new System.Drawing.Size(719, 268);
            this.txtContent.TabIndex = 5;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(13, 173);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(65, 12);
            this.label2.TabIndex = 0;
            this.label2.Text = "记事内容：";
            // 
            // btnSave
            // 
            this.btnSave.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnSave.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnSave.Location = new System.Drawing.Point(701, 8);
            this.btnSave.Name = "btnSave";
            this.btnSave.Size = new System.Drawing.Size(87, 58);
            this.btnSave.TabIndex = 6;
            this.btnSave.Text = "保  存";
            this.btnSave.UseVisualStyleBackColor = true;
            this.btnSave.Click += new System.EventHandler(this.BtnSave_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(13, 59);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(65, 12);
            this.label3.TabIndex = 0;
            this.label3.Text = "提醒时间：";
            // 
            // txtNoteTime
            // 
            this.txtNoteTime.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtNoteTime.Location = new System.Drawing.Point(69, 56);
            this.txtNoteTime.Name = "txtNoteTime";
            this.txtNoteTime.Size = new System.Drawing.Size(626, 21);
            this.txtNoteTime.TabIndex = 0;
            this.txtNoteTime.TabStop = false;
            this.txtNoteTime.Click += new System.EventHandler(this.TxtNoteTime_MouseClick);
            // 
            // chkDingDing
            // 
            this.chkDingDing.AutoSize = true;
            this.chkDingDing.Location = new System.Drawing.Point(69, 84);
            this.chkDingDing.Name = "chkDingDing";
            this.chkDingDing.Size = new System.Drawing.Size(72, 16);
            this.chkDingDing.TabIndex = 3;
            this.chkDingDing.Text = "钉钉提醒";
            this.chkDingDing.UseVisualStyleBackColor = true;
            this.chkDingDing.CheckedChanged += new System.EventHandler(this.chkDingDing_CheckedChanged);
            // 
            // txtDingDingToken
            // 
            this.txtDingDingToken.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtDingDingToken.Location = new System.Drawing.Point(159, 82);
            this.txtDingDingToken.Name = "txtDingDingToken";
            this.txtDingDingToken.Size = new System.Drawing.Size(536, 21);
            this.txtDingDingToken.TabIndex = 4;
            this.txtDingDingToken.Visible = false;
            // 
            // btnCancel
            // 
            this.btnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.btnCancel.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnCancel.Location = new System.Drawing.Point(701, 72);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(87, 29);
            this.btnCancel.TabIndex = 7;
            this.btnCancel.Text = "取  消";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
            // 
            // chkEnabled
            // 
            this.chkEnabled.AutoSize = true;
            this.chkEnabled.Location = new System.Drawing.Point(69, 6);
            this.chkEnabled.Name = "chkEnabled";
            this.chkEnabled.Size = new System.Drawing.Size(72, 16);
            this.chkEnabled.TabIndex = 1;
            this.chkEnabled.Text = "允许提醒";
            this.chkEnabled.UseVisualStyleBackColor = true;
            // 
            // txtUrl
            // 
            this.txtUrl.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtUrl.Location = new System.Drawing.Point(159, 108);
            this.txtUrl.Name = "txtUrl";
            this.txtUrl.Size = new System.Drawing.Size(536, 21);
            this.txtUrl.TabIndex = 9;
            this.txtUrl.Visible = false;
            // 
            // chkUrl
            // 
            this.chkUrl.AutoSize = true;
            this.chkUrl.Location = new System.Drawing.Point(69, 110);
            this.chkUrl.Name = "chkUrl";
            this.chkUrl.Size = new System.Drawing.Size(90, 16);
            this.chkUrl.TabIndex = 8;
            this.chkUrl.Text = "URL回调提醒";
            this.chkUrl.UseVisualStyleBackColor = true;
            this.chkUrl.CheckedChanged += new System.EventHandler(this.ChkUrl_CheckedChanged);
            // 
            // txtPhone
            // 
            this.txtPhone.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtPhone.Location = new System.Drawing.Point(159, 134);
            this.txtPhone.Name = "txtPhone";
            this.txtPhone.Size = new System.Drawing.Size(234, 21);
            this.txtPhone.TabIndex = 11;
            this.txtPhone.Visible = false;
            // 
            // chkPhone
            // 
            this.chkPhone.AutoSize = true;
            this.chkPhone.Location = new System.Drawing.Point(69, 136);
            this.chkPhone.Name = "chkPhone";
            this.chkPhone.Size = new System.Drawing.Size(72, 16);
            this.chkPhone.TabIndex = 10;
            this.chkPhone.Text = "短信提醒";
            this.chkPhone.UseVisualStyleBackColor = true;
            this.chkPhone.CheckedChanged += new System.EventHandler(this.ChkPhone_CheckedChanged);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.ForeColor = System.Drawing.Color.Red;
            this.label4.Location = new System.Drawing.Point(399, 137);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(125, 12);
            this.label4.TabIndex = 0;
            this.label4.Text = "短信提醒仅支持阿里云";
            // 
            // NoteForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.btnCancel;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.txtPhone);
            this.Controls.Add(this.chkPhone);
            this.Controls.Add(this.txtUrl);
            this.Controls.Add(this.txtDingDingToken);
            this.Controls.Add(this.chkUrl);
            this.Controls.Add(this.chkEnabled);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.chkDingDing);
            this.Controls.Add(this.txtContent);
            this.Controls.Add(this.txtNoteTime);
            this.Controls.Add(this.txtTitle);
            this.Controls.Add(this.btnSave);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label1);
            this.MinimizeBox = false;
            this.Name = "NoteForm";
            this.Text = "提醒编辑";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.NoteForm_FormClosing);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txtTitle;
        private System.Windows.Forms.TextBox txtContent;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnSave;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox txtNoteTime;
        private System.Windows.Forms.CheckBox chkDingDing;
        private System.Windows.Forms.TextBox txtDingDingToken;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.CheckBox chkEnabled;
        private System.Windows.Forms.TextBox txtUrl;
        private System.Windows.Forms.CheckBox chkUrl;
        private System.Windows.Forms.TextBox txtPhone;
        private System.Windows.Forms.CheckBox chkPhone;
        private System.Windows.Forms.Label label4;
    }
}