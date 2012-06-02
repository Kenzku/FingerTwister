package org.androidaalto.fingertwister;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

	Engine engine;
	ArrayList <GameCircle> circles;
	Bitmap green;
	
	public GamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		
//		colors -> get images
//		green = BitmapFactory.decodeResource(context.getResources(), R.drawable.green);
		
		engine = new Engine(this);
		circles = new ArrayList<GameCircle>();
//		GameCircle(Point center, int radius, boolean pressed, int color, Resources resources) 
		circles.add(new GameCircle(new Point(200,200), 100, false, Color.GREEN, context.getResources()));
	}
	
	public void update(){
	
		// Update gamelogic here (Will be called 25 times/second)
		// detect user input
	}
	
	
	public void draw(Canvas canvas){
		// Draw everything on the screen here (called 25times/second)
		
		//Draw the background (blue)
		canvas.drawColor(Color.BLUE);
		
		//TODO: iterate thru the list of circles and call .draw in them 
		for(GameCircle gc: circles){
			gc.draw(canvas);
		}
		
		
		
		
	}

	
	
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		if(!engine.isAlive()){
			engine = new Engine(this);
			engine.setRunning(true);
			engine.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if(engine.isAlive())
			engine.setRunning(false);
	}

	
	
	
}
