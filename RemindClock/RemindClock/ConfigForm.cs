using System;
using System.Windows.Forms;
using RemindClock.Services;
using Version = RemindClock.Repository.Model.Version;

namespace RemindClock
{
    public partial class ConfigForm : Form
    {
        private NotesService notesService = new NotesService();
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

            notesService.SaveVersion(version);
        }
    }
}