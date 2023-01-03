package simpleString;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static poc.Base.GENERAL_PRODUCER;
import static poc.Base.GENERAL_TOPIC;
import static poc.Base.getBrokers;

import com.github.javafaker.Faker;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class StringProducer {

  private static final Faker faker = new Faker();

  public static void main(String[] args) {

    //add properties for a producer like brokers' ip, and key-value serializers
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, getBrokers());
    //messages in the brokers is sequence of bytes
    //to know how this sequence should be encoded we need to have serializer on producer
    //and deserializer for decoding on Consumer
    props.put(KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.IntegerSerializer");
    props.put(VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    //make leader to wait while all followers will be ready to get copy of message before send it
    props.put(ACKS_CONFIG, "all");
    props.put(CLIENT_ID_CONFIG, GENERAL_PRODUCER);

    //send messages from producer to broker
    try (Producer<Integer, String> producer = new KafkaProducer<>(props)) {
      int numOfRecords = 50;
      for (int i = 0; i < numOfRecords; i++) {
        String messageValue = i + "_" + faker.animal().name();
        producer.send(new ProducerRecord<>(GENERAL_TOPIC, i, messageValue)); //topic, key, value
        System.out.printf("New message [%s] was successfully sent\n", messageValue);
        Thread.sleep(300);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
