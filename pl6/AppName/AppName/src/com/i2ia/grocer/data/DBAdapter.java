package com.i2ia.grocer.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Manager for saving lists using a local SQLite database
 * @author Daniel
 *
 */
public class DBAdapter{
	static final int DATABASE_VERSION = 1;
	static final String DATABASE_GET_TABLES = "SELECT name FROM sqlite_master WHERE type='table';";
	static String DATABASE_PATH = null;
	final Context context;
	private HashMap<String, String> mAliasMap;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	/**
	 * 
	 * @param context
	 */
	public DBAdapter(Context context){
		this.context = context;
		DBHelper = new DatabaseHelper(context);
		DATABASE_PATH = Constants.DATABASE_PATH;
		// This HashMap is used to map table fields to Custom Suggestion fields
        mAliasMap = new HashMap<String, String>();
 
        // Unique id for the each Suggestions ( Mandatory )
        mAliasMap.put("_ID", "rowid as " + "_id" );
 
        // Text for Suggestions ( Mandatory )
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, Constants.PRODUCTS_TABLE_KEY_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);
 
        // Icon for Suggestions ( Optional )
        mAliasMap.put( SearchManager.SUGGEST_COLUMN_ICON_1, Constants.PRODUCTS_TABLE_KEY_IMG + " as " + SearchManager.SUGGEST_COLUMN_ICON_1);
 
