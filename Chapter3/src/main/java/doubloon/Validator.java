package doubloon;

import java.io.IOException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Validator implements Producer {

    private final KafkaProducer<String, String> producer;
    private final String goodTopic;
    private final String badTopic;

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    public Validator(String servers, String goodTopic, String badTopic) { // 1
        this.producer = new KafkaProducer<String, String>(Producer.createConfig(servers));
        this.goodTopic = goodTopic;
        this.badTopic = badTopic;
    }

    @Override
    public void produce(String message) { //2
        ProducerRecord<String, String> pr = null;
        try {
            JsonNode root = MAPPER.readTree(message);
            String error = "";
            error = error.concat(validate(root, "event")); 
            error = error.concat(validate(root, "customer"));
            error = error.concat(validate(root, "currency"));
            error = error.concat(validate(root, "timestamp"));
            // TO_DO: implement for the inner children

            if (error.length() > 0) {
                pr = new ProducerRecord<String, String>(this.badTopic, "{\"error\": \" " + error + "\"}"); // 3
            } else {
                pr = new ProducerRecord<String, String>(this.goodTopic, MAPPER.writeValueAsString(root));// 4
            }
        } catch (IOException e) {
            pr = new ProducerRecord<String, String>(this.badTopic,
                    "{\"error\": \"" + e.getClass().getSimpleName() + ": " + e.getMessage() + "\"}"); // 5
        } finally {
            if (null != pr) {
                producer.send(pr);
            }
        }
    }

    private String validate(JsonNode root, String path) {
        if (!root.has(path)) {
            return path.concat(" is missing. ");
        }

        JsonNode node = root.path(path);
        if (node.isMissingNode()) {
            return path.concat(" is missing. ");
        }

        return "";
    }

}