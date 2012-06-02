package org.androidaalto.fingertwister;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Engine extends Thread{

		// desired fps
			private final int 	FPS = 25;
			// the frame period
			private final int	SKIP_TICKS = 1000 / FPS; //40
			
			private GamePanel gPanel;
			private SurfaceHolder sHolder;
			private boolean running = false;
			
			
			public Engine(GamePanel gPanel)
			{
				this.gPanel = gPanel;
				sHolder = gPanel.getHolder();
			}
			
			public void setRunning(boolean running)
			{
				this.running = running;
			}
			
			
			public void run(){
				Canvas canvas;
				//long beginTime;		// the time when the cycle begun
				long timeDiff;		// the time it took for the cycle to execute
				int sleepTime = 0;		// ms to sleep (<0 if we're behind)
				long nextGameTick=System.currentTimeMillis();
				//long beginTime = System.currentTimeMillis();
				while (running){
					canvas = null;
					try
					{
						canvas = sHolder.lockCanvas();
						synchronized(sHolder)
						{
							gPanel.update();
							gPanel.draw(canvas);
							nextGameTick+= SKIP_TICKS;
							sleepTime = (int) (nextGameTick - System.currentTimeMillis());
							if(sleepTime >= 0)
							{
								try{
									sleep(sleepTime);
								}catch(InterruptedException e){}
							}
						}
					}finally{
						if(canvas!=null) sHolder.unlockCanvasAndPost(canvas);
					}//endfinally
				}//endwhile
			}//end void run
	
	
}
