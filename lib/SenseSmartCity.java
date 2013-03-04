/**
 * 
 * @author Jim Gunnarsson
 */

import java.util.*;
import org.json.*;

 
public class SenseSmartCity {
   
   private final RestfulClient ssc_client = null;
   private SSCResources resource = new SSCResources();
  
   private String user = null;
   private String pwd = null;
   
   private Map<Sensor, List<SnowPressure>> snowdata = null;
   
   /**
    * 
    */
   private SenseSmartCity() {}
  
   /**
    * 
    */
   public SenseSmartCity(String user, String pwd) {
     
      if (user == null || pwd == null) {
         
         throw new SSCException.NoUserCredentials(
            "SenseSmartCity: No user credentials provided");
      }
         
      this.user = user;
      this.pwd = pwd;
      
      this.ssc_client = new RestfulClient(user, pwd);
      
   }
   
   /**
    * 
    */
   public List<Sensor> requestSensorList(List<String> sensors, boolean all) {
      
      Map<String, String> args = new HashMap<String, String>();
      
      if (!sensors.isEmpty()) {
         args.put(resource.Query.SENSORS, new JSONArray(sensors));
      }
      
      args.put(resource.Query.PUBLIC_SENSORS, ((all)? "yes": "no"));
      args.put(resource.Query.FORMAT, "json");
      
      String data = ssc_client.getData(
         resource.Url.HTTPS_SSC + 
         resource.Url.GET_SENSOR_LIST,
         args);
      
      return parseSensorData(responseArray(data));
      
   }
   
   /**
    * 
    */
   public Map<Sensor, List<SnowPressure>> requestSnowPressure(
      List<Sensor> sensors, List<String> Querys, String period) {
         
      Map<String, String> args = new HashMap<String, String>();
      
      args.put(
         resource.Query.SENSOR, 
         new JSONArray(getSensorSerials(sensors, "SnowPressure")));
      args.put(resource.Query.FIELDS, new JSONArray(Querys));
      args.put(resource.Query.PERIOD, new JSONArray(period));
      
      String data = ssc_client.getData(
         resource.Url.HTTPS_SSC + 
         resource.Url.GET_SNOWPRESURE,
         args);
         
         
      return parseSnowPressureData(sensors, responseObject(data));
      
   }
   
   /**
    * 
    */
   private List<String> getSensorSerials(List<Sensor> sensors, String type) {
      
      List<String> serials = new ArrayList<String>();

      Iterator i = sensors.iterator();
      
      while (i.hasNext()) {
         
         Sensor s = i.next();
         
         if (s.getTypeName().equals(type)) {
            
            serials.add(s.serial());
            
         }
      }
         
      return serials;
      
   }
   
   private JSONArray responseArray(String data) {
      
      JSONArray data_array;
      
      try {
         
         JSONObject response = new JSONObject(data);
         data_array = getJSONArray(resource.Field.RESPONSE);
      
      } catch (org.json.JSONException e) {
      
         throw new SSCException.MalformedData(e);
         
      }
      
      return data_array;
      
   }
   
   private JSONObject responseObject(String data) {
      
      JSONArray data_obj;
      
      try {
         
         JSONObject response = new JSONObject(data);
         data_obj = 
            getJSONObject(resource.Field.RESPONSE);
      
      } catch (org.json.JSONException e) {
      
         throw new SSCException.MalformedData(e);
         
      }
      
      return data_obj;
      
   }
   
   /**
    * 
    */
   private List<Sensor> parseSensorData(JSONArray data) {
      
      return Sensor.getSensors(data);
      
   }
   
   /**
    * 
    */
   private Map<Sensor, SnowPressure> parseSnowPressureData(
      List<Sensor> sensors, JSONObject data) {
      
      Map<Sensor, SnowPressure> readings = 
         new HashMap<Sensor, List<SnowPressure>>();
         
      Iterator i = sensors.iterator();
      
      while (i.hasNext()) {
         
         Sensor sensor = i.next();
         JSONArray fields = data.getJSONArray(sensor.getSerial());
         List<SnowPressure> snowdata = 
            SnowPressure.SnowPressure(sensor.getSerial(), fields);
            
         readings.put(sensor, snowdata);
         
      }
         
      return readings;
   }
      
}
