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
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.txtAliSmsUrl = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.txtAliAk = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.txtAliSk = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.txtAliSign = new System.Windows.Forms.TextBox();
            this.label6 = new System.Windows.Forms.Label();
            this.txtAliTemplateCode = new System.Windows.Forms.TextBox();
            this.lab1 = new System.Windows.Forms.Label();
            this.txtAliTempParam = new System.Windows.Forms.TextBox();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.SuspendLayout();
            // 
            // chkEnableSync
            // 
            this.chkEnableSync.AutoSize = true;
            this.chkEnableSync.Location = new System.Drawing.Point(7, 27);
            this.chkEnableSync.Name = "chkEnableSync";
            this.chkEnableSync.Size = new System.Drawing.Size(72, 16);
            this.chkEnableSync.TabIndex = 0;
            this.chkEnableSync.Text = "启用同步";
            this.chkEnableSync.UseVisualStyleBackColor = true;
            // 
            // labSyncUrl
            // 
            this.labSyncUrl.AutoSize = true;
            this.labSyncUrl.Location = new System.Drawing.Point(7, 50);
            this.labSyncUrl.Name = "labSyncUrl";
            this.labSyncUrl.Size = new System.Drawing.Size(65, 12);
            this.labSyncUrl.TabIndex = 1;
            this.labSyncUrl.Text = "同步地址：";
            // 
            // txtSyncUrl
            // 
            this.txtSyncUrl.Location = new System.Drawing.Point(68, 47);
            this.txtSyncUrl.Name = "txtSyncUrl";
            this.txtSyncUrl.Size = new System.Drawing.Size(408, 21);
            this.txtSyncUrl.TabIndex = 2;
            // 
            // txtSyncUser
            // 
            this.txtSyncUser.Location = new System.Drawing.Point(68, 74);
            this.txtSyncUser.Name = "txtSyncUser";
            this.txtSyncUser.Size = new System.Drawing.Size(408, 21);
            this.txtSyncUser.TabIndex = 4;
            // 
            // labSyncUser
            // 
            this.labSyncUser.AutoSize = true;
            this.labSyncUser.Location = new System.Drawing.Point(7, 77);
            this.labSyncUser.Name = "labSyncUser";
            this.labSyncUser.Size = new System.Drawing.Size(65, 12);
            this.labSyncUser.TabIndex = 3;
            this.labSyncUser.Text = "同步账号：";
            // 
            // txtSyncToken
            // 
            this.txtSyncToken.Location = new System.Drawing.Point(68, 101);
            this.txtSyncToken.Name = "txtSyncToken";
            this.txtSyncToken.Size = new System.Drawing.Size(408, 21);
            this.txtSyncToken.TabIndex = 6;
            // 
            // labSyncToken
            // 
            this.labSyncToken.AutoSize = true;
            this.labSyncToken.Location = new System.Drawing.Point(7, 104);
            this.labSyncToken.Name = "labSyncToken";
            this.labSyncToken.Size = new System.Drawing.Size(65, 12);
            this.labSyncToken.TabIndex = 5;
            this.labSyncToken.Text = "同步密钥：";
            // 
            // btnSave
            // 
            this.btnSave.Location = new System.Drawing.Point(128, 366);
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
            this.btnCancel.Location = new System.Drawing.Point(251, 366);
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
            this.label1.Location = new System.Drawing.Point(85, 28);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(305, 12);
            this.label1.TabIndex = 9;
            this.label1.Text = "启用后，可以在任意电脑编辑数据，同步到所有其它电脑";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.txtSyncUrl);
            this.groupBox1.Controls.Add(this.txtSyncToken);
            this.groupBox1.Controls.Add(this.txtSyncUser);
            this.groupBox1.Controls.Add(this.labSyncUrl);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Controls.Add(this.chkEnableSync);
            this.groupBox1.Controls.Add(this.labSyncUser);
            this.groupBox1.Controls.Add(this.labSyncToken);
            this.groupBox1.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.groupBox1.Location = new System.Drawing.Point(3, 12);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(485, 132);
            this.groupBox1.TabIndex = 10;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "同步配置";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.txtAliTempParam);
            this.groupBox2.Controls.Add(this.lab1);
            this.groupBox2.Controls.Add(this.txtAliTemplateCode);
            this.groupBox2.Controls.Add(this.label6);
            this.groupBox2.Controls.Add(this.txtAliSign);
            this.groupBox2.Controls.Add(this.label5);
            this.groupBox2.Controls.Add(this.txtAliSk);
            this.groupBox2.Controls.Add(this.label4);
            this.groupBox2.Controls.Add(this.txtAliAk);
            this.groupBox2.Controls.Add(this.label3);
            this.groupBox2.Controls.Add(this.txtAliSmsUrl);
            this.groupBox2.Controls.Add(this.label2);
            this.groupBox2.Location = new System.Drawing.Point(3, 160);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(485, 192);
            this.groupBox2.TabIndex = 11;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "阿里云短信配置";
            // 
            // txtAliSmsUrl
            // 
            this.txtAliSmsUrl.Location = new System.Drawing.Point(68, 26);
            this.txtAliSmsUrl.Name = "txtAliSmsUrl";
            this.txtAliSmsUrl.Size = new System.Drawing.Size(408, 21);
            this.txtAliSmsUrl.TabIndex = 4;
            this.txtAliSmsUrl.Text = "https://dysmsapi.aliyuncs.com/?Action=SendSms";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(7, 29);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(65, 12);
            this.label2.TabIndex = 3;
            this.label2.Text = "服务地址：";
            // 
            // txtAliAk
            // 
            this.txtAliAk.Location = new System.Drawing.Point(68, 53);
            this.txtAliAk.Name = "txtAliAk";
            this.txtAliAk.Size = new System.Drawing.Size(408, 21);
            this.txtAliAk.TabIndex = 11;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(43, 57);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(29, 12);
            this.label3.TabIndex = 10;
            this.label3.Text = "AK：";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(43, 84);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(29, 12);
            this.label4.TabIndex = 10;
            this.label4.Text = "SK：";
            // 
            // txtAliSk
            // 
            this.txtAliSk.Location = new System.Drawing.Point(68, 80);
            this.txtAliSk.Name = "txtAliSk";
            this.txtAliSk.Size = new System.Drawing.Size(408, 21);
            this.txtAliSk.TabIndex = 11;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(7, 110);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(65, 12);
            this.label5.TabIndex = 10;
            this.label5.Text = "短信签名：";
            // 
            // txtAliSign
            // 
            this.txtAliSign.Location = new System.Drawing.Point(68, 107);
            this.txtAliSign.Name = "txtAliSign";
            this.txtAliSign.Size = new System.Drawing.Size(408, 21);
            this.txtAliSign.TabIndex = 11;
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(7, 137);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(65, 12);
            this.label6.TabIndex = 10;
            this.label6.Text = "短信模板：";
            // 
            // txtAliTemplateCode
            // 
            this.txtAliTemplateCode.Location = new System.Drawing.Point(68, 134);
            this.txtAliTemplateCode.Name = "txtAliTemplateCode";
            this.txtAliTemplateCode.Size = new System.Drawing.Size(408, 21);
            this.txtAliTemplateCode.TabIndex = 11;
            // 
            // lab1
            // 
            this.lab1.AutoSize = true;
            this.lab1.Location = new System.Drawing.Point(7, 164);
            this.lab1.Name = "lab1";
            this.lab1.Size = new System.Drawing.Size(65, 12);
            this.lab1.TabIndex = 10;
            this.lab1.Text = "模板参数：";
            // 
            // txtAliTempParam
            // 
            this.txtAliTempParam.Location = new System.Drawing.Point(68, 161);
            this.txtAliTempParam.Name = "txtAliTempParam";
            this.txtAliTempParam.Size = new System.Drawing.Size(408, 21);
            this.txtAliTempParam.TabIndex = 11;
            // 
            // ConfigForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.btnCancel;
            this.ClientSize = new System.Drawing.Size(494, 395);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.btnSave);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "ConfigForm";
            this.Text = "应用配置";
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.ResumeLayout(false);

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
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.TextBox txtAliSmsUrl;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox txtAliSign;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.TextBox txtAliSk;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox txtAliAk;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox txtAliTempParam;
        private System.Windows.Forms.Label lab1;
        private System.Windows.Forms.TextBox txtAliTemplateCode;
        private System.Windows.Forms.Label label6;
    }
}