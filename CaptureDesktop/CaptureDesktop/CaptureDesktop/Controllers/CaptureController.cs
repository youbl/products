using System;
using System.Drawing.Imaging;
using System.Net;

namespace CaptureDesktop.Controllers
{
    public class CaptureController : IController
    {
        public void Process(HttpListenerRequest request, HttpListenerResponse response)
        {
            var url = request.RawUrl;

            // 截图，并转成二进制
            byte[] buffer;
            using (var img = CaptureUtil.Default.CaptureScreen())
            {
                buffer = CaptureUtil.Default.GetByteImage(img, ImageFormat.Jpeg);
            }

            // 输出
            using (response)
            using (var output = response.OutputStream)
            {
                response.AddHeader("Content-Disposition", "attachment;filename=test.jpg");
                response.AddHeader("Content-Type", "application/x-jpg");

                response.ContentLength64 = buffer.Length;
                output.Write(buffer, 0, buffer.Length);
            }
        }
    }
}