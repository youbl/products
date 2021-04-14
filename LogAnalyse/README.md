# LogAnalyse
## 1、Nginx服务器每天自动截断日志，并生成压缩文件
## 2、通过rsync，每天从Nginx服务器日志目录下，同步日志文件到job机器
## 3、执行本程序，解析并载入到MySQL数据库