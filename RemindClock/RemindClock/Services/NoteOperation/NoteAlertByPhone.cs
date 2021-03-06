﻿using RemindClock.Repository.Model;
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
            if (version.SmsConfig == null || version.SmsConfig.IsInvalid)
            {
                return null;
            }

            return new AliSmsSend(version.SmsConfig);
        }
    }
}