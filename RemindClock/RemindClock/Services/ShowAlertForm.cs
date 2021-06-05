using System.Collections.Generic;
using RemindClock.Utils;

namespace RemindClock.Services
{
    class ShowAlertForm
    {
        public void Show(int noteId)
        {
            FormHelper.Invoke(MainForm.Default, () => { new AlertForm(noteId).Show(); });
        }
    }
}