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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
* Helper class for creating and updating the snowflake database. This
* class is also a name manifest for all tables and columns. New subclasses
* require update of this class.
*
* @author Jim Gunnarsson, di98jgu (edited by Viktor Stärn)
*/
public class SnowflakeHelper extends SQLiteOpenHelper {
   
   private static final String TAG = "logogram.DbRista";

   private static final String DB_FILENAME = "snowflake.db";
   private static final int VERSION = 1;

   /** Table for snow data */
   public static final String TABLE_SNOW = "snowtable";
   /** Table for sensor data */
   public static final String TABLE_SENSOR = "sensortable";
   
   /** ID, this is always the primary key in all tables */
   public static final String ID = "_id";
   /** The Unix epoch, for sorting by time  */
   public static final String TIMESTAMP = "timestamp";
   /** Serial number of measurement-point */
   public static final String SERIAL = "serial";
   /** Name of measurement-point */
   public static final String NAME = "name";
   /** Location of measurement-point */
   public static final String LOCATION = "location";
   /** Latitude of measurement-point */
   public static final String LATITUDE = "latitude";
   /** Longitude of measurement-point */
   public static final String LONGITUDE = "longitude";
   /** Type of measurement-point */
   public static final String TYPENAME = "typename";
   /** State of measurement-point */
   public static final String DEPLOYEDSTATE = "deployedstate";
   /** Visible for other domains i.e. public or private */
   public static final String VISIBILITY = "visibility";
   /** Info about measurement-point */
   public static final String INFO = "info";
   /** Domain to which this sensor belongs */
   public static final String DOMAIN = "domain";
   /** Measurement-point time of creation */
   public static final String CREATED = "created";
   /** Time when measurement-point was last updated*/
   public static final String UPDATED = "updated";
   /** Snow shoveled at measurement-point */
   public static final String SHOVELED = "shoveled";
   /** Snow weight at measurement-point */
   public static final String WEIGHT = "weight";
   /** Snow depth at measurement-point */
   public static final String DEPTH = "depth";
   /** Temperature at measurement-point */
   public static final String TEMPERATURE = "temperature";
   /** Humidity at measurement-point */
   public static final String HUMIDITY = "humidity";
   /** Data time */
   public static final String DATA_TIME = "data_time";
   
   
   /**
    * New table helper.
    *
    * @param ctxt Context
    */
   public SnowflakeHelper(Context ctxt) {
      
      super(ctxt, DB_FILENAME, null, VERSION);
      
   }
   

   /**
    * Create a new set of table(s). It is assumed that the db file is
    * clean of any tables.
    *
    * @param db Database handle.
    * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
    */
   @Override
   public void onCreate(SQLiteDatabase db) {

	  String table1Data = 
			  "CREATE TABLE " + 
			  TABLE_SENSOR + 
			  " ( " + 
			  ID + 
			  " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			  SERIAL + 
			  " TEXT UNIQUE, " + 
			  NAME + 
			  " TEXT, " + 
			  LOCATION + 
			  " TEXT, " + 
			  LATITUDE + 
			  " TEXT, " + 
			  LONGITUDE + 
			  " TEXT, " + 
			  TYPENAME + 
			  " TEXT, " + 
			  DEPLOYEDSTATE + 
			  " TEXT, " + 		
			  VISIBILITY +
			  " TEXT, " +
			  INFO + 
			  " TEXT, " + 
			  DOMAIN + 
			  " TEXT, " + 
			  CREATED + 
			  " TEXT, " + 
			  UPDATED + 
			  " TEXT );";
	   
	  db.execSQL(table1Data);	  
        
	  String table2Data = 
			  "CREATE TABLE " + 
			  TABLE_SNOW + 
			  " ( " + 
			  ID + 
			  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			  SERIAL +	
			  " TEXT NOT NULL, " + 
			  SHOVELED +	
			  " TEXT, " + 
			  WEIGHT +	
			  " TEXT, " + 
			  DEPTH +	
			  " TEXT, " + 
			  TEMPERATURE +	
			  " TEXT, " + 
			  HUMIDITY +	
			  " TEXT, " + 
			  DATA_TIME +	
			  " TEXT, " + 
			  TIMESTAMP + 
			  " TEXT, " + 
			  " FOREIGN KEY ("+SERIAL+") REFERENCES "+TABLE_SENSOR+"("+SERIAL+"));";
	   
	  db.execSQL(table2Data);	
	  
	  db.execSQL("PRAGMA foreign_keys = ON;");
	  
	  Log.d(TAG, "New tables created for snowflake database");
	  
      return;

   }

   /**
    * Update of database, not currently implemented.
    *
    * @param db Database handle
    * @param old_version Current version
    * @param new_version New version
    * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
    */
   @Override
   public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
     
      // Nothing to do here yet...
      
      if (VERSION < new_version) {
         Log.d(TAG, "Change of version number in our database!");
      }
         
      return;

   }
   
   /**
    * Manually reset table(s) to initial state. All data currently stored
    * in the database will be lost.
    */
   public synchronized void onReset() {

      SQLiteDatabase db = getWritableDatabase();

      db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNOW);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR);
      
      Log.d(TAG, "Droped all tables in snowflake database");
      
      onCreate(db);
      
      return;
      
   }

}
