package treu.extractors; 
 
import com.fasterxml.jackson.databind.JsonNode; 
import com.fasterxml.jackson.databind.ObjectMapper; 
import doubloon.extractors.OpenExchange; 
import java.io.IOException; 
import java.net.MalformedURLException; 
import java.net.URL; 
import java.util.logging.Level; 
import java.util.logging.Logger; 
 
public class OpenWeather { 
   
  private static final String API_KEY = "API_KEY_VALUE";                 //1 
  protected static final ObjectMapper MAPPER = new ObjectMapper(); 
   
  public double getTemperature(String lat, String lon) { 
             
    try { 
      URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon="+  lon + "&units=metric&appid=" + API_KEY); 
     
      JsonNode root = MAPPER.readTree(url); 
      JsonNode node = root.path("main").path("temp"); 
      return Double.parseDouble(node.toString()); 
     
    } catch (MalformedURLException ex) { 
      Logger.getLogger(OpenExchange.class.getName()).log(Level.SEVERE, null, ex); 
    } catch (IOException ex) { 
      Logger.getLogger(OpenExchange.class.getName()).log(Level.SEVERE, null, ex); 
    }     
    return 0;   
  }  
} 