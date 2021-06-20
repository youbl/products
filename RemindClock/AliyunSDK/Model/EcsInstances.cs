using System.Collections.Generic;

namespace AliyunSDK.Model
{
    public class EcsInstances
    {
        public string RequestId { get; set; }

        public int? TotalCount { get; set; }

        public int? PageNumber { get; set; }

        public int? PageSize { get; set; }

        public EcsInstanceData Instances { get; set; }

        public class EcsInstanceData
        {
            public List<EcsInstance> Instance { get; set; }
        }
        public class EcsInstance
        {
            public string InstanceId { get; set; }

            public string InstanceName { get; set; }

            public string Description { get; set; }

            public string ImageId { get; set; }

            public string OSName { get; set; }

            public string OSType { get; set; }

            public string RegionId { get; set; }

            public string ZoneId { get; set; }

            public string ClusterId { get; set; }

            public string InstanceType { get; set; }

            public int? Cpu { get; set; }

            public int? Memory { get; set; }

            public string HostName { get; set; }

            public string Status { get; set; }

            public string SerialNumber { get; set; }

            public string InternetChargeType { get; set; }

            public int? InternetMaxBandwidthIn { get; set; }

            public int? InternetMaxBandwidthOut { get; set; }

            public string VlanId { get; set; }

            public string CreationTime { get; set; }

            public string StartTime { get; set; }

            public string InstanceNetworkType { get; set; }

            public string InstanceChargeType { get; set; }

            public string SaleCycle { get; set; }

            public string ExpiredTime { get; set; }

            public string AutoReleaseTime { get; set; }

            public bool? IoOptimized { get; set; }

            public bool? DeviceAvailable { get; set; }

            public string InstanceTypeFamily { get; set; }

            public long? LocalStorageCapacity { get; set; }

            public int? LocalStorageAmount { get; set; }

            public int? GPUAmount { get; set; }

            public string GPUSpec { get; set; }

            public string SpotStrategy { get; set; }

            public float? SpotPriceLimit { get; set; }

            public string ResourceGroupId { get; set; }

            public string KeyPairName { get; set; }

            public bool? Recyclable { get; set; }

            public string HpcClusterId { get; set; }

            public string StoppedMode { get; set; }

            public NetworkInterfaceArr NetworkInterfaces
            {
                get;
                set;
            }
            public class NetworkInterfaceArr
            {
                public List<EcsNetworkInterface> NetworkInterface { get; set; }
            }

            public EcsLockReasonArr OperationLocks{ get; set; }

            public class EcsLockReasonArr
            {
                public List<EcsLockReason> LockReason { get; set; }
            }

            public List<EcsTag> Tags { get; set; }

            public SecurityGroupIdArr SecurityGroupIds { get; set; }
            public class SecurityGroupIdArr
            {
                public List<string> SecurityGroupId { get; set; }
            }
            public IpAddressArr PublicIpAddress { get; set; }

            public IpAddressArr InnerIpAddress { get; set; }
            public class IpAddressArr
            {
                public List<string> IpAddress { get; set; }
            }

            public List<string> RdmaIpAddress { get; set; }

            public EcsVpcAttributes VpcAttributes
            {
                get;
                set;
            }

            public EcsEipAddress EipAddress
            {
                get;
                set;
            }

            public EcsDedicatedHostAttribute DedicatedHostAttribute
            {
                get;
                set;
            }

            public class EcsNetworkInterface
            {
                public string NetworkInterfaceId { get; set; }

                public string MacAddress { get; set; }

                public string PrimaryIpAddress { get; set; }
            }

            public class EcsLockReason
            {
                public string LockReason { get; set; }

                public string LockMsg { get; set; }
            }

            public class EcsTag
            {
                public string TagKey { get; set; }

                public string TagValue { get; set; }
            }

            public class EcsVpcAttributes
            {
                public string VpcId { get; set; }

                public string VSwitchId { get; set; }

                public string NatIpAddress { get; set; }

                public IpAddressArr PrivateIpAddress { get; set; }

            }

            public class EcsEipAddress
            {
                public string AllocationId { get; set; }

                public string IpAddress { get; set; }

                public int? Bandwidth { get; set; }

                public string InternetChargeType { get; set; }

                public bool? IsSupportUnassociate { get; set; }
            }

            public class EcsDedicatedHostAttribute
            {
                public string DedicatedHostId { get; set; }

                public string DedicatedHostName { get; set; }
            }
        }
    }


}
