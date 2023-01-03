package objectSerializer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static poc.Base.GENERAL_TOPIC;
import static poc.Base.getBrokers;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ObjectConsumer {

  public static void main(String[] args) {

    //Please use [ObjectProducer]  as a producer for this consumer

    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS_CONFIG, getBrokers());
    props.setProperty("group.id", "second-consumer-group");
    props.setProperty("enable.auto.commit", "true");
    props.setProperty("auto.commit.interval.ms", "1000");
    props.setProperty(KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.IntegerDeserializer");
    props.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, "objectSerializer.UserDeserializer");

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
      consumer.subscribe(List.of(GENERAL_TOPIC));
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
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
