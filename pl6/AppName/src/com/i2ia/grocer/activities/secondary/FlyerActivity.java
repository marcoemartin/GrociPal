package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
/**
 * Activity for displaying a stores full flyer
 * @author Daniel
 *
 */
public class FlyerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flyer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flyer, menu);
		return true;
	}

}
