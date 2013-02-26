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


package se.ltu.dicnix;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import database.Snowdata;
//import database.*;

/**
 * View details about a location. A location is position with a set of data.
 * The information is read only, 
 * 
 * @author Jim Gunnarsson, di98jgu (edited by Viktor Stärn)
 */
public class LocationDetailsActivity extends Activity {
	
	/** Location details id tag for use in communication between activity's */
	public final static String ID_LOCATION_DETAILS = "dicnix.id_location_details";
	public final static String[] columns = {Snowdata.ID, Snowdata.TIMESTAMP, Snowdata.SERIAL, Snowdata.NAME, Snowdata.LOCATION, Snowdata.LATITUDE, Snowdata.LONGITUDE, Snowdata.TYPENAME, Snowdata.DEPLOYEDSTATE, Snowdata.VISIBILITY, Snowdata.INFO, Snowdata.DOMAIN, Snowdata.CREATED, Snowdata.UPDATED};

    Cursor returnedCursor = null;
	
	ContentValues cv = new ContentValues(14);
	
    
	/**
    * Start of this activity.
    * 
    */
   @Override
   public void onCreate(Bundle state) {
      
      super.onCreate(state);
      setContentView(R.layout.location_details_view);
      
      Intent i = getIntent();   
      long clicked_item_id = i.getLongExtra("clicked_item_id", 0);
      String location_id = String.valueOf(clicked_item_id);      
     
      /**
       * Create database
       */
      Snowdata DB = new Snowdata(getApplicationContext());
      
      /**
       * Open database
       */
      DB.open();
      
      /**
       * Create columns for database
       */
      DB.setColumns(columns);  
     
      
      /**
       * Insert test-data into database
       */
//      cv.put(columns[0], "1");
//      cv.put(columns[1], "00:03");
//      cv.put(columns[2], "SKE-824224");
//      cv.put(columns[3], "Treriksröset");
//      cv.put(columns[4], "None Given");
//      cv.put(columns[5], "69.06");
//      cv.put(columns[6], "20.5486");
//      cv.put(columns[7], "SnowPressure");
//      cv.put(columns[8], "DEPLOYED");
//      cv.put(columns[9], "1");
//      cv.put(columns[10], "None Given");
//      cv.put(columns[11], "ThomasDomain");
//      cv.put(columns[12], "2013-02-21 11:36:25");
//      cv.put(columns[13], "2013-02-21 11:36:25");
//      
//      DB.insert(cv);

      /**
       * Query database for all of its information
       */
      returnedCursor = DB.all();
      
      /**
       * Send information to LocationDetailsAdp for presentation
       */
      LocationDetailsAdp details = new LocationDetailsAdp(returnedCursor);      
      details.populate(location_id);
      
      /**
       * Close database
       */
      DB.close();

   }
	
   /**
    * Nothing to destroy yet.
    */
   @Override
   public void onDestroy() {

      super.onDestroy();

   }
   
   /**
    * Container for all information about a selected data point. Represent
    * the location_details_view.
    * 
    * @author Jim Gunnarsson, di98jgu
    */
   private class LocationDetailsAdp {
	      
	  private ImageView img = null;
      private TextView serial = null;
      private TextView name = null;
      private TextView location = null;
      private TextView latitude = null;
      private TextView longitude = null;
      private TextView typename = null;
      private TextView deployedstate = null;
      private TextView visibility = null;
      private TextView info = null;
      private TextView domain = null;
      private TextView created = null;
      private TextView updated = null;
      private TextView id = null;
      
      Cursor returnedCursor = null;
      
      /**
       * Create a new container for location details view
       */
      public LocationDetailsAdp(Cursor data) {
         
    	 this.returnedCursor = data;
    	  
         img = (ImageView) findViewById(R.id.snow_img);
         serial = (TextView) findViewById(R.id.serial);
         name = (TextView) findViewById(R.id.name);
         location = (TextView) findViewById(R.id.location);
         latitude = (TextView) findViewById(R.id.latitude);
         longitude = (TextView) findViewById(R.id.longitude);
         typename = (TextView) findViewById(R.id.type_name);
         deployedstate = (TextView) findViewById(R.id.deployed_state);
         visibility = (TextView) findViewById(R.id.visibility);
         info = (TextView) findViewById(R.id.info);
         domain = (TextView) findViewById(R.id.domain);
         created = (TextView) findViewById(R.id.created);
         updated = (TextView) findViewById(R.id.updated);
         id = (TextView) findViewById(R.id.location_id);
    	 
         
         return;

      }
      
      /**
       * Populate location details view.
       */
      public void populate(String clicked_item_id) {
         String location_id = clicked_item_id;
    	  
         returnedCursor.moveToFirst();
       
         img.setImageResource(R.drawable.igloo);
         serial.setText(returnedCursor.getString(returnedCursor.getColumnIndex("serial")));
         name.setText(returnedCursor.getString(returnedCursor.getColumnIndex("name")));
         location.setText(returnedCursor.getString(returnedCursor.getColumnIndex("location")));
         latitude.setText(returnedCursor.getString(returnedCursor.getColumnIndex("latitude")));
         longitude.setText(returnedCursor.getString(returnedCursor.getColumnIndex("longitude")));
         typename.setText(returnedCursor.getString(returnedCursor.getColumnIndex("typename")));
         deployedstate.setText(returnedCursor.getString(returnedCursor.getColumnIndex("deployedstate")));
         visibility.setText(returnedCursor.getString(returnedCursor.getColumnIndex("visibility")));
         info.setText(returnedCursor.getString(returnedCursor.getColumnIndex("info")));
         domain.setText(returnedCursor.getString(returnedCursor.getColumnIndex("domain")));
         created.setText(returnedCursor.getString(returnedCursor.getColumnIndex("created")));
         updated.setText(returnedCursor.getString(returnedCursor.getColumnIndex("updated")));

         id.setText("Clicked item id: " + location_id);
         
         return;

      }
      


   }
   
   /** Called when the user clicks the Back button */
   public void backMethod(View view) {
   		finish();
   }
}
