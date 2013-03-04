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
   
   /** 
    * Private enum TypeName. This is the allowed type of sensors
    * used. 
    * 
    * */
   private enum TypeName { 
      FREETEXT("FreeText"), 
      TEMPERATURE("Temperature"), 
      MOTION("Motion"), 
      GEOLOCATION("GeoLocation"), 
      SNOWPRESSURE("SnowPressure");
      
      private String value;
      
      private TypeName(String value) {
         
         this.value = value;
      }
      
      public String getValue() {
         
         return this.value;
      }
      
      
      @Nullable
      public static TypeName getTypeName(String value) {
         
         if (value == null) {
            return null;
         }
         
         for (TypeName i: TypeName.values()) {
            
            if (value.equalsIgnoreCase(i.value)) {
               return i;
            }
         }
         
         // Not matching value found so...
         throw new SSCException.MalformedData(
            "TypeName: " + value + " is not a valid type_name");
            
      }
   }
   
   /** */
   private enum DeployedState {
      DEPLOYED("DEPLOYED"), 
      NOT_DEPLOYED("NOT_DEPLOYED");
      
      private String value;
      
      private DeployedState(String value) {
         
         this.value = value;
      }
      
      public String getValue() {
         
         return this.value;
      }
      
      
      @Nullable
      public static DeployedState getDeployedState(String value) {
         
         if (value == null) {
            return null;
         }
         
         for (DeployedState i: DeployedState.values()) {
            
            if (value.equalsIgnoreCase(i.value)) {
               return i;
            }
         }
         
         // Not matching value found so...
         throw new SSCException.MalformedData(
            "DeployedState: " + value + " is not a valid deploy state");
            
      }
   }
   
   /**  */
   private String serial = null;
   /**  */
   private String name = null;
   /**  */
   private String location = null;
   /**  */
   private SSCPosition position = null;
   /**  */
   private TypeName type_name;
   /**  */
   private DeployedState deployed_state;
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
         obj.getString(resource.Field.SERIAL);
      
      this.name = truncate(
         obj.getString(resource.Field.NAME), NAME_LENGTH);
         
      this.location = truncate(
         obj.getString(resource.Field.LOCATION), LOCATION_LENGTH);
         
      this.position = new SSCPosition(
         obj.getDouble(resource.Field.LATITUDE),
         obj.getDouble(resource.Field.LONGITUDE));
         
      this.type_name = TypeName.getTypeName(
         obj.getString(resource.Field.TYPE_NAME));
         
      this.deployed_state = DeployedState.getDeployedState(
         obj.getString(resource.Field.DEPLOYED_STATE));
         
      this.visibility = 
         obj.getBoolean(resource.Field.VISIBILITY);
      
      this.info = truncate(
         obj.getString(resource.Field.INFO), INFO_LENGTH);
         
      this.domain = 
         obj.getString(resource.Field.DOMAIN);
      
      this.created = new SSCTimeUnit(
         obj.getString(resource.Field.CREATED));
         
      this.updated = new SSCTimeUnit(
         obj.getString(resource.Field.UPDATED));
      
   }
   
   /**
    * 
    */
   public static List<Sensor> getSensors(JSONArray obj_array) {
      
      List<Sensor> sensors = new ArrayList<Sensor>();
        
      for (int i = 0; i < obj_array.length(); i++) {
         
         JSONObject obj = obj_array.getJSONObject(i);
         sensors.add(new Status(obj));
      }
        
      return sensors;
      
   }
   
   /**  
    * 
    */
   public String getSerial() {
      
      return serial();
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
      
      return type_name.getValue();
   }
   
   /**  
    * 
    */
   public String getDeployedState() {
      
      return deployed_state.getValue();
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
      
      this.type_name = TypeName.setTypeName(type_name);
   }
   
   /**  
    * 
    */
   public void setDeployedState(String deployed_state) {
      
      this.deployed_state = 
         DeployedState.setDeployedState(deployed_state);
   }
   
   /**  
    * 
    */
   public boolean setVisibility(String visibility) {
      
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
   public SSCTimeUnit setCreated(SSCTimeUnit created) {
      
      this.created = created;
   }
   
   /**  
    * 
    */
   public SSCTimeUnit setUpdated(SSCTimeUnit updated) {
      
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
      sb.append("type_name: ").append(type_name.getValue());
      sb.append("deployed_state: ").append(deployed_state.getValue);
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
