package com.seanxie.remotecontroller_mark0;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class KeyBindingActivity extends Activity {
    
	private ImageButton mouseAndKeyboardButton;
	private GridView gridView;
	
  	private int[] mImageIds = {
  			R.drawable.myfolder,
  			R.drawable.coursework,
  			R.drawable.webbrowser,
  			R.drawable.movie,
  			R.drawable.music,
  			R.drawable.youtube,
  			R.drawable.pptcontroller,
  			R.drawable.gamekeybinder,
  			R.drawable.videoplayer
	};
  
	private int[] TitleTexts = {
			R.string.personalfolder,
			R.string.coursework,
			R.string.webbrowser,
			R.string.movie,
			R.string.music,
			R.string.youtube,
			R.string.pptcontroller,
			R.string.gamekeybinder,
			R.string.videoplayer
	};
	
	private String[] TitleTags = { // file locations
			"C:\\Users\\Sean Xie", // MyFolder
			"E:\\Study\\UCLA\\9th fall", // CourseWork
			"http://www.google.com/", // Browser
			"C:\\temp\\Movie", // Movie
			"C:\\temp\\Music", // Music
			"http://www.youtube.com/", // Youtube
			"attend:com.seanxie.remotecontroller_mark0.PPTControllerActivity", // PPTController
			"C:\\temp\\Movie", // GameKeybinder
			"attend:com.seanxie.remotecontroller_mark0.VedioPlayerActivity", // VideoPlayer
	};
	

    @Override  
    public void onCreate(Bundle savedInstanceState) {  
    	super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_keybinding);
	    setTitle("Key Binding");
	    
	 // Get the intent
     	Intent intent = getIntent();
	    
     	gridView =(GridView)this.findViewById(R.id.GridViewId);  
        gridView.setAdapter(new gridViewAdapter(mImageIds,TitleTexts));
        gridView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        		String data = null;
        		TextView tv = (TextView) v.findViewById(R.id.TextItemId);
        		
        		if(tv.getTag().toString().startsWith("attend:")){ // redirect to another activity
        			String className = tv.getTag().toString().substring(7, tv.getTag().toString().length());        		
					try {
						Intent intent;
						intent = new Intent(KeyBindingActivity.this, Class.forName(className));
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
        		} else { // send message to Server to execute corresponding functionality
        			data = "b," + ((TextView) v.findViewById(R.id.TextItemId)).getTag().toString() + ",";
//        			Utilities.sendDataTCP(data);
        			Utilities.sendBroadcastTCP(KeyBindingActivity.this, data);
        		}
        	}
        	});
        
	    mouseAndKeyboardButton = (ImageButton)findViewById(R.id.mouseAndKeyboardButton);
	    mouseAndKeyboardButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(KeyBindingActivity.this, MouseActivity.class);
				startActivity(intent);
			}
		});
        
     // Show the Up button in the action bar.
        setupActionBar();        
	}

    public class gridViewAdapter extends BaseAdapter {  
        private View[] itemViews;  
  
        public gridViewAdapter(int[] mImageIds, int[] TitleTexts) {  
            itemViews = new View[mImageIds.length];  
  
            for (int i = 0; i < itemViews.length; i++) {  
                itemViews[i] = makeItemView(mImageIds[i], TitleTexts[i], TitleTags[i]);  
            }  
        }  
 
        public int getCount() {  
            return itemViews.length;  
        }  
  
        public View getItem(int position) {  
            return itemViews[position];  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  
  
        private View makeItemView(int strmImageIds, int strTitleTexts, String strTitleTags) {  
            LayoutInflater inflater = (LayoutInflater)KeyBindingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
            View itemView = inflater.inflate(R.layout.singlegrid, null);  
            TextView title = (TextView) itemView.findViewById(R.id.TextItemId);  
            title.setText(strTitleTexts);
            title.setTag(strTitleTags);
            ImageView image = (ImageView) itemView.findViewById(R.id.ImageItemId); 
            image.setImageResource(strmImageIds);    
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return itemView;  
        }  
  
       public View getView(int position, View convertView, ViewGroup parent) {  
            if (convertView == null)  
              return itemViews[position];  
           return convertView;  
       }  

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