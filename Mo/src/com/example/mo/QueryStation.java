package com.example.mo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QueryStation extends Activity {
private DB mD;
private Cursor cR;
private Button next;
private ImageButton last;
private Intent in;
private String UI;
private RadioGroup mRadioGroup1; 
private Bundle bu = new Bundle();
private RadioButton mRadio1,mRadio2; 
private String mode;
private Set<String> QueryStation = new HashSet<String>();
private Double lat;
private Double lng;
private String Counties [] = new String[]{				   
		   "新北市",

};
private String Mode [] = new String[]{
		"電池排序",
	    "距離排序"
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_station);
		String latlng[] = prefer.getLatLng(this).split("/");
		 lat = Double.parseDouble(latlng[0]);
		 lng = Double.parseDouble(latlng[1]);
         
	
	View();
	 mode = Mode[0];
	next.setEnabled(false);
	last.setOnClickListener(new Button.OnClickListener(){
		@Override
		public void onClick(android.view.View v) {
			QueryStation.this.finish();
			
		} 
		
	});
	
			
				 Spinner sp1 = (android.widget.Spinner) findViewById(R.id.spinner1);
				 ArrayAdapter<String> ad1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, Counties);
					ad1.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
					sp1.setAdapter(ad1);
					sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
						}
  
						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							LoadingDB(arg2);	
						}	
					});
				    mRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
				    		  {
					    @Override 
					    public void onCheckedChanged(RadioGroup group, int checkedId)
					    {  
					      // TODO Auto-generated method stub 
					      if(checkedId==mRadio1.getId())
					      { 
					    	mode = Mode[0];
					    	  bu.putString("mode",mode); 
					          Bundel();
					        
					       
					      } 
					      else if(checkedId==mRadio2.getId()) 
					      { 
					    	  mode = Mode[1];
					    	  bu.putString("mode",mode); 
					    	  Bundel();
					    	
					    	  
					      }       
					    } 
					  }); 
	  } 

	
	protected void onResume(){
		super.onResume();
		String latlng[] = prefer.getLatLng(this).split("/");
		 lat = Double.parseDouble(latlng[0]);
		 lng = Double.parseDouble(latlng[1]);
		
	}

		 
	private void View() {
		last = (ImageButton) findViewById(R.id.Previous);
		next = (Button) findViewById(R.id.Inquiry);
		  mRadioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		    mRadio1 = (RadioButton) findViewById(R.id.radio0);
		    mRadio2 = (RadioButton) findViewById(R.id.radio1); 
	}
	private void LoadingDB( int arg) {
		 mD = new DB(this); 
		 mD.open();
	     cR = mD.getALL();
		UI = null;
		final ArrayList<String> spinner = new ArrayList<String>();
		QueryStation.clear();
		char Cou1 ;
		char Cou2;
		
	       Cou1 = Counties[arg].charAt(0);
	       Cou2 = Counties[arg].charAt(1);
	       if(cR.getCount() != 0){
		 while(cR.moveToNext()){
			    if(Cou1 == cR.getString(1).charAt(0)){
			    	if( Cou2 == cR.getString(1).charAt(1)){
			    		QueryStation.add(cR.getString(2));	
			    		
			    	}
			    }
			 		 
		}
	       }else{  	   
	    	   Toast.makeText(QueryStation.this,"請開啟網路,然後進行更新 ",Toast.LENGTH_SHORT).show();
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
			 UI= Counties[arg];
			sp2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					
						in = new Intent();
						in.setClass(QueryStation.this,MAPStatuon.class);
						
						bu.putString("Counties",UI);
						bu.putString("region",spinner.get(arg2));
						
				    	  bu.putString("mode",mode); 
				          Bundel();
 
						next.setOnClickListener(new Button.OnClickListener(){
                                  
							@Override
							public void onClick(android.view.View v) {
								if(mode.equals("距離排序")){
									if(lat != 0){
									startActivity(in);					
									}else{
										Toast.makeText(QueryStation.this,"無法使用此功能,請在手機設定裡開起定位服務後,重啟程式", Toast.LENGTH_SHORT).show();
									}
									}else{
										startActivity(in);
									}
								}
						});
				}	 
			});
				}
	public void Bundel(){
		in.putExtras(bu);
	}
	

}
