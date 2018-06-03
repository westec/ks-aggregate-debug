package debug.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.ArrayList;
import java.util.Map;

public final class EventArraySerde implements Serde<ArrayList<String>> {
  @Override
  public void configure(Map<String, ?> configs, boolean isKey) { }

  @Override
  public void close() { }

  @Override
  public Serializer<ArrayList<String>> serializer() {
    return new EventArraySerializer();
  }

  @Override
  public Deserializer<ArrayList<String>> deserializer() {
    return new EventArrayDeserializer();
  }
}

