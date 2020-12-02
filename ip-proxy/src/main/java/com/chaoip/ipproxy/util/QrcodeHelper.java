package com.chaoip.ipproxy.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * QrcodeHelper
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/2 23:32
 */
public class QrcodeHelper {
    /**
     * 生成指定内容的二维码，高宽均为400
     *
     * @param content 二维码内容
     * @return base64的图片流
     * @throws WriterException 异常
     */
    public String getQrcode(String content) throws WriterException, IOException {
        return getQrcode(content, 400, 400);
    }

    /**
     * 生成指定内容的二维码
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @return base64的图片流
     * @throws WriterException 异常
     */
    public String getQrcode(String content, int width, int height) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //边框距
        hints.put(EncodeHintType.MARGIN, 0);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix img = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return getImageBase64(img);
    }

    String getImageBase64(BitMatrix img) throws IOException {
        byte[] bytes;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(img, "png", stream);
            bytes = stream.toByteArray();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        return png_base64.replaceAll("[\\r\\n]", "");
    }
}
