package com.chaoip.ipproxy.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class ImgHelper {

    private int weight = 100;           //验证码图片的长和宽
    private int height = 40;
    private Random r = new Random();    //获取随机数对象
    //private String[] fontNames = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};   //字体数组
    //字体数组
    private String[] fontNames = {"Georgia"};

    /**
     * 获取验证码图片对象
     *
     * @param text 难码
     * @return 图片
     */
    public BufferedImage getImage(String text) {
        BufferedImage image = createImage();
        Graphics2D g = (Graphics2D) image.getGraphics(); //获取画笔
        for (int i = 0; i < text.length(); i++)             //画四个字符即可
        {
            String chr = text.substring(i, i + 1);
            float x = i * 1.0F * weight / 4;   //定义字符的x坐标
            g.setFont(randomFont());           //设置字体，随机
            g.setColor(randomColor());         //设置颜色，随机
            g.drawString(chr, x, height - 5);
        }
        drawLine(image);
        return image;
    }

    /**
     * 获取验证码图片字符串
     *
     * @param text 难码
     * @return 图片的base64流
     */
    public String getImageBase64(String text) throws IOException {
        BufferedImage img = getImage(text);
        byte[] bytes;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(img, "JPEG", stream);
            bytes = stream.toByteArray();
        }
        String base64 = Base64.getEncoder().encodeToString(bytes).trim();//转换成base64串
        return base64.replaceAll("[\\r\\n]", "");
    }


    private Color randomColor() {
        int r = this.r.nextInt(225);  //这里为什么是225，因为当r，g，b都为255时，即为白色，为了好辨认，需要颜色深一点。
        int g = this.r.nextInt(225);
        int b = this.r.nextInt(225);
        return new Color(r, g, b);            //返回一个随机颜色
    }

    /**
     * 获取随机字体
     *
     * @return 字体
     */
    private Font randomFont() {
        int index = r.nextInt(fontNames.length);  //获取随机的字体
        String fontName = fontNames[index];
        int style = r.nextInt(4);         //随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int size = r.nextInt(10) + 24;    //随机获取字体的大小
        return new Font(fontName, style, size);   //返回一个随机的字体
    }

    /**
     * 画干扰线，验证码干扰线用来防止计算机解析图片
     *
     * @param image 图片
     */
    private void drawLine(BufferedImage image) {
        int num = r.nextInt(10); //定义干扰线的数量
        Graphics2D g = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(weight);
            int y1 = r.nextInt(height);
            int x2 = r.nextInt(weight);
            int y2 = r.nextInt(height);
            g.setColor(randomColor());
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 创建图片的方法
     *
     * @return 图片
     */
    private BufferedImage createImage() {
        //创建图片缓冲区
        BufferedImage image = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        //设置背景色随机
        g.setColor(new Color(255, 255, r.nextInt(245) + 10));
        g.fillRect(0, 0, weight, height);
        //返回一个图片
        return image;
    }

}
