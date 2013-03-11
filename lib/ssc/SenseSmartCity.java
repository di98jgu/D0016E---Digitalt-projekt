package ssc;
/**
 * 
 * @author Jim Gunnarsson
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

 
public class SenseSmartCity {
   
   private final RestfulClient ssc_client;
  
   private String user = null;
   private String pwd = null;
   
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
      
      if (notNull(sensors) && !sensors.isEmpty()) {
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
         
      nullWatch(sensors);
         
      Map<String, String> args = new HashMap<String, String>();
      
      // Requested sensors, for now we only accept SnowPressure
      JSONArray sensors_json = 
         new JSONArray(getSensorSerials(sensors, "SnowPressure"));
      args.put(SSCResources.Query.SENSORS, sensors_json.toString());
      
      // Fields, none provided means all available.
      if (notNull(querys) && !querys.isEmpty()) {

         JSONArray fields_json = new JSONArray(querys);
         args.put(SSCResources.Query.FIELDS, fields_json.toString());
      }
      
      // Time period, just ignore it if null.
      if (notNull(period) && period.trim() != "") {
         
         args.put(SSCResources.Query.PERIOD, validatePeriod(period));
      }
      
      // Undocumented option but needed, else we get XML in return.
      args.put(SSCResources.Query.FORMAT, "json");
      
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

      Iterator<Sensor> i = sensors.iterator();
      
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
      
      Iterator<Sensor> i = sensors.iterator();
      
      try {
         
         while (i.hasNext()) {
            
            Sensor sensor = (Sensor) i.next();
            JSONArray fields = data.getJSONArray(sensor.getSerial());
            List<SnowPressure> snowdata = 
               SnowPressure.getSnowPressure(sensor.getSerial(), fields);
               
            readings.put(sensor, snowdata);
            
         }
      
      } catch (org.json.JSONException e) {
         
         throw new SSCException.MalformedData(e);
         
      }
         
      return readings;
   }
   
   private <T> T nullWatch(T obj) {
      
      if (obj == null) {
         
         String msg = "Need a value and none was provided";
         throw new java.lang.NullPointerException(msg);
         
      }
      
      return obj;
   
   }
   
   private <T> boolean notNull(T obj) {
      
      return (obj == null)? false: true;
      
   }
   
   private String validatePeriod(String period) {
      
      String str = period.trim();
      
      // Will throw a suitable exception if period is not valid
      SSCResources.Period.getState(period);
      
      return str;
      
   }
}
