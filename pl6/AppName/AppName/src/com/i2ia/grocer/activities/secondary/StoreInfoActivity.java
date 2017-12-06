package com.i2ia.grocer.activities.secondary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.activities.primary.FavouritesActivity;
import com.i2ia.grocer.activities.primary.SettingsActivity;
import com.i2ia.grocer.activities.primary.StoreBrowserActivity;
import com.i2ia.grocer.data.GPSAccess;
import com.i2ia.grocer.data.SharedPreferenceManager;
import com.i2ia.grocer.data.Store;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * Activity for displaying information about a specific store
 * @author Daniel
 *
 */
public class StoreInfoActivity extends SecondaryBaseActivity implements OnItemSelectedListener{	
	GoogleMap map;
	private String storeIdentifier;
	public static final String API_KEY = "AIzaSyDCbUIJNl5mAP46OO1xiH3PP19QNv98ZyM";
	public SharedPreferenceManager preferenceManager;
	private Geocoder geoCoder;
	private LatLng userLoc;
	private Marker userMarker = null;
	private ArrayList<Marker> storeMarkers = new ArrayList<Marker>();
	private Spinner spinner;
	
	//private List<String> storeVariations = new ArrayList<String>();
	private ArrayList<String> validStores;
	private boolean generalSearch = false;
	private String searchString = "";
	private String userLocString = "" ;	
	private String searchQuery = "";
	private HashMap<Marker,JSONObject> storeMarkerList = new HashMap<Marker,JSONObject>();
	
	private String streetAddress = null;
	private boolean favouriteStore = false;
	private String storeID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get store name - user requested specific brand
		storeIdentifier = getIntent().getExtras().getString(Constants.STORE_NAME);
		searchString = storeIdentifier;
		
