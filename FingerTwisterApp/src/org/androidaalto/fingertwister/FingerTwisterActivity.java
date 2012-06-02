package org.androidaalto.fingertwister;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class FingerTwisterActivity extends Activity implements UserEventCallback {

    private GamePanel gamePane;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);

        gamePane = (GamePanel) findViewById(R.id.game_field);
        gamePane.setUserEventCallback(this);

        // Add function to the restart button
        Button restart_btn = (Button) findViewById(R.id.restart_button);
        restart_btn.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v)	{
        		// TODO: Restart the game
        	}
        });
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
        return gamePane.onTouchEvent(event);
    }

	@Override
	public void onUserEvent(UserEvent event) {
		boolean success = event.getSuccess();
		if (success == true)	{
			// get new instruction
		}
		else	{
			// end game
		}
	}
    
    

}
