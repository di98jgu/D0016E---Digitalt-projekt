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
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Container for a reading of type SnowPressure. This is a reading meaning it
 * wraps all data for this reading and provide get and set methods to the data. 
 * No information about the location of this readning is provided here, it is
 * found in the Sensor. 
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class SnowPressure implements Comparable<SnowPressure> {
   
   /** Maximum length of info field */
   public static final int INFO_LENGTH = 100;
   
   /** Id to the sensor this reading belongs, default empty string */
   private String serial = null;
   /** Additional information about this particular reading, default empty string */
   private String info = null;
   /** Have the sensor location been shoveled, default false */
   private boolean shoveld = false;
   /** The snow weight in grams, default 0 */
   private int weight = 0;
   /** The snow depth in centimetres, default 0 */
   private int depth = 0;
   /** The temperature in Celsius degrees, default -273 */
   private int temperature = 0;
   /** Percentage in relative humidity, default 0 */
   private int humidity = 0;
   /** Time and date of registration, default 1970-01-01 00:00:00 */
   private SSCTimeUnit data_time = null;
   
   /**
    * Create a new SnowPressure reading from a JSON object. A sensor serial to
    * which this reading belongs needs to provided. Fields not provided by the
    * JSON object is filled with default values.
    * 
    * @param sensor Sensor serial to which this reading belong
    * @param obj Snow data
    */
   public SnowPressure(String sensor, JSONObject obj) {
      
      String INFO = SSCResources.Field.INFO;
      String SHOVELD = SSCResources.Field.SHOVELD;
      String WEIGHT = SSCResources.Field.WEIGHT;
      String DEPTH = SSCResources.Field.DEPTH;
      String TEMP = SSCResources.Field.TEMPERATURE;
      String HUMIDITY = SSCResources.Field.HUMIDITY;
      String TIME = SSCResources.Field.DATA_TIME;
      
      this.serial = sensor;
      
      try {
         
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
         
      } catch (org.json.JSONException e) {
         
         throw new SSCException.MalformedData(e);
         
      }
      
   }
   
   /**
    * Create a list of readings from a JSON array. All readings is asumed to be
    * from the same sensor. 
    * 
    * @param sensor Sensor serial to which this reading belong
    * @param obj_array JSON array with readings
    * 
    * @return List of readings of type SnowPressure
    * 
    * @throws SSCException.MalformedData if obj_array is not extractable
    */
   public static List<SnowPressure> getSnowPressure(
      String sensor, JSONArray obj_array) {
      
      List<SnowPressure> snowdata = new ArrayList<SnowPressure>();
      
      try {
      
         for (int i = 0; i < obj_array.length(); i++) {
            
            JSONObject obj = obj_array.getJSONObject(i);
            snowdata.add(new SnowPressure(sensor, obj));
         }
      
      } catch (org.json.JSONException e) {
         
         throw new SSCException.MalformedData(e);
         
      }
        
      return snowdata;
      
   }
   
   /**  
    * Get sensor serial to which this reading belongs.
    * 
    * @return Sensor serial
    */
   public String getSensorSerial() {
      
      return this.serial;
   }
   
   /**  
    * Get optional information about this reading.
    * 
    * @return Info about this reading
    */
   public String getInfo() {
      
      return this.info;
   }
   
   /**
    * Have the sensor location been shoveled.
    * 
    * @return <code>true</code> if the sensor location been shoveled
    *  
    */
   public boolean getShoveld() {
      
      return this.shoveld;
   }
   
   /**  
    * Get snow weight.
    * 
    * @return Snow weight
    */
   public int getWeight() {
   
      return this.weight;
   }
   
   /**
    * Get snow depth 
    * 
    * @return Snow depth
    */
   public int getDepth() {
   
      return this.depth;
   }
   
   /**  
    * Get temperature in grader Celsius.
    * 
    * @return Temperature
    */
   public int getTemperature() {
      
      return this.temperature;
   }
   
   /**
    * Get humidity in percent
    * 
    * @return Humidity
    */
   public int getHumidity() {
      
      return this.humidity;
   }
   
   /**
    * Get time unit, date and time
    * 
    * @return SSC time unit
    */
   public SSCTimeUnit getDataTime() {
      
      return this.data_time;
   }
   
   /**  
    * Set sensor serial to which this reading should belong.
    * 
    * @param sensor Sensor serial
    */
   public void setSensorSerial(String sensor) {
      
      this.serial = sensor;
   }
   /**
    * Add optional information about this reading.
    * 
    * @param info Info about this reading
    */
   public void setInfo(String info) {
      
      this.info = info;
   }
   /**
    * Mark if the sensor location been shoveled.
    * 
    * @param shoveld Boolean <code>true</code> if location have been shoveled
    */
   public void setShoveld(boolean shoveld) {
      
      this.shoveld = shoveld;
   }
   
   /**
    * Set snow weight.
    * 
    * @param weight Snow weight
    */
   public void setWeight(int weight) {
      
      this.weight = weight;
   }
   /**
    * Set snow depth 
    * 
    * @param depth Snow depth
    */
   public void setDepth(int depth) {
   
      this.depth = depth;
   }
   /**
    * Set temperature in grader Celsius.
    * 
    * @param temperature Temperature in grader Celsius
    */
   public void setTemperature(int temperature) {
      
      this.temperature = temperature;
   }
   /**
    * Get humidity in percent
    * 
    * @param humidity Humidity
    */
   public void setHumidity(int humidity) {
      
      this.humidity = humidity;
   }
   
   /**
    * Set time unit, date and time
    * 
    * @param unit A SSC time unit
    */
   public void setDataTime(SSCTimeUnit unit) {
      
      this.data_time = unit;
   }
   
   /**
    * Compares this object with the specified object to determine the order. 
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
   public int compareTo(SnowPressure obj) {
      
      if (obj == null) {
         
         throw new java.lang.NullPointerException(
            "SnowPressure: Can't compare with null");
      }
      
      return (int) Math.signum(this.data_time.diffSeconds(obj.getDataTime()));
   
   }
   
   /** 
    * Indicates whether given object is equal to this one.
    * 
    * @param obj Object with which to compare
    * 
    * @return <code>true</code> if equal
    */
   @Override
   public boolean equals(Object obj) {
      
      if (obj == null || obj.getClass() != getClass()) {
         
         return false;
      } 
      
      return this.hashCode() == obj.hashCode();
      
   }
   
   /**
    * Returns a hash code value for this object. 
    * 
    * @return A hash code value
    */
   @Override
   public int hashCode() {
      
      if (serial == null || data_time == null) {
         
         return 0;
      }
      
      String token = serial + data_time.toString();
      
      return token.hashCode();
      
   }
   
   /**
    * Returns a string representation of this object.
    * 
    * @return A string representation
    */
   @Override
   public String toString() {
      
      StringBuilder sb = new StringBuilder(300);
      
      sb.append("Sense Smart City SnowPressure (");
      sb.append(" sensor: ").append(serial);
      sb.append(" info: ").append(info);
      sb.append(" shoveld: ").append(shoveld);
      sb.append(" weight: ").append(weight);
      sb.append(" depth: ").append(depth);
      sb.append(" temperature: ").append(temperature);
      sb.append(" humidity: ").append(humidity);
      sb.append(" data_time: ").append(data_time.toString());
      sb.append(")");
         
      return sb.toString();
      
   }
   
   /**
    * Truncate a string to a precise length. This prevents a field to exceeds a
    * given size limit.
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
   
   /**
    * Boolean from SSC server is represented as 1 and 0. This method transforms
    * those values to boolean. Note only 1 return true 0 or any other string
    * inclusive a null value return false. 
    * 
    * @param str String with 1 or 0
    * 
    * @return <code>true</code> for 1 and <code>false</code> for 0
    */
   private boolean toBoolean(String str) {
      
      return (str != null && str == "1")? true: false;
   }
}
