## 目录
1. [图片转图片](#图片转图片)  
   [参数调整](参数调整)  
2. [gif转gif](#gif转gif)  
3. [视频转视频](#视频转视频)  


<h2 id="图片转图片">图片转图片</h2>  

> ImgUtil.java

![原图](img/head.png) ![转换后](img/head_copy.png)  

```java
    @Test
    public static void imgTest() {
        String inputFile = "F:/123/head.png";
        String outputFile = "F:/123/head_copy.png";
        // String base = "01"; // 替换的字符串
        String base = "@#&$%*o!;.";// 字符串由复杂到简单
        int threshold = 8;// 阈值
        ImgUtil.toTextImg(inputFile, outputFile, base, threshold);
    }
```

<h2 id="参数调整">具体参数调整如下图所示：</h2>  

![参数调整](img/paranCode.png)  

1. 调整字符大小，颜色    
2. 调整字符间距    
3. 调整字符的区域  （index数值越小，灰度越大）    


<h2 id="gif转gif">gif转gif</h2>  

>  GifUtil.java  

![原图](img/1.gif) ![转换后1](img/1_03.gif)  
![原图](img/123.gif) ![转换后1](img/123_03.gif) ![转换后2](img/123_04.gif)  

测试代码
```java
    @Test
    public static void gifTest() {
        String srcFile = "F:/123/123.gif";
        String targetFile = "F:/123/123_04.gif";
        String base = "01"; // 替换的字符串
        // String base = "@#&$%*o!;.";// 字符串由复杂到简单
        int threshold = 3;// 阈值
        GifUtil.toTextGif(srcFile, targetFile, base, threshold);
    }
```



<h2 id="视频转视频">视频转视频</h2> 

> FfmpegUtil.java\VideoUtil.java
没做细致的调整，视频的转换可以调调参数

<video src="http://pab9ul5c4.bkt.clouddn.com/123.mp4" width="320" height="240" controls="controls">
Your browser does not support the video tag.
</video>
<video src="http://pab9ul5c4.bkt.clouddn.com/1234.mp4" width="320" height="240" controls="controls">
Your browser does not support the video tag.
</video>
<iframe height=498 width=510 src="http://pab9ul5c4.bkt.clouddn.com/1234.mp4" frameborder=0 allowfullscreen></iframe>

测试代码
```java
    @Test
    public static void videoTest() {
        String srcVideoPath = "F:/123/123.mp4";
        String tarImagePath = "F:/123/mp/";
        String tarAudioPath = "F:/123/mp/audio.aac";
        String tarVideoPath = "F:/123/1234.mp4";
        VideoUtil.readVideo(srcVideoPath,tarImagePath,tarAudioPath,tarVideoPath);
    }
```
