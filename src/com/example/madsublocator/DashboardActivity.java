package com.example.madsublocator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import library.DatabaseHandler;
import library.UserFunctions;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.carwaale.taximate.R;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.PorterDuff;



import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "NewApi", "ValidFragment" })
public class DashboardActivity extends FragmentActivity implements OnCameraChangeListener,OnMarkerDragListener{ 
	private static final String TAG = null;
	UserFunctions userFunctions;
	Button btnLogout;
	Button btnDetails;
	Button btn_newsfeed;
	Button btn_friends;
	Button myLocation;
	Button book;
	Button displayAdd;
	ProgressDialog progress;
	Calendar cal =null;
	int hour;
	int min;
	static int d;
	static int m;
	static int y;
//	Button locationButton=(Button)findViewById(R.id.getlocation);
	DatabaseHandler session;
//	Location locationtext;
	
//	final int RQS_GooglePlayServices = 1;
	private GoogleMap googleMap;
	String Address="";
	
	Double myLat;
	Double myLng;
	Context contextMain;
	CameraChange c;
	MarkerOptions addMarker;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		
		contextMain=this;
		myLat=12.77878787;
		myLng=77.77888888;
		
		c=new CameraChange();
		
		//System.out.println(getAddress(myLat,myLng));
        
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
				
