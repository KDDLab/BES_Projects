package com.example.mo;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.audiofx.BassBoost.Settings;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater; 
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint({ "SdCardPath", "ShowToast", "SimpleDateFormat" })
public class Ui extends FragmentActivity { 
	private static LocationManager Lo;
	private static String pr;
	private CountDownTimer CDT;
	private static TextView Name, Distance,Address,Quantity,Station;
	public  static ArrayList<StationAddress> ag = new ArrayList<StationAddress>();
	public static double  lat;
	public  static double  lng;
	private String [] fg = new String []{
		"目前位置",
		"更新資訊"
		
	};
	private static Marker markme;
	private DB mD ;
	private Cursor cR;
	private ImageButton Previous,Letgo;
	private Location MyLocation ;
	private boolean navigation; 
	private static final int result_1 = 100;
	private MarkerOptions mmm  = new MarkerOptions();
	private Message msg;
	 public ProgressDialog  Pr = null;
	private Handler Headlermsg = new Handler(){
			
		    @Override
		    public void handleMessage(Message msg) {
			 switch(msg.what){
			 case 0:
				CDT.cancel();
				adde();
				break;
			 case 1:
				   JI(); 
				 break;
			 case 2:
				 Toast.makeText(Ui.this,"網路未開啟,無法更新最新資訊", Toast.LENGTH_SHORT).show();
				 adde();
				 break;
			 case 3:
				 adde();
				 break;
			 }
			 super.handleMessage(msg);
		 }
	 };
	
	GoogleMap map ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_ui);

			 map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			 TY();
				navigation = false;

		View();

		    msg = new Message();
			msg.what = 3;
			Headlermsg.sendMessage(msg);
		 touchMap();

		
		 Letgo.setEnabled(false);
 
	
	
			
		 Previous =  (ImageButton) findViewById(R.id.Previous);
		 
		 Previous.setOnClickListener(new Button.OnClickListener(){

			 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Ui.this.finish();
			}
		 });
	}
private void View() {
	Name = (TextView) findViewById(R.id.name);
	Distance = (TextView) findViewById(R.id.distance);
	Address = (TextView) findViewById(R.id.address);
	Station = (TextView) findViewById(R.id.station);
	Quantity = (TextView) findViewById(R.id.quantity);
	Letgo = (ImageButton) findViewById(R.id.Letgo);
		
	}

	public void updata(View v){
		new AlertDialog.Builder(Ui.this)
		.setItems(fg, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			switch(which){
				case 0:
					if(lng != 0 && lat != 0){	
					onResume();
					UO();
					}else{
						Toast.makeText(Ui.this,"無法使用此功能,請在手機設定裡開起定位服務後,再按更新", Toast.LENGTH_SHORT).show();
					}
					break;
				case 1:
					if(CheckNetWrok()){
						 onResume();
						 getDatabases();
						}else{
							Toast.makeText(Ui.this,"請開啟網路", Toast.LENGTH_SHORT).show();
						}
					break;
			}
			}
		}).show();
		
		
		
	}

