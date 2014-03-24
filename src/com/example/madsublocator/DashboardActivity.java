package com.example.madsublocator;

import library.DatabaseHandler;
import library.UserFunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carwaale.taximate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class DashboardActivity extends Activity{ 
	UserFunctions userFunctions;
	Button btnLogout;
	Button btnDetails;
	Button btn_newsfeed;
	Button btn_friends;
	Button myLocation;
//	Button locationButton=(Button)findViewById(R.id.getlocation);
	DatabaseHandler session;
//	Location locationtext;
	
//	final int RQS_GooglePlayServices = 1;
	private GoogleMap googleMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Dashboard Screen for the application
		 * */
		// Check login status in database
		userFunctions = new UserFunctions();
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			session = new DatabaseHandler(getApplicationContext());
			setContentView(R.layout.dashboard_layout);
			try {
				// Loading map
				initializeMap();
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				googleMap.setMyLocationEnabled(true);
				// Enable / Disable zooming controls
				googleMap.getUiSettings().setZoomControlsEnabled(false);

				// Enable / Disable my location button
				googleMap.getUiSettings().setMyLocationButtonEnabled(false);

				// Enable / Disable Compass icon
				googleMap.getUiSettings().setCompassEnabled(true);

				// Enable / Disable Rotate gesture
				googleMap.getUiSettings().setRotateGesturesEnabled(false);

				// Enable / Disable zooming functionality
				googleMap.getUiSettings().setZoomGesturesEnabled(true);

				double latitude = 12.9667;
				double longitude = 77.5667;

				// lets place some 10 random markers
				for (int i = 0; i < 10; i++) {
					// random latitude and logitude
					double[] randomLocation = createRandLocation(latitude,
							longitude);

					// Adding a marker
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(randomLocation[0], randomLocation[1]))
							.title("Hello Maps " + i);

					Log.e("Random", "> " + randomLocation[0] + ", "
							+ randomLocation[1]);

					// changing marker color
					marker.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.taximarker));
					googleMap.addMarker(marker);

					// Move the camera to last position with a zoom level

				}
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(googleMap.getMyLocation()
								.getLatitude(), googleMap.getMyLocation()
								.getLongitude())).zoom(15).build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
	//			  googleMap.setOnMapClickListener(this);

			} catch (Exception e) {
				e.printStackTrace();
			}
			// TextView lblName = (TextView) findViewById(R.id.lblName);
			// user already logged in show databoard

			btnLogout = (Button) findViewById(R.id.btnLogout);
			myLocation = (Button) findViewById(R.id.myLocation);
			btnDetails = (Button) findViewById(R.id.btnDetails);
			btnLogout.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					userFunctions.logoutUser(getApplicationContext());
					Intent login = new Intent(getApplicationContext(),
							LoginActivity.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(login);
					// Closing dashboard screen
					finish();
				}
			});
			

			btnDetails.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// Launching News Feed Screen
					Intent i = new Intent(getApplicationContext(),
							UserDetails.class);
					startActivity(i);
				}
			});
			myLocation.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(googleMap.getMyLocation()
									.getLatitude(), googleMap.getMyLocation()
									.getLongitude())).zoom(15).build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			});

		} else {
			// user is not logged in show login screen
			Intent login = new Intent(getApplicationContext(),
					LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
		}
	}

	private void initializeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	protected void onResume() {
		super.onResume();
		initializeMap();
	}
	/*
	 @Override
	 public void onMapClick(LatLng point) {
	  locationButton.setText("number");
	  Log.e("onDisabled","info");
	 }
*/
	private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 10),
				longitude + ((Math.random() - 0.5) / 10),
				150 + ((Math.random() - 0.5) * 10) };
	}

	public class DrawView extends View {

		public DrawView(Context context) {
			super(context);
			setFocusable(true); // necessary for getting the touch events

			// setting the start point for the balls

		}
		
/*
		public boolean onTouchEvent(MotionEvent event) {
			int action = event.getAction();
			// int X = (int)event.getX();
			// int Y = (int)event.getY();
			if (action == MotionEvent.ACTION_MOVE) {

				double lati = googleMap.getCameraPosition().target.latitude;
				double longi = googleMap.getCameraPosition().target.longitude;
				MarkerOptions centerMarker = new MarkerOptions().position(
						new LatLng(lati, longi)).title("Hello Maps ");
				centerMarker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mappin));
				googleMap.addMarker(centerMarker);
			}
			return true;
		}
		*/
	}
}