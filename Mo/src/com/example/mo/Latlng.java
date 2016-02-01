package com.example.mo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.google.android.gms.internal.al;
import com.google.android.gms.maps.model.LatLng;

public class Latlng {
private Vector<Double> v = new Vector<Double>();
public TreeMap<Double,Integer> tr = new TreeMap<Double,Integer>();

	public LatLng  getnear (double lat, double lng){
	
	LatLng gg = null ;
	v.clear();
	ArrayList<StationAddress> ag = new ArrayList<StationAddress>();
	TreeMap<Double,Double> map = new TreeMap<Double,Double>();
		Ui aL = new Ui();
		ag = aL.aL();
		
		
		for(StationAddress as : ag){			
			map.put(Distance(lat,lng,Double.valueOf(as.getlat()),Double.valueOf(as.getlng())),0.0);

		}

		for(Map.Entry <Double, Double> y :map.entrySet()){
			v.add((Double) y.getKey());
		}
		
		for(StationAddress as: ag){
			if(Distance(lat,lng,Double.valueOf(as.getlat()),Double.valueOf(as.getlng())) ==  v.get(0)){
				double x = Double.valueOf(as.getlat());
				double y = Double.valueOf(as.getlng());
				gg = new LatLng(x,y);
				break;
			}
		}

	
		
		return gg;

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

}
