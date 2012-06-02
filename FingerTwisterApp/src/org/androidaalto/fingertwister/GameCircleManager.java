package org.androidaalto.fingertwister;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

/**
 * manage all the circles used in this game panel
 * @author shaohong
 *
 */
public class GameCircleManager {

	GameCircle circles[][];
	
	Canvas theCanvas;
	
	Resources resources; // we need this to get the images

	
	public GameCircleManager(Canvas theCanvas, Resources resource) {
		super();
		this.theCanvas = theCanvas;
		this.resources = resource;

		//TODO calculate the radius of the circle;
		int radius = 100;
		
		// constructed the 4x4 array of circles;
		circles = new GameCircle[4][4];
		
		for (int row=1; row <=4; row++) {
			for (int col=1; col<=4; col++) {
				// get the center position of the circle;
				Point centerCoordinate = getCircleCenterCoordinates(row, col);
				circles[row-1][col-1] = new GameCircle(centerCoordinate, radius, false, getColor(col), resource );
				
			}
		}
	}


	private int getColor(int col) {
		switch(col){
		case 1:
			return Color.GREEN;

		case 2:
			return Color.YELLOW;

		case 3:
			return Color.BLUE;
			
		case 4:
			return Color.RED;

		}

		throw new IllegalArgumentException("wrong column value! (1,2,3,4) are valid ones, but you supplied" + col);

	}


	/**
	 * draw all the circles managed by this manager
	 * @param canvas
	 */
	public void drawCircles() {
		
		for (int row=1; row <=4; row++) {
			for (int col=1; col<=4; col++) {
				GameCircle gameCircle = circles[row-1][col-1];
				gameCircle.draw(theCanvas);
			}
		}
	}

	/**
	 * get the coordinate, on the canvas, of the center of the given circle (defined by row, column);
	 * valid row, column values are between 1..4
	 * @return
	 */
	private Point getCircleCenterCoordinates(int row, int column) {
		return null;
	}
	
	/**
	 * Given a row, column of the circle, return the gamecircle itself. 
	 * e.g. row=1, column=1, returns the top left circle.
	 * @param row
	 * @param column
	 * @return
	 */
	public GameCircle getTheCircle(int row, int column) {
		return null;
	}
	
	
	/**
	 * Given a touch point, returns the circle that this touch point falls into.
	 * @param touchPoint
	 * @return null, means no gamecircle encircles this touch point.
	 * 
	 */
	public GameCircle getAssociatedCircle(Point touchPoint) {
		return null;
	}
}
