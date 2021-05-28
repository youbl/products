using System.Windows.Forms;
using RemindClock.Properties;

namespace RemindClock
{
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
            // 设置窗体icon
            this.Icon = Resources.clock;
        }
    }
}
