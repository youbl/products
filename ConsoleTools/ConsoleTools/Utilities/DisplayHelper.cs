using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;

namespace ConsoleTools.Utilities
{
    /// <summary>
    /// https://www.codeproject.com/Articles/36664/Changing-Display-Settings-Programmatically
    /// </summary>
    internal class DisplayHelper
    {
        public const int ENUM_CURRENT_SETTINGS = -1; // Retrieves the current display mode
        public const int ENUM_REGISTRY_SETTINGS = -2; // Retrieves the current display mode stored in the Registry


        public const int DISP_CHANGE_SUCCESSFUL = 0; // Indicates that the function succeeded.
        public const int DISP_CHANGE_BADMODE = -2; //The graphics mode is not supported.
        public const int DISP_CHANGE_FAILED = -1; // The display driver failed the specified graphics mode.
        public const int DISP_CHANGE_RESTART = 1; //The computer must be restarted for the graphics mode to work.


        [DllImport("User32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        private static extern Boolean EnumDisplaySettings(
            [param: MarshalAs(UnmanagedType.LPTStr)]
            string lpszDeviceName,
            [param: MarshalAs(UnmanagedType.U4)] int iModeNum,
            [In, Out] ref DEVMODE lpDevMode);


        [DllImport("User32.dll")]
        [return: MarshalAs(UnmanagedType.I4)]
        private static extern int ChangeDisplaySettings(
            [In, Out] ref DEVMODE lpDevMode,
            [param: MarshalAs(UnmanagedType.U4)] uint dwflags);


        /// <summary>
        /// 获取当前显示设置
        /// </summary>
        /// <returns></returns>
        public static string GetCurrentSettings()
        {
            DEVMODE mode = new DEVMODE();
            mode.dmSize = (ushort) Marshal.SizeOf(mode);

            if (EnumDisplaySettings(null, ENUM_CURRENT_SETTINGS, ref mode) == true) // Succeeded
            {
                // dmDisplayOrientation 取值 0 1 2 4 8
                // 参考 https://docs.microsoft.com/en-us/uwp/api/Windows.Graphics.Display.DisplayOrientations?redirectedfrom=MSDN&view=winrt-22000
                return string.Format("分辨率:{0}*{1}, 每像素占显存位数:{2}bit, 显示器翻转度数:{3}degrees, 刷新率:{4}hz",
                    mode.dmPelsWidth,
                    mode.dmPelsHeight,
                    mode.dmBitsPerPel,
                    mode.dmDisplayOrientation * 90,
                    mode.dmDisplayFrequency);
            }

            return "unknown";
        }

        /// <summary>
        /// 枚举出所有支持的模式，仅分辨率，不考虑位数、度数、刷新率
        /// </summary>
        public static string[] EnumerateSupportedModes()
        {
            var ret = new HashSet<string>();

            DEVMODE mode = new DEVMODE();
            mode.dmSize = (ushort) Marshal.SizeOf(mode);

            int modeIndex = 0; // 0 = The first mode
            while (EnumDisplaySettings(null, modeIndex, ref mode)) // Mode found
            {
                var item = string.Format("{0}*{1}", mode.dmPelsWidth, mode.dmPelsHeight);
                ret.Add(item);
                modeIndex++; // The next mode
            }

            return ret.ToArray();
        }

        /// <summary>
        /// 修改为指定的分辨率
        /// </summary>
        /// <param name="width">宽度</param>
        /// <param name="height">高度</param>
        /// <returns>结果，0为成功</returns>
        public static int ChangeSetting(int width, int height)
        {
            DEVMODE originalMode = new DEVMODE();
            originalMode.dmSize = (ushort) Marshal.SizeOf(originalMode);

            // Retrieving current settings
            // to edit them
            EnumDisplaySettings(null, ENUM_CURRENT_SETTINGS, ref originalMode);

            // Making a copy of the current settings
            // to allow reseting to the original mode
            DEVMODE newMode = originalMode;

            // Changing the settings
            newMode.dmPelsWidth = (uint) width;
            newMode.dmPelsHeight = (uint) height;
            // newMode.dmBitsPerPel = (uint) bitCount;

            // Capturing the operation result
            return ChangeDisplaySettings(ref newMode, 0);
            // if (result == DISP_CHANGE_SUCCESSFUL)
            // {
            //     GetCurrentSettings();
            // }
            // else if (result == DISP_CHANGE_BADMODE)
            //     Console.WriteLine("Mode not supported.");
            // else if (result == DISP_CHANGE_RESTART)
            //     Console.WriteLine("Restart required.");
            // else
            //     Console.WriteLine("Failed. Error code = {0}", result);
        }

        [StructLayout(LayoutKind.Sequential,
            CharSet = CharSet.Ansi)]
        public struct DEVMODE
        {
            // You can define the following constant
            // but OUTSIDE the structure because you know
            // that size and layout of the structure
            // is very important
            // CCHDEVICENAME = 32 = 0x50
            [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 32)]
            public string dmDeviceName;
            // In addition you can define the last character array
            // as following:
            //[MarshalAs(UnmanagedType.ByValArray, SizeConst = 32)]
            //public Char[] dmDeviceName;

            // After the 32-bytes array
            [MarshalAs(UnmanagedType.U2)] public UInt16 dmSpecVersion;

            [MarshalAs(UnmanagedType.U2)] public UInt16 dmDriverVersion;

            [MarshalAs(UnmanagedType.U2)] public UInt16 dmSize;

            [MarshalAs(UnmanagedType.U2)] public UInt16 dmDriverExtra;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmFields;

            public POINTL dmPosition;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmDisplayOrientation;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmDisplayFixedOutput;

            [MarshalAs(UnmanagedType.I2)] public Int16 dmColor;

            [MarshalAs(UnmanagedType.I2)] public Int16 dmDuplex;

            [MarshalAs(UnmanagedType.I2)] public Int16 dmYResolution;

            [MarshalAs(UnmanagedType.I2)] public Int16 dmTTOption;

            [MarshalAs(UnmanagedType.I2)] public Int16 dmCollate;

            // CCHDEVICENAME = 32 = 0x50
            [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 32)]
            public string dmFormName;
            // Also can be defined as
            //[MarshalAs(UnmanagedType.ByValArray,
            //    SizeConst = 32, ArraySubType = UnmanagedType.U1)]
            //public Byte[] dmFormName;

            [MarshalAs(UnmanagedType.U2)] public UInt16 dmLogPixels;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmBitsPerPel;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmPelsWidth;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmPelsHeight;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmDisplayFlags;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmDisplayFrequency;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmICMMethod;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmICMIntent;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmMediaType;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmDitherType;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmReserved1;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmReserved2;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmPanningWidth;

            [MarshalAs(UnmanagedType.U4)] public UInt32 dmPanningHeight;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct POINTL
        {
            [MarshalAs(UnmanagedType.I4)] public int x;
            [MarshalAs(UnmanagedType.I4)] public int y;
        }
    }
}