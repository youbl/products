namespace MstscIps.Feign.Dto
{
    public class VpsMachineDto
    {
        /// <summary>
        /// 创建时间
        /// </summary>
        public string CreateDate { get; set; } = "";

        /// <summary>
        /// VPS IP
        /// </summary>

        public string VpsIp { get; set; } = "";

        /// <summary>
        /// VPS 密码
        /// </summary>

        public string VpsPwd { get; set; } = "";

        /// <summary>
        /// 所属分组
        /// </summary>
        public string GroupCode { get; set; } = "";

        /// <summary>
        /// VPS登录用户名
        /// </summary>
        public string User { get; set; } = "administrator";
    }
}