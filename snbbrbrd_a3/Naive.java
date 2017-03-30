import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;

public class Naive implements MTSPAlgorithm {

	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of Naive.java.";
    }

    //Setting that property extractor since '95'
    public void setPropertyExtractor(int algID, PropertyExtractor prop) {

    }



  /* Constructor Summary
   *	Pointd() 
   *        Default Pointd constructor: x=y=0.
   *	Pointd(double x, double y) 
   *        Pointd constructor.
   */

  /* 
   * Method computeTours() is the only method in the interface. 
   * The number of salesmen, m, and the the locations of the points 
   * (cities) are passed in as parameters. You need to return a 2D array 
   * of ints, one row for each of the m salesmen. Each row will list in 
   * an array the points (cities) assigned in tour-order to that particular
   * salesman. For example, suppose m=5, and array A (of type int[][]) is 
   * returned. Then, A[3] = {0, 4, 5} implies that cities 0, 4 and 5 were 
   * assigned to salesman 3 (the fourth salesman, counting from 0 onwards).
   *
   *
   * A "naive" algorithm in Naive.java that merely divides the points more 
   * or less evenly among the salesmen ("more or less" because m may not exactly
   * divide the number of points).
   *
   */
    public int[][] computeTours(int m, Pointd[] points) {

    	int i, j, count = 0;
    	double size;
    	int[][] assignedPoints = new int[m][];

    	size = Math.floor(points.length / m);
    	//System.out.println("size: " + size + " | m: " + m + " | points.length: " + points.length);

    	//If m divides evenly
    	if (points.length % m == 0) {

	    	for (i = 0; i < assignedPoints.length; i++) {
	    		assignedPoints[i] = new int[(int)size];
	    		for (j = 0; j < assignedPoints[i].length; j++) 
	    			assignedPoints[i][j] = count++;
	    	}
	    //Else m does not divide evenly, must add to last row size + points.length % m
	    } else {

	    	for (i = 0; i < assignedPoints.length-1; i++) {
	    		assignedPoints[i] = new int[(int)size];
	    		for (j = 0; j < assignedPoints[i].length; j++) 
	    			assignedPoints[i][j] = count++;
	    	}

	    	assignedPoints[assignedPoints.length-1] = new int[(int)size + points.length % m];
	    	for (j = 0; j < assignedPoints[assignedPoints.length-1].length; j++) 
	    		assignedPoints[assignedPoints.length-1][j] = count++;
	    }

	    //printTours(assignedPoints, m);

	    return assignedPoints;
    }

    public void printTours(int[][] assignedPoints, int m) {

    	int i, j;

    	for (i = 0; i < m; i++) {
    		System.out.print("[");
    		for (j = 0; j < assignedPoints[i].length; j++)
    			System.out.print(assignedPoints[i][j] + " ");
    		System.out.println("]");
    	}
    }

}