		try{
			streetAddress = getIntent().getExtras().getString(Constants.FAV_STORE_KEY_STREETADDRESS);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(streetAddress != null){
			//User is coming from favorites	
			storeID = getIntent().getExtras().getString(Constants.FAV_STORE_KEY_ID);
			favouriteStore = true;
			
		}
			
		//User has selected "stores nearby"
		if(storeIdentifier.equals(Constants.nearbyStores)){
			searchString="";
			validStores = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.valid_stores)));
			generalSearch = true;
		}
		
		//Initialize geoCoder
		geoCoder = new Geocoder(this);
		
		//Initialize preferenceManager
		preferenceManager = new SharedPreferenceManager(this);
		
		//Google maps fragment management
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setOnInfoWindowClickListener(new WindowListener());
		//add spinner adapter
		fillLayoutViews();
		//Calls onItemSelectionListener -> calls populateMap()
		
		//default address not available warning
		if(preferenceManager.getAddress() == null){
			homeAddressWarning();
		}
	}
	
	private void homeAddressWarning(){
		AlertDialog.Builder builder = new AlertDialog.Builder(StoreInfoActivity.this);
		builder.setTitle("Set Home Address");
		builder.setMessage("The default home address has not been set up.\nUpdate location now?")
        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Intent intent =  new Intent(StoreInfoActivity.this,SettingsActivity.class);
				startActivity(intent);
            }
        })
        .setNegativeButton(R.string.ignore_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Toast.makeText(StoreInfoActivity.this, "Modify address in Settings to set up home address.", Toast.LENGTH_LONG).show();
            }
        });
		// Create the AlertDialog object and return it
		builder.create().show();
	}
	
	/**
	 * Gets stores and adds them to the map
	 */
	public void populateMap(){
		class getPlaces extends AsyncTask<String, Void, JSONArray>{
			private String searchTerm;
			
			public getPlaces(String i_searchTerm){
				this.searchTerm = i_searchTerm;
			}
			@Override
			/**
			 * Get the store results using google places
			 */
			protected JSONArray doInBackground(String... params) {
				searchQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
						+ "location=" + userLocString
						+ "&radius=9000"
						+ "&sensor=true"
						+ "&name=" + searchTerm
						+ "&key=" + API_KEY;
				
				if(generalSearch){
					searchQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
							+ "location=" + userLocString
							+ "&radius=5000"
							+ "&sensor=true"
							+ "&name=" + searchTerm
							+ "&key=" + API_KEY;
				}
				
				if(favouriteStore){
					Log.d("TAG",searchTerm);
					searchQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
							+ "location=" + userLocString
							+ "&radius=100000"
							+ "&sensor=true"
							+ "&keyword=" +  searchTerm
							+ "&key=" + API_KEY;
					
					
					//+ "&keyword=" + searchTerm
					
				}
				
				
				
				StringBuilder placesBuilder = new StringBuilder();

				try{
					//get places search result
					HttpClient placesClient = new DefaultHttpClient();
					HttpGet placesGet = new HttpGet(searchQuery);
					HttpResponse placesResponse = placesClient.execute(placesGet);
					StatusLine placeSearchStatus = placesResponse.getStatusLine();
					
					//If successful
					if (placeSearchStatus.getStatusCode() == 200) {
						
						HttpEntity placesEntity = placesResponse.getEntity();
						InputStream placesContent = placesEntity.getContent();
						InputStreamReader placesInput = new InputStreamReader(placesContent);
						BufferedReader placesReader = new BufferedReader(placesInput);
						String lineIn;
						
						while ((lineIn = placesReader.readLine()) != null) {
						    placesBuilder.append(lineIn);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				//Initialize jsob objects and array - results will be cast here
				JSONArray resultArray = new JSONArray();
				
				try {
					//Cast result to jsonarray
					resultArray = new JSONObject(placesBuilder.toString()).getJSONArray("results");;

				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultArray;
			}

			/**
			 * Filter google places results and add stores to map
			 */
			protected void onPostExecute(JSONArray resultArray){
				Log.d("RESULT ARRAY",resultArray.toString());
				//Add user marker to map
				userMarker = map.addMarker(new MarkerOptions().title("Your location").position(userLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				//Focus camera on user location
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 12.0f));
				
				ArrayList<JSONObject> filteredArray = new ArrayList<JSONObject>();
				
				//Metro filtering
				if(storeIdentifier == "Metro"){
					ArrayList<String> metroVar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.metro_variations)));
					for(int i = 0;i<resultArray.length();i++){
						try{
							if(metroVar.contains(resultArray.getJSONObject(i).getString("name"))){
								filteredArray.add(resultArray.getJSONObject(i));
							}else{
								//Do not add
							}
						}catch(Exception e){	
							}
					}
				}
				//T&T filtering
				if(storeIdentifier == "T&T"){
					for(int i = 0;i<resultArray.length();i++){
						try{
							if(resultArray.getJSONObject(i).getString("name") == "T&T Supermarket Inc"){
								filteredArray.add(resultArray.getJSONObject(i));
							}else{
								//Do not add
							}
						}catch(Exception e){	
							}
					}
				}
				//Nearby search filtering
				if(generalSearch){
					for(int i = 0;i<resultArray.length();i++){
						try{
							for(String validName: validStores){
								if(((String) resultArray.getJSONObject(i).getString("name")).toLowerCase().contains(validName.toLowerCase())){
									filteredArray.add(resultArray.getJSONObject(i));
								}else{
									//Do not add
								}
							}
						}catch(Exception e){	
							}
					}
				}
				if(favouriteStore){
					for(int i = 0;i<resultArray.length();i++){
						try{
							if(resultArray.getJSONObject(i).getString("place_id") == storeID){
								filteredArray.add(resultArray.getJSONObject(i));
							}
						}catch(Exception e){	
							}
					}
				}
				
				//Any other store - no filtering needed
				if(!generalSearch && storeIdentifier != "Metro"&&storeIdentifier != "T&T"){
					for(int i = 0;i<resultArray.length();i++){
						try {
							filteredArray.add(resultArray.getJSONObject(i));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				//Plot filtered stores on the map
				plotStores(filteredArray);

			}
			
			/**
			 * Plot the given stores to the map
			 * @param stores
			 */
			protected void plotStores(ArrayList<JSONObject> stores){
				for(int i = 0;i<stores.size();i++){
					LatLng JobjLatLng;
						try {
							Log.d("TEST",stores.get(i).toString());
							JSONObject Jobj = stores.get(i);
							
							JobjLatLng = new LatLng(Jobj.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
									Jobj.getJSONObject("geometry").getJSONObject("location").getDouble(("lng")));
							
							boolean openHours = Jobj.getJSONObject(("opening_hours")).getBoolean("open_now");
							String hourString = "Hours: ";
							
							if(openHours){
								hourString = hourString + "open";
							}else{
								hourString = hourString + "closed";
							}
							storeMarkerList.put(map.addMarker(new MarkerOptions().position(JobjLatLng).title(Jobj.getString("name")).snippet(hourString)), Jobj);
							
							if(favouriteStore){
								map.moveCamera(CameraUpdateFactory.newLatLngZoom(JobjLatLng, 10.0f));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}	
				}
				
			}
		}
		
		
		//Get needed strings
		userLocString = Double.toString(userLoc.latitude) + "," + Double.toString(userLoc.longitude);
		
		//Filter out spaces from searchterm
		if(searchString.contains(" ")){
			searchString = searchString.replace(" ", "%22");
		}
		
		//If user selects stores nearby
		if(generalSearch){
			for(String store: validStores){
				searchString = store;
				if(searchString.contains(" ")){
					searchString = searchString.replace(" ", "%22");
				}
				Log.d("LOOPY",searchString);

				new getPlaces(searchString).execute();
			}
			
		//If user selects specific brand of store
		}else{
			new getPlaces(searchString).execute();

		}
	}
	
	/**
	 * Fill layout view, spinner etc.
	 */
	public void fillLayoutViews(){
		//Fill and create spinner
		spinner = (Spinner) findViewById(R.id.location_dropdown);
		int locationOptions;
		int indx;
		
		if(preferenceManager.getAddress() != null){
			locationOptions = R.array.gpsHome;
			indx = 1;
		}
		else{
			locationOptions = R.array.gps;
			indx=0;
		}
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,locationOptions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_row);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		spinner.setSelection(indx);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//
		int id = item.getItemId();
		
		if(id == android.R.id.home){
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Return bottom left and top right corners for a search area square
	 * @param userLoc
	 * @return
	 */
	public LatLng[] getSearchArea(LatLng userLoc){
		LatLng[] corners = new LatLng[2];
		//Bottom left
		corners[0] = new LatLng(userLoc.latitude + Constants.BOTTOM_LEFT_X,userLoc.longitude + Constants.BOTTOM_LEFT_Y);	
		//Top right
		corners[1] = new LatLng(userLoc.latitude + Constants.TOP_RIGHT_X,userLoc.longitude + Constants.TOP_RIGHT_Y);
		return corners;
	}


	@Override
	/**
	 * Manage selections of the spinner
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {		
		//Remove old markers
		if(!storeMarkers.isEmpty()){
			for(Marker storeMarker: storeMarkers){
				storeMarker.remove();
				storeMarkers = new ArrayList<Marker>();
			}
			}
		
		//Remove old userMarker
		if(userMarker != null){
			userMarker.remove();
		}

		if(position == 0){
			Log.d("POSITION","0");
			//get GPS location
			GPSAccess gpsAccessor = new GPSAccess(this);
			userLoc = gpsAccessor.getLocation();
			userLocString = Double.toString(userLoc.latitude) + "," + Double.toString(userLoc.longitude);
			populateMap();

		}
		if(position == 1){
			Log.d("POSITION","1");
			//get user Home marker

			SharedPreferenceManager prefManager = new SharedPreferenceManager(this);
			String address = prefManager.getAddress();
			Address geoAddress;
			try {
				List<Address> resultAdresses = geoCoder.getFromLocationName(address, 1);
				if(resultAdresses.size() != 0){
					geoAddress = resultAdresses .get(0);
					userLoc = new LatLng(geoAddress.getLatitude(),geoAddress.getLongitude());
					userLocString = Double.toString(userLoc.latitude) + "," + Double.toString(userLoc.longitude);
					populateMap();
				}else{
					Intent mapFailed =  new Intent(getApplicationContext(),StoreBrowserActivity.class);
					mapFailed.putExtra(Constants.MAP_FAIL_TAG,"fail");
					mapFailed.putExtra(Constants.ACTIVATE_BUTTON_TAG, "false");
					startActivity(mapFailed);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.activity_store_info_alt;
	}

	@Override
	protected int getMenuResourceId() {
		return R.menu.store_info;
	}
	
	/**
	 * Manage clicks on store info windows	
	 * @author Daniel
	 *
	 */
	private class WindowListener implements OnInfoWindowClickListener{
		@Override
		public void onInfoWindowClick(Marker selectedMarker) {
			JSONObject storeJSON = storeMarkerList.get(selectedMarker);
			Intent intent =  new Intent(getApplicationContext(), StoreInfoDialog.class);
			intent.putExtra("store",storeJSON.toString());
			startActivity(intent);	
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
