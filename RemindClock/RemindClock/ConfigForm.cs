using System;
using System.Windows.Forms;
using RemindClock.Services;
using RemindClock.Utils.Sms;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock
{
    public partial class ConfigForm : Form
    {
        private NotesService notesService = NotesService.Default;
        private Version version;

        public ConfigForm()
        {
            InitializeComponent();
        }

        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);

            this.Icon = RemindClock.Properties.Resources.clock;

            version = notesService.GetVersion();
            this.chkEnableSync.Checked = version.SyncEnable;
            this.txtSyncUrl.Text = version.SyncUrl;
            this.txtSyncUser.Text = version.SyncUser;
            this.txtSyncToken.Text = version.SyncToken;

            if (version.SmsConfig == null)
            {
                version.SmsConfig = new AliSmsConfig();
            }

            if (!string.IsNullOrEmpty(version.SmsConfig.ApiUrl))
            {
                txtAliSmsUrl.Text = version.SmsConfig.ApiUrl;
            }

            txtAliAk.Text = version.SmsConfig.AK;
            txtAliSk.Text = version.SmsConfig.SK;
            txtAliSign.Text = version.SmsConfig.SignName;
            txtAliTemplateCode.Text = version.SmsConfig.TemplateCode;
            txtAliTempParam.Text = version.SmsConfig.TemplateParamJson;
        }

        private void BtnCancel_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void BtnSave_Click(object sender, EventArgs e)
        {
            version = notesService.GetVersion();
            version.SyncEnable = this.chkEnableSync.Checked;
            version.SyncUrl = this.txtSyncUrl.Text.Trim();
            version.SyncUser = this.txtSyncUser.Text.Trim();
            version.SyncToken = this.txtSyncToken.Text.Trim();
            if (version.SyncEnable)
            {
                if (version.SyncUrl.Length <= 0 || version.SyncUser.Length <= 0 || version.SyncToken.Length <= 0)
                {
                    MessageBox.Show("URL、账号、密钥不能为空");
                    return;
                }
            }

            if (version.SmsConfig == null)
            {
                version.SmsConfig = new AliSmsConfig();
            }

            version.SmsConfig.ApiUrl = txtAliSmsUrl.Text.Trim();
            version.SmsConfig.AK = txtAliAk.Text.Trim();
            version.SmsConfig.SK = txtAliSk.Text.Trim();
            version.SmsConfig.SignName = txtAliSign.Text.Trim();
            version.SmsConfig.TemplateCode = txtAliTemplateCode.Text.Trim();
            version.SmsConfig.TemplateParamJson = txtAliTempParam.Text.Trim();

            notesService.SaveVersion(version);
            this.Close();
        }
    }
}