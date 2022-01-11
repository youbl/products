using System;
using System.Net;

namespace CaptureDesktop.Controllers
{
    public class IndexController : IController
    {
        public void Process(HttpListenerRequest request, HttpListenerResponse response)
        {
            var url = request.RawUrl;

            var responseString =
                $"<HTML><BODY>{DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff")} 请求地址:{url} </BODY></HTML>";
            byte[] buffer = System.Text.Encoding.UTF8.GetBytes(responseString);
            //对客户端输出相应信息.
            using (response)
            using (var output = response.OutputStream)
            {
                response.ContentType = "text/html;charset=utf-8";
                response.ContentLength64 = buffer.Length;
                output.Write(buffer, 0, buffer.Length);
            }
        }
    }
}