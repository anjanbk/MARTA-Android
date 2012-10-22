package com.karanam.marta;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Displays all stations in an alphabetical ListView.
 * @author AKaranam
 *
 */
public class StationListActivity extends ListActivity {
	private PopupWindow pw;
	/**
	 * We copy the public Array and use the copy so that sorting it won't affect the original.
	 */
	private void populateList() {
		ArrayList<String> localStationArray = new ArrayList<String>();
		for (int i = 0; i< SystemData.stationArray.size(); i++)
			localStationArray.add(SystemData.stationArray.get(i).name);
		/*
		for (String station : SystemData.stationArray) {
			localStationArray.add(station);
		}*/
		
		Collections.sort(localStationArray);
		
		ListView lv = (ListView)findViewById(android.R.id.list);
		String[] stationList = new String[localStationArray.size()];
		stationList = localStationArray.toArray(stationList);
		lv.setAdapter(new ScheduleListAdapter<String>(this, R.layout.list_item, stationList));
		lv.setOnItemClickListener(new ScheduleItemClickListener<String>());
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		
		populateList();
	}
	
	/**
	 * Opens a pop-up window containing the passed station's next north/south/east/west-bound trains wherever applicable.
	 * @param station
	 */
	private void initiatePopupWindow() {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View layout = inflater.inflate(R.layout.train_popup, (ViewGroup)findViewById(R.id.train_popup_layout));
		
		// Create pop-up whose width is 98% of the screen width
		DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);
		int width = (int) (0.98 * display.widthPixels);
		
		pw = new PopupWindow(layout, width, 500, true);
		
		// Initialize Layout variables
		RelativeLayout nbContainer, sbContainer, ebContainer, wbContainer;
		TextView header, nb, sb, eb, wb;
		Button stationButton = (Button)layout.findViewById(R.id.train_popup_button1);
		Button backButton = (Button)layout.findViewById(R.id.train_popup_button2);
		
		stationButton.setOnClickListener(new PopUpButtonListener());
		backButton.setOnClickListener(new PopUpButtonListener());
		
		header = (TextView)layout.findViewById(R.id.train_popup_header);
        
        nb = (TextView)layout.findViewById(R.id.train_popup_northbound);
		sb = (TextView)layout.findViewById(R.id.train_popup_southbound);
		eb = (TextView)layout.findViewById(R.id.train_popup_eastbound);
		wb = (TextView)layout.findViewById(R.id.train_popup_westbound);
		
		nbContainer = (RelativeLayout)layout.findViewById(R.id.train_popup_northbound_container);
		sbContainer = (RelativeLayout)layout.findViewById(R.id.train_popup_southbound_container);
		ebContainer = (RelativeLayout)layout.findViewById(R.id.train_popup_eastbound_container);
		wbContainer = (RelativeLayout)layout.findViewById(R.id.train_popup_westbound_container);
		
		header.setText(StationManager.station.getName());
		
		// By default, all are invisible until the station information validates otherwise
		for (int i = 0; i < StationManager.station.getLines().size(); i++) {
			if (StationManager.station.getLines().get(i).getName().equals("Red") || StationManager.station.getLines().get(i).getName().equals("Gold")) {
				nbContainer.setVisibility(View.VISIBLE);
				sbContainer.setVisibility(View.VISIBLE);
			}
			
			if (StationManager.station.getLines().get(i).getName().equals("Green") || StationManager.station.getLines().get(i).getName().equals("Blue")) {
				ebContainer.setVisibility(View.VISIBLE);
				wbContainer.setVisibility(View.VISIBLE);
			}
		}
		
		
		
		// TEMP
		nb.setText("5 minutes to Doraville");
		nb.setTextColor(Color.YELLOW);
		sb.setText("3 minutes to Airport");
		sb.setTextColor(Color.RED);
		
		pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}
	
	private class PopUpButtonListener implements OnClickListener {
		public void onClick(View v) {
			pw.dismiss();
			switch (v.getId()) {
			case R.id.train_popup_button1:
				Intent intent = new Intent(StationListActivity.this, StationActivity.class);
		    	startActivity(intent);
		    	break;
			}
		}
	}
	
	
	private class ScheduleItemClickListener<T> implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try {
				JSONParser parser = new JSONParser(getApplicationContext());
				StationManager.station = parser.parse(((TextView)view).getText().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			initiatePopupWindow();
		}
		
	}
	
	private class ScheduleListAdapter<T> extends ArrayAdapter<String> {
		public ScheduleListAdapter(Context context, int resource, String[] list) {
			super(context, resource, list);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			return view;
		}
	}
}
