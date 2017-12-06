 package com.i2ia.grocer.activities.secondary;

import java.util.ArrayList;

import provider.SearchSuggestionsProvider;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.primary.HomeActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;
import com.i2ia.grocer.data.RemoteDatabaseConnector;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchProductsHomeActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
	ArrayList<String> items;
	ArrayList<String> images;
	static DBAdapter db;
	ListView list;
	ListView mLVProducts;
	SimpleCursorAdapter mCursorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_products_home);
		
		mLVProducts = (ListView) findViewById(R.id.results);
		mLVProducts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		
		// Defining CursorAdapter for the ListView
		//String[] d = db.getImageIDs(s);
        mCursorAdapter = new SimpleCursorAdapter(getBaseContext(),
            R.layout.activity_search_products,
            null,
            new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_ICON_1},
            new int[] { android.R.id.text1, android.R.id.icon}, 0);
 
        // Setting the cursor adapter for the country listview
        mLVProducts.setAdapter(mCursorAdapter);
		db = new DBAdapter(getApplicationContext());
		handleIntent(getIntent());
	}
	
	private void handleIntent(Intent intent) {
		// If this activity is invoked by selecting an item from Suggestion of Search dialog or
        // from listview of SearchActivity
        if(intent.getAction().equals(Intent.ACTION_VIEW)){
        	Uri detailUri = intent.getData(); 
        	String id = detailUri.getLastPathSegment();
        	db.open();
        	Product p = db.getItemObject(Integer.valueOf(id), Constants.PRODUCTS_TABLE);
        	db.close();
        	//check price of selected product
        	HomeActivity home= new HomeActivity();
        	RemoteDatabaseConnector mRemoteDB = new RemoteDatabaseConnector();
			mRemoteDB.getPrice(p.getProductnum(), home);
            finish();
        }else if(intent.getAction().equals(Intent.ACTION_SEARCH)){ // If this activity is invoked, when user presses "Go" in the Keyboard of Search Dialog
            String query = intent.getStringExtra(SearchManager.QUERY);
            //check price of user defined product if exits
            finish();
        }
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle data) {
		Uri uri = SearchSuggestionsProvider.CONTENT_URI;
        return new CursorLoader(getBaseContext(), uri, null, null , new String[]{data.getString("query")}, null);
    }

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		mCursorAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}
