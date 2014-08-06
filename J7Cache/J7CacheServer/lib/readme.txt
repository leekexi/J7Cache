1、启动J7CacheServer
Window：X:>J7CacheServer>start.bat
Linux：/J7CacheServer/start.sh

2、运行测试客户端
支持的SQL例子
String SQL_CREATE_TABLE = "CREATE TABLE Test";
String SQL_INSERT_INTO = "INSERT INTO Test(A,B,C) VALUES(1,ddssss,aaaa)";
String SQL_SELECT = "SELECT A,B,C FROM Test";
String SQL_UPDATE = "UPDATE Test SET B=111,C=eee WHERE A=1";
String SQL_DELETE = "DELETE FROM Test WHERE A=1";
String SQL_DROP_TABLE = "DROP TABLE Test";


运行客户端：
Window：X:>J7CacheServer>java -jar J7CacheTestClient.jar 127.0.0.1 7777

Linux：/J7CacheServer/java -jar J7CacheTestClient.jar 127.0.0.1 7777

客户端会读取配置文件（sql.properties）的SQL语句加以执行，可以变换

3、配置文件理解
server.properties

本经服务端口：LOCALPORT=7777
集群服务：CLUSTER=IP1:PORT,IP2:PORT