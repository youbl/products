using System.Collections.Generic;

namespace AliyunSDK.Model
{
    /// <summary>
    /// 子域名列表类
    /// </summary>
    public class DomainRecordList
    {
        public int PageNumber { get; set; }
        public int TotalCount { get; set; }
        public int PageSize { get; set; }
        public string RequestId { get; set; }
        public DomainRecords DomainRecords { get; set; }
    }

    public class DomainRecords
    {
        public List<Record> Record { get; set; }
    }

    public class Record
    {
        public string RR { get; set; }
        public string Status { get; set; }
        public string Value { get; set; }
        public int Weight { get; set; }
        public string RecordId { get; set; }
        public string Type { get; set; }
        public string DomainName { get; set; }
        public bool Locked { get; set; }
        public string Line { get; set; }
        public int TTL { get; set; }

    }
}
