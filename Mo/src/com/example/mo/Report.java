package com.example.mo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Report extends Activity {
	private EditText OtherLine;
	private ImageButton Pre ;
	private Button next, call;
	private String UI;
	private Message msg;
	private String SocketOther1, SocketOther2;
	private Set<String> QueryStation = new HashSet<String>();
	private String other1[] = new String[]{
			"新北市"
	};
	
	private Handler Headlermsg = new Handler(){
		
	    @Override
	    public void handleMessage(Message msg) {
	      
		 switch(msg.what){
		 case 0:
			Toast.makeText(Report.this,"成功送出!", Toast.LENGTH_SHORT).show();
			Report.this.finish();
		 }
		 super.handleMessage(msg);
	 }
 }; 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		view();
	
		Butt();
		


	}
	private void LoadingDB( int arg) {
		DB  mD = new DB(this); 
		 mD.open();
		 Cursor  cR = mD.getALL();
		 SocketOther1 = null;
		final ArrayList<String> spinner = new ArrayList<String>();
		QueryStation.clear();
		char Cou1;
		char Cou2;
		
	       Cou1 = other1[arg].charAt(0);
	       Cou2 = other1[arg].charAt(1);
	       if(cR.getCount() != 0){
		 while(cR.moveToNext()){
			    if(Cou1 == cR.getString(1).charAt(0)){
			    	if( Cou2 == cR.getString(1).charAt(1)){
			    		QueryStation.add(cR.getString(2));	
			    		
			    	}
			    }
			 		 
		}
	       }
		   cR.close();   
		   mD.close();
		   
		 spinner.addAll(QueryStation);
		 if(spinner.isEmpty()){
			 next.setEnabled(false);
		 }else{
			 next.setEnabled(true);
		 }
		 Spinner sp2 = (android.widget.Spinner) findViewById(R.id.spinner2);
		 ArrayAdapter<String> ad2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, spinner);
			ad2.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
			sp2.setAdapter(ad2);
			SocketOther1 = other1[arg];
			sp2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
	
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					SocketOther2 = spinner.get(arg2);
						
				}	
			});
		 }
	private void Butt() {
		
		SocketOther1 = "null";
		SocketOther2 = "null";
		call.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("tel:0222989311");
				Intent in  = new Intent(Intent.ACTION_DIAL,uri);
				startActivity(in);	
			}
		});
	Pre.setOnClickListener(new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			Report.this.finish();	
		}
	});
	
	 Spinner sp1 = (android.widget.Spinner) findViewById(R.id.spinner1);
	 ArrayAdapter<String> ad1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, other1);
		ad1.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
		sp1.setAdapter(ad1);
		sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
		
			}

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				LoadingDB(arg2);	

				
				
			}	
		});
		

		
		
		
		
	next.setOnClickListener(new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!OtherLine.getText().toString().equals("")){
				if(CheckNetWrok()){
					
					final ProgressDialog Pr = ProgressDialog.show(Report.this,"回報問題中","請稍等正在回報問題中......",true);
					new Thread(){
						public void run(){
							try{
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

								Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間

								String str = formatter.format(curDate);
							String reportString = SocketOther1+"/"+SocketOther2+"/"+getOtherline()+"/"+str;
							sleep(500);
							Updata.upReport(reportString);
							sleep(1000);
							   msg = new Message();
					 			msg.what = 0;
					 			Headlermsg.sendMessage(msg);
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								Pr.cancel();
							}
						}
					}.start();
					
				

					
				}else{
					Toast.makeText(Report.this,"要開啟網路連接才可以進行問題回報", Toast.LENGTH_SHORT).show();
				}
				
				
	}else{
	Toast.makeText(Report.this,"須將必填寫故障內容才能回報", Toast.LENGTH_SHORT).show();
	} 
		}

		private String getOtherline() {
			if(!OtherLine.getText().toString().equals("")){
				return OtherLine.getText().toString();
			}else{
			return "null";
			}
		}
		
	});
	
	
	
	}

	public boolean CheckNetWrok(){
		ConnectivityManager H = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = H.getActiveNetworkInfo();
		if(info != null){
			return true;
		}
		return false;
	}
		
	private void view() {
		call = (Button) findViewById(R.id.button2);
		Pre = (ImageButton) findViewById(R.id.Pre);
		next = (Button) findViewById(R.id.button1);
		OtherLine = (EditText) findViewById(R.id.editText1);

	}

	

}
