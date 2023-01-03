# How to start manually (send and receive massages from command line)?

1. Download kafka https://kafka.apache.org/downloads and unzip archive somewhere
2. Go to the unzipped folder
3. Start zookeeper:<br />
   `bin/zookeeper-server-start.sh config/zookeeper.properties`
4. Start broker (s). For this example we start 3 brokers<br />
   `bin/kafka-server-start.sh config/server.properties`<br />
   Please note that if we need to start more than one broker we need to create file with
   configuration for each broker.<br />
   Inside these config files we should set unique `broker.id`, port for
   listener `listeners=PLAINTEXT://:9092` and path where logs will be
   stored `log.dirs=/tmp/kafka-logs-0`
5. Create topic <br />

```
bin/kafka-topics.sh \
   --bootstrap-server localhost:9092,localhost:9093,localhost:9093 \
   --create \
   --replication-factor 1 \
   --partitions 3 \
   --topic animals
```

Where:

* `topic` - special entity where all messages store on broker. Every topic should have unique name
  for kafka cluster
* `bootstrap-server localhost:9092,localhost:9093,localhost:9093` - list of brokers
* `replication-factor` - how menu copy of partitions will be created on others (not lead) brokers. 1
  means that there is no any replication exists. For example if we have 7 partitions and 3 brokers
  with replication-factor 3, that is mean that every partition will be duplicated 3 times 7*3=21 So
  all this 21 partitions should be divided by all brokers, so 21/3=7. Every broker will contain 7
  partitions.
* `partitions` - represent real folder with log file on hard drive where producers write new
  messages and consumers read them. So in case above we have 3 partitions and 3 brokers what is mean
  that in each broker will be 1 partition (that is how we can reduce read/write operations on single
  server if all partitions are in one broker)

5. See the list of topics:

```
bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--list
```

6. See topic details

```
bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--describe \
--topic animals
```

7. Start producer

```
bin/kafka-console-producer.sh \
--broker-list localhost:9092 \
--topic animals
```

8. Start consumer

```
bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic test
```

**After that you will be able to generate massages with producer and see same massages in consumer**

9. Additionally, you can start consumer with parameter:

* `--from-beginning` - read all messages from the very beginning
* `--partition 1` - read from specific partition
* `--offset 0` - read from offset. Offset this is unique message number inside partition

# How to use Java for crating producers and consumers?

1. You need to start `zookeeper`, `broker` and create `topic` with name `general`, so just do steps
   3-5 from first section
2. Inside `simpleString` package you can run a consumer with auto-commit (`ConsumerWithAutoCommit`).
   Producer for this consumer is `StringProducer`.
3. To understand how to create own serializer/deserializer please check `objectSerializer` package
4. To understand how manual commit work - go to `manualCommit` package
5. And finally you can assign consumer to read from specific partition `assignSpecificPartition`
   package

To remove all connected to kafka you need to run following command <br />
`$ rm -rf /tmp/kafka-logs /tmp/zookeeper /tmp/kraft-combined-logs`<br />
Also you need to remove kafka folder and unzip it again