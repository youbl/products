using System;
using System.Collections.Generic;
using System.Net;
using System.Threading;
using CaptureDesktop.Controllers;

namespace CaptureDesktop
{
    /// <summary>
    /// 监听和处理http请求的类
    /// </summary>
    public class HttpHandler
    {
        public static HttpHandler Default = new HttpHandler();

        private string defaultController = "Index";
        private Dictionary<string, IController> allControllers;

        private HttpHandler()
        {
            allControllers = new Dictionary<string, IController>();
            allControllers.Add(defaultController, new IndexController());
            allControllers.Add("Capture", new CaptureController());
        }

        public void StartListen()
        {
            var listener = new HttpListener();
            // 注：不能用localhost，否则不会监听127.0.0.1;而用IP则会监听localhost
            listener.Prefixes.Add("http://127.0.0.1:8881/"); // 监听8881端口
            listener.Start();

            while (true)
            {
                ThreadPool.QueueUserWorkItem(ProcessRequest, listener.GetContext());
            }

            // ReSharper disable once FunctionNeverReturns
        }

        private void ProcessRequest(object obj)
        {
            var context = obj as HttpListenerContext;
            if (context == null)
            {
                return;
            }

            var request = context.Request;
            var url = request.RawUrl; // 不含主机和端口，也没有#后的内容
            var controllerName = GetController(url);
            if (controllerName.Length <= 0)
            {
                controllerName = defaultController;
            }

            if (!allControllers.TryGetValue(controllerName, out var controller))
            {
                controller = allControllers[defaultController];
            }

            controller.Process(request, context.Response);
        }

        /// <summary>
        /// 获取请求url里的第一段
        /// </summary>
        /// <param name="url">url</param>
        /// <returns></returns>
        private string GetController(string url)
        {
            if (string.IsNullOrEmpty(url))
            {
                return defaultController;
            }

            url = url.TrimStart('/');
            var splitChs = new[] {'/', '?', '&', '='};
            foreach (var ch in splitChs)
            {
                var idx = url.IndexOf(ch);
                if (idx > 0)
                {
                    return url.Substring(0, idx);
                }
            }

            return url;
        }
    }
}