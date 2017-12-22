package doubloon;

import org.apache.kafka.clients.consumer.*;

public class Reader implements Consumer {

  private final KafkaConsumer<String, String> consumer;
  private final String topic;
  
  public Reader(String servers, String groupId, String topic) {
    this.consumer = new KafkaConsumer(Consumer.createConfig(servers, groupId));
    this.topic = topic;
  }

  public void run(Producer producer) {
    this.consumer.subscribe(java.util.Arrays.asList(this.topic));
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        producer.process(record.value());
      }
    }
  }

}
