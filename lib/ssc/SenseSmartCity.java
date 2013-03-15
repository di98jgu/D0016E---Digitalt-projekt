/*
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 */
package ssc;
/**
 * Class SenseSmartCity
 * 
 * This library is the client side of Sense Smart City. SSC is a restful 
 * server providing snow information such as weight, humidity, depth and 
 * temperature for a given location. The aim is to protect people and property.
 * 
 * SSC is developed and maintained by the Swedish city of Skellefteå. It is
 * part of the city's goal of using technology to improve service for citizens 
 * and utilize resources more efficiently. 
 * 
 * This library is the result of a student project in the course D0016E digital
 * projekt lp 3 2013. The target system is Android but this library is tested 
 * on standard JDK 6 without problem. 
 * 
 * Snow information is collected using a sensor. There is different kinds of
 * sensors depending on type of measurement. Here only snow pressure is 
 * implemented. Measurement is done periodly and each reading is taged with
 * time and date and the id of the sensor. So for each sensor there is a set of 
 * readings. 
 * 
 * Sensors belongs to a specific domain. In order to collect data from a sensor
 * a user creditials is needed. A sensor can be visible to all users or
 * private for users in the domain of that particular sensor. A sensor have 
 * location. Important here is that sensor and snow data collected by the 
 * sensor is not mixed.  
 * 
 * Much functionality is missing but it should not be any problem to continue 
 * the development. At least that was the idea behind the code structure. 
 * 
 * @author Jim Gunnarsson
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

 
public class SenseSmartCity {
   
   private final RestfulClient ssc_client;
   
   /** Persistent data */
   protected Map<Sensor, List<SnowPressure>> ssc_data;
   protected Map<String, List<String>> ssc_fields;
   protected String ssc_period; 
   
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
      
      this.ssc_data = new HashMap<Sensor, List<SnowPressure>>();
      this.ssc_fields = defaultFields();
      this.ssc_period = defaultPeriod();
      
   }
   
   public List<Sensor> getSensors() {
      
      if (ssc_data.isEmpty()) {
         
         ssc_data = requestAll();
      }
      
      return getKeys(ssc_data);
      
   }
   
   public List<SnowPressure> getSnowPressure(Sensor sensor) {
      
      if (ssc_data.isEmpty()) {
         
         ssc_data = requestAll();
      }
      
      List<SnowPressure> readings = ssc_data.get(sensor); 
      
      if (notNull(readings)) {
         
         return readings;
      }
      
      return new ArrayList<SnowPressure>();
      
   }
   
   public Map<Sensor, List<SnowPressure>> requestAll() {
      
      List<Sensor> sensors = 
         requestSensorList(new ArrayList<String>(), false);
      
      Map<Sensor, List<SnowPressure>> readings = requestSnowPressure(
         sensors, 
         ssc_fields.get(SSCResources.TypeName.SNOWPRESSURE.getField()), 
         ssc_period);
      
      return readings;
      
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
   
   /** 
    * Get all keys in a map as a list
    * 
    * @param map Map with keys K
    * 
    * @return List with keys K
    */
   private <K, V> List<K> getKeys(Map<K, V> map) {
      
      List<K> keys = new ArrayList<K>();
      
      Iterator<K> i = map.keySet().iterator();
      
      while (i.hasNext()) {
         
         keys.add(i.next());
         
      }
      
      return keys;
      
   }
   
   private Map<String, List<String>> defaultFields() {
      
      Map<String, List<String>> default_fields = 
         new HashMap<String, List<String>>();
      
      List<String> fields = new ArrayList<String>(); 
      
      fields.add(SSCResources.Field.SHOVELD);
      fields.add(SSCResources.Field.WEIGHT);
      fields.add(SSCResources.Field.DEPTH);
      fields.add(SSCResources.Field.TEMPERATURE);
      fields.add(SSCResources.Field.HUMIDITY);
      fields.add(SSCResources.Field.DATA_TIME);
      fields.add(SSCResources.Field.INFO);
      
      default_fields.put(
         SSCResources.TypeName.SNOWPRESSURE.getField(), fields);
     
      return default_fields;
      
   }
   
   private String defaultPeriod() {
      
      return SSCResources.Period.YEAR.getField();
      
   }
   
}
