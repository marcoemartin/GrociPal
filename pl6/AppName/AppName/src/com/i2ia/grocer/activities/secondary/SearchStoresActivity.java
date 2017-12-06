package com.i2ia.grocer.activities.secondary;

import java.util.ArrayList;

import provider.SearchSuggestionsProvider;

import com.i2ia.grocer.R;
import com.i2ia.grocer.R.id;
import com.i2ia.grocer.R.layout;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter.LengthFilter;
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
import android.widget.Toast;

public class SearchStoresActivity extends FragmentActivity implements LoaderCallbacks<Cursor>{

	ArrayList<String> items;
	ArrayList<String> images;
	static DBAdapter db;
	ListView list;
	static String tableName;
	ListView mLVProducts;
	SimpleCursorAdapter mCursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_stores);
		
		mLVProducts = (ListView) findViewById(R.id.results);
		mLVProducts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		
		// Defining CursorAdapter for the ListView
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
        	Intent intnt = new Intent(this, StoreInfoActivity.class);
        	intnt.setData(intent.getData());
        	startActivity(intnt);
            finish();
        }else if(intent.getAction().equals(Intent.ACTION_SEARCH)){ // If this activity is invoked, when user presses "Go" in the Keyboard of Search Dialog
            Toast.makeText(getApplicationContext(), "Store not Found", Toast.LENGTH_SHORT).show();
        }
	}

	 /** This method is invoked by initLoader() */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle data) {
        Uri uri = SearchSuggestionsProvider.CONTENT_URI_STORES;
        return new CursorLoader(getBaseContext(), uri, null, null , new String[]{data.getString("query")}, null);
    }
 
    /** This method is executed in ui thread, after onCreateLoader() */
    @Override
        public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        mCursorAdapter.swapCursor(c);
    }

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
	}
}
