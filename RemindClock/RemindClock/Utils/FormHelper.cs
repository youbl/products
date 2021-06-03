using System;
using System.Windows.Forms;

namespace RemindClock.Utils
{
    public static class FormHelper
    {
        public static void Invoke(Control control, Action action)
        {
            if (control.InvokeRequired)
            {
                control.Invoke(action);
            }
            else
            {
                action();
            }
        }
    }
}