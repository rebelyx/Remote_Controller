package com.seanxie.remotecontroller_mark0;


import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class SendMessageService extends Service {
	private Socket tcp_clientSocket = null;
	private TCPMessageReceiver TCPMsgReceiver;
	private UDPMessageReceiver UDPMsgReceiver;
	private OutputStream outStream = null;
	
	private DatagramSocket udp_clientSocket = null;
	private InetAddress IPAddress;
	byte[] sendData = null;

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("Service onBind");
		return null;
	}

	@Override
	public void onCreate() {
		IntentFilter TCP_filter = new IntentFilter();
		TCP_filter.addAction(Utilities.ACTION_TCP_SENDMESSAGE);
		TCPMsgReceiver = new TCPMessageReceiver();
		registerReceiver(TCPMsgReceiver, TCP_filter);
		
		IntentFilter UDP_filter = new IntentFilter();
		UDP_filter.addAction(Utilities.ACTION_UDP_SENDMESSAGE);
		UDPMsgReceiver = new UDPMessageReceiver();
		registerReceiver(UDPMsgReceiver, UDP_filter);
		
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			tcp_clientSocket = new Socket(Utilities.IP_ADDRESS, Utilities.PORT);
			udp_clientSocket = new DatagramSocket();
			IPAddress = InetAddress.getByName(Utilities.IP_ADDRESS);
			sendData = new byte[1024];
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		try {
			tcp_clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	public void sendTCPMessage(String data) {
		data = data + "\0";
		byte[] msgBuffer = null;
		try {
			// encode the sending message
			msgBuffer = data.getBytes("GB2312");
			// get Socket's OutputStream
			outStream = tcp_clientSocket.getOutputStream();
			// send message
			outStream.write(msgBuffer);
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendUDPMessage(String data) {
		try {
			sendData = data.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6001);
			udp_clientSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class TCPMessageReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String msg = intent.getStringExtra(Utilities.DATA_MESSAGE);
			System.out.println("onReceive(TCP): " + msg);//*************
			sendTCPMessage(msg);
		}
	}
	
	public class UDPMessageReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String msg = intent.getStringExtra(Utilities.DATA_MESSAGE);
			System.out.println("onReceive(UDP): " + msg);//*************
			sendUDPMessage(msg);
		}
	}
}
