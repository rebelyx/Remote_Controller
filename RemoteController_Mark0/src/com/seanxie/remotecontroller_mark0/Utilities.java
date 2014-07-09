package com.seanxie.remotecontroller_mark0;

import android.app.Activity;
import android.content.Intent;

public class Utilities {
	public static String DEFAULT_IP = "192.168.2.61";
	public static String IP_ADDRESS = DEFAULT_IP;
	public static final int PORT = 6000;
	static final String ACTION_TCP_SENDMESSAGE = "com.seanxie.remotecontroller_mark0.TCPSENDMSG";
	static final String ACTION_UDP_SENDMESSAGE = "com.seanxie.remotecontroller_mark0.UDPSENDMSG";
	static final String DATA_MESSAGE = "com.seanxie.remotecontroller_mark0.MSG";
	static Intent intent = new Intent();

	public static void sendBroadcastTCP(Activity SenderActivity, String data) {
		intent.setAction(ACTION_TCP_SENDMESSAGE);
		intent.putExtra(DATA_MESSAGE, data);
		SenderActivity.sendBroadcast(intent);
	}
	
	public static void sendBroadcastUDP(Activity SenderActivity, String data) {
		intent.setAction(ACTION_UDP_SENDMESSAGE);
		intent.putExtra(DATA_MESSAGE, data);
		SenderActivity.sendBroadcast(intent);
	}
}
	
/*	public void initiateSocket() {
		try {
			socket = new Socket(IP_ADDRESS, PORT);
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
/*	public void sendDataTCP(String data) {
		try {
			initiateSocket();
			out.write(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
/*	public static void sendDataTCP(String data) {
		try {
			Socket socket = new Socket(IP_ADDRESS, PORT);  
            try {
            	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);  
                out.println(data);
                out.close();
                socket.close();
            } catch (Exception e) {  
                e.printStackTrace();
            }  
        } catch (UnknownHostException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}*/
	
	
/*	public static void sendDataUDP(String data) {
		try
		{
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(Utilities.IP_ADDRESS);
			byte[] sendData = new byte[1024];
			sendData = data.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
			clientSocket.send(sendPacket);
			clientSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
	
/*	public static ArrayList<String> getAvailableIPStrings() {
		ArrayList<String> ipArrayList = new ArrayList<String>();
		for (int i = 0; i <= 255; i++){
			for (int j = 0; j <= 255; j++){
				String ipAddress = "192.168." + i + "." + j;
				try {
					Socket socket = new Socket(ipAddress, PORT);
					if (socket.isConnected())
						ipArrayList.add(ipAddress);
					socket.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ipArrayList;
	}*/
	
/*	public static String getLocalIpAddress() {  
	    try {  
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
	            NetworkInterface intf = en.nextElement();  
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                InetAddress inetAddress = enumIpAddr.nextElement();  
	                if (!inetAddress.isLoopbackAddress()) {
	                	System.out.println("getLocalIpAddress: " + inetAddress.getHostAddress().toString());//**************************************
	                    return inetAddress.getHostAddress().toString();  
	                }  
	            }  
	        }  
	    } catch (SocketException se) {  
	    	se.printStackTrace();
	    }  
	    return null;  
	}*/
