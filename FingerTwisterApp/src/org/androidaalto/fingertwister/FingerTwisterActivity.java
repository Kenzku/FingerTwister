package org.androidaalto.fingertwister;

import org.androidaalto.fingertwister.GamePanel.Fingers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

public class FingerTwisterActivity extends Activity implements UserEventCallback {

	static final int DIALOG_GAME_OVER = 0;
	
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
			GamePanel.Instruction currentInstruction = gamePane.getCurrentInstruction();
			
			TextView colorTextView = (TextView) findViewById(R.id.instruction_color);
			colorTextView.setBackgroundColor(currentInstruction.color);
			
			TextView fingerTextView = (TextView) findViewById(R.id.instruction_finger);
			
//			switch (currentInstruction.finger.ordinal())	{
//			case GamePanel.Fingers.INDEX.ordinal():
//				fingerTextView.setText(R.string.index_finger);
//				break;
//			case GamePanel.Fingers.MIDDLE.ordinal():
//				fingerTextView.setText(R.string.middle_finger);
//				break;
//			case GamePanel.Fingers.RING.ordinal():
//				fingerTextView.setText(R.string.ring_finger);
//				break;
//			case GamePanel.Fingers.LITTLE.ordinal():
//				fingerTextView.setText(R.string.little_finger);
//				break;
//			}
		}
		else	{
			showDialog(DIALOG_GAME_OVER);
		}
	}
    
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id)	{
		case DIALOG_GAME_OVER:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Game over!")
			       .setCancelable(false)
			       .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   // TODO: new game
			                //MyActivity.this.finish();
			           }
			       })
			       .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   Intent intent = new Intent(Intent.ACTION_MAIN);
			        	   intent.addCategory(Intent.CATEGORY_HOME);
			        	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        	   startActivity(intent);
			           }
			       });
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		
		return dialog;
	}
    

}
