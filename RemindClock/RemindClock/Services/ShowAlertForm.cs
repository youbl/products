using System.Collections.Generic;
using RemindClock.Utils;

namespace RemindClock.Services
{
    class ShowAlertForm
    {
        public void Show(int noteId)
        {
            var msg = "我是一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十";
            FormHelper.Invoke(MainForm.Default, () => { new AlertForm("哈哈", msg).Show(); });
        }
    }
}