package org.androidaalto.fingertwister;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

/**
 * A game circle represents a valid circular region on the screen.
 * 
 * @author shaohong
 *
 */
public class GameCircle {
	
	Point leftTopPoint;
	
	Bitmap image;
	
	int radius;	// the radius of the circle
	
	boolean pressed = false;	// if this circle is currently being pressed on the screen
	
	Color color = null; // the color of the game circle; how to render it is another thing
	
	public GameCircle(Point leftTopPoint, int radius, boolean pressed, Color color, Bitmap image) {
		super();
		this.leftTopPoint = leftTopPoint;
		this.radius = radius;
		this.pressed = pressed;
		this.color = color;
		this.image = image;
	}
		
	
	/**
	 * check if the given point is within the circle or not
	 * @param aPoint
	 * @return
	 */
	public boolean isInsideCircle(Point aPoint) {
		double distance  = (leftTopPoint.x - aPoint.x) * (leftTopPoint.x - aPoint.x) + (leftTopPoint.y-aPoint.y)*(leftTopPoint.y - aPoint.y);
		distance = Math.sqrt(distance);
		
		return (distance > radius);
	}

	/**
	 * draw itself on the given canvas
	 * @param theCanvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, leftTopPoint.x, leftTopPoint.y, null);
	}
}
