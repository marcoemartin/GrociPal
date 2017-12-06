package com.i2ia.grocer.activities.secondary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.primary.ManageListsActivity;
import com.i2ia.grocer.activities.primary.StoreBrowserActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;
import com.i2ia.grocer.data.RemoteDatabaseConnector;
import com.i2ia.grocer.data.SharedPreferenceManager;
import com.i2ia.grocer.data.Store;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
/**
 * Activity for viewing a list in detail
 * @author Daniel
 *
 */
public class ListViewActivity extends SecondaryBaseActivity {
	private static String tableName;
	public static ArrayList<String> itemNames = new ArrayList<String>();
	private static ArrayList<String> itemImgNames = new ArrayList<String>();
	private static ArrayList<Integer> itemIDs = new ArrayList<Integer>();
	private static ListView listView;
	private static DBAdapter db;
	private static ArrayAdapter<String> mArrayAdapter;
	private static Context context;
	private Button btn;
	public static ArrayList<String> category;
	public static ArrayList<String> categoryImgName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListViewActivity.context = this;
		//setContentView(R.layout.activity_list_view);
		
		db = new DBAdapter(this);
		
		//Displaying Home Button on ActionBar
		ActionBar actBar = getSupportActionBar();
		actBar.setDisplayHomeAsUpEnabled(true);
		actBar.setHomeButtonEnabled(true);
//        
        //Get list Name
        Intent intent = getIntent();
		Bundle nameBundle = intent.getExtras();
		tableName = nameBundle.getString(Constants.TABLE_TAG);
		int next = nameBundle.getInt(Constants.NEXT_TAG);
		actBar.setTitle(tableName);//Display list/table name
		
		//Get table contents
		getTableContents();
		
		//Filling list view with lists
		listView = (ListView) this.findViewById(R.id.listView_items);
		populateList();
				
//		//Display table contents
//        mArrayAdapter = new ArrayAdapter<String>(this,R.layout.list_view_item,itemNames);
//		listView = (ListView) findViewById(R.id.listView_items);	
//		listView.setAdapter(mArrayAdapter);			
//		listView.setOnItemClickListener(listViewClickListener);
       
		//Enable Buttons
		clickableButtons();
		if(next != -1 && next < itemNames.size()){
			//grab next item and open dialog
			db.open();
			Product p = db.getItemObject(itemNames.get(next), tableName);
			db.close();
			Intent intnt = new Intent(context.getApplicationContext(),CustomDialogActivity.class);
			intnt.putExtra(Constants.ITEM_TAG, p);
			intnt.putExtra(Constants.TABLE_TAG, tableName);
			intnt.putExtra(Constants.POSITION_TAG, next);
    		context.startActivity(intnt);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.delete:
	        	db.open();
	    		db.deleteItem(itemNames.get(info.position), tableName);
	    		db.close();
	    		reload();
	            return true;
	        case R.id.details:
	        	db.open();
				Product p = db.getItemObject(itemNames.get(info.position), tableName);
				db.close();
	        	Intent intent = new Intent(context.getApplicationContext(),CustomDialogActivity.class);
				intent.putExtra(Constants.ITEM_TAG, p);
				intent.putExtra(Constants.TABLE_TAG, tableName);
				intent.putExtra(Constants.POSITION_TAG, info.position);
	    		context.startActivity(intent);
	        	return true;
	        case R.id.edit:
	        	db.open();
	    		db.deleteItem(itemNames.get(info.position), tableName);
	    		db.close();
	    		reload();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}	
	
	protected void createCategoryDialog(final int position) {
		final ArrayList<Integer> categoryImgIDs = (ArrayList<Integer>)db.getImageIDs(categoryImgName);
		ListAdapter adapter = new ArrayAdapterWithIcon(context, category, categoryImgIDs);
		
        new AlertDialog.Builder(context).setTitle("Pick a Category")
            .setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item ) {
                    String categoryName = category.get(item);
                    String[] c = context.getResources().getStringArray(R.array.Categories);
                    List<String> cat = Arrays.asList(c);
                    
                    db.open();
					db.updateField(tableName, Constants.USER_TABLE_KEY_CATEGORY, 
									Integer.toString(cat.indexOf(categoryName)), itemNames.get(position));
					db.updateField(tableName, Constants.USER_TABLE_KEY_IMG, 
									categoryImgName.get(item), itemNames.get(position));
					db.close();
					ListViewActivity lva = new ListViewActivity();
					lva.reload();
                }

        }).show();
	}
	
	public void populateList(){
		CustomItemBrowserListAdapter cla = new CustomItemBrowserListAdapter(context, 
				itemNames, 
				itemImgNames,R.layout.row_layout_list_view);
		listView.setAdapter(cla);
		registerForContextMenu(listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				db.open();
				Product p = db.getItemObject(itemNames.get(position), tableName);
				db.close();
				Intent intent = new Intent(context.getApplicationContext(),CustomDialogActivity.class);
				intent.putExtra(Constants.ITEM_TAG, p);
				intent.putExtra(Constants.TABLE_TAG, tableName);
				intent.putExtra(Constants.POSITION_TAG, position);
	    		context.startActivity(intent);
			}
		});
		
