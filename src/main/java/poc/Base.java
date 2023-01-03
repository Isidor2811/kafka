package poc;

import java.util.Arrays;
import java.util.List;

public class Base {

  //topic should be created before start this code
  public static final String GENERAL_TOPIC = "general";
  public static final String GENERAL_PRODUCER = "general-producer";
  private static final List<String> brokers = Arrays.asList(
      "localhost:9092"
//      "localhost:9093",
//      "localhost:9094"
  );

  public static String getBrokers() {
    return String.join(",", brokers);
  }
}
