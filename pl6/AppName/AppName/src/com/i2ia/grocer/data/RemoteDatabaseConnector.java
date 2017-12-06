package com.i2ia.grocer.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.i2ia.grocer.activities.primary.HomeActivity;
import com.i2ia.grocer.activities.secondary.ItemBrowserActivity;
import com.i2ia.grocer.activities.secondary.ListViewActivity;

/**
 * Manages connections to servers database of items
 * @author Daniel
 *
 */
public class RemoteDatabaseConnector {
	String priceQuery = "SELECT price FROM ExampleStore WHERE productnum";
	
	/**
	 * Tester method to connect and retrieve data from remoteDatabase
	 */
	public void getAllItems(){
		
		
		AccessItems accItem = new AccessItems();
		accItem.execute();
		
	}
	
	/**
	 * Gets price given list of product numbers
	 * @param productNumbers
	 * @return
	 */
	public void getPrice(ArrayList<Integer> productNumbers, ListViewActivity activity){
		
		//Build query string
		for(int i = 0;i < productNumbers.size();i++){
			if((i == (productNumbers.size() - 1))){
				//Last item to add
				priceQuery = priceQuery + " = " + productNumbers.get(i);
			}else{
				priceQuery = priceQuery + " = " + productNumbers.get(i) + " or productnum";
			}
		}
		
		PriceChecker priceCheck = new PriceChecker(priceQuery, activity);
		priceCheck.execute();
	}
	
	/**
	 * Gets price of a product given its product number
	 * @param productNumbers
	 * @return
	 */
	public void getPrice(int productNumber, HomeActivity activity){
		priceQuery = priceQuery+" = "+productNumber;
		PriceChecker priceCheck = new PriceChecker(priceQuery, activity);
		priceCheck.execute();
	}
	
	/**
	 * Given list of items retrieves price information from remoteDB
	 */
	class PriceChecker extends AsyncTask<String, String, String>{
		private String queryString;
		private Activity activity;
		private int timeoutConn = 4000;
		
		public PriceChecker(String i_queryString,ListViewActivity listViewAct){
			queryString = i_queryString;
			this.activity = listViewAct;
		}
		
		public PriceChecker(String i_queryString,HomeActivity listViewAct){
			queryString = i_queryString;
			this.activity = listViewAct;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "http://192.168.1.126/webservice/getPrice.php";
			InputStream isr = null;
			String result = null;
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Query",queryString));
			
			
			try{
				//Connection time out
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConn);
				
				HttpClient httpClient = new DefaultHttpClient(httpParams);
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpClient.execute(httpPost);
				
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();
				Log.d("ISR",isr.toString());
				Log.d("PASS1","connection success ");
			}catch(Exception e){
			 Log.d("ErrorDB","Could not connect to database");	
			 
			}
			
			//Convert InputStream string of cost
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				
				while((line = reader.readLine()) != null){
					sb.append(line + "\n");
					}
				isr.close();
				result = sb.toString();
				
				Log.d("SB",sb.toString());
				Log.d("pass2", "Connection Successful");
			}catch(Exception e){
				Log.d("Fail2",e.toString());
			}
			
			
			
			return result;
		}
		protected void onPostExecute(String cost){
			if(activity instanceof ListViewActivity){
				if(cost == null){
					 ((ListViewActivity) activity).cannotConnect();
				}else{
					((ListViewActivity) activity).priceCalculated(cost);
				}
			}else if(activity instanceof HomeActivity){
				if(cost == null){
					 ((HomeActivity) activity).cannotConnect();
				}else{
					((HomeActivity) activity).priceCalculated(cost);
				}
			}
		}
	}
	/**
	 * Querys remote db and gets back JSON OBJECT
	 * @author Daniel
	 *
	 */
	class AccessItems extends AsyncTask<String,String, JSONArray>{
		private String name;
		private JSONObject result;
		protected void onPreExecute(){
			
		}
		@Override
		protected JSONArray doInBackground(String... params) {	
			//Location of server, will change
			String serverIP = "http://192.168.1.126/";
			//serverIP = "192.168.43.26";
			String url = "http://" + serverIP+ "/webservice/getItems.php";
			JSONObject result = null;
			InputStream isr = null;
			String productNumber = "1";
			JSONArray resultArray = null;
			
			//List of name value pairs that will be passed as POST variables to .php code
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("ProductNumber",productNumber));
			
			//Send namevalue pairs to server
			//get response as InputStream
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();
				Log.d("ISR",isr.toString());
				Log.d("PASS1","connection success on JSON");
			}catch(Exception e){
			 Log.d("ErrorDB","Could not connect to database");	
			 
			}
			
			//Convert InputStream to JSONObject
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line = reader.readLine()) != null){
					sb.append(line);
					}
				isr.close();
				
				resultArray = new JSONArray(sb.toString());
				//Log.d("RESULT ARRAY",resultArray.toString());
				
				
			}catch(Exception e){
				Log.d("Fail2",e.toString());
			}
			
			//Return JSONObject result
			return resultArray;	
		}
		
		protected void onPostExecute(JSONArray resultArray){
			ArrayList<Product> resultProducts = new ArrayList<Product>();
			for(int i=0; i<resultArray.length(); i++){
				try{
					JSONObject Jobj = (JSONObject) resultArray.get(i);
					resultProducts.add(new Product(Jobj.getString("name"),Jobj.getInt("productnum"),
							Jobj.getString("category"),"nearby_icon", Jobj.getInt("amount"), 
							Jobj.getString("units"), Jobj.getString("brand"), Jobj.getString("organic")));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			//Call method in ItemBrowser to populate listview
			//Log.d("RESULTARRAYTAG",resultProducts.toString());
			ItemBrowserActivity.itemsDownloaded(resultProducts);
			
		}
		
		
		
		
	}
	

}
