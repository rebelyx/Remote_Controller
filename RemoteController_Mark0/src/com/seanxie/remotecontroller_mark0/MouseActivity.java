package com.seanxie.remotecontroller_mark0;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MotionEventCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MouseActivity extends Activity{
	
	private ImageButton mouseLeftButton;
	private ImageButton mouseRightButton;
	private ImageButton scrollUpButton;
	private ImageButton scrollDownButton;
	private SeekBar sensitivityBar;
	
	private EditText keyboardMsg;
	private ImageButton keyboardMsgSendButton;
	private ImageButton keyboardBackspaceButton;

	public final static int INVALID_POINTER_ID = -1;	
	private int mActivePointerId = INVALID_POINTER_ID; // The Active pointer is the one currently moving our object.
	private float mLastTouchX;
	private float mLastTouchY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mouse);
		
		// mouse section
		mouseLeftButton = (ImageButton)findViewById(R.id.mouseLeftKeyButton);
		mouseRightButton = (ImageButton)findViewById(R.id.mouseRightKeyButton);
		scrollUpButton = (ImageButton)findViewById(R.id.mouseScrollUpButton);
		scrollDownButton = (ImageButton)findViewById(R.id.mouseScrollDownButton);
		sensitivityBar = (SeekBar)findViewById(R.id.mouseSensitivitySeekBar);
		sensitivityBar.setProgress(50);
	    
		mouseLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String data = "c,"+Integer.valueOf(1).toString()+ ","+Integer.valueOf(0).toString();
				Utilities.sendBroadcastTCP(MouseActivity.this, data);
			}
		});
		mouseRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String data = "c,"+Integer.valueOf(0).toString()+ ","+Integer.valueOf(1).toString();
				Utilities.sendBroadcastTCP(MouseActivity.this, data);
			}
		});
		scrollUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(MouseActivity.this, "s,u,");
			}
		});
		scrollDownButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(MouseActivity.this, "s,d,");
			}
		});
		
		// keyboard section
		keyboardMsg = (EditText) findViewById(R.id.keyboardMsgText);
		keyboardMsgSendButton = (ImageButton)findViewById(R.id.keyboardSendButton);	
		keyboardMsgSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try
				{
					// send data
					String data = "k,"+ keyboardMsg.getText().toString();
					Utilities.sendBroadcastTCP(MouseActivity.this, data);
					// refresh message area
					keyboardMsg.setText(null);
					// hide the soft input
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(keyboardMsg.getWindowToken(), 0);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		keyboardBackspaceButton = (ImageButton)findViewById(R.id.keyboardBackspaceButton);
		keyboardBackspaceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.sendBroadcastTCP(MouseActivity.this, "a,");
			}
		});
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Get the intent
	    Intent intent = getIntent();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Let the ScaleGestureDetector inspect all events.
		final int action = MotionEventCompat.getActionMasked(ev); 

		switch (action) { 
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(ev); 
			final float x = MotionEventCompat.getX(ev, pointerIndex); 
			final float y = MotionEventCompat.getY(ev, pointerIndex); 
			// Remember where we started (for dragging)
			mLastTouchX = x;
			mLastTouchY = y;
			// Save the ID of this pointer (for dragging)
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// Find the index of the active pointer and fetch its position
			final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);  

			final float x = MotionEventCompat.getX(ev, pointerIndex);
			final float y = MotionEventCompat.getY(ev, pointerIndex);

			// Calculate the distance moved
			final float dx = x - mLastTouchX;
			final float dy = y - mLastTouchY;
			
			int value = sensitivityBar.getProgress();
			String dataString = "m"+","+Double.valueOf(dx).toString() + "," + Double.valueOf(dy).toString()+ ","+Integer.valueOf(value).toString() + ",";
			Utilities.sendBroadcastUDP(MouseActivity.this, dataString);
			
			// Remember this touch position for the next move event
			mLastTouchX = x;
			mLastTouchY = y;

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = MotionEventCompat.getActionIndex(ev); 
			final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex); 

			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex); 
				mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex); 
				mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
			}
			break;
		}
		}       
		return true;
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
		Intent intent = new Intent(this, ConnectActivity.class);
		startActivity(intent);
	}
	
}
