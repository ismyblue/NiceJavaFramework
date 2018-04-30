# NiceJavaFramework
自己封装的java类，高内聚低耦合的工具类，帮助类，ui组件类

```
sql--------
|SqlHelper.java //数据库静态操作类，免实例化操作数据库，支持事务，支持多线程，连接池。
                //实现了增删改查，查询后返回 与数据库数据类型匹配的对象数组列表。
|dbinfo.properties  //SqlHelper.java的配置文件，配置数据库驱动字段，连接url，用户名，密码，连接池的连接数。
```
