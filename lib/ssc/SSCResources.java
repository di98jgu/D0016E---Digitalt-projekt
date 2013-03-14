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
 * Class SSCResources
 * 
 * Collection of all constant values used for communication with Sense
 * Smart City and management of data. Constants is grouped in Url,
 * Query and Field. Certain fields and query have a fixed set of valid
 * values and those have been wrapped into enumerators.
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public final class SSCResources {
   
   /**
    * A url is build up of two parts, a absolute base url and a 
    * relative Url. Each relative Url corresponds to a specific 
    * GET or POST request. 
    */
   public static final class Url {
      
      /** Base url to Sense Smart City */
      public static final String HTTPS_SSC = 
         "https://ip30.csse.tt.ltu.se/ssc/api/basic/";
      
      /** List of sensors */
      public static final String GET_SENSOR_LIST = 
         "sensor/list";
      
      /** Description of sensor(s). Note this field is not mandatory,
          it always exist but may not contain any data. */
      public static final String GET_FREE_TEXT = 
         "free_text/request";
      
      /** List of readings from sensor. */
      public static final String GET_SNOWPRESURE = 
         "snow_pressure/request";
      
      /** Current position of sensor, used for measure the movement 
          of snow */
      public static final String GET_GEO_LOCATION = 
         "geo_location/request";
      
      /** Temperature only */
      public static final String GET_TEMPERATURE = 
         "temperature/request";
         
   }
   
   /**
    * This is the keys for the query string following the url. Which
    * key(s) is relevant and allowed depend on the type of request.
    */ 
   public static final class Query {
      
      /** A successful query yields in case JSON is requested a JSON
          object containing exactly one key value pair. This is the 
          key. The value is the requested data. */ 
      public static final String RESPONSE = "response";
      
      /** JSON formatted list of serials sensors requested */
      public static final String SENSORS = "sensors";
      /** Show sensors for other domains then ours, 'yes' or 'no' */
      public static final String PUBLIC_SENSORS = "public";
      /** Defines the response format to 'json' or 'xml' */
      public static final String FORMAT = "format";
      /** JSON formatted list of data fields requested, available 
          fields depend on type of sensor */
      public static final String FIELDS = "fields";
      /** Time period for data request, see Period for valid periods */
      public static final String PERIOD = "period";
      /** Start date/time for data request (YYYY-MM-DD HH:MM:SS) */
      public static final String START_OF = "start";
      /** End date/time for data request, start time mandatory  */
      public static final String END_OF = "end";
      /** Limit result set to this many records */
      public static final String LIMIT = "limit";
      /** Return data from this offset position in the possible result set */
      public static final String OFFSET = "offset";
      /** Sort data in 'desc' or 'asc' order. */
      public static final String SORT = "sort";
   
   }
   
   /** 
    * Enumerate Period
    * 
    * All possible values for key PERIOD in request. 
    */
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
      
      /**
       * Get field value of this period. 
       */
      public String getField() {
         
         return this.value;
      }
      
      /** 
       * Get state or enumerate from a given time period.
       * 
       * @param field Field value representing a valid time period
       * 
       * @return A enumerate representing the time period
       * 
       * @throws SSCException.MalformedData if field is not a valid
       * time period.
       */
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
   
   /**
    * Static class Field
    * 
    * The return value from Sense Smart City comes in two flavors 
    * xml or JSON. It is here assumed that JSON is used. The return
    * value is typically a JSONObject, a table. This is the field 
    * names, keys, used in that table.
    * 
    * Note available and valid fields depend on type of request to
    * Sense Smart City. 
    */
   public static class Field {
      
      /** Unique sensor serial number to identify the sensor */
      public static final String SERIAL = "serial";
      /** Sensor status, see enum DeployedState for possible values */
      public static final String DEPLOYED_STATE = "deployed_state";
      /** Longitude in degrees */
      public static final String LONGITUDE = "longitude";
      /** Last update of sensor */
      public static final String UPDATED = "updated";
      /** Creation time of sensor */
      public static final String CREATED = "created";
      /** Domain sensor belongs to */
      public static final String DOMAIN = "domain";
      /** Additional information field for sensor. Each data set 
          also have this field for the same purpose. */
      public static final String INFO = "info";
      /** Set visibility on sensor, i.e. public or private */
      public static final String VISIBILITY = "visibility";
      /** Information on sensor type, see enum TypeName for all types. */
      public static final String TYPE_NAME = "type_name";
      /** Latitude in degrees */
      public static final String LATITUDE = "latitude";
      /** User friendly sensor name */
      public static final String NAME = "name";
      /** User friendly information on sensor location. */
      public static final String LOCATION = "location";
      /** Have sensor location been cleaned from snow */
      public static final String SHOVELD = "shoveld";
      /** The snow weight in grams */
      public static final String WEIGHT = "weigh";
      /** The snow depth in centimeters */
      public static final String DEPTH = "depth";
      /** The temperature in celcius degrees */
      public static final String TEMPERATURE = "temperature";
      /** Percentage in RH (relative humidity) */
      public static final String HUMIDITY = "humidity";
      /** Timestamp for a reading */
      public static final String DATA_TIME = "data_time";
      
   }
   
   /** 
    * Enumerator TypeName
    * 
    * Every sensor is of a given type. Type of sensor determine which
    * data and thus fields available. This enumerator list all
    * know type of sensors. It do not make any connection between a
    * given type and valid fields.
    */
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
      
      /**
       * Get field value of this sensor type.
       * 
       * @return Field value of this sensor type
       */
      public String getField() {
         
         return this.value;
      }
      
      /** 
       * Get state or enumerate from a given time period.
       * 
       * @param field Field value representing a valid sensor type
       * 
       * @return A enumerate representing the sensor type
       * 
       * @throws SSCException.MalformedData if field is not a valid
       * sensor type.
       */
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
    * Enumerator DeployedState
    * 
    * A senor have a state of deployment. This enumerator hold all
    * valid states of a sensor. 
    */
   public static enum DeployedState {
      DEPLOYED("DEPLOYED"), 
      NOT_DEPLOYED("NOT_DEPLOYED");
      
      private String value;
      
      private DeployedState(String value) {
         
         this.value = value;
      }
      
      /**
       * Get field value of deployment state
       * 
       * @return Field value of this sensor type
       */
      public String getField() {
         
         return this.value;
      }
       
      /** 
       * Get state or enumerate from a given deployment state
       * 
       * @param field Field value representing a valid deployment state
       * 
       * @return A enumerate representing the deployment state
       * 
       * @throws SSCException.MalformedData if field is not a valid
       * deployment state
       */
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
