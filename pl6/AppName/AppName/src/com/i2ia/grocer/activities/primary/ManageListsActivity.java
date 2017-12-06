package com.i2ia.grocer.activities.primary;

import java.util.ArrayList;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.activities.secondary.ListViewActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
/**
 * Activity for managing lists (edit,new,delete)
 * @author Daniel
 *
 */
public class ManageListsActivity extends BaseActivity {
	public ArrayList<String> user_lists;
	private ListView listDisplay;
	protected ActionMode mActionMode;
	Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Populates Listview with users lists
		DBAdapter db = new DBAdapter(getApplicationContext());
		db.open();
		user_lists = db.getAllTables();

		context = this;

		//Display Lists
		listDisplay = (ListView) findViewById(R.id.display_lists);	
		listDisplay.setAdapter(new ArrayAdapter<String>(this, R.layout.manage_lists_item, user_lists));
		listDisplay.setOnItemClickListener(new ListItemClickListener());
		registerForContextMenu(listDisplay);
		
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
	            deleteTable(info.id);
	            return true;
	        case R.id.details:
	        	Intent intnt = new Intent(this, ListViewActivity.class);
	        	intnt.putExtra(Constants.TABLE_TAG, user_lists.get((int) info.id));
				intnt.putExtra(Constants.NEXT_TAG, -1);
				startActivity(intnt);
	        	return true;
	        case R.id.edit:
	        	editName(user_lists.get((int) info.id));
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	private void editName(final String tableName) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle("Rename List");

		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		input.setHint("Enter new Name");
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getEditableText().toString();
		  if(!value.equals("")){
			  DBAdapter db = new DBAdapter(getApplicationContext());
			  db.open();
			  db.renameTable(tableName, value);
			  db.close();
			  
			  ActionBar actBar = getSupportActionBar();
			  actBar.setDisplayHomeAsUpEnabled(true);
			  actBar.setHomeButtonEnabled(true);
			  actBar.setTitle(value);
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
	
	private void deleteTable(long id) {
		DBAdapter db = new DBAdapter(getApplicationContext());
		db.open();
		db.deleteTable(user_lists.get((int) id));
		db.close();
		reload();
	}

	/***
	 * on tap of item on list
	 * @author marcom
	 *
	 */
	private class ListItemClickListener implements ListView.OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			selectItem(position);
		}
		private void selectItem(int position){
			//List item clicked, launches ListViewActivity
			String selectedList = user_lists.get(position);
			Intent intent = new Intent(getApplicationContext(),ListViewActivity.class);
			intent.putExtra(Constants.TABLE_TAG, selectedList);
			intent.putExtra(Constants.NEXT_TAG, -1);
			startActivity(intent);
		}
	}
	
	private void reload(){
		//Populates Listview with users lists
		DBAdapter db =  new DBAdapter(getApplicationContext());
		db.open();
		user_lists = db.getAllTables();
				
		//Remove default tables in database

		//Display Lists
		listDisplay = (ListView) findViewById(R.id.display_lists);	
		listDisplay.setAdapter(new ArrayAdapter<String>(this,R.layout.manage_lists_item,user_lists));
		listDisplay.setOnItemClickListener(new ListItemClickListener());
		registerForContextMenu(listDisplay);
	}
	
	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_list_management;
	}

	@Override
	protected int getMenuResourceId() {
		// TODO Auto-generated method stub
		return R.menu.list_management;
	}

	@Override
	protected String getActivityString() {
		// TODO Auto-generated method stub
		return Constants.MANAGE_ACTIVITY;
	}

}
