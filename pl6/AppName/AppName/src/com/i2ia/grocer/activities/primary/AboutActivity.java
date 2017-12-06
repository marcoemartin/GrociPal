package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.Constants;
import com.i2ia.grocer.R;
import com.i2ia.grocer.R.id;
import com.i2ia.grocer.R.layout;
import com.i2ia.grocer.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView copyright =(TextView)findViewById(R.id.copyright);
		TextView website =(TextView)findViewById(R.id.website);
		TextView support =(TextView)findViewById(R.id.support);
		TextView privacy =(TextView)findViewById(R.id.privacy);
		
		copyright.setClickable(true);
		copyright.setMovementMethod(LinkMovementMethod.getInstance());
		String copytext = "<a href='http://www.i2ia.com'> i2i-Advances </a>";
		copyright.setText(Html.fromHtml(copytext));
		
		website.setClickable(true);
		website.setMovementMethod(LinkMovementMethod.getInstance());
		String webtext = "<a href='http://www.grocipal.com'> Website </a>";
		website.setText(Html.fromHtml(webtext));
		
		support.setClickable(true);
		support.setMovementMethod(LinkMovementMethod.getInstance());
		String suptext = "<a href='http://www.grocipal.com/support'> Support</a>";
		support.setText(Html.fromHtml(suptext));
		
		privacy.setClickable(true);
		privacy.setMovementMethod(LinkMovementMethod.getInstance());
		String privtext = "<a href='http://www.grocipal.com/terms-of-use'> Privacy Policy </a>";
		privacy.setText(Html.fromHtml(privtext));
	}

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_about;
	}

	@Override
	protected int getMenuResourceId() {
		// TODO Auto-generated method stub
		return R.menu.about;
	}

	@Override
	protected String getActivityString() {
		// TODO Auto-generated method stub
		return Constants.ABOUT_ACTIVITY;
	}


}
