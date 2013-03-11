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
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * View details about a location. A location is position with a set of data.
 * The information is read only, 
 * 
 * @author Jim Gunnarsson, di98jgu
 */
public class LocationDetails extends Activity {
	
	/** Location details id tag for use in communication between activity's */
	public final static String ID_LOCATION_DETAILS = "dicnix.id_location_details";
	
	/**
    * Start of this activity.
    * 
    */
   @Override
   public void onCreate(Bundle state) {
      
      super.onCreate(state);
      setContentView(R.layout.location_details_view);
      
      DetailsAdp details = new DetailsAdp();
      details.populate();

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
   private class DetailsAdp {
      
      private ImageView img = null;
      private TextView a = null;
      private TextView b = null;
      private TextView c = null;

      /**
       * Create a new container for location details view
       */
      public DetailsAdp() {
         
         img = (ImageView) findViewById(R.id.snow_img);
         a = (TextView) findViewById(R.id.snow_a);
         b = (TextView) findViewById(R.id.snow_b);
         c = (TextView) findViewById(R.id.snow_c);
         
         return;

      }
      
      /**
       * Populate location details view.
       */
      public void populate() {
         
         img.setImageResource(R.drawable.igloo);
         a.setText("Value a");
         b.setText("Value b");
         c.setText("Value c");
         
         return;

      }

   }
}
