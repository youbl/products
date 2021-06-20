using System;
using System.Collections.Generic;
using System.IO;
//using Aliyun.OSS;
using AliyunSDK.Model;

namespace AliyunSDK.Services
{
    /// <summary>
    /// 阿里云OSS操作类
    /// </summary>
    public class OssOperation : AliOperation
    {
        public Uri EndPoint { get; protected set; }

        public OssOperation(string endpoint, string accessKeyId, string accessKeySecret) : base(accessKeyId,
            accessKeySecret)
        {
            if (string.IsNullOrWhiteSpace(endpoint))
                throw new ArgumentException("endpoint can't be empty.", nameof(endpoint));

            endpoint = endpoint.Trim();
            string canonicalizedEndpoint = endpoint.ToLower();
            if (canonicalizedEndpoint.StartsWith("http://") ||
                canonicalizedEndpoint.StartsWith("https://"))
            {
                EndPoint = new Uri(endpoint.Trim());
            }
            else
            {
                EndPoint = new Uri("http://" + endpoint);
            }
        }

        // https://help.aliyun.com/document_detail/31978.html?spm=a2c4g.11186623.6.1122.1f62734c638Zud
        public object Upload(string bucketName, string key, Stream content)
        {
            /*
request.Headers:
Date: Wed, 27 Mar 2019 07:01:16 GMT
Content-Type: image/png
Authorization: OSS xxxxxxxxxxxxxxx
User-Agent: aliyun-sdk-dotnet/2.9.1.0(windows 10.0/10.0.17763.0/x86;4.0.30319.42000)
Host: xxxtest-xj.oss-cn-shenzhen.aliyuncs.com
Transfer-Encoding: chunked
Expect: 100-continue
Connection: Keep-Alive

response.Headers:
Connection: keep-alive
x-oss-request-id: 5C9B1FBFA8BCB76D80478F99
x-oss-hash-crc64ecma: 12769383852839880692
Content-MD5: H5YCuKlq/McZ1gdUNnPwSg==
x-oss-server-time: 51
Content-Length: 0
Date: Wed, 27 Mar 2019 07:01:20 GMT
ETag: 1F9602B8A96AFCC719D607543673F04A
Server: AliyunOSS

response:
{
    ETag:1F9602B8A96AFCC719D607543673F04A,
    ResponseStream:null,
    HttpStatusCode:200,
    RequestId:5C9B1FBFA8BCB76D80478F99,
    ContentLength:0,
    ResponseMetadata:
    {
        x-oss-server-time:51,
        Date:Wed, 27 Mar 2019 07:01:20 GMT,
        ETag:1F9602B8A96AFCC719D607543673F04A,
        x-oss-hash-crc64ecma:12769383852839880692,
        Content-MD5:H5YCuKlq/McZ1gdUNnPwSg==
    }
}
             */
            // var client = new OssClient(EndPoint, AccessKeyId, AccessKeySecret);
            // var ret = client.PutObject(bucketName, key, content);
            // return ret;
            //
            //
            //
            // var verb = "PUT";
            // var contentMD5 = "";
            // var contentType = "";
            // var gmtDate = DateTime.Now.ToString("r");
            /*
Signature = base64(hmac-sha1(AccessKeySecret,
            verb + "\n" + contentMD5 + "\n" + contentType + "\n" + gmtDate + "\n" + CanonicalizedOSSHeaders + CanonicalizedResource))
             *
             */
            // var Authorization = "OSS " + AccessKeyId + ":" + Signature;
            return null;
        }
    }
}
