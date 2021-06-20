using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.Script.Serialization;

namespace AliyunSDK
{
    public class Utility
    {
        #region 阿里云公共参数处理相关方法

        /// <summary>
        /// 添加阿里云API需要的公共参数
        /// </summary>
        /// <param name="param"></param>
        /// <param name="version"></param>
        /// <param name="accessKeyId"></param>
        /// <param name="accessKeySecret"></param>
        public static string AddCommonPara(Dictionary<string, string> param, string version,
            string accessKeyId, string accessKeySecret)
        {
            if (param == null)
                return "";

            // 公共参数
            param["Format"] = "JSON";
            param["Version"] = version;
            param["AccessKeyId"] = accessKeyId;
            param["SignatureMethod"] = "HMAC-SHA1";
            param["Timestamp"] = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ssZ"); // 转成ISO8601标准
            param["SignatureVersion"] = "1.0";
            param["SignatureNonce"] = Guid.NewGuid().ToString("N");
            param["Signature"] = GetSign(param, accessKeySecret, out var signStr);
            return signStr;
        }


        /// <summary>
        /// 阿里云签名参数算法。
        /// 参考阿里云示例实现：https://help.aliyun.com/document_detail/29819.html
        /// </summary>
        /// <param name="param"></param>
        /// <param name="accessKeySecret"></param>
        /// <param name="signStr"></param>
        /// <param name="method"></param>
        /// <returns></returns>
        private static string GetSign(Dictionary<string, string> param, string accessKeySecret,
            out string signStr, string method = "GET")
        {
            var SEPARATOR = "&";

            StringBuilder stringToSign = new StringBuilder();
            stringToSign.Append(method).Append(SEPARATOR);
            stringToSign.Append(PercentEncode("/")).Append(SEPARATOR);

            StringBuilder canonicalizedQueryString = new StringBuilder();
            //var allKeys = param.OrderBy(item => item.Key);
            var allKeys = param.Keys.ToArray();
            Array.Sort(allKeys, string.CompareOrdinal); // 阿里要求按Ascii排序
            foreach (var key in allKeys)
            {
                // 这里注意对key和value进行编码
                canonicalizedQueryString.Append(SEPARATOR)
                    .Append(PercentEncode(key)).Append("=")
                    .Append(PercentEncode(param[key]));
            }

            // 这里注意对canonicalizedQueryString进行编码
            stringToSign.Append(PercentEncode(canonicalizedQueryString.ToString().Substring(1)));
            signStr = stringToSign.ToString();

            var ret = DoHMACSHA1(signStr, accessKeySecret + SEPARATOR);
            // 签名结果串里有特殊符号，如+ =，所以要Encode
            return Uri.EscapeDataString(ret);
        }

        /// <summary>
        /// HMAC签名
        /// </summary>
        /// <param name="desStr"></param>
        /// <param name="accessKeySecret"></param>
        /// <returns></returns>
        public static string DoHMACSHA1(string desStr, string accessKeySecret)
        {
            byte[] result;
            byte[] data = Encoding.UTF8.GetBytes(desStr);
            using (HMACSHA1 hmac = new HMACSHA1(Encoding.UTF8.GetBytes(accessKeySecret)))
            {
                result = hmac.ComputeHash(data);
            }

            return Convert.ToBase64String(result);
        }

        /// <summary> 
        /// 阿里云要求的UrlEncode，那些特殊字符%2F要求是大写的……
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        private static String PercentEncode(String value)
        {
            if (string.IsNullOrEmpty(value))
                return "";
            var lowStr = Uri.EscapeDataString(value);

            // HttpUtility.UrlEncode编码值为小写，要转换为大写
            var sb = new StringBuilder(lowStr);
            int i = lowStr.IndexOf('%'), j = sb.Length;
            while (i >= 0 && i + 2 < j) // 找到%，且后面还有2个字符时进入循环
            {
                if (sb[i + 1] >= 'a')
                    sb[i + 1] = (char) (sb[i + 1] - 32);
                if (sb[i + 2] >= 'a')
                    sb[i + 2] = (char) (sb[i + 2] - 32);
                i = lowStr.IndexOf('%', i + 2);
            }

            sb.Replace("+", "%20").Replace("*", "%2A").Replace("%7E", "~");
            return sb.ToString();
        }

        #endregion


        #region 远程访问url相关方法

