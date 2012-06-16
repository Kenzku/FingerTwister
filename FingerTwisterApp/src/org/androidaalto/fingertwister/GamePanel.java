package org.androidaalto.fingertwister;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback,
        MultiTouchController.MultiTouchObjectCanvas<Object> {

	private static final String LOG_TAG = "FingerTwister";
	
    private static final int MAX_FINGERS = 5;

//    Engine engine;
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

//        engine = new Engine(this);
//        engine.setRunning(true);
//        engine.start();

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
        	
        	if (isUserDoingOK()) {
        		
        		
        		if (circleManager.getNumTouchedCircle() == mCurrTouchPoint.getNumTouchPoints() ) {
        			// nothing interesting. because nothing changed
        			return;
        		}
        		
        		// save the current touching points in the corresponding game circles
        		saveCurrentTouchingState(mCurrTouchPoint);
        		
        		if (mCurrTouchPoint.getNumTouchPoints() == MAX_FINGERS ) {
        			Log.i(LOG_TAG, "max number of fingers are used. You win!");
        			updateGameState(GameState.WIN);
        		}
        		else {
	        		// generate new instruction
	                generateNextInstruction();
	                
	                // update the screen accordingly and waiting for user input
	                notifyUserEvent(UserEvent.UserState.Proceeding);
        		}
        		
        	} else {
            	Log.i(LOG_TAG, "finger position doesn't match instructiions!");
            	
                updateGameState(GameState.GAME_OVER);        		
        	}
        	
//        	
//        	
//        	
//            int index = mCurrTouchPoint.getNumTouchPoints() - 1;
//            float[] xs = mCurrTouchPoint.getXs();
//            float[] ys = mCurrTouchPoint.getYs();
//            float[] pressures = mCurrTouchPoint.getPressures();
//            int[] pointerIds = mCurrTouchPoint.getPointerIds();
//            float x = mCurrTouchPoint.getX(), y = mCurrTouchPoint.getY();
//            float wd = getWidth(), ht = getHeight();
//
//            // Check if matches current instruction
//            if (matchTouchToInstructionAndSetActive((int) xs[index], (int) ys[index])) {
//                // Store it in instructions
//                if (index >= MAX_FINGERS - 1) {
//                    updateGameState(GameState.WIN);
//                } else {
//                	// save the instruction 
//                    mInstructionHistory.add(mCurrentInstruction);
//
//                    generateNextInstruction();
//                    // update the screen and waiting for user input
//                    notifyUserEvent(true);
//                }
//            } else {
//            	Log.i(LOG_TAG, "finger position doesn't match instructiions!");
//            	
//                updateGameState(GameState.GAME_OVER);
//            }
        }
    }

    
    /**
     * Save the touching points info to circleManager
     * @param touchingPointsInfo
     */
	private void saveCurrentTouchingState(PointInfo touchingPointsInfo) {
		
		for (int pointerIdx = 0; pointerIdx <touchingPointsInfo.getNumTouchPoints(); pointerIdx ++) {
			// check all the current touchpoints are either within the already pressed circle or matching the current instruction
			float x = touchingPointsInfo.getXs()[pointerIdx];
			float y = touchingPointsInfo.getYs()[pointerIdx];
			Point touchPoint = new Point((int)x, (int)y);
			GameCircle gameCircle = circleManager.getAssociatedCircle(touchPoint);
			gameCircle.setPressed(true);
		}		
	}

	private boolean isUserDoingOK() {
		// check if the current finger positions are in line with the instructions
		for (int pointerIdx = 0; pointerIdx <mCurrTouchPoint.getNumTouchPoints(); pointerIdx ++) {
			// check all the current touchpoints are either within the already pressed circle or matching the current instruction
			float x = mCurrTouchPoint.getXs()[pointerIdx];
			float y = mCurrTouchPoint.getYs()[pointerIdx];
			Point touchPoint = new Point((int)x, (int)y);
			GameCircle gameCircle = circleManager.getAssociatedCircle(touchPoint);
			
			if (null == gameCircle) {
				Log.i(LOG_TAG, "your finger position is outside all the circles!");
				return false;
			}
			if (gameCircle.isPressed()) {
				// Yes, this is expected
				continue;
			} else {
				// check if this new touch point matches current instruction
				if (gameCircle.color == mCurrentInstruction.color) {
					// OK, it is expected that user's new finger touches here
					
				} else {
					return false;
				}
			}
		}

		return true;
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


    /**
     * update the graphics on this view
     */
	public void updateGraphicalView() {
		Log.i(LOG_TAG, "updateGraphicalView");
		SurfaceHolder sHolder = this.getHolder();

		Canvas canvas = null;

		do {

			canvas = sHolder.lockCanvas();
			if (canvas == null) {
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {

				}
			}
		} while (canvas == null);

		draw(canvas);

		sHolder.unlockCanvasAndPost(canvas);

	}
    
    public void draw(Canvas canvas) {
        // Draw everything on the screen here (called 25times/second)
    	canvas.drawColor(Color.LTGRAY);
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
        
        startGame();        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
//        if (engine.isAlive()) {
//            engine.setRunning(false);
//        }
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
    	if (mCurrTouchPoint == null) {
    		mCurrTouchPoint = touchPoint;
    	} else {
    		mCurrTouchPoint.set(touchPoint);
    	}
        update();
    }

    private void updateGameState(GameState newState) {
    	
    	if (newState == GameState.GAME_OVER) {
    		if (mState == GameState.WIN || mState == GameState.GAME_OVER) {
    			// I'm in the middle of handling some final state
    			return;
    		}
    	}

    	mState = newState;

        switch(newState) {
        case GAME_OVER:
            notifyUserEvent(UserEvent.UserState.Lose);
            return;

        case IDLE:
        case WAITING_ACTION:
        	notifyUserEvent(UserEvent.UserState.Proceeding);
            return;
            
        case WIN:
            notifyUserEvent(UserEvent.UserState.Win);
        	return;
        }

    }

    public void startGame() {
        if (mState != GameState.WAITING_ACTION) {
            init();
        }
    }


    private boolean matchTouchToInstructionAndSetActive(int x, int y) {
        boolean result = false;

        GameCircle circle = circleManager.getAssociatedCircle(new Point(x, y));
        if (circle != null && circle.color == mCurrentInstruction.color) {
            circle.setPressed(true);
            result = true;
        }

        return result;
    }

    public void setUserEventCallback(UserEventCallback uec) {
        this.userEventCallback = uec;
    }

    public void notifyUserEvent(UserEvent.UserState userState) {
        UserEvent event = new UserEvent(this, userState);
		
        // notify activity
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
