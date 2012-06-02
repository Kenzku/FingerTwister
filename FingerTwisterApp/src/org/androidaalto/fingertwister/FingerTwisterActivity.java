package org.androidaalto.fingertwister;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public class FingerTwisterActivity extends Activity {

	GamePanel gamepanel;
	

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        //layoutRoot = (FrameLayout) this.findViewById(R.id.GameViewRoot);
        
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//           // gamePane.setTouch((int) event.getX(), (int) event.getY());
//        } else {
//          //  gamePane.setTouch(0, 0);
//        }
//
//        return super.onTouchEvent(event);
//    }

 
    /**
     * Prints event data (action and event coordinates) to android system log.
     * @param event Event to be logged.
     */
    private void logTouchEvent(MotionEvent event) {
        final String logTag = "TouchEvent";
        String action;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_CANCEL:
                action = "Cancel";
                break;
            case MotionEvent.ACTION_DOWN:
                action = "Down";
                break;
            case MotionEvent.ACTION_UP:
                action = "Up";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "Move";
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action = "Pointer down";
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = "Pointer up";
                break;
            default:
                action = "Unknown";
        }

        Log.i(logTag, "Action: " + action);
        Log.i(logTag, "Point X: " + event.getX() + "; Point Y: " + event.getY());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        logTouchEvent(event);
 
        return super.onTouchEvent(event);
    }


}
