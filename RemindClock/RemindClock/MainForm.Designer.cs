namespace RemindClock
{
    partial class MainForm
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要修改
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.notifyIcon1 = new System.Windows.Forms.NotifyIcon(this.components);
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.打开提醒机ToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.开机启动ToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.退出ToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.btnOverwriteLocal = new System.Windows.Forms.Button();
            this.labSync = new System.Windows.Forms.Label();
            this.labSyncTitle = new System.Windows.Forms.Label();
            this.btnOverwriteServer = new System.Windows.Forms.Button();
            this.btnReload = new System.Windows.Forms.Button();
            this.btnNew = new System.Windows.Forms.Button();
            this.lvData = new System.Windows.Forms.ListView();
            this.columnHeader1 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.columnHeader2 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.columnHeader3 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.col4 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.columnHeader5 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.btnClose = new System.Windows.Forms.Button();
            this.btnSyncConfig = new System.Windows.Forms.Button();
            this.contextMenuStrip1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.SuspendLayout();
            // 
            // notifyIcon1
            // 
            this.notifyIcon1.ContextMenuStrip = this.contextMenuStrip1;
            this.notifyIcon1.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.notifyIcon1_MouseDoubleClick);
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.打开提醒机ToolStripMenuItem,
            this.开机启动ToolStripMenuItem,
            this.退出ToolStripMenuItem});
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(137, 70);
            // 
            // 打开提醒机ToolStripMenuItem
            // 
            this.打开提醒机ToolStripMenuItem.Name = "打开提醒机ToolStripMenuItem";
            this.打开提醒机ToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.打开提醒机ToolStripMenuItem.Text = "打开提醒机";
            this.打开提醒机ToolStripMenuItem.Click += new System.EventHandler(this.打开提醒机ToolStripMenuItem_Click);
            // 
            // 开机启动ToolStripMenuItem
            // 
            this.开机启动ToolStripMenuItem.Name = "开机启动ToolStripMenuItem";
            this.开机启动ToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.开机启动ToolStripMenuItem.Text = "开机启动";
            this.开机启动ToolStripMenuItem.Click += new System.EventHandler(this.开机启动ToolStripMenuItem_Click);
            // 
            // 退出ToolStripMenuItem
            // 
            this.退出ToolStripMenuItem.Name = "退出ToolStripMenuItem";
            this.退出ToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.退出ToolStripMenuItem.Text = "退出";
            this.退出ToolStripMenuItem.Click += new System.EventHandler(this.退出ToolStripMenuItem_Click);
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.FixedPanel = System.Windows.Forms.FixedPanel.Panel1;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Margin = new System.Windows.Forms.Padding(1);
            this.splitContainer1.Name = "splitContainer1";
            this.splitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.btnSyncConfig);
            this.splitContainer1.Panel1.Controls.Add(this.btnOverwriteLocal);
            this.splitContainer1.Panel1.Controls.Add(this.labSync);
            this.splitContainer1.Panel1.Controls.Add(this.labSyncTitle);
            this.splitContainer1.Panel1.Controls.Add(this.btnOverwriteServer);
            this.splitContainer1.Panel1.Controls.Add(this.btnReload);
            this.splitContainer1.Panel1.Controls.Add(this.btnNew);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.lvData);
            this.splitContainer1.Panel2.Controls.Add(this.btnClose);
            this.splitContainer1.Size = new System.Drawing.Size(841, 450);
            this.splitContainer1.SplitterDistance = 52;
            this.splitContainer1.SplitterWidth = 1;
            this.splitContainer1.TabIndex = 1;
            this.splitContainer1.TabStop = false;
            // 
            // btnOverwriteLocal
            // 
            this.btnOverwriteLocal.Location = new System.Drawing.Point(722, 29);
            this.btnOverwriteLocal.Name = "btnOverwriteLocal";
            this.btnOverwriteLocal.Size = new System.Drawing.Size(116, 23);
            this.btnOverwriteLocal.TabIndex = 5;
            this.btnOverwriteLocal.Text = "强制覆盖本地";
            this.btnOverwriteLocal.UseVisualStyleBackColor = true;
            this.btnOverwriteLocal.Click += new System.EventHandler(this.BtnOverwriteLocal_Click);
            // 
            // labSync
            // 
            this.labSync.AutoSize = true;
            this.labSync.Location = new System.Drawing.Point(591, 30);
            this.labSync.Name = "labSync";
            this.labSync.Size = new System.Drawing.Size(119, 12);
            this.labSync.TabIndex = 0;
            this.labSync.Text = "2021-06-10 09:10:11";
            // 
            // labSyncTitle
            // 
            this.labSyncTitle.AutoSize = true;
            this.labSyncTitle.Location = new System.Drawing.Point(537, 30);
            this.labSyncTitle.Name = "labSyncTitle";
            this.labSyncTitle.Size = new System.Drawing.Size(59, 12);
            this.labSyncTitle.TabIndex = 0;
            this.labSyncTitle.Text = "前次同步:";
            // 
            // btnOverwriteServer
            // 
            this.btnOverwriteServer.Location = new System.Drawing.Point(722, 2);
            this.btnOverwriteServer.Name = "btnOverwriteServer";
            this.btnOverwriteServer.Size = new System.Drawing.Size(116, 23);
            this.btnOverwriteServer.TabIndex = 4;
            this.btnOverwriteServer.Text = "强制覆盖远端";
            this.btnOverwriteServer.UseVisualStyleBackColor = true;
            this.btnOverwriteServer.Click += new System.EventHandler(this.BtnOverwriteServer_Click);
            // 
            // btnReload
            // 
            this.btnReload.Location = new System.Drawing.Point(111, 12);
            this.btnReload.Name = "btnReload";
            this.btnReload.Size = new System.Drawing.Size(93, 23);
            this.btnReload.TabIndex = 2;
            this.btnReload.Text = "刷新";
            this.btnReload.UseVisualStyleBackColor = true;
            this.btnReload.Click += new System.EventHandler(this.btnReload_Click);
            // 
            // btnNew
            // 
            this.btnNew.Location = new System.Drawing.Point(12, 12);
            this.btnNew.Name = "btnNew";
            this.btnNew.Size = new System.Drawing.Size(93, 23);
            this.btnNew.TabIndex = 1;
            this.btnNew.Text = "新增提醒...";
            this.btnNew.UseVisualStyleBackColor = true;
            this.btnNew.Click += new System.EventHandler(this.BtnNew_Click);
            // 
            // lvData
            // 
            this.lvData.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1,
            this.columnHeader2,
            this.columnHeader3,
            this.col4,
            this.columnHeader5});
            this.lvData.Dock = System.Windows.Forms.DockStyle.Fill;
            this.lvData.FullRowSelect = true;
            this.lvData.HideSelection = false;
            this.lvData.Location = new System.Drawing.Point(0, 0);
            this.lvData.Name = "lvData";
            this.lvData.Size = new System.Drawing.Size(841, 397);
            this.lvData.TabIndex = 0;
            this.lvData.TabStop = false;
            this.lvData.UseCompatibleStateImageBehavior = false;
            this.lvData.View = System.Windows.Forms.View.Details;
            this.lvData.DoubleClick += new System.EventHandler(this.lvData_DoubleClick);
            this.lvData.MouseClick += new System.Windows.Forms.MouseEventHandler(this.lvData_MouseClick);
            // 
            // columnHeader1
            // 
            this.columnHeader1.Text = "ID";
            this.columnHeader1.Width = 40;
            // 
            // columnHeader2
            // 
            this.columnHeader2.Text = "标题";
            this.columnHeader2.Width = 300;
            // 
            // columnHeader3
            // 
            this.columnHeader3.Text = "提醒";
            this.columnHeader3.Width = 400;
            // 
            // col4
            // 
            this.col4.Text = "";
            this.col4.Width = 45;
            // 
            // columnHeader5
            // 
            this.columnHeader5.Text = "";
            this.columnHeader5.Width = 45;
            // 
            // btnClose
            // 
            this.btnClose.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.btnClose.Location = new System.Drawing.Point(727, 43);
            this.btnClose.Name = "btnClose";
            this.btnClose.Size = new System.Drawing.Size(75, 23);
            this.btnClose.TabIndex = 0;
            this.btnClose.TabStop = false;
            this.btnClose.Text = "隐藏的关闭按钮";
            this.btnClose.UseVisualStyleBackColor = true;
            this.btnClose.Click += new System.EventHandler(this.btnClose_Click);
            // 
            // btnSyncConfig
            // 
            this.btnSyncConfig.Location = new System.Drawing.Point(600, 2);
            this.btnSyncConfig.Name = "btnSyncConfig";
            this.btnSyncConfig.Size = new System.Drawing.Size(116, 23);
            this.btnSyncConfig.TabIndex = 3;
            this.btnSyncConfig.Text = "同步设置...";
            this.btnSyncConfig.UseVisualStyleBackColor = true;
            this.btnSyncConfig.Click += new System.EventHandler(this.BtnSyncConfig_Click);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.btnClose;
            this.ClientSize = new System.Drawing.Size(841, 450);
            this.Controls.Add(this.splitContainer1);
            this.Name = "MainForm";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
            this.SizeChanged += new System.EventHandler(this.MainForm_SizeChanged);
            this.contextMenuStrip1.ResumeLayout(false);
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel1.PerformLayout();
            this.splitContainer1.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.NotifyIcon notifyIcon1;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.ToolStripMenuItem 打开提醒机ToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 退出ToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 开机启动ToolStripMenuItem;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.Button btnNew;
        private System.Windows.Forms.ListView lvData;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.ColumnHeader columnHeader2;
        private System.Windows.Forms.ColumnHeader columnHeader3;
        private System.Windows.Forms.ColumnHeader col4;
        private System.Windows.Forms.ColumnHeader columnHeader5;
        private System.Windows.Forms.Button btnReload;
        private System.Windows.Forms.Button btnOverwriteLocal;
        private System.Windows.Forms.Button btnOverwriteServer;
        private System.Windows.Forms.Button btnClose;
        private System.Windows.Forms.Label labSync;
        private System.Windows.Forms.Label labSyncTitle;
        private System.Windows.Forms.Button btnSyncConfig;
    }
}

