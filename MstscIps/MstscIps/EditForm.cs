using System;
using System.Windows.Forms;

namespace MstscIps
{
    public partial class EditForm : Form
    {
        public EditForm()
        {
            InitializeComponent();

            LoadConfig();
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            var ret = ConfigUtil.Default.SaveConfig(txtConfig.Text);
            if (ret.Length == 0)
            {
                MessageBox.Show("保存成功");
                LoadConfig();
            }
            else
            {
                MessageBox.Show(ret);
            }
        }

        private void LoadConfig()
        {
            txtConfig.Text = ConfigUtil.Default.ReadConfig();
        }
    }
}