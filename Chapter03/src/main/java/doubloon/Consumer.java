package doubloon;

import java.util.Properties;

public interface Consumer {

  public static Properties createConfig(String servers, String groupId) {
    Properties props = new Properties();
    props.put("bootstrap.servers", servers);
    props.put("group.id", groupId);
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("auto.offset.reset", "earliest");
    props.put("session.timeout.ms", "30000");
    props.put("key.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
    return props;
  }

  public ConsumerRecords<String, String> consume();
  
}
