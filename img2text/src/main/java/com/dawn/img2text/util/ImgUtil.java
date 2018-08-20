/**   
* @Title: ImgUtil.java 
* @Package com.dawn.img2text.util 
* @Description: TODO 
* @author jiang   
* @date 2018年8月14日 下午10:15:56 
* @version V1.0   
*/
package com.dawn.img2text.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ImgUtil
 * @Description: TODO
 * @author jiang
 * @date 2018年8月14日 下午10:15:56
 * 
 */
public class ImgUtil {

    static Logger logger = LoggerFactory.getLogger(ImgUtil.class);

    public static boolean toTextImg(String inputFile, String outputFile, final String base, int threshold) {

//        String blackFile = "F:/123/head_black.png";
        BufferedImage src = null;
        BufferedImage tag = null;
        boolean res = false;
        try {
            src = ImageIO.read(new FileInputStream(inputFile));
            int[] rgb = new int[3];
            int width = src.getWidth();
            int height = src.getHeight();
            int minx = src.getMinX();
            int miny = src.getMinY();
            // 黑白化
/*-                        
            src = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(src, null);
            res = ImageIO.write(src, blackFile.substring(blackFile.lastIndexOf(".") + 1), new File(blackFile));
            src = ImageIO.read(new FileInputStream(blackFile));*/

            tag = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = tag.getGraphics();
            g.setFont(new Font("微软雅黑", Font.PLAIN, 10));// 设置字体
            g.setColor(Color.BLUE);// 设置颜色
            for (int x = minx; x < width; x += 6) {
                for (int y = miny; y < height; y += 6) {
                    int pixel = src.getRGB(x, y); // 下面三行代码将一个数字转换为RGB数字
                    rgb[0] = (pixel & 0xff0000) >> 16;// red
                    rgb[1] = (pixel & 0xff00) >> 8;// green
                    rgb[2] = (pixel & 0xff);// blue
                    final float gray = 0.299f * rgb[0] + 0.578f * rgb[1] + 0.114f * rgb[2];
                    final int index = Math.round(gray * (base.length() + 1) / 255);
//                    logger.debug("{},{}",index,base.length() / threshold);
                    if (index <= threshold) {
                        g.drawString(String.valueOf(base.charAt(index % base.length())), x, y);// 文字的编写及位置
                    }

                    /*-
                    if (rgb[0] + rgb[1] + rgb[2] <= 500) {
                        System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
                        g.drawString("v", i, j);// 文字的编写及位置
                    }*/
                }
            }
            g.dispose();

            // 输出图片
            res = ImageIO.write(tag, outputFile.substring(outputFile.lastIndexOf(".") + 1),
                    new File(outputFile));
            logger.debug("字符化结果：{}", res);
        } catch (IOException e) {
            logger.error("err", e);
            return false;
        }
        return true;
    }

}
