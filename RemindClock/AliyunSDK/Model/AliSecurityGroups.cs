using System;
using System.Collections.Generic;

namespace AliyunSDK.Model
{
    public class AliSecurityGroups
    {
        // public string RequestId { get; set; }
        public int TotalCount { get; set; }
        public int PageNumber { get; set; }
        public int PageSize { get; set; }
        public string RegionId { get; set; }

        public AliSecurityGroupArr SecurityGroups { get; set; }

        public class AliSecurityGroupArr
        {
            public List<AliSecurityGroup> SecurityGroup { get; set; }

        }
    }

    public class AliSecurityGroup
    {
        #region 获取安全组列表才有的字段

        public DateTime CreationTime { get; set; }

        public string ResourceGroupId { get; set; }
        // public string Tags { get; set; }

        #endregion

        #region 获取安全组详情才有的字段

        // public string RequestId { get; set; } // 没用，避免反复提交
        public string RegionId { get; set; }
        public string InnerAccessPolicy { get; set; }
        public AliPermissionArr Permissions { get; set; }

        public class AliPermissionArr
        {
            public List<AliPermission> Permission { get; set; }
        }

        public class AliPermission
        {
            public string SourceCidrIp { get; set; }
            public string DestCidrIp { get; set; }
            public string Description { get; set; }
            public string NicType { get; set; }
            public string DestGroupName { get; set; }
            public string PortRange { get; set; }
            public string DestGroupId { get; set; }
            public string Direction { get; set; }
            public int Priority { get; set; }
            public string IpProtocol { get; set; }
            public string SourcePortRange { get; set; }
            public string SourceGroupOwnerAccount { get; set; }
            public string Policy { get; set; }
            public DateTime CreateTime { get; set; }
            public string SourceGroupId { get; set; }
            public string DestGroupOwnerAccount { get; set; }
            public string SourceGroupName { get; set; }
        }

        #endregion

        public string SecurityGroupId { get; set; }
        public string SecurityGroupName { get; set; }
        public string Description { get; set; }
        public string VpcId { get; set; }

    }
}