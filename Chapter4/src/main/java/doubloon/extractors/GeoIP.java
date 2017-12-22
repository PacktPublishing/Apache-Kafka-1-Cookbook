package doubloon.extractors;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeoIP {

  private static final String MAXMINDDB = "./GeoLiteCity.dat";

  public Location getLocation(String ipAddress) {

    try {
      LookupService maxmind = new LookupService(MAXMINDDB, LookupService.GEOIP_MEMORY_CACHE);
      Location location = maxmind.getLocation(ipAddress);
      return location;
    } catch (IOException ex) {
      Logger.getLogger(GeoIP.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }

}
