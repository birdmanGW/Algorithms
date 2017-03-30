import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.sun.image.codec.jpeg.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.text.*;

// A simple class to display a grid of binary cells: black (filled) or white (unfilled).
// Clicking on a cell "selects" the cell.


public class Clickable2DGrid extends JPanel implements MouseListener {

    int width, height;      // The "N x N" of the image. width=height.
    int[][] grid;           // The grid has int values for grey-scale intensity.
                            // A signal value of 0 corresponds to grey.
    int cellSize;           // Calculated later.
    
    // We need a reference to this, to call back some methods in it.
    DemoDFT2D dftFrame;

    // Toggle between showing cell values or not.
    boolean showValues = false;
    

    public Clickable2DGrid (DemoDFT2D dftFrame, int width, int height)
    {
        this.dftFrame = dftFrame;
        this.width = width;
        this.height = height;
        this.addMouseListener (this);
        grid = new int [width][height];
    }


    // The binary grid to be drawn. It's assumed the caller has created this.

    public void setGrid (int[][] grid)
    {
	this.grid = grid;
	if (grid != null) {
	    width = grid.length;
	    height = grid[0].length;
	}
    }
  

    public int[][] getGrid ()
    {
	return grid;
    }
    
    public void showValuesToggle ()
    {
	showValues = !showValues;
    }

    public void paintComponent (Graphics g)
    {
	super.paintComponent (g);
	
	Dimension D = this.getSize ();
	int drawWidth = D.width;
	int drawHeight = D.height;
	
	
	if ( (width == 0) || (height == 0) )
	    return;
	
	// First dimension of grid goes horizontally.
	int cellWidth = drawWidth / width;
	int cellHeight = drawHeight / height;
	cellSize = cellWidth;
	if (cellHeight < cellWidth) {
	    cellSize = cellHeight;
	}
	
	// Draw cells.
	for (int x=0; x<width; x++) {
	    for (int y=0; y<height; y++) {
		// Draw cell x,y.
		int topLeftX = x*cellSize;
		int topLeftY = y*cellSize;
		int intensity = 128 + grid[x][y];
		int blackness = 255-intensity;
		g.setColor (new Color (blackness, blackness, blackness));
		g.fillRect (topLeftX, topLeftY, cellSize, cellSize);
		if (showValues) {
		    if (blackness > 150) {
			g.setColor (new Color (0, 0, 0));
		    }
		    else {
			g.setColor (new Color (255, 255, 255));
		    }
		    g.drawString (""+grid[x][y], topLeftX+cellSize/4, topLeftY+cellSize/2);
		}
	    }
	}
	
	// Draw grid lines.
	g.setColor (Color.BLACK);
	for (int x=0; x<width; x++) {
	    int xVal = x*cellSize;
	    g.drawLine (xVal,0, xVal, drawHeight);
	}
	
	for (int y=0; y<height; y++) {
	    int yVal = y*cellSize;
	    g.drawLine (0,yVal, drawWidth,yVal);
	}
    }
    
    
    
    public void mouseClicked (MouseEvent e)
    {
	// Find out where.
	int xval = e.getX();
	int yval = e.getY();
	dftFrame.currentGrid = this;
	dftFrame.selecti = xval/cellSize;
	dftFrame.selectj = yval/cellSize;
	dftFrame.setTextField ();
	System.out.println ("Mouse clicked: i=" + dftFrame.selecti + " j=" + dftFrame.selectj);
	// Draw a little box.
	Graphics g = this.getGraphics ();
	int topLeftX = dftFrame.selecti*cellSize;
	int topLeftY = dftFrame.selectj*cellSize;
	g.setColor (Color.black);
	g.drawRect (topLeftX+2, topLeftY+2, cellSize-4, cellSize-4);
	
    }
    
    // Remaining methods in MouseListener interface.
    public void mouseEntered (MouseEvent e){}
    public void mouseExited (MouseEvent e){}
    public void mousePressed (MouseEvent e){}
    public void mouseReleased (MouseEvent e){}
}

