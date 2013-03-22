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

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Date and time is always in the format yyyy-MM-dd HH:mm:ss, 
 * example '1987-01-23 18:19:34'. This format is used by Sense Smart 
 * City for all timestamps. It is the SSC time unit.
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class SSCTimeUnit {
   
   /** All dates is in this form only */
   protected final SimpleDateFormat time_format = 
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      
   /** This is the date this class represent */
   protected Date time_unit = null;
   
   /**
    * Constructs a time unit object given a time string. The time is 
    * strictly in the form '1987-01-23 18:19:34'. Any other format
    * result in a MalformedData exception. This format is set by the
    * Sense Smart City API. 
    * 
    * Note that toString result in a valid time string.
    * 
    * @param time_str The date and time as a string
    * 
    * @throws SSCException.MalformedData If time string is not in
    * proper format
    */
   public SSCTimeUnit(String time_str) {
      
      try {
         
         this.time_unit = time_format.parse(time_str);
         
      } catch (java.text.ParseException e) {
         
         throw new SSCException.MalformedData(
            "SSCTimeUnit: " + time_str + " is not a valid time string");
         
      }
      
   }
   
   /**
    * Get time, example '18:19:34', for this time unit. 
    * 
    * @return Time for this time unit
    */
   public String getTime() {
      
      SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
      
      return time.format(time_unit).toString();
      
   }
   
   /**
    * Get date, example '1987-01-23', for this time unit. 
    * 
    * @return Date for this time unit
    */
   public String getDate() {
      
      SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
      
      return time.format(time_unit).toString();
   }
   
   /**
    * Get Unix epoch for this time unit.
    * 
    * @return Epoch for this time unit
    */
   public long getEpoch() {
   
      return this.time_unit.getTime() / 1000;
   
   }
   
   /**
    * Return the difference in seconds between this time unit and
    * specified unit. If this unit is greater, of later date, then
    * specified unit the result is positive. 
    * 
    * Rounding errors may occur.
    * 
    * @param unit Source TimeUnit 
    * 
    * @return Difference in seconds between this time unit and source 
    * time unit.
    */
   public long diffSeconds(SSCTimeUnit unit) {
      
      long diff = 
         this.time_unit.getTime() - unit.time_unit.getTime();
         
      return (long) diff/1000;
      
   }
   
   /**
    * Return time and date for this unit in the form 
    * '1987-01-23 18:19:34'
    * 
    * @return String with date and time
    */
   @Override
   public String toString() {
      
      return time_format.format(time_unit).toString();
      
   }
   
}
