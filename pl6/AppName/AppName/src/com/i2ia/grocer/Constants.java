package com.i2ia.grocer;
/**
 * Class for storing and accessing constants
 * @author Daniel
 *
 */
public class Constants {
	//Used for refering to which drawer is open in navigation drawer
	public static final String HOME_ACTIVITY = "HomeActivity";
	public static final String STORE_ACTIVITY = "StoreActivity";
	public static final String MANAGE_ACTIVITY = "ManageActivity";
	public static final String FAVOURITES_ACTIVITY = "FavouritesActivity";
	public static final String SETTINGS_ACTIVITY = "SettingsActivity";
	public static final String ABOUT_ACTIVITY = "AboutActivity";

	//Used for sending and receiving list/table names for intent.putExtra() & Bundle.getString()
	public static final String TABLE_TAG = "TABLE_TAG";
	//Used for sending and receiving the position of the item clicked on the listView
	public static final String POSITION_TAG = "position";
	//Used for sending and receiving the name of the item clicked on the lisView
	public static final String ITEM_TAG = "object";
	//Used for sending and receiving search strings between classes
	public static final String SEARCH_TAG = "SEARCH_TAG";
	//Used for identifying which class started a certain activity
	public static final String CALLER_TAG = "CALLER_TAG";
	//Used for passing null objects between acitivities
	public static final String EMPTY_TAG= "empty_tag";
	//Used for telling the listViewActivity it needs to go to the next item in the list
	public static final String NEXT_TAG = "save and next tag";
	//Used for sharedPreference key-value pair
	public static final String NOTIF_KEY = "NOTIF_KEY";
	//Used for sharedPreference key-value pair
	public static final String TRAVEL_KEY = "TRAVEL_KEY";
	//Used for passing selected store to ItemBrowserActivity class
	public static final String SELECTED_STORE = "SELECTED_STORE";
	//Used when the map fails and returns to previous activity
	public static final String MAP_FAIL_TAG = "maps_fail";
	
	
		//SQL command for deleting tables
	public static final String DATABASE_DELETE_TABLE = "DROP TABLE IF EXISTS '%s'";
	
	//Refers to foodItems table in PredefinedItems.db
	public static final String HOUSECARE_TABLE = "housecareitems2014";
	
	//Refers to foodItems table in PredefinedItems.db
	public static final String HYGIENE_TABLE = "hygenicitems2014";
	
	//Refers to the brands table
	public static final String BRAND_TABLE = "brands2014";
		public static final String BRAND_TABLE_KEY_NAME = "brandName";
		public static final String BRAND_TABLE_KEY_IMG = "imageName";
	
	//Refers to the item favorites
	public static final String FAV_ITEMS_TABLE = "favouriteItems2014";
		public static final String FAV_ITEMS_KEY_ROWID = "docid";
		public static final String FAV_ITEMS_KEY_NAME = "name";
		public static final String FAV_ITEMS_KEY_PRODNUM = "productnum";
		public static final String FAV_ITEMS_KEY_CATEGORY = "category";
		public static final String FAV_ITEMS_KEY_IMG = "imageName";
		public static final String FAV_ITEMS_KEY_AMOUNT = "amount";
		public static final String FAV_ITEMS_KEY_UNIT = "units";
		public static final String FAV_ITEMS_KEY_BRAND = "brand";
		public static final String FAV_ITEMS_KEY_ORGANIC = "organic";
		
	//Refers to foodItems table in PredefinedItems.db
	public static final String PRODUCTS_TABLE = "products2014";
		//Refer to Food table column names
		public static final String PRODUCTS_TABLE_KEY_ROWID = "rowid";
		public static final String PRODUCTS_TABLE_KEY_NAME = "name";
		public static final String PRODUCTS_TABLE_KEY_PRODNUM = "productnum";
		public static final String PRODUCTS_TABLE_KEY_CATEGORY = "category";
		public static final String PRODUCTS_TABLE_KEY_IMG = "imageName";
		public static final String PRODUCTS_TABLE_KEY_AMOUNT = "amount";
		public static final String PRODUCTS_TABLE_KEY_UNIT = "units";
		public static final String PRODUCTS_TABLE_KEY_BRAND = "brand";
		public static final String PRODUCTS_TABLE_KEY_ORGANIC = "organic";
		public static final String CREATE_PRODUCT_TABLE= "CREATE TABLE %s (name text not null, " +
																						"productnum integer not null, " +
																						"category text not null, " +
																						"imageName text not null, " +
																						"amount numeric, " +
																						"units text, brand text, organic text);";
		public static final String FOOD_TABLE_GET_ITEM = "Select * FROM %s WHERE " + PRODUCTS_TABLE_KEY_ROWID + " = %s";
		
