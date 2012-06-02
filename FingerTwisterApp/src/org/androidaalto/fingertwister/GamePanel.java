package org.androidaalto.fingertwister;

import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback,
        MultiTouchController.MultiTouchObjectCanvas<Object> {

    private static final int MAX_FINGERS = 5;

    Engine engine;
    GameCircleManager circleManager;
    
    Random randomNumberGenerator;

    private enum GameState {
        IDLE,
        WAITING_ACTION,
        GAME_OVER,
        WIN
    }

    public enum Fingers {
        THUMB,
        INDEX,
        MIDDLE,
        RING,
        LITTLE
    }

    private MultiTouchController<Object> mTouchController;
    private PointInfo mCurrTouchPoint;

    private GameState mState;
    private Instruction mCurrentInstruction;
    private List<Instruction> mInstructionHistory;

    public GamePanel(Context context) {
        super(context);
    }

    UserEventCallback userEventCallback;

    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);

        this.randomNumberGenerator = new Random();
        mTouchController = new MultiTouchController<Object>(this);
    }

    /**
     * Initializes view.
     */
    private void init() {
        mCurrTouchPoint = new PointInfo();
        mInstructionHistory = new ArrayList<Instruction>();

        engine = new Engine(this);
        engine.setRunning(true);
        engine.start();

        Rect frameRect = getHolder().getSurfaceFrame();
        circleManager = new GameCircleManager(frameRect, getResources());

        generateNextInstruction();
        updateGameState(GameState.WAITING_ACTION);
    }

    public Instruction getCurrentInstruction() {
        return mCurrentInstruction;
    }

    public void update() {

        // Update gamelogic here (Will be called 25 times/second)
        // detect user input
        if (mState == GameState.WAITING_ACTION && mCurrTouchPoint.isDown()) {
            int index = mCurrTouchPoint.getNumTouchPoints() - 1;
            float[] xs = mCurrTouchPoint.getXs();
            float[] ys = mCurrTouchPoint.getYs();
            float[] pressures = mCurrTouchPoint.getPressures();
            int[] pointerIds = mCurrTouchPoint.getPointerIds();
            float x = mCurrTouchPoint.getX(), y = mCurrTouchPoint.getY();
            float wd = getWidth(), ht = getHeight();

            // Check if matches current instruction
            if (matchTouchToInstruction((int) xs[index], (int) ys[index])) {
                // Store it in instructions
                if (index >= MAX_FINGERS - 1) {
                    updateGameState(GameState.WIN);
                } else {
                    mInstructionHistory.add(mCurrentInstruction);
                    generateNextInstruction();
                    notifyUserEvent(true);
                }
            } else {
                updateGameState(GameState.GAME_OVER);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                updateGameState(GameState.GAME_OVER);
                return true;
            default:
                return mTouchController.onTouchEvent(event);
        }
    }

    public void draw(Canvas canvas) {
        // Draw everything on the screen here (called 25times/second)

        //Draw the background (blue)
        canvas.drawColor(Color.GRAY);

        this.circleManager.drawCircles(canvas);
    }

    private int getRealPixels(float dpi) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dpi, getResources().getDisplayMetrics());
        return value;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        mState = GameState.IDLE;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (engine.isAlive()) {
            engine.setRunning(false);
        }
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
     * @param touchPoint
     */
    private void touchPointChanged(PointInfo touchPoint) {
        // Take a snapshot of touch point info, the touch point is volatile
        mCurrTouchPoint.set(touchPoint);
        update();
    }

    private void updateGameState(GameState newState) {
        mState = newState;

        // TODO: notify activity or update something
        if (newState == GameState.GAME_OVER) {
            notifyUserEvent(false);
        } else {
            notifyUserEvent(true);
        }
    }

    public void startGame() {
        if (mState != GameState.WAITING_ACTION) {
            init();
        }
    }


    private boolean matchTouchToInstruction(int x, int y) {
        boolean result = false;

        GameCircle circle = circleManager.getAssociatedCircle(new Point(x, y));
        if (circle != null && circle.color == mCurrentInstruction.color) {
            result = true;
        }

        return result;
    }

    public void setUserEventCallback(UserEventCallback uec) {
        this.userEventCallback = uec;
    }

    public void notifyUserEvent(boolean success) {
        UserEvent event = new UserEvent(this, success);
        if (userEventCallback != null) {
            userEventCallback.onUserEvent(event);
        }
    }

    public void generateNextInstruction() {
        Instruction newInstruction = new Instruction();

        newInstruction.finger = Fingers.values()[randomNumberGenerator.nextInt(5)];

        switch (randomNumberGenerator.nextInt(4)) {
            case 0:
                newInstruction.color = Color.RED;
                break;
            case 1:
                newInstruction.color = Color.BLUE;
                break;
            case 2:
                newInstruction.color = Color.GREEN;
                break;
            case 3:
                newInstruction.color = Color.YELLOW;
                break;
        }

        mCurrentInstruction = newInstruction;
    }

    /**
     * Represents current instruction.
     */
    public class Instruction {

        public Fingers finger;
        public int color;

    }

}
