using RemindClock.Repository.Model;

namespace RemindClock.Services.NoteOperation
{
    public class NoteAlertByForm : INoteAlert
    {
        public void Alert(Notes note)
        {
            // 弹窗
            AlertForm.Show(note.Title, note.Content);
        }
    }
}