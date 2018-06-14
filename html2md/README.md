## csdn 文章抓取 
>   CSDN2mdService.java
```
convertAllBlogByUserName("ricohzhanglong"); // 作者名
```

基础版已完成,根据作者抓取所有文章
目前存在的问题
> 1、本地文件夹 TARGET_DIR 必须存在，未判空  <br/>
  2、文章标题特殊字符，会跳过，未做处理 <br/>
  3、对一些异常的处理，一次使用暂时凑合用吧

## cnblog 文章抓取 
>   CNBlog2mdService.java
```
convertAllBlogByUserName("lossingdawn"); // 作者名
```

基础版已完成,根据作者抓取所有文章
目前存在的问题
> 1、本地文件夹 TARGET_DIR 必须存在，未判空 <br/>
  2、文章标题特殊字符，会跳过，未做处理 <br/>
  3、对一些异常的处理，一次使用暂时凑合用吧

## csdn 文章链接抓取
> http://localhost:8002/tools/csdn4url <br/>
  http://localhost:8002/tools/csdn4author

注：csdn单文章抓取未做修改

参考地址：
https://github.com/vector4wang/blog-export-quick

