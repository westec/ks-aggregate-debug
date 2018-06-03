package debug.serde;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

public class EventArrayDeserializer implements Deserializer<ArrayList<String>> {
//  private Log log = LogFactory.getLog(EventArrayDeserializer.class);

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public ArrayList<String> deserialize(String topic, byte[] data) {
    ArrayList<String> result = null;
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(bis);
      result = (ArrayList<String>) ois.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        if(ois != null) {
          ois.close();
        }
        bis.close();
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

