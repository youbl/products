namespace LogAnalyse.Utils
{
    public static class StrHelper
    {
        public static string ProcessSqlVal(string sqlVal)
        {
            if (string.IsNullOrEmpty(sqlVal))
                return sqlVal;
            return sqlVal.Replace("'", "''").Replace("\\", "\\\\");
        }
    }
}