package com.i2ia.grocer.activities.primary;

import java.util.ArrayList;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.R.color;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Base Activity extended by all activites that need navigation drawer, actionbar, SearchView
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
		//actBar.setBackgroundDrawable(new ColorDrawable(0xFFFF8533));
		actBar.setBackgroundDrawable(new ColorDrawable(R.color.main_color));
		//Possible Colours
		//#46abe0 Blue
		//#a1e877 Green
		//#faf189 Yellow
		//#fe767a Red
		//#91D16B Green - Current
		//#FF8533 Orange

		//Creating navigation drawer
		drawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);//METHOD CALL
        drawerToggle = new ActionBarDrawerToggle(
        		this,
        		drawerLayout,
        		R.drawable.ic_navigation_drawer,
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
		AdapterClass adClass = new AdapterClass(BaseActivity.this, primaryPages);
		//drawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_item,primaryPages));
		drawerList.setAdapter(adClass);
			
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.syncState();
		
		
		//Makes SearchView dissapear when other view is clicked
		ViewGroup top_view = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
		ArrayList<View> allViews = getAllViews(top_view);
		
		for(View v: allViews){
			if(!(v instanceof SearchView) && (v != null)){
			v.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					hideKeyboard();
					return false;
				}
			});
		}
		}
		
	}
	
	/**
	 * 
	 * @param top_view
	 * @return list of all views
	 */
	public ArrayList<View> getAllViews(ViewGroup topView){
		ArrayList<View> allViews = new ArrayList<View>();
		
		int child_count = topView.getChildCount();
		for(int i = 0; i <= child_count;i++){
			View current_child = topView.getChildAt(i);
			if(current_child instanceof ViewGroup){
				allViews.add(current_child);
				allViews.addAll(getAllViews((ViewGroup) current_child));
			}else{
				allViews.add(current_child);
			}
		}
		return allViews;
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
		    	if(getActivityString() == Constants.HOME_ACTIVITY){
		    		drawerLayout.closeDrawer(drawerList);
		    		break;
		    		}
		        Intent intent_home = new Intent(getApplicationContext(),HomeActivity.class);
		        startActivity(intent_home);
		        break;
		    case 1:
	    		if(getActivityString() == Constants.STORE_ACTIVITY){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
		        Intent intent_store = new Intent(getApplicationContext(), StoreBrowserActivity.class);
		        intent_store.putExtra(Constants.ACTIVATE_BUTTON_TAG, "false");
		        startActivity(intent_store);
		        break;
		    case 2:
	    		if(getActivityString() == Constants.MANAGE_ACTIVITY){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
	           Intent intent_lists = new Intent(getApplicationContext(), ManageListsActivity.class);
	           startActivity(intent_lists);
	           break;
		    case 3:
	    		if(getActivityString() == Constants.FAVOURITES_ACTIVITY){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
	           Intent intent_fav = new Intent(getApplicationContext(), FavouritesActivity.class);
	           startActivity(intent_fav);
	           break;
		    case 4:
	    		if(getActivityString() == Constants.SETTINGS_ACTIVITY){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
		    	Intent intent_settings = new Intent(getApplicationContext(),SettingsActivity.class);
		    	startActivity(intent_settings);
		    	break;
		    case 5:
		    	if(getActivityString() == Constants.ABOUT_ACTIVITY){
	    			drawerLayout.closeDrawer(drawerList);
	    			break;
	    			}
		    	Intent intent_about = new Intent(getApplicationContext(),AboutActivity.class);
		    	startActivity(intent_about);
		    	break;
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
//	public boolean onCreateOptionsMenu(Menu menu) {		
////		getMenuInflater().inflate(getMenuResourceId(), menu);
////		
////		//Keeps SearchView open at all times
////		MenuItem searchItem = menu.findItem(R.id.item_search);		
////		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
////		//searchView.setIconifiedByDefault(false);
////		
//		return true;
//	}

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
    
    public void hideKeyboard(){
    	try{
	    	InputMethodManager inputManager = (InputMethodManager) 
	    	getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	  		inputManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
   }

    
    public class AdapterClass  extends ArrayAdapter<String> {
    	Context context;
    	private String[] TextValue;
    	private int[] imageValue = {R.drawable.home, R.drawable.shopping_bag,
    								R.drawable.manage_lists_dark, R.drawable.ic_action_important,
    								R.drawable.ic_action_settings, R.drawable.ic_action_about};

    	public AdapterClass(Context context, String[] TextValue) {
    	    super(context, R.layout.drawer_item, TextValue);
    	    this.context = context;
    	    this.TextValue= TextValue;

    	}


    	@Override
    	public View getView(int position, View coverView, ViewGroup parent) {
    	    // TODO Auto-generated method stub

    	    LayoutInflater inflater = (LayoutInflater) context
    	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.drawer_item,
    	            parent, false);

    	    TextView text1 = (TextView)rowView.findViewById(R.id.text1);
    	    text1.setText(TextValue[position]);
    	    ImageView image1 = (ImageView) rowView.findViewById(R.id.image1);
    	    image1.setBackgroundResource(imageValue[position]);

    	    return rowView;

    	}

    	}
}

	


