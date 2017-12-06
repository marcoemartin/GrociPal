package provider;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.data.DBAdapter;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class SearchSuggestionsProvider extends ContentProvider{
	public static final String AUTHORITY = "provider.SearchSuggestionsProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/products2014");
    public static final Uri CONTENT_URI_STORES = Uri.parse("content://" + AUTHORITY + "/stores2014");
 
    private static final int SUGGESTIONS = 1;
    private static final int SEARCH = 2;
    private static final int STORE_SUGGESTIONS_SELECT = 3;
    private static final int STORE_SEARCH = 4;
    
    DBAdapter db;
    UriMatcher mUriMatcher = buildUriMatcher();
    
	@Override
	public boolean onCreate() {
		db = new DBAdapter(getContext());
		return true;
	}
	
	private UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        
        // This URI is invoked, when user selects a suggestion from search dialog or an item from the listview
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SUGGESTIONS);
        
        // This URI is invoked, when user presses "Go" in the Keyboard of Search Dialog
        uriMatcher.addURI(AUTHORITY, Constants.PRODUCTS_TABLE, SEARCH);
        
        // This URI is invoked, when user selects a suggestion from search dialog or an item from the listview
       // uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, STORE_SUGGESTIONS_SELECT);
        
        //uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, STORE_SUGGESTIONS_SELECT);
        uriMatcher.addURI(AUTHORITY, Constants.STORES_TABLE, STORE_SEARCH);
        return uriMatcher;
    }

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		db.open();
		
		switch(mUriMatcher.match(uri)){
		case SUGGESTIONS :
            c = db.searchProducts(selectionArgs);
            break;
        case SEARCH :
            c = db.searchProducts(selectionArgs);
            break;
        default:
        	throw new IllegalArgumentException("Unknown URL " + uri);	
		 }
		return c;
	}

	@Override
	public String getType(Uri uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}
}
