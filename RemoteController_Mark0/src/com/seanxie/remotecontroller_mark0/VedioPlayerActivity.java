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

public class VedioPlayerActivity extends Activity{
	
	private ImageButton vpVolumeDownButton;
	private ImageButton vpVolumeUpButton;
	private ImageButton vpMuteButton;
	private ImageButton vpPlayLastButton;
	private ImageButton vpPlayButton;
	private ImageButton vpPlayNextButton;
	private ImageButton vpFastRewindButton;
	private ImageButton vpQuitButton;
	private ImageButton vpFastForwardButton;
	
	private final int PLAYING = 1;
	private final int PAUSING = -1;
	private int playingOrPausing = PAUSING;
	
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {  
    	super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_videoplayer);
	    setTitle("Video Player");
	    
	    vpVolumeDownButton = (ImageButton)findViewById(R.id.vpVolumeDownButton);
		vpVolumeUpButton = (ImageButton)findViewById(R.id.vpVolumeUpButton);
		vpMuteButton = (ImageButton)findViewById(R.id.vpMuteButton);
		vpPlayLastButton = (ImageButton)findViewById(R.id.vpPlayLastButton);
		vpPlayButton = (ImageButton)findViewById(R.id.vpPlayButton);
		vpPlayNextButton = (ImageButton)findViewById(R.id.vpPlayNextButton);
		vpFastRewindButton = (ImageButton)findViewById(R.id.vpFastRewindButton);
		vpQuitButton = (ImageButton)findViewById(R.id.vpQuitButton);
		vpFastForwardButton = (ImageButton)findViewById(R.id.vpFastForwardButton);
	    
		vpVolumeDownButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,d");
			}
		});
	    
		vpVolumeUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,u");
			}
		});
		
		vpMuteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,m");
			}
		});
		
		vpPlayLastButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,l");
			}
		});
		
		vpPlayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (playingOrPausing == PLAYING){
					vpPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.vpplay));
				} else if (playingOrPausing == PAUSING){
					vpPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.vppause));
				}
				playingOrPausing *= -1;
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,p");
			}
		});
		
		vpPlayNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,n");
			}
		});
		
		vpFastRewindButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,r");
			}
		});
		
		vpQuitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,q");
			}
		});
		
		vpFastForwardButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(VedioPlayerActivity.this, "v,f");
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