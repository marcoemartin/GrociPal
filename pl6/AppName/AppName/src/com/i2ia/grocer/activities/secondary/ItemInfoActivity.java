package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
/**
 * Activity for displaying detailed information on a specific item
 * @author Daniel
 *
 */
public class ItemInfoActivity extends SecondaryBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_item_info);
		
		
//		ActionBar actBar = getSupportActionBar();
//		actBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_info, menu);
		return true;
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_item_info;
	}

	@Override
	protected int getMenuResourceId() {
		return R.menu.item_info;
	}

}