//		listView = (ListView) ((Activity) context).findViewById(R.id.listView_items);
//		CustomItemBrowserListAdapter cla = new CustomItemBrowserListAdapter(context, 
//				itemNames, 
//				itemImgNames,R.layout.row_layout_list_view);
//		listView.setAdapter(cla);
//		listView.setOnItemClickListener(new ListView.OnItemClickListener(){
//			
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				db.open();
//				Product p = db.getItemObject(position+1, tableName);
//				db.close();
//				Intent intent = new Intent(context.getApplicationContext(),CustomDialogActivity.class);
//				intent.putExtra(Constants.ITEM_TAG, p);
//				intent.putExtra(Constants.TABLE_TAG, tableName);
//				intent.putExtra(Constants.POSITION_TAG, position);
//	    		context.startActivity(intent);
//			}
//		});
	}
	
	/**
	 * Return contents (list items) of current table
	 */
	public void getTableContents(){
		db.open();		
		//Clear tableContents so listView doesn't double onResume()
		itemNames.clear();
		itemImgNames.clear();
		categoryImgName = new ArrayList<String>();
		category = new ArrayList<String>();;
		
		itemImgNames = db.getAllItemImages(tableName);
		itemNames = db.getAllItemNames(tableName);
		itemIDs = db.getAllItemRowIds(tableName);
		categoryImgName.addAll(Arrays.asList(getResources().getStringArray(R.array.CategoryIcons)));
		category.addAll(Arrays.asList(getResources().getStringArray(R.array.Categories)));
		db.close();
	}

	
	/**
	 * Manages selection of menu items
	 */
	public boolean onOptionsItemSelected(MenuItem item){
	    switch (item.getItemId()) {
	    //If menu item selected is trash icon
        case R.id.action_delete:
        	confirmDelete();
            return true;
        case R.id.action_edit:
        	editName();
        	return true;
        case R.id.action_add:
        	Intent intent = new Intent(getApplicationContext(),ItemBrowserActivity.class);
			intent.putExtra(Constants.TABLE_TAG, tableName);
			startActivity(intent);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}
	

	private void editName() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Rename List");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setHint("Enter new Name");
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getEditableText().toString();
		  if(!value.equals("")){
			  db.open();
			  db.renameTable(tableName, value);
			  db.close();
			  
			  tableName = value;
			  ActionBar actBar = getSupportActionBar();
			  actBar.setDisplayHomeAsUpEnabled(true);
			  actBar.setHomeButtonEnabled(true);
			  actBar.setTitle(tableName);
			  reload();
		  }
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    dialog.dismiss();
		  }
		});

		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view, menu);
		return true;
	}
	
	public void reload(){
		db.open();
		itemImgNames = db.getAllItemImages(tableName);
		itemNames = db.getAllItemNames(tableName);
		itemIDs = db.getAllItemRowIds(tableName);
		db.close();
		
		populateList();
	}

	/**
	 * Dialog box to confirm delete of list
	 */
	public void confirmDelete(){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	db.open();
	        		db.deleteTable(tableName);
	        		Intent intent = new Intent(getApplicationContext(),ManageListsActivity.class);
	        		startActivity(intent);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Delete " + tableName  +"?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
	}
	/**
	 * Displays result from pricecheck
	 * @param cost
	 */
	public static void displayPrice(String cost){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case DialogInterface.BUTTON_POSITIVE:
					break;
				}
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(cost).setPositiveButton("Okay", dialogClickListener).show();
	}
	
	/**
	 * Manages onClick behavior of buttons
	 */
	public void clickableButtons(){
		Button addItemsButton = (Button) findViewById(R.id.button_add_items);
		addItemsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				//pass table name to ItemBrowserActivity
				Intent intent = new Intent(getApplicationContext(),ItemBrowserActivity.class);
				intent.putExtra(Constants.TABLE_TAG, tableName);
				startActivity(intent);
			}
		});
		
		
		Button priceCheckbutton = (Button) findViewById(R.id.button_check_price);
		priceCheckbutton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				db.open();
				ArrayList<Integer> productNumbers = db.getAllProductNum(tableName);
				db.close();
				RemoteDatabaseConnector mRemoteDB = new RemoteDatabaseConnector();
				mRemoteDB.getPrice(productNumbers, ListViewActivity.this);
				
			}
		});
		
		
		Button shopByStoreButton = (Button) findViewById(R.id.button_by_store);
		shopByStoreButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent  = new Intent(getApplicationContext(),StoreBrowserActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_list_view;
	}

	@Override
	protected int getMenuResourceId() {
		return R.menu.list_view;
	}
	
	public static void priceCalculated(String cost){
		displayPrice("Cost: $" + cost);
	}
	public static void cannotConnect(){
		displayPrice("Could not connect to server");
	}
	

	
	/***
	 * 
	 * @author marcom
	 *
	 */  
	public static class CustomItemBrowserListAdapter extends BaseAdapter{
		private ArrayList<String> items;
		private ArrayList<String> images;
		private Context context;
		LayoutInflater myInflater;
		private int rowLayout;
		private SharedPreferenceManager preferenceManager;

		
		public CustomItemBrowserListAdapter(Context c, ArrayList<String> item, ArrayList<String> image,int i_rowLayout) {
			items = item;
			context = c;
			preferenceManager = new SharedPreferenceManager(context);
			myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowLayout = i_rowLayout;
			images = getIDs(image);
			//layoutResourceId = resourceId;
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		
		public ArrayList<String> getIDs(ArrayList<String> itemIconNames){
			ArrayList<String> itemIDs = new ArrayList<String>();
			for(String s: itemIconNames){
				itemIDs.add(Integer.toString(
						context.getResources()
						.getIdentifier(s, "drawable",  
								context.getPackageName())));
			}
			return itemIDs;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = myInflater.inflate(rowLayout, parent, false);	
				populateView(convertView,position);
				
				
			}else{
				LinearLayout view = (LinearLayout) convertView;
				populateView(view,position);
			}
			return convertView;
		}
		
		/***
		 * Adds the name, images, and 
		 * @param v
		 * @param position
		 */
		public void populateView(View v, final int position){
			TextView name = (TextView) v.findViewById(R.id.itemName);
			ImageButton btn = (ImageButton) v.findViewById(R.id.imageButton1);
			TextView amountTxt = (TextView) v.findViewById(R.id.units_amount);
			
			db.open();
			final Product itemObject = db.getItemObject(itemIDs.get(position), tableName);
			db.close();
			name.setText(items.get(position));
			amountTxt.setText(itemObject.getAmount()+" "+itemObject.getUnits());
			if(Integer.valueOf(itemObject.getCategory()) != -1){
				btn.setImageResource(Integer.parseInt(images.get(position)));
			}
			
			btn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					ListViewActivity lva = new ListViewActivity();
					lva.createCategoryDialog(position);
				}
			});
			ToggleButton fav = (ToggleButton) v.findViewById(R.id.fav);
			fav.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(((ToggleButton) v).isChecked()){
						preferenceManager.SetPreference(itemObject.getName(), true);
						db.open();
						db.insertFavItem(itemObject);
						db.close();
						Toast.makeText(context, "Saved to Favourites", Toast.LENGTH_SHORT).show();
					}else{
						preferenceManager.SetPreference(itemObject.getName(), false);
						db.open();
						db.deleteItem(itemObject.getName(), Constants.FAV_ITEMS_TABLE);
						db.close();
						Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
					}
				}
			});
			fav.setTextOn("");
			fav.setTextOff("");
			if(itemObject != null){
				fav.setChecked(preferenceManager.getPreference(itemObject.getName()));
			}
		}
		
	}
	
	public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

		private List<Integer> images;

		public ArrayAdapterWithIcon(Context context, List<String> items, List<Integer> images) {
		    super(context, android.R.layout.select_dialog_item, items);
		    this.images = images;
		}

		public ArrayAdapterWithIcon(Context context, String[] items, Integer[] images) {
		    super(context, android.R.layout.select_dialog_item, items);
		    this.images = Arrays.asList(images);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		    View view = super.getView(position, convertView, parent);
		    TextView textView = (TextView) view.findViewById(android.R.id.text1);
		    textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), 0, 0, 0);
		    textView.setCompoundDrawablePadding(
		            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
		    return view;
		}

		}
	
}
	

