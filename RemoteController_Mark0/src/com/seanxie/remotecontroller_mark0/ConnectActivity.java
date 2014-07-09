package com.seanxie.remotecontroller_mark0;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConnectActivity extends Activity {
	
	private RelativeLayout connectScreen;
	private ImageView connectView;
	private TextView ipTextView;
	private ImageButton confirmIpButton;
	private EditText ipEditText;
	private ImageButton startButton;
	private ImageButton getAvailableIPsButton;
	public static boolean isConnected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		connectScreen = (RelativeLayout)findViewById(R.id.connectScreen);
		connectView = (ImageView)findViewById(R.id.connectImage);
		ipTextView = (TextView)findViewById(R.id.ipTextView);
		confirmIpButton = (ImageButton)findViewById(R.id.confirmIpButton);
		ipEditText = (EditText)findViewById(R.id.ipEditText);
		startButton = (ImageButton)findViewById(R.id.startButton);
		getAvailableIPsButton = (ImageButton)findViewById(R.id.getAvailableIPsButton);
		
		if (!Utilities.IP_ADDRESS.equals(Utilities.DEFAULT_IP)){
			ipEditText.setHint(Utilities.IP_ADDRESS);
		}
		
		getAvailableIPsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ipEditText.setText(Utilities.DEFAULT_IP);
			}
		});
		
		// Click the getAvailableIPsButton to get a list of available IPs
/*		getAvailableIPsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<String> ipArrayList = Utilities.getAvailableIPStrings();
				System.out.println("Number of available IPs: " + ipArrayList.size());
				
				Iterator<String> it = ipArrayList.iterator();
				
				
				try{
					String filePath = "C:\\Users\\Sean Xie\\Downloads\\test.txt";
					File file = new File(filePath);
					FileWriter filewriter = new FileWriter(file, true);
					while(it.hasNext()){
						filewriter.write(it.next().toString() + "\n");
					}
			        filewriter.close();
			        } catch (Exception e) {
			        	e.printStackTrace();
			        }
			}
		});*/
		
		// Click the screen to disappear the keyboard
		connectScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 if (v.getId() == R.id.connectScreen) {
                     InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				 }
			}
		});
		
		// Click hostAddressButton to save the IP address into a string
		confirmIpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.IP_ADDRESS = ipEditText.getText().toString();
//				Utilities.initiateSocket();
				
				// start service(connect socket)
				Intent intent = new Intent();
				intent.setClass(ConnectActivity.this, SendMessageService.class);
				startService(intent);
				
				// disappear the keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                // change background color, appear entry icon
                connectScreen.setBackgroundColor((getResources().getColor(R.color.backgroundyellow)));
				confirmIpButton.setBackgroundColor((getResources().getColor(R.color.backgroundyellow)));
				startButton.setBackgroundColor((getResources().getColor(R.color.backgroundyellow)));
				getAvailableIPsButton.setBackgroundColor((getResources().getColor(R.color.backgroundyellow)));
                connectView.setImageDrawable(getResources().getDrawable(R.drawable.connected));
				startButton.setVisibility(View.VISIBLE);

/*				ipAddress = ipEditText.getText().toString();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                
				ConnectTestingThread testingThread = new ConnectTestingThread();
				long beginTime = new Date().getTime();
				testingThread.start();
				while (new Date().getTime() - beginTime <= 3000) {
					try {
						Thread.currentThread().sleep(700);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			
				if(isConnected){
					connectScreen.setBackgroundColor((getResources().getColor(R.color.backgroundyellow)));
					confirmIpButton.setBackgroundColor((getResources().getColor(R.color.backgroundyellow)));
					startButton.setBackgroundColor((getResources().getColor(R.color.backgroundyellow)));
					
					connectView.setImageDrawable(getResources().getDrawable(R.drawable.connected));
					startButton.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(getApplicationContext(), "Please type a valid IP address and try again.", Toast.LENGTH_LONG).show();
				}*/
			}	
		});
		
		
		
		// Click mouseButton to direct to the Mouse activity
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConnectActivity.this, KeyBindingActivity.class);
				startActivity(intent);
				}
			});
		}

	class ConnectTestingThread extends Thread {
		@Override
		public void run() {
			try {
				Socket socket = new Socket(Utilities.IP_ADDRESS, Utilities.PORT);
				ConnectActivity.isConnected = socket.isConnected();
				if (ConnectActivity.isConnected) {
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);  
					out.println("test");
					out.close();
				}
                socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