				////////////
				///when user drag the screen , get the lat lng of center screen , get 
				///the address store in "Address".
				////////////
				googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				    @Override
				    public void onCameraChange(CameraPosition cameraPosition) {
				      
				    	System.out.println("oncameraChange");
				    	System.out.println("lat="+cameraPosition.target.latitude);
				    	System.out.println("lng="+cameraPosition.target.longitude);
				
				    	myLat=cameraPosition.target.latitude;
				    	myLng=cameraPosition.target.longitude;
				    	
				    	c.cancel(true);
				    	   // do your work here
				    	c=new CameraChange();
				    	c.execute();
				    	
				    	
				    	//new CameraChange().execute();
				
				    }
				});
				////////////
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

			//	googleMap.animateCamera(CameraUpdateFactory
				//		.newCameraPosition(cameraPosition));
	//			  googleMap.setOnMapClickListener(this);

			} catch (Exception e) {
				e.printStackTrace();
			}
			// TextView lblName = (TextView) findViewById(R.id.lblName);
			// user already logged in show databoard
			
			//////////
			// Adding a marker (for the center screen- help in display the address(TRY)
			//////////
			addMarker = new MarkerOptions().position(
					new LatLng(myLat,myLng))
					.title("Loading Address...");
			googleMap.addMarker(addMarker);
			addMarker.draggable(false);

			///////
	
			btnLogout = (Button) findViewById(R.id.btnLogout);
			myLocation = (Button) findViewById(R.id.myLocation);
			btnDetails = (Button) findViewById(R.id.btnDetails);
			displayAdd= (Button) findViewById(R.id.getlocation);
			book = (Button) findViewById(R.id.book);
			book.getBackground().setColorFilter(0xcccccc00, PorterDuff.Mode.MULTIPLY);
			/////////////////////
			//when you press book button
			///////
			book.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					progress = new ProgressDialog(contextMain);
					progress.setMessage("Loading...");
					new Book(progress).execute();
					AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
				    
			        builder.setTitle("Ride Time");
			    
			    builder.setPositiveButton("Now", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			        	Intent n = new Intent(DashboardActivity.this,WaitForDriver.class);
			        	n.putExtra("now", "1");
			        	startActivity(n);
			       }
			   });
			            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			           	DialogFragment newTFragment = new TimePickerFragment();
			           		newTFragment.show(getSupportFragmentManager(), "timePicker");
			           		DialogFragment newDFragment = new DatePickerFragment();
			           	 newDFragment.show(getSupportFragmentManager(), "datePicker");
			               }
			           });
			    builder.show();
				}
			});
			//////////////////
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

	@SuppressLint("NewApi")
	private void initializeMap() {
		if (googleMap == null) {
			System.out.println("googleMap was null");
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
			googleMap.setOnMarkerDragListener(this);
			googleMap.addMarker(new MarkerOptions()
	        .position(new LatLng(myLat,myLng))
	        .title("My Spot")
	        .snippet("This is my spot!")
	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true));
			
			
			 
			googleMap.setMyLocationEnabled(true);
			Criteria criteria = new Criteria();
		    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    String provider = locationManager.getBestProvider(criteria, false);
		    Location location = locationManager.getLastKnownLocation(provider);
		    Double Lat =  location.getLatitude();
		    Double Lng = location.getLongitude();
System.out.println("initiaizing .... \n my last know Loc = "+Lat+";"+Lng);			

CameraPosition cameraPosition = new CameraPosition.Builder()
.target(new LatLng(Lat,Lng)).zoom(18).build();
System.out.println("MapSet");
googleMap.animateCamera(CameraUpdateFactory
		.newCameraPosition(cameraPosition));
	
		}
		
		System.out.println("done initialinzing");

		
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
/*
	public class DrawView extends View {

		public DrawView(Context context) {
			super(context);
			setFocusable(true); // necessary for getting the touch events

			// setting the start point for the balls

		}
		

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
				System.out.println(lati+longi);
			}
			return true;
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
		    // MotionEvent reports input details from the touch screen
		    // and other input controls. In this case, you are only
		    // interested in events where the touch position changed.
			int action = event.getAction();
			// int X = (int)event.getX();
			// int Y = (int)event.getY();
			System.out.println("ontouchevent");
			if (action == MotionEvent.ACTION_MOVE) {

				double lati = googleMap.getCameraPosition().target.latitude;
				double longi = googleMap.getCameraPosition().target.longitude;
				MarkerOptions centerMarker = new MarkerOptions().position(
						new LatLng(lati, longi)).title("Hello Maps ");
				centerMarker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mappin));
				googleMap.addMarker(centerMarker);
				System.out.println(lati+longi);
				
			}
		    return true;
		}
		
	}
*/
	
	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		System.out.println("draging ...");
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		System.out.println("draged");
		System.out.println(arg0.getPosition().latitude);
		System.out.println(arg0.getPosition().longitude);
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		System.out.println("start draging");
	}


	

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		
	}
	
	///////////////
	////Method for getting address from lat lng
	/////////////
	private String getAddress(double lat,double lng)
	{
		Geocoder geocoder = new Geocoder(this,Locale.getDefault());
      String Add = null;
		try {
        	System.out.println("Trying to get address");
            List<Address> addresses = geocoder.getFromLocation(lat,
                    lng, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder(
                        "Address:\n");
                
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                                    "\n");
                }
                Add=strReturnedAddress.toString();
               System.out.println(Add);
            } else {
                System.out.println("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Canont get Address!");
        }
        System.out.println("address acquired");
        return Add;
	}
	
	///////
	///Async class for Book button
	//////
	 private class Book extends AsyncTask<Void,Void,Void> {
		 ProgressDialog progress1;
		 /////////
		 //constructor purpose:Loading signal
		 /////////
		 public Book(ProgressDialog progress2) {
			    
				this.progress1 = progress2;
			    progress=progress1;
			  }
		 
		 protected void onPreExecute(Long result) {
			 progress.show();
	     }

		 @Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
			 try {
				 HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://kidiyoor.site88.net/resfeber/sendNotification.php");
			   System.out.println("connect to website to send notification");
			   
			     // Add your data
		       List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		            nameValuePairs.add(new BasicNameValuePair("la", String.valueOf(myLat)));
		            nameValuePairs.add(new BasicNameValuePair("lo", String.valueOf(myLng)));
		          httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        // Execute HTTP Post Request
			   	        HttpResponse response = httpclient.execute(httppost);
		      
			   	     System.out.println("Done notifiying");
		       HttpEntity entity = response.getEntity();
	    	  InputStream is = entity.getContent();
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    	System.out.println("catch1");
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    	System.out.println("catch2");
		    }
				return null;
			}

		 protected void onPostExecute(Long result) {
			 progress.dismiss();
	     }

		
	 }
	 //////////////
	 ///class for oncameraChange
	 ///To get address asyc
	 //////////////
	 private class CameraChange extends AsyncTask<Void,Void,Void> {
		 ProgressDialog progress1;
		 /////////
		 //constructor purpose:Loading signal
		 /////////
		 
		 protected void onPreExecute(Void unused) {
			 System.out.println("pre execute camera");
			// displayAdd.setText("loading address...");
			// Toast.makeText( getApplicationContext(), "loading address...", Toast.LENGTH_SHORT).show();
	     }

		 @Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
			 System.out.println("in Background For address");
			// displayAdd.setText("loading address...");
			// Toast.makeText( getApplicationContext(), "loading address...", Toast.LENGTH_SHORT).show();

				Address=getAddress(myLat,myLng);
				System.out.println("Done address");
				 addMarker.snippet(Address);
				
				return null;			
				}
		 
		 @Override
		protected void onPostExecute(Void unused) {
			 System.out.println("in Post Execute Background oncam change");
			 displayAdd.setText(Address);
			//System.out.println(Address);
			 Toast.makeText( getApplicationContext(), Address, Toast.LENGTH_SHORT).show();
			 
			 addMarker.position(new LatLng(myLat,myLng));
			  addMarker.title(Address);


	     }
		
	 }
	 
	 /////////////////
	 /////onBookClick
	 /////ClassToGetTimeDate
	 /////////////////
	 @SuppressLint("ValidFragment")
	public class TimePickerFragment extends DialogFragment
		implements TimePickerDialog.OnTimeSetListener {

		@SuppressLint("NewApi")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
			Calendar c = Calendar.getInstance();
		 int hou = c.get(Calendar.HOUR_OF_DAY);
		 int minute = c.get(Calendar.MINUTE);
		 
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
		DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
			hour=hourOfDay;
			min=minute;
			Intent n = new Intent(DashboardActivity.this,WaitForDriver.class);	
			String m1=Integer.toString(m);
			String y1=Integer.toString(y);
			String d1=Integer.toString(d);
			String h1=Integer.toString(hour);
			String min1=Integer.toString(min);
			System.out.println(m1);
			System.out.println(y1);
			System.out.println(d1);
			
			n.putExtra("year", y1);
			n.putExtra( "month",m1);
			n.putExtra("date", d1);
			n.putExtra("hour", h1);
			n.putExtra("min", min1);
			n.putExtra("now", "0");
			startActivity(n);
			finish();
		}
		}
		
	public static class DatePickerFragment extends DialogFragment
	    implements DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Use the current date as the default date in the picker
	final Calendar c = Calendar.getInstance();
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);

	// Create a new instance of DatePickerDialog and return it
	return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
	// Do something with the date chosen by the user
		y=year;
		m=month+1;
		d=day;
	}
	}
}