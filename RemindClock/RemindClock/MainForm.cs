using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using Beinet.Feign;
using RemindClock.FeignService;
using RemindClock.Repository.Model;
using RemindClock.Services;
using RemindClock.Utils;

namespace RemindClock
{
    public partial class MainForm : Form
    {
        public const string APPNAME = "贝可提醒机";
        private static MainForm _default;

        private const int COL_ID = 0; // ID在第几列（ListView）
        private const int COL_TITLE = 1;
        private const int COL_NOTE = 2;
        private const int COL_EDIT = 3;
        private const int COL_DEL = 4;
        private const int COL_COUNT = 5; // ListView的总列数

        // 是关闭窗体还是最小化
        private bool realClose = false;

        private NotesService notesService = new NotesService();
        private SyncFeign syncFeign = ProxyLoader.GetProxy<SyncFeign>();

        private Dictionary<int, Notes> notesList;

        public static MainForm Default
        {
            get
            {
                if (_default == null)
                {
                    _default = new MainForm();
                }

                return _default;
            }
        }

        public MainForm()
        {
            InitializeComponent();

            this.Icon = RemindClock.Properties.Resources.clock;
            this.notifyIcon1.Icon = RemindClock.Properties.Resources.clock;
            this.Text = APPNAME;
            this.notifyIcon1.Text = APPNAME;
        }

        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);
            CheckAutoStart();

            LoadNotes();

