package com.example.madsublocator;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carwaale.taximate.R;

import library.DatabaseHandler;
//import library.DatabaseHandler;
import library.UserFunctions;

public class UserDetails extends Activity {
	UserFunctions userFunctions;
	Button btn_newsfeed;
	Button btn_friends;
	DatabaseHandler session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Check login status in database
		userFunctions = new UserFunctions();

		session = new DatabaseHandler(getApplicationContext());
		setContentView(R.layout.userdetails);
		TextView lblName = (TextView) findViewById(R.id.lblName);
		// user already logged in show databoard

		// btnLogout = (Button) findViewById(R.id.btnLogout);
		btn_newsfeed = (Button) findViewById(R.id.btn_news_feed);
		btn_friends = (Button) findViewById(R.id.btn_friends);

		HashMap<String, String> user = session.getUserDetails();

		// name
		String name = user.get(DatabaseHandler.KEY_NAME);

		// email
		// String email = user.get(DatabaseHandler.KEY_EMAIL);

		// displaying user data
		lblName.setText(Html.fromHtml("<b>Hola! " + name + "</b><br>"));

		btn_newsfeed.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(),
						NewsFeedActivity.class);
				startActivity(i);
			}
		});

		// Listening Friends button click

		btn_friends.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(),
						FriendsActivity.class);
				startActivity(i);
			}
		});
	}
}