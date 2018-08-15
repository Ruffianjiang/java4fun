package com.dawn.img2text.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImgUtil4gif {

    public static void readGif() {
        // TODO Auto-generated method stub
        Icon icon = new ImageIcon("F:\\workspace\\123\\123.gif");
        JLabel label = new JLabel(icon);

        JFrame f = new JFrame("Animation");
        f.getContentPane().add(label);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void readImg(String inputFile, String blackFile, String outputFile) {
        // String inputFile = "F:/123/logo_img.jpg";
        // String outputFile = "F:/123/logo_img_copy.jpg";
        // String inputFile = "F:/123/head.png";
        // String blackFile = "F:/123/head_black.png";
        // String outputFile = "F:/123/head_copy.png";
        BufferedImage src = null;
        BufferedImage tag = null;

        try {
            src = ImageIO.read(new FileInputStream(inputFile));
            int[] rgb = new int[3];
            int width = src.getWidth();
            int height = src.getHeight();
            int minx = src.getMinX();
            int miny = src.getMinY();
            // 黑白化
            src = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(src, null);
            boolean res = ImageIO.write(src, blackFile.substring(blackFile.lastIndexOf(".") + 1), new File(blackFile));
            System.out.println("黑化结果：" + res);

            src = ImageIO.read(new FileInputStream(blackFile));

            tag = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = tag.getGraphics();
            g.setFont(new Font("微软雅黑", Font.PLAIN, 10));// 设置字体
            g.setColor(Color.BLUE);// 设置颜色

            for (int i = minx; i < width; i += 6) {
                for (int j = miny; j < height; j += 6) {
                    int pixel = src.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                    rgb[0] = (pixel & 0xff0000) >> 16;// red
                    rgb[1] = (pixel & 0xff00) >> 8;// green
                    rgb[2] = (pixel & 0xff);// blue
                    // final String base = "@#&$%*o!;.";// 字符串由复杂到简单
                    // final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    // final int index = Math.round(gray * (base.length() + 1) / 255);
                    if (rgb[0] + rgb[1] + rgb[2] <= 500) {
                        System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
                        // Color ss = new Color(rgb[0], 50, 50);
                        // int ssd = ss.getRGB();
                        // src.setRGB(i, j, ssd);
                        g.drawString("v", i, j);// 文字的编写及位置
                    }
                    // src.setRGB(i, j, pixel);
                }
            }
            // tag.flush();
            g.dispose();

            // 输出图片
            res = ImageIO.write(tag, outputFile.substring(outputFile.lastIndexOf(".") + 1), new File(outputFile));
            System.out.println("字符化结果：" + res);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
