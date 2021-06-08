using RemindClock.Repository.Model;

namespace RemindClock.Services.NoteOperation
{
    public interface INoteAlert
    {
        void Alert(Notes note);
    }
}