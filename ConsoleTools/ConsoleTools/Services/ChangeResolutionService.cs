using ConsoleTools.Utilities;

namespace ConsoleTools.Services
{
    /// <summary>
    /// 修改系统分辨率
    /// </summary>
    internal class ChangeResolutionService : ServicesBase
    {
        public string Operate(string[] args)
        {
            string resolution = args.Length > 0 ? args[0] : "";
            if (string.IsNullOrEmpty(resolution))
            {
                return "分辨率参数不能为空, 举例 1280*800";
            }

            string[] arrWH = resolution.Split('*');
            if (arrWH.Length != 2)
            {
                return "分辨率参数有误, 举例 1280*800";
            }

            if (!int.TryParse(arrWH[0], out var width) || width <= 0)
            {
                return "分辨率宽度参数有误, 举例 1280*800";
            }

            if (!int.TryParse(arrWH[1], out var height) || height <= 0)
            {
                return "分辨率高度参数有误, 举例 1280*800";
            }

            var oldSetting = DisplayHelper.GetCurrentSettings();

            var result = DisplayHelper.ChangeSetting(width, height);
            if (result == DisplayHelper.DISP_CHANGE_SUCCESSFUL)
            {
                return "成功\n    当前分辨率:" + DisplayHelper.GetCurrentSettings() + "\n    修改前:" + oldSetting;
            }

            if (result == DisplayHelper.DISP_CHANGE_BADMODE)
                return "Mode not supported: " + resolution;
            if (result == DisplayHelper.DISP_CHANGE_RESTART)
                return "Restart required: " + resolution;


            return "Failed. Error code: " + result;
        }
    }
}