package com.i2ia.grocer.activities.secondary;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.activities.primary.ManageListsActivity;
import com.i2ia.grocer.data.DBAdapter;
import com.i2ia.grocer.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListAdapter extends BaseAdapter{
	private ArrayList<String> items;
	private ArrayList<String> images;
	private int layoutResourceId;
	private Context context;
	LayoutInflater myInflater;
	DBAdapter db;
	
	
	private int rowLayout;
	private ArrayList<Integer> imageIDs = new ArrayList<Integer>();

	public CustomListAdapter(Context c, ArrayList<String> item, ArrayList<String> image,int i_rowLayout) {
		items = item;
		context = c;
		myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowLayout = i_rowLayout;
		images = getIDs(image);
		//layoutResourceId = resourceId;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null){
			convertView = myInflater.inflate(rowLayout, parent, false);
			TextView item = (TextView) convertView.findViewById(R.id.txt);
			ImageView img = (ImageView) convertView.findViewById(R.id.img);
			
			item.setText(items.get(position));
			img.setImageResource(Integer.parseInt(images.get(position)));
			
			Button description = (Button) convertView.findViewById(R.id.favorite);
			description.setTag(position);
			//description.setTag(position);
			description.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					db = new DBAdapter(context);
					db.open();
					ArrayList<String> row= db.getItem(items.get((Integer) v.getTag()+1), Constants.PRODUCTS_TABLE);
					db.insertItem(Constants.FAV_ITEMS_TABLE, row.get(1), row.get(2), row.get(3), row.get(4),
									Integer.parseInt(row.get(5)), row.get(6), row.get(7), row.get(8));
					db.close();
				}
			});
			
//			convertView.setOnClickListener(new OnClickListener() {
//
//		        @Override
//		        public void onClick(View v) {
//
//		        	Toast.makeText(context.getApplicationContext(), "Pressed bar", Toast.LENGTH_SHORT).show();
//		        }
//		    });
		}
		return convertView;
	}
	
	public ArrayList<String> getIDs(ArrayList<String> storeIconNames){
		ArrayList<String> storeIDs = new ArrayList<String>();
		for(String s: storeIconNames){
			storeIDs.add(Integer.toString(
					context.getResources()
					.getIdentifier(s, "drawable", 
							context.getPackageName())));
		}
		return storeIDs;
		
	}

	/**
	 * Dialog box that shows a description of product and description(if exists)
	 */
	public void favorite(){
		//AlertDialog OptionDialog = new AlertDialog.Builder(context).create();
		//OptionDialog.setContentView(R.layout.custom_dialog_box);
//		d = new Dialog(context);
//		d.setContentView(R.layout.custom_dialog_box);
//		d.show();
		
	}
	
//	public void dialogButtons(View v){
//		switch(v.getId()){
//			case R.id.ok_button:
//				d.dismiss();
//			case R.id.fav_button:
//				//fav
//			default:
//				break;
//		}
//	}
	
	
}

