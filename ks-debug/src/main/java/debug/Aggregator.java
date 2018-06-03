package debug;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import debug.serde.EventArraySerde;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class Aggregator {
  private static final Logger log = LoggerFactory.getLogger(Aggregator.class);

  public static void main(String[] args) throws CertificateException, NoSuchAlgorithmException,
    KeyStoreException, IOException, URISyntaxException {

    final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";
    final Properties streamsConfiguration = new Properties();
    streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "ks-debug-app");
    streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "ks-debug-client");
    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
    streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

    final Serde<String> stringSerde = Serdes.String();
    final Serde<ArrayList<String>> arraySerde = new EventArraySerde();

    final StreamsBuilder builder = new StreamsBuilder();

    final KStream<String, String> events = builder.stream("ks-debug-input");

    events
      .map((k, v) -> KeyValue.pair(v.split(",")[1], v.split(",")[0]))
      .groupByKey()
      .windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis(10)))
      .aggregate(
        ArrayList::new,
        (type, id, eventList) -> {
          eventList.add(id);
          return eventList;
        },
        Materialized.with(stringSerde, arraySerde)
      )
      .toStream((k,v) -> k.key())
      .mapValues((v)-> String.join(",", v))
      .to("ks-debug-output", Produced.with(stringSerde, stringSerde));

    final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);

    streams.cleanUp();
    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

}
