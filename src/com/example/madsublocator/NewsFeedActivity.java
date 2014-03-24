package com.example.madsublocator;

import android.app.Activity;
import com.carwaale.taximate.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class NewsFeedActivity extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// storing string resources into Array
		String[] absence_information = getResources().getStringArray(
				R.array.absence_info);

		// Binding resources Array to ListAdapter
		this.setListAdapter(new ArrayAdapter<String>(this,
				R.layout.absence_list, R.id.label, absence_information));
	}
}
