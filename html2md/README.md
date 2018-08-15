## 目录
1. [csdn文章抓取](#csdn文章抓取)  
2. [cnblog文章抓取 ](#cnblog文章抓取)  
3. [csdn文章链接抓取](#csdn文章链接抓取)  
4. [使用jar包执行](#使用jar包执行)  

<h2 id="csdn文章抓取">csdn 文章抓取</h2> 

>   CSDN2mdService.java

```java
convertAllBlogByUserName("ricohzhanglong"); // 作者名
```

基础版已完成,根据作者抓取所有文章  
目前存在的问题
> 1、本地文件夹 TARGET_DIR 必须存在，未判空    
  2、文章标题特殊字符，会跳过，未做处理   
  3、对一些异常的处理，一次使用暂时凑合用吧  



<h2 id="cnblog文章抓取 ">cnblog 文章抓取 </h2> 

> CNBlog2mdService.java  

```java
convertAllBlogByUserName("lossingdawn"); // 作者名
```

基础版已完成,根据作者抓取所有文章   
目前存在的问题
> 1、本地文件夹 TARGET_DIR 必须存在，未判空   
  2、文章标题特殊字符，会跳过，未做处理  
  3、对一些异常的处理，一次使用暂时凑合用吧



<h2 id="csdn文章链接抓取">csdn 文章链接抓取</h2>   

> http://localhost:8002/tools/csdn4url   
  http://localhost:8002/tools/csdn4author  


<h2 id="使用jar包执行">使用jar包执行</h2> 

> jar包地址  -> [html2md-0.0.1-SNAPSHOT.jar](http://pab9ul5c4.bkt.clouddn.com/html2md-0.0.1-SNAPSHOT.jar)   
       在jar包所在位置，执行命令    
  java -jar html2md-0.0.1-SNAPSHOT.jar   
       访问地址  http://localhost:8002/tools/csdn4author  
  
![导出页面.png](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/15289690656019.png "导出页面") 
 
![运行日志.png](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/1528970063.png "运行日志") 
 
![导出日志.png](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/1528970083.png "导出日志") 
 
![特殊字符.png](https://github.com/Ruffianjiang/dawn-example/blob/master/html2md/img/1529025363.png "特殊字符不能导出的") 
 

参考地址：  
[https://github.com/vector4wang/blog-export-quick](https://github.com/vector4wang/blog-export-quick)

