package com.i2ia.grocer.activities.primary;

import java.util.Arrays;
import java.util.List;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.data.SharedPreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Activity for managing user settings
 * @author Daniel
 *
 */
public class SettingsActivity extends BaseActivity {
	private SharedPreferenceManager settingsManager;
	private LinearLayout locationBtn = null;
	private LinearLayout notif = null;
	private LinearLayout travel = null;
	org.jraf.android.backport.switchwidget.Switch switchOne, switchTwo;
	Context context;
	String newAddress;
	TextView address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
        
		//Get Toggle buttons
		locationBtn = (LinearLayout) findViewById(R.id.location_btn);
		notif = (LinearLayout) findViewById(R.id.notif);
		travel = (LinearLayout) findViewById(R.id.travel);
		switchOne = (org.jraf.android.backport.switchwidget.Switch) findViewById(R.id.switch1);
		switchTwo = (org.jraf.android.backport.switchwidget.Switch) findViewById(R.id.switch2);
		
		//Get current state of toggle buttons
		settingsManager = new SharedPreferenceManager(this);
		boolean notif_on = settingsManager.getPreference(Constants.NOTIF_KEY);
		boolean travel_on = settingsManager.getPreference(Constants.TRAVEL_KEY);	
		
		//Apply saved state of toggle buttons
		switchOne.setChecked(notif_on);
		switchTwo.setChecked(travel_on);
		
		//Add click listeners
		toggleButtons();
		
		address = (TextView) findViewById(R.id.address);
		address.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		if(settingsManager.getAddress() != null){
			address.setText(settingsManager.getAddress());
		}
		
		locationBtn.setOnClickListener(new LinearLayout.OnClickListener(){

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
	            alert.setTitle("Default Location"); //Set Alert dialog title here
	            alert.setMessage("Enter desired location"); //Message here
	 
	            LinearLayout layout = new LinearLayout(context);
	            layout.setOrientation(LinearLayout.VERTICAL);
	            
	            // Set an EditText view to get user input 
	            final EditText street = new EditText(context);
	            street.setHint("Street Name (i.e 1 Main St.)");
	            layout.addView(street);
	            
	            final EditText city = new EditText(context);
	            city.setHint("City");
	            layout.addView(city);
	            
	            final EditText province = new EditText(context);
	            province.setHint("Province/State");
	            layout.addView(province);
	            
	            final EditText postalCode = new EditText(context);
	            postalCode.setHint("Postal Code");
	            layout.addView(postalCode);
	            
	            String currAddress = settingsManager.getAddress();
	            if(currAddress != null){
	            	List<String> splitAddress = Arrays.asList(currAddress.split(","));
	            	
	            	if(!splitAddress.get(0).equals("") && splitAddress.size()>2){
		            	street.setText(splitAddress.get(0));
		            }if(splitAddress.size()>1 && !splitAddress.get(1).equals("")){
		            	city.setText(splitAddress.get(1));
		            }if(splitAddress.size()>2 && !splitAddress.get(2).equals("")){
		            	province.setText(splitAddress.get(2));
		            }if(splitAddress.size()>3 && !splitAddress.get(3).equals("")){
		            	postalCode.setText(splitAddress.get(3));
		            }if(splitAddress.size()==1){
		            	postalCode.setText(splitAddress.get(0));
		            }
	            }
	            
	            alert.setView(layout);
	 
	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	             //You will get as string input data in this variable.
	             // here we convert the input to a string and show in a toast.
	             String strt = street.getEditableText().toString();
	             String cty = city.getEditableText().toString();
	             String prvnc = province.getEditableText().toString();
	             String postal = postalCode.getEditableText().toString();
	             
	             if(!strt.equals("") &&  !cty.equals("") && !prvnc.equals("")){
	            	 if(!postal.equals("")){
	            		 newAddress = strt+", "+cty+", "+prvnc+", "+postal;    
	            	 }else{
	            		 newAddress = strt+", "+cty+", "+prvnc;
	            	 }
		             
		             address.setText(newAddress);
	             }else if(!postal.equals("")){
	            	 newAddress = postal;
	            	 address.setText(newAddress);
	             }else{
	            	 Toast.makeText(context, "Please fill out all text boxes", Toast.LENGTH_LONG).show();
	            	 newAddress = null;
	             }
	            } // End of onClick(DialogInterface dialog, int whichButton)
	        }); //End of alert.setPositiveButton
	            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int whichButton) {
	                // Canceled.
	                  dialog.cancel();
	              }
	        }); //End of alert.setNegativeButton
	            AlertDialog alertDialog = alert.create();
	            alertDialog.show();
			}
			
		});
		
		notif.setOnClickListener(new LinearLayout.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(settingsManager.getPreference(Constants.NOTIF_KEY)){
					//Turn notification freature ON
					settingsManager.SetPreference(Constants.NOTIF_KEY, true);
					
					}
				else{
					settingsManager.SetPreference(Constants.NOTIF_KEY, false);
				}
			}
		});

		travel.setOnClickListener(new LinearLayout.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(settingsManager.getPreference(Constants.TRAVEL_KEY)){
					settingsManager.SetPreference(Constants.TRAVEL_KEY, true);
					
					}
				else{
					settingsManager.SetPreference(Constants.TRAVEL_KEY, false);
				}	
			}
		});
	}
	
	@Override
	public void onPause(){
		if(newAddress != null){
			settingsManager.saveAddress(newAddress);
		}
		super.onPause();

	}
	
	/**
	 * Manages onClick behavior of toggle buttons
	 */
	public void toggleButtons(){
		//final EditText address = (EditText) findViewById(R.id.enter_address);
		switchOne.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(((org.jraf.android.backport.switchwidget.Switch) v).isChecked()){
					//Turn notification freature ON
					settingsManager.SetPreference(Constants.NOTIF_KEY, true);
					
					}
				else{
					settingsManager.SetPreference(Constants.NOTIF_KEY, false);
				}
			}
			
		});
		
		switchTwo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((org.jraf.android.backport.switchwidget.Switch) v).isChecked()){
					settingsManager.SetPreference(Constants.TRAVEL_KEY, true);
					
					}
				else{
					settingsManager.SetPreference(Constants.TRAVEL_KEY, false);
				}	
			}
		});
		
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_settings;
	}

	@Override
	protected int getMenuResourceId() {
		return R.menu.settings;
	}

	@Override
	protected String getActivityString() {
		// TODO Auto-generated method stub
		return Constants.SETTINGS_ACTIVITY;
	}



}
