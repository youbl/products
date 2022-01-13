using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace MstscIps.Utils
{
    internal static class StrHelper
    {
        public static bool IsIp(string str)
        {
            if (str == null)
            {
                return false;
            }

            return Regex.IsMatch(str, @"^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$");
        }
    }
}