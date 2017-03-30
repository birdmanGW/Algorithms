import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;

public class Greedy implements MTSPAlgorithm {

	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of Greedy.java.";
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
     * A "greedy" algorithm in Greedy.java that will execute in polynomial time. 
     * Note: implement a "greedy" algorithm (that builds a solution step by step) 
     * as opposed to a "greedy-local-search" algorithm (that explores the state space).
     *
     */
    public int[][] computeTours(int m, Pointd[] points) {

    	int i, j, k, minIndex = 0, numCities = points.length, count = 0;
    	int[] visited = new int[numCities];

    	int[][] assignedPoints = new int[m][];

    	double size = Math.floor(numCities / m), min = 999.0, realSize;

    	int remainder = numCities % m;

    	//Populate visited array with unvisited cities
    	for (i = 0; i < numCities; i++)
    		visited[i] = 0;

    	//Creat backup initial size
		realSize = size;

    	for (i = 0; i < m; i++) {

    		//Roll back to realSize
    		size = realSize;

    		//If remainder exists, append size and decrement remainder
    		if (remainder > 0) {
    			size++;
    			remainder--;
    		} 

    		//Create column of variable size size (Variable becase could have remainder)
    		assignedPoints[i] = new int[ (int)size ];
    		//System.out.println("i: " + i + " || count: " + count + " || minIndex: " + minIndex);

    		//Check if minIndex (Most recently visited city) is marked unvisited
    		if (visited[minIndex] == 0) {
				assignedPoints[i][count++] = minIndex;
				visited[minIndex] = 1;
    		}

			//Iterate through salesman designated size
    		while (count < size) {

				j = minIndex;

				//Iterate through each city
	    		for (k = 0; k < numCities; k++) {

	    			//If current computed distance less than minimum, and city unvisited
	    			if ( j != k && computeDistance(points[j].x, points[j].y, points[k].x, points[k].y) < min && visited[k] == 0) {
	    				min = computeDistance(points[j].x, points[j].y, points[k].x, points[k].y);
	    				minIndex = k;
	    			}
	    			//System.out.println("DEBUG: j: " + j + " | k: " + k + " | minIndex: " + minIndex + " | count: " + count);
	    		}
	    		//Update visited array
	    		visited[minIndex] = 1;
	    		//Update our int[][]
	    		assignedPoints[i][count++] = minIndex;
	    		//Reset min
	    		min = 9999.0;
	    	} 

	    count = 0;
    	//System.out.println("Next salesman...");
    	}

	    //printTours(assignedPoints, m);
        
	    return assignedPoints;


    }

    /*
     * Compute distance between two seperate points
     */
    public double computeDistance(double x1, double y1, double x2, double y2) {

        return Math.sqrt ( ((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)) );
    
    }

    /*
     * Prints for debugging purposes
     */
    public void printTours(int[][] assignedPoints, int m) {

    	int i, j;

    	for (i = 0; i < m; i++) {
    	    System.out.print("[");
    		for (j = 0; j < assignedPoints[i].length; j++)
    			System.out.print(assignedPoints[i][j] + " ");
    	  	System.out.println("]");
    	}
    }

    public static void main(String args[]) {

        Greedy myGreed = new Greedy();
        Pointd[] points = new Pointd[9];

        Pointd p1 = new Pointd(5, 5);
        Pointd p2 = new Pointd(1, 6);
        Pointd p3 = new Pointd(4, 9);
        Pointd p4 = new Pointd(5, 3);
        Pointd p5 = new Pointd(8, 2);
        Pointd p6 = new Pointd(9, 7);
        Pointd p7 = new Pointd(3, 0);
        Pointd p8 = new Pointd(7, 5);
        Pointd p9 = new Pointd(2, 8);

        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;
        points[4] = p5;
        points[5] = p6;
        points[6] = p7;
        points[7] = p8;
        points[8] = p9;


        //myGreed.computeTours(2, points);

    }

}

