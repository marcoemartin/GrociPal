package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.R;

import android.os.Bundle;
/**
 * Activity for managing and display favourites (items & stores)
 * @author Daniel
 *
 */
public class FavouritesActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
	}

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_favourites;
	}

	@Override
	protected int getMenuResourceId() {
		// TODO Auto-generated method stub
		return R.menu.favourites;
	}

	@Override
	protected String getActivityString() {
		// TODO Auto-generated method stub
		return "FavouritesActivity";
	}



}
