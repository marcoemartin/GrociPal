package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ToggleButton;
/**
 * Activity for managing user settings
 * @author Daniel
 *
 */
public class SettingsActivity extends ActionBarActivity {
	private ToggleButton notif_toggle,travel_toggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getSupportActionBar().setIcon(R.drawable.inv_icon);
		
		toggleButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void toggleButtons(){
		notif_toggle = (ToggleButton) findViewById(R.id.toggleButton_notif);
		travel_toggle = (ToggleButton) findViewById(R.id.toggleButton_travel);
		
		notif_toggle.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(((ToggleButton) v).isChecked()){
					//Turn Notification feature ON
					
					
					}
				else{
					//Turn Notification feature OFF
					
					
				}
				
			}
			
		});
		
		travel_toggle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((ToggleButton) v).isChecked()){
					//Turn travel costs ON
					
					
					}
				else{
					//Turn travel costs OFF
					
					
				}
				
			}
		});
		
	}


}
