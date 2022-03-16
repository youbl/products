using System.Drawing;
using System.Drawing.Imaging;
using System.Windows.Forms;

namespace ConsoleTools.Utilities
{
    /// <summary>
    /// 屏幕截图工具
    /// </summary>
    public class CaptureImg2
    {
        /// <summary>
        /// 单例
        /// </summary>
        public static CaptureImg2 Default = new CaptureImg2();

        private CaptureImg2()
        {
        }

        /// <summary>
        /// 支持多显示器截图
        /// </summary>
        /// <returns></returns>
        public Bitmap GetScreen()
        {
            int screenLeft = SystemInformation.VirtualScreen.Left;
            int screenTop = SystemInformation.VirtualScreen.Top;
            int screenWidth = SystemInformation.VirtualScreen.Width;
            int screenHeight = SystemInformation.VirtualScreen.Height;
            Bitmap bmp = new Bitmap(screenWidth, screenHeight);

            // Draw the screenshot into our bitmap.
            using (Graphics g = Graphics.FromImage(bmp))
            {
                g.CopyFromScreen(screenLeft, screenTop, 0, 0, bmp.Size);
            }
            return bmp;
        }

        public void CaptureScreenToFile(string filename, ImageFormat format)
        {
            using (Image img = GetScreen())
            {
                img.Save(filename, format);
            }
        }
    }
}