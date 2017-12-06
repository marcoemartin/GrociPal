package com.i2ia.grocer.activities.secondary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.R.array;
import com.i2ia.grocer.R.id;
import com.i2ia.grocer.R.layout;
import com.i2ia.grocer.activities.SplashScreen;
import com.i2ia.grocer.activities.primary.FavouritesActivity;
import com.i2ia.grocer.activities.primary.HomeActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;
import com.i2ia.grocer.data.RemoteDatabaseConnector;
import com.i2ia.grocer.data.SharedPreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ItemBrowserActivity extends SecondaryBaseActivity {
	
	private static ArrayList<String> parentItems = new ArrayList<String>();
	private static ArrayList<String> parentImages = new ArrayList<String>();
	private static HashMap<String, ArrayList<Product>> childItems = new HashMap<String, ArrayList<Product>>();
	private String tableName = "";
	private static boolean storeBrowse = false;
	private static Context context;
	private static ArrayList<Product> resultArray = new ArrayList<Product>();
	static HashMap<Integer,Integer> positionCategory = new HashMap<Integer,Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tableName = getIntent().getExtras().getString(Constants.TABLE_TAG);
		context = this;
		ActionBar actBar = getSupportActionBar();
		actBar.setHomeButtonEnabled(true);
		actBar.setDisplayHomeAsUpEnabled(true);


		try{
			storeBrowse = getIntent().getExtras().getBoolean("storeBrowse");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(storeBrowse){
			//User is browsing by store
			RemoteDatabaseConnector remoteDB = new RemoteDatabaseConnector();
			remoteDB.getAllItems();
			//start spinner - wait for activity to complete
			
		}else{
			parentItems = new ArrayList<String>();;
			parentImages = new ArrayList<String>();;
			childItems = new HashMap<String, ArrayList<Product>>();
			fillLayout();
		}
	}
	
	public static void fillLayout(){
		
		setParentData();
		setChildData();
		
		final FloatingGroupExpandableListView list = (FloatingGroupExpandableListView) ((Activity) context).findViewById(R.id.sample_activity_list);
        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		list.setChildDivider(new ColorDrawable(Color.BLACK));
		
		final ExpandableAdapter adapter = ((ItemBrowserActivity) context).new ExpandableAdapter(parentItems,parentImages,childItems,((Activity) context).getLayoutInflater());
		final WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
		list.setAdapter(wrapperAdapter);	
	}
	
	
	
	public static void itemsDownloaded(ArrayList<Product> i_result){
		resultArray = i_result;
		//Stop spinner
		processResults(resultArray);
		//set new data
		fillLayout();
		//populate views
	}
	
	public static  void processResults(ArrayList<Product> i_result){
		HashMap<String, ArrayList<Product>> products = new HashMap<String,ArrayList<Product>>();
		
		for(Product p: resultArray){
			if(products.containsKey(p.getCategory())){
				products.get(p.getCategory()).add(p);
				
			}else{
				ArrayList<Product> newCategory = new ArrayList<Product>();
				newCategory.add(p);
				products.put(p.getCategory(), newCategory);
			}
		}
		childItems = products;
	}
	
	
	
	//Populates the parent list view - the categories
	public static void setParentData(){
		
		if(storeBrowse){
			String[] categories = context.getResources().getStringArray(R.array.Categories);
			String[] categoryImages = context.getResources().getStringArray(R.array.CategoryIcons);
			//User is browsing by store
			for(int i = 0; i<resultArray.size(); i++){
				positionCategory.put(i, Integer.parseInt(resultArray.get(i).getCategory()));
				parentItems.add(categories[Integer.parseInt(resultArray.get(i).getCategory())]);
				parentImages.add(categoryImages[Integer.parseInt(resultArray.get(i).getCategory())]);
			}
		}else{
			//Special case for favourites
			parentItems.add("Favorites");
			parentImages.add("star");
			//Gets all categories
			parentItems.addAll((Arrays.asList(context.getResources().getStringArray(R.array.Categories))));
			parentImages.addAll((Arrays.asList(context.getResources().getStringArray(R.array.CategoryIcons))));
			//Make change if by store
		}
	
	}
	
	public static void setChildData(){
		if(storeBrowse){
			//Do nothing now
			
		}else{
			//Get child data
			DBAdapter db = new DBAdapter(context);
			db.open();
			childItems = db.getAllItems("");
			db.close();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.item_browser, menu);
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) 
	    							MenuItemCompat.getActionView(menu.findItem(R.id.item_browse_search));
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		
		return super.onCreateOptionsMenu( menu );
	}
	
	@Override
	public void startActivity(Intent intent) {      
	    // check if search intent
	    intent.putExtra("KEY", tableName);
	    super.startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//
		int id = item.getItemId();
		if (id == R.id.add) {
			if(tableName.equals("")){
				Intent intent = new Intent(this, FavouritesActivity.class);
				startActivity(intent);
			}else{
				Intent intent = new Intent(this, ListViewActivity.class);
				intent.putExtra(Constants.TABLE_TAG, tableName);
				intent.putExtra(Constants.NEXT_TAG, -1);
				startActivity(intent);
			}
			return true;
		}
		
		if(id == android.R.id.home){
			Intent intent = new Intent(this, ListViewActivity.class);
			intent.putExtra(Constants.TABLE_TAG, tableName);
			intent.putExtra(Constants.NEXT_TAG, -1);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class ExpandableAdapter extends BaseExpandableListAdapter{
		
		private ArrayList<String> parentItems = new ArrayList<String>();
		private ArrayList<String> parentImages = new ArrayList<String>();
		private HashMap<String, ArrayList<Product>> childItems;
		private LayoutInflater inflater;
		private SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getApplicationContext());
		private DBAdapter db = new DBAdapter(getApplicationContext());
		private ArrayList<String> selectedItemPosition = new ArrayList<String>();

		
		public ExpandableAdapter(ArrayList<String> i_parentItems,ArrayList<String> i_parentImages, HashMap<String, ArrayList<Product>> i_childItems,LayoutInflater i_inflater){
			parentItems = i_parentItems;	
			parentImages = i_parentImages;
			childItems = i_childItems;
			inflater = i_inflater;
		}
		
		@Override
		public int getGroupCount() {
			return parentItems.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if(!storeBrowse){
				groupPosition = groupPosition - 1;
				if(childItems.get(Integer.toString(groupPosition)) != null){
					return childItems.get(Integer.toString(groupPosition)).size();
				}
			}else{
				if(childItems.get(Integer.toString(positionCategory.get(groupPosition))) != null){
					return childItems.get(Integer.toString(positionCategory.get(groupPosition))).size();
				}
			}
			

			return 0;
			
		}

		@Override
		public Object getGroup(int groupPosition) {
			return parentItems.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childItems.get(Integer.toString(groupPosition)).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			if(storeBrowse){
				return groupPosition;
				}
			return groupPosition - 1;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			
			if (convertView == null){
				convertView = inflater.inflate(R.layout.item_browser_group_item,parent,false);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.sample_activity_list_group_item_text);
			textView.setText(parentItems.get(groupPosition));
			
			ImageView imageView = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_item_image);
			int imageResource = getApplicationContext().getResources().getIdentifier(
					(parentImages.get(groupPosition)), 
					"drawable", getApplicationContext().getPackageName());
			imageView.setImageResource(imageResource);
			
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ArrayList<Product> currentChildItems  = new ArrayList<Product>();
			//Accounts for adding of favorites group
			
			if(!storeBrowse){
				groupPosition = groupPosition - 1;
				currentChildItems = childItems.get(Integer.toString((groupPosition)));
			}else{
				currentChildItems = childItems.get(Integer.toString(positionCategory.get(groupPosition)));
			}
			
			if (convertView == null){
				convertView = inflater.inflate(R.layout.item_browser_child_item,parent,false);
			}
			
			if(!currentChildItems.isEmpty()){
				final Product p;
				
				if(!storeBrowse){
					p = (Product) getChild(groupPosition,childPosition);
				}else{
					p = (Product) getChild(positionCategory.get(groupPosition),childPosition);
				}
				
				convertView.setBackgroundColor(0x00000000);
				//Set text of textview
				TextView textView = (TextView) convertView.findViewById(R.id.sample_activity_list_child_item_text);
				textView.setText(p.getName());
				
				//Set imageview of item
				ImageView imageView = (ImageView) convertView.findViewById(R.id.sample_activity_list_child_item_image);
				int imageResource = getApplicationContext().getResources().getIdentifier(
						p.getImageName(),"drawable", getApplicationContext().getPackageName());
							imageView.setImageResource(imageResource);
				
				ToggleButton fav = (ToggleButton) convertView.findViewById(R.id.child_item_fav);
				fav.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(((ToggleButton) v).isChecked()){
							preferenceManager.SetPreference(p.getName(), true);
							db.open();
							db.insertFavItem(p);
							db.close();
							Toast.makeText(getApplicationContext(), "Saved to Favourites", Toast.LENGTH_SHORT).show();
						}else{
							preferenceManager.SetPreference(p.getName(), false);
							db.open();
							db.deleteItem(p.getName(), Constants.FAV_ITEMS_TABLE);
							db.close();
							Toast.makeText(getApplicationContext(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Add item to user list
						if(!tableName.equals("")){
							db.open();
							db.insertItem(tableName,p.getName(),Integer.toString(p.getProductnum()), 
									p.getCategory(), p.getImageName(), p.getAmount(),
									p.getUnits(), p.getBrand(),p.getOrganic());
							db.close();
						}else{
								preferenceManager.SetPreference(p.getName(), true);
								db.open();
								db.insertFavItem(p);
								db.close();
								Toast.makeText(getApplicationContext(), "Saved to Favourites", Toast.LENGTH_SHORT).show();
						}
						selectedItemPosition.add(p.getName());
						v.setBackgroundColor(context.getResources().getColor(R.color.selected));
					}
				});
				
				if (selectedItemPosition.contains(p.getName())) {
				      // set the desired background color
				      convertView.setBackgroundColor(context.getResources().getColor(R.color.selected));
				    }
				    else {
				      // set the default (not selected) background color to a transparent color (or any other)
				    	convertView.setBackgroundColor(context.getResources().getColor(R.color.list_view_items));
				    }
				
				fav.setTextOn("");
				fav.setTextOff("");
				if(p != null){
					fav.setChecked(preferenceManager.getPreference(p.getName()));
				}
			}
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	
	}

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_item_browser;
	}
	@Override
	protected int getMenuResourceId() {
		// TODO Auto-generated method stub
		return R.menu.item_browser;
	}
}