        /// <summary>
        /// 访问页面返回内容
        /// </summary>
        /// <param name="url"></param>
        /// <param name="param"></param>
        /// <param name="isPost"></param>
        /// <param name="encoding"></param>
        /// <param name="showHeader"></param>
        /// <param name="allowRedirect"></param>
        /// <param name="headers"></param>
        /// <returns></returns>
        public static string GetPage(string url, string param = null, bool isPost = false,
            Encoding encoding = null, bool showHeader = false, bool allowRedirect = false,
            IDictionary<string, string> headers = null)
        {
            if (encoding == null)
                encoding = Encoding.UTF8;

            if (!isPost && !string.IsNullOrEmpty(param))
            {
                url = url + "&" + param;
            }

            HttpWebRequest request = (HttpWebRequest) WebRequest.Create(url);
            //request.Referer = url;
            request.AllowAutoRedirect = allowRedirect;
            request.UserAgent = "Mozilla/4.0 (compatible; MSIE 9.0; Beinet;)";
            request.Headers.Add("Accept-Encoding", "gzip, deflate");
            if (headers != null)
            {
                foreach (var pair in headers)
                {
                    request.Headers.Add(pair.Key, pair.Value);
                }
            }

            request.Timeout = 10000;

            if (!isPost)
            {
                request.Method = "GET";
                //request.ContentType = "text/html";
            }
            else
            {
                request.Method = "POST";
                request.ContentType = "application/x-www-form-urlencoded";
                if (!string.IsNullOrEmpty(param))
                {
                    byte[] bytes = encoding.GetBytes(param);
                    request.ContentLength = bytes.Length;
                    // 必须先设置ContentLength，才能打开GetRequestStream
                    using (Stream requestStream = request.GetRequestStream())
                    {
                        requestStream.Write(bytes, 0, bytes.Length);
                        requestStream.Close();
                    }
                }
                else
                    request.ContentLength = 0; // POST时，必须设置ContentLength属性
            }

            HttpWebResponse response;
            try
            {
                response = (HttpWebResponse) request.GetResponse();
            }
            catch (WebException webExp)
            {
                if (webExp.Response != null)
                {
                    using (var responseErr = (HttpWebResponse) webExp.Response)
                    {
                        var html = GetResponseString(responseErr, encoding);
                        throw new Exception(html, webExp);
                    }
                }

                throw;
            }

            using (response)
            {
                if (response.StatusCode != HttpStatusCode.OK)
                {
                    string errMsg = $"Response.StatusCode:{response.StatusCode}, {response.StatusDescription}";
                    throw new Exception(errMsg);
                }

                var str = GetResponseString(response, encoding);
                if (showHeader)
                {
                    str = string.Concat("请求头信息：\r\n", request.Headers, "\r\n\r\n响应头信息：\r\n", response.Headers,
                        "\r\n", str);
                }

                return str;
            }
        }

        static string GetResponseString(HttpWebResponse response, Encoding encoding)
        {
            using (Stream stream = response.GetResponseStream())
            {
                if (stream == null)
                    return "GetResponseStream is null";
                string str;
                string contentEncoding = response.ContentEncoding.ToLower();
                if (contentEncoding.Contains("gzip"))
                    using (Stream stream2 = new GZipStream(stream, CompressionMode.Decompress))
                        str = GetFromStream(stream2, encoding);
                else if (contentEncoding.Contains("deflate"))
                    using (Stream stream2 = new DeflateStream(stream, CompressionMode.Decompress))
                        str = GetFromStream(stream2, encoding);
                else
                    str = GetFromStream(stream, encoding);
                return str;
            }
        }

        static string GetFromStream(Stream stream, Encoding encoding)
        {
            using (StreamReader reader = new StreamReader(stream, encoding))
                return reader.ReadToEnd();
        }

        /// <summary>
        /// 把参数列表加到url后面
        /// </summary>
        /// <param name="url"></param>
        /// <param name="param"></param>
        /// <returns></returns>
        public static string AddUrlPara(string url, Dictionary<string, string> param)
        {
            url = url ?? "";
            int idx = url.IndexOf('?');
            foreach (var pair in param)
            {
                string firstCh = "&";
                if (idx < 0)
                {
                    firstCh = "?";
                    idx = 0;
                }

                url += firstCh + pair.Key + "=" + pair.Value;
            }

            return url;
        }

        #endregion

        #region JSON序列化

        public static string ToJson<T>(T obj)
        {
            JavaScriptSerializer js = new JavaScriptSerializer();
            return js.Serialize(obj);
        }

        public static T FromJson<T>(string json)
        {
            JavaScriptSerializer js = new JavaScriptSerializer();
            return js.Deserialize<T>(json);
        }

        #endregion
    }
}