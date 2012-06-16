package org.androidaalto.fingertwister;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class MyMusic {
	private static final String LOG_TAG = "FingerTwister";

	private Context context = null;
	private MediaPlayer mpWinSound = null;
	private MediaPlayer mpFailSound = null;
	
	
	
	public MyMusic(Context context) {
		this.context = context;
		mpFailSound = MediaPlayer.create(getContext(), R.raw.fail);
		
		if (null == mpFailSound) {
			Log.e(LOG_TAG, "failed to create media player for fail sound");
		}
		
		mpWinSound = MediaPlayer.create(getContext(), R.raw.win);
		
		if (null == mpWinSound) {
			Log.e(LOG_TAG, "failed to create media player for win sound");			
		}
		
	}
	public void playWinSound() {
		if (null == mpWinSound) {
			mpWinSound = MediaPlayer.create(getContext(), R.raw.win);
		}
		
		if (null != mpWinSound) {
			mpWinSound.start();			
		}

	}
	
	private Context getContext() {
		// TODO Auto-generated method stub
		return this.context;
	}
	public void playLoseSound() {
		if (null == mpFailSound) {
			mpFailSound = MediaPlayer.create(getContext(), R.raw.fail);
		}
		
		if (null != mpFailSound) {
			mpFailSound.start();
		}
	}
	
	public void releaseAll(){
		if (mpWinSound != null) {
			mpWinSound.release();	
		}
		
		if (null != mpFailSound) {
			mpFailSound.release();
		}
	}

}
