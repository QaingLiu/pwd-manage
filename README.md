# 密码管理系统
基于SpringBoot2.x+Thymeleaf+layui开发

## 项目背景
由于目前信息化速度比较快，工作、生活中很多网站都需要注册才能使用，导致账号密码越来越多，管理很麻烦，经常忘记账号密码，所以自己动手写了这个简单的系统。能够新增、更新、删除信息，列表快速搜索功能，带有生成不同格式的随机密码。

## 启动项目
系统非常简单，也很小，直接编译就能正常运行：
1.进入系统目录：pwd-manage
2.maven打包：mvn clean install
3.运行：java -jar .\target\pwd-manage-1.0.0.jar

运行完会打印访问地址：
---------------------------------------------------------
	Application is running! Access address:
	Local:		http://localhost:10086
	External:	http://192.168.20.1:10086
---------------------------------------------------------

*说明：所有的数据保存在xml文件中，其中密码、邮箱、手机号、密保问题都是经过加密的*

## 预览效果
![首页](https://github.com/QaingLiu/pwd-manage/img/index.png)

![新增](https://github.com/QaingLiu/pwd-manage/img/add.png)

![生成随机密码](https://github.com/QaingLiu/pwd-manage/img/ramPwd.png)

