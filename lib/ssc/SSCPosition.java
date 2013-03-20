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
 * Class SSCPosition
 * 
 * Represent a position in SSC. Position system used is latitude and 
 * longitude in degrees using decimal notation. Altitude is not used
 * in SSC and thus not part of a position.
 * 
 * Snow movement is measured in SSC and thus distance and direction is
 * of interest. There might be a need for transformation of this 
 * position to another position system. For display on a map to take
 * an example. None of this is implemented.
 * 
 * @author Jim Gunnarsson, di98jgu
 * 
 */
public class SSCPosition {
   
   private double latitude;
   private double longitude;
   
   /**
    * Create a new position given latitude longitude.
    * 
    * @param latitude Latitude in degrees
    * @param longitude Longitude in degrees
    */
   public SSCPosition(double latitude, double longitude) {
      
      this.latitude = latitude;
      this.longitude = longitude;
   }
   
   /**
    * Get latitude.
    * 
    * @return Latitude in degrees
    */
   public double getLatitude() {
      
      return latitude;
   }
   
   /**
    * Get longitude.
    * 
    * @return Longitude in degrees
    */
   public double getLongitude() {
      
      return longitude;
   }
   
   /** 
    * Set latitude 
    * 
    * @param latitude Latitude in degrees
    */
   public void setLatitude(double latitude) {
      
      this.latitude = latitude;
   }
   
   /**
    * Set longitude 
    * 
    * @param longitude Longitude in degrees
    */
   public void setLongitude(double longitude) {

      this.longitude = longitude;
   }
   
   /** 
    * Roll this position to a string
    * 
    * @return This position as a string
    */
   @Override
   public String toString() {
      
      String str = 
         "Latitude: " + String.valueOf(this.latitude) +
         " Longitude: " + String.valueOf(this.longitude);
      
      return str;
      
   }
   
}
