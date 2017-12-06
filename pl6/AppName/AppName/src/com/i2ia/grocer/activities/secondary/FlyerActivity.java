package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
/**
 * Activity for displaying a stores full flyer
 * @author Daniel
 *
 */
public class FlyerActivity extends SecondaryBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flyer);
		
//		ActionBar actBar = getSupportActionBar();
//		actBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flyer, menu);
		return true;
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_flyer;
	}

	@Override
	protected int getMenuResourceId() {
		return R.menu.flyer;
	}

}
