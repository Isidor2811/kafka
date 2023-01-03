package objectSerializer;

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

public class ObjectProducer {

  private static final Faker faker = new Faker();

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, getBrokers());
    props.put(KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.IntegerSerializer");
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, "objectSerializer.UserSerializer");
    props.put(ACKS_CONFIG, "all");
    props.put(CLIENT_ID_CONFIG, GENERAL_PRODUCER);

    //send messages from producer to broker
    try (Producer<Integer, UserDTO> producer = new KafkaProducer<>(props)) {
      int numOfRecords = 50;
      for (int i = 0; i < numOfRecords; i++) {

        //create user model
        UserDTO user = UserDTO.builder()
            .id(faker.random().nextInt(1, 500))
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .job(faker.job().position())
            .build();

        //send to broker
        producer.send(new ProducerRecord<>(GENERAL_TOPIC, i, user)); //topic, key, value
        System.out.printf("New message [%s] was successfully sent\n", user);
        Thread.sleep(300);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
