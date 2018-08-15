package com.dawn.img2text.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.core.ImageCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Im4JavaUtils {
    private static final Logger logger = LoggerFactory.getLogger(Im4JavaUtils.class);

    /** 是否使用 GraphicsMagick (Windows下为true) **/
    private static final boolean USE_GRAPHICS_MAGICK_PATH = false;
    /** ImageMagick 安装目录 **/
    private static final String IMAGE_MAGICK_PATH = "C:\\Program Files (x86)\\ImageMagick-6.3.9-Q8";
    /** GraphicsMagick 安装目录 **/
    private static final String GRAPHICS_MAGICK_PATH = "C:\\Program Files\\GraphicsMagick-1.3.30-Q16";

    /**
     * 获取 ImageCommand
     * @param comm 命令类型（convert, identify）
     * @return
     */
    private static ImageCommand getImageCommand(String comm) {
        ImageCommand cmd = null;
        if ("convert".equalsIgnoreCase(comm)) {
            cmd = new ConvertCmd(USE_GRAPHICS_MAGICK_PATH);
        } else if ("identify".equalsIgnoreCase(comm)) {
            cmd = new IdentifyCmd(USE_GRAPHICS_MAGICK_PATH);
        } // else if....
//        Windows环境下需要配置应用路径
        if (cmd != null && System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            cmd.setSearchPath(USE_GRAPHICS_MAGICK_PATH ? GRAPHICS_MAGICK_PATH : IMAGE_MAGICK_PATH);
        }
        return cmd;
    }


    /**
     * 降低品质，以减小文件大小
     * @param path 原文件路径
     * @param des 目标文件路径
     * @param quality 保留品质（1-100）
     * @throws Exception
     */
    public static void reduceQuality(String path, String des, double quality) throws Exception {
        createDirectory(des);
        IMOperation op = new IMOperation();
        op.addImage(path);
        op.quality(quality);
        op.addImage(des);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.run(op);
    }

    /**
     * 改变图片大小比例
     * @param path 原文件路径
     * @param des 目标文件路径
     * @param ratio 缩放比例
     * @throws Exception
     */
    public static void resizeImage(String path, String des, String ratio) throws Exception {
//        创建目标文件
        createDirectory(des);
        IMOperation op = new IMOperation();
        op.addImage(path);
        op.addRawArgs(ratio);
        op.addImage(des);

        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.run(op);
    }

    /**
     * 等比缩放图片（如果width为空，则按height缩放; 如果height为空，则按width缩放）
     * @param path 原文件路径
     * @param des 目标文件路径
     * @param width 缩放后的宽度
     * @param height 缩放后的高度
     * @param sample 是否以缩放方式，而非缩略图方式
     * @throws Exception 
     */
    public static void scaleResizeImage(String path, String des, Integer width, Integer height, boolean sample) throws Exception {
        createDirectory(des);
        IMOperation op = new IMOperation();
        op.addImage(path);
        if (sample) op.resize(width, height);
        else op.sample(width, height);
        op.addImage(des);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.run(op);
    }

    /**
     * 从原图中裁剪出新图
     * @param path 原文件路径
     * @param des 目标文件路径
     * @param x 原图左上角
     * @param y 原图左上角
     * @param width 新图片宽度
     * @param height 新图片高度
     * @throws Exception
     */
    public static void cropImage(String path, String des, int x, int y, int width, int height) throws Exception {
        createDirectory(des);
        IMOperation op = new IMOperation();
        op.addImage(path);
        op.crop(width, height, x, y);
        op.addImage(des);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.run(op);
    }

    /**
     * 将图片分割为若干小图
     * @param path 原文件路径
     * @param des 目标文件路径
     * @param width 指定宽度（默认为完整宽度）
     * @param height 指定高度（默认为完整高度）
     * @return 小图路径
     * @throws Exception
     */
    public static List<String> subsectionImage(String path, String des, Integer width, Integer height) throws Exception {
        createDirectory(des);
        IMOperation op = new IMOperation();
        op.addImage(path);
        op.crop(width, height);
        op.addImage(des);

        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.run(op);

        return getSubImages(des);
    }

    /**
     * <pre>
     * <li>去除Exif信息</li>
     * <li>按指定的宽度等比缩放图片</li>
     * <li>降低图片品质</li>
     * <li>将图片分割分指定高度的小图</li>
     *
     * @param path 原文件路径
     * @param des 目标文件路径
     * @param width 指定宽度
     * @param subImageHeight 指定高度
     * @param quality 保留品质
     * @return 小图路径
     * @throws Exception
     * </pre>
     */
    public static List<String> resizeAndCropImage(String path, String des, int width, int subImageHeight, double quality) throws Exception {
        createDirectory(des);
        IMOperation op = new IMOperation();
        op.addImage(path);

//        op.profile("*");
        op.resize(width, null);
        op.quality(quality);
        op.crop(null, subImageHeight);

        op.addImage(des);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.run(op);

        return getSubImages(des);
    }

    /***
     * 切图
     * @param source path       源图片路径
     * @param target            目标图片路径
     * @param width             限宽
     * @param subImageHeight    最终高度
     * @param quality           图片质量(0~100)
     * @throws Exception
     */
    public static void cropImage(String source, String target, int width, int subImageHeight, double quality) throws Exception {
        createDirectory(target);
        IMOperation op = new IMOperation();
        op.addImage(source);
        op.resize(width, null);
        op.quality(quality);
        op.crop(null, subImageHeight, 0, 15);
        op.addImage(target);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.run(op);
    }
    /**
     * 拷贝图片 - 同步
     * @param source
     * @param target
     * @return
     * @throws Exception
     */
    public static boolean copyImage(String source, String target) throws Exception {
        createDirectory(target);
        IMOperation op = new IMOperation();
        op.addImage(source);
        op.addImage(target);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.setAsyncMode(false);
        cmd.run(op);
        return true;
    }
    /**
     * 拷贝图片 - 异步
     * @param source
     * @param target
     * @return
     * @throws Exception
     */
    public static void copyImageAsync(String source, String target) throws Exception {
        createDirectory(target);
        IMOperation op = new IMOperation();
        op.addImage(source);
        op.addImage(target);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.setAsyncMode(true);
        cmd.run(op);
    }

    /**
     * 创建目录
     * @param path
     */
    private static void createDirectory(String path) {
        File file = new File(path);
        if (file.exists()){
            return;
        }
        file.getParentFile().mkdirs();
    }

    /**
     * 获取图片分割后的小图路径
     * @param des 目录路径
     * @return 小图路径
     */
    private static List<String> getSubImages(String des) {
        String fileDir = des.substring(0, des.lastIndexOf(File.separatorChar)); // 文件所在目录
        String fileName = des.substring(des.lastIndexOf(File.separatorChar) + 1); // 文件名称
        String n1 = fileName.substring(0, fileName.lastIndexOf(".")); // 文件名（无后缀） 
        String n2 = fileName.replace(n1, ""); // 后缀

        List<String> fileList = new ArrayList<String>();
        String path = null;
        for (int i = 0;; i++) {
            path = fileDir + File.separatorChar + n1 + "-" + i + n2;
            if (new File(path).exists()) fileList.add(path);
            else break;
        }
        return fileList;
    }

    /**
     * 获取GIF图片一帧图片 - 同步执行
     * @param src
     * @param target
     * @param frame
     * @throws Exception
     */
    public static boolean getGifOneFrame(String src, String target, int frame) throws Exception {
        if(!src.endsWith(".gif")){
            return false;
        }
        createDirectory(target);
        IMOperation op = new IMOperation();
        op.addImage(src + "["+frame+"]");
        op.addImage(target);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.setAsyncMode(false);
        cmd.run(op);
        return true;
    }
    /**
     * 获取GIF图片一帧图片 - 异步执行
     * @param src
     * @param target
     * @param frame
     * @throws Exception
     */
    public static void getGifOneFrameAsync(String src, String target, int frame) throws Exception {
        if(!src.endsWith(".gif")){
            return;
        }
        createDirectory(target);
        IMOperation op = new IMOperation();
        op.addImage(src + "["+frame+"]");
        op.addImage(target);
        ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
        cmd.setAsyncMode(true);
        cmd.run(op);
    }

    public static void main(String[] args) throws Exception {
        String src = "F:/workspace/123/123.gif";
        String target = "F:/workspace/123/1.jpg";
        getGifOneFrameAsync(src, target,1);
        logger.info("处理完成");
    }
}
