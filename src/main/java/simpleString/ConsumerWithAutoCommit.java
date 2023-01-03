package simpleString;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static poc.Base.GENERAL_TOPIC;
import static poc.Base.getBrokers;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerWithAutoCommit {

  public static void main(String[] args) {

    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS_CONFIG, getBrokers());
    props.setProperty("group.id", "first-consumer-group");
    //here we use auto-commit every second
    props.setProperty("enable.auto.commit", "true");
    props.setProperty("auto.commit.interval.ms", "1000");
    props.setProperty(KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.IntegerDeserializer");
    props.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
      //subscribe to topic
      consumer.subscribe(List.of(GENERAL_TOPIC));
      while (true) {
        //every 100ms ask broker for new messages
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        //if some new messages exist - print them
        for (ConsumerRecord<String, String> record : records) {
          System.out.printf("offset = %d, key = %s, value = %s, partition = %s%n",
              record.offset(), record.key(), record.value(), record.partition());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

}
