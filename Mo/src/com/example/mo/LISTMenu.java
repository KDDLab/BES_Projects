package com.example.mo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LISTMenu extends Activity {
	private long exitTime = 0;
	private boolean click;
	private Bundle bu;
    private Button ReportProblem,CityPowerStation,News,User,AboutCity,
		StationInquiry,personal;
   
    private ViewFlipper vf1;
	private float oldtv;
	private boolean pagec ;
	private ImageView PageChange;
	private String fg[] = new String[]{
			"開發人員"
	};
    ArrayList<StationAddress> ag = new ArrayList<StationAddress>();
   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listmenu);
		
	
		 pagec = true;
		View();
	    ButtonInstruction();
	
		
	}
	public void updata(View v){
		new AlertDialog.Builder(LISTMenu.this)
		.setItems(fg, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			switch(which){
				case 0:
					Intent in = new Intent();
					in.setClass(LISTMenu.this,SDK.class);
					startActivity(in);
					break;
				
			}
			}
			
		}).show();
		
	}
	protected void onResume(){
		super.onResume();
		if(!cheakSQL()){
			 new AlertDialog.Builder(LISTMenu.this).setMessage("資料無法載入,可能是您的網路不穩定所造成的問題,請在網路穩定的地方重新下載")	
			 .setCancelable(false)
				.setPositiveButton("關閉程式",new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0);
						
					}
				})
				.show();
		}
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
	
	private void ButtonInstruction() {

		News.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				Uri uri = Uri.parse("http://www.citypower.com.tw/news.htm");
				Intent in  = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(in);
				
			}
		
		});
		News.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  if (event.getAction() == MotionEvent.ACTION_DOWN) {
					  News.setBackgroundColor(android.graphics.Color.parseColor("#DDDDDD"));
				  }
				  if(event.getAction() == MotionEvent.ACTION_UP){
					  News.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
				  }
				return false;
			}
			
		});


		personal.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				if(!cheakSQL()){
					 Toast.makeText(LISTMenu.this,"請開啟網路,然後重啟程式,進行一次性的載入 ",Toast.LENGTH_SHORT).show();
				}else{
				Intent in = new Intent();
				in.setClass(LISTMenu.this,Ui.class);
				startActivity(in);
				}
			}
		});
		personal.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  if (event.getAction() == MotionEvent.ACTION_DOWN) {
					  personal.setBackgroundColor(android.graphics.Color.parseColor("#DDDDDD"));
				  }
				  if(event.getAction() == MotionEvent.ACTION_UP){
					  personal.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
				  }
				return false;
			}
			
		});
		StationInquiry.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				
				if(!cheakSQL()){
					 Toast.makeText(LISTMenu.this,"請開啟網路,然後重啟程式,進行一次性的載入 ",Toast.LENGTH_SHORT).show();
				}else{
				Intent in = new Intent();
				in.setClass(LISTMenu.this,QueryStation.class);
				startActivity(in);
				}
			}
			
		});
		StationInquiry.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  if (event.getAction() == MotionEvent.ACTION_DOWN) {
					  StationInquiry.setBackgroundColor(android.graphics.Color.parseColor("#DDDDDD"));
				  }
				  if(event.getAction() == MotionEvent.ACTION_UP){
					  StationInquiry.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
				  }
				return false;
			}
			
		});
		ReportProblem.setOnClickListener(new Button.OnClickListener(){
			
				 				@Override 
								public void onClick(android.view.View v) {
				 					if(!cheakSQL()){
				 						 Toast.makeText(LISTMenu.this,"請開啟網路,然後重啟程式,進行一次性的載入 ",Toast.LENGTH_SHORT).show();
				 					}else{
				 					Intent in = new Intent();
				 					in.setClass(LISTMenu.this,Report.class);
									startActivity(in);
				 					}
								}
							});
		ReportProblem.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  if (event.getAction() == MotionEvent.ACTION_DOWN) {
					  ReportProblem.setBackgroundColor(android.graphics.Color.parseColor("#DDDDDD"));
				  }
				  if(event.getAction() == MotionEvent.ACTION_UP){
					  ReportProblem.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
				  }
				return false;
			}
			
		});
		User.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				Intent in = new Intent();
				in.setClass(LISTMenu.this,User.class);
				startActivity(in);
				
			}
			
		});

		User.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  if (event.getAction() == MotionEvent.ACTION_DOWN) {
					  User.setBackgroundColor(android.graphics.Color.parseColor("#DDDDDD"));
				  }
				  if(event.getAction() == MotionEvent.ACTION_UP){
					  User.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
				  }
				return false;
			}
			
		});
		CityPowerStation.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				Uri uri = Uri.parse("http://www.citypower.com.tw/");
				Intent in  = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(in);
			}
		});
		CityPowerStation.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  if (event.getAction() == MotionEvent.ACTION_DOWN) {
					  CityPowerStation.setBackgroundColor(android.graphics.Color.parseColor("#DDDDDD"));
				  }
				  if(event.getAction() == MotionEvent.ACTION_UP){
					  CityPowerStation.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
				  }
				return false;
			}
			
		});
		AboutCity.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				Intent in = new Intent();
				in.setClass(LISTMenu.this,AboutCity.class);
				startActivity(in);
				
			}
			
		});
		AboutCity.setOnTouchListener(new Button.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  if (event.getAction() == MotionEvent.ACTION_DOWN) {
					  AboutCity.setBackgroundColor(android.graphics.Color.parseColor("#DDDDDD"));
				  }
				  if(event.getAction() == MotionEvent.ACTION_UP){
					  AboutCity.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));
				  }
				return false;
			}
			
		});
	}
	
	private void View() {
		News = (Button) findViewById(R.id.button1);
		
		AboutCity = (Button) findViewById(R.id.button7);
		User= (Button) findViewById(R.id.button5);
		ReportProblem = (Button) findViewById(R.id.button4);
		CityPowerStation = (Button) findViewById(R.id.button6);
		StationInquiry = (Button) findViewById(R.id.button3);
		personal = (Button) findViewById(R.id.button2);
	
		 
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	    	new AlertDialog.Builder(LISTMenu.this)
			.setMessage("你確定要關閉?")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent in = new Intent();
					setResult(RESULT_OK,in);
					LISTMenu.this.finish();	
					System.exit(0); 
					}
				
			}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				
					
				}
			}).show();
	    

	    return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
