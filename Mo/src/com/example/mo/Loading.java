package com.example.mo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Loading extends Activity {
	private Cursor cR;
	private DB mD ;
	private  Message msg;
	private Bundle bu;
	private TextView Prompt;
	private LocationManager Lo;
	private boolean getLo ;
	private  CountDownTimer CDT;
	private String DBpath = "/data/data/com.example.mo/databases/";
	private String DBname = "BatteryStation";

	private static ArrayList<StationAddress> ag = new ArrayList<StationAddress>();

	private static int result_1 = 100;
	
	private Handler Headlermsg = new Handler(){
		
	    @Override
	    public void handleMessage(Message msg) { 
		 switch(msg.what){
		 case 0:
			        CDT.cancel();
			        Intent in = new Intent();
		 		    in.setClass(Loading.this,LISTMenu.class);
					startActivity(in);
					System.exit(0);
			break;
		 case 1:
				CDT.cancel();
			 new AlertDialog.Builder(Loading.this).setMessage("請開啟網路,進行初次的載入")	
			 .setCancelable(false)
				.setPositiveButton("關閉程式",new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0);
						
					}
				})
				.show(); 
			 break;
		 case 2:
			   CDT.cancel();
			 new AlertDialog.Builder(Loading.this).setMessage("請開啟設定中的定位服務,這樣才能取得您的位子")	
			 .setCancelable(false)
				.setPositiveButton("繼續使用",new DialogInterface.OnClickListener() {				
							@Override
							public void onClick(DialogInterface dialog, int which) {
								getLo = false;
								CdtView();
								getDatabases(); 
							}
						}).setNegativeButton("關閉程式", new DialogInterface.OnClickListener() {				
							@Override
							public void onClick(DialogInterface dialog, int which) {
								System.exit(0);
							}
						})
				.show();
			 
			 break;
		 }
		 super.handleMessage(msg);
	 }
 };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		Prompt = (TextView) findViewById(R.id.Prompt);
		getLo = true;
		CdtView();
		getDatabases(); 
		
	} 
	
	private void CdtView() {
CDT =  new CountDownTimer(12000,1000){
            
            @Override 
            public void onFinish() {
            	msg = new Message();
      			msg.what = 0;
      			Headlermsg.sendMessage(msg);
            	
            }
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub              
            }
        }.start();
		
	}

	public boolean cheakSQL(){
		DB mD = new DB(this); 
		 mD.open();
		 Cursor cR = mD.getALL();
		if(cR.getCount() != 0){
			 cR.close();   
			  mD.close();
			return true;  

		}else{
			 cR.close();   
			  mD.close();
			return false;
		}
	}

	private void EstablishSQL(){
		File f = new File(DBpath);
		if(!f.exists()){
			f.mkdir();
			try {				
		InputStream in = getResources().openRawResource(R.raw.batterystation);
		 OutputStream out = new FileOutputStream(DBpath+DBname);
			
			byte [] b = new byte[8192];
			int  i;					
				while((i = in.read(b)) != -1){
					out.write(b,0,i);
				}
				out.flush();
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}				
		}	
	}
	

	public boolean CheckNetWrok(){
		ConnectivityManager H = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = H.getActiveNetworkInfo();
		if(info != null){
			return true;
		}
		return false;
	}
	   public  void getDatabases() {
	   		
	 		new Thread(){ 
	 			public  void run(){
	    	try{
	    		
	    	       EstablishSQL();
			    		sleep(1000);
	    	
									
				if(CheckNetWrok()){	
					if(getLo){
					GetMyLocation();
					if(LocationService()){
					 ag = Updata.UpArray(); 
				if(cheakSQL()){					  
				  sleep(3000);
				  openDB();			
				  }else{		 
				  sleep(6000);
				   openDB();
					}
					}
					}else{
						    String lng = String.valueOf(0);
							String lat = String.valueOf(0);	
							 prefer.setLatLng(Loading.this,lat, lng);
						 ag = Updata.UpArray(); 
							if(cheakSQL()){					  
							  sleep(3000);
							  openDB();			
							  }else{		 
							  sleep(6000);
							   openDB();
								}
						
					}
				}else{ 
					if(cheakSQL()){	
						sleep(1500);
						msg = new Message();
		      			msg.what = 0;
		      			Headlermsg.sendMessage(msg);			
					}else{
					
						msg = new Message();
		      			msg.what = 1;
		      			Headlermsg.sendMessage(msg);
					}
				
	 		 
				
				}
			} catch (Exception e) {
				Toast.makeText(Loading.this,"錯誤",Toast.LENGTH_SHORT).show();
			}finally{
		    	 		}
	 			}

				private void GetMyLocation() {
					if(LocationService()){
						EAER();				
					}
					else{
						msg = new Message();
		      			msg.what = 2;
		      			Headlermsg.sendMessage(msg);
					}
				}

	 		}.start();	
		}
	   
		private boolean LocationService() {
			  Lo = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
					if (Lo.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {		 
					  return true;
					 }
				return false;
				}
		private void EAER() {
			Location My ;	
			if(CheckNetWrok()){	    	  
				    	My = Lo.getLastKnownLocation(Lo.NETWORK_PROVIDER);			    	
			}
			else{				
			My = null;
			}	    					
			if(My != null){
			  
			    String lng = String.valueOf(My.getLongitude());
	          
				String lat = String.valueOf(My.getLatitude());	
				 prefer.setLatLng(Loading.this,lat, lng);
			
		
				
				
			}
				
			}
				private void openDB() {
					   
				if(ag != null){
		 			 mD = new DB(this); 
		 			 mD.open();
		 		  
            for(StationAddress add: ag){
            	if(mD.updata(add.id,add.city_name,add.region_Name,add.Stattion_Name,add.Station_ADDRESS,
        				add.Station_LNG,add.Station_LAT,add.Battery_count,add.Status) == false){		 				
        		    mD.create(add.id,add.city_name,add.region_Name,add.Stattion_Name,add.Station_ADDRESS,
        					add.Station_LNG,add.Station_LAT,add.Battery_count,add.Status);    		    
            	}
            } 
				}
		 		 
            mD.close();
				
         
            msg = new Message();
  			msg.what = 0;
  			Headlermsg.sendMessage(msg);
          
				} 
				public boolean onKeyDown(int keyCode, KeyEvent event) {
				    if(keyCode == KeyEvent.KEYCODE_BACK) { 
				    	 System.exit(0);
				       Loading.this.finish();
				       
				        return true;
				    } 
				    return super.onKeyDown(keyCode, event);
				}
}
