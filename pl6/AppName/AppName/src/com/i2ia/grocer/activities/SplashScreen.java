package com.i2ia.grocer.activities;

import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.primary.HomeActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
/**
 * loading screen to display to user upon application launch 
 * @author Daniel
 *
 */
public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		//RUN THREAD LOADING HomeActivity
		//Go to HomeActivity 
		//Display only on first launch or as loading screen if needed
		//Temporary thread - displays splash screen
		new Handler().postDelayed(new Runnable(){
			
			public void run(){
				Intent intent = new Intent(SplashScreen.this,HomeActivity.class);
				SplashScreen.this.startActivity(intent);
				SplashScreen.this.finish();
			}
			//Splash Screen time display 
		}, 1000);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
