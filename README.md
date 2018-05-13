# NiceJavaFramework
自己封装的java类，高内聚低耦合的工具类，帮助类，ui组件类


## sql
- ***[SqlHelper.java](https://github.com/ismyblue/NiceJavaFramework/blob/master/sql/SqlHelper.java)*** 数据库静态操作类，免实例化操作数据库，支持事务，支持多线程，连接池。实现了增删改查，查询后返回与数据库数据类型匹配的对象数组列表。
- ***[dbinfo.properties](https://github.com/ismyblue/NiceJavaFramework/blob/master/sql/dbinfo.properties)*** SqlHelper.java的配置文件，配置数据库驱动字段，连接url，用户名，密码，连接池的连接数。

## xml
-------------
- ***[Book.xml](https://github.com/ismyblue/NiceJavaFramework/blob/master/xml/Book.xml)***  xml文件，操作的样本

-----------------
- ***jaxp方式:***
- ***[XmlDomHelper.java](https://github.com/ismyblue/NiceJavaFramework/blob/master/xml/XmlDomHelper.java)*** java标准库Jaxp的DOM方法读写xml封装类。可以一句话获得xml文档对象，一句话更新xml文件。
- ***[XmlSaxHelper.java](https://github.com/ismyblue/NiceJavaFramework/blob/master/xml/XmlSaxHelper.java)***  java标准库jaxp的Sax方法读取xml封装类。配合xml实体的处理器类去使用(sax解析器触发事件，xml实体的处理器就会做相关数据处理)。
- ***[BookSaxHandler.java](https://github.com/ismyblue/NiceJavaFramework/blob/master/xml/BookSaxHandler.java)*** xml中Book实体的处理器类，继承自DefaultHandler. 每个实体类都要单独继承这个Defaulthandler类,实现各自的处理方法。要解析一个xml文件时,将这个处理器类交给saxParser，当saxParser扫描xml文件时，触发相关事件，会使用这个实体处理器类去处理数据。

-----------------
- ***dom4j方式(推荐使用):适用于增删改查***
- ***[dom4j-2.1.0.jar](https://github.com/ismyblue/NiceJavaFramework/blob/master/xml/dom4j-2.1.0.jar)***  这是一个开源的xml解析第三方类库。结合了jaxp的优点，以sax方式读取文件，生成dom树，解析方便。并且可以以友好的方式格式化输出xml文档对象，美观的写入xml文件，同时也能紧凑的格式写入xml文件。
这是项目的[主页](https://github.com/dom4j/dom4j)
[快速开始](https://github.com/dom4j/dom4j/wiki/Quick-Start-Guide)
[dom4j的博客](https://dom4j.github.io/)

-----------------
- ***XPath方式(推荐使用):适用于快速查询***
- ***[jaxen-1.1.6.jar](https://github.com/ismyblue/NiceJavaFramework/blob/master/xml/jaxen-1.1.6.jar)***  XPath是是一门在XML 文档中查找信息的语言。XPath 用于在XML 文档中通过元素和属性进行导航。XPath 使用路径表达式在XML 文档中进行导航.类比XPath查询xml节点，就像sql查询数据库一样。在dom4j中使用XPath需要引入这个Jaxen的jar包。[jaxen的主页](https://github.com/jaxen-xpath/jaxen) 以及 [XPath tutorial](http://zvon.org/xxl/XPathTutorial/General/examples.html)

## fileio
- ***[FileUploadUtil.java]()*** 文件上传工具类，一句话接受浏览器上传文件到指定目录（jar包所需:commons-fileupload-1.3.3.jar,commons-io-2.6.jar)
- ***[FileDownloadUtil.java]()*** 文件下载工具类，一句话下载webapp指定目录的文件到浏览器

> jar包

- ***[commons-fileupload-1.3.3.jar]()*** FileUploadUtil.java运行的核心jar包
- ***[commons-io-2.6.jar]()*** FileUploadUtil.java运行的依赖jar包 

### ***注意：***
各文件夹下的Project里面是各个封装的类的使用工程，测试工程
