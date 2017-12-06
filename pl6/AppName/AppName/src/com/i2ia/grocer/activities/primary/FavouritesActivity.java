package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
/**
 * Activity for managing and display favourites (items & stores)
 * @author Daniel
 *
 */
public class FavouritesActivity extends BaseActivity implements ActionBar.TabListener{
	//Tabs tutorial: http://tinyurl.com/n6e7u5f
	private String[] tabs = {"Items", "Stores"};
	private ViewPager viewPage;
	private SearchView searchView = null;
	private FavouritesHelper adapter;
	private ActionBar actBar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_favourites);
        viewPage = (ViewPager) findViewById(R.id.pager);
        
		actBar = getSupportActionBar();
        adapter = new FavouritesHelper(getSupportFragmentManager());
        viewPage.setAdapter(adapter);
        actBar.setHomeButtonEnabled(true);
        actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        for(String tab: tabs){
        	actBar.addTab(actBar.newTab().setText(tab).setTabListener(this));
        }
        
    	viewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    		 
    	    @Override
    	    public void onPageSelected(int position) {
    	        actBar.setSelectedNavigationItem(position);
    	    }
    	 
    	    @Override
    	    public void onPageScrolled(int arg0, float arg1, int arg2) {
    	    }
    	 
    	    @Override
    	    public void onPageScrollStateChanged(int arg0) {
    	    }
    	});	
        }
    
	/**
	 * Inflates Menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {		
		return true;
	}

	
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPage.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_favourites;
	}


	@Override
	protected int getMenuResourceId() {
		// TODO Auto-generated method stub
		return R.menu.favourites;
	}


	@Override
	protected String getActivityString() {
		// TODO Auto-generated method stub
		return Constants.FAVOURITES_ACTIVITY;
	}
	
	



}
