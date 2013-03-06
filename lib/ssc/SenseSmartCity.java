package ssc;
/**
 * 
 * @author Jim Gunnarsson
 */

import java.util.*;
import org.json.*;

 
public class SenseSmartCity {
   
   private final RestfulClient ssc_client;

   private SSCResources resource = new SSCResources();
  
   private String user = null;
   private String pwd = null;
   
   private Map<Sensor, List<SnowPressure>> snowdata = null;
   
   /**
    * 
    */
   private SenseSmartCity() {
   
      ssc_client = null;
      
   }
  
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
         String sensors_str = (new JSONArray(sensors)).toString();
         args.put(SSCResources.Query.SENSORS, sensors_str);
      }
      
      args.put(SSCResources.Query.PUBLIC_SENSORS, ((all)? "yes": "no"));
      args.put(SSCResources.Query.FORMAT, "json");
      
      String data = ssc_client.getData(
         SSCResources.Url.HTTPS_SSC + 
         SSCResources.Url.GET_SENSOR_LIST,
         args);
      
      return parseSensorData(responseArray(data));
      
   }
   
   /**
    * 
    */
   public Map<Sensor, List<SnowPressure>> requestSnowPressure(
      List<Sensor> sensors, List<String> querys, String period) {
         
      Map<String, String> args = new HashMap<String, String>();
      
      JSONArray sensors_json = 
         new JSONArray(getSensorSerials(sensors, "SnowPressure"));
      JSONArray fields_json = 
         new JSONArray(querys);
      
      args.put(SSCResources.Query.SENSOR, sensors_json.toString());
      args.put(SSCResources.Query.FIELDS, fields_json.toString());
      args.put(SSCResources.Query.PERIOD, period);
      
      String data = ssc_client.getData(
         SSCResources.Url.HTTPS_SSC + 
         SSCResources.Url.GET_SNOWPRESURE,
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
         
         Sensor s = (Sensor) i.next();
         
         if (s.getTypeName().equals(type)) {
            
            serials.add(s.getSerial());
            
         }
      }
         
      return serials;
      
   }
   
   private JSONArray responseArray(String data) {
      
      JSONArray data_array;
      
      try {
         
         JSONObject response = new JSONObject(data);
         data_array = 
            response.getJSONArray(SSCResources.Query.RESPONSE);
      
      } catch (org.json.JSONException e) {
      
         throw new SSCException.MalformedData(e);
         
      }
      
      return data_array;
      
   }
   
   private JSONObject responseObject(String data) {
      
      JSONObject data_obj;
      
      try {
         
         JSONObject response = new JSONObject(data);
         data_obj = 
            response.getJSONObject(SSCResources.Query.RESPONSE);
      
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
   private Map<Sensor, List<SnowPressure>> parseSnowPressureData(
      List<Sensor> sensors, JSONObject data) {
      
      Map<Sensor, List<SnowPressure>> readings = 
         new HashMap<Sensor, List<SnowPressure>>();
         
      Iterator i = sensors.iterator();
      
      while (i.hasNext()) {
         
         Sensor sensor = (Sensor) i.next();
         JSONArray fields = data.getJSONArray(sensor.getSerial());
         List<SnowPressure> snowdata = 
            SnowPressure.getSnowPressure(sensor.getSerial(), fields);
            
         readings.put(sensor, snowdata);
         
      }
         
      return readings;
   }
      
}
