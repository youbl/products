using System;
using System.Text;
using Beinet.Core.Util;
using Beinet.Feign;
using NLog;
using RemindClock.FeignService;
using RemindClock.FeignService.Dto;
using RemindClock.Repository.Model;

namespace RemindClock.Services.NoteOperation
{
    public class NoteAlertByDingDing : INoteAlert
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();

        public void Alert(Notes note)
        {
            if (string.IsNullOrEmpty(note.DingDingToken))
            {
                return;
            }

            var sb = new StringBuilder();
            sb.Append(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"))
                .Append(" ")
                .Append(MainForm.APPNAME)
                .Append(":\n")
                .Append(note.Title)
                .Append("\n")
                .Append(note.Content)
                .Append("\n客户端:")
                .Append(IpHelper.GetServerIpList());

            DingDingDto.Text text = new DingDingDto.Text();
            text.content = sb.ToString();
            DingDingDto.At at = new DingDingDto.At();
            //at.atMobiles.Add("手机号"); 在钉钉群里的人的手机号

            DingDingDto dto = new DingDingDto();
            dto.text = text;
            dto.at = at;
            var ret = ProxyLoader.GetProxy<DingDingFeign>().send(note.DingDingToken, dto);
            logger.Info("钉钉返回结果:" + ret);
        }
    }
}