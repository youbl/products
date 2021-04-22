# LogAnalyse
## 1、Nginx服务器每天自动截断日志，并生成压缩文件
## 2、通过rsync，每天从Nginx服务器日志目录下，同步日志文件到job机器
## 3、执行本程序，解析并载入到MySQL数据库

```
nginx日志格式定义：
log_format  main  '[$time_local] $remote_addr $remote_user '
                  '$host "$request" '
                  '[$status] $request_length $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for" "$upstream_addr" '
                  '[$request_time] [$upstream_response_time] [$upstream_status] '
                  '$content_length $http_content_length $sent_http_content_length';
```