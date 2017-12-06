package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.primary.HomeActivity;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
/**
 * Activity for displaying search results (from HomeActivity)
 * @author Daniel
 *
 */
public class SearchResultsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		clickableButtons();
		String query = getIntent().getStringExtra(HomeActivity.searchTag);
		String resultsForString = getResources().getString(R.string.search_result);
		getSupportActionBar().setTitle(resultsForString +" \"" + query+ "\"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}
	
	public void clickableButtons(){
		
		//TEMPORARY: buttons providing links between pages (fake search results)
		Button foodResult = (Button) findViewById(R.id.button_food);
		foodResult.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),ItemInfoActivity.class);
				startActivity(intent);
			}
		});
		
		Button storeResult = (Button) findViewById(R.id.button_store);
		storeResult.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),StoreInfoActivity.class);
				startActivity(intent);
			}
		});
	}

}
