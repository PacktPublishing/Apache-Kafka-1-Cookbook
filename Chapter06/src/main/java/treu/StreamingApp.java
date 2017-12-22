package treu; 
 
import org.apache.kafka.streams.StreamsBuilder; 
import org.apache.kafka.streams.Topology; 
 
import org.apache.kafka.streams.KafkaStreams; 
import org.apache.kafka.streams.StreamsConfig; 
 
import org.apache.kafka.streams.kstream.KStream; 
 
import java.util.Properties; 
 
public class StreamingApp { 
 
  public static void main(String[] args) throws Exception { 
 
    Properties props = new Properties(); 
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streaming_app_id");// 1 
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //2 
 
    StreamsConfig config = new StreamsConfig(props); // 3 
    StreamsBuilder builder = new StreamsBuilder(); //4 
 
    Topology topology = builder.build(); 
 
    KafkaStreams streams = new KafkaStreams(topology, config); 
 
    KStream<String, String> simpleFirstStream = builder.stream("src-topic"); //5 
 
    KStream<String, String> upperCasedStream = simpleFirstStream.mapValues(String::toUpperCase); //6 
 
    upperCasedStream.to("out-topic"); //7 
    
 
    System.out.println("Streaming App Started"); 
    streams.start(); 
    Thread.sleep(30000);  //8 
    System.out.println("Shutting down the Streaming App"); 
    streams.close(); 
  } 
} 