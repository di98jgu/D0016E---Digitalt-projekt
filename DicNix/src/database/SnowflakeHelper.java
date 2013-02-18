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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Helper class for creating and updating the snowflake database. This
 * class is also a name manifest for all tables and columns. New subclasses
 * require update of this class. 
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class SnowflakeHelper extends SQLiteOpenHelper {
   
   private static final String TAG = "logogram.DbRista";

   private static final String DB_FILENAME = "snowflake.db";
   private static final int VERSION = 1;

   /** Main table for snow data */
   public static final String TABLE_SNOW = "snowtable";

   /** ID, this is always the primary key in all tables */
   public static final String ID = "_id";
   /** Time of last update */
   public static final String TIMESTAMP = "timestamp";
   /** Snowvalue of type string */
   public static final String STR_VALUE_A = "str_value_a";
   /** Snowvalue of type string */
   public static final String STR_VALUE_B = "str_value_b";
   /** Snowvalue of type int */
   public static final String INT_VALUE_A = "int_value_a";
   /** Snowvalue of type int */
   public static final String INT_VALUE_B = "int_value_b";
   
   
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
      
      db.execSQL(
            "CREATE TABLE ? " +
            "(? INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "? TEXT, " +
            "? TEXT, " +
            "? TEXT, " +
            "? INTEGER, " +
            "? INTEGER)", 
            new Object[] {
                  TABLE_SNOW, 
                  ID, 
                  TIMESTAMP, 
                  STR_VALUE_A, 
                  STR_VALUE_B, 
                  INT_VALUE_A, 
                  INT_VALUE_B});
         
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