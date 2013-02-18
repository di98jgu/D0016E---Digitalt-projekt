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
package database;

import android.content.Context;

/**
 * Table snowdata manager. Each row in snowdata contain a position and and
 * set of data measured at that position. The number of row is thus equal to
 * the number of positions. 
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class Snowdata extends Snowflake {
   
   /** Name of this table, defined in SnowflakeHelper */
   public static final String TABLE_NAME = SnowflakeHelper.TABLE_SNOW;
   
   /** ID, this is always the primary key in all tables */
   public static final String ID = SnowflakeHelper.ID;
   /** Time of last update */
   public static final String TIMESTAMP = SnowflakeHelper.TIMESTAMP;
   /** Snowvalue of type string */
   public static final String STR_VALUE_A = SnowflakeHelper.STR_VALUE_A;
   /** Snowvalue of type string */
   public static final String STR_VALUE_B = SnowflakeHelper.STR_VALUE_B;
   /** Snowvalue of type int */
   public static final String INT_VALUE_A = SnowflakeHelper.INT_VALUE_A;
   /** Snowvalue of type int */
   public static final String INT_VALUE_B = SnowflakeHelper.INT_VALUE_B;
   
   /** 
    * Initialize our database snowflake and table snowdata. The database
    * must be opened before any calls and should be closed when work is
    * done. 
    * 
    * @param ctxt Context
    */
   public Snowdata(Context ctxt) {
      
      super(ctxt);
      
      String[] columns = {STR_VALUE_A, STR_VALUE_B, INT_VALUE_A, INT_VALUE_B};
      
      super.field = new SnowflakeFields(TABLE_NAME, columns);
     
      
   }
   
   

}
