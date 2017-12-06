package com.i2ia.grocer.activities.secondary;

import java.util.ArrayList;

import com.i2ia.grocer.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
/**
 * Base Activity extended by all activites that need navigation drawer
 *
 * @author Daniel
 * @version 1.0 
 */
public abstract class SecondaryBaseActivity extends ActionBarActivity {
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

	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResourceId());
		
		ActionBar actBar = getSupportActionBar();
		actBar.setDisplayHomeAsUpEnabled(true);
        actBar.setHomeButtonEnabled(true);
		actBar.setIcon(R.drawable.inv_icon);
		actBar.setBackgroundDrawable(new ColorDrawable(0xFFFF8533));
		
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
	 * Inflates Menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {		
	//	getMenuInflater().inflate(getMenuResourceId(), menu);
		
//		//Keeps SearchView open at all times
//		MenuItem searchItem = menu.findItem(R.id.item_search);		
//		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//		searchView.setIconifiedByDefault(false);
		
		return true;
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
	
	public void hideKeyboard(){
    	try{
	    	InputMethodManager inputManager = (InputMethodManager) 
	    	getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	  		inputManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
   }

}

	