        // This value will be appended to the Intent data on selecting an item from Search result or Suggestions ( Optional )
        mAliasMap.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );
	}
	
	/**
	 * 
	 * @author Daniel
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper{
		DatabaseHelper(Context context){
			super(context,Constants.DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public void onCreate(SQLiteDatabase db)
		{
		}
		/**
		 * 
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
				//Do nothing
		}
		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
				//Do nothing
		}
	}
	/**
	 * 
	 * @return
	 */
	public DBAdapter open() {
		
		String  mydir = "data/data/com.i2ia.grocer/databases";
		File file = new File(mydir);
		
		if(!file.exists()){
			file.mkdir();
			
				try {
					copyDB(context.getAssets().open(Constants.DATABASE_NAME),new FileOutputStream(DATABASE_PATH));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
		
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void copyDB(InputStream inputStream,OutputStream outputStream){
		
		byte[] buffer = new byte[1024];
		int length;
		try {
			while((length = inputStream.read(buffer))>0){
				outputStream.write(buffer,0,length);
			}
			inputStream.close();
			outputStream.close();
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 */
	public void close(){
		DBHelper.close();
	}
	
	/**
	 * 
	 * @param tableName
	 * @param prodName
	 * @param productNum
	 * @return
	 */
	public long insertItem(String tableName, String name, String productnumber, String category, 
						String img, int amount, String units, String brand, String organic){
		if(!rowExists(tableName,Constants.USER_TABLE_KEY_NAME,name)){
			ContentValues initValues = new ContentValues();
			initValues.put(Constants.USER_TABLE_KEY_NAME, name);
			initValues.put(Constants.USER_TABLE_KEY_PRODNUM, productnumber);
			initValues.put(Constants.USER_TABLE_KEY_CATEGORY, category);
			initValues.put(Constants.USER_TABLE_KEY_IMG, img);
			initValues.put(Constants.USER_TABLE_KEY_AMOUNT, amount);
			initValues.put(Constants.USER_TABLE_KEY_UNIT, units);
			initValues.put(Constants.USER_TABLE_KEY_BRAND, brand);
			initValues.put(Constants.USER_TABLE_KEY_ORGANIC, organic);
			return db.insert(tableName, null, initValues);
		}
		return -1;
	}
	/**
	 * 
	 * @return
	 */
	public boolean rowExists(String tableName,String columnKey,String name){
		Cursor c = db.rawQuery(String.format(Constants.GET_ITEM_BY_COLUMN,tableName,columnKey, name),null);
		if(c.moveToFirst()){
			Log.d("HERE","True");
			return true;
		}
		else{
			Log.d("HERE","False");
			return false;
		}
	}
	
	/***
	 * inserts a product given a Product object
	 * @param p
	 * @param tableName
	 * @return 
	 */
	public long insertItem(Product p, String tableName){
		if(!rowExists(tableName,Constants.USER_TABLE_KEY_NAME,p.getName())){
			ContentValues initValues = new ContentValues();
			initValues.put(Constants.USER_TABLE_KEY_NAME, p.getName());
			initValues.put(Constants.USER_TABLE_KEY_PRODNUM, p.getProductnum());
			initValues.put(Constants.USER_TABLE_KEY_CATEGORY, p.getCategory());
			initValues.put(Constants.USER_TABLE_KEY_IMG, p.getImageName());
			initValues.put(Constants.FAV_ITEMS_KEY_AMOUNT, p.getAmount());
			initValues.put(Constants.FAV_ITEMS_KEY_UNIT, p.getUnits());
			initValues.put(Constants.FAV_ITEMS_KEY_BRAND, p.getBrand());
			initValues.put(Constants.FAV_ITEMS_KEY_ORGANIC, p.getOrganic());
			return db.insert(tableName, null, initValues);
		}
		return -1;
	}
	
	/**
	 * 
	 * @param rowId
	 * @return
	 */
	public boolean deleteItem(long rowId, String tableName){
		return (db.delete(tableName, Constants.PRODUCTS_TABLE_KEY_ROWID + "=" + rowId,null) > 0);
	}
	public boolean deleteItem(String name, String tableName){
		return (db.delete(tableName, Constants.FAV_ITEMS_KEY_NAME + " = " +"'"+name+"'",null) > 0);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean addProductTable(String name){
		if(!getAllTables().contains(name)){
			String CREATE_TABLE = String.format(Constants.CREATE_PRODUCT_TABLE, name);
			db.execSQL(CREATE_TABLE);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getAllTables(){
		ArrayList<String> userTables = new ArrayList<String>();
		String[] censored_tables= context.getResources().getStringArray(R.array.CensoredTables);
		Cursor c = db.rawQuery(DATABASE_GET_TABLES, null);
		if (c.moveToFirst()) {
		    do {
		    	String tableName = c.getString(0);
		    	if(!Arrays.asList(censored_tables).contains(tableName)){
		    		userTables.add(tableName);
		    		
		    	}
		    	c.moveToNext();
		    }while(!c.isAfterLast());
		}
		
		return userTables;
	}
	
	/**
	 * Returns the names of all the products in the given table
	 * @param table
	 * @return
	 */
	public ArrayList<String> getAllItemNames(String table){
		return getColumnData(table, Constants.PRODUCTS_TABLE_KEY_NAME);
	}
	
	/**
	 * Returns the product numbers of all the products in the given table
	 * @param table
	 * @return
	 */
	public ArrayList<Integer> getAllProductNum(String table){
		ArrayList<String> temp = getColumnData(table, Constants.PRODUCTS_TABLE_KEY_PRODNUM);
		ArrayList<Integer> newList = new ArrayList<Integer>();
		for(String s: temp){
			newList.add(Integer.parseInt(s));
		}
		
		return newList;
	}
	
	/***
	 * returns an arraylist with the row IDs of all the products in the table
	 * @param table
	 * @return
	 */
	public ArrayList<Integer> getAllItemRowIds(String table) {
		ArrayList<String> temp= getColumnData(table, Constants.PRODUCTS_TABLE_KEY_ROWID);
		ArrayList<Integer> newList = new ArrayList<Integer>();
		for(String s: temp){
			newList.add(Integer.parseInt(s));
		}
		
		return newList;
	}
	/**
	 * return arraylist of all street addresses from given table
	 * @param table
	 * @return
	 */
	public ArrayList<String> getAllAddresses(String table){
		return getColumnData(table, "streetAddress");
	}
	
	/***
	 * Returns all the content in a given column of a given table
	 * @param table name of the table 
	 * @param column the name of the column in the table
	 * @return all the values of the column in an ArrayList 
	 */
	private ArrayList<String> getColumnData(String table, String column){
		ArrayList<String> allItems= new ArrayList<String>();
		Cursor c = db.query(table, new String[] {column}, null,null,null,null,null);
		if (c.moveToFirst()) {
		    do {
		    	allItems.add(c.getString(0));
		        c.moveToNext();
		    }while(!c.isAfterLast());
		}
		return allItems;
	}
	
	/**
	 * Returns the names of all the icons in the Products table
	 * @param table
	 * @return
	 */
	public ArrayList<String> getAllItemImages(String table){
		return getColumnData(table, Constants.PRODUCTS_TABLE_KEY_IMG);
	}
	
	/**
	 * Returns the entire row of a given product in the Products table
	 * @param rowId
	 * @return an array with the value of every column as an element of the list. 
	 * e.g. docid, name, product number, category
	 */
	public ArrayList<String> getItem(String name, String tableName){
		ArrayList<String> row = new ArrayList<String>();
		Cursor c = db.rawQuery(String.format(Constants.GET_ITEM_BY_COLUMN, tableName, Constants.PRODUCTS_TABLE_KEY_NAME, name),null);
		if (c.moveToFirst()) {
			int indx = 0;
			while(indx < c.getColumnCount()){
				row.add(c.getString(indx));
				indx++;
			}
		}
		return row;
	}
	
	/***
	 * Returns a Product object given the rowID of the given table 
	 * @param rowId
	 * @param itemTable
	 * @return
	 */
	public Product getItemObject(int rowId, String itemTable) {
		ArrayList<String> row = new ArrayList<String>();
		Cursor c = db.rawQuery(String.format(Constants.GET_ITEM, itemTable, rowId),null);
		if (c.moveToFirst()) {
			int indx = 0;
			while(indx < c.getColumnCount()){
				row.add(c.getString(indx));
				indx++;
			}
		}
		return new Product(row.get(0), Integer.parseInt(row.get(1)), row.get(2), row.get(3), 
				Integer.parseInt(row.get(4)), row.get(5), row.get(6), row.get(7));
	}
	
	/***
	 * Returns a Product object given the rowID of the given table 
	 * @param rowId
	 * @param itemTable
	 * @return
	 */
	public Product getItemObject(String name, String itemTable) {
		ArrayList<String> row = new ArrayList<String>();
		Cursor c = db.rawQuery(String.format(Constants.GET_ITEM_BY_COLUMN, itemTable, Constants.USER_TABLE_KEY_NAME, name),null);
		if (c.moveToFirst()) {
			int indx = 0;
			while(indx < c.getColumnCount()){
				row.add(c.getString(indx));
				indx++;
			}
		}
		return new Product(row.get(0), Integer.parseInt(row.get(1)), row.get(2), row.get(3), 
				Integer.parseInt(row.get(4)), row.get(5), row.get(6), row.get(7));
	}
	
	/**
	 * Updates a row in the Products table
	 * @param rowId
	 * @param name
	 * @param productnum
	 * @return
	 */
	public boolean updateItem(long rowId,String tableName, String name, String productnum, String category, String img){
		ContentValues args = new ContentValues();
		args.put(Constants.PRODUCTS_TABLE_KEY_NAME, name);
		args.put(Constants.PRODUCTS_TABLE_KEY_PRODNUM, productnum);
		args.put(Constants.PRODUCTS_TABLE_KEY_CATEGORY, category);
		args.put(Constants.PRODUCTS_TABLE_KEY_IMG, img);
		return (db.update(tableName, args, Constants.PRODUCTS_TABLE_KEY_ROWID + "=" + rowId, null) > 0);
	}
	
	/**
	 * Deletes given table from database
	 * Return true if successful
	 * @param table
	 * @return 
	 */
	public void deleteTable(String table){
		db.execSQL("DROP TABLE IF EXISTS '" + table + "'");
		}
	
	/**
	 * Returns an ArrayList of all the names of the stores in the given table 
	 * @param table name of the table with the stores
	 * @return
	 */
	public ArrayList<String> getAllStoreNames(String table) {
		return getColumnData(table, Constants.FAV_STORES_KEY_NAME);
	}
	
	/***
	 * returns an arraylist with the row IDs of all the stores in the table
	 * @param table
	 * @return
	 */
	public ArrayList<Integer> getAllStoreRowIds(String table) {
		ArrayList<String> temp= getColumnData(table, Constants.STORES_TABLE_KEY_ROWID);
		ArrayList<Integer> newList = new ArrayList<Integer>();
		for(String s: temp){
			newList.add(Integer.parseInt(s));
		}
		
		return newList;
	}
	
	/**
	 * Return arraylist of all storeIDs in given table
	 */
	public ArrayList<String> getStoreIDs(String table){
		return getColumnData(table,Constants.FAV_STORE_KEY_ID);
	}
	
	/**
	 * Returns 
	 * @param storesTable
	 * @return
	 */
	public ArrayList<String> getAllStoreImages(String table) {
		return getColumnData(table, Constants.STORES_TABLE_KEY_ICON);
	}

	
//	/**
//	 * Checks if the given table exists
//	 * @param tableName
//	 * @return
//	 */
//	public boolean tableExist(String tableName){
//		db.rawQuery("SELECT " + + "FROM sqlite_master WHERE type = 'table' AND name=" + tableName);
//	}
	/**
	 * 
	 * @param storeName
	 * @return
	 */
	public Store getStoreData(String storeName){
		Store store = null;
		Cursor c = db.rawQuery("Select "+Constants.STORES_TABLE_KEY_NAME+", "+Constants.STORES_TABLE_KEY_ICON+", "
								+" FROM " +Constants.STORES_TABLE + " WHERE name = " + "\'" +storeName + "\'" ,null);
		if(c.moveToFirst()){
			store = new Store(c);
			}
		return store;
		}
	
	/**
	 * Inserts a store to the favorite stores table
	 * @param store
	 * @return
	 */
	public boolean insertFavStore(Store store){
		ContentValues initValues = new ContentValues();
		
		initValues.put(Constants.FAV_STORES_KEY_NAME, store.getName());
		initValues.put(Constants.FAV_STORE_KEY_ICON,store.getIconName());
		
		return (db.insert(Constants.FAV_STORES_TABLE, null, initValues) > 0);
	}
	
	public boolean insertFavStore(JSONObject store){
		ContentValues initValues = new ContentValues();
		
		String streetAddress = "";
		
		try {
			streetAddress = streetAddress + store.getString("vicinity");
			initValues.put(Constants.FAV_STORE_KEY_STREETADDRESS, streetAddress);
			initValues.put(Constants.FAV_STORES_KEY_NAME, store.getString("name"));
			initValues.put(Constants.FAV_STORE_KEY_ID, store.getString("place_id"));
			initValues.put(Constants.FAV_STORE_KEY_ICON, "nearby_icon");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
;
		
		return (db.insert(Constants.FAV_STORES_TABLE, null, initValues) > 0);
	}
	
	/***
	 * Removes a store from the favorites table 
	 * @param store an object Store
	 */
	public void removeFavStore(String name){
		db.execSQL(String.format(Constants.DELETE_RECORD, Constants.FAV_STORES_TABLE, Constants.FAV_STORES_KEY_NAME, name));
	}
	
	public void removeFavStore(JSONObject store){
		String storeID = "";
		
		try{
			storeID = store.getString("place_id");
		}catch(Exception e){
			e.printStackTrace();
		}
		db.execSQL(String.format(Constants.DELETE_RECORD, Constants.FAV_STORES_TABLE, Constants.FAV_STORE_KEY_ID, storeID));
	}
	
	/***
	 * Removes a product/item from the favorites table
	 * @param product
	 * @param rowId
	 * @return
	 */
	public void removeFavItem(String name){
		db.execSQL(String.format(Constants.DELETE_RECORD, Constants.FAV_ITEMS_TABLE, Constants.FAV_ITEMS_KEY_NAME, name));
	}
	
	/***
	 * Adds the specified product/item to the favorites table
	 * @param item
	 * @return
	 */
	public boolean insertFavItem(Product item){
		ContentValues initValues = new ContentValues();

		initValues.put(Constants.FAV_ITEMS_KEY_NAME, item.getName());
		initValues.put(Constants.FAV_ITEMS_KEY_PRODNUM, item.getProductnum());
		//Change category because item is now favourite
		initValues.put(Constants.FAV_ITEMS_KEY_CATEGORY, item.getCategory());
		initValues.put(Constants.FAV_ITEMS_KEY_IMG, item.getImageName());
		initValues.put(Constants.FAV_ITEMS_KEY_AMOUNT, item.getAmount());
		initValues.put(Constants.FAV_ITEMS_KEY_UNIT, item.getUnits());
		initValues.put(Constants.FAV_ITEMS_KEY_BRAND, item.getBrand());
		initValues.put(Constants.FAV_ITEMS_KEY_ORGANIC, item.getOrganic());

		return (db.insert(Constants.FAV_ITEMS_TABLE, null, initValues) > 0);
	}

	/***
	 * Returns a Store object given the rowID of the store in the given table 
	 * @param rowId
	 * @param storesTable
	 * @return
	 * 
	 */
	public Store getStoreObject(int rowId, String storesTable) {
		ArrayList<String> row = new ArrayList<String>();
		Cursor c = db.rawQuery(String.format(Constants.GET_ITEM, storesTable, rowId),null);
		if (c.moveToFirst()) {
			int indx = 0;
			while(indx < c.getColumnCount()){
				row.add(c.getString(indx));
				indx++;
			}
		}
		return new Store(row.get(0), row.get(1));
	}

	/***
	 * Replaces a specific value in a the user table given for the specified column name 
	 * @param tableName
	 * @param columnName
	 * @param value
	 * @param rowId
	 */
	public void updateField(String tableName, String columnName, String value, String name) {
		db.execSQL(String.format(Constants.UPDATE_USER_FIELD, tableName, columnName, value, name));
	}

	public Cursor searchProducts(String[] selectionArgs) {
		//ArrayList<String> row = new ArrayList<String>();
		//Cursor c = db.rawQuery(String.format(Constants.SEARCH, Constants.FOOD_TABLE, Constants.FOOD_TABLE, query), null);
		String selection = Constants.PRODUCTS_TABLE_KEY_NAME + " like ? ";
		 
        if(selectionArgs[0]!=null){
            selectionArgs[0] = "%"+selectionArgs[0] + "%";
        }
 
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setProjectionMap(mAliasMap);
 
        queryBuilder.setTables(Constants.PRODUCTS_TABLE);
		
		Cursor c=  queryBuilder.query(db, new String[]{ "_ID",
                SearchManager.SUGGEST_COLUMN_TEXT_1 ,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID},
				 selection, selectionArgs ,null,null,Constants.PRODUCTS_TABLE_KEY_NAME + " asc ","10");
		return c;
	}
	
	public Cursor searchProduct(String id){
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		 
        queryBuilder.setTables(Constants.PRODUCTS_TABLE);
 
        Cursor c = queryBuilder.query(db, new String[] {Constants.PRODUCTS_TABLE_KEY_ROWID, Constants.PRODUCTS_TABLE_KEY_NAME,
        												Constants.PRODUCTS_TABLE_KEY_PRODNUM, Constants.PRODUCTS_TABLE_KEY_CATEGORY, 
        												Constants.PRODUCTS_TABLE_KEY_IMG}, "rowid = ?", new String[] { id },
        												null, null, null ,"1");
        return c;
	}



	/***
	 * Gets the ID of all the images in the array list and returns an array list
	 * with the resource IDs
	 * @param iconNames
	 * @return
	 */
	public ArrayList<Integer> getImageIDs(ArrayList<String> iconNames){
		ArrayList<Integer> storeIDs = new ArrayList<Integer>();
		for(String s: iconNames){
			storeIDs.add(context.getResources()
					.getIdentifier(s, "drawable", 
							context.getPackageName()));
		}
		return storeIDs;
	}
	
	/**
	 * Returns Hashmap of products, mapping category to array of items
	 * 
	 * @param category
	 * @return
	 */
	public HashMap<String, ArrayList<Product>> getAllItems(String category) {
		HashMap<String, ArrayList<Product>> products = new HashMap<String, ArrayList<Product>>();
		Cursor fav = db.rawQuery(Constants.GET_FAV_ITEMS, null);
		Cursor c = db.rawQuery(Constants.GET_ITEMS, null);

		//GET regular products
		if (c.moveToFirst()) {
			do {
				// If list already exists, add new product
				if (products.containsKey(c.getString(2))) {
					String s = c.getString(4);
					products.get(c.getString(2)).add(
							new Product(c.getString(0), Integer.parseInt(c.getString(1)), c.getString(2), c.getString(3), 
									Integer.parseInt(s), 
									c.getString(5), c.getString(6), 
									c.getString(7)));
				} else {
					// If list doesn't exist create new list and add first
					// product
					ArrayList<Product> newChildList = new ArrayList<Product>();
					newChildList.add(new Product(c.getString(0), Integer.parseInt(c.getString(1)), c.getString(2), c.getString(3), 
							Integer.parseInt(c.getString(4)), c.getString(5), c.getString(6), c.getString(7)));
					products.put(c.getString(2), newChildList);
				}

				c.moveToNext();

			} while (!c.isAfterLast());
		}
		
		//Get favourite products
		if (fav.moveToFirst()) {
			Log.d("FAV test",fav.toString());
			Log.d("FAV TEST2",fav.getString(2));
			do {
				// If list already exists, add new product
				if (products.containsKey("-1")) {
					products.get("-1").add(
							new Product(fav.getString(0), Integer.parseInt(fav
									.getString(1)), fav.getString(2), fav
									.getString(3), Integer.parseInt(fav.getString(4)), 
									fav.getString(5), fav.getString(6), fav.getString(7)));
				} else {
					// If list doesn't exist create new list and add first
					// product
					ArrayList<Product> newChildList = new ArrayList<Product>();
					newChildList.add(new Product(fav.getString(0), Integer
							.parseInt(fav.getString(1)), fav.getString(2), fav
							.getString(3), Integer.parseInt(fav.getString(4)), 
							fav.getString(5), fav.getString(6), fav.getString(7)));
					products.put("-1", newChildList);
				}
				fav.moveToNext();
			} while (!fav.isAfterLast());
		}
		return products;
	}

	/***
	 * Creates a table to hold the items the user clicks when selecting items to add
	 * from the suggestions
	 */
	public void createTempTalbe() {
		db.execSQL(String.format(Constants.DATABASE_DELETE_TABLE, Constants.TEMP_TABLE));
		db.execSQL(Constants.CREATE_TEMP_TABLE);
	}
	
	public void renameTable(String oldName, String newName){
		db.execSQL(String.format(Constants.RENAME_TABLE, oldName, newName));
	}
}
