package com.example.mo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

class intComparator implements Comparator<Integer>{
	@Override
	public int compare(Integer lhs, Integer rhs) {
		// TODO Auto-generated method stub
		return -lhs.compareTo(rhs);
	}
	
}

public class MAPStatuon extends FragmentActivity {
private Bundle  bu;
private String Counties,region;
private ListView listview1;
private boolean navigation; 
private ArrayList<Double> DistanceSort = new ArrayList<Double>();
private CountDownTimer CDT;
private ImageButton Pre;
public ProgressDialog  Pr = null;
private  Cursor cR;
private Message msg;
private Location My ;
private double lat,lng;
private  ArrayList<Integer> BatterySort = new ArrayList<Integer>();
private DB mD;
private static String pr;
private static LocationManager Lo;
private String mode;
private ArrayList<StationAddress> ar = new ArrayList<StationAddress>();
private ArrayList<StationAddress> ag = new ArrayList<StationAddress>();
private String [] fg = new String []{
		
		"更新資訊"
		
	};
private Handler Headlermsg = new Handler(){
	
    @Override
    public void handleMessage(Message msg) {
      
	 switch(msg.what){
	 case 0:
		 CDT.cancel();
		break;
	 case 1:
		 CDT.cancel();
		 List();
		 ListView();
		 break;
	 case 2:
		 
		 List();
		 ListView();
		 break;
	 case 3:
		 Toast.makeText(MAPStatuon.this,"請開啟網路",Toast.LENGTH_SHORT ).show();
		 break;
	
		 
	 }
	 super.handleMessage(msg);
 }
};

private LocationListener locationLis = new LocationListener(){
	@Override

	public void onLocationChanged(Location My) {
		 lng = My.getLongitude();
		
		 lat = My.getLatitude();
			
	}
private void Update(Location my) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String pr) {

	  
	}

