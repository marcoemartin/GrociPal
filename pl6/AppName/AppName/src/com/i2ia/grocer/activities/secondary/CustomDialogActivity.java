package com.i2ia.grocer.activities.secondary;

import java.util.ArrayList;
import java.util.Arrays;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.R.array;
import com.i2ia.grocer.R.id;
import com.i2ia.grocer.R.layout;
import com.i2ia.grocer.R.menu;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.data.Product;
import com.i2ia.grocer.data.SharedPreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CustomDialogActivity extends Activity {
	private SharedPreferenceManager preferenceManager;
	public String amount;
	public String brand;
	public String unit;
	
	public int position;
	public ArrayList<String> itemNames;
	
	public EditText amountET;
	public EditText brandET;
	public String tableName;
	public Spinner categorySpinner;
	public Spinner amountSpinner;
	
	Button btnSave;
	Button btnCancel;
	Button btnNext;
	CheckBox organicCheckBox;
	
	DBAdapter db;
	String spinnerDefault;
	int unitsArrayID;
	Product product;
	int img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_dialog);
		
		db = new DBAdapter(this);
		preferenceManager = new SharedPreferenceManager(this);
		
		//Get list Name
        Intent intent = getIntent();
		Bundle nameBundle = intent.getExtras();
		product = nameBundle.getParcelable(Constants.ITEM_TAG);
		tableName = nameBundle.getString(Constants.TABLE_TAG);
		position = nameBundle.getInt(Constants.POSITION_TAG);
		itemNames = ListViewActivity.itemNames;
		
		ArrayList<String> nameTemp = new ArrayList<String>();
		nameTemp.add(product.getImageName());
		db.open();
		img = db.getImageIDs(nameTemp).get(0);
		db.close();
		
		TextView name = (TextView) findViewById(R.id.item_name);
		ImageView icon = (ImageView) findViewById(R.id.imageView1);
		name.setText(product.getName());
		icon.setImageResource(img);
		
		if(product.getImageName().equals("")){
			//product is a user created product
			setUpCategory();
		}else{
			//product is one chosen from database
			setTextFields();
			populateAmountSpinner();
			fillTextFields();
		}
		
		btnSave.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				amount = amountET.getText().toString();
				brand = brandET.getText().toString();
				
				//save editTexts to database
				db.open();
				db.updateField(tableName, Constants.USER_TABLE_KEY_AMOUNT, amount, itemNames.get(position));
				db.updateField(tableName, Constants.USER_TABLE_KEY_UNIT, unit, itemNames.get(position));
				db.updateField(tableName, Constants.USER_TABLE_KEY_BRAND, brand, itemNames.get(position));
				db.close();
				
				Toast.makeText(getApplicationContext(), "Details Saved", Toast.LENGTH_SHORT).show();
				
				//go Back to the listView
				Intent intent = new Intent(getBaseContext(), ListViewActivity.class);
				intent.putExtra(Constants.TABLE_TAG, tableName);
				intent.putExtra(Constants.NEXT_TAG, -1);
				startActivity(intent);
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				amount = amountET.getText().toString();
				brand = brandET.getText().toString();
				
				//save editTexts to database
				db.open();
				db.updateField(tableName, Constants.USER_TABLE_KEY_AMOUNT, amount, itemNames.get(position));
				db.updateField(tableName, Constants.USER_TABLE_KEY_UNIT, unit, itemNames.get(position));
				db.updateField(tableName, Constants.USER_TABLE_KEY_BRAND, brand, itemNames.get(position));
				db.close();
				
				Toast.makeText(getApplicationContext(), "Details Saved", Toast.LENGTH_SHORT).show();
				
				//go Back to the listView
				Intent intent = new Intent(getBaseContext(), ListViewActivity.class);
				intent.putExtra(Constants.TABLE_TAG, tableName);
				intent.putExtra(Constants.NEXT_TAG, position+1);
				startActivity(intent);
			}
		});
	}

	private void setUpCategory() {
		categorySpinner = (Spinner) findViewById(R.id.category);
		TextView category = (TextView) findViewById(R.id.textView3);
		btnSave = (Button) findViewById(R.id.btn_ok);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnNext = (Button) findViewById(R.id.next);
		categorySpinner.setVisibility(View.VISIBLE);
		category.setVisibility(View.VISIBLE);
		populateCategorySpinner();
	}

	private void fillTextFields() {
		db.open();
		ArrayList<String> row = db.getItem(itemNames.get(position), tableName);
		db.close();
		
		if(!row.isEmpty()){
			amountET.setText(row.get(4));
			brandET.setText(row.get(6));
			spinnerDefault = row.get(5); 
			
			if(unitsArrayID == R.array.UnitsVolume && spinnerDefault!=null){
				int index = Arrays.asList(getResources().getStringArray(R.array.UnitsVolume)).indexOf(spinnerDefault);
				amountSpinner.setSelection(index);
			}else if(unitsArrayID == R.array.UnitsMass && spinnerDefault!=null){
				int index = Arrays.asList(getResources().getStringArray(R.array.UnitsMass)).indexOf(spinnerDefault);
				amountSpinner.setSelection(index);
			}else if(unitsArrayID == R.array.UnitsQuantity && spinnerDefault!=null){
				int index = Arrays.asList(getResources().getStringArray(R.array.UnitsQuantity)).indexOf(spinnerDefault);
				amountSpinner.setSelection(index);
			}
		}
	}

	/***
	 * According to the category sets the right fields to be displayed
	 */
	private void setTextFields() {
		String[] unitsVolume = getResources().getStringArray(R.array.UnitsVolume);
		String[] unitsMass = getResources().getStringArray(R.array.UnitsMass);
		String[] unitsQuantity = getResources().getStringArray(R.array.UnitsQuantity);
		
		//The items units uses volume
		if(Arrays.asList(unitsVolume).contains(product.getUnits())){
			unitsArrayID = R.array.UnitsVolume;
			
		//The items units uses Mass
		}else if(Arrays.asList(unitsMass).contains(product.getUnits())){
			unitsArrayID = R.array.UnitsMass;
			
		//The items units uses Quantities
		}else if(Arrays.asList(unitsQuantity).contains(product.getUnits())){
			unitsArrayID = R.array.UnitsQuantity;
			
		//The item can be organic
		}if(product.getOrganic().equals(Constants.USES_ORGANIC)){
			organicCheckBox = (CheckBox) findViewById(R.id.organic);
			organicCheckBox.setChecked(preferenceManager.getPreference("Organic"));
			organicCheckBox.setVisibility(View.VISIBLE);
		}
			
		//add buttons and editTexts
		amountET = (EditText) findViewById(R.id.amountEditText);
		brandET = (EditText) findViewById(R.id.BrandEditText);
		btnSave = (Button) findViewById(R.id.btn_ok);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnNext = (Button) findViewById(R.id.next);
		
		amountET.setVisibility(View.VISIBLE);
		brandET.setVisibility(View.VISIBLE);
		
	}

	private void populateAmountSpinner() {
		amountSpinner = (Spinner) findViewById(R.id.amountSpinner);
		amountSpinner.setVisibility(View.VISIBLE);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				unitsArrayID, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		amountSpinner.setAdapter(adapter);
		
		amountSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
	}
	
	private void populateCategorySpinner() {
		categorySpinner = (Spinner) findViewById(R.id.category);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.Categories, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		categorySpinner.setAdapter(adapter);
		
		categorySpinner.setOnItemSelectedListener(new SpinnerCategorySelectedListener());
	}
	
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.organic:
	            if (checked){
	                // add organic to database and preferences
	            	preferenceManager.SetPreference("Organic", true);
	            	db.open();
	            	db.updateField(tableName, Constants.USER_TABLE_KEY_ORGANIC, 
	            					Constants.ORGANIC_CHEKED, itemNames.get(position));
	            	db.close();
	            }else{
	                // Remove organic from database and preferences
	            	preferenceManager.SetPreference("Organic", false);
	            	db.open();
	            	db.updateField(tableName, Constants.USER_TABLE_KEY_ORGANIC, 
	            					Constants.ORGANIC_UNCHECKED, itemNames.get(position));
	            	db.close();
	            }
	            break;
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custom_dialog, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class SpinnerItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        unit = parent.getItemAtPosition(pos).toString();
	    }

	    public void onNothingSelected(AdapterView parent) {
	        // Do nothing.
	    }
	}
	
	public class SpinnerCategorySelectedListener implements OnItemSelectedListener {

		//From the categories select the corresponding image and save it to db
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        String selectedCategory = parent.getItemAtPosition(pos).toString();
	        int index = Arrays.asList(getResources().getStringArray(R.array.Categories)).indexOf(selectedCategory);
	        String iconName = Arrays.asList(getResources().getStringArray(R.array.CategoryIcons)).get(index);
	        
	        //updates the image name and category number 
	        db.open();
	        db.updateField(tableName, Constants.USER_TABLE_KEY_IMG, iconName, itemNames.get(position));
	        db.updateField(tableName, Constants.USER_TABLE_KEY_CATEGORY, 
	        				String.valueOf(index), itemNames.get(position));
	        db.updateField(tableName, Constants.USER_TABLE_KEY_UNIT, updateUnits(index), itemNames.get(position));
	        db.updateField(tableName, Constants.USER_TABLE_KEY_ORGANIC, updateOrganic(index), itemNames.get(position));
	        db.close();
	        setTextFields();
			populateAmountSpinner();
			fillTextFields();
	    }

	    private String updateOrganic(int index) {
	    	String[] usingOrganic = getResources().getStringArray(R.array.UsingOrganic);
	    	String[] categories = getResources().getStringArray(R.array.Categories);
	    	
	    	String organic = "no"; 
	    	
	    	if(Arrays.asList(usingOrganic).contains(categories[index])){
				organic = "yes";
			}
	    	return organic;
		}

		private String updateUnits(int categoryIndx) {
	    	String[] usingVolume = getResources().getStringArray(R.array.UsingVolume);
			String[] usingMass = getResources().getStringArray(R.array.UsingMass);
			String[] usingQuantity = getResources().getStringArray(R.array.UsingQuantity);
			
			String[] categories = getResources().getStringArray(R.array.Categories);
			String units = "Quantity";
			
			//The items units uses volume
			if(Arrays.asList(usingVolume).contains(categories[categoryIndx])){
				units = getResources().getStringArray(R.array.UsingVolume)[0];
				
			//The items units uses Mass
			}else if(Arrays.asList(usingMass).contains(categories[categoryIndx])){
				units = getResources().getStringArray(R.array.UsingVolume)[0];
				
			//The items units uses Quantities
			}else if(Arrays.asList(usingQuantity).contains(categories[categoryIndx])){
				units = getResources().getStringArray(R.array.UsingVolume)[0];
			}
			
			return units;
		}

		public void onNothingSelected(AdapterView parent) {
	        // Do nothing.
	    }
	}
}
