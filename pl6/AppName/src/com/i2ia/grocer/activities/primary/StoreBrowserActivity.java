package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.secondary.StoreInfoActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
/**
 * Activity for browsing stores
 * @author Daniel
 *
 */
public class StoreBrowserActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Button favouritesButton = (Button) findViewById(R.id.button_store);
		favouritesButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),StoreInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_store_browser;
	}

	@Override
	protected int getMenuResourceId() {
		// TODO Auto-generated method stub
		return R.menu.store_browser;
	}

	@Override
	protected String getActivityString() {
		// TODO Auto-generated method stub
		return "StoreBrowserActivity";
	}



}
