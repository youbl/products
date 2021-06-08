namespace RemindClock
{
    sealed partial class AlertForm
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(AlertForm));
            this.labTime = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.labTitle = new System.Windows.Forms.Label();
            this.labNote = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // labTime
            // 
            this.labTime.AutoSize = true;
            this.labTime.Location = new System.Drawing.Point(12, 99);
            this.labTime.Name = "labTime";
            this.labTime.Size = new System.Drawing.Size(167, 12);
            this.labTime.TabIndex = 0;
            this.labTime.Text = "2021年06月05日 20点42分40秒";
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(376, 93);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 1;
            this.button1.Text = "关闭";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.Button1_Click);
            // 
            // labTitle
            // 
            this.labTitle.AutoSize = true;
            this.labTitle.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.labTitle.Location = new System.Drawing.Point(12, 9);
            this.labTitle.Name = "labTitle";
            this.labTitle.Size = new System.Drawing.Size(44, 12);
            this.labTitle.TabIndex = 2;
            this.labTitle.Text = "提醒：";
            // 
            // labNote
            // 
            this.labNote.AutoSize = true;
            this.labNote.ForeColor = System.Drawing.Color.Red;
            this.labNote.Location = new System.Drawing.Point(12, 31);
            this.labNote.Name = "labNote";
            this.labNote.Size = new System.Drawing.Size(65, 12);
            this.labNote.TabIndex = 3;
            this.labNote.Text = "我是提醒！";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.ForeColor = System.Drawing.Color.Red;
            this.label1.Location = new System.Drawing.Point(200, 99);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(185, 12);
            this.label1.TabIndex = 4;
            this.label1.Text = "建议：完成后再关闭，以免忘记！";
            // 
            // AlertForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.LightCyan;
            this.ClientSize = new System.Drawing.Size(458, 118);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.labNote);
            this.Controls.Add(this.labTitle);
            this.Controls.Add(this.labTime);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "AlertForm";
            this.Text = "AlertForm";
            this.TopMost = true;
            this.MouseDown += new System.Windows.Forms.MouseEventHandler(this.AlertForm_MouseDown);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label labTime;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Label labTitle;
        private System.Windows.Forms.Label labNote;
        private System.Windows.Forms.Label label1;
    }
}