package com.seanxie.remotecontroller_mark0;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class PPTControllerActivity extends Activity{
	private ImageButton pptQuitButton;
	private ImageButton pptLastSlideButton;
	private ImageButton pptNextSlideButton;
	private ImageButton pptPlayButton;
	

	@Override 
	public void onCreate(Bundle savedInstanceState) {  
    	super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_pptcontroller);
	    setTitle("PPT Controller");
	    
	    pptQuitButton = (ImageButton)findViewById(R.id.pptQuitButton);
	    pptLastSlideButton = (ImageButton)findViewById(R.id.pptLastSlideButton);
		pptNextSlideButton = (ImageButton)findViewById(R.id.pptNextSlideButton);
		pptPlayButton = (ImageButton)findViewById(R.id.pptPlayButton);
		
		pptQuitButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(PPTControllerActivity.this, "p,q");
			}
		});	    
	    
		pptLastSlideButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(PPTControllerActivity.this, "p,l");
			}
		});	
		
		pptNextSlideButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(PPTControllerActivity.this, "p,n");
			}
		});	
		
		pptPlayButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(PPTControllerActivity.this, "p,p");
			}
		});	
	    
	 // Show the Up button in the action bar.
        setupActionBar();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("WrongViewCast")
	public void returnToMain() {
		Intent intent = new Intent(this, KeyBindingActivity.class);
		startActivity(intent);
	}
}