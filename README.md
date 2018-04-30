# NiceJavaFramework
自己封装的java类，高内聚低耦合的工具类，帮助类，ui组件类


## sql
- ***[SqlHelper.java](https://github.com/ismyblue/NiceJavaFramework/blob/master/sql/SqlHelper.java)*** 数据库静态操作类，免实例化操作数据库，支持事务，支持多线程，连接池。实现了增删改查，查询后返回与数据库数据类型匹配的对象数组列表。
- ***[dbinfo.properties](https://github.com/ismyblue/NiceJavaFramework/blob/master/sql/dbinfo.properties)*** SqlHelper.java的配置文件，配置数据库驱动字段，连接url，用户名，密码，连接池的连接数。

## xml
- ***[XmlDomHelper.java](https://github.com/ismyblue/NiceJavaFramework/blob/master/xml/XmlDomHelper.java)*** xml文件读取帮助类。java标准库Jaxp的DOM方法读写xml文件的一个封装类。可以一句话获得xml文档对象，一句话更新xml文件。

### ***注意：***
各文件夹下的Project里面是各个封装的类的使用工程，测试工程
