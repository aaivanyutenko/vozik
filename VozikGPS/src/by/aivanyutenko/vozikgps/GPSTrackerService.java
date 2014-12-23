package by.aivanyutenko.vozikgps;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

public class GPSTrackerService extends IntentService {

	public GPSTrackerService() {
		super("GPSTrackerService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				int id_agent = 1;
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				float speed = location.getSpeed();
				long timestamp = new Date().getTime();
				final String url = String.format(Locale.getDefault(), "http://5.231.39.118:8000/track?id_agent=%d&latitude=%f&longitude=%f&speed=%f&timestamp=%d", id_agent, latitude, longitude, speed, timestamp);
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						try {
							HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
							con.getResponseCode();
						} catch (MalformedURLException e) {
						} catch (IOException e) {
						}
						return null;
					}
				}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void []) null);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
	}

}
