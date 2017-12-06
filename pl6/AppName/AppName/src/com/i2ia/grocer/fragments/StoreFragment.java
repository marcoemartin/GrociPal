package com.i2ia.grocer.fragments;

import java.util.ArrayList;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.primary.StoreBrowserActivity;
import com.i2ia.grocer.activities.primary.StoreBrowserActivity.CustomBrowserListAdapter;
import com.i2ia.grocer.activities.secondary.ItemBrowserActivity;
import com.i2ia.grocer.activities.secondary.StoreInfoActivity;
import com.i2ia.grocer.activities.secondary.StoreInfoDialog;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;
import com.i2ia.grocer.data.SharedPreferenceManager;
import com.i2ia.grocer.data.Store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class StoreFragment extends Fragment {
	DBAdapter db;
	Context context;
	ArrayList<String> storeNames;
	ArrayList<String> storeImageNames;
	ArrayList<String> storeIDs;
	ArrayList<String> storeAddress;
	public static View rootView;
	Button addFav;
	
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
        context = getActivity();
        db = new DBAdapter(context);
        rootView = inflater.inflate(R.layout.fragment_favourites_stores, container, false);
        loadListView(rootView);
        onClickButtons(rootView);
        return rootView;
    }
		
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//inflater.inflate(R.menu.favourites, menu);
	    super.onCreateOptionsMenu(menu, inflater);

	}
	
	/**
	 * Manage click of 'Done' Button of item adding process
	 */
	public void onClickButtons(View rootView){
		
		//Done button, once user has finished adding stores
		Button addFav = (Button) rootView.findViewById(R.id.add_fav_store);
		addFav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, StoreBrowserActivity.class);
				intent.putExtra(Constants.ACTIVATE_BUTTON_TAG, "true");
				startActivity(intent);
				
			}
		});
	}
	
	/**
	 * Manages selection of menu stores
	 */
//	public boolean onOptionsItemSelected(MenuItem item){
//	    switch (item.getItemId()) {
//	    //If menu item selected is trash icon
//        case R.id.action_add:{
//        	Intent intent = new Intent(this.getActivity(),StoreBrowserActivity.class);
//        	startActivity(intent);
//        	}
//            return true;
//        default:
//            return super.onOptionsItemSelected(item);
//	    }
//
//	}
	
	public void loadListView(View rootView){
		db.open();
		storeNames = db.getAllStoreNames(Constants.FAV_STORES_TABLE);
		storeImageNames = db.getAllStoreImages(Constants.FAV_STORES_TABLE);
		storeIDs = db.getStoreIDs(Constants.FAV_STORES_TABLE);
		storeAddress = db.getAllAddresses(Constants.FAV_STORES_TABLE);
		
		Log.d("FAVDATA",storeNames.toString());
		
		db.close();
		if(storeNames != null){
			//Filling list view with lists 
			ListView listView = (ListView) rootView.findViewById(R.id.display_stores);
			CustomBrowserListAdapter cla = new CustomBrowserListAdapter(context, storeNames, storeImageNames, storeIDs,storeAddress, R.layout.row_store_favourites);
			listView.setAdapter(cla);
			listView.setOnItemClickListener(new ListItemClickListener());
		}
	}
	
	/**
	 * Handles clicks of store items
	 * @author Daniel
	 *
	 */
	public class ListItemClickListener implements ListView.OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			selectItem(position);
		}
		private void selectItem(int position){
			//User selects item on list
		}
	}

	/**
	 * 
	 * @author Daniel
	 *
	 */
	public static class CustomBrowserListAdapter extends BaseAdapter{
		private ArrayList<String> stores;
		private ArrayList<String> images;
		private ArrayList<String> storeIDs;
		private ArrayList<String> storeAddress;
		private Context context;
		LayoutInflater myInflater;
		private int rowLayout;
		private SharedPreferenceManager preferenceManager;
		private DBAdapter db;
		

		
		public CustomBrowserListAdapter(Context c, ArrayList<String> store, ArrayList<String> image, ArrayList<String> i_storeIDs, ArrayList<String> i_storeAddress, int i_rowLayout) {
			stores = store;
			context = c;
			preferenceManager = new SharedPreferenceManager(context);
			myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowLayout = i_rowLayout;
			images = getIDs(image);
			db = new DBAdapter(context);
			storeIDs = i_storeIDs;
			storeAddress = i_storeAddress;
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
		
		
		public ArrayList<String> getIDs(ArrayList<String> storeIconNames){
			ArrayList<String> storeIDs = new ArrayList<String>();
			Log.d("storeIconNames",Integer.toString(storeIconNames.size()));
			for(String s: storeIconNames){
				storeIDs.add(Integer.toString(
						context.getResources()
						.getIdentifier(s, "drawable", 
								context.getPackageName())));
			}
			
			return storeIDs;
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
			TextView name = (TextView) v.findViewById(R.id.store_name);
			TextView address = (TextView) v.findViewById(R.id.store_address);
			ImageView img = (ImageView) v.findViewById(R.id.img);
			Button showMap = (Button) v.findViewById(R.id.map);
			
			name.setText(stores.get(position));
			address.setText(storeAddress.get(position));
			img.setImageResource(Integer.parseInt(images.get(position)));	
			
			
			showMap.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//SHOW STORE ON MAP
					Intent intent = new Intent(context,StoreInfoActivity.class);
					intent.putExtra(Constants.FAV_STORE_KEY_STREETADDRESS,storeAddress.get(position));
					intent.putExtra(Constants.STORE_NAME, stores.get(position));
					intent.putExtra(Constants.FAV_STORE_KEY_ID, storeIDs.get(position));
					context.startActivity(intent);
				}
			});
		}
		
	}
	
}
