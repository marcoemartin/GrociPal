package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
/**
 * Activity for browsing items to add to a list
 * @author Daniel
 *
 */
public class ItemBrowserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_browser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_browser, menu);
		return true;
	}

}
