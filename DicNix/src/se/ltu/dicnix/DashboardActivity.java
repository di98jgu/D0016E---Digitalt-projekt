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

//import se.ltu.dicnix.R; 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
* Dashboard. Overview of the most common options of the application.
*
* @author Viktor Stärn
*/
public class DashboardActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dashboard);

	}
	
    /**
     *  Called when the user clicks the list button 
     */
    public void listMethod(View view) {
    	Intent i = new Intent(getApplicationContext(), ListViewActivity.class);
    	startActivity(i);
    }
}