	//From Suggestions table items to add to user table
	public static final String CREATE_TEMP_TABLE = "CREATE TABLE temp (name, productnum, category, imageName)";
		public static final String TEMP_TABLE = "temp";
		public static final String TEMP_KEY_NAME = "name";
		public static final String TEMP_KEY_PRODNUM = "productnum";
		public static final String TEMP_KEY_CATEGORY = "category";
		public static final String TEMP_KEY_IMG = "imageName";
		
	//USER defined lists columns
		public static final String USER_TABLE_KEY_ROWID = "docid";
		public static final String USER_TABLE_KEY_NAME = "name";
		public static final String USER_TABLE_KEY_PRODNUM = "productnum";
		public static final String USER_TABLE_KEY_CATEGORY = "category";
		public static final String USER_TABLE_KEY_IMG = "imageName";
		public static final String USER_TABLE_KEY_AMOUNT = "amount";
		public static final String USER_TABLE_KEY_UNIT = "units";
		public static final String USER_TABLE_KEY_BRAND = "brand";
		public static final String USER_TABLE_KEY_ORGANIC = "organic";
	public static final String UPDATE_USER_FIELD = "UPDATE %s SET %s = '%s' WHERE "+USER_TABLE_KEY_NAME+" = '%s'";
	public static final String ORGANIC_CHEKED = "Yes";
	public static final String ORGANIC_UNCHECKED = "No";
		
	//Refers to stores table
	public static final String STORES_TABLE = "stores2014";
		public static final String STORES_TABLE_KEY_ROWID = "docid";
		public static final String STORES_TABLE_KEY_NAME = "name";
		public static final String STORES_TABLE_KEY_ICON = "imageName";
	
	//Refers to favourites table
	public static final String FAV_STORES_TABLE = "favouriteStores2014";
		public static final String FAV_STORES_KEY_ROWID = "docid";
		public static final String FAV_STORES_KEY_NAME = "name";
		public static final String FAV_STORES_KEY_POSTAL = "postalCode";
		public static final String FAV_STORE_KEY_STREETADDRESS = "streetAddress";
		public static final String FAV_STORE_KEY_COUNTRY = "country";
		public static final String FAV_STORE_KEY_PROVINCE = "province";
		public static final String FAV_STORE_KEY_ICON = "imageName";
		public static final String FAV_STORE_KEY_ID = "storeID";
		public static final String FAV_STORE_KEY_PHONE = "phonenumber";
		public static final String FAV_STORE_KEY_WEBSITE = "website";
		public static final String FAV_STORE_KEY_CITY = "city";
		
	//Refers to internaldatabase
	public static final String DATABASE_NAME = "internaldatabase.db";
	//Refers to database path
	public static final String DATABASE_PATH = "/data/data/" + "com.i2ia.grocer" + "/databases/" + DATABASE_NAME;
	//Refers to name of store
	public static final String STORE_NAME = "store";
	//Used for accessing google api servers
	public static final String GOOGLE_API_KEY = "AIzaSyDCbUIJNl5mAP46OO1xiH3PP19QNv98ZyM";
	public static final String GET_ITEM = "Select * FROM %s WHERE rowid = %s";
	public static final String GET_ITEMS = "Select * FROM products2014";
	public static final String DELETE_RECORD = "DELETE FROM %s WHERE %s = '%s'";
	public static final String SEARCH = "SELECT * FROM %s WHERE %s LIKE '/%%s/%'";
	public static final String ACTIVATE_BUTTON_TAG = "activate button";
	public static final String GET_FAV_ITEMS = "Select * FROM "+FAV_ITEMS_TABLE;
	public static final String RENAME_TABLE = "ALTER TABLE %s RENAME TO %s";
	
	public static final String GET_ITEM_BY_COLUMN = "Select * FROM %s WHERE %s = '%s'";
	public static final String FAVOURITE_CATEGORY = "-1";
	
	//top left XY modification
	public static final double TOP_LEFT_X = 0.018;
	public static final double TOP_LEFT_Y = -0.1;
	
	//Top right XY modification
	public static final double TOP_RIGHT_X = 0.09;
	public static final double TOP_RIGHT_Y = 0.09;
	
	//Bottom left XY modification
	public static final double BOTTOM_LEFT_X = -0.09;
	public static final double BOTTOM_LEFT_Y = -0.09;
	
	//Bottom right XY modification
	public static final double BOTTOM_RIGHT_X = -0.01;
	public static final double BOTTOM_RIGHT_Y = 0.15;
	
	public static final String nearbyStores = "Stores nearby";
	
	//Used to identify what scale is used for product
	public static final Object USES_ORGANIC = "yes";

}
