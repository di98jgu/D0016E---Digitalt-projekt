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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <p>This library is the client side of Sense Smart City. SSC is a restful 
 * server providing snow information such as weight, humidity, depth and 
 * temperature for a given location. The aim is to protect people and property.
 * SSC is developed and maintained by the Swedish city of Skellefteå.
 * </p>
 * 
 * <p>This library is the result of a student project in the course D0016E 
 * digital projekt lp 3 2013. The target system is Android but this library is 
 * tested on standard JDK 6 without problem. Some minor changes was needed in 
 * RestfulClient to make it work on Android.
 * </p>
 * 
 * <p>Snow information is collected using sensors. There is different kinds of
 * sensors depending on type of measurement. Here only snow pressure is 
 * implemented. Measurement is done periodly and each reading is taged with
 * time, date and the id of the sensor. So for each sensor there is a set of 
 * readings. Sensors belongs to a specific domain. In order to collect data from
 * SSC user credentials is needed. A sensor can be visible to all users or 
 * private for users in the domain of that particular sensor. A sensor have 
 * location. 
 * </p>
 * 
 * <p>Note that the sensor itself and snow data collected by the same sensor is 
 * two different entities.
 * </p>
 * 
 * <p>Much functionality is missing but it should not be any problem to continue
 * the development. At least that was the idea behind the code structure. 
 * </p>
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class SenseSmartCity {
   
   /** Restful client */
   private final RestfulClient ssc_client;
   
   /** Map sensor to readings */
   protected Map<Sensor, List<SnowPressure>> ssc_data;
   /** Default fields at request if none is given */
   protected Map<String, List<String>> ssc_fields;
   /** Default time period at request if none is given */
   protected String ssc_period; 
   
   /** Username */
   private String user = null;
   /** Password */
   private String pwd = null;
   
   /**
    * User credentials is mandatory. 
    */
   private SenseSmartCity() {
   
      ssc_client = null;
      
   }
  
   /**
    * Initialises a new library given the user credentials
    * 
    * @param user Username 
    * @param pwd Password
    * 
    * @throws SSCException.NoUserCredentials if no user or password is provided
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
   
   /**
    * Get a list of all sensors available for this user.
    * 
    * @return A list of all sensors
    */
   public List<Sensor> getSensors() {
      
      if (ssc_data.isEmpty()) {
         
         ssc_data = requestAll();
      }
      
      return getKeys(ssc_data);
      
   }
   
   /**
    * Return all readings for a particular sensor. A empty list is returned if
    * no readings is found.
    * 
    * @param sensor A valid sensor
    * 
    * @return List of readings
    */
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
   
   /** 
    * Request all data available for this user. Predefined values is used for 
    * fields and time period is to collect data. Default is last year and all
    * fields.
    * 
    * @return Map with Sensor as keys and values as a list of readings
    */
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
    * Request list of sensors. A list of sensor id, serials, can be provided. A
    * empty list equals all sensors. All sensors regardless of domain sensors is
    * requested by setting all to true. 
    * 
    * @param sensors List of sensor id, empty for all sensors
    * @param all <code>True</code> for all public sensors, <code>false</code> 
    * for private sensors only 
    * 
    * @return List with sensors.
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
    * Request snow pressure readings. This is a compromise greatly simplify the
    * design of this library but of course limit readings to snow pressure only.
    * Should of course be able to handle all type of sensors.
    * 
    * Need a list of sensors, at least one. None throws an exception. Desired
    * fields is given as a list of queries, this can be empty but only a limit
    * number of fields is then returned. A time period might be given, none is
    * needed. All valid periods is found in SSCResources.
    * 
    * @param sensors List of sensors
    * @param querys List of fields
    * @param period Time period
    * 
    * @return A map with sensor as keys and list of readings as value
    */
   public Map<Sensor, List<SnowPressure>> requestSnowPressure(
      List<Sensor> sensors, List<String> querys, String period) {
         
      nullWatch(sensors);
         
      Map<String, String> args = new HashMap<String, String>();
      
      // Filter requested sensors, for now we only accept SnowPressure
      List<Sensor> sensors_sp = getSensorType(sensors, "SnowPressure");
      emptyWatch(sensors_sp);
      
      JSONArray sensors_json = new JSONArray(getSensorSerials(sensors_sp));
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
      
      return parseSnowPressureData(sensors_sp, responseObject(data));
      
   }
   
   /**
    * Get sensors of a given type. A filter simply, returning a list of Sensors
    * of a desired type.
    * 
    * @param sensors List of sensors
    * @param type Type of sensor desired
    * 
    * @return A sublist of sensors of desired type or empty if none is found
    */
   private List<Sensor> getSensorType(List<Sensor> sensors, String type) {
      
      List<Sensor> sensor_type = new ArrayList<Sensor>();

      Iterator<Sensor> i = sensors.iterator();
      
      while (i.hasNext()) {
         
         Sensor s = i.next();
         
         if (s.getTypeName().equals(type)) {
            
            sensor_type.add(s);
            
         }
      }
         
      return sensor_type;
      
   }
   
   /**
    * Get a list of sensor serials. Serials is the unique id for each sensor.
    * 
    * @param sensors List of sensors
    * 
    * @return List of serials
    */
   private List<String> getSensorSerials(List<Sensor> sensors) {
      
      List<String> serials = new ArrayList<String>();

      Iterator<Sensor> i = sensors.iterator();
      
      while (i.hasNext()) {
         
         Sensor s = i.next();
         serials.add(s.getSerial());
            
      }
         
      return serials;
      
   }
   
   /** 
    * Take a response object and return response array. The response object is
    * the raw JSON data retrieved from SSC server. It contain a key and a value.
    * Here the value is assumed to be an array.
    * 
    * @param data Raw JSON object from SSC server
    * 
    * @return A JSON array containing the requested data
    * 
    * @throws SSCException.MalformedData if data is not extractable
    */
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
   
   /** 
    * Take a response object and return response object. The response object is
    * the raw JSON data retrieved from SSC server. It contain a key and a value.
    * Here the value is assumed to be a object.
    * 
    * @param data Raw JSON object from SSC server
    * 
    * @return A JSON object containing the requested data
    * 
    * @throws SSCException.MalformedData if data is not extractable 
    */
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
    * Parse the sensor data in a JSON array. 
    * 
    * @param data A JSON array with sensor data
    * 
    * @return List with sensors
    */
   private List<Sensor> parseSensorData(JSONArray data) {
      
      return Sensor.getSensors(data);
      
   }
   
   /**
    * Parse a JSON object for sensor readings. A list of sensors is needed to
    * verify and link sensor and readings together. 
    * 
    * @param sensors List of Sensors
    * @param data A JSON object with sensor readings
    * 
    * @return A map with Sensors as keys and list of readings as values 
    * 
    * @throws SSCException.MalformedData if data is not extractable 
    */
   private Map<Sensor, List<SnowPressure>> parseSnowPressureData(
      List<Sensor> sensors, JSONObject data) {
      
      Map<Sensor, List<SnowPressure>> readings = 
         new HashMap<Sensor, List<SnowPressure>>();
      
      Iterator<Sensor> i = sensors.iterator();
      
      try {
         
         while (i.hasNext()) {
            
            Sensor sensor =  i.next();
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
   
   /**
    * Null watch. If given object is null a exception is thrown.
    * 
    * @param obj Object to check
    * 
    * @return obj if it is not null
    * 
    * @throws java.lang.NullPointerException if obj is null
    */
   private <T> T nullWatch(T obj) {
      
      if (obj == null) {
         
         String msg = "Need a value and none was provided";
         throw new java.lang.NullPointerException(msg);
         
      }
      
      return obj;
   
   }
   
   /**
    * Empty watch. If given list is empty a exception is thrown.
    * 
    * @param list List to check
    * 
    * @return list if it is not empty
    * 
    * @throws SSCException.MalformedData if list is empty
    */
   private <T> List<T> emptyWatch(List<T> list) {
      
      if (list.isEmpty()) {
         
         String msg = "List must not be empty, need at least one list item";
         throw new SSCException.MalformedData(msg);
      }
      
      
      return list;
      
   }
   
   /**
    * Return true if obj is not null. 
    * 
    * @param obj Object to check
    * 
    * @return <code>true</code> if obj is not null else <code>false</code>
    */
   private <T> boolean notNull(T obj) {
      
      return (obj == null)? false: true;
      
   }
   
   /**
    * Validate a given time period. All valid time periods is defined by
    * SSCResources.Period.
    * 
    * @param period Time period to validate
    * 
    * @throws SSCException.MalformedData if period is not valid
    */
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
   
   /**
    * Set values for default fields. If no fields is given at a request to SSC 
    * server this is the values to to use. Each sensor type have set of valid
    * fields. A map is created there keys is the type of sensor and value is a
    * list of all valid fields for that type. 
    * 
    * @return A map with type of sensor as key and a list of fields as value
    */
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
