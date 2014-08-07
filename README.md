J7Cache
=======
## Abstract
J7Cache is a high-performance in-memory database, support classes SQL, support for clusters, support for persistent data, suitable for high frequency applications and the small amount of data, such as auction systems, telecommunications pricing systems, shopping carts, order systems, online gaming platforms, stock real-time platform, the member center, log system, etc.



##How to use

### First Step
Understanding the configuration file "server.properties"
```
**DataBase IP
IP=127.0.0.1

**Listening port
LOCALPORT=7777

**user name 
**In the server's console by executing #> add user username = password
USERNAME=ROOT

**user password md5 
PASSWORD=e10adc3949ba59abbe56e057f20f883e

**Cluster configuration, multiple servers separated by commas
CLUSTER=127.0.0.1:7777,127.0.0.1:8888,127.0.0.1:9999

** Set whether to open the synchronized data to the hard disk, 1 sync 
HDSYNC = 1 

**Set the memory and hard drive data synchronization event interval in seconds 
HDSYNC_TIME = 30 

** Set the hard disk data storage location, according to the different Windows and Linux
HDSYNC_DATA_DIR = /usr/datas/j7cache/datas
```

### The second step
Through the console or JDBC, execute create table "CREATE TABLE XXXX"
```
**Start J7CacheServer
Window X:>J7CacheServer>start.bat
Linux >/J7CacheServer/start.sh
```

**Through the console or JDBC, execute create table "CREATE TABLE XXXX"

**Supported SQL example, pay attention to keywords in uppercase,
WHERE conditions currently supports only one condition, and must
be the primary key, you can simultaneously query multiple values.

```
String SQL_INSERT_INTO = "INSERT INTO Test(A,B,C)
VALUES(1,ddssss,aaaa)";
String SQL_SELECT = "SELECT A,B,C FROM Test";
String SQL_UPDATE = "UPDATE Test SET B=111,C=eee WHERE A=1,2,3";
String SQL_DELETE = "DELETE FROM Test WHERE A=1";
String SQL_DROP_TABLE = "DROP TABLE Test";
```

##Console instruction
**Exit
```
Window: X:> bye!
Linux:> bye!
```
**execute SQL statements, SQL statements according to the second point of writing standards
