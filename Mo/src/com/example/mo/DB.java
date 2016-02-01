package com.example.mo;


import java.io.UnsupportedEncodingException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DB {	
	public static  String DATABASE_TABLE = "BASStaion";
	public static  String Station_id = "id";
	public static  String city_name = "city_name";
	public static  String region_Name= "region_name";	
	public static  String Stattion_Name = "bes_station_name";
	public static  String Station_ADDRESS= "address";

	public static  String Station_LNG = "gps_longitude";
	public static  String Station_LAT = "gps_latitude";	
	public static  String Battery_count= "battery_count";
	public static  String Status = "bes_station_status";



	private Context mContext = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public DB(Context context){
		this.mContext = context;
	}
	public DB open() throws SQLException{

		dbHelper = new DatabaseHelper(mContext);
		db = dbHelper.getWritableDatabase();
		return this;
		
	}
	public void close(){
		dbHelper.close();
	}
	public void delete(){
		 db.delete(DATABASE_TABLE, null, null);
		
	}
	public boolean updata(String id, String city, String region, String Name,String ADDRESS, String LNG
			 ,String LAT,String Battery,String status){
		
		ContentValues args = new ContentValues();  
	
		args.put(city_name, city);
		args.put(region_Name, region);
		args.put(Stattion_Name, Name);
		args.put(Station_ADDRESS,ADDRESS);
		args.put(Station_LNG,LNG);
		args.put(Station_LAT, LAT);
		args.put(Battery_count, Battery);
		args.put(Status, status);
		
	   return db.update(DATABASE_TABLE, args, Station_id +"="+ id, null)>0;
		
	}
	public void create(String id, String city, String region, String Name,String ADDRESS, String LNG
			 ,String LAT,String Battery,String status){
		ContentValues args = new ContentValues();
		args.put(Station_id,id);
		args.put(city_name, city);
		args.put(region_Name, region);
		args.put(Stattion_Name, Name);
		args.put(Station_ADDRESS,ADDRESS);
		args.put(Station_LNG,LNG);
		args.put(Station_LAT, LAT);
		args.put(Battery_count, Battery);
		args.put(Status, status);
		
		db.insert(DATABASE_TABLE, null, args);
	}
	public Cursor getALL(){
		return db.rawQuery("SELECT*FROM "+ DATABASE_TABLE, null);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper{
	
		private static String DATABASE_NAME = "BatteryStation";
		private static int DATABASE_VERSION = 1;

		
		public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		 
	}


	public void onCreate(SQLiteDatabase db) {		
//		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS"+DATABASE_TABLE);
		onCreate(db);
		
	}
}
	

}