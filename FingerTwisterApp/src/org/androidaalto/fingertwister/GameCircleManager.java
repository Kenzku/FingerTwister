package org.androidaalto.fingertwister;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * manage all the circles used in this game panel
 * @author shaohong
 */
public class GameCircleManager {

	GameCircle circles[][];
	
	Rect canvasRect;
	
	Resources resources; // we need this to get the images

	
	public GameCircleManager(Rect canvasRect, Resources resource) {
		super();
		this.canvasRect = canvasRect;
		this.resources = resource;

		//TODO calculate the radius of the circle;
		int radius = getRadius(canvasRect);
		
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


	private int getRadius(Rect canvasRect2) {
		int canvasHeight = canvasRect.height();
		int canvasWidth = canvasRect.width();
		
		int radius = canvasHeight/8;
		if (canvasHeight > canvasWidth) {
			radius = canvasWidth/8;
		}
		
		radius = (int)(radius * 0.8);
		
		return radius;
	}


	/**
	 * given a column, return the corresponding color
	 * @param col
	 * @return
	 */
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
	 * @param theCanvas
	 */
	public void drawCircles(Canvas theCanvas) {
		
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
		int canvasHeight = canvasRect.height();
		int canvasWidth = canvasRect.width();
		
		int averageHeightLength = canvasHeight/4;
		int averageWidthLength = canvasWidth/4;
		
		int centreX = (column-1) * averageWidthLength + averageWidthLength/2;
		int centreY = (row - 1) * averageHeightLength + averageHeightLength/2;
		
		Point point = new Point(centreX,centreY);
		
		return point;
	}
	
	/**
	 * Given a row, column of the circle, return the gamecircle itself. 
	 * e.g. row=1, column=1, returns the top left circle.
	 * @param row
	 * @param column
	 * @return
	 */
	public GameCircle getTheCircle(int row, int column) {
		
		if (row <1 || row > 4) 
			throw new IllegalArgumentException("wrong row number! " + row);
		
		if (column <1 || column > 4)
			throw new IllegalArgumentException("wrong column number! " + column);
		
		return circles[row-1][column-1];
	}
	
	
	/**
	 * Given a touch point, returns the circle that this touch point falls into.
	 * @param touchPoint
	 * @return null, means no gamecircle encircles this touch point.
	 * 
	 */
	public GameCircle getAssociatedCircle(Point touchPoint) {
		for (int row=1; row <=4; row++) {
			for (int col=1; col<=4; col++) {
				GameCircle gameCircle = circles[row-1][col-1];
				if (gameCircle.isInsideCircle(touchPoint)) {
					return gameCircle;
				}
			}
		}
		
		return null;
	}
}
