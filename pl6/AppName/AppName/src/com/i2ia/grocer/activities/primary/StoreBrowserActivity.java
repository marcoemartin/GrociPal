package com.i2ia.grocer.activities.primary;

import java.util.ArrayList;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.secondary.ListViewActivity;
import com.i2ia.grocer.activities.secondary.StoreInfoActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.SharedPreferenceManager;
import com.i2ia.grocer.data.Store;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
/**
 * Activity for browsing stores
 * @author Daniel
 *
 */
public class StoreBrowserActivity extends BaseActivity {
	public static DBAdapter db;
	private static ArrayList<String> storeNames = new ArrayList<String>();
	private static ArrayList<String> storeImageNames = new ArrayList<String>();
	private static ArrayList<Integer> storeIDs= new ArrayList<Integer>();
	Button doneBtn;
	public Context context = this;
	String activateButton = "false";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DBAdapter(this);
		
		Intent intent = getIntent();
		Bundle intent_bundle = intent.getExtras();
		try{
			activateButton = intent_bundle.getString(Constants.ACTIVATE_BUTTON_TAG);	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		loadListView();
		if(activateButton.equals("true")){
			doneBtn = (Button) findViewById(R.id.done_btn);
			doneBtn.setVisibility(android.view.View.VISIBLE);
			doneBtn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FavouritesActivity.class);
				startActivity(intent);
			}
		});
		}
		try{
			String mapFail = intent_bundle.getString(Constants.MAP_FAIL_TAG);
			if(mapFail.equals("fail")){
				promptAddressError();
			}
		}catch(NullPointerException e){
			
		}
	}
	
	private void promptAddressError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Address Not Valid");
		builder.setMessage("The Address is not valid. \nWould you like to modify it?")
        .setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Intent intent =  new Intent(getApplicationContext(),SettingsActivity.class);
				startActivity(intent);
            }
        })
        .setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Toast.makeText(context, "Modify address in Settings to access Maps.", Toast.LENGTH_LONG).show();
            }
        });
		// Create the AlertDialog object and return it
		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.store_browser, menu);

	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) 
	    							MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		
		return super.onCreateOptionsMenu( menu );
	}
	
	
	public void loadListView(){
		db.open();
		storeNames = db.getAllStoreNames(Constants.STORES_TABLE);	
		storeImageNames = db.getAllStoreImages(Constants.STORES_TABLE);
		storeIDs = db.getAllItemRowIds(Constants.STORES_TABLE);
		db.close();
		
		addNearbyStore();
		
		//Filling list view with lists 
		ListView listView = (ListView) this.findViewById(R.id.display_stores);
		CustomBrowserListAdapter cla = new CustomBrowserListAdapter(this, storeNames, storeImageNames,R.layout.row_store_browser);
		listView.setAdapter(cla);
		listView.setOnItemClickListener(new ListItemClickListener());
	}
	
	public void addNearbyStore(){
		storeNames.add(0, "Stores nearby");
		storeImageNames.add(0,"nearby_icon");
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
		return Constants.STORE_ACTIVITY;
	}
	
	/**
	 * Handles clicks of ListView for the stores
	 * @author Daniel
	 *
	 */
	public class ListItemClickListener implements ListView.OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			selectItem(position);
		}
		private void selectItem(int position){
			String selectedStore = storeNames.get(position);
			Intent intent = new Intent(getApplicationContext(),StoreInfoActivity.class);
			intent.putExtra(Constants.STORE_NAME,selectedStore);
			startActivity(intent);
			
		}
	}
	
	/**
	 * 
	 * @author Daniel
	 *
	 */
	public static class CustomBrowserListAdapter extends BaseAdapter{
		private ArrayList<String> stores;
		private ArrayList<Integer> images;
		private Context context;
		LayoutInflater myInflater;
		private int rowLayout;
		private SharedPreferenceManager preferenceManager;

		
		public CustomBrowserListAdapter(Context c, ArrayList<String> item, ArrayList<String> image,int i_rowLayout) {
			stores = item;
			context = c;
			preferenceManager = new SharedPreferenceManager(context);
			myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowLayout = i_rowLayout;
			images = db.getImageIDs(image);
			
			Log.d("CBLA DATA",item.toString() +image.toString() + c.toString() );
			//layoutResourceId = resourceId;
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return stores.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return stores.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = myInflater.inflate(rowLayout, parent, false);	
				populateView(convertView,position);
				
			}else{
				RelativeLayout view = (RelativeLayout) convertView;
				populateView(view,position);
			}
			return convertView;
		}
		
		public void populateView(View v, final int position){
			TextView name = (TextView) v.findViewById(R.id.txt);
			ImageView img = (ImageView) v.findViewById(R.id.img);
			name.setText(stores.get(position));
			img.setImageResource(images.get(position));	
		}

		
		
	}
	
	



}
