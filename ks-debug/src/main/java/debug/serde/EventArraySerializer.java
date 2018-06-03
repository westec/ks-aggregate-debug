package debug.serde;


import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

public class EventArraySerializer implements Serializer<ArrayList<String>> {
  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public byte[] serialize(String topic, ArrayList<String> data) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = null;
    byte[] result = null;
    try {
      oos = new ObjectOutputStream(bos);
      oos.writeObject(data);
      result = bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if(oos == null) {
          oos.close();
        }
        bos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  @Override
  public void close() {

  }
}

