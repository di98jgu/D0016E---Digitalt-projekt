package ssc;
/**
 *
 */
 
public class SSCPosition {
   
   private double latitude;
   private double longitude;
   
   public SSCPosition(double latitude, double longitude) {
      
      this.latitude = latitude;
      this.longitude = longitude;
   }
   
   public double getLatitude() {
      
      return latitude;
   }
   
   public double getLongitude() {
      
      return longitude;
   }
   
   public void setLatitude(double latitude) {
      
      this.latitude = latitude;
   }
   
   public void setLongitude(double longitude) {

      this.longitude = longitude;
   }
   
   @Override
   public String toString() {
      
      String str = 
         "Latitude: " + String.valueOf(this.latitude) +
         " Longitude: " + String.valueOf(this.longitude);
      
      return str;
      
   }
   
}
