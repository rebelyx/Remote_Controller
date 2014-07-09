package com.seanxie.rcserver_mark0;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class MouseTCP {

	private final static int SERVER_PORT = 6000;
	
	
	private static String check_ip(String str, char separator, int index) {
        String str_result = "";
        int count = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == separator) {
                count++;
                if(count == index) {
                    break;
                }
            } else {
                if(count == index-1) {
                    str_result += str.charAt(i);
                }
            }
        }
        return str_result;
    }
	
	static void get_ip(NetworkInterface netint) throws SocketException {
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			if(netint.getInetAddresses() != null && (check_ip(inetAddress.getHostAddress(),'.',1)).equals("128"))
				System.out.printf("InetAddress: %s\n", inetAddress.getHostAddress());
        }
	}
	
	public static void getIpAddress() {
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets))
				get_ip(netint);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class MouseThread extends Thread {
		@Override
		public void run() {
			DatagramSocket mouseSocket = null;
			byte[] receiveData = null;
			DatagramPacket receivePacket = null;
			Robot robot = null;
			
			try {				
				mouseSocket = new DatagramSocket(6001);;
				receiveData = new byte[1024*4];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				robot = new Robot();
				
				while(true) {
					mouseSocket.receive(receivePacket);
					String mouseString = new String(receivePacket.getData());
					
					String[] ss = mouseString.split(",");
					if(ss.length>0 && ss[0].equals("m"))
					{
						double deltaX = Double.parseDouble(ss[1]);
						double deltaY = Double.parseDouble(ss[2]);
						double sense = Double.parseDouble(ss[3]);
					
						//take action here
						System.out.println("DeltaX: " + deltaX + ",  DeltaY: " + deltaY + ", s:"+sense);
					
						// SET THE MOUSE X Y POSITION
						
						Point mouse = MouseInfo.getPointerInfo().getLocation();
						int x = (int) (mouse.getX() + deltaX*(sense/25));
						int y = (int) (mouse.getY() + deltaY*(sense/25));
						robot.mouseMove(x, y);
					}
				}
				
			} catch (Exception e) {
				mouseSocket.close();
				run();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		getIpAddress();
		System.out.println("Port: " + SERVER_PORT);
		
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		Robot robot = null;
		InputStream inputStream = null;
		String[] command = null;
		byte buffer[] = new byte[1024*4];
		String dataString = null;
		
		MouseThread mouseThread = new MouseThread();
		mouseThread.start();
		
		// build server socket, and connect to client
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			clientSocket = serverSocket.accept();
			robot = new Robot();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		// parse the inputStream from client to get command, and execute
		try
		{
			while(true)
			{
				
				System.out.println("in loop!");//*************
				inputStream = clientSocket.getInputStream();
				System.out.println("after getInputStream!");//********

				int temp = inputStream.read(buffer);
				if (temp != -1) {
					dataString = (new String(buffer,0,temp)).split("\0")[0];
				} else {
					System.out.println("continue");
					continue;
				}
				
				System.out.println("Out of loop--" + temp + ":\t" + dataString);//***************

				command = (dataString != null) ? dataString.toString().split(",") : null;
				
				Exception NullPointerException = null;
				if(command.length<=0)
					throw NullPointerException;
				
				switch (command[0]) {
				case "a": // keyboard backspace
					robot.keyPress(KeyEvent.VK_BACK_SPACE);
					break;
				case "b": // key binding
					System.out.println(command[1]);
					
					if(command[1].startsWith("http")){
				        ProcessBuilder builder = new ProcessBuilder("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", command[1]);
				        builder.start();
					} else {
						try {
							java.awt.Desktop.getDesktop().open(new File(command[1]));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					break;
				case "c": // mouse click
					double left = Double.parseDouble(command[1]);
					double right = Double.parseDouble(command[2]);
					// SET THE MOUSE X Y POSITION
					if(left >0.0)
					{	 robot.mousePress(InputEvent.BUTTON1_MASK);
						 robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
					
					if(right >0.0)
					{
						 robot.mousePress(InputEvent.BUTTON3_MASK);
						 robot.mouseRelease(InputEvent.BUTTON3_MASK);
					}
					break;
				case "k": // keyboard function
					String data;
					if(dataString.length() == 2)
						data = "";
					else
						data = command[1];
					Keyboard keyboard = new Keyboard();
			    	keyboard.type(data);
			    	break;
				case "p": // ppt controller
					switch(command[1]){
					case "q": // quit
						robot.keyPress(KeyEvent.VK_ESCAPE);
						Thread.sleep(10);
						robot.keyPress(KeyEvent.VK_ESCAPE);
						Thread.sleep(10);
						break;
					case "l": // last slide
						robot.keyPress(KeyEvent.VK_LEFT);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_LEFT);
						Thread.sleep(10);
						break;
					case "n": // next slide
						robot.keyPress(KeyEvent.VK_RIGHT);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_RIGHT);
						Thread.sleep(10);
						break;
					case "p": // play
						robot.keyPress(KeyEvent.VK_SHIFT);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_F5);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_F5);
						robot.keyRelease(KeyEvent.VK_SHIFT);
						Thread.sleep(10);
						break;
					}
					break;
				case "s": // mouse scroll
					if (command[1].equals("u")){
						robot.mouseWheel(-5);
					}
					else if (command[1].equals("d")){
						robot.mouseWheel(5);
					}
					break;
				case "v": // vedio controller
					switch(command[1]){
					case "d": // volume down
						robot.keyPress(KeyEvent.VK_DOWN);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_DOWN);
						Thread.sleep(10);
						break;
					case "u": // volume up
						robot.keyPress(KeyEvent.VK_UP);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_UP);
						Thread.sleep(10);
						break;
					case "m": // mute
						robot.keyPress(KeyEvent.VK_CONTROL);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_M);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_M);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						Thread.sleep(10);
						break;
					case "l": //play last
						robot.keyPress(KeyEvent.VK_PAGE_UP);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_PAGE_UP);
						Thread.sleep(10);
						break;
					case "p": // play
						robot.keyPress(KeyEvent.VK_SPACE);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_SPACE);
						Thread.sleep(10);
						break;
					case "n": // play next
						robot.keyPress(KeyEvent.VK_PAGE_DOWN);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
						Thread.sleep(10);
						break;
					case "r": // fast rewind
						robot.keyPress(KeyEvent.VK_CONTROL);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_LEFT);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_LEFT);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						Thread.sleep(10);
						break;
					case "q": // quit
						robot.keyPress(KeyEvent.VK_CONTROL);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_W);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						Thread.sleep(10);
						break;
					case "f": // fast forward
						robot.keyPress(KeyEvent.VK_CONTROL);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_RIGHT);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_RIGHT);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						Thread.sleep(10);
						break;
					}
					break;
				default:
					break;
				}
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// close sockets
		try {
			inputStream.close();
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
