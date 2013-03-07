package ssc;
/**
 * 
 * @author Jim Gunnarsson
 */
 
import java.util.*;
import org.json.*;
import java.lang.Math;

public class SnowPressure implements Comparable<SnowPressure> {
   
   private SSCResources resource = new SSCResources();
   
   /**  */
   private static final int INFO_LENGTH = 100;
   
   /**  */
   private String serial = null;
   /**  */
   private String info = null;
   /**  */
   private boolean shoveld = false;
   /**  */
   private int weight = 0;
   /**  */
   private int depth = 0;
   /**  */
   private int temperature = 0;
   /**  */
   private int humidity = 0;
   /**  */
   private SSCTimeUnit data_time = null;
   
   public SnowPressure(String sensor, JSONObject obj) {
      
      String INFO = SSCResources.Field.INFO;
      String SHOVELD = SSCResources.Field.SHOVELD;
      String WEIGHT = SSCResources.Field.WEIGHT;
      String DEPTH = SSCResources.Field.DEPTH;
      String TEMP = SSCResources.Field.TEMPERATURE;
      String HUMIDITY = SSCResources.Field.HUMIDITY;
      String TIME = SSCResources.Field.DATA_TIME;
      
      this.serial = sensor;
      
      this.info = (obj.has(INFO))? 
         truncate(obj.getString(INFO), INFO_LENGTH): "";
      
      this.shoveld = (obj.has(SHOVELD))? 
         toBoolean(obj.getString(SHOVELD)): false;
      
      this.weight = (obj.has(WEIGHT))? 
         obj.getInt(WEIGHT): 0;
      
      this.depth = (obj.has(DEPTH))? 
         obj.getInt(DEPTH): 0;
      
      this.temperature = (obj.has(TEMP))? 
         obj.getInt(TEMP): -273;
      
      this.humidity = (obj.has(HUMIDITY))? 
         obj.getInt(HUMIDITY): 0;
      
      this.data_time = (obj.has(TIME))? 
         new SSCTimeUnit(obj.getString(TIME)):
         new SSCTimeUnit("1970-01-01 00:00:00");
      
   }
   
   public static List<SnowPressure> getSnowPressure(
      String sensor, JSONArray obj_array) {
      
       List<SnowPressure> snowdata = new ArrayList<SnowPressure>();
        
      for (int i = 0; i < obj_array.length(); i++) {
         
         JSONObject obj = obj_array.getJSONObject(i);
         snowdata.add(new SnowPressure(sensor, obj));
      }
        
      return snowdata;
      
   }
   
   /**  */
   public String getSensorSerial() {
      
      return this.serial;
   }
   /**  */
   public String getInfo() {
      
      return this.info;
   }
   /**  */
   public boolean getShoveld() {
      
      return this.shoveld;
   }
   /**  */
   public int getWeight() {
   
      return this.weight;
   }
   /**  */
   public int getDepth() {
   
      return this.depth;
   }
   /**  */
   public int getTemperature() {
      
      return this.temperature;
   }
   /**  */
   public int getHumidity() {
      
      return this.humidity;
   }
   /**  */
   public SSCTimeUnit getDataTime() {
      
      return this.data_time;
   }
   
   /**  */
   public void setSensorSerial(String sensor) {
      
      this.serial = sensor;
   }
   /**  */
   public void setInfo(String info) {
      
      this.info = info;
   }
   /**  */
   public void setShoveld(boolean shoveld) {
      
      this.shoveld = shoveld;
   }
   /**  */
   public void setWeight(int weight) {
      
      this.weight = weight;
   }
   /**  */
   public void setDepth(int depth) {
   
      this.depth = depth;
   }
   /**  */
   public void setTemperature(int temperature) {
      
      this.temperature = temperature;
   }
   /**  */
   public void setHumidity(int humidity) {
      
      this.humidity = humidity;
   }
   /**  */
   public void setDataTime(SSCTimeUnit unit) {
      
      this.data_time = unit;
   }
   
   /**
    * Compares this object with the specified object for order. 
    * Returns a negative integer, zero, or a positive integer as this 
    * object is less than, equal to, or greater than the specified 
    * object. 
    * 
    * Note: this class has a natural ordering that is inconsistent 
    * with equals. Two readings is equal if timestamp and sensor 
    * is the same while comparing is done on timestamp only. 
    * 
    * @param obj The reading to be compared
    * 
    * @return A negative integer, zero, or a positive integer as this 
    * object is less than, equal to, or greater than the specified 
    * object. 
    * 
    * @throws NullPointerException if specified reading is null
    */
   @Override 
   public int compareTo(SnowPressure obj) {
      
      if (obj == null) {
         
         throw new java.lang.NullPointerException(
            "SnowPressure: Can't compare with null");
      }
      
      return (int) Math.signum(this.data_time.diffSeconds(obj.getDataTime()));
   
   }
   
   @Override
   public boolean equals(Object obj) {
      
      if (obj == null || obj.getClass() != getClass()) {
         
         return false;
      } 
      
      return this.hashCode() == obj.hashCode();
      
   }
   
   @Override
   public int hashCode() {
      
      if (serial == null || data_time == null) {
         
         return 0;
      }
      
      String token = serial + data_time.toString();
      
      return token.hashCode();
      
   }
   
   @Override
   public String toString() {
      
      StringBuilder sb = new StringBuilder(300);
      
      sb.append("Sense Smart City SnowPressure (");
      sb.append("sensor: ").append(serial);
      sb.append("info: ").append(info);
      sb.append("shoveld: ").append(shoveld);
      sb.append("weight: ").append(weight);
      sb.append("depth: ").append(depth);
      sb.append("temperature: ").append(temperature);
      sb.append("humidity: ").append(humidity);
      sb.append("data_time: ").append(data_time.toString());
      sb.append(")");
         
      return sb.toString();
      
   }
   
   /**
    * Truncate a String to the given length with no warnings or error 
    * raised if it is bigger.
    * 
    * @param  str String to be truncated
    * @param  length  Maximum length of string
    * 
    * @return Returns value if value is null or value.length() is less 
    * or equal to than length, otherwise a String representing
    * value truncated to length.
    */
   private String truncate(String str, int length) {
      
      if (str != null && str.length() > length) {
         str = str.substring(0, length);
      }
      
      return str;
   }
   
   private boolean toBoolean(String str) {
      
      return (str != null && str == "1")? true: false;
   }
}
