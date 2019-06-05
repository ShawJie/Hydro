<div align="center">
  <img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/hydro-icon.png" width="200"/> 
  <br>
  <a href="https://jdk.java.net/11" target="_blank">
    <img src="https://img.shields.io/badge/JDK-11-yellow.svg">
  </a>
  <a href="https://github.com/ShawJie/Hydro/blob/master/LICENSE" target="_blank">
    <img src="https://img.shields.io/badge/licence-GPL-red.svg">
  </a>
  <a href="https://github.com/ShawJie" target="_blank">
    <img src="https://img.shields.io/badge/%3C%2F%3E%20%E2%99%A5-Shaw-blue.svg">
  </a>
</div>

<br>

> A simple dyamic blog system by Java

Why choose Hydro：
  - dosen't need other environment except system require，try to keep the deployment environment clean and tidy
  - system installation is simple，only one line of commands to start or shutdown system
  - Highly customizable theme, pure HTML, customizable route
  
## Environmental description

- deployment environment require：
  - JDK11
  - Mysql
- deployment platform：
  - Windows
  - Linux
  
## How to install
- Windows：(Don't use Windows PowerShell to execute commands)
  - Download [hydro.zip](https://github.com/ShawJie/Hydro-Release/blob/master/hydro-lastest-full.zip?raw=true) and decompress to any folder
  - Download [hydro-cli](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/boot/hydro-cli.bat) place in project Root directory
  - Create a database in Mysql
  - Edit the `hydro-config.yaml` in project root directory，fill database info and server info（host/port/DatabaseName/DatabaseUserName/DatabasePassword/ServerPort）And save
  - Execute command `hydro-cli -i` in project root directory to start project
  - Browser access "domain[:prot]/initial" to initial blog user info
  - Execute command `hydro-cli -s` in project root directory to shutdown project
- Linux：(Don't use administrator privileges to do the following, or you may not be able to update)
  - Download [hydro.zip](https://github.com/ShawJie/Hydro-Release/blob/master/hydro-lastest-full.zip?raw=true) and decompress to any folder
  - Download [hydro-cli](https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/boot/hydro-cli.sh) place in project Root directory
  - Create a database in Mysql
  - Edit the `hydro-config.yaml`in project root directory，fill database info and server info（host/port/DatabaseName/DatabaseUserName/DatabasePassword/ServerPort）And save
  - Execute command `/bin/bash hydro-cli.sh -i` in project root directory to start project
    - If you are connecting to Linux via SSH, you can refer to [Screen](https://www.ibm.com/developerworks/cn/linux/l-cn-screen/index.html) command to keep project running after close SSH connection
  - Browser access "domain[:port]/initial" to initial blog user info
  - Execute command `/bin/bash hydro-cli -s` in project root directory to shutdown project
  
## Theme development
> hydro's system has something different with other blog system, you can develop according to your development habits, just need place a config file in theme root directory to tell system the theme page path and custom option

**Example：<br>**
> The following is the directory structure of the Hydro system default theme "Semantic"  <br>
- Semantic /* Root directory */
  - image
    - ...Some image file
  - pages
    - archives.html
    - postDetail.html
    - posts.html
    - wrapper.html
    - ...
  - source
    - css
      - ...Some css file
    - js
      - ...Some js file
  - index.html /* Theme main page */
  - SemanticPreview.jpg /* Theme preview image */
  - theme.properties /* Theme config file */
  
**theme.properties Detail content**
 
```properties
## Fields marked with * are required
Name = Semantic UI Style ## Theme Name *
Creator = Irene ## Theme Creator *
Description = The Hydro blob theme with Semantic UI ## Theme description *
Rendering = SemanticStyle.jpg ## Theme preview image *

wrapper = pages/wrapper ## the wrapper with user custom page *

## the following is route config module
## Route. is prefix suffix is requesit address, Example: is domain is "www.hydro.com" then "www.hydro.com/index" is the index page request address
## the property value is the page file in theme relative path
## Currently, the system only supports single-level routing addresses.
## Special：Route.post.detail when user request "domain/post/{Articleid}" will jump to pages/postDetail
Route.index = index  ## index page is reuqire * 
Route.post.detail = pages/postDetail ## article detail is require * 
Route.posts = pages/posts
Route.archives = pages/archives

## The following is custom option, Option. is prefix, suffix is custom option name
## property value is a JSON String，textName is dieplay option name in theme config page，value is the option value（can be empty）
## in page, can use <html xmlns:th="http://www.thymeleaf.org">
## then use "th:atturibute='${option.customOptionName}'" to set value
Option.Index_Background = {"textName": "Index Banner", "value": "image/cactus.jpg"}
Option.About_Link = {"textName": "Aboute me Link", "value": "/sample-page"}

## Example: <img th:src="${option.Index_Background}">
## Other usage can ref "thymeleaf"

```

## System Preview

<div align="center">Home</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/home.jpg">

<div align="center">Login</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_login.png">

<div align="center">Dashboard</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_dashboard.png">

<div align="center">Article List</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_articles.png">

<div align="center">Media</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_media.png">

<div align="center">Themes</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_theme.png">

<div align="center">Setting</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_setting.png">

<div align="center">System update</div>
<img src="https://raw.githubusercontent.com/ShawJie/Hydro-Release/master/Images/sys_update.png">

