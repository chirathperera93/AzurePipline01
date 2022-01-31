package com.ayubo.life.ayubolife.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.model.SImpleListString;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimateView_Activity extends AppCompatActivity implements View.OnTouchListener {
    TextView _view;
    LinearLayout lv_main;
    ViewGroup _root;
    private int _xDelta;
    private int _yDelta;
    private ListView mListView;
    CustomListAdapterLite adapter;
    private GestureDetector mDetector;


    static final String[] FRUITS = new String[]{"Apple", "Avocado", "Banana",
            "Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
            "Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple"};
    int height, width;


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG", "onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");
            String sSelectivityRate = String.valueOf(distanceY);
            System.out.println("===================" + sSelectivityRate);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return true;
        }
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mDetector.onTouchEvent(event);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_view_);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        height = deviceScreenDimension.getDisplayHeight();
        width = deviceScreenDimension.getDisplayWidth();

        _root = (ViewGroup) findViewById(R.id.root);

        lv_main = (LinearLayout) findViewById(R.id.lv_main);

        mListView = (ListView) findViewById(R.id.recipe_list_view);

        ArrayList<SImpleListString> data = new ArrayList<SImpleListString>();


        Bitmap bitmap1 = BitmapFactory.decodeResource(AnimateView_Activity.this.getResources(), R.drawable.runnings);
        data.add(new SImpleListString(bitmap1, "RUNNING"));

        Bitmap bitmap2 = BitmapFactory.decodeResource(AnimateView_Activity.this.getResources(), R.drawable.climbings);
        data.add(new SImpleListString(bitmap2, "HIKING"));

        Bitmap bitmap3 = BitmapFactory.decodeResource(AnimateView_Activity.this.getResources(), R.drawable.badmintons);
        data.add(new SImpleListString(bitmap3, "BADMINTON"));

        Bitmap bitmap4 = BitmapFactory.decodeResource(AnimateView_Activity.this.getResources(), R.drawable.cyclings);
        data.add(new SImpleListString(bitmap4, "CYCLING"));

        Bitmap bitmap5 = BitmapFactory.decodeResource(AnimateView_Activity.this.getResources(), R.drawable.basketballs);
        data.add(new SImpleListString(bitmap5, "BASKETBALL"));

        Bitmap bitmap6 = BitmapFactory.decodeResource(AnimateView_Activity.this.getResources(), R.drawable.swimmings);
        data.add(new SImpleListString(bitmap6, "SWIMMING"));

        adapter = new CustomListAdapterLite(AnimateView_Activity.this, data);

        mListView.setAdapter(adapter);
        mListView.setClickable(false);

        mDetector = new GestureDetector(this, new MyGestureListener());
        mListView.setOnTouchListener(touchListener);

        mListView.setScrollContainer(false);
//        mListView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        _view = new TextView(this);
        _view.setText("TextView!!!!!!!!");

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, 450);
        layoutParams.leftMargin = 20;
        layoutParams.topMargin = 50;
        layoutParams.bottomMargin = -250;
        layoutParams.rightMargin = 20;
        lv_main.setLayoutParams(layoutParams);

        lv_main.setOnTouchListener(this);
        _root.addView(_view);

    }

    public boolean onTouch(View view, MotionEvent event) {


        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                int xmargin = X - _xDelta;
                int ymargin = Y - _yDelta;
                if (ymargin > 1100) {
                    layoutParams.topMargin = 1100;
                } else if (ymargin < 120) {
                    layoutParams.topMargin = 120;
                } else {
                    layoutParams.topMargin = Y - _yDelta;
                }
                //  layoutParams.leftMargin = X - _xDelta;


                System.out.println("===========leftMargin====x==========" + Integer.toString(xmargin));
                System.out.println("===========leftMargin=====y=========" + Integer.toString(ymargin));

                layoutParams.rightMargin = 20;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        _root.invalidate();
        return true;
    }

    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];

        private Context activity;
        private ArrayList<SImpleListString> data;
        private LayoutInflater inflater = null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a, ArrayList<SImpleListString> d) {
            activity = a;
            data = d;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.health_custome_list_raw, null);

            TextView title = (TextView) vi.findViewById(R.id.title); // title
            ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image

            HashMap<String, String> song = new HashMap<String, String>();

            SImpleListString obj = data.get(position);

            title.setText(obj.getName());
            thumb_image.setImageBitmap(obj.getBitmap());

            return vi;
        }
    }

}
