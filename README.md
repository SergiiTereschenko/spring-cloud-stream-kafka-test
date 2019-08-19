https://www.sohamkamani.com/blog/2017/11/22/how-to-install-and-run-kafka/

1. Download kafka
2. Go to kafka folder
3. Run zookeeper<br>
**bin/zookeeper-server-start.sh config/zookeeper.properties**
4. Create 2 more properties<br>
**cp config/server.properties config/server1.properties**<br>
**cp config/server.properties config/server2.properties**
5. Change server1.properties<br> 
**broker.id=1<br>
listeners=PLAINTEXT://:9093<br>
log.dirs=/tmp/kafka-logs-1**
6. Change server2.properties<br>
**broker.id=2<br>
listeners=PLAINTEXT://:9094<br>
log.dirs=/tmp/kafka-logs-2**
7. Run 3 brokers<br>
**bin/kafka-server-start.sh config/server.properties<br>
bin/kafka-server-start.sh config/server1.properties<br>
bin/kafka-server-start.sh config/server2.properties**
8. Create topic t_messages<br>
**bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 2 --topic t_messages**
9. [Optional] Check topic<br>
**bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic t_messages**<br>
  9.1) Run Kafka-consumer spring boot service  (in order to consumer group be created)
10. [Optional] Check consumer group<br>
**bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group  stream-listener-group  --members --verbose**
11. [Optional] Reset offsets<br>
**bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --reset-offsets --to-earliest --topic t_messages --group  stream-listener-group --execute**

12. **To test in console:**<br>
_Run:_  
 bin/kafka-console-producer.sh --broker-list localhost:9093,localhost:9094 --topic t_messages<br>
_Run:_  
 bin/kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic t_messages --from-beginning
