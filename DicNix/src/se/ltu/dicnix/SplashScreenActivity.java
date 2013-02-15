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

import se.ltu.dicnix.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
 
/**
* Splash screen
*
* @author Viktor Stärn
*/
public class SplashScreenActivity extends Activity {
 
    /**
     * Find out if the back button was pressed in the splash screen activity and avoid opening the next activity
     */
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 2000; 
 
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.splash_screen);
 
        Handler handler = new Handler();
 
        /**
         * Run a thread after 2 seconds to start the home screen
         */
        handler.postDelayed(new Runnable() {
 
            @Override
            public void run() {
 
                /**
                 * Close the splash screen so the user won't come back when it presses back key
                 */ 
                finish();
                 
                if (!mIsBackButtonPressed) {
                    /**
                     * Go to dashboard if the back button wasn't pressed already
                     */
                    Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    SplashScreenActivity.this.startActivity(intent);
               }
                 
            }
 
        }, SPLASH_DURATION);
 
    }
 
    @Override
   public void onBackPressed() {
 
        /**
         * Set flag to true so the next activity won't start up
         */
        mIsBackButtonPressed = true;
        super.onBackPressed();
 
    }
}