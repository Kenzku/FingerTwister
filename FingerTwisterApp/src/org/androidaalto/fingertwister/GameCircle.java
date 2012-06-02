package org.androidaalto.fingertwister;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	 
	Resources resources;
	
	Point center;
		
	int radius;	// the radius of the circle
	
	boolean pressed = false;	// if this circle is currently being pressed on the screen
	
	int color ; // the color of the game circle; how to render it is another thing
	
	public GameCircle(Point center, int radius, boolean pressed, int color, Resources resources) {
		super();
		this.center = center;
		this.radius = radius;
		this.pressed = pressed;
		this.color = color;
		this.resources = resources;
	}
		
	 
	/**
	 * check if the given point is within the circle or not
	 * @param aPoint
	 * @return
	 */
	public boolean isInsideCircle(Point aPoint) {
		double distance  = (center.x - aPoint.x) * (center.x - aPoint.x) + (center.y-aPoint.y)*(center.y - aPoint.y);
		distance = Math.sqrt(distance);
		
		return (distance > radius);
	}

	/**
	 * draw itself on the given canvas
	 * @param theCanvas
	 */
	public void draw(Canvas canvas) {
		// canvas.drawBitmap(image, leftTopPoint.x, leftTopPoint.y, null);

		Bitmap image = getImage();
		if (image != null) {
			int bitmapLeft = center.x -image.getWidth()/2;
			int bitmapTop = center.y - image.getHeight()/2;
			canvas.drawBitmap(image, bitmapLeft, bitmapTop, null);
		} 
	}


	private Bitmap getImage() {
		Bitmap image = null;

		switch (color) {
		case Color.GREEN:
			if (pressed) {
				image = BitmapFactory.decodeResource(resources, R.drawable.greenpress);
			} else {
				image = BitmapFactory.decodeResource(resources, R.drawable.green);				
			}

			break;
		case Color.YELLOW:
			if (pressed) {
				image = BitmapFactory.decodeResource(resources, R.drawable.yellowpress);
				
			} else {
				image = BitmapFactory.decodeResource(resources, R.drawable.yellow);
			}

			break;
		case Color.BLUE:
			if (pressed) {
				image = BitmapFactory.decodeResource(resources, R.drawable.bluepress);
				
			} else {
			image = BitmapFactory.decodeResource(resources, R.drawable.blue);
			}

			break;
		case Color.RED:
			if (pressed) {
				image = BitmapFactory.decodeResource(resources, R.drawable.redpress);				
			} else {
				image = BitmapFactory.decodeResource(resources, R.drawable.red);				
			}

			break;

		}
		
		return image;
	}
}
