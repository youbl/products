using System.Net;

namespace CaptureDesktop.Controllers
{
    public interface IController
    {
        void Process(HttpListenerRequest request, HttpListenerResponse response);
    }
}