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

import java.util.HashMap;
import java.util.Map;

/**
 * Field container. The snowflake class make use of this container everywhere
 * a query is needed. The subclass of snowflake is expected to fill this
 * container with acceptable values. Note no check is done here, except for
 * for set columns to insure that a proper subset is provided. 
 *
 * @author Jim Gunnarsson, di98jgu
 */
public class SnowflakeFields {
   
   /** This table */
   private String table;
   /** List of columns to return selected by user of snowflake */
   public String[] columns = null;
   /** Map of all columns in this table */
   private Map<Integer, String> columns_all = null;
   /** How to order the rows */
   public String order_by = null; 
   
   /**
    * Create a new container for snow fields. 
    *
    * @param table Table name
    * @param columns_all A array of all columns
    */
   public SnowflakeFields(String table, String[] columns_all) {
      
      this.table = table;
      this.columns_all = new HashMap<Integer, String>();
      
      for (int i = 0; i < columns_all.length; i++) {
         
         this.columns_all.put(new Integer(columns_all[i].hashCode()), columns_all[i]);
         
      }
      
   }
   
   public String getTable() {
      
      return table;
      
   }
   
   /** 
    * Set a public set of columns. This set must be a subset of columns given
    * at creation of this query. Should the subset of columns be greater 
    * then the number of available columns then the set is simply truncated. No 
    * matching columns result in a null valued columns.
    * 
    * @param column_set Subset of columns
    */
   public void setColumns(String[] column_set) {
      
      if (column_set == null) {
         
         columns = null;
         return;
         
      }
      
      int len = (column_set.length < columns_all.size())? 
            column_set.length: columns_all.size();
      
      columns = new String[len];
      int count = 0;
      
      for (int i = 0; i < len; i++) {
         // We don't need to worry about null values since we can
         // trust database.sqlite to ignore them.
         columns[i] = null;
         
         Integer key = new Integer(column_set[i].hashCode());
         
         if (columns_all.containsKey(key)) {

            columns[count] = column_set[i];
            count++;
            
         }
         
      }
      
      return;
      
   }

}