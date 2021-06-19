using RemindClock.Repository.Model;

namespace RemindClock.Services.NoteOperation
{
    public class NoteAlertByPhone : INoteAlert
    {
        public void Alert(Notes note)
        {
            if (string.IsNullOrEmpty(note.Phone))
            {
                return;
            }

            // 短信功能尚未实现
        }
    }
}