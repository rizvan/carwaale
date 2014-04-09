package com.example.madsublocator;

import com.carwaale.taximate.R;
import com.carwaale.taximate.R.layout;
import com.carwaale.taximate.R.menu;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class WaitForDriver extends Activity {
TextView v5;
TextView v2;
TextView v3;
TextView v4;
TextView v6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wait_for_driver);
		v2=(TextView)findViewById(R.id.textView2);
		v3=(TextView)findViewById(R.id.textView3);
		v4=(TextView)findViewById(R.id.textView4);
		v5=(TextView)findViewById(R.id.textView5);
		v6=(TextView)findViewById(R.id.textView6);
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
	
	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wait_for_driver, menu);
		return true;
	}

}
