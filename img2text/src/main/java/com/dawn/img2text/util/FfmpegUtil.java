/**   
* @Title: FfmpegUtil.java 
* @Package com.dawn.img2text.external 
* @Description: TODO 
* @author jiang   
* @date 2018年8月14日 下午9:43:12 
* @version V1.0   
*/
package com.dawn.img2text.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: FfmpegUtil
 * @Description: TODO
 * @author jiang
 * @date 2018年8月14日 下午9:43:12
 * 
 */
public class FfmpegUtil {
    // final static Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);

    public final static String ffmpegPath = "F:/123/ffmpeg-20180201-b1af0e2-win64-static/bin/ffmpeg.exe";

    public static String getInfo(String srcVideoPath) {
        String info = null;
        List<String> commend = new java.util.ArrayList<String>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(srcVideoPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.redirectErrorStream(true);
            builder.redirectOutput(new File("F:/123/dp/log.log"));
            Process process = builder.start();
            doWaitFor(process);
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * ffmpeg 截图，并指定图片的大小
     * 
     * @param srcVideoPath
     * @param tarImagePath
     *            截取后图片路径
     * @param width
     *            截图的宽
     * @param hight
     *            截图的高
     * @param offsetValue
     *            表示相对于文件开始处的时间偏移值 可以是分秒
     * @param vframes
     *            表示截图的桢数
     * 
     * @return
     */
    public static boolean processFfmpegImage(String srcVideoPath, String tarImagePath, int width, int hight,
            float offsetValue, float vframes) {
        if (!checkfile(srcVideoPath)) {
            System.out.println("【" + srcVideoPath + "】  不存在 !");
            // logger.error("【" + srcVideoPath + "】 不存在 !");
            return false;
        }
        List<String> commend = new java.util.ArrayList<String>();

        commend.add(ffmpegPath);

        commend.add("-i");

        commend.add(srcVideoPath);

        commend.add("-y");

        commend.add("-f");

        commend.add("image2");

        commend.add("-ss");

        commend.add(offsetValue + ""); // 在视频的某个插入时间截图，例子为5秒后

        // commend.add("-vframes");

        commend.add("-t");// 添加参数＂-t＂，该参数指定持续时间

        commend.add(vframes + ""); // 截图的桢数,添加持续时间为1毫秒

        commend.add("-s");

        commend.add(width + "x" + hight); // 截图的的大小

        commend.add(tarImagePath);

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.redirectErrorStream(true);
            builder.redirectOutput(new File("F:/123/dp/log.log"));
            Process process = builder.start();
            doWaitFor(process);
            process.destroy();
            if (!checkfile(tarImagePath)) {
                System.out.println(tarImagePath + " is not exit!  processFfmpegImage 转换不成功 !");
                // logger.info(tarImagePath + " is not exit! processFfmpegImage
                // 转换不成功 !");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("【" + srcVideoPath + "】 processFfmpegImage  转换不成功 !");
            // logger.error("【" + srcVideoPath + "】 processFfmpegImage 转换不成功
            // !");
            return false;
        }
    }

    /**
     * 等待进程处理
     * 
     * @param p
     * @return
     */
    @SuppressWarnings("unused")
    public static int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1; // returned to caller when p is finished
        try {
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false; // Set to true when p is finished
            while (!finished) {
                try {
                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                    }
                    while (err.available() > 0) {
                        Character c = new Character((char) err.read());
                    }
                    exitValue = p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException e) {
                    Thread.currentThread();
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            System.out.println("doWaitFor();: unexpected exception - " + e.getMessage());
            // logger.error("doWaitFor();: unexpected exception - " +
            // e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("等待进程处理错误");
                // logger.error("等待进程处理错误");
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                    System.out.println("等待进程处理错误");
                    // logger.error("等待进程处理错误");
                }
            }
        }
        return exitValue;
    }

    /**
     * 删除文件
     * 
     * @param filepath
     */
    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("文件【" + filepath + "】已删除");
                // logger.info("文件【" + filepath + "】已删除");
            }
        }
    }

    /**
     * 根据时间返回总秒数 形如：（00:12:12）
     * 
     * @param timeStr
     * @return
     */
    public static String getSplitStr(String timeStr) {
        String secStr = "0";// 返回秒
        if (timeStr != null && !timeStr.equals("")) {
            String[] str = timeStr.split(":");
            int subInt0 = Integer.parseInt(str[0]);
            int subInt1 = Integer.parseInt(str[1]);
            String str2s = "";
            if (str[2].length() > 2 && str[2].indexOf(".") > 0) {
                str2s = str[2].substring(0, str[2].indexOf("."));
            } else {
                str2s = str[2];
            }
            int subInt2 = Integer.parseInt(str2s);
            Long countNum = subInt0 * 3600L + subInt1 * 60 + subInt2;
            secStr = countNum + "";
        }
        return secStr;
    }

    /**
     * 计算两个字符串时间相减 如：("00:22:22")
     * 
     * @param time1
     * @param time2
     * @return
     */
    public static String calTime(String time1, String time2) {
        Long time1Long = Long.parseLong(time1);
        Long time2Long = Long.parseLong(time2);
        Long timeLong = time2Long - time1Long;
        StringBuffer sbuffer = null;
        if (timeLong > 0) {
            int hour = (int) (timeLong / 3600);
            int minute = (int) ((timeLong - hour * 3600) / 60);
            int second = (int) ((timeLong - hour * 3600 - minute * 60) % 60);
            sbuffer = new StringBuffer();
            if (hour < 10) {
                sbuffer.append("0");
            }
            sbuffer.append(Integer.toString(hour));
            sbuffer.append(":");
            if (minute < 10) {
                sbuffer.append("0");
            }
            sbuffer.append(Integer.toString(minute));
            sbuffer.append(":");
            if (second < 10) {
                sbuffer.append("0");
            }
            sbuffer.append(Integer.toString(second));
            return sbuffer.toString();
        } else {
            System.out.println("时间不能为负数！可能原因是传入的时间位置不对!");
            // logger.error("时间不能为负数！可能原因是传入的时间位置不对!");
            return "";
        }
    }

    public static boolean checkfile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 获取视频总时间
     * 
     * @param viedo_path
     *            视频路径
     * @param ffmpeg_path
     *            ffmpeg路径
     * @return
     */
    public static int getVideoTime(String video_path, String ffmpeg_path) {
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpeg_path);
        commands.add("-i");
        commands.add(video_path);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            final Process p = builder.start();

            // 从输入流中读取视频信息
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            // 从视频信息中解析时长
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
            Pattern pattern = Pattern.compile(regexDuration);
            Matcher m = pattern.matcher(sb.toString());
            if (m.find()) {
                int time = getTimelen(m.group(1));
                System.out
                        .println(video_path + ",视频时长：" + time + ", 开始时间：" + m.group(2) + ",比特率：" + m.group(3) + "kb/s");
                return time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 格式:"00:00:10.68"
    private static int getTimelen(String timelen) {
        int second = 0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            second += Integer.valueOf(strs[0]) * 60 * 60;// 秒
        }
        if (strs[1].compareTo("0") > 0) {
            second += Integer.valueOf(strs[1]) * 60;
        }
        if (strs[2].compareTo("0") > 0) {
            second += Math.round(Float.valueOf(strs[2]));
        }
        return second;
    }
}