protected void onActivityResult(int requestCode, int resultCode,Intent in){
	super.onActivityResult(requestCode, resultCode, in);
	if(resultCode == RESULT_OK){
		if(requestCode == result_1){
			
			UO();
			Bundle bu = in.getExtras();
			LatLng lans = new LatLng(bu.getDouble("lat"),bu.getDouble("lng"));
			EE(lans); 
			
		}
	}
	
}

	private void touchMap() {
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker m) {
				
				 ShowInformation(m.getPosition());				 
				 
				return navigation;
			
			}
			 
		});
	}


	private void ShowInformation(final LatLng pos) {
		String status = null;
				for(StationAddress as :ag){
					DecimalFormat df=new DecimalFormat("#.######");
					String x = df.format(pos.latitude);
					String y = df.format(pos.longitude);
					String b = df.format(as.getlat());
					String a = df.format(as.getlng());
					

 					if( Double.valueOf(y).equals(Double.valueOf(b)) && Double.valueOf(x).equals(Double.valueOf(a))){
 						  if(as.Status.equals("Y")){
 				        	 status =  "正常運作";
 				        }else{
 				        	 status = "維修中";
 				        }
 						  Name.setText(as.Stattion_Name);
 						  Address.setText(as.Station_ADDRESS);
 						  Quantity.setText("數量"+" "+as.Battery_count);
 						  Station.setText(status);
 						 if(lat == 0 && lng == 0){
 							 Distance.setText("未取得位置");
 						 }else{
 						     Distance.setText("大約距離您"+DistanceText(Distance(Double.valueOf(as.Station_LAT),Double.valueOf(as.Station_LNG),lng,lat)));
 						 }
			
 						  Letgo.setEnabled(true);
 						  Letgo.setOnClickListener(new Button.OnClickListener(){

							@Override
							public void onClick(android.view.View v) {
								EE(pos);								
							}
 							  
 						  });
						break; 
					}
				
				}
				
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
	

	public boolean CheckNetWrok(){
		ConnectivityManager H = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = H.getActiveNetworkInfo();
		if(info != null){
			return true;
		}
		return false;
	}
	public  synchronized void getDatabases() {
		
	if(CheckNetWrok()){
		CDT =  new CountDownTimer(8000,1000){
		
	            public void onFinish() {
	            	Pr.dismiss();
	            	Toast.makeText(Ui.this,"更新超時,請檢查網路", Toast.LENGTH_SHORT).show();
	            
	            	msg = new Message();
	       			msg.what = 0;
	       			Headlermsg.sendMessage(msg);
	            }
	            @Override
	            public void onTick(long millisUntilFinished) {
	                             
	            }
	        }.start();
		
	    
 Pr = ProgressDialog.show(Ui.this,"更新資訊中","請稍等正在更新資料......",true);
		
			ag.clear();
	
 		new Thread(){
 			public synchronized void run(){
    	try{
    		
    		
    		 ArrayList<StationAddress> temp = Updata.UpArray();
    		 sleep(3000);
    		 if(temp != null){
    			 ag = temp;
    			 openDB();
    		 }
    		
 		
 		   
			
		} catch (Exception e) {
			Toast.makeText(Ui.this,"無法連接網路",Toast.LENGTH_SHORT).show();
		}finally{
 		Pr.dismiss();
 		}
     }
 		}.start(); 
 		}else{
	  
 			Toast.makeText(Ui.this,"網路未開啟,無法更新最新資訊", Toast.LENGTH_SHORT).show();
			
		}
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
	msg.what = 0;
	Headlermsg.sendMessage(msg);
	} 

     

	protected void onResume(){
			super.onResume();
		
			if(LocationService()){
				
				EAER();				
			}
			else{
				new AlertDialog.Builder(Ui.this).setMessage("請開啟Google的定位服務,這樣才能取得您的位子")		
				.setPositiveButton("確定",new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
				
					}
				})
				.show();
			}
		}

public void  adde() {

	
	ag.clear();
	 mD = new DB(this); 
	 mD.open();
     cR = mD.getALL(); 
	if(cR.getCount() > 0){
		while(cR.moveToNext()){
			ag.add(new StationAddress(cR.getString(0),cR.getString(1),cR.getString(2),cR.getString(3),cR.getString(4),cR.getString(5),cR.getString(6)
					,cR.getString(7),cR.getString(8)));
		}
	}else{
		Toast.makeText(Ui.this,"請點選右下方更新資訊",Toast.LENGTH_SHORT).show();
	}
	
	   cR.close();   
	   mD.close();
	   
	   JI();
	    msg = new Message();
		msg.what = 1;
		Headlermsg.sendMessage(msg);
	   
	 
	} 
