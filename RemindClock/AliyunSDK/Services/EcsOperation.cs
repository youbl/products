using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AliyunSDK.Model;

namespace AliyunSDK.Services
{
    /// <summary>
    /// 阿里云的ECS服务器和安全组、SLB负载均衡操作类
    /// </summary>
    public class EcsOperation : AliOperation
    {
        public EcsOperation(string accessKeyId, string accessKeySecret) : base(accessKeyId, accessKeySecret)
        {
        }

        /// <summary>
        /// 获取阿里的所有区域信息.
        /// 官方文档：https://help.aliyun.com/document_detail/27584.html
        /// </summary>
        /// <returns></returns>
        public AliRegions GetAllRegion()
        {
            var url = "https://slb.aliyuncs.com/";
            var version = "2014-05-15";

            var param = new Dictionary<string, string>();
            param["Action"] = "DescribeRegions";
            return AccessAli<AliRegions>(url, version, param);
        }

        /// <summary>
        /// 获取所有ECS域名.
        /// 官方参数参考：https://help.aliyun.com/document_detail/25506.html
        /// </summary>
        public EcsInstances GetAllEcs(string region)
        {
            var url = "https://ecs.aliyuncs.com/";
            var version = "2014-05-26";

            var param = new Dictionary<string, string>();
            param["Action"] = "DescribeInstances";
            param["PageSize"] = "100"; // 最大只能100
            param["RegionId"] = region;

            return AccessAli<EcsInstances>(url, version, param);
        }

        /// <summary>
        /// 获取指定区域所有SLB信息。
        /// 官方文档：https://help.aliyun.com/document_detail/27582.html
        /// </summary>
        /// <param name="region"></param>
        public AliLoadBalancers GetAllSlb(string region)
        {
            var version = "2014-05-15";

            var param = new Dictionary<string, string>();
            param["Action"] = "DescribeLoadBalancers";
            param["RegionId"] = region;

            // slb要分2次请求，先得到域名，再获取slb信息
            return AccessAli<AliLoadBalancers>(region, "slb", version, param);
        }

        /// <summary>
        /// 获取安全组列表。
        /// 官方文档：https://help.aliyun.com/document_detail/25556.html
        /// </summary>
        /// <param name="region"></param>
        public AliSecurityGroups GetSecurityGroups(string region)
        {
            // 不需要先Get Endpoint也可以获取到德国安全组
            var url = $"http://ecs.aliyuncs.com/"; 
            var version = "2014-05-26";

            var param = new Dictionary<string, string>();
            param["Action"] = "DescribeSecurityGroups";
            param["RegionId"] = region;
            param["PageSize"] = "50"; // 最大值50
            return AccessAli<AliSecurityGroups>(url, version, param);
        }

        /// <summary>
        /// 获取安全组详情。
        /// 官方文档：https://help.aliyun.com/document_detail/25555.html
        /// </summary>
        /// <param name="region"></param>
        /// <param name="groupId"></param>
        public AliSecurityGroup GetSecurityGroup(string region, string groupId)
        {
            var url = $"http://ecs.aliyuncs.com/";
            var version = "2014-05-26";

            var param = new Dictionary<string, string>();
            param["Action"] = "DescribeSecurityGroupAttribute";
            param["RegionId"] = region;
            param["SecurityGroupId"] = groupId;

            return AccessAli<AliSecurityGroup>(url, version, param);
        }
    }

}
