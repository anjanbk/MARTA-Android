package com.karanam.marta;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.OnTabChangeListener;

public class StationActivity extends TabActivity {
	private TabHost tabHost;
	private Spinner spinner;
	
	/**
	 * Private helper function that sets the tabs according to the selected line (selected from the spinner).
	 * @param line
	 */
	private void initializeTabs(String line) {
		Log.d("Function:", "initializeTabs()");
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.clearAllTabs();
		TabHost.TabSpec spec1, spec2;
		Intent intent = new Intent(this, TimeListActivity.class);
		
		Line l = StationManager.station.getLineByName(line.substring(0,line.indexOf(" Line")));
		
		Direction d = l.getDirection1();
		Log.d("Direction -> Line", d.getName() + "->" + l.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("Direction", d.getName());
		intent.putExtra("Line", l.getName());
		spec1 = tabHost.newTabSpec(d.getName())
					  .setIndicator(d.getName())
					  .setContent(intent);
		tabHost.addTab(spec1);
		
		
		d = l.getDirection2();
		intent = new Intent(this, TimeListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("Direction", d.getName());
		intent.putExtra("Line", l.getName());
		spec2 = tabHost.newTabSpec(d.getName())
					   .setIndicator(d.getName())
					   .setContent(intent);
		tabHost.addTab(spec2);
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String tabId) {
				Log.d("Spinner Listener", spinner.getSelectedItemId() + "");
				Log.d("Tab Listener", tabId);
			}
			
		});
	}
	
	/**
	 * Populate the spinner selector with the station's lines.
	 */
	private void initializeSpinner() {
		spinner = (Spinner)findViewById(R.id.line_selector);
		//spinner.clearFocus();
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (Line line : StationManager.station.getLines())
			adapter.add(line.getName() + " Line");
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
				Log.d("Spinner Item", pos + " selected");
				initializeTabs(parent.getItemAtPosition(pos).toString());
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// Load the first spinner item by default
				initializeTabs(parent.getItemAtPosition(0).toString());
			}
		});
	}
	
	private void initializeButton() {
		ImageButton mapButton = (ImageButton)findViewById(R.id.button1);
		mapButton.setOnClickListener(new MapButtonOnClickListener(this));
	}
	
	private class MapButtonOnClickListener implements OnClickListener {
		Context context;
		public MapButtonOnClickListener(Context context) {
			this.context = context;
		}
		
		public void onClick(View v) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle("Navigate around " + StationManager.station.getName());
			alertDialogBuilder
								.setMessage("Show station in Google Maps?")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent;
										try {
											intent = Intent.parseUri("geo:0,0?q=MARTA+Midtown+Station+Atlanta", Intent.URI_INTENT_SCHEME);
											startActivity(intent);
										} catch (URISyntaxException e) {
											e.printStackTrace();
										}
									}
									
								})
								.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
								});
			AlertDialog dialog = alertDialogBuilder.create();
			dialog.show();
		}
	}
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.overridePendingTransition(R.anim.left_to_right_transition, 0);
		setContentView(R.layout.sandbox);
		
		TextView tv = (TextView)findViewById(R.id.textView1);
		tv.setText(StationManager.station.getName());
		
		initializeButton();
		initializeSpinner();
	}
}
