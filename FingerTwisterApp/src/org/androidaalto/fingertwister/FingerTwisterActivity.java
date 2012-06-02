package org.androidaalto.fingertwister;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FingerTwisterActivity extends Activity {

    private Panel gamePane;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gamePane = new Panel(this);
        setContentView(gamePane);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gamePane.setTouch((int) event.getX(), (int) event.getY());
        } else {
            gamePane.setTouch(0, 0);
        }

        return super.onTouchEvent(event);
    }

    class Panel extends SurfaceView implements SurfaceHolder.Callback {

        private TutorialThread _thread;

        public static final int RADIUS = 60;

        private int touchPointX;
        private int touchPointY;

        public Panel(Context context) {
            super(context);
            getHolder().addCallback(this);
            _thread = new TutorialThread(getHolder(), this);
        }

        public void setTouch(int x, int y) {
            touchPointX = x;
            touchPointY = y;
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            if (touchPointX != 0 && touchPointY != 0) {
                paint.setColor(Color.GREEN);
                canvas.drawCircle(touchPointX, touchPointY, RADIUS, paint);
            } else {
                canvas.drawCircle(40, 40, RADIUS, paint);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            _thread.setRunning(true);
            _thread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // simply copied from sample application LunarLander:
            // we have to tell thread to shut down & wait for it to finish, or else
            // it might touch the Surface after we return and explode
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }
    }

    class TutorialThread extends Thread {

        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;

        public TutorialThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
