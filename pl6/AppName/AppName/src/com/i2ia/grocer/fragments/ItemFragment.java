package com.i2ia.grocer.fragments;

import java.util.ArrayList;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.secondary.ItemBrowserActivity;
import com.i2ia.grocer.activities.secondary.ListViewActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;
import com.i2ia.grocer.data.SharedPreferenceManager;

import android.app.Activity;
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
import android.view.View.OnClickListener;
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

public class ItemFragment extends Fragment {	
	DBAdapter db;
	Context context;
	ArrayList<String> itemNames;
	ArrayList<String> itemImageNames;
	ArrayList<Integer> itemIDs;
	public static View rootView;
	Button addFav;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_favourites_items, container, false);
        db = new DBAdapter(context);
        loadListView(rootView);
        onClickButtons(rootView);
        return rootView;
    }
		
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//inflater.inflate(R.menu.favourites, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	public void reload(){
        loadListView(rootView);
	}
	
	/**
	 * Manage click of 'Done' Button of item adding process
	 */
	public void onClickButtons(View rootView){
		
		//Done button, once user has finished adding items
		Button addFav = (Button) rootView.findViewById(R.id.add_fav_item);
		addFav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ItemBrowserActivity.class);
				intent.putExtra(Constants.TABLE_TAG, "");
				startActivity(intent);
				
			}
		});
	}
	
	public void loadListView(View rootView){
		db.open();
		itemNames = db.getAllItemNames(Constants.FAV_ITEMS_TABLE);
		itemImageNames = db.getAllItemImages(Constants.FAV_ITEMS_TABLE);
		itemIDs = db.getAllItemRowIds(Constants.FAV_ITEMS_TABLE);
		db.close();
		if(itemNames != null){
			//Filling list view with lists 
			ListView listView = (ListView) rootView.findViewById(R.id.display_items);
			CustomBrowserListAdapter cla = new CustomBrowserListAdapter(context, itemNames, itemImageNames,itemIDs,R.layout.row_items_browser);
			listView.setAdapter(cla);
			listView.setOnItemClickListener(new ListItemClickListener());
		}
	}
	
	/**
	 * Handles clicks of row items
	 * @author Daniel
	 *
	 */
	public class ListItemClickListener implements ListView.OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			selectItem(position);
		}
		private void selectItem(int position){
			//open description... or something
		}
	}
	
	/**
	 * 
	 * @author Daniel
	 *
	 */
	public static class CustomBrowserListAdapter extends BaseAdapter{
		private ArrayList<String> items;
		private ArrayList<String> images;
		private ArrayList<Integer> itemIDs;
		private Context context;
		LayoutInflater myInflater;
		private int rowLayout;
		private SharedPreferenceManager preferenceManager;
		private DBAdapter db;

		
		public CustomBrowserListAdapter(Context c, ArrayList<String> item, ArrayList<String> image, ArrayList<Integer> i_imageIDs, int i_rowLayout) {
			items = item;
			context = c;
			preferenceManager = new SharedPreferenceManager(context);
			myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowLayout = i_rowLayout;
			images = getIDs(image);
			db = new DBAdapter(context);
			itemIDs = i_imageIDs;
			//layoutResourceId = resourceId;
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		
		public ArrayList<String> getIDs(ArrayList<String> itemIconNames){
			ArrayList<String> itemIDs = new ArrayList<String>();
			for(String s: itemIconNames){
				itemIDs.add(Integer.toString(
						context.getResources()
						.getIdentifier(s, "drawable", 
								context.getPackageName())));
			}
			return itemIDs;
			
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
			ToggleButton fav = (ToggleButton) v.findViewById(R.id.favorite);
			fav.setTextOn("");
			fav.setTextOff("");
			fav.setChecked(preferenceManager.getPreference(items.get(position)));
			name.setText(items.get(position));
			img.setImageResource(Integer.parseInt(images.get(position)));	
			
			
			fav.setChecked(preferenceManager.getPreference(items.get(position)));
			
			//clickListnener for favorite toggle button
			fav.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					db.open();
					final Product itemObject = db.getItemObject(itemIDs.get(position), Constants.FAV_ITEMS_TABLE);
					db.close();
					
					if(((ToggleButton) v).isChecked()){
						preferenceManager.SetPreference(items.get(position), true);
						db.open();
						db.insertFavItem(itemObject);
						db.close();
						Toast.makeText(context, "Saved to Favourites", Toast.LENGTH_SHORT).show();
					}else{
						preferenceManager.SetPreference(items.get(position), false);
						db.open();
						db.removeFavItem(itemObject.getName());
						db.close();
						Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		
		
	}
}
