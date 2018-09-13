package com.dawn.img2text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.dawn.img2text.external.GifDecoder;
import com.dawn.img2text.util.FfmpegUtil;
import com.dawn.img2text.util.GifUtil;
import com.dawn.img2text.util.Im4JavaUtils;
import com.dawn.img2text.util.ImgUtil;
import com.dawn.img2text.util.VideoUtil;

public class TestImg {

    public static void main(String[] args) {
        // readImg();
        // readGif();
        // helloGif();
        // 
        imgTest();
         gifTest();
         videoTest();
    }

    public static void imgTest() {
        String inputFile = "F:/123/head.png";
        String outputFile = "F:/123/head_copy.png";
        // String base = "01"; // 替换的字符串
        String base = "@#&$%*o!;.";// 字符串由复杂到简单
        int threshold = 8;// 阈值
        ImgUtil.toTextImg(inputFile, outputFile, base, threshold);
    }

    public static void gifTest() {
        String srcFile = "F:/123/123.gif";
        String targetFile = "F:/123/123_04.gif";
        String base = "01"; // 替换的字符串
        // String base = "@#&$%*o!;.";// 字符串由复杂到简单
        int threshold = 3;// 阈值
        GifUtil.toTextGif(srcFile, targetFile, base, threshold);
    }

    public static void videoTest() {
        String srcVideoPath = "F:/123/123.mp4";
        String tarImagePath = "F:/123/mp/";
        String tarAudioPath = "F:/123/mp/audio.aac";
        String tarVideoPath = "F:/123/1234.mp4";
        VideoUtil.readVideo(srcVideoPath,tarImagePath,tarAudioPath,tarVideoPath);
    }

    public static boolean readVideo() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(40, 40, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        String srcVideoPath = "F:/123/123.mp4";
        String tarImagePath = "F:/123/mp/";
        int second = FfmpegUtil.getVideoTime(srcVideoPath, FfmpegUtil.ffmpegPath);
        for (float i = 0; i < second; i += 0.001F) {
            FfmpegUtil.processFfmpegImage(srcVideoPath, tarImagePath + (int) (i * 1000) + ".jpg", 300, 300, i, 0.001F);
            // System.out.println(tarImagePath + (int)(i*1000) + ".jpg");
        }
        return true;
    }

    public static void readImg() {
        // String inputFile = "F:/123/logo_img.jpg";
        // String outputFile = "F:/123/logo_img_copy.jpg";
        String inputFile = "F:/123/head.png";
        String blackFile = "F:/123/head_black.png";
        String outputFile = "F:/123/head_copy.png";
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
                    // final int index = Math.round(gray * (base.length() + 1) /
                    // 255);
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

    public static void readGif() {
        String inputFile = "F:/123/123.gif";
        String outputFile = "F:/123/321.gif";
        Iterator readers = ImageIO.getImageReadersByFormatName("gif");
        ImageReader reader = (ImageReader) readers.next();
        ImageInputStream iis;
        try {
            BufferedInputStream source = new BufferedInputStream(new FileInputStream(inputFile));
            iis = ImageIO.createImageInputStream(source);
            // 设置解码器的输入流
            reader.setInput(iis, true);

            System.out.println(reader.getWidth(0));
            System.out.println(reader.getHeight(0));

            int num = reader.getNumImages(false);
            System.out.println(num);

            reader.dispose();
            iis.close();
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*-  public static void getGif() {
        
        FileImageInputStream in = null;
        FileImageOutputStream out = null;
        try {
            in = new FileImageInputStream(new File(src));
            ImageReaderSpi readerSpi = new GIFImageReaderSpi();
            GIFImageReader gifReader = (GIFImageReader) readerSpi.createReaderInstance();
            gifReader.setInput(in);
            int num = gifReader.getNumImages(true);
        //  要取的帧数要小于总帧数
            if (num > frame) {
                ImageWriterSpi writerSpi = new GIFImageWriterSpi();
                GIFImageWriter writer = (GIFImageWriter) writerSpi.createWriterInstance();
                for (int i = 0; i < num; i++) {
                    if (i == frame) {
                        File newfile = new File(target);
                        out = new FileImageOutputStream(newfile);
                        writer.setOutput(out);
    //                      读取读取帧的图片
                        writer.write(gifReader.read(i));
                        return true;
                    }
                }
            }
    }*/

    public static void readImg1() {
        // String inputFile = "F:/123/logo_img.jpg";
        // String outputFile = "F:/123/logo_img_copy.jpg";
        String inputFile = "F:/123/head.png";
        String outputFile = "F:/123/head_copy.png";
        BufferedImage src = null;
        BufferedImage tag = null;

        try {
            src = ImageIO.read(new FileInputStream(inputFile));
            int[] rgb = new int[3];
            int width = src.getWidth();
            int height = src.getHeight();
            int minx = src.getMinX();
            int miny = src.getMinY();
            // 目标图片，根据源文件类型
            tag = new BufferedImage(width, height, src.getType());
            // 绘制 后的图片
            tag.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, width, height,
                    null);
            // 黑白化
            tag = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(tag, null);

            Graphics2D graphic = tag.createGraphics();
            graphic.setColor(new Color(0.2f, 0.3f, 0.4f, 0.4f));
            graphic.fillRect(0, 0, width, height);

            for (int i = minx; i < width; i++) {
                for (int j = miny; j < height; j++) {
                    int pixel = tag.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                    rgb[0] = (pixel & 0xff0000) >> 16;
                    rgb[1] = (pixel & 0xff00) >> 8;
                    rgb[2] = (pixel & 0xff);
                    System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
                    tag.setRGB(i, j, 123);
                    // src.setRGB(123, 123, 123);
                }
            }

            // 输出图片
            boolean res = ImageIO.write(tag, outputFile.substring(outputFile.lastIndexOf(".") + 1),
                    new File(outputFile));
            System.out.println(res);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sads1() {
        try {
            String inputFileName = "F:/workspace/123/2/2.gif";
            GifDecoder decoder = new GifDecoder();
            decoder.read(inputFileName);
            int n = decoder.getFrameCount();// 得到frame的个数
            for (int i = 0; i < n; i++) {
                BufferedImage frame = decoder.getFrame(i); // 得到frame
                int delay = decoder.getDelay(i);// 得到延迟时间
                // 生成JPG文件
                String outFilePath = "F:/workspace/123/2/" + i + ".jpg";
                FileOutputStream out = new FileOutputStream(outFilePath);
                ImageIO.write(frame, "jpeg", out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sads() {
        String inputFile = "F:/workspace/123/2/2.gif";
        String outputFile = "F:/workspace/123/2/1234.gif";
        String outputPath = "F:/workspace/123/2/";
        GifDecoder d = new GifDecoder();
        d.read(inputFile);
        // d.decodeImageData();
        int n = d.getFrameCount();
        System.out.println(n);
        System.out.println(d.gctFlag);
        System.out.println(d.gctSize);
        System.out.println(d.gct);
        // for(int i : d.gct) System.out.println(i);
        for (int i = 0; i < n; i++) {
            try {
                String target = outputPath + i + ".jpg";
                boolean res = Im4JavaUtils.getGifOneFrame(inputFile, target, i);
                // ImgUtil4gif.readImg(outputPath + i + ".jpg", outputPath + i + "_black.jpg",
                // outputPath + i + "_copy.jpg");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
