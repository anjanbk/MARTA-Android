package com.karanam.marta;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.os.*;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.widget.*;

/**
 * Map screen. Displays the MARTA rail system map. User can drag the image around, as well as pinch zoom.
 * User can also click on a station for more options.
 * @author AKaranam
 *
 */
public class MapActivity extends Activity implements OnTouchListener {
	static final boolean DEBUG = false;
	ArrayList<Integer> hashkeys;
	static final float DRAG_TOLERANCE = 5f;
	float TAP_TOLERANCE = 0.01f; // Not static, as it changes while zooming
	
	Matrix matrix, savedMatrix;
	Bitmap bitmap;
	PointF start, mid;
	Canvas c;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	boolean TAPPED = false;
	float oldDist;
	int mode;
	
	Point leftCorner;
	float[] imageDimensions;
	
	JSONParser parser;
	
	PopupWindow pw;
	
	public void initialize() {
		mode = NONE;
		matrix = new Matrix();
		savedMatrix = new Matrix();
		start = new PointF();
		mid = new PointF();
		oldDist = 1f;
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marta_map);
		c = new Canvas(bitmap.copy(Bitmap.Config.ARGB_8888, true));
		leftCorner = new Point();
		imageDimensions = new float[2];
			//parser = new JSONParser(new InputStreamReader(getResources().getAssets().open("marta.json")));
			parser = new JSONParser(this);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	this.overridePendingTransition(R.anim.right_to_left_transition, 0);
    	initialize();
        setContentView(R.layout.map);
        
        // Initialize layout views
        ImageView iv = (ImageView)findViewById(R.id.marta_map);
        
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        matrix = iv.getImageMatrix();
        
