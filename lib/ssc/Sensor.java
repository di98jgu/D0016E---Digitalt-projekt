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
 * This is a Sensor in SSC. It wraps all sensor values and provide get and set 
 * methods for them. Type of sensor is important. The data collected depends on 
 * sensor type, in other what kind of reading is conducted. To get a 
 * predictable response from the SSC server it is important to only use valid 
 * data fields for a given type of sensor. 
 * 
 * What is not implemented but most desirable would be to readings to Sensor and
 * thus let Sensor manage for relation between sensor and readings. This would I
 * believe be the natural order of things.
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class Sensor {
   
   /** Max length for field location */
   public static final int LOCATION_LENGTH = 128;
   /** Max length for field name */
   public static final int NAME_LENGTH = 50;
   /** Max length for field info */
   public static final int INFO_LENGTH = 255;
   
   /** Unique sensor serial number to identify this sensor, 
    * only alphanumeric characters allowed */
   private String serial = null;
   /** Name of sensor */
   private String name = null;
   /** Description of sensor location */
   private String location = null;
   /** Position of sensor */
   private SSCPosition position = null;
   /** Type of sensor */
   private SSCResources.TypeName type_name;
   /** Current sensor state */
   private SSCResources.DeployedState deployed_state;
   /** Visible for other domains i.e. public or private */
   private boolean visibility = false;
   /** Additional information about this sensor */
   private String info = null;
   /** Domain to which this sensor belongs */
   private String domain = null;
   /** Time of registration */
   private SSCTimeUnit created = null;
   /** Time of latest modification */
   private SSCTimeUnit updated = null;
   
   /**
    * Create a new Sensor from a JSON object. 
    * 
    * @param obj Sensor data
    */
   public Sensor(JSONObject obj) {

      try {
         
         this.serial = 
            obj.getString(SSCResources.Field.SERIAL);
         
         this.name = truncate(
            obj.getString(SSCResources.Field.NAME), NAME_LENGTH);
            
         this.location = truncate(
            obj.getString(SSCResources.Field.LOCATION), LOCATION_LENGTH);
            
         this.position = new SSCPosition(
            obj.getDouble(SSCResources.Field.LATITUDE),
            obj.getDouble(SSCResources.Field.LONGITUDE));
            
         this.type_name = SSCResources.TypeName.getState(
            obj.getString(SSCResources.Field.TYPE_NAME));
            
         this.deployed_state = SSCResources.DeployedState.getState(
            obj.getString(SSCResources.Field.DEPLOYED_STATE));
            
         String v = obj.getString(SSCResources.Field.VISIBILITY);
         this.visibility = (v == "1")? true: false;
         
         this.info = truncate(
            obj.getString(SSCResources.Field.INFO), INFO_LENGTH);
            
         this.domain = 
            obj.getString(SSCResources.Field.DOMAIN);
         
         this.created = new SSCTimeUnit(
            obj.getString(SSCResources.Field.CREATED));
            
         this.updated = new SSCTimeUnit(
            obj.getString(SSCResources.Field.UPDATED));
         
      } catch (org.json.JSONException e) {
         
         throw new SSCException.MalformedData(e);
         
      }
      
   }
   
   /**
    * Create a list of sensors from a JSON array. 
    * 
    * @param obj_array JSON array with sensor data
    * 
    * @return List of sensors
    * 
    * @throws SSCException.MalformedData if obj_array is not extractable
    */
   public static List<Sensor> getSensors(JSONArray obj_array) {
      
      List<Sensor> sensors = new ArrayList<Sensor>();
      
      try {
         
         for (int i = 0; i < obj_array.length(); i++) {
            
            JSONObject obj = obj_array.getJSONObject(i);
            sensors.add(new Sensor(obj));
         }
         
      } catch (org.json.JSONException e) {
         
         throw new SSCException.MalformedData(e);
         
      }
        
      return sensors;
      
   }
   
   /**  
    * Unique sensor serial number to identify this sensor, the sensor id.
    * 
    * @return Serial number for this sensor
    */
   public String getSerial() {
      
      return serial;
   }
   
   /**  
    * Get human readable name of this sensor.
    * 
    * @return Name of the sensor
    */
   public String getName() {
      
      return name;
   }
   
   /**  
    * Description of sensor location.
    * 
    * @return Sensor location for this sensor
    */
   public String getLocation() {
      
      return location;
   }
   
   /**  
    * Get latitude in degrees with decimal notation.
    * 
    * @return Latitude of this sensor
    */
   public double getLatitude() {
      
      return position.getLatitude();
   }
   
   /**  
    * Get longitude in degrees with decimal notation.
    * 
    * @return Longitude of this sensor
    */
   public double getLongitude() {
      
      return position.getLongitude();
   }
   
   /**  
    * Get position of sensor
    * 
    * @return Position of sensor
    */
   public SSCPosition getPosition() {
      
      return position;
   }
   
   /**  
    * Get type of sensor, see SSCResources.TypeName for all types.
    * 
    * @return Type of sensor
    */
   public String getTypeName() {
      
      return type_name.getField();
   }
   
   /**  
    * Get current sensor state. See SSCResources.DeployedState all states.
    * 
    * @return Current sensor state
    */
   public String getDeployedState() {
      
      return deployed_state.getField();
   }
   
   /**  
    * Visible for other domains i.e. public or private. A sensor belongs to a
    * domain but will be visible for all domains if visibility is set to 
    * <code>true</code>.
    * 
    * @return Visibility of this sensor
    */
   public boolean getVisibility() {
      
      return visibility;
   }
   
   /**  
    * Get additional information about this sensor.
    * 
    * @return Additional info about this sensor
    */
   public String getInfo() {
      
      return info;
   }
   
   /**  
    * Get domain to which this sensor belongs.
    * 
    * @return Domain to which this sensor belongs.
    */
   public String getDomain() {
      
      return domain;
   }
   
   /**  
    * Get time of registration for this sensor.
    * 
    * @return Time of registration
    */
   public SSCTimeUnit getCreated() {
      
      return created;
   }
   
   /**  
    * Get time of latest modification for this sensor.
    * 
    * @return Time of latest modification
    */
   public SSCTimeUnit getUpdated() {
      
      return updated;
   }
   
   /**  
    * Unique sensor serial number to identify this sensor, 
    * only alphanumeric characters allowed
    * 
    * @param serial Serial number for this sensor
    */
   public void setSerial(String serial) {
      
      this.serial = serial;
   }
   
   /**  
    * Set human readable name of this sensor. Will be truncated if name exceeds
    * NAME_LENGTH.
    * 
    * @param name New name of the sensor
    */
   public void setName(String name) {
      
      this.name = truncate(name, NAME_LENGTH);
   }
   
   /**  
    * Description of sensor location, Will be truncated if location exceeds
    * LOCATION_LENGTH.
    * 
    * @param location Description of sensor location
    */
   public void setLocation(String location) {
      
      this.location = truncate(location, LOCATION_LENGTH);
   }
   
   /**  
    * Set latitude, must be in degrees with decimal notation.
    * 
    * @param latitude Latitude of this sensor
    */
   public void setLatitude(double latitude) {
      
      this.position.setLatitude(latitude);
   }
   
   /**
    * Set longitude, must be in degrees with decimal notation.
    * 
    * @param longitude Longitude of this sensor
    */
   public void setLongitude(double longitude) {
      
      this.position.setLongitude(longitude);
   }
   
   /**  
    * Set position of sensor
    * 
    * @param position Position of this sensor
    */
   public void setPosition(SSCPosition position) {
      
      this.position = position;
   }
   
   /**  
    * Set type of sensor, see SSCResources.TypeName for all valid types.
    * 
    * @param type_name Type of sensor
    * 
    * @throws SSCException.MalformedData if not a valid type name is given
    */
   public void setTypeName(String type_name) {
      
      this.type_name = SSCResources.TypeName.getState(type_name);
   }
   
   /**  
    * Set current sensor state. See SSCResources.DeployedState all valid states.
    * 
    * @param deployed_state New sensor state
    * 
    * @throws SSCException.MalformedData if not a valid state is given
    */
   public void setDeployedState(String deployed_state) {
      
      this.deployed_state = 
         SSCResources.DeployedState.getState(deployed_state);
   }
   
   /**  
    * Visible for other domains i.e. public or private. A sensor belongs to a
    * domain but will be visible for all domains if visibility is set to 
    * <code>true</code>.
    * 
    * @param visibility Visibility of this sensor
    */
   public void setVisibility(boolean visibility) {
      
      this.visibility = visibility;
   }
   
   /**  
    * Provide additional information about this sensor. Will be truncated if
    * location exceeds INFO_LENGTH.
    * 
    * @param info Additional info about this sensor
    */
   public void setInfo(String info) {
      
      this.info = truncate(info, INFO_LENGTH);
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
      
      return serial.hashCode();
      
   }

   /**
    * Returns a string representation of this object.
    * 
    * @return A string representation
    */
   @Override
   public String toString() {
      
      StringBuilder sb = new StringBuilder(400);
      
      sb.append("Sense Smart City sensor (");
      sb.append(" serial: ").append(serial);
      sb.append(" name: ").append(name);
      sb.append(" location: ").append(location);
      sb.append(" latitude: ").append(position.getLatitude());
      sb.append(" longitude: ").append(position.getLongitude());
      sb.append(" type_name: ").append(type_name.getField());
      sb.append(" deployed_state: ").append(deployed_state.getField());
      sb.append(" visibility: ").append(visibility);
      sb.append(" info: ").append(info);
      sb.append(" domain: ").append(domain);
      sb.append(" created: ").append(created.toString());
      sb.append(" updated: ").append(updated.toString());
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
         
}
