package com.aron.blog.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class Captcha {

    // 定义干扰线的数量
    private static final int LINE_NUMBER = 3;


    // 定义图片扩展名
    private static final String IMAGE_EXT = "JPEG";

    // 验证码图片的长和宽
    private int weight = 100;
    private int height = 50;
    // 验证码长度
    private int size = 4;

    // 用来保存验证码的文本内容
    private String text;
    // 获取随机数对象
    private Random r = new Random();
    // 字体数组
    private String[] fontNames = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};

    // 验证码数组
    private static final String SOURCE = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

    public Captcha() {}

    public Captcha(int height, int weight, int size) {
        this.height = height;
        this.weight = weight;
        this.size = size;
    }

    /**
     * 获取随机的颜色
     * @return
     */
    private Color randomColor() {
        // 这里为什么是150，因为当r，g，b都为255时，即为白色，为了好辨认，需要颜色深一点。
        int r = this.r.nextInt(150);
        int g = this.r.nextInt(150);
        int b = this.r.nextInt(150);
        return new Color(r, g, b);
    }

    /**
     * 获取随机字体
     * @return
     */
    private Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        // 随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int style = r.nextInt(4);
        // 随机获取字体的大小
        int size = r.nextInt(5) + 24;
        return new Font(fontName, style, size);
    }

    /**
     * 获取随机字符
     * @return
     */
    private char randomChar() {
        int index = r.nextInt(SOURCE.length());
        return SOURCE.charAt(index);
    }

    /**
     * 画干扰线，验证码干扰线用来防止计算机解析图片
     * @param image
     */
    private void drawLine(BufferedImage image) {

        Graphics2D g = (Graphics2D) image.getGraphics();
        for (int i = 0; i < LINE_NUMBER; i++) {
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
     * @return
     */
    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB); //创建图片缓冲区
        // 获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, weight, height);
        return image;
    }

    /**
     * 获取验证码图片的方法
     * @return
     */
    public BufferedImage getImage() {

        BufferedImage image = createImage();
        // 获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            // 随机生成字符，因为只有画字符串的方法，没有画字符的方法，所以需要将字符变成字符串再画
            String s = randomChar() + "";
            // 添加到StringBuilder里面
            sb.append(s);
            // 定义字符的x坐标
            float x = i * 1.0F * weight / size;
            // 设置字体，随机
            g.setFont(randomFont());
            // 设置颜色，随机
            g.setColor(randomColor());
            g.drawString(s, x, height - 5);
        }
        this.text = sb.toString();
        drawLine(image);
        return image;
    }

    /**
     * 获取验证码文本的方法
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * 将验证码图片写出的方法
     * @param image
     * @param out
     */
    public void output(BufferedImage image, OutputStream out) {
        try {
            ImageIO.write(image, IMAGE_EXT, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}