package com.dawn.img2text.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VideoHelper {

    public static boolean readVideo() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 60, 3, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1000));
        executor.allowCoreThreadTimeOut(true);
        String srcVideoPath = "F:/123/123.mp4";
        String tarImagePath = "F:/123/mp/";
        float step = 0.1F;// 多少s截一张图
        float length = 1F;// 每个线程做多少s的事情
        int second = FfmpegUtil.getVideoTime(srcVideoPath, FfmpegUtil.ffmpegPath);
        for (float i = 0; i < second; i += length) {
            executor.execute(new videoScreenshot(srcVideoPath, tarImagePath, i, length, step, 300, 300));
        }
        executor.shutdown();
        while(executor.getActiveCount() > 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("------------------------------------------");
            System.out.println("当前队列可用数量" +executor.getQueue().remainingCapacity());
            System.out.println("当前已完成任务数" +executor.getCompletedTaskCount());
            System.out.println("当前活跃线程数" +executor.getActiveCount());
        };
        
        return true;
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        readVideo();
        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}

class videoScreenshot implements Runnable {

    String srcVideoPath;
    String tarImagePath;
    float index;
    float length;
    float step;
    int width;
    int height;

    public videoScreenshot(final String srcVideoPath, final String tarImagePath, final float index,final float length, final float step,
            final int width, final int height) {
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
        System.out.println("我是线程：" + index);
        for (float i = index; i < index + length; i += step) {
            FfmpegUtil.processFfmpegImage(srcVideoPath, tarImagePath + (int) (index / step) + ".jpg", width, height, index,
                    step);
        }
    }
}