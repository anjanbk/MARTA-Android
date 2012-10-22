package com.karanam.marta;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	private void initializeTabs() {
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.clearAllTabs();
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent(this, MapActivity.class);
		spec = tabHost.newTabSpec("MapTab")
					.setIndicator("System Map")
					.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent(this, StationListActivity.class);
		spec = tabHost.newTabSpec("ListTab")
					.setIndicator("Station List")
					.setContent(intent);
		tabHost.addTab(spec);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);
		initializeTabs();
	}
}
