namespace RemindClock
{
    partial class ConfigForm
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
            this.chkEnableSync = new System.Windows.Forms.CheckBox();
            this.labSyncUrl = new System.Windows.Forms.Label();
            this.txtSyncUrl = new System.Windows.Forms.TextBox();
            this.txtSyncUser = new System.Windows.Forms.TextBox();
            this.labSyncUser = new System.Windows.Forms.Label();
            this.txtSyncToken = new System.Windows.Forms.TextBox();
            this.labSyncToken = new System.Windows.Forms.Label();
            this.btnSave = new System.Windows.Forms.Button();
            this.btnCancel = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // chkEnableSync
            // 
            this.chkEnableSync.AutoSize = true;
            this.chkEnableSync.Location = new System.Drawing.Point(13, 13);
            this.chkEnableSync.Name = "chkEnableSync";
            this.chkEnableSync.Size = new System.Drawing.Size(72, 16);
            this.chkEnableSync.TabIndex = 0;
            this.chkEnableSync.Text = "启用同步";
            this.chkEnableSync.UseVisualStyleBackColor = true;
            // 
            // labSyncUrl
            // 
            this.labSyncUrl.AutoSize = true;
            this.labSyncUrl.Location = new System.Drawing.Point(13, 36);
            this.labSyncUrl.Name = "labSyncUrl";
            this.labSyncUrl.Size = new System.Drawing.Size(65, 12);
            this.labSyncUrl.TabIndex = 1;
            this.labSyncUrl.Text = "同步地址：";
            // 
            // txtSyncUrl
            // 
            this.txtSyncUrl.Location = new System.Drawing.Point(74, 33);
            this.txtSyncUrl.Name = "txtSyncUrl";
            this.txtSyncUrl.Size = new System.Drawing.Size(408, 21);
            this.txtSyncUrl.TabIndex = 2;
            // 
            // txtSyncUser
            // 
            this.txtSyncUser.Location = new System.Drawing.Point(74, 60);
            this.txtSyncUser.Name = "txtSyncUser";
            this.txtSyncUser.Size = new System.Drawing.Size(408, 21);
            this.txtSyncUser.TabIndex = 4;
            // 
            // labSyncUser
            // 
            this.labSyncUser.AutoSize = true;
            this.labSyncUser.Location = new System.Drawing.Point(13, 63);
            this.labSyncUser.Name = "labSyncUser";
            this.labSyncUser.Size = new System.Drawing.Size(65, 12);
            this.labSyncUser.TabIndex = 3;
            this.labSyncUser.Text = "同步账号：";
            // 
            // txtSyncToken
            // 
            this.txtSyncToken.Location = new System.Drawing.Point(74, 87);
            this.txtSyncToken.Name = "txtSyncToken";
            this.txtSyncToken.Size = new System.Drawing.Size(408, 21);
            this.txtSyncToken.TabIndex = 6;
            // 
            // labSyncToken
            // 
            this.labSyncToken.AutoSize = true;
            this.labSyncToken.Location = new System.Drawing.Point(13, 90);
            this.labSyncToken.Name = "labSyncToken";
            this.labSyncToken.Size = new System.Drawing.Size(65, 12);
            this.labSyncToken.TabIndex = 5;
            this.labSyncToken.Text = "同步密钥：";
            // 
            // btnSave
            // 
            this.btnSave.Location = new System.Drawing.Point(74, 115);
            this.btnSave.Name = "btnSave";
            this.btnSave.Size = new System.Drawing.Size(75, 23);
            this.btnSave.TabIndex = 7;
            this.btnSave.Text = "保存";
            this.btnSave.UseVisualStyleBackColor = true;
            this.btnSave.Click += new System.EventHandler(this.BtnSave_Click);
            // 
            // btnCancel
            // 
            this.btnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.btnCancel.Location = new System.Drawing.Point(197, 115);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(75, 23);
            this.btnCancel.TabIndex = 8;
            this.btnCancel.Text = "取消";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.BtnCancel_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.ForeColor = System.Drawing.Color.Red;
            this.label1.Location = new System.Drawing.Point(91, 14);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(305, 12);
            this.label1.TabIndex = 9;
            this.label1.Text = "启用后，可以在任意电脑编辑数据，同步到所有其它电脑";
            // 
            // ConfigForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.btnCancel;
            this.ClientSize = new System.Drawing.Size(498, 159);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.btnSave);
            this.Controls.Add(this.txtSyncToken);
            this.Controls.Add(this.labSyncToken);
            this.Controls.Add(this.txtSyncUser);
            this.Controls.Add(this.labSyncUser);
            this.Controls.Add(this.txtSyncUrl);
            this.Controls.Add(this.labSyncUrl);
            this.Controls.Add(this.chkEnableSync);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "ConfigForm";
            this.Text = "同步配置";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.CheckBox chkEnableSync;
        private System.Windows.Forms.Label labSyncUrl;
        private System.Windows.Forms.TextBox txtSyncUrl;
        private System.Windows.Forms.TextBox txtSyncUser;
        private System.Windows.Forms.Label labSyncUser;
        private System.Windows.Forms.TextBox txtSyncToken;
        private System.Windows.Forms.Label labSyncToken;
        private System.Windows.Forms.Button btnSave;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Label label1;
    }
}