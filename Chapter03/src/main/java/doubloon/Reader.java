package doubloon;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Reader implements Consumer {

    private final KafkaConsumer<String, String> consumer; // 1
    private final String topic;

    public Reader(String servers, String groupId, String topic) {
        this.consumer = new KafkaConsumer<String, String>(Consumer.createConfig(servers, groupId));
        this.topic = topic;
    }

    @Override
    public ConsumerRecords<String, String> consume() {
        this.consumer.subscribe(java.util.Arrays.asList(this.topic)); // 2
        ConsumerRecords<String, String> records = consumer.poll(100); // 3
        return records;
    }
}