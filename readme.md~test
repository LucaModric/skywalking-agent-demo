# skywalking-agent-demo
skywalking可插拔式架构实现原理Demo

## standalone-plugins
单独的插件模式，每个插件都需要使用-javaagent引入；mysql和springmvc是两个独立的agent,在app引用时，需要分别使用-javaagent引入。如-javaagent:路径/mysql.jar -javaagent:路径/springmvc.jar,中间空格分开。


##  apm-sniffer
可插拔式agent实现

### apm-agent
入口

### apm-agent-core
核心实现

### apm-plugins
可插拔的插件实现

## dist
 apm-sniffer打包后存放路径,手动将打好的插件jar包复制到plugins下，也可以用antrun插件实现，代码只设置了apm-agent.jar的复制到dist目录。

## app
一个简单的springboot项目，用于验证apm-sniffer和standalone-plugins的agent功能。

## 总结
代码源自[bytebuddy进阶实战-skywalking agent可插拔式架构实现](https://www.bilibili.com/video/BV1Jv4y1a7Kw/?vd_source=a1140fd3f4c8a495bd13a2a7c3d8da26)，主要学习作者对可插拔式插件实现的抽象思路，以及其中涉及到的基础知识，如类加载，JAR文件的操作，泛型，内部类等基础知识。可以对比下ja-netfilter的抽象，跟本demo的思路几乎是一样的。