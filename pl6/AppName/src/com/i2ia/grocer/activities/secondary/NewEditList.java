package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
/**
 * Activity for creating a new list or editing one
 * @author Daniel
 *
 */
public class NewEditList extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_edit_list);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_edit_list, menu);
		return true;
	}




}
