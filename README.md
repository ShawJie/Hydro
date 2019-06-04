# Hydro
[English]() <br><br>
[JDK | 11](https://jdk.java.net/11/) &nbsp;&nbsp;&nbsp;&nbsp;
[Lince | MIT]() &nbsp;&nbsp;&nbsp;&nbsp;
[</> | Shaw](https://github.com/ShawJie) &nbsp;&nbsp;&nbsp;&nbsp;

> 一个简单一点的Java动态博客系统

为什么要选择Hydro：
  - 不需要系统之外的运行环境，尽量保持部署环境的干净整洁
  - 系统安装简洁方便，只需要一行命令完成部署/关闭操作
  - 主题定制自由度高，纯HTML编写，无需其他学习成本，路由自定义
  
## 环境说明

- 部署环境要求：
  - JDK11
  - Mysql
- 可部署平台：
  - Windows
  - Linux
  
## 安装方法
- Windows：(请勿使用Windows PowerShell执行命令，否则会出现项目无法运行的情况)
  - 下载[Hydro.zip]() 并解压到任意文件夹
  - 下载[hydro-cli]() 放置在项目根目录
  - 在Mysql下创建一个数据库
  - 编辑项目根目录的`hydro-config.yaml`文件，补充数据库信息（host/端口/数据库名/数据库用户名/数据库密码）并保存
  - 在项目根目录执行执行 `hydro-cli -i` 命令以运行系统
  - 浏览器访问 域名[:端口]/initial 进行用户初始化操作
  - 若要关闭项目则在项目根目录执行 `hydro-cli -s` 命令
- Linux：(请勿使用管理员权限进行以下操作，否则可能无法进行更新操作)
  - 下载[Hydro.tar.gz]() 并解压到任意文件夹
  - 下载[hydro-cli]() 放置在项目根目录
  - 在Mysql下创建一个数据库
  - 编辑项目根目录的`hydro-config.yaml`文件，补充数据库信息（host/端口/数据库名/数据库用户名/数据库密码）并保存
  - 在项目根目录执行 `/bin/bash hydro-cli.sh -i` 命令以运行系统
    - 若是通过SSH连接linux的使用者可以参考[Screen](https://www.ibm.com/developerworks/cn/linux/l-cn-screen/index.html)命令以保持在SSH关闭连接后项目能持续运行
  - 浏览器访问 域名[:端口]/initial 进行用户初始化操作
  - 若要关闭项目则在项目根目录执行 `/bin/bash hydro-cli -s` 命令
  
## 主题开发
> hydro的主题系统相对于其他博客系统会比较特殊，您可以按照您的开发习惯进行开发，只需要在主题根目录放置一个配置文件告知服务器页面的地址以及一些自定义配置即可

**示例：<br>**
>以下是Hydro系统默认主题Semantic的目录结构 <br>
- Semantic /* 根文件夹 */
  - image
    - ...一些图片文件资源
  - pages
    - archives.html
    - postDetail.html
    - posts.html
    - wrapper.html
    - ...
  - source
    - css
      - ...一些样式文件资源
    - js
      - ...一些页面脚本文件资源
  - index.html /* 主题主页 */
  - SemanticPreview.jpg /* 主题预览图 */
  - theme.properties /* 主题配置文件 */
  
**theme.properties 具体内容**
 
```properties
## 带*号为必填项
Name = Semantic UI Style ## 主题名称 *
Creator = Irene ## 主题作者 *
Description = The Hydro blob theme with Semantic UI ## 主题描述 *
Rendering = SemanticStyle.jpg ## 主题预览图 *

wrapper = pages/wrapper ## 用户自定义页面外部框架

## 以下为路由配置模块 
## Route.为前缀 后跟实际请求地址 例如网站域名为www.hydro.com 那么index页面的访问地址则为 www.hydro.com/hydro
## 而属性值为页面在主题内的相对地址
## 目前系统只支持单级路由地址
## 特殊：Route.post.detail 在用户访问 "域名/post/{文章id}" 会跳转到pages/postDetail页面
Route.index = index
Route.post.detail = pages/postDetail
Route.posts = pages/posts
Route.archives = pages/archives

## 以下为自定义属性 以Option.为前缀 后跟自定义属性名
## 属性值为JSON格式，textName为显示的配置名，value为配置的值（可为空，由用户修改）
## 页面中可以通过给html 标签附加属性 <html xmlns:th="http://www.thymeleaf.org">
## 之后在页面中通过 "th:属性名='${option.自定义属性名}'"进行取值配置
Option.Index_Background = {"textName": "首页背景图", "value": "image/cactus.jpg"}
Option.About_Link = {"textName": "关于我页面链接", "value": "/sample-page"}

## 例如页面中可通过 <img th:src="${option.Index_Background}">进行设值注入

```

## 页面预览

![系统主页](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/home.jpg)
<div align="center">主页</div>

![登陆界面](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_login.png)
<div align="center">登陆</div>

![仪表盘](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_dashboard.png)
<div align="center">仪表盘</div>

![文章列表](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_articles.png)
<div align="center">文章列表</div>

![媒体](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_media.png)
<div align="center">媒体</div>

![主题](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_theme.png)
<div align="center">主题列表</div>

![系统设置](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_setting.png)
<div align="center">系统设置</div>

![系统更新](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_update.png)
<div align="center">系统更新</div>
