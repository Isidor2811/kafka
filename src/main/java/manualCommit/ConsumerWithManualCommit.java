package manualCommit;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static poc.Base.GENERAL_TOPIC;
import static poc.Base.getBrokers;

import java.io.FileWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerWithManualCommit {

  @SneakyThrows
  public static void main(String[] args) {

    //Please use [StringProducer] as a producer for this consumer

    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS_CONFIG, getBrokers());
    props.setProperty("group.id", "third-consumer-group");
    props.setProperty(KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.IntegerDeserializer");
    props.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");

    final int maximumRecordBeforeCommit = 60;
    List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
    FileWriter fileWriter = new FileWriter("src/main/resources/files/animals.txt", true);

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
      consumer.subscribe(List.of(GENERAL_TOPIC));
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        //here we not write records to file immediately, we add them to buffer
        for (ConsumerRecord<String, String> record : records) {
          buffer.add(record);
          System.out.printf("Record %s was added to buffer\n", record.value());
        }

        if (buffer.size() >= maximumRecordBeforeCommit) {
          //write to file
          System.out.print("Writing to file");
          fileWriter.write(String.valueOf(buffer));
          consumer.commitSync();
          buffer.clear();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      fileWriter.close();
    }


  }

}
