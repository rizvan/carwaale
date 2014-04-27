package com.example.madsublocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.carwaale.taximate.R;

public class WaitForDriver extends Activity {
TextView v5;
TextView v2;
TextView v3;
TextView v4;
TextView v6;
TextView d;
Double addLat;
Double addLng;
Button submit;
ProgressDialog progress;
Context v;
String result;
InputStream is;
String driverDetails="dr";
int flagDriver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wait_for_driver);
		v=this;
		progress = new ProgressDialog(this);
d=(TextView)findViewById(R.id.d);
		v2=(TextView)findViewById(R.id.textView2);
		v3=(TextView)findViewById(R.id.textView3);
		v4=(TextView)findViewById(R.id.textView4);
		v5=(TextView)findViewById(R.id.textView5);
		v6=(TextView)findViewById(R.id.textView6);
		submit=(Button)findViewById(R.id.submit);
		Bundle bundle = getIntent().getExtras();
if(bundle.getString("now").equals("1"))
{
	System.out.println("send taxi right now");
	}
else
{
		String date= bundle.getString("date");
		String year=bundle.getString("year");
		String month= bundle.getString("month");
		String hour=bundle.getString("hour");
		String min= bundle.getString("min");
		v2.setText(date);
		v3.setText(month);
		v4.setText(year);
		v5.setText(min);
		v6.setText(hour);
		System.out.println(hour);
System.out.println(min);
System.out.println(month);
System.out.println(date);
System.out.println(year);
getLatLng();
getLatLng();
getLatLng();
	
	}

	submit.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			progress.setMessage("Loading...");
			new check(progress).execute();
		}
	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wait_for_driver, menu);
		return true;
	}
	
	void getLatLng()
	{
		Geocoder geocoder = new Geocoder(getBaseContext());  
		List<Address> addresses;
		try {
		   addresses = geocoder.getFromLocationName("Example StreeT, UK, DNFE", 20);

		   for(int i = 0; i < addresses.size(); i++) { // MULTIPLE MATCHES

		     Address addr = addresses.get(i);

		      addLat = addr.getLatitude();
		      addLng = addr.getLongitude(); // DO SOMETHING WITH VALUES
System.out.println("addLat : "+addLat);
		   }

		}
		catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("catch latlngAdd");
	    }
	}
	
	private class check extends AsyncTask<Void,Void,Void> {
		 ProgressDialog progress1;
		 /////////
		 //constructor purpose:Loading signal
		 /////////
		 public check(ProgressDialog progress2) {
			    
				this.progress1 = progress2;
			    progress=progress1;
			  }
		 @Override
	        protected void onPreExecute(){
			 progress.show();
			 System.out.println("pre-excution");
		 }
		

		 @Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub

			 try {
				 System.out.println("background");
				 HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://kidiyoor.site88.net/resfeber/checkDriver.php");
			   System.out.println("connect to website to checkDriver");
			   
			     // Add your data
		       List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		            nameValuePairs.add(new BasicNameValuePair("custNo", "1"));
		            
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        // Execute HTTP Post Request
			   	        HttpResponse response = httpclient.execute(httppost);
		      
			   	     System.out.println("Done notifiying");
		       HttpEntity entity = response.getEntity();
	    	  is = entity.getContent();
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    	System.out.println("catch1");
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    	System.out.println("catch2");
		    }
			 System.out.println("in download");
				result = null;
				try{
						BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				           StringBuilder sb = new StringBuilder();
				           String line = null;
				           int y=0;
				           while ((line = reader.readLine()) != null&&y==0) {
				                   sb.append(line + "\n");y=1;
				                  
				           }
				        
				           is.close();
				           result  = sb.toString();
				          
				           System.out.println("end of first half");
				   }catch(Exception e){
				           Log.e("log_tag", "Error converting result "+e.toString());
				           System.out.println("caught");
				           
				   }
				
				try{
					System.out.println("After download ");
					
				JSONArray jArray = new JSONArray(result);
			
				  int y = jArray.length();
		
				  System.out.println("y="+y);


				JSONObject json_data = jArray.getJSONObject(0);
				System.out.println("one oject recieved");
				flagDriver=Integer.parseInt(json_data.getString("request"));
				driverDetails=json_data.getString("driver");
				
				System.out.println("recived :"+flagDriver+":"+driverDetails);
				
				} catch (JSONException e) {System.out.println("caught e");
			     
			 }
				
				return null;
			}
		 @Override
		 protected void onPostExecute(Void result){
			 d.setText(flagDriver+":"+driverDetails);
			 progress.dismiss();
	    	 
		 }
		
		
	 }
}
