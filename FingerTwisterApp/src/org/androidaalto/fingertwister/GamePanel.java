package org.androidaalto.fingertwister;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;

import java.util.ArrayList;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback,
        MultiTouchController.MultiTouchObjectCanvas<Object> {

    Engine engine;
    ArrayList<GameCircle> circles;

    private MultiTouchController<Object> mTouchController;
    private PointInfo mCurrTouchPoint;

    public GamePanel(Context context) {
        super(context);
    }

    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);

        mTouchController = new MultiTouchController<Object>(this);
        mCurrTouchPoint = new PointInfo();

        engine = new Engine(this);
        engine.start();
        circles = new ArrayList<GameCircle>();
        circles.add(new GameCircle(new Point(200, 200), 100, false, Color.GREEN, context.getResources()));
    }

    public void update() {

        // Update gamelogic here (Will be called 25 times/second)
        // detect user input
        if (mCurrTouchPoint.isDown()) {
            int numPoints = mCurrTouchPoint.getNumTouchPoints();
            float[] xs = mCurrTouchPoint.getXs();
            float[] ys = mCurrTouchPoint.getYs();
            float[] pressures = mCurrTouchPoint.getPressures();
            int[] pointerIds = mCurrTouchPoint.getPointerIds();
            float x = mCurrTouchPoint.getX(), y = mCurrTouchPoint.getY();
            float wd = getWidth(), ht = getHeight();

            if (numPoints == 1) {
                // Draw ordinate lines for single touch point
                GameCircle newCircle = new GameCircle(new Point((int) x, (int) y),
                        50, false, Color.RED, getResources());
                synchronized (circles) {
                    circles.add(newCircle);
                }
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchController.onTouchEvent(event);
    }

    public void draw(Canvas canvas) {
        // Draw everything on the screen here (called 25times/second)

        //Draw the background (blue)
        canvas.drawColor(Color.BLUE);

        //TODO: iterate thru the list of circles and call .draw in them
        synchronized (circles) {
            for (GameCircle gc : circles) {
                gc.draw(canvas);
            }
        }

    }

    private int getRealPixels(float dpi) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dpi, getResources().getDisplayMetrics());
        return value;
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (!engine.isAlive()) {
            engine = new Engine(this);
            engine.setRunning(true);
            engine.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (engine.isAlive())
            engine.setRunning(false);
    }


    @Override
    public Object getDraggableObjectAtPoint(MultiTouchController.PointInfo touchPoint) {
        return this;
    }

    @Override
    public boolean pointInObjectGrabArea(MultiTouchController.PointInfo touchPoint, Object obj) {
        return false;
    }

    @Override
    public void getPositionAndScale(Object obj,
            MultiTouchController.PositionAndScale objPosAndScaleOut) {

    }

    @Override
    public boolean setPositionAndScale(Object obj,
            MultiTouchController.PositionAndScale newObjPosAndScale,
            MultiTouchController.PointInfo touchPoint) {
        touchPointChanged(touchPoint);
        return true;
    }

    @Override
    public void selectObject(Object obj, MultiTouchController.PointInfo touchPoint) {
        touchPointChanged(touchPoint);
    }

    /**
     * Called when the touch point info changes, causes a redraw.
     *
     * @param touchPoint
     */
    private void touchPointChanged(PointInfo touchPoint) {
        // Take a snapshot of touch point info, the touch point is volatile
        mCurrTouchPoint.set(touchPoint);
        update();
    }
}
