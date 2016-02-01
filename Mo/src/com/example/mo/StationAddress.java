package com.example.mo;

public class StationAddress {
	public   String id;
	public   String city_name;
	public   String region_Name;
	public   String Stattion_Name;
	public   String Station_ADDRESS;
	public   String Station_LNG;
	public   String Station_LAT;
	public   String Battery_count;
	public   String Status;
	


	public StationAddress(String id ,String city_name, String region_Name, String  Stattion_Name,String Station_ADDRESS, String Station_LNG
			 ,String Station_LAT,String Battery_count,String Status) {
		this.id = id;
		this.city_name = city_name;
		this.region_Name = region_Name;
		this.Stattion_Name =  Stattion_Name;
		this.Station_ADDRESS = Station_ADDRESS;
		this.Station_LNG = Station_LNG;
		this.Station_LAT = Station_LAT;
		this.Battery_count = Battery_count;
		this.Status = Status;

	}
	public String id(){
		return id;
	}
	public String  getaddress(){
		return Station_ADDRESS;
	}
	public double getlat(){
		return Double.valueOf(Station_LAT);
	}
	public double  getlng(){
		return Double.valueOf(Station_LNG);
	}


}
