package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.secondary.ListViewActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
/**
 * Activity for managing lists (edit,new,delete)
 * @author Daniel
 *
 */
public class ManageListsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Temporary button stand in for list object
		Button listButton = (Button) findViewById(R.id.button_list);
		listButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),ListViewActivity.class);
				startActivity(intent);
			}
		});
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
		return "ManageListsActivity";
	}



}
