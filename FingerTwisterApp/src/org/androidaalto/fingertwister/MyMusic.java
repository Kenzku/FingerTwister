package org.androidaalto.fingertwister;

import android.content.Context;
import android.media.MediaPlayer;

public class MyMusic {
	
	private Context context = null;
	private MediaPlayer mpWinSound = null;
	private MediaPlayer mpFailSound = null;
	
	
	
	public MyMusic(Context context) {
		this.context = context;
		mpFailSound = MediaPlayer.create(getContext(), R.raw.fail);
		mpWinSound = MediaPlayer.create(getContext(), R.raw.win);
		
	}
	public void playWinSound() {
		
		mpWinSound.start();
	}
	
	private Context getContext() {
		// TODO Auto-generated method stub
		return this.context;
	}
	public void playLoseSound() {
		mpFailSound.start();
	}
	
	public void releaseAll(){
		mpWinSound.release();
		mpFailSound.release();
	}

}
