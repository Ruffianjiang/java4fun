/**   
* @Title: GifUtil.java 
* @Package com.dawn.img2text.util 
* @Description: TODO 
* @author jiang   
* @date 2018年8月14日 下午9:47:29 
* @version V1.0   
*/
package com.dawn.img2text.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dawn.img2text.external.AnimatedGifEncoder;
import com.dawn.img2text.external.GifDecoder;

/**
 * @ClassName: GifUtil
 * @Description: TODO
 * @author jiang
 * @date 2018年8月14日 下午9:47:29
 * 
 */
public class GifUtil {

    static Logger logger = LoggerFactory.getLogger(GifUtil.class);

    /**
     * 
     * @Title toTextGif
     * @Description
     * @param srcFile
     *            源文件
     * @param targetFile
     *            目标文件
     * @param base
     *            字符串
     * @param threshold
     *            阈值
     * @return
     * @return boolean
     */
    public static boolean toTextGif(final String srcFile, final String targetFile, final String base, int threshold) {

        long startTime = System.currentTimeMillis();
        try {
            GifDecoder gd = new GifDecoder();
            // 要处理的图片
            int status = gd.read(new FileInputStream(new File(srcFile)));
            if (status != GifDecoder.STATUS_OK) {
                return false;
            }
            //
            AnimatedGifEncoder ge = new AnimatedGifEncoder();
            // 这里是关键，设置要替换成透明的颜色
            ge.setTransparent(Color.WHITE);
            //
            ge.start(new FileOutputStream(new File(targetFile)));
            ge.setRepeat(0);
            for (int i = 0; i < gd.getFrameCount(); i++) {
                // 取得gif的每一帧
                BufferedImage frame = gd.getFrame(i);
                // 你可以对每一帧做点什么，比如缩放什么的，这里就什么都不做了
                int[] rgb = new int[3];
                int width = frame.getWidth();
                int height = frame.getHeight();
                int minx = frame.getMinX();
                int miny = frame.getMinY();
                int delay = gd.getDelay(i);
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics g = tag.getGraphics();
                g.setFont(new Font("微软雅黑", Font.PLAIN, 2));// 设置字体
                g.setColor(Color.BLACK);// 设置颜色
                for (int x = minx; x < width; x += 1) {
                    for (int y = miny; y < height; y += 1) {
                        int pixel = frame.getRGB(x, y); // 下面三行代码将一个数字转换为RGB数字
                        rgb[0] = (pixel & 0xff0000) >> 16;// red
                        rgb[1] = (pixel & 0xff00) >> 8;// green
                        rgb[2] = (pixel & 0xff);// blue

                        final float gray = 0.299F * rgb[0] + 0.578F * rgb[1] + 0.114F * rgb[2];
                        // index [0,base.length()),index越小颜色越深
                        final int index = Math.round(gray * (base.length() + 1) / 255);
                        if (index <= base.length() % threshold) {
                            g.drawString(String.valueOf(base.charAt(index % base.length())), x, y);// 文字的编写及位置
                        }
                        /*-
                        if (rgb[0] + rgb[1] + rgb[2] <= 300) {
                            g.drawString(String.valueOf(base.charAt(index % base.length())), x, y);// 文字的编写及位置
                        }*/
                    }
                }
                ge.setDelay(delay);
                ge.addFrame(tag);
            }
            // 输出图片
            ge.finish();
            logger.debug("{} toTextGif cost time： {}s", srcFile, System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            logger.error("err", e);
            return false;
        }
        return true;
    }
}
