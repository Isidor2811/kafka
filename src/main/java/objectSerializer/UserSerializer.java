package objectSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;

public class UserSerializer implements Serializer<UserDTO> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  @Override
  public byte[] serialize(String topic, UserDTO userDTO) {
    return objectMapper.writeValueAsBytes(userDTO);
  }
}
