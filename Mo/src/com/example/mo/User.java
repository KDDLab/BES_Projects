package com.example.mo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;

public class User extends Activity {
	private ImageButton Return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		View();
		
		Return.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(android.view.View v) {
				User.this.finish();
				
			} 
			
		});
	}

	private void View() {
		Return = (ImageButton) findViewById(R.id.Return);
		
	}


}
