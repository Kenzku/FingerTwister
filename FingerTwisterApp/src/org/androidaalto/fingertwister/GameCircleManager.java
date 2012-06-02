package org.androidaalto.fingertwister;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * manage all the circles used in this game panel
 * @author shaohong
 *
 */
public class GameCircleManager {

	
	Canvas theCanvas;
	
	
	public GameCircleManager(Canvas theCanvas) {
		super();
		this.theCanvas = theCanvas;
	}


	/**
	 * draw all the circles managed by this manager
	 * @param canvas
	 */
	public void drawCircles(Canvas canvas) {

	}

	/**
	 * get the coordinate, on the canvas, of the center of the given circle (defined by row, column);
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
