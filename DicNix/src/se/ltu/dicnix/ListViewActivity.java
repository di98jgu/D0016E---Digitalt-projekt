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

package se.ltu.dicnix;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
* Lists registered measurement locations for the user to choose from 
*
* @author Viktor Stärn
*/
public class ListViewActivity extends ListActivity {

    Cursor sensorColumn = null;
    Cursor nameColumn = null;
	List<String> listOfSensorSerials = new ArrayList<String>();
	List<String> listOfSensorNames = new ArrayList<String>();
	List<String> finalList = new ArrayList<String>();
	static String[] serial = {"serial"};
	static String[] name = {"name"};


	
	
	protected DicNixApp application;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Fetch registered measurement locations
         */
        application = (DicNixApp) getApplication();
        
        /**
         * Fetch serial numbers of registered measurement locations
         */
        application.snowsensor.setColumns(serial);
        sensorColumn = application.snowsensor.all();	
        

        sensorColumn.moveToFirst();
        while (sensorColumn.isAfterLast() == false) 
        {
        	listOfSensorSerials.add(sensorColumn.getString(0));
            sensorColumn.moveToNext();
        }
        
        /**
         * Fetch names of registered measurement locations
         */
        application.snowsensor.setColumns(name);
        nameColumn = application.snowsensor.all();	
        

        nameColumn.moveToFirst();
        while (nameColumn.isAfterLast() == false) 
        {
        	listOfSensorSerials.add(nameColumn.getString(0));
            nameColumn.moveToNext();
        }
        
        
        /**
         * Build final list of registered measurement locations
         */
        for (int i = 0; i < listOfSensorSerials.size(); i++) {
        	if (!listOfSensorNames.get(i).equals(null)) {
        		finalList.add(listOfSensorNames.get(i));
        	}
        	else {
        		finalList.add(listOfSensorSerials.get(i));
        	}
        }
 
 
        /**
         * Binding resources Array to ListAdapter
         */
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_item, finalList));
 
        ListView lv = getListView();
        
        /**
         * Listening to single list item on click
         */
        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
 
              /**
               * Selected item
               */
              String location = ((TextView) view).getText().toString();
              int sensor_id = (int)id;
              
              /**
               * Launching new Activity on selecting single List Item
               */
              Intent i = new Intent(getApplicationContext(), LocationDetailsActivity.class);
              
              /**
               * Sending data to new activity
               */
              i.putExtra("location", location);
           	  i.putExtra("sensor_serial", listOfSensorSerials.get(sensor_id));
              startActivity(i);
 
          }
        });
        
    }

}
