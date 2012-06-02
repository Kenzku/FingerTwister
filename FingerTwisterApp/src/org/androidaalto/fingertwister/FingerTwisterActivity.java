package org.androidaalto.fingertwister;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public class FingerTwisterActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TESTING
        setContentView(new GamePanel(this));
        //setContentView(R.layout.main);
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

   

    
}
