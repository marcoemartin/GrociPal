package com.i2ia.grocer.data;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
/**
 * Used to get location information from GPS or network towers
 * @author Daniel
 *
 */
public class GPSAccess implements LocationListener{
	Context context;
	LocationManager locationManager;
	boolean gpsStatus = false;
	boolean networkStatus = false;
	
	/**
	 * Determine if the gps is on, and if there is a network connection
	 * @param i_context
	 */
	public GPSAccess(Context i_context){
		context = i_context;
		locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		
		//Determine gps & network status
		gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		networkStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if(gpsStatus){
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,100,this);

		}
		if(networkStatus){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 100, this);
		}
	}
	@Override
	public void onLocationChanged(Location arg0) {

	}
	
	/**
	 * Return users location from GPS or network
	 * @return
	 */
	public LatLng getLocation(){
		Location loc = null;
		LatLng latLngLoc = null;
		
		try{
			if(networkStatus){
				if(locationManager != null){
					loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				if(loc !=null){
					latLngLoc = new LatLng(loc.getLatitude(),loc.getLongitude());
				}
				
			}
			
			if(gpsStatus){
				if(locationManager != null){
					loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
				if(loc !=null){
					latLngLoc = new LatLng(loc.getLatitude(),loc.getLongitude());
				}
				
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return latLngLoc;	
	}
	

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		private class MyLocationListener implements LocationListener{

			@Override
			public void onLocationChanged(Location location) {
				location.getLatitude();
				location.getLongitude();
				}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
				}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
				}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
				}
			}
			
		private class MyGPSListener implements GpsStatus.Listener{
			//http://stackoverflow.com/qu estions/2021176/how-can-i-check-the-current-status-of-the-gps-receiver
			@Override
			public void onGpsStatusChanged(int event) {
				
				
				}
			
			}
		
			
	}
	