        iv.setOnTouchListener(this);
        Log.d("Status", "Listening...");
    }
    
    /**
     * Stores the image's absolute dimensions (taking into account scaling) in the float array parameter.
     * @param dst
     */
    public void getScaledImageDimensions(float[] dst) {
    	float[] values = new float[9];
    	matrix.getValues(values);

    	dst[0] = values[0] * bitmap.getWidth();
    	dst[1] = values[4] * bitmap.getHeight();
    }
    
    public Point getScaledImagePosition() {
    	Point pos = new Point();
    	float[] values = new float[9];
    	
    	matrix.getValues(values);
    	pos.x = (int)values[2];
    	pos.y = (int)values[5];
    	return pos;
    }

	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView)v;
		//dumpEvent(event);
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
         
			if (DEBUG)
				Log.d("Touch mode", "mode=DRAG");
         
			mode = DRAG;
			TAPPED = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			
			if (DEBUG)
				Log.d("Touch event", "oldDist=" + oldDist);
		 
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				
				if (DEBUG)
					Log.d("Touch event", "mode=ZOOM");
				}
			break;
		case MotionEvent.ACTION_UP:
			if (TAPPED)
			{
				// Handle tap:
				// If tapped on station point, pop up station info
				// If tapped elsewhere, bring up a help
				try {
					tapListen(event.getX(), event.getY(), v);
				} catch (IOException e) {
					Log.d("Exception", "There was an IOException. JSON File was not found.");
					e.printStackTrace();
				} catch (JSONException e) {
					Log.d("Exception", "There was a JSONException. JSON file was invalid.");
					e.printStackTrace();
				}
				Log.d("Tap", "(" + event.getX() + "," + event.getY() + ")");
			}
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			
			if (DEBUG)
				Log.d("Touch Event", "mode=NONE");
			
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				float dx = event.getX() - start.x;
				float dy = event.getY() - start.y;
				if (Math.abs(dx) > DRAG_TOLERANCE || Math.abs(dy) > DRAG_TOLERANCE) {
					matrix.set(savedMatrix);
					matrix.postTranslate(dx, dy);
					TAPPED = false;
				} else {
					TAPPED = true;
				}
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				
				if (DEBUG)
					Log.d("Touch Event", "newDist=" + newDist);
				
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
				TAPPED = false;
			}
			break;
		}
		
		float[] dimensions = new float[2];
		getScaledImageDimensions(dimensions);
		
		Log.d("Image Location", "(" + getScaledImagePosition().x + "," + getScaledImagePosition().y + ")");
		Log.d("Image Dimensions", dimensions[0] + " x " + dimensions[1]);
		
		view.setImageMatrix(matrix);
		view.invalidate();
		return true; // indicate event was handled
	}
	
	/**
	 * Returns the spacing between two fingers.
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event)
	{
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * Returns the mid-point between two fingers to find the start location.
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event)
	{
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	
	// This feature will be for the next version
	@SuppressWarnings("unused")
	private void drawOverlay(float x, float y, View v)
	{
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setAlpha(125);
			
		ImageView iv = (ImageView)v;
		// Save the current bitmap from the ImageView
		iv.buildDrawingCache();
		Bitmap bm = iv.getDrawingCache();
		
		// Create the circle
		Bitmap overlay = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(), Bitmap.Config.ARGB_8888);
		c = new Canvas(overlay);
		c.drawBitmap(bm, new Matrix(), null);
		c.drawCircle(x, y, 50f, paint);
		
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		iv.setImageBitmap(overlay);
	}
	
	private void tapListen(float x, float y, View v) throws IOException, JSONException {
		getScaledImageDimensions(imageDimensions);
		leftCorner = getScaledImagePosition();
		float[] currStation = new float[2];
		
		currStation[0] = 100 * (x - leftCorner.x)/imageDimensions[0];
		currStation[1] = 100 * (y - leftCorner.y)/imageDimensions[1];
		
		int key = -1;
		for (float i = currStation[0] - TAP_TOLERANCE * currStation[0] ; i < currStation[0] + TAP_TOLERANCE * currStation[0] ; i += 0.1f) {
			key = hashFunction(currStation, v);
			String resName;
			if (SystemData.stationHashMap.containsKey(key)) {
				if ((resName = SystemData.stationArray.get(SystemData.stationHashMap.get(key)).resName) != null) {
					Log.d("Collision", resName + " selected.");
					StationManager.station = parser.parse(resName);//parser.parse(name);
					//drawOverlay((stationX * imageDimensions[0]) + leftCorner.x, (stationY * imageDimensions[1]) + leftCorner.y, v);
					initiatePopupWindow();
					break;
				}
			}
		}
		
		
		Log.d("Point Data", "Point: (" + currStation[0] + "," + currStation[1] + ") Hash Key: " + key);
	}
	
	private int hashFunction(float[] currStation, View v) throws IOException, JSONException {
		int key = 0;
		key = ((int)currStation[1] << 16) ^ ((int)currStation[0]);
		key /= 10000;
		
		return key;
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
				Calendar nextNorth = CalendarUtils.getNextTrain(StationManager.DIRECTION_NORTHBOUND);
				Calendar nextSouth = CalendarUtils.getNextTrain(StationManager.DIRECTION_SOUTHBOUND);
				
				
				nbContainer.setVisibility(View.VISIBLE);
				sbContainer.setVisibility(View.VISIBLE);
				nb.setTextColor(Color.BLACK);
				sb.setTextColor(Color.BLACK);
				
				if (nextNorth != null)
					nb.setText(CalendarUtils.difference(Calendar.getInstance(), nextNorth, Calendar.MINUTE) + " minutes");
				else
					nb.setText("No more trains");
				
				if (nextSouth != null)
					sb.setText(CalendarUtils.difference(Calendar.getInstance(), nextSouth, Calendar.MINUTE) + " minutes");
				else
					sb.setText("No more trains");
			}
			
			if (StationManager.station.getLines().get(i).getName().equals("Green") || StationManager.station.getLines().get(i).getName().equals("Blue")) {
				Calendar nextEast = CalendarUtils.getNextTrain(StationManager.DIRECTION_EASTBOUND);
				Calendar nextWest = CalendarUtils.getNextTrain(StationManager.DIRECTION_WESTBOUND);
				
				ebContainer.setVisibility(View.VISIBLE);
				wbContainer.setVisibility(View.VISIBLE);
				
				eb.setText(CalendarUtils.difference(Calendar.getInstance(), nextEast, Calendar.MINUTE) + " minutes");
				wb.setText(CalendarUtils.difference(Calendar.getInstance(), nextWest, Calendar.MINUTE) + " minutes");
			}
		}
		
		// TEMP
		//nb.setText("5 minutes to Doraville");
		//nb.setTextColor(Color.YELLOW);
		//sb.setText("3 minutes to Airport");
		//sb.setTextColor(Color.RED);
		
		pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}
	
	private class PopUpButtonListener implements OnClickListener {
		public void onClick(View v) {
			pw.dismiss();
			switch (v.getId()) {
			case R.id.train_popup_button1:
				Intent intent = new Intent(MapActivity.this, StationActivity.class);
		    	startActivity(intent);
		    	break;
			}
		}
	}
	
	/**
	 * To be used while debugging. Displays current motion event and coordinates in the log.
	 * @param e
	 */
	public void dumpEvent(MotionEvent e) {
		String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", 
						"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
		
		StringBuilder sb = new StringBuilder();
		
		int action = e.getAction();
		
		int actionCode = action & MotionEvent.ACTION_MASK;
		
		sb.append("ACTION " + action);
		
		sb.append("event ACTION_").append(names[actionCode]);
		
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		
		sb.append("[");
		for (int i = 0;i < e.getPointerCount(); i++) {
			sb.append("# ").append(i);
			sb.append("(pid ").append(e.getPointerId(i));
			sb.append(")=").append((int) e.getX(i));
			sb.append(",").append((int) e.getY(i));
			if (i + 1 < e.getPointerCount())
				sb.append(";");
		}
		sb.append("]");
		
		Log.d("Touch Event", sb.toString());
	}
}