public ArrayList<StationAddress> aL(){
	return ag;
}
public boolean cheakSQL(){
	 mD = new DB(this); 
	 mD.open();
     cR = mD.getALL();
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



	private void EAER() {
		
		Location My ;	
		if(CheckNetWrok()){	    	  
			    	My = Lo.getLastKnownLocation(Lo.NETWORK_PROVIDER);
			    	Lo.requestLocationUpdates(Lo.NETWORK_PROVIDER,2500,1, locationLis);	
			    	
		}
		else{
		My = null;
		String latlng[] = prefer.getLatLng(this).split("/");
		 lat = Double.parseDouble(latlng[0]);
		 lng = Double.parseDouble(latlng[1]);
		 TY();
		}	    		
		this.MyLocation = My;
		Update(My);
		if(My != null){
		
			 lng = My.getLongitude();
        
			 lat = My.getLatitude();	
			 prefer.setLatLng(Ui.this,String.valueOf( My.getLatitude()), String.valueOf(My.getLongitude()));
			 TY();
		}else{
			if(lat ==0 && lng ==0){
				Toast.makeText(Ui.this, "無法使用此功能,請在手機設定裡開起定位服務後,重啟程式", Toast.LENGTH_SHORT).show();
				TY();
			}else{
				showme(lat,lng);
				TY();
			}
			
			 
		}
		       
			
		
	}
public void TY(){
	 CameraPosition cam = new CameraPosition.Builder()
		.zoom(12)
		.target(new LatLng(25.040991, 121.459910))
		.build();
		 map.animateCamera(CameraUpdateFactory.newCameraPosition(cam));
	
}
	private boolean LocationService() {
  Lo = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
		if (Lo.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {		 
		  return true;
		 }
	return false;
	}
	
	
	protected void	onPause(){
		Lo.removeUpdates(locationLis);
		super.onPause();			
	}
@SuppressLint("ShowToast")


	LocationListener locationLis = new LocationListener(){
		@Override

		public void onLocationChanged(Location My) {
			Update(My);			
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
		      Toast.makeText(Ui.this, "連線斷線", Toast.LENGTH_SHORT).show();
		      break;
		      case LocationProvider.TEMPORARILY_UNAVAILABLE:
		     
		      Toast.makeText(Ui.this, "暫時無法連線", Toast.LENGTH_SHORT).show();
		      break;
		      case LocationProvider.AVAILABLE:
		             
		      Toast.makeText(Ui.this, "以連線", Toast.LENGTH_SHORT).show();
		      break;
		  }
		 }

	};

	

	private void Update(Location My) {
	
		 if (My != null) {
		
		 lng = My.getLongitude();
	
		 lat = My.getLatitude();
	
		 
		 showme(lat,lng);

		 }else{
			String where = "";
		  where = "無法定位"
		  		+ "\n請開啟網路連接";
		  
		 }
	}

	

	


private void showme( double lat,  double lng) {
	
	 if (markme != null) {
		 markme.remove();
		 }
	MarkerOptions mark = new  MarkerOptions();
	mark.position(new LatLng(lat,lng));
	mark.title("現在位子");
	mark.icon(BitmapDescriptorFactory.fromResource(R.drawable.loocc));
	markme = map.addMarker(mark);
	
}

	public  void EE(LatLng m) {
	
		if(MyLocation != null){
			
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


	try{
		Uri uri = Uri.parse("http://maps.google.com/maps? f=d&saddr="+start.latitude+"%20"+start.longitude+"&daddr="+finish.latitude
				+"%20"+finish.longitude+"&hl=en");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it); 

			}catch(Exception e){
				Toast.makeText(Ui.this, "Exception e", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(Ui.this, "取讀不到位置", Toast.LENGTH_SHORT).show();
		}
	}

	
	 		
	
	private void UO() {
		CameraPosition cam = new CameraPosition.Builder()
		.zoom(15)
		.target(new LatLng(lat,lng))
		.build();
		
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cam));
	}


	

	private void JI() {
		if (ag.size()>0){
			for(StationAddress as:ag){
				double x = as.getlng();
				double y = as.getlat();


				LatLng	path = new LatLng(x,y);	 
 
				MarkerOptions mmm  = new MarkerOptions();

				mmm.position(path);


				mmm.icon(BitmapDescriptorFactory.fromResource(BatteryImage(Integer.valueOf(as.Battery_count))));
				map.addMarker(mmm);
			}
		}
	}
	public int BatteryImage(int quantity){
		int q = 0;
		switch(quantity){
		case 0:
			q = R.drawable.jo0;
			break;
		case 1:
			q = R.drawable.jo1;
			break;
		case 2:
			q = R.drawable.jo2;
			break;
		case 3:
			q = R.drawable.jo3;
			break;
		case 4:
			q = R.drawable.jo4;
			break;
		case 5:
			q = R.drawable.jo5;
			break;
		case 6:
			q = R.drawable.jo6;
			break;
		case 7:
			q = R.drawable.jo7;
			break;
		case 8:
			q = R.drawable.jo8;
			break;
		case 9:
			q = R.drawable.jo9;
			break;
		case 10:
			q = R.drawable.jo10;
			break;
		case 11:
			q = R.drawable.jo11;
			break;
		case 12:
			q = R.drawable.jo12;
			break;
		case 13:
			q = R.drawable.jo13;
			break;
		case 14:
			q = R.drawable.jo14;
			break;
		case 15:
			q = R.drawable.jo15;
			break;
		case 16:
			q = R.drawable.jo16;
			break;
		case 17:
			q = R.drawable.jo17;
			break;
		case 18:
			q = R.drawable.jo18;
			break;
		case 19:
			q = R.drawable.jo19;
			break;
		case 20:
			q = R.drawable.jo20;
			break;
		case 21:
			q = R.drawable.jo21;
			break;
		case 22:
			q = R.drawable.jo22;
			break;
		case 23:
			q = R.drawable.jo23;
			break;
		case 24:
			q = R.drawable.jo24;
			break;
		case 25:
			q = R.drawable.jo25;
			break;
		case 26:
			q = R.drawable.jo26;
			break;
		case 27:
			q = R.drawable.jo27;
			break;
		case 28:
			q = R.drawable.jo28;
			break;
		case 29:
			q = R.drawable.jo29;
			break;
		case 30:
			q = R.drawable.jo30;
			break;
		case 31:
			q = R.drawable.jo31;
			break;
		case 32:
			q = R.drawable.jo32;
			break;
		case 33:
			q = R.drawable.jo33;
			break;
		case 34:
			q = R.drawable.jo34;
			break;
		case 35:
			q = R.drawable.jo35;
			break;
		case 36:
			q = R.drawable.jo36;
			break;
		case 37:
			q = R.drawable.jo37;
			break;
		case 38:
			q = R.drawable.jo38;
			break;
		case 39:
			q = R.drawable.jo39;
			break;
		case 40:
			q = R.drawable.jo40;
			break;
		case 41:
			q = R.drawable.jo41;
			break;
		case 42:
			q = R.drawable.jo42;
			break;
		case 43:
			q = R.drawable.jo43;
			break;
		case 44:
			q = R.drawable.jo44;
			break;
		case 45:
			q = R.drawable.jo45;
			break;
		case 46:
			q = R.drawable.jo46;
			break;
		case 47:
			q = R.drawable.jo47;
			break;
		case 48:
			q = R.drawable.jo48;
			break;
		case 49:
			q = R.drawable.jo49;
			break;
		case 50:
			q = R.drawable.jo50;
			break;
		case 51:
			q = R.drawable.jo51;
			break;
		case 52:
			q = R.drawable.jo52;
			break;
		case 53:
			q = R.drawable.jo53;
			break;
		case 54:
			q = R.drawable.jo54;
			break;
		case 55:
			q = R.drawable.jo55;
			break;
		case 56:
			q = R.drawable.jo56;
			break;
		case 57:
			q = R.drawable.jo57;
			break;
		case 58:
			q = R.drawable.jo58;
			break;
		case 59:
			q = R.drawable.jo59;
			break;
		case 60:
			q = R.drawable.jo60;
			break;
		case 61:
			q = R.drawable.jo61;
			break;
		case 62:
			q = R.drawable.jo62;
			break;
		case 63:
			q = R.drawable.jo63;
			break;
		case 64:
			q = R.drawable.jo64;
			break;
			
		}
		return q;
	}
	}

	


