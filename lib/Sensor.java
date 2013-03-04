/**
 * 
 * @author Jim Gunnarsson
 */
 
import java.util.*;
import org.json.*;

public class Sensor {
   
   private SSCResources resource = new SSCResources();
   
   /** Max length for field location */
   private static final int LOCATION_LENGTH = 128;
   /** Max length for field name */
   private static final int NAME_LENGTH = 50;
   /** Max length for field info */
   private static final int INFO_LENGTH = 255;
   
   /**  */
   private String serial = null;
   /**  */
   private String name = null;
   /**  */
   private String location = null;
   /**  */
   private SSCPosition position = null;
   /**  */
   private SSCResources.TypeName type_name;
   /**  */
   private SSCResources.DeployedState deployed_state;
   /**  */
   private boolean visibility = false;
   /**  */
   private String info = null;
   /**  */
   private String domain = null;
   /**  */
   private SSCTimeUnit created = null;
   /**  */
   private SSCTimeUnit updated = null;
   
   /**
    * 
    */
   public Sensor(JSONObject obj) {
      
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
         
      this.visibility = 
         obj.getBoolean(SSCResources.Field.VISIBILITY);
      
      this.info = truncate(
         obj.getString(SSCResources.Field.INFO), INFO_LENGTH);
         
      this.domain = 
         obj.getString(SSCResources.Field.DOMAIN);
      
      this.created = new SSCTimeUnit(
         obj.getString(SSCResources.Field.CREATED));
         
      this.updated = new SSCTimeUnit(
         obj.getString(SSCResources.Field.UPDATED));
      
   }
   
   /**
    * 
    */
   public static List<Sensor> getSensors(JSONArray obj_array) {
      
      List<Sensor> sensors = new ArrayList<Sensor>();
        
      for (int i = 0; i < obj_array.length(); i++) {
         
         JSONObject obj = obj_array.getJSONObject(i);
         sensors.add(new Sensor(obj));
      }
        
      return sensors;
      
   }
   
   /**  
    * 
    */
   public String getSerial() {
      
      return serial;
   }
   
   /**  
    * 
    */
   public String getName() {
      
      return name;
   }
   
   /**  
    * 
    */
   public String getLocation() {
      
      return location;
   }
   
   /**  
    * 
    */
   public double getLatitude() {
      
      return position.getLatitude();
   }
   
   /**  
    * 
    */
   public double getLongitude() {
      
      return position.getLongitude();
   }
   
   /**  
    * 
    */
   public SSCPosition getPosition() {
      
      return position;
   }
   
   /**  
    * 
    */
   public String getTypeName() {
      
      return type_name.getField();
   }
   
   /**  
    * 
    */
   public String getDeployedState() {
      
      return deployed_state.getField();
   }
   
   /**  
    * 
    */
   public boolean getVisibility() {
      
      return visibility;
   }
   
   /**  
    * 
    */
   public String getInfo() {
      
      return info;
   }
   
   /**  
    * 
    */
   public String getDomain() {
      
      return domain;
   }
   
   /**  
    * 
    */
   public SSCTimeUnit getCreated() {
      
      return created;
   }
   
   /**  
    * 
    */
   public SSCTimeUnit getUpdated() {
      
      return updated;
   }
   
   /**  
    * 
    */
   public void setSerial(String serial) {
      
      this.serial = serial;
   }
   
   /**  
    * 
    */
   public void setName(String name) {
      
      this.name = truncate(name, NAME_LENGTH);
   }
   
   /**  
    * 
    */
   public void setLocation(String location) {
      
      this.location = truncate(location, LOCATION_LENGTH);
   }
   
   /**  
    * 
    */
   public void setLatitude(double latitude) {
      
      this.position.setLatitude(latitude);
   }
   
   /**  
    * 
    */
   public void setLongitude(double longitude) {
      
      this.position.setLongitude(longitude);
   }
   
   /**  
    * 
    */
   public void setPosition(SSCPosition position) {
      
      this.position = position;
   }
   
   /**  
    * 
    */
   public void setTypeName(String type_name) {
      
      this.type_name = SSCResources.TypeName.getState(type_name);
   }
   
   /**  
    * 
    */
   public void setDeployedState(String deployed_state) {
      
      this.deployed_state = 
         SSCResources.DeployedState.getState(deployed_state);
   }
   
   /**  
    * 
    */
   public void setVisibility(boolean visibility) {
      
      this.visibility = visibility;
   }
   
   /**  
    * 
    */
   public void setInfo(String info) {
      
      this.info = truncate(info, INFO_LENGTH);
   }
   
   /**  
    * 
    */
   public void setDomain(String domain) {
      
      this.domain = domain;
   }
   
   /**  
    * 
    */
   public void setCreated(SSCTimeUnit created) {
      
      this.created = created;
   }
   
   /**  
    * 
    */
   public void setUpdated(SSCTimeUnit updated) {
      
      this.updated = updated;
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
      
      return serial.hashCode();
      
   }

   @Override
   public String toString() {
      
      StringBuilder sb = new StringBuilder(400);
      
      sb.append("Sense Smart City sensor (");
      sb.append("serial: ").append(serial);
      sb.append("location: ").append(location);
      sb.append("latitude: ").append(position.getLatitude());
      sb.append("longitude: ").append(position.getLongitude());
      sb.append("type_name: ").append(type_name.getField());
      sb.append("deployed_state: ").append(deployed_state.getField());
      sb.append("visibility: ").append(visibility);
      sb.append("info: ").append(info);
      sb.append("domain: ").append(domain);
      sb.append("created: ").append(created.toString());
      sb.append("updated: ").append(updated.toString());
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
         
}
