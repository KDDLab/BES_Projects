package com.example.mo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;

public class Updata {
public static String server="besapp.citypower.com.tw";


public static ArrayList <StationAddress> UpArray(){

	ArrayList <StationAddress> ag = new ArrayList<StationAddress>();	
//	3307
//	8000
 		int servPort=8000;
 		Socket socket;
		try {
			InetAddress[] ip = InetAddress.getAllByName("besapp.citypower.com.tw");
			socket = new Socket(ip[0].getHostAddress(),servPort);
			socket = new Socket("203.64.104.16",servPort);
 		BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
 		PrintWriter out=new PrintWriter(new BufferedWriter(  
                 new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),true);  
 		
 		String i;
 		 
 		String Pu = "";
 		while((Pu = in.readLine())!= null){
 			
 			
 			String GD[] = Pu.split("/");
 			ag.add(new StationAddress(GD[0],GD[1],GD[2],GD[3],GD[4],GD[6],GD[5],GD[7],GD[8]));
 		}
 	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ag;
	}


public static void upReport(String report){

	Socket socket;
	try{
		
		
		InetAddress[] ip = InetAddress.getAllByName("besapp.citypower.com.tw");
//		8001
//		7090
		socket = new Socket(ip[0].getHostAddress(),8001);
		socket = new Socket("203.64.104.16",7090);
		PrintWriter out=new PrintWriter(new BufferedWriter(  
	    new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),true);
				out.println(report);
				out.flush();	 
			 
			out.close();
			socket.close();
	}catch(Exception e){
		e.printStackTrace(); 
	}
	
	
}
}
