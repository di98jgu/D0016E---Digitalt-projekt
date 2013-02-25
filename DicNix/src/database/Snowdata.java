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
   
   /** Name of this table, defined in SnowflakeHelper */
   public static final String TABLE_NAME = SnowflakeHelper.TABLE_SNOW;
   
   /** ID, this is always the primary key in all tables */
   public static final String ID = SnowflakeHelper.ID;
   /** Time of last update */
   public static final String TIMESTAMP = SnowflakeHelper.TIMESTAMP;

   public static final String SERIAL = SnowflakeHelper.SERIAL;

   public static final String NAME = SnowflakeHelper.NAME;

   public static final String LOCATION = SnowflakeHelper.LOCATION;

   public static final String LATITUDE = SnowflakeHelper.LATITUDE;

   public static final String LONGITUDE = SnowflakeHelper.LONGITUDE;

   public static final String TYPENAME = SnowflakeHelper.TYPENAME;

   public static final String DEPLOYEDSTATE = SnowflakeHelper.DEPLOYEDSTATE;

   public static final String VISIBILITY = SnowflakeHelper.VISIBILITY;

   public static final String INFO = SnowflakeHelper.INFO;

   public static final String DOMAIN = SnowflakeHelper.DOMAIN;

   public static final String CREATED = SnowflakeHelper.CREATED;

   public static final String UPDATED = SnowflakeHelper.UPDATED;
   
   /**
    * Initialize our database snowflake and table snowdata. The database
    * must be opened before any calls and should be closed when work is
    * done.
    *
    * @param ctxt Context
    */
   public Snowdata(Context ctxt) {
      
      super(ctxt);
      
      String[] columns = {SERIAL, NAME, LOCATION, LATITUDE, LONGITUDE, TYPENAME, DEPLOYEDSTATE, VISIBILITY, INFO, DOMAIN, CREATED, UPDATED};
      
      super.field = new SnowflakeFields(TABLE_NAME, columns);
     
      
   }
   
   

}