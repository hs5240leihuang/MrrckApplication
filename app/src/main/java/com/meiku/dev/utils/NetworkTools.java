package com.meiku.dev.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkTools {

	public static boolean isNetworkAvailable(Context context){
		if(context == null) {
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager != null){
			NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
			if(infos != null){
				for(NetworkInfo info:infos){
					if(info.getState() == NetworkInfo.State.CONNECTED)
						return true;
				}				
			}
		}
		return false;
	}
	
	public static boolean isWiFiNetworkAvailable(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager != null){
			NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
			if(infos != null){
				for(NetworkInfo info:infos){
					if(info.getType() == ConnectivityManager.TYPE_WIFI)
						return true;
				}				
			}
		}
		return false;
	}
	
	public static String getIpAddress(Context context){
		String ips = "";
		
		try {
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
				NetworkInterface ni = en.nextElement();
				for(Enumeration<InetAddress> en1 = ni.getInetAddresses(); en1.hasMoreElements();){
					InetAddress ia = en1.nextElement();
					if(!ia.isLoopbackAddress()){
						ips = ia.getHostAddress().toString() + ";" + ips;
					}
				}
			}
		} catch (SocketException e) {
		}
		
		return ips;
	}
	
	
	
	public static boolean isOpenGPSSettings(Activity context) {
		LocationManager alm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		}

		return false;
	}
}
