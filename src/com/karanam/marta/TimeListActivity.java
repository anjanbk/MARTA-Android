package com.karanam.marta;

import java.util.*;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class TimeListActivity extends ListActivity {
	private String line, direction;
	private void populateList(String lineName, String direction) {
		ArrayList<Calendar> times;
		ArrayList<String> timeStrings = new ArrayList<String>();
		
		Log.d("Function: ", "populateList()");
		Log.d("Direction Extra", direction);
		Log.d("Line Extra", lineName);
		
		Line currentLine = StationManager.station.getLineByName(lineName);
		Log.d("Fetched Line: ", currentLine.getName());
		
		if (direction.equals("Northbound") || direction.equals("Eastbound"))
			times = currentLine.getDirection1().getWeekday().getTimes();
		else
			times = currentLine.getDirection2().getWeekday().getTimes();
		
		for (Calendar c : times) {
			timeStrings.add(CalendarUtils.getStringFromTime(c));
		}
		
		String[] timesArr = new String[times.size()];
		timesArr = timeStrings.toArray(timesArr);
		
		ListView lv = (ListView)findViewById(android.R.id.list);
		lv.setAdapter(new ScheduleListAdapter<Calendar>(this, R.layout.list_item, timesArr));
		lv.setOnItemClickListener(new ScheduleItemClickListener<String>(this));
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		
		line = getIntent().getStringExtra("Line");
		direction = getIntent().getStringExtra("Direction");
		Log.d("List Activity",  line + "->" + direction);
		populateList(line, direction);
	}
	
	public void onResume() {
		super.onResume();
		Log.d("Activity", "RESUMED");
		line = getIntent().getStringExtra("Line");
		direction = getIntent().getStringExtra("Direction");
		Log.d("Extras", getIntent().getStringExtra("Line") + "->" + getIntent().getStringExtra("Direction"));
		populateList(line, direction);
	}
	
	private class ScheduleItemClickListener<T> implements OnItemClickListener {
		Context context;
		public ScheduleItemClickListener(Context context) {
			this.context = context;
		}
		
		public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle("Set Train Reminder");
			alertDialogBuilder
								.setMessage("Add this train to your Calendar?")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(Intent.ACTION_EDIT);
										intent.setType("vnd.android.cursor.item/event");
										Calendar c = CalendarUtils.getTimeFromString(((TextView)view).getText().toString());
										intent.putExtra("beginTime", c.getTimeInMillis());
										intent.putExtra("title", "MARTA " + line + " Line " + direction);
										startActivity(intent);
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
