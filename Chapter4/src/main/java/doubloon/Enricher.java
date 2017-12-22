package doubloon;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.geoip.Location;
import doubloon.extractors.GeoIP;
//import doubloon.extractors.OpenExchange; 

import java.io.IOException;

import org.apache.kafka.clients.producer.*;

public class Enricher implements Producer {

  private final KafkaProducer<String, String> producer;
  private final String goodTopic;
  private final String badTopic;

  protected static final ObjectMapper MAPPER = new ObjectMapper();

  public Enricher(String servers, String goodTopic,
          String badTopic) {
    this.producer = new KafkaProducer(
            Producer.createConfig(servers));
    this.goodTopic = goodTopic;
    this.badTopic = badTopic;
  }

  @Override
  public void process(String message) {

    try {
      JsonNode root = MAPPER.readTree(message);
      JsonNode ipAddressNode = root.path("customer").path("ipAddress");

      if (ipAddressNode.isMissingNode()) {                               //1 
        Producer.write(this.producer, this.badTopic,
                "{\"error\": \"customer.ipAddress is missing\"}");
      } else {
        String ipAddress = ipAddressNode.textValue();

        Location location = new GeoIP().getLocation(ipAddress);          //2 
        ((ObjectNode) root).with("customer").put("country", location.countryName); //3 
        ((ObjectNode) root).with("customer").put("city", location.city);

        //OpenExchange oe = new OpenExchange();                            //2 
        //((ObjectNode) root).with("currency").put("rate", oe.getPrice("BTC"));   //3 
        Producer.write(this.producer, this.goodTopic,
                MAPPER.writeValueAsString(root));                        //4 
      }
    } catch (IOException e) {
      Producer.write(this.producer, this.badTopic, "{\"error\": \""
              + e.getClass().getSimpleName() + ": " + e.getMessage() + "\"}");
    }
  }

}
