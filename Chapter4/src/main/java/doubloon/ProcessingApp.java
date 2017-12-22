package doubloon;

import java.io.IOException;

public class ProcessingApp {

  public static void main(String[] args) throws IOException{
    String servers = args[0];
    String groupId = args[1];
    String sourceTopic = args[2];
    String goodTopic = args[3];
    String badTopic = args[4];
    
    Reader reader = new Reader(servers, groupId, sourceTopic);      
    Enricher enricher = new Enricher(servers, goodTopic, badTopic);
  
    reader.run(enricher);
  
  }
}
