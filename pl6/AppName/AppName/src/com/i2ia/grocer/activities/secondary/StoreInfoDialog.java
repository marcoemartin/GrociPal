package com.i2ia.grocer.activities.secondary;

import org.json.JSONException;
import org.json.JSONObject;

import com.i2ia.grocer.R;
import com.i2ia.grocer.R.id;
import com.i2ia.grocer.R.layout;
import com.i2ia.grocer.R.menu;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.SharedPreferenceManager;
import com.i2ia.grocer.data.Store;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class StoreInfoDialog extends Activity {
	SharedPreferenceManager prefManager = new SharedPreferenceManager(this);
	DBAdapter db = new DBAdapter(this);
	JSONObject store;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_info_dialog);
		final ToggleButton fav = (ToggleButton) findViewById(R.id.store_fav);
		TextView storeName = (TextView) findViewById(R.id.storeName);
		TextView storeAddress = (TextView) findViewById(R.id.address);
		TextView storeHours = (TextView) findViewById(R.id.hours);
		Button browseProducts = (Button) findViewById(R.id.browse);
		
		browseProducts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ItemBrowserActivity.class);
				intent.putExtra("storeBrowse", true);
				startActivity(intent);
				
			}
		});

		try {
			store = new JSONObject(getIntent().getExtras().getString("store"));
			storeName.setText(store.getString("name"));
			storeAddress.setText(store.getString("vicinity"));
			
			String storeOpen = "Closed";
			if(store.getJSONObject("opening_hours").getBoolean("open_now")){
				storeOpen="Open now";
			}
					
			storeHours.setText(storeOpen);

			fav.setChecked(prefManager.getPreference(store.getString("place_id")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fav.isChecked()){
					//Add to favourites
					try {
						prefManager.SetPreference(store.getString("place_id"),true);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					db.open();
					db.insertFavStore(store);
					db.close();
					Toast.makeText(getApplicationContext(), "Saved to Favourites", Toast.LENGTH_SHORT).show();
					
				}else{
					try {
						prefManager.SetPreference(store.getString("place_id"),false);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					db.open();
					db.removeFavStore(store);
					db.close();
					Toast.makeText(getApplicationContext(), "Saved to Favourites", Toast.LENGTH_SHORT).show();
					
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_info_dialog, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
