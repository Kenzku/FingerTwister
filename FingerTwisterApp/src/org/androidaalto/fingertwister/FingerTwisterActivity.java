package org.androidaalto.fingertwister;

import org.androidaalto.fingertwister.UserEvent.UserState;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FingerTwisterActivity extends Activity implements UserEventCallback {

    static final int DIALOG_GAME_OVER = 0;
    static final int WIN = 1;

    private GamePanel gamePane;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        gamePane = (GamePanel) findViewById(R.id.game_field);
        gamePane.setUserEventCallback(this);

        // Add function to the restart button
        Button restart_btn = (Button) findViewById(R.id.restart_button);
        restart_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gamePane.startGame();
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
//        logTouchEvent(event);
        return gamePane.onTouchEvent(event);
    }

    @Override
	public void onUserEvent(UserEvent event) {

		UserState userState = event.getUserState();

		gamePane.updateGraphicalView();

		switch (userState) {
		case Win:
			Log.i("FingerTwister", "recieved Win event");
			showDialog(WIN);
			return;

		case Lose:
			Log.i("FingerTwister", "recieved Lose event");
			showDialog(DIALOG_GAME_OVER);
			return;

		case Proceeding:
			GamePanel.Instruction currentInstruction = gamePane
					.getCurrentInstruction();

			TextView colorTextView = (TextView) findViewById(R.id.instruction_color);
			colorTextView.setBackgroundColor(currentInstruction.color);

			TextView fingerTextView = (TextView) findViewById(R.id.instruction_finger);

			switch (currentInstruction.finger) {
			case INDEX:
				fingerTextView.setText(R.string.index_finger);
				break;
			case MIDDLE:
				fingerTextView.setText(R.string.middle_finger);
				break;
			case RING:
				fingerTextView.setText(R.string.ring_finger);
				break;
			case LITTLE:
				fingerTextView.setText(R.string.little_finger);
				break;
			}
			return;
		}
	}

    @Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_GAME_OVER:
		case WIN:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (id == DIALOG_GAME_OVER) {
				builder = builder.setMessage("Game over!");
			} else {
				builder = builder.setMessage("You Win!");
			}

			builder.setCancelable(false)
					.setPositiveButton("New Game",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									gamePane.startGame();
								}
							})
					.setNegativeButton("Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent intent = new Intent(
											Intent.ACTION_MAIN);
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
