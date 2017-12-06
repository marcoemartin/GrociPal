package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
/**
 * Activity for viewing a list in detail
 * @author Daniel
 *
 */
public class ListViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		clickableButtons();

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view, menu);
		return true;
	}
	
	public void clickableButtons(){
		Button addItemsButton = (Button) findViewById(R.id.button_add_items);
		addItemsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),ItemBrowserActivity.class);
				startActivity(intent);
			}
		});
		
		
		Button priceCheckbutton = (Button) findViewById(R.id.button_check_price);
		priceCheckbutton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(getApplicationContext(),PriceCheck.class);
				startActivity(intent);
			}
		});
	}

}
