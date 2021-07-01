using RemindClock.Repository.Model;
using RemindClock.Services.SmsChannel;
using RemindClock.Utils;

namespace RemindClock.Services.NoteOperation
{
    public class NoteAlertByPhone : INoteAlert
    {
        public void Alert(Notes note)
        {
            if (string.IsNullOrEmpty(note.Phone) || !StrHelper.IsMobile(note.Phone))
            {
                return;
            }

            var sender = GetSender();
            if (sender == null)
            {
                return;
            }

            sender.Send(note.Phone, note.Title);
        }

        SmsSend GetSender()
        {
            var version = NotesService.Default.GetVersion();
            if (version.SmsConfig == null
                || string.IsNullOrEmpty(version.SmsConfig.AK)
                || string.IsNullOrEmpty(version.SmsConfig.SK)
                || string.IsNullOrEmpty(version.SmsConfig.ApiUrl)
                || string.IsNullOrEmpty(version.SmsConfig.SignName)
                || string.IsNullOrEmpty(version.SmsConfig.TemplateCode))
            {
                return null;
            }

            return new AliSmsSend(version.SmsConfig);
        }
    }
}