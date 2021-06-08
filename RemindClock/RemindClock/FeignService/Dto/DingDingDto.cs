using System;
using System.Collections.Generic;

namespace RemindClock.FeignService.Dto
{
    public class DingDingDto
    {
        // 参考 https://developers.dingtalk.com/document/app/custom-robot-access
        //(value = "消息类型：文本 (text)、链接 (link)、markdown(markdown)、ActionCard、FeedCard")
        public String msgtype { get; set; } = "text";

        //(required = true, value = "消息内容")
        public Text text { get; set; }

        public At at { get; set; }

        public class Text
        {
            public String content { get; set; }
        }

        public class At
        {
            //(value = "要@的人列表")
            public List<String> atMobiles { get; set; } = new List<string>();

            //(value = "是否@所有人")
            public bool isAtAll { get; set; } = false;
        }


        public class DingDingResultDto
        {
            public int errcode { get; set; }

            public String errmsg { get; set; }
        }
    }
}