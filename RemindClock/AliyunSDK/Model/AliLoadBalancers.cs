using System.Collections.Generic;

namespace AliyunSDK.Model
{
    public class AliLoadBalancers
    {
        public string RequestId { get; set; }

        public int? PageNumber { get; set; }

        public int? PageSize { get; set; }

        public int? TotalCount { get; set; }

        public AliLoadBalancer LoadBalancers { get; set; }

        public class AliLoadBalancer
        {
            public List<LoadBalancerItem> LoadBalancer { get; set; }
        }
        public class LoadBalancerItem
        {
            public string LoadBalancerId { get; set; }

            public string LoadBalancerName { get; set; }

            public string LoadBalancerStatus { get; set; }

            public string Address { get; set; }

            public string AddressType { get; set; }

            public string RegionId { get; set; }

            public string RegionIdAlias { get; set; }

            public string VSwitchId { get; set; }

            public string VpcId { get; set; }

            public string NetworkType { get; set; }

            public string MasterZoneId { get; set; }

            public string SlaveZoneId { get; set; }

            public string InternetChargeType { get; set; }

            public string CreateTime { get; set; }

            public long? CreateTimeStamp { get; set; }

            public string PayType { get; set; }

            public string ResourceGroupId { get; set; }
        }
    }


}