	@Override
	public void onProviderEnabled(String pr) {
	
	}
	@SuppressLint("ShowToast")
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		  switch (status) {
	      case LocationProvider.OUT_OF_SERVICE:		     
	      Toast.makeText(MAPStatuon.this, "連線斷線", Toast.LENGTH_SHORT).show();
	      break;
	      case LocationProvider.TEMPORARILY_UNAVAILABLE:
	     
	      Toast.makeText(MAPStatuon.this, "暫時無法連線", Toast.LENGTH_SHORT).show();
	      break;
	      case LocationProvider.AVAILABLE:
	             
	      Toast.makeText(MAPStatuon.this, "以連線", Toast.LENGTH_SHORT).show();
	      break;
	  }
	 }
 
};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mapstatuon);

	
		navigation = false;
		
		
		
		
		 bu = this.getIntent().getExtras();
		 Counties = bu.getString("Counties");
		 region = bu.getString("region");
		 mode = bu.getString("mode");
		 String latlng[] = prefer.getLatLng(this).split("/");
		 lat = Double.parseDouble(latlng[0]);
		 lng = Double.parseDouble(latlng[1]);
		 cheaknet(); 
		
		 View();
		
		    msg = new Message();
			msg.what = 2;
			Headlermsg.sendMessage(msg);
		
		


}
	public void updata(View v){
		new AlertDialog.Builder(MAPStatuon.this)
		.setItems(fg, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			switch(which){
				case 0:
					 getDatabases();
					break;
				
			}
			}
			
		}).show();
		
	}
	private void openDB() {
		 
		 mD = new DB(this); 
		 mD.open();

for(StationAddress add: ag){
		if(mD.updata(add.id,add.city_name,add.region_Name,add.Stattion_Name,add.Station_ADDRESS,
				add.Station_LNG,add.Station_LAT,add.Battery_count,add.Status) == false){		 				
			 mD.create(add.id,add.city_name,add.region_Name,add.Stattion_Name,add.Station_ADDRESS,
 					add.Station_LNG,add.Station_LAT,add.Battery_count,add.Status);

		}					
	}
mD.close();
msg = new Message();
	msg.what = 1;
	Headlermsg.sendMessage(msg);
	} 
	public  synchronized void getDatabases() {
		 
	if(CheckNetWrok()){
		CDT =  new CountDownTimer(10000,1000){
		
	            public void onFinish() {
	            	Pr.dismiss();
	            	Toast.makeText( MAPStatuon.this,"更新超時,請檢查網路", Toast.LENGTH_SHORT).show();
	            	msg = new Message();
	       			msg.what = 0;
	       			Headlermsg.sendMessage(msg);
	            }
	            @Override
	            public void onTick(long millisUntilFinished) {
	                // TODO Auto-generated method stub              
	            }
	        }.start();
		
	    
 Pr = ProgressDialog.show(MAPStatuon.this,"更新資訊中","請稍等正在更新資料......",true);
		
			ag.clear();
		 
 		new Thread(){
 			public synchronized void run(){
    	try{
    		 
    		ag = Updata.UpArray();
 		    sleep(2500);
 		    openDB();
 		
 		   
		} catch (Exception e) {
			Toast.makeText(MAPStatuon.this,"無法連接網路",Toast.LENGTH_SHORT);
		}finally{
			
 		Pr.dismiss();
 		}
     }
 		}.start(); 
 		}else{
	   
	    msg = new Message();
		msg.what = 3;
		Headlermsg.sendMessage(msg);
		}
		}

	public void  cheaknet(){
		if(LocationService()){
			EAER();				
		}
		else{
			new AlertDialog.Builder(MAPStatuon.this).setMessage("請開啟Google的定位服務,這樣才能取得您的位子")		
			.setPositiveButton("確定",new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
			
				}
			})
			.show();
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
	
	private void EAER() {
	
	
		
		if(CheckNetWrok()){	    	  
	    	My = Lo.getLastKnownLocation(Lo.NETWORK_PROVIDER);
	    	Lo.requestLocationUpdates(Lo.NETWORK_PROVIDER,2500,1, locationLis);	
	    	
}
		else{
		My = null;
		}	    		
		if(My != null){
			

			 lng = My.getLongitude();
  
			 lat = My.getLatitude();	
			 prefer.setLatLng(MAPStatuon.this,String.valueOf( My.getLatitude()), String.valueOf(My.getLongitude()));	 
		}        
		}
	private boolean LocationService() {
		  Lo = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	

				if (Lo.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {		 
				  return true;
				 }
			return false;
			}
	
		public double Distance(double lat1, double lng1, double lat2, double lng2) 
		{
			final double f = 1/298.257223563;                        
			final double a = 6378137;
			double di;
				double d1 = Math.atan((1-f)*Math.tan(lat1));                                                                                                                                                                                                                
				double d2 = Math.atan((1-f)*Math.tan(lat2));
				double P = Math.pow(Math.sin(d1) + Math.sin(d2), 2);
				double Q = Math.pow(Math.sin(d2) - Math.sin(d2), 2);
				double b = Math.acos(Math.sin(d1)*Math.sin(d2) + Math.cos(d1)*Math.cos(d2)*Math.cos(lng1-lng2));
				double X = (b-Math.sin(b))/(1+Math.cos(b))/4;
				double Y = (b+Math.sin(b))/(1-Math.cos(b))/4;
				double d = (int)(a*(b - f*(P*X + Q*Y)));
				
				return di = d*Math.PI/180;
		}
		public  String DistanceText( Double v)
		{
			double distance = v;
		   if(distance < 1000 ) return String.valueOf((int)distance) + "m" ;
		   else return new DecimalFormat("#.00").format(distance/1000) + "km" ;
		}
	private void View() {

		 Pre = (ImageButton) findViewById(R.id.Pre);
		
		 

		 
		 Pre.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(android.view.View v) {
				MAPStatuon.this.finish();
				
			}
			
		});
	}


	public  void EE(LatLng m) {
		if( lng != 0 && lat != 0){
			LatLng start;
			LatLng finish;
			start = null;
			finish = null;
			
			start  = new LatLng(lat,lng);
			Latlng point = new Latlng();
			
			if(m == null){
				finish = point.getnear(lat, lng);
			}else{
				finish = m;
			}

			Uri uri = Uri.parse("http://maps.google.com/maps? f=d&saddr="+start.latitude+"%20"+start.longitude+"&daddr="+finish.latitude
					+"%20"+finish.longitude+"&hl=en");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it); 
	}else{Toast.makeText(MAPStatuon.this, "無法使用此功能,請在手機設定裡開起定位服務後,重啟程式", Toast.LENGTH_SHORT).show();}
		
		}
	private void ListView() {

		listview1.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0,
					android.view.View arg1, int arg2, long arg3) {
				final int get = arg2;
			
							 
				new AlertDialog.Builder( MAPStatuon.this)
				.setPositiveButton("GOOGLE路線導航",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LatLng m = new LatLng(Double.valueOf(ar.get(get).Station_LNG),
								Double.valueOf(ar.get(get).Station_LAT));
						
						cheaknet();
						if(CheckNetWrok() && My != null){
							EE(m);
						}else{
							Toast.makeText(MAPStatuon.this, "請開啟網路",Toast.LENGTH_SHORT).show();
						}
						
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub								
					}
				}).show();
						 
			           
				 
				 
			}
		});
	
	}
	 

 
	private void List() { 
		 listview1 = (ListView)findViewById(R.id.listu);
	     SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.list,
	            new String[]{"mode","title","info","img","dail"},
	            new int[]{R.id.textView3,R.id.textView1,R.id.textView2,R.id.quantity,R.id.station});
	      listview1.setAdapter(adapter);
	}

	
	private java.util.List<? extends Map<String, ?>> getData() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		  Map<String, String> map = new HashMap<String, String>();
		  ArrayList<StationAddress> batteryag = new ArrayList<StationAddress>();
		  ar.clear();
		  map.clear();
		  BatterySort.clear();
		  batteryag.clear();
		  mD = new DB(this);
		  mD.open();
		  cR = mD.getALL(); 
		  char Cou1 = Counties.charAt(0);
		      char Cou2 = Counties.charAt(1);
		      char Reg1 = region.charAt(0);
		      char Reg2 = region.charAt(1);

		 while(cR.moveToNext()){
			
			    if(Cou1 == cR.getString(1).charAt(0)){
			    	 if( Cou2 == cR.getString(1).charAt(1)){ 
			    		 if( Reg1 == cR.getString(2).charAt(0)){
			    			 if( Reg2 == cR.getString(2).charAt(1)){
			    		 
			    				 batteryag.add(new StationAddress(cR.getString(0),cR.getString(1),cR.getString(2),cR.getString(3),cR.getString(4),cR.getString(5),cR.getString(6)
					,cR.getString(7),cR.getString(8)));	
			    				 if(mode.equals("電池排序")){
			    				 BatterySort.add(Integer.parseInt(cR.getString(7)));	 
			    				 }else{
			    					
			    						 double  distance = Distance(Double.valueOf(cR.getString(6)),Double.valueOf(cR.getString(5)),lng,lat);
			    					 DistanceSort.add(distance);
			    				
			    				 
			    		 }
			    			 }
			    	 }}
			    	 }
			    }
		 if(mode.equals("電池排序")){
		 int x = 1;
 Collections.sort(BatterySort, new intComparator()); 
 for(int bs:BatterySort){
	 
	
			 int i = 0;
		 for(StationAddress ag:batteryag){
				 String fo = String.valueOf(bs);
				 if(ag.Battery_count.equals(fo)){
					 
					 batteryag.remove(i);
					 map = new HashMap<String,String>();
					 if(lat == 0 && lng == 0){
					  	 map.put("info","未取得位置");
					 }else{
					map.put("info", String.valueOf(DistanceText(Distance(
							Double.valueOf(ag.Station_LAT),Double.valueOf(ag.Station_LNG),lng,lat))));
					 }
					 
					map.put("title",ag.Stattion_Name);
                    map.put("mode",ag.Station_ADDRESS); 
                    map.put("img"," 電池數量:"+String.valueOf(bs));
        
            	
                    String Status = null;
                       if(ag.Status.equals("Y")){
                            Status =  "正常運作中";
                          }else{
                             Status = "暫停運作中";
                               }
                       map.put("dail", Status);
                       list.add(map);	
                       ar.add(new StationAddress(ag.id,ag.city_name,ag.region_Name,ag.Stattion_Name,ag.Station_ADDRESS,
               				ag.Station_LNG,ag.Station_LAT,ag.Battery_count,ag.Status));
                       x++;
                       break;
				 }
				 i++;
			 }
		 }
		 }else{
			
			 int x = 1;
			 Collections.sort( DistanceSort);
			 for(double bs: DistanceSort){
				 int i = 0;
			 for(StationAddress ag:batteryag){
					 String fo = String.valueOf(bs);
					double dissort = Distance(Double.valueOf(ag.Station_LAT),Double.valueOf(ag.Station_LNG),lng,lat);
					 if(String.valueOf(dissort).equals(fo)){
						 
						 batteryag.remove(i);
						 map = new HashMap<String,String>();
						map.put("info", String.valueOf(DistanceText(dissort)));
						map.put("title",ag.Stattion_Name);
	                    map.put("mode",ag.Station_ADDRESS); 
	                    map.put("img"," 電池數量:"+ag.Battery_count);
	                    String Status = null;
	                       if(ag.Status.equals("Y")){
	                            Status =  "正常運作中";
	                          }else{
	                             Status = "暫停運作中";
	                               }
	                       map.put("dail", Status);
	                       list.add(map);	
	                       ar.add(new StationAddress(ag.id,ag.city_name,ag.region_Name,ag.Stattion_Name,ag.Station_ADDRESS,
	               				ag.Station_LNG,ag.Station_LAT,ag.Battery_count,ag.Status));
	                       x++;
	                       break;
					 } 
					 i++;       
				 }
			 }
		
		 
		 }  

		 
		
		   cR.close();   
		   mD.close();
		   
		return  list;
	}
}
