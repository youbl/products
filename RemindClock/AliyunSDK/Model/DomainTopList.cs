using System.Collections.Generic;

namespace AliyunSDK.Model
{
    /// <summary>
    /// 顶级域名列表类
    /// </summary>
    public class DomainTopList
    {
        public string RequestId { get; set; }

        public int? TotalItemNum { get; set; }

        public int? CurrentPageNum { get; set; }

        public int? TotalPageNum { get; set; }

        public int? PageSize { get; set; }

        public bool? PrePage { get; set; }

        public bool? NextPage { get; set; }

        public DomainData Data { get; set; }

        public class DomainData
        {
            public List<QueryDomainList_Domain> Domain { get; set; }
        }
        public class QueryDomainList_Domain
        {
            public string DomainName { get; set; }

            public string SaleId { get; set; }

            public string DeadDate { get; set; }

            public string RegDate { get; set; }

            public string DomainAuditStatus { get; set; }

            public string DomainRegType { get; set; }

            public string GroupId { get; set; }

            public string DomainType { get; set; }

            public string DomainStatus { get; set; }

            public string DeadDateStatus { get; set; }

            public string ProductId { get; set; }

            public long? DeadDateLong { get; set; }

            public long? RegDateLong { get; set; }

            public string Remark { get; set; }

            public bool? Premium { get; set; }
        }
    }

}