            // 下面3行，用于设置ListView的行高
            var il = new ImageList();
            il.ImageSize = new Size(1, 25);
            lvData.SmallImageList = il;
        }

        /// <summary>
        /// 最小化到托盘的双击事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void notifyIcon1_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            this.ShowInTaskbar = true; // 任务栏显示
            this.Visible = true; // 设置窗体可见
            this.WindowState = FormWindowState.Normal; // 设置窗体为默认大小
            this.Activate(); // 激活当前窗口，避免不可见
            notifyIcon1.Visible = false;
        }

        /// <summary>
        /// 窗体大小变更事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void MainForm_SizeChanged(object sender, System.EventArgs e)
        {
            if (this.WindowState == FormWindowState.Minimized)
            {
                // 进入最小化状态时, 任务栏隐藏
                this.ShowInTaskbar = false;
                // 托盘可见
                notifyIcon1.Visible = true;
                // 隐藏窗体
                this.Visible = false;
            }
        }

        private void 打开提醒机ToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            notifyIcon1_MouseDoubleClick(sender, null);
        }

        private void 退出ToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            this.realClose = true;
            //this.Dispose(true);
            this.Close();
        }

        private void 开机启动ToolStripMenuItem_Click(object sender, System.EventArgs e)
        {
            var checkedStatus = !开机启动ToolStripMenuItem.Checked;
            try
            {
                AutoStartHelper.AutoStart(this.Text, checkedStatus);
                开机启动ToolStripMenuItem.Checked = checkedStatus;
            }
            catch (Exception exp)
            {
                MessageBox.Show("请先获取管理员权限:" + exp.Message);
            }
        }

        /// <summary>
        /// 已设置开机启动时，自动最小化
        /// </summary>
        private void CheckAutoStart()
        {
            if (AutoStartHelper.IsAutoStart(this.Text))
            {
                开机启动ToolStripMenuItem.Checked = true;
                this.WindowState = FormWindowState.Minimized;
            }
        }

        private void BtnNew_Click(object sender, EventArgs e)
        {
            ShowEditForm(new Notes());
        }

        // 不让关闭，改成最小化
        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (this.realClose)
            {
                return; // 菜单退出，要真实的退出
            }

            // 点x，最小化，而不是退出
            e.Cancel = true;
            this.WindowState = FormWindowState.Minimized;
        }

        /// <summary>
        /// 重新加载提醒。
        /// 注：不用管计划任务，它都是实时拉取数据的
        /// </summary>
        public void LoadNotes()
        {
            var colorArr = new Color[]
            {
                Color.FromArgb(42, 0xa0, 0xe7, 0xae),
                Color.FromArgb(42, 0xdb, 0xd5, 0xf7), //DBD5F7
            };
            var colorDisabled = Color.Gray;
            lvData.Items.Clear();

            var idx = 0;
            notesList = notesService.FindAll().ToDictionary(item => item.Id, item => item);
            foreach (var note in notesList.Values.OrderByDescending(item => item.Id))
            {
                var dataArr = new string[COL_COUNT];
                dataArr[COL_ID] = note.Id.ToString();
                dataArr[COL_TITLE] = note.Title;
                dataArr[COL_NOTE] = note.GetStrDetail();
                dataArr[COL_EDIT] = "编辑";
                dataArr[COL_DEL] = "删除";
                var row = new ListViewItem(dataArr, 0);
                lvData.Items.Add(row);

                if (note.Enable)
                {
                    lvData.Items[idx].BackColor = colorArr[idx % colorArr.Length];
                }
                else
                {
                    lvData.Items[idx].BackColor = colorDisabled;
                }

                idx++;
            }

            var version = notesService.GetVersion();
            if (version.ServerVersion <= 0)
            {
                labSync.Text = "未同步";
            }
            else
            {
                labSync.Text = version.LastSyncTime.ToString("yyyy-MM-dd HH:mm:ss");
            }
        }

        /// <summary>
        /// 双击弹出编辑框
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void lvData_DoubleClick(object sender, EventArgs e)
        {
            var notes = GetSelectedNote();
            if (notes != null)
            {
                ShowEditForm(notes);
            }
        }

        private void ShowEditForm(Notes notes)
        {
            new NoteForm(notes).ShowDialog(this);
            LoadNotes();
        }

        private Notes GetSelectedNote()
        {
            if (lvData.SelectedItems.Count <= 0)
                return null;
            var selectedRow = lvData.SelectedItems[0];
            if (selectedRow.SubItems.Count <= 0 || !int.TryParse(selectedRow.SubItems[0].Text, out var id))
                return null;
            if (notesList.TryGetValue(id, out var notes))
                return notes;
            return null;
        }

        /// <summary>
        /// 鼠标单击事件，判断删除
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void lvData_MouseClick(object sender, MouseEventArgs e)
        {
            var notes = GetSelectedNote();
            if (notes == null)
                return;

            var item = lvData.SelectedItems[0];
            int intCol = item.SubItems.IndexOf(item.GetSubItemAt(e.X, e.Y)); //列索引
            switch (intCol)
            {
                case COL_EDIT:
                    ShowEditForm(notes);
                    break;
                case COL_DEL:
                    DelNote(notes);
                    break;
            }
        }

        private void DelNote(Notes notes)
        {
            var msg = "真的要删除:" + notes.Title + "?";
            var result = MessageBox.Show(msg, "删除确认", MessageBoxButtons.YesNo, MessageBoxIcon.Warning,
                MessageBoxDefaultButton.Button2);
            if (DialogResult.Yes != result)
            {
                return;
            }

            if (notesService.Del(notes))
            {
                LoadNotes();
            }
        }

        private void btnReload_Click(object sender, EventArgs e)
        {
            LoadNotes();
        }


        private void BtnOverwriteLocal_Click(object sender, EventArgs e)
        {
            var msg = "本地数据将被清空，以远端为准，确认要执行吗?";
            var result = MessageBox.Show(msg, "覆盖确认", MessageBoxButtons.YesNo, MessageBoxIcon.Warning,
                MessageBoxDefaultButton.Button2);
            if (DialogResult.Yes != result)
            {
                return;
            }

            // 本地强制设置为初始版本，这样job就会自动从线上同步
            notesService.SetVersion(0, 0);
        }

        private void BtnOverwriteServer_Click(object sender, EventArgs e)
        {
            var msg = "远端数据将被清空，以本地为准，确认要执行吗?";
            var result = MessageBox.Show(msg, "覆盖确认", MessageBoxButtons.YesNo, MessageBoxIcon.Warning,
                MessageBoxDefaultButton.Button2);
            if (DialogResult.Yes != result)
            {
                return;
            }

            // 本地强制设置为服务器版本+1，这样job就会自动同步到线上
            var serverVerNow = syncFeign.GetServerVersion(SyncService.SyncUser, SyncService.SyncToken);
            notesService.SetVersion(serverVerNow + 1, serverVerNow);
        }

        private void btnClose_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}