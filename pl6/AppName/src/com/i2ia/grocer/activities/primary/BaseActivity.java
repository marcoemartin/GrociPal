package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.secondary.SettingsActivity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Base Activity extended by all activites that need navigation drawer
 *
 * @author Daniel
 * @version 1.0 
 */
public abstract class BaseActivity extends ActionBarActivity {
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private String[] primaryPages;
	private ListView drawerList;
	private SearchView searchView;
	
	/**
	 * Abstract method gets layout to expand
	 * @return integer referring to layout resource
	 */
	protected abstract int getLayoutResourceId();
	
	/**
	 * Abstract method gets menu to expand
	 * @return integer referring to menu resource
	 */
	protected abstract int getMenuResourceId();
	
	/**
	 * Abstract method gets name of activity currently extending this activity
	 * @return String of activity name
	 */
	protected abstract String getActivityString();
			
	@Override
	/**
	 * Creates and loads activity contents
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResourceId());
		
		//Managing action bar icon/button
		ActionBar actBar = getSupportActionBar();
		actBar.setDisplayHomeAsUpEnabled(true);
        actBar.setHomeButtonEnabled(true);
		actBar.setIcon(R.drawable.inv_icon);
		
		//Creating navigation drawer
		drawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);//METHOD CALL
        drawerToggle = new ActionBarDrawerToggle(
        		this,
        		drawerLayout,
        		R.drawable.ic_drawer,
        		R.string.hello_world,
        		R.string.hello_world){
        	
        	public void onDrawerClosed(View view){
        		super.onDrawerClosed(view);
        	}
        	public void onDrawerOpened(View view){
        		super.onDrawerOpened(view);
        	}
        	
        };
        //Populating Navigation drawer
		primaryPages = getResources().getStringArray(R.array.PrimaryPagesArray);
		drawerList = (ListView) findViewById(R.id.left_drawer);	//METHOD CALL	
		drawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_item,primaryPages));
			
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.syncState();
		

	}
	/**
	 * Click listener for navigation drawer items
	 * @author Daniel
	 *
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener{
		public void onItemClick(AdapterView parent, View view, int position, long id){
			selectItem(position);
		}
		
		private void selectItem(int position){
		    switch(position) {
		    case 0:
		    	if(getActivityString()== "HomeActivity"){
		    		drawerLayout.closeDrawer(drawerList);
		    		break;
		    		}
		        Intent intent_home = new Intent(getApplicationContext(),HomeActivity.class);
		        startActivity(intent_home);
		        break;
		    case 1:
	    		if(getActivityString()== "StoreBrowserActivity"){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
		        Intent intent_store = new Intent(getApplicationContext(), StoreBrowserActivity.class);
		        startActivity(intent_store);
		        break;
		    case 2:
	    		if(getActivityString()== "ManageListsActivity"){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
	           Intent intent_lists = new Intent(getApplicationContext(), ManageListsActivity.class);
	           startActivity(intent_lists);
	           break;
		    case 3:
	    		if(getActivityString()== "FavouritesActivity"){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
	           Intent intent_fav = new Intent(getApplicationContext(), FavouritesActivity.class);
	           startActivity(intent_fav);
	           break;
		    case 4:
		    	Intent intent_settings = new Intent(getApplicationContext(),SettingsActivity.class);
		    	startActivity(intent_settings);
		    }
		}
	}
	
	/**
	 * Handle navigation drawer action bar activation
	 */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        return super.onOptionsItemSelected(item);
    }

	/**
	 * Inflates Menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {		
		getMenuInflater().inflate(getMenuResourceId(), menu);
		
		//Keeps SearchView open at all times
		MenuItem searchItem = menu.findItem(R.id.action_search);		
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setIconifiedByDefault(false);
		
		return true;
	}

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
    
    
	//TODO:HIDE soft keyboard(from searchView) when user clicks off

//    public void hideKeyboard(){
//    	InputMethodManager inputManager = (InputMethodManager) 
//    			getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//    	inputManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
//    }

}

	


