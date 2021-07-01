using System.Text.RegularExpressions;

namespace RemindClock.Utils
{
    public static class StrHelper
    {
        private static Regex regMobile = new Regex(@"^1\d{10}$", RegexOptions.Compiled);

        /// <summary>
        /// 是否11位手机号
        /// </summary>
        /// <param name="phone"></param>
        /// <returns></returns>
        public static bool IsMobile(string phone)
        {
            return regMobile.IsMatch(phone);
        }
    }
}