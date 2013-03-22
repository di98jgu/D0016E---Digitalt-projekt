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
import android.widget.ImageView;
import android.widget.TextView;
import database.Snowdata;
import database.Snowsensor;

/**
 * View details about a location. A location is position with a set of data.
 * The information is read only, 
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class LocationDetailsActivity extends Activity {
	
	/** Location details id tag for use in communication between activities */
	public final static String ID_LOCATION_DETAILS = "dicnix.id_location_details";
	public final static String[] columns1 = {Snowsensor.ID, Snowsensor.SERIAL, Snowsensor.NAME, Snowsensor.LOCATION, Snowsensor.LATITUDE, Snowsensor.LONGITUDE, Snowsensor.TYPENAME, Snowsensor.DEPLOYEDSTATE, Snowsensor.VISIBILITY, Snowsensor.INFO, Snowsensor.DOMAIN, Snowsensor.CREATED, Snowsensor.UPDATED};
	public final static String[] columns2 = {Snowdata.ID, Snowdata.SERIAL, Snowdata.INFO, Snowdata.SHOVELED, Snowdata.WEIGHT, Snowdata.DEPTH, Snowdata.TEMPERATURE, Snowdata.HUMIDITY, Snowdata.DATA_TIME, Snowdata.TIMESTAMP};

    Cursor returnedCursor1 = null;
    Cursor returnedCursor2 = null;
	
	ContentValues cv1 = new ContentValues(13);
	ContentValues cv2 = new ContentValues(10);
	

	protected DicNixApp application;
	
	/**
    * Start of this activity.
    * 
    */
   @Override
   public void onCreate(Bundle state) {
      
      super.onCreate(state);
      setContentView(R.layout.location_details_view);
      application = (DicNixApp) getApplication();
      
      Intent i = getIntent();   
      long clicked_item_id = i.getLongExtra("clicked_item_id", 0);
      String sensor_serial = i.getStringExtra("sensor_serial");
      String location_id = String.valueOf(clicked_item_id);      
 
           
      /**
       * Create columns for database
       */
      application.snowsensor.setColumns(columns1);  
      application.snowdata.setColumns(columns2);  
    	  
    
      
      /**
       * Query database for wanted information
       */
      returnedCursor1 = application.snowsensor.select("serial = " + sensor_serial, null);
      returnedCursor2 = application.snowdata.select("serial = " + sensor_serial, null);
      
      LocationDetailsAdp details = new LocationDetailsAdp(returnedCursor1, returnedCursor2);
      details.populate(location_id);   
      
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
    * @author Jim Gunnarsson, di98jgu (edited by Viktor Stärn)
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
	      private TextView shoveled = null;
	      private TextView weight = null;
	      private TextView depth = null;
	      private TextView temperature = null;
	      private TextView humidity = null;
	      private TextView data_time = null;
	      
	      Cursor returnedCursor1 = null;
	      Cursor returnedCursor2 = null;
      
	      /**
	       * Create a new container for location details view
	       */
	      public LocationDetailsAdp(Cursor data1, Cursor data2) {
	         
	    	 this.returnedCursor1 = data1;
	    	 this.returnedCursor2 = data2;
	    	  
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
	         shoveled = (TextView) findViewById(R.id.shoveled);
	         weight = (TextView) findViewById(R.id.weight);
	         depth = (TextView) findViewById(R.id.depth);
	         temperature = (TextView) findViewById(R.id.temperature);
	         humidity = (TextView) findViewById(R.id.humidity);
	         data_time = (TextView) findViewById(R.id.data_time);
	         
	         return;


      }
      
	      /**
	       * Populate location details view.
	       */
	      public void populate(String clicked_item_id) {
	    	  
	         returnedCursor1.moveToFirst();
	         returnedCursor2.moveToFirst();
	       
	         img.setImageResource(R.drawable.igloo);
	         id.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("id")));
	         serial.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("serial")));
	         name.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("name")));
	         location.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("location")));
	         latitude.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("latitude")));
	         longitude.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("longitude")));
	         typename.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("typename")));
	         deployedstate.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("deployedstate")));
	         info.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("info")));
	         domain.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("domain")));
	         created.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("created")));
	         updated.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("updated")));
	         visibility.setText(returnedCursor1.getString(returnedCursor1.getColumnIndex("visibility")));
	         
	         shoveled.setText(returnedCursor2.getString(returnedCursor2.getColumnIndex("shoveled")));
	         weight.setText(returnedCursor2.getString(returnedCursor2.getColumnIndex("weight")));
	         depth.setText(returnedCursor2.getString(returnedCursor2.getColumnIndex("depth")));
	         temperature.setText(returnedCursor2.getString(returnedCursor2.getColumnIndex("temperature")));
	         humidity.setText(returnedCursor2.getString(returnedCursor2.getColumnIndex("humidity")));
	         data_time.setText(returnedCursor2.getString(returnedCursor2.getColumnIndex("data_time")));
	       	         
	         
         return;

      }
      

   }
}
