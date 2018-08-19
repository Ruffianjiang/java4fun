package com.dawn.img2text.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoUtil {

    static Logger logger = LoggerFactory.getLogger(ImgUtil.class);

    public static boolean readVideo(String srcVideoPath, String tarImagePath, String tarAudioPath,
            String tarVideoPath) {

        String imagePath = tarImagePath + "%d.jpg";

        ThreadPoolExecutor executor = new ThreadPoolExecutor(60, 100, 3, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(10000));
        executor.allowCoreThreadTimeOut(true);
        float step = 0.1F;// 多少s截一张图
        float length = 0.5F;// 每个线程做多少s的事情
        int second = FfmpegUtil.getVideoTime(srcVideoPath, FfmpegUtil.ffmpegPath);
        for (float i = 0; i < second; i += length) {
            executor.execute(new videoScreenshot(srcVideoPath, tarImagePath, i, length, step, 300, 300));
        }
        executor.shutdown();
        while (executor.getActiveCount() > 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error("err",e);
            }
            System.out.println("------------------------------------------");
            System.out.println("当前队列可用数量" + executor.getQueue().remainingCapacity());
            System.out.println("当前已完成任务数" + executor.getCompletedTaskCount());
            System.out.println("当前活跃线程数" + executor.getActiveCount());
        }
        logger.debug("{}", "图片转换完成");

        FfmpegUtil.processFfmpegAudio(srcVideoPath, tarAudioPath);
        logger.debug("{}", "音频转换完成");

        FfmpegUtil.processFfmpegVideo(imagePath, tarAudioPath, tarVideoPath, (int) (1 / step));
        logger.debug("{}", "视频转换完成");
        return true;
    }

    public static void main(String[] args) {
        String srcVideoPath = "F:/123/123.mp4";
        String tarImagePath = "F:/123/mp/";
        String tarAudioPath = "F:/123/mp/audio.aac";
        String tarVideoPath = "F:/123/1234.mp4";
        long start = System.currentTimeMillis();
        readVideo(srcVideoPath, tarImagePath, tarAudioPath, tarVideoPath);
        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}

class videoScreenshot implements Runnable {

    static Logger logger = LoggerFactory.getLogger(videoScreenshot.class);
    String srcVideoPath;
    String tarImagePath;
    float index;
    float length;
    float step;
    int width;
    int height;

    public videoScreenshot(final String srcVideoPath, final String tarImagePath, final float index, final float length,
            final float step, final int width, final int height) {
        this.srcVideoPath = srcVideoPath;
        this.tarImagePath = tarImagePath;
        this.index = index;
        this.length = length;
        this.step = step;
        this.width = width;
        this.height = height;
    }

    @Override
    public void run() {
        logger.debug("我是线程：{}", (int) (index / length));
        String fileName = null;
        for (float max = index + length; index < max; index += step) {
            fileName = tarImagePath + (int) (index / step) + ".jpg";
            FfmpegUtil.processFfmpegImage(srcVideoPath, fileName, width, height, index, step);
            ImgUtil.toTextImg(fileName, fileName, "@#&$%^*", 3);
        }
    }
}