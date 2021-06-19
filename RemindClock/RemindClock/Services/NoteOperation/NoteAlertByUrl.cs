using System.Web;
using Beinet.Core.Util;
using NLog;
using RemindClock.Repository.Model;

namespace RemindClock.Services.NoteOperation
{
    /// <summary>
    /// 通知指定URL的功能
    /// </summary>
    public class NoteAlertByUrl : INoteAlert
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();

        public void Alert(Notes note)
        {
            if (string.IsNullOrEmpty(note.NoticeUrl))
            {
                return;
            }

            var url = note.NoticeUrl.Trim();
            var idx = url.IndexOf('?');
            url += idx > 0 ? '&' : '?';
            url += "title=" + HttpUtility.UrlEncode(note.Title);
            var ret = WebHelper.GetPage(url);
            logger.Info("回调URL: " + url + " 结果:" + ret);
        }
    }
}