package objectSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;

public class UserDeserializer implements Deserializer<UserDTO> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  @Override
  public UserDTO deserialize(String topic, byte[] bytes) {
    return objectMapper.readValue(bytes, UserDTO.class);
  }
}
