# StackBenTool
Stack benchmark toolkit

## Riak Write test
java -jar benchmark-version.jar 
 -bn,--bucketname <arg>   Riak bucket name  
 -bt,--buckettype <arg>   Riak bucket type  
 -fs,--size <arg>         File size in KB  
 -maxc,--maxconn <arg>    Riak max connection  
 -minc,--maxconn <arg>    Riak max connection  
 -rh,--riakhost <arg>     Riak hosts, exp: 127.0.0.1:8087 or  
                          192.168.1.10:8087,192.168.1.11:8087  
 -th,--thread <arg>       Concurrent threads  
 -tw,--write <arg>        Total writes  


### Example 
100k file write 5000 times with 50 threads, riak host is 127.0.0.1:8087 with bucket type "default" and bucket name 
"testname", you can experiment -maxc -minc to tune connections.
benchmark-0.1.jar -fs 100 -th 50 -tw 5000 -rh 127.0.0.1:8087 -bt default -bn testname
