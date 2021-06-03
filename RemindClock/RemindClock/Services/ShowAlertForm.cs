namespace RemindClock.Services
{
    class ShowAlertForm
    {
        private AlertForm alertForm;
        private readonly object lockObj = new object();

        public void Show(int noteId)
        {
            lock (lockObj)
            {
                if (alertForm == null)
                {
                    alertForm = new AlertForm();
                }

                if (!alertForm.Visible)
                {
                    alertForm.NoteId = noteId;
                    alertForm.ShowMe();
                }
            }
        }
    }
}