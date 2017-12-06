 package com.i2ia.grocer.activities.secondary;

import java.util.ArrayList;

import provider.SearchSuggestionsProvider;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.primary.SettingsActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ClipData.Item;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

/**
 * Activity for displaying search results for itemsBrowserActivity
 * @author marcom
 *
 */
public class SearchProductsActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
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
		setContentView(R.layout.activity_search_products);
		
		Intent intent = getIntent();
		tableName = intent.getStringExtra("KEY");
		
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
            new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1},
            new int[] { android.R.id.text1}, 0);
 
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
        	db.insertItem(p, tableName);
        	db.close();
            finish();
        }else if(intent.getAction().equals(Intent.ACTION_SEARCH)){ //this activity is invoked, when user presses "Go" in the Keyboard of Search Dialog
            String query = intent.getStringExtra(SearchManager.QUERY);
            db.open();
            db.insertItem(tableName, query, "-1", "-1", "", 1,"","None","no");
            db.close();
            Toast.makeText(getApplicationContext(), "added item", Toast.LENGTH_SHORT).show();
            finish();
            //confirmItemAdded();
        }
	}
	
	private void confirmItemAdded(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());//QUE PONGO AQUI!?!?
		
		builder.setMessage("Item has been added to List.")
        .setPositiveButton(R.string.ok_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	finish();
            }
        });
		// Create the AlertDialog object and return it
		builder.create().show();
	}

	 /** This method is invoked by initLoader() */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle data) {
        Uri uri = SearchSuggestionsProvider.CONTENT_URI;
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
