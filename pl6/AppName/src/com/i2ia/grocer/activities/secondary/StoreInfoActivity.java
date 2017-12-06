package com.i2ia.grocer.activities.secondary;

import com.i2ia.grocer.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * Activity for displaying information about a specific store
 * @author Daniel
 *
 */
public class StoreInfoActivity extends Activity {
	private String[] storeInfoList;
	private ListView storeListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_info);
		
		storeInfoList = getResources().getStringArray(R.array.StoreInfoArray);
		storeListView = (ListView) findViewById(R.id.store_info_list);	//METHOD CALL	
		storeListView.setAdapter(new ArrayAdapter<String>(this,R.layout.store_list_item,storeInfoList));
			
		storeListView.setOnItemClickListener(new StoreItemClickListener());
		
		
	}
	
	private class StoreItemClickListener implements ListView.OnItemClickListener{
		public void onItemClick(AdapterView parent, View view, int position, long id){
			selectItem(position);
		}
		private void selectItem(int position){
			switch(position) {
		    case 0:
		    	//Hours -  detailed (weekely) hours
		    	break;
		    case 1:
		    	//Directions launch google maps?
		    	break;
		    case 2:
		    	//Flyer - Launch flyer activity - if store has flyer.
		    	Intent intent = new Intent(getApplicationContext(), FlyerActivity.class);
		    	startActivity(intent);
		    	break;
		    case 3:
		    	//Phone Number - dials number in users phone

		    case 4:
		    	//Website - Launch website
		    	
		    case 5:
		    	//Add to favourites - Add to users favourites
		    	

		    default:
			}
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_info, menu);
		return true;
	}
	
	

}
