package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.secondary.ListViewActivity;
import com.i2ia.grocer.activities.secondary.NewEditList;
import com.i2ia.grocer.activities.secondary.SearchResultsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
/**
 * Activity for home page of app
 * @author Daniel
 *
 */
public class HomeActivity extends BaseActivity {
	
	public static String searchTag = "SearchQuery";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clickableButtons();//Creates onclick functions of buttons
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuItem menuSearchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				Intent intent = new Intent(getApplicationContext(),SearchResultsActivity.class);
				intent.putExtra(searchTag, query);
				startActivity(intent);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String query) {
				return false;
			}
		});
		
		
		return true;
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
		return "HomeActivity";
	}
	
	private void clickableButtons(){
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
		
		Button recentListButton = (Button) findViewById(R.id.button_recent_list);
		recentListButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),ListViewActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	
	
	
	
	
}	
	


