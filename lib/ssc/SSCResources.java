package ssc;
/**
 * 
 */

public final class SSCResources {
   
   public static final class Url {
         
      public static final String HTTPS_SSC = 
         "https://ip30.csse.tt.ltu.se/ssc/api/basic/";
      
      public static final String GET_SENSOR_LIST = 
         "sensor/list";
      
      public static final String GET_FREE_TEXT = 
         "free_text/request";
      
      public static final String GET_SNOWPRESURE = 
         "snow_pressure/request";
      
      public static final String GET_GEO_LOCATION = 
         "geo_location/request";
      
      public static final String GET_TEMPERATURE = 
         "temperature/request";
         
   }
   
   public static final class Query {
      
      public static final String RESPONSE = "response";
      
      public static final String SENSORS = "sensors";
      
      public static final String PUBLIC_SENSORS = "public";
      
      public static final String FORMAT = "format";
      
      public static final String SENSOR = "sensor";
      
      public static final String FIELDS = "fields";
      
      public static final String PERIOD = "period";
      
      public static final String START_OF = "start";
      
      public static final String END_OF = "end";
      
      public static final String LIMIT = "limit";
      
      public static final String SORT = "sort";
   
   }
   
   public static enum Period { 
      HOUR("last-hour"), 
      DAY("last-day"),
      WEEK("last-week"),
      MONTH("last-month"),
      YEAR("last-year");
      
      private String value;
      
      private Period(String value) {
         
         this.value = value;
      }
      
      public String getField() {
         
         return this.value;
      }
      
      public static Period getState(String field) {
         
         if (field == null) {
            return null;
         }
         
         for (Period i: Period.values()) {
            
            if (field.equalsIgnoreCase(i.value)) {
               return i;
            }
         }
         
         // Not matching value found so...
         throw new SSCException.MalformedData(
            "Period: " + field + " is not a valid time period");
            
      }
   }
   
   public static class Field {
      
      public static final String SERIAL = "serial";
      
      public static final String DEPLOYED_STATE = "deployed_state";
      
      public static final String LONGITUDE = "longitude";
      
      public static final String UPDATED = "updated";
      
      public static final String CREATED = "created";
      
      public static final String DOMAIN = "domain";
      
      public static final String INFO = "info";
      
      public static final String VISIBILITY = "visibility";
      
      public static final String TYPE_NAME = "type_name";
      
      public static final String LATITUDE = "latitude";
      
      public static final String NAME = "name";
      
      public static final String LOCATION = "location";
      
      public static final String SHOVELD = "shoveld";
      
      public static final String WEIGHT = "weigh";
      
      public static final String DEPTH = "depth";
      
      public static final String TEMPERATURE = "temperature";
      
      public static final String HUMIDITY = "humidity";
      
      public static final String DATA_TIME = "data_time";
      
   }
   
   public static enum TypeName { 
      FREETEXT("FreeText"), 
      TEMPERATURE("Temperature"), 
      MOTION("Motion"), 
      GEOLOCATION("GeoLocation"), 
      SNOWPRESSURE("SnowPressure");
      
      private String value;
      
      private TypeName(String value) {
         
         this.value = value;
      }
      
      public String getField() {
         
         return this.value;
      }
      
      public static TypeName getState(String field) {
         
         if (field == null) {
            return null;
         }
         
         for (TypeName i: TypeName.values()) {
            
            if (field.equalsIgnoreCase(i.value)) {
               return i;
            }
         }
         
         // Not matching value found so...
         throw new SSCException.MalformedData(
            "TypeName: " + field + " is not a valid type_name");
            
      }
   }
   
   /**
    * 
    */
   public static enum DeployedState {
      DEPLOYED("DEPLOYED"), 
      NOT_DEPLOYED("NOT_DEPLOYED");
      
      private String value;
      
      private DeployedState(String value) {
         
         this.value = value;
      }
      
      public String getField() {
         
         return this.value;
      }
      
      public static DeployedState getState(String field) {
         
         if (field == null) {
            return null;
         }
         
         for (DeployedState i: DeployedState.values()) {
            
            if (field.equalsIgnoreCase(i.value)) {
               return i;
            }
         }
         
         // Not matching value found so...
         throw new SSCException.MalformedData(
            "DeployedState: " + field + " is not a valid deploy state");
            
      }
   }

}
