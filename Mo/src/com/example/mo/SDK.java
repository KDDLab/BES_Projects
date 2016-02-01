package com.example.mo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SDK extends Activity {
private ImageButton Jo ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdk);
		Jo = (ImageButton) findViewById(R.id.Previous);
		Jo.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
			SDK.this.finish();
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sdk, menu);
		return true;
	}

}
