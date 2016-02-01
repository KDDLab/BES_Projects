package com.example.mo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceActivity; 
import android.preference.PreferenceManager;

@SuppressLint("NewApi")
public class prefer extends PreferenceActivity{


	
	public static String getLatLng(Context c){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
		return pref.getString("latlng","");
		
	}
	public static void setLatLng(Context c,String Lat,String Lng){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
		Editor e = pref.edit();
		String latlng = Lat+"/"+Lng;
		e.putString("latlng",latlng);
		e.commit();
	
		
	}

}

