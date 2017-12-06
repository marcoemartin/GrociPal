package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.data.DBAdapter;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.method.KeyListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity for creating a new list or editing one
 * 
 * @author Daniel
 * 
 */
public class NewEditList extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//this.setTitle("List name: ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_edit_list);
//		ActionBar actBar = getSupportActionBar();
//		actBar.setDisplayHomeAsUpEnabled(true);
//		actBar.setHomeButtonEnabled(true);

		//Replace return button with done button
		EditText listName = (EditText) findViewById(R.id.editText_name);

		listName.setOnKeyListener(new View.OnKeyListener() 
	    {
	        @Override
	        public boolean onKey(View v, int keyCode, KeyEvent event) 
	        {
	            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
	            {
	                saveList();
	            }
	            return false;
	        }
	    });
		
		onClickButtons();
		
	}
	
	
	public void saveList(){
		String listNameString = ((EditText) findViewById(R.id.editText_name)).getText().toString();
		
		//Checks validity of name
		if(nameValid(listNameString)){
			DBAdapter db =  new DBAdapter(getApplicationContext());
			db.open();
		
			if(!db.addProductTable(listNameString)){ 
				//New table unsuccessful
				Toast.makeText(getApplicationContext(), "List of same name exists", Toast.LENGTH_SHORT).show();
				db.close();
				
			}else{
				//Table successfully added
				db.close();
				Intent intent = new Intent(getApplicationContext(),ListViewActivity.class);
				intent.putExtra(Constants.TABLE_TAG,listNameString);
				intent.putExtra(Constants.NEXT_TAG, -1);
				startActivity(intent);
				}			
		}else{
			Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_SHORT).show();
			}
		
	}
	
	public void onClickButtons(){
		Button saveButton = (Button) findViewById(R.id.button_save);
		saveButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				saveList();
				}
			});
	}
	
	/**
	 * Determine if an entered name is valid
	 * @param name
	 * @return
	 */
	public boolean nameValid(String name){
//		if(!(name.trim().length() > 0) || name.contains(" ")){
//			return false;
//		}
		return true;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.new_edit_list, menu);
//		return true;
//	}


}
