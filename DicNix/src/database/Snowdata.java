/*
* This program is free software; you can redistribute it and/or modify 
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA 02110-1301, USA.
*/
package database;

import android.content.Context;

/**
* Table snowdata manager. Each row in snowdata contain a position and and
* set of data measured at that position. The number of row is thus equal to
* the number of positions.
*
* @author Jim Gunnarsson, di98jgu (edited by Viktor Stärn)
*/
public class Snowdata extends Snowflake {
   
   /** Name of sensor-table, defined in SnowflakeHelper */
   public static final String TABLE_NAME = SnowflakeHelper.TABLE_SNOW;
	
   /** Serial number of measurement-point */
   public static final String SERIAL = SnowflakeHelper.SERIAL;
   /** Visibility at measurement-point */
   public static final String VISIBILITY = SnowflakeHelper.VISIBILITY;
   /** Snow shoveled at measurement point */
   public static final String SHOVELED = SnowflakeHelper.SHOVELED;
   /** Snow weight at measurement-point */
   public static final String WEIGHT = SnowflakeHelper.WEIGHT;
   /** Snow depth at measurement-point */
   public static final String DEPTH = SnowflakeHelper.DEPTH;
   /** Temperature at measurement-point */
   public static final String TEMPERATURE = SnowflakeHelper.TEMPERATURE;
   /** Humidity at measurement-point */
   public static final String HUMIDITY = SnowflakeHelper.HUMIDITY;
   /** Data time */
   public static final String DATA_TIME = SnowflakeHelper.DATA_TIME;


   
   /**
    * Initialize our database snowflake and table snowdata. The database
    * must be opened before any calls and should be closed when work is
    * done.
    *
    * @param ctxt Context
    */
   public Snowdata(Context ctxt) {
      
      super(ctxt);
      
      String[] columns = {VISIBILITY, SHOVELED, WEIGHT, DEPTH, TEMPERATURE, HUMIDITY, DATA_TIME, SERIAL};
         
      super.field = new SnowflakeFields(TABLE_NAME, columns);
           
      
   }
   
   

}