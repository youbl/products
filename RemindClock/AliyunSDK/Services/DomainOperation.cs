using System.Collections.Generic;
using AliyunSDK.Model;

namespace AliyunSDK.Services
{
    /// <summary>
    /// 阿里云的域名操作类
    /// </summary>
    public class DomainOperation : AliOperation
    {
        public DomainOperation(string accessKeyId, string accessKeySecret) : base(accessKeyId, accessKeySecret)
        {
        }

        /// <summary>
        /// 获取所有顶级域名.
        /// 官方参数参考：https://help.aliyun.com/document_detail/50158.html
        /// 使用的签名机制：https://help.aliyun.com/document_detail/42884.html
        /// </summary>
        public DomainTopList GetAllTopDomain()
        {
            var url = "http://domain.aliyuncs.com/";
            var version = "2016-05-11";

            var param = new Dictionary<string, string>();
            param["Action"] = "QueryDomainList";
            param["PageNum"] = "1";
            param["PageSize"] = "500";
            return AccessAli<DomainTopList>(url, version, param);
        }

        /// <summary>
        /// 返回指定顶级域名的所有子域名解析列表.
        /// 官方参数参考：https://help.aliyun.com/document_detail/29776.html
        /// 使用的签名机制：https://help.aliyun.com/document_detail/29747.html
        /// </summary>
        /// <param name="domain"></param>
        /// <returns></returns>
        public DomainRecordList GetAllSubDomain(string domain)
        {
            // 签名源串：GET&%2F&AccessKeyId%3Dtestid%26Action%3DDescribeDomainRecords%26DomainName%3Dexample.com%26Format%3DXML%26SignatureMethod%3DHMAC-SHA1%26SignatureNonce%3Df59ed6a9-83fc-473b-9cc6-99c95df3856e%26SignatureVersion%3D1.0%26Timestamp%3D2016-03-24T16%253A41%253A54Z%26Version%3D2015-01-09
            // 签名结果：uRpHwaSEt3J+6KQD//svCh/x+pI=
            if (string.IsNullOrWhiteSpace(domain))
                return null;
            var url = "http://alidns.aliyuncs.com/";
            var version = "2015-01-09";

            var param = new Dictionary<string, string>();
            param["Action"] = "DescribeDomainRecords";
            param["DomainName"] = domain;
            param["PageNumber"] = "1";
            param["PageSize"] = "500"; // 最大500
            //param["RRKeyWord"] = "www";
            //param["TypeKeyWord"] = "MX";
            //param["ValueKeyWord"] = "com";

            return AccessAli<DomainRecordList>(url, version, param);
        }
    }
}
