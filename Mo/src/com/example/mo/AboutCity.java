package com.example.mo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AboutCity extends Activity {
private ImageButton Return;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_city);
		Return = (ImageButton) findViewById(R.id.Return); 
		
		Return.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				AboutCity.this.finish();
				
			}
			
		});
	}



}
