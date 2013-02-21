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
* @author Jim Gunnarsson, di98jgu (bastardized by Viktor Stärn)
*/
public class SnowflakeHelper extends SQLiteOpenHelper {
   
   private static final String TAG = "logogram.DbRista";

   private static final String DB_FILENAME = "snowflake3.db";
   private static final int VERSION = 1;

   /** Main table for snow data */
   public static final String TABLE_SNOW = "snowtable";
   
   /** ID, this is always the primary key in all tables */
   public static final String ID = "id";
   /** Time of last update */
   public static final String TIMESTAMP = "timestamp";

   public static final String SERIAL = "serial";

   public static final String NAME = "name";

   public static final String LOCATION = "location";

   public static final String LATITUDE = "latitude";
   
   public static final String LONGITUDE = "longitude";

   public static final String TYPENAME = "typename";

   public static final String DEPLOYEDSTATE = "deployedstate";

   public static final String VISIBILITY = "visibility";

   public static final String INFO = "info";

   public static final String DOMAIN = "domain";

   public static final String CREATED = "created";

   public static final String UPDATED = "updated";
   
   
   
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

	  String tableData = "CREATE TABLE " + TABLE_SNOW + " ( " + 
	  ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
	  + TIMESTAMP + " TEXT NOT NULL, " + SERIAL 
	  + " TEXT NOT NULL, " + NAME + " TEXT NOT NULL, " 
	  + LOCATION + " TEXT NOT NULL, " + LATITUDE 
	  + " TEXT NOT NULL, " + LONGITUDE + " TEXT NOT NULL, " + 
	  TYPENAME + " TEXT NOT NULL, " + DEPLOYEDSTATE + 
	  " TEXT NOT NULL, " + VISIBILITY + " TEXT NOT NULL, " + 
	  INFO + " TEXT NOT NULL, " + DOMAIN + " TEXT NOT NULL, " + 
	  CREATED + " TEXT NOT NULL, " + UPDATED + " TEXT NOT NULL );";
	   
	  db.execSQL(tableData);	  
        
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
   synchronized public void onReset() {

      SQLiteDatabase db = getWritableDatabase();

      db.execSQL("DROP TABLE IF EXISTS ?", new Object[] {TABLE_SNOW});

      onCreate(db);
      
      return;
      
   }

}