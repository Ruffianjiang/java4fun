## csdn 文章抓取 
>   CSDN2mdService.java
```
convertAllBlogByUserName("ricohzhanglong"); // 作者名
```

基础版已完成,根据作者抓取所有文章 <br/>
目前存在的问题
> 1、本地文件夹 TARGET_DIR 必须存在，未判空  <br/>
  2、文章标题特殊字符，会跳过，未做处理 <br/>
  3、对一些异常的处理，一次使用暂时凑合用吧

## cnblog 文章抓取 
>   CNBlog2mdService.java
```
convertAllBlogByUserName("lossingdawn"); // 作者名
```

基础版已完成,根据作者抓取所有文章 <br/>
目前存在的问题
> 1、本地文件夹 TARGET_DIR 必须存在，未判空 <br/>
  2、文章标题特殊字符，会跳过，未做处理 <br/>
  3、对一些异常的处理，一次使用暂时凑合用吧

## csdn 文章链接抓取
> http://localhost:8002/tools/csdn4url <br/>
  http://localhost:8002/tools/csdn4author

## 使用jar包执行
> jar包地址  -> [html2md-0.0.1-SNAPSHOT.jar](http://pab9ul5c4.bkt.clouddn.com/html2md-0.0.1-SNAPSHOT.jar) <br/> 
       在jar包所在位置，执行命令  <br/>
  java -jar html2md-0.0.1-SNAPSHOT.jar <br/>
       访问地址  http://localhost:8002/tools/csdn4author 
  
![15289690656019.png](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/15289690656019.png "导出页面")
![1528970063.jpg](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/1528970063.jpg "运行日志")
![1528970083.jpg](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/1528970083.jpg "导出日志")
![1529025363.jpg](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/1529025363.jpg "特殊字符不能导出的")

参考地址：
https://github.com/vector4wang/blog-export-quick

