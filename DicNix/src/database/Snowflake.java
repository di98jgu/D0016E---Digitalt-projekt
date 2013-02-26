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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
* The snowflake database manager. This is the baseclass for all database
* manager using the snowflake database. Basic functions for select, insert
* update and delete is provided. SQL terminology is used here in favour of
* Android.
*
* The subclass may overwrite or extends this basic functions. Management of
* columns and pointers is not implemented here. Exception is the setColumns
* function needed for user to selection which columns to return.
*
* The subclass must use SnowflakeFields and set the field variable in this
* class. This should be improved with something more safe but for now this get
* things done.
*
* @author Jim Gunnarsson, di98jgu
*/
public abstract class Snowflake {
   
   /** This is snowflake our database */
   protected SQLiteDatabase db = null;
   /** Help class to snowflake our database */
   private SnowflakeHelper db_helper = null;
   /** Manager class for inquiries */
   protected SnowflakeFields field = null;
   
   
   /**
    * Initialise our database.
	*
	* @param ctxt Context
	*/
   protected Snowflake(Context ctxt) {
      
      db_helper = new SnowflakeHelper(ctxt);
      
   }
   
   /**
    * Open or create this database snowflake. Make sure you close it when
    * you are done.
    *
    * @exception SQLiteException - If snowflake can't be opened
    */
   public void open() {
      
      db = db_helper.getWritableDatabase();
      
   }
   
   /**
    * Close this database.
    */
   public void close() {
      
      db.close();
      
      return;
      
   }
   /**
    * Set which columns should be returned when method select is called.
    *
    * @param columns
    */
   public void setColumns(String[] columns) {
      
      field.setColumns(columns);
      
      return;
      
   }
   
   
   /**
    * Get all rows for current table. Table is set by subclass and can't be
    * changed. See get table for current working table.
    *
    * @return Cursor with all rows
    */
   public Cursor all() {
      
      // Skip a few method calls
      return db.queryWithFactory(
            null, // No factory
            false, // Accept identical rows
            field.getTable(),
            field.columns,
            null,
            null,
            null,
            null,
            field.order_by,
            null); // No limit
      
      
   }
   
   
   /**
    * Select data from database, this is a query in Android terminology.
    *
    * @param selection The WHERE clause, ?s can be used as placeholders for
    * arguments
    * @param args Arguments replacing ?s
    *
    * @return Cursor with selected rows.
    */
   public Cursor select(String selection, String[] args) {
      
      // Skip a few method calls
      return db.queryWithFactory(
            null, // No factory
            false, // Accept identical rows
            field.getTable(),
            field.columns,
            selection,
            args,
            null,
            null,
            field.order_by,
            null); // No limit
      
   }
   
   
   /**
    * Insert values into snowflake database. Which table used is determined by
    * the subclass. An empty row is not allowed.
    *
    * @param content Row to insert, keys must match columns name
    *
    * @return The row ID or -1 if failed
    */
   public long insert(ContentValues values) {
      
      return db.insert(
    		field.getTable(),
            null,
            values);

   }
   
   /**
    * Delete row given row ID from snowflake database.
    *
    * @param row Row ID, row to be removed
    *
    * @return True if successful
    */
   public boolean delete(long row) {
      
      String at = SnowflakeHelper.ID + " = " + row;
      
      return db.delete(
            field.getTable(),
            at,
            null) > 0;
      
   }
   
   /**
    * Delete selected rows from database. Be careful, passing null as
    * selection will erase all data.
    *
    * @param selection WHERE clause, placeholders ? might be used
    * @param args Arguments replacing ?s
    *
    * @return Number of rows removed. A trick: To remove all rows and get a
    * count, pass "1" as WHERE clause
    */
   public int delete(String selection, String[] args) {
      
      return db.delete(
            field.getTable(),
            selection,
            args);
      
   }
   
   /**
    * Update a row in database given new values and a row ID.
    *
    * @param values New values, keys must match columns name
    * @param row Row ID
    *
    * @return True if successful
    */
   public boolean update(ContentValues values, long row) {
      
      String at = SnowflakeHelper.ID + " = " + row;
      
      return db.updateWithOnConflict(
            field.getTable(),
            values,
            at,
            null,
            SQLiteDatabase.CONFLICT_NONE) > 0;
      
   }
   
   /**
    * Update a range of rows given new values and selection.
    *
    * @param values New values, keys must match columns name
    * @param selection WHERE clause, placeholders ? might be used
    * @param args Arguments replacing ?s
    *
    * @return Return Number of row updated
    */
   public int update(ContentValues values, String selection, String[] args) {
      
      return db.updateWithOnConflict(
            field.getTable(),
            values,
            selection,
            args,
            SQLiteDatabase.CONFLICT_NONE);
      
   }
   
}


