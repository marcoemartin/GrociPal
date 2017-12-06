package com.i2ia.grocer.activities.primary;

import java.util.ArrayList;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.secondary.ListViewActivity;
import com.i2ia.grocer.activities.secondary.NewEditList;
import com.i2ia.grocer.activities.secondary.SearchProductsActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.RemoteDatabaseConnector;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.SearchView;
/**
 * Activity for home page of app
 * @author Daniel
 *
 */
public class HomeActivity extends BaseActivity {
    private static ListView listView;
    private static ArrayList<String> userLists = new ArrayList<String>();
	private static ArrayAdapter<String> mArrayAdapter = null;
	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		HomeActivity.context = this;
		//Manage onClick behavior of buttons
		onClickButtons();

	}	
	public void loadListView(){
		userLists.clear();
		//Get Name of user made tables
		DBAdapter db = new DBAdapter(this);
		db.open();
		//All tables in database
		userLists = db.getAllTables();
		db.close();
		
		//Filling list view with lists 
		mArrayAdapter = new ArrayAdapter<String>(this,R.layout.manage_lists_item,userLists);
		listView = (ListView) findViewById(R.id.listView_items_home);
		listView.setAdapter(mArrayAdapter);
		listView.setOnItemClickListener(new ListItemClickListener());	
	}
	/**
	 * Manages Clicks of Recent Lists display
	 * @author Daniel
	 *
	 */
	private class ListItemClickListener implements ListView.OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			selectItem(position);
		}
		private void selectItem(int position){
			String selectedList = userLists.get(position);
			//Pass name of selected table to ListViewActivity class
			Intent intent = new Intent(getApplicationContext(),ListViewActivity.class);
			intent.putExtra(Constants.TABLE_TAG, selectedList);
			intent.putExtra(Constants.NEXT_TAG, -1);
			startActivity(intent);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home, menu);
	    
	 // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) 
	    							MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		
		return super.onCreateOptionsMenu( menu );
	}
	
	@Override
	/**
	 * Override, loadlists each time HomeActivity is resumed or started
	 */
	public void onResume(){
		loadListView();
		super.onResume();
	}

	@Override
	protected int getMenuResourceId(){
		// TODO Auto-generated method stub
		return R.menu.home;
	}
	
	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_home;
	}

	@Override
	protected String getActivityString() {
		// TODO Auto-generated method stub
		return Constants.HOME_ACTIVITY;
	}
	
	/**
	 * Handles clicks of buttons
	 */
	private void onClickButtons(){
		Button newListButton = (Button) findViewById(R.id.button_new_list);
		newListButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),NewEditList.class);
				startActivity(intent);
			}
		});
		
		Button manageListsButton = (Button) findViewById(R.id.button_manage_lists);
		manageListsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),ManageListsActivity.class);
				startActivity(intent);
			}
		});
		
		Button favouritesButton = (Button) findViewById(R.id.button_favourites);
		favouritesButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),FavouritesActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * Displays result from pricecheck
	 * @param cost
	 */
	public static void displayPrice(String cost){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case DialogInterface.BUTTON_POSITIVE:
					break;
				}
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(cost).setPositiveButton("Okay", dialogClickListener).show();
	}
	
	public static void priceCalculated(String cost){
		displayPrice("Cost: $" + cost);
	}
	public static void cannotConnect(){
		displayPrice("Could not connect to server");
	}
}	
	


