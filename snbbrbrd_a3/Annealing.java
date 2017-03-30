import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;

public class Annealing implements MTSPAlgorithm {

    //Global Temperature
    int temperature = 100;

	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of Annealing.java.";
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

    	double size = Math.floor(numCities / m), min = 999.0, realSize, cost1, cost2;

    	int remainder = numCities % m;

    	//Creat backup initial size
		realSize = size;

        //System.out.println("=================================| STARTING |==============================");

        //Populate visited array with unvisited cities
        for (i = 0; i < numCities; i++)
            visited[i] = 0;

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
	    			if ( j != k && computeDistance(points[j], points[k]) < min && visited[k] == 0) {
	    				min = computeDistance(points[j], points[k]);
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

        //While temperature still hot
        while (temperature > 0) {

            //Compute totalDistane of initial array
            cost1 = getTotalDist(assignedPoints, points);

            //System.out.println("Printing assignedPoints... cost: " + cost1);
            //printTours(assignedPoints, m);

            //System.out.println("");

            //Generate new swapped matrix
            int[][] newMatrix = neighborSwap(assignedPoints, points);
            cost2 = getTotalDist(newMatrix, points);

            //System.out.println("Printing newMatrix... cost: " + cost2);
            //printTours(newMatrix, m);

            //If newMatrix more optimal than initial Matrix
            if (cost2 < cost1) {
                //System.out.println("==> newMatrix more optimal");
                assignedPoints = newMatrix;
            }

            //Else, let tempFunction(acceptReject) decide
            else {
                if (acceptReject(assignedPoints, newMatrix, points)) {
                    //System.out.println("ACCEPTED!!");
                    assignedPoints = newMatrix;
                }
                /*
                else {
                    System.out.println("REJECTED!!");
                }
                */
            }
            //System.out.println("Temp: " + temperature);

            temperature = temperature - 1;
        
        }

	    //printTours(assignedPoints, m);
        
	    return assignedPoints;


    }

    /*
     * Will swap two random points in a matrix:
     *      -Generate two random points in points[]
     *      -If index of two random points lies in initial matrix, swap them
     */
    public int[][] neighborSwap(int[][] initial, Pointd[] points) {
        
        int i, j, x = 0, y = 0;
        int[][] newMatrix = new int[initial.length][initial[0].length];
        double rand1, rand2;
        Pointd p1, p2;

        //Copy initial inputed array into newArray
        copy(initial, newMatrix);

        while (x == y) {
            //Generate two random values
            rand1 = Math.random() * points.length;
            rand2 = Math.random() * points.length;

            //Cast random doubles to int
            x = (int) rand1;
            y = (int) rand2;

            //System.out.println("x: " + x + " | y: " + y);
        }

        //Set points to be swapped
        p1 = points[x];
        p2 = points[y];

        //System.out.println("p1: " + p1 + " | p2: " + p2);
        
        //Iterate through salesman in newMatrix
        for(i = 0; i < newMatrix.length; i++) {

            //Iterate through each city per salesman
            for(j = 0; j < newMatrix[i].length; j++) {

                //System.out.println("initial[" + i + "][" + j + "]: " + initial[i][j] + " =? swapIndex(p1): " + swapIndex(p1, points));
                //System.out.println("initial[" + i + "][" + j + "]: " + initial[i][j] + " =? swapIndex(p2): " + swapIndex(p2, points));
                //If city in inital Array equals index of random point p1 in points
                if(initial[i][j] == swapIndex(p1, points)) 
                    newMatrix[i][j] = swapIndex(p2, points);

                //Else if city in initial Array equals index of random point p2 in points
                else if (initial[i][j] == swapIndex(p2, points))
                    newMatrix[i][j] = swapIndex(p1, points);
            }
        }

        //Return new random array
        return newMatrix;
    }

    /*
     * Compares given Pointd p against set of Pointd's in points
     *      : Returns index if found, else -1
     */
    public int swapIndex(Pointd p, Pointd[] points) {
        
        int counter = 0;

        for(int i = 0; i < points.length; i++) {
            if(p.equals(points[i])) 
                return counter;
            
            counter++;
        }

        return -1;
    }

    /*
     * Computes total distance of an inputed matrix 
     */    
    public double getTotalDist(int[][] matrix, Pointd[] points) {
        
        int i, j;
        double total = 0;
        int[] costArray = new int[matrix.length];

        //Fill intial cost array with zeros
        for(i = 0; i < costArray.length; i++) 
            costArray[i] = 0;

        //Iterate through salesman
        for(i = 0; i < matrix.length; i++) 

            //Iterate through cities
            for(j = 1; j<matrix[i].length; j++) 

                //Update cost position
                costArray[i] += computeDistance(points[matrix[i][j-1]], points[matrix[i][j]]);

        //Sum up all costs in costArray, append to returning total variable
        for(i = 0; i < costArray.length; i++)
            total += costArray[i];
        
        return total;
    }

    /*
     * Temperature function that computed whether or not
     * to accept or reject a tour that is of sub-optimal length
     */    
    public boolean acceptReject(int[][] matrix1, int[][] matrix2, Pointd[] points) {
        
        //Generate random value based of temp Function
        double value = Math.exp( (-getTotalDist(matrix2, points) - getTotalDist(matrix1, points)) / temperature);

        //Generate random number
        double rand = Math.random();
     
        //If rand number < value, accept!
        if(rand < value) {
            return true;
        }

        return false;
    }
    

    /*
     * Compute distance between two seperate points
     */
    public double computeDistance(Pointd p1, Pointd p2) {

        double x1 = p1.x, x2 = p2.x;
        double y1 = p1.y, y2 = p2.y;


        return Math.sqrt ( ((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)) );
    
    }

    /*
     * Copy Matrix 1 into Matrix2
     */
    public void copy(int[][] matrix1, int[][] matrix2) {

        int i, j;

        for (i = 0; i < matrix1.length; i++)

            for (j = 0; j < matrix1[i].length; j++)

                matrix2[i][j] = matrix1[i][j];
    }

    /*
     * Prints for debugging purposes
     */
    public void printTours(int[][] matrix, int m) {

    	int i, j;

    	for (i = 0; i < m; i++) {
    	    System.out.print("[");
    		for (j = 0; j < matrix[i].length; j++)
    			System.out.print(matrix[i][j] + " ");
    	  	System.out.println("]");
    	}
        System.out.println("Finished printing..");
    }

    public static void main(String args[]) {

        Annealing myAnneal = new Annealing();
        Pointd[] points = new Pointd[8];

        Pointd p1 = new Pointd(5, 5);
        Pointd p2 = new Pointd(1, 6);
        Pointd p3 = new Pointd(4, 9);
        Pointd p4 = new Pointd(5, 3);
        Pointd p5 = new Pointd(8, 2);
        Pointd p6 = new Pointd(9, 7);
        Pointd p7 = new Pointd(3, 0);
        Pointd p8 = new Pointd(7, 5);
        //Pointd p9 = new Pointd(2, 8);

        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;
        points[4] = p5;
        points[5] = p6;
        points[6] = p7;
        points[7] = p8;
        //points[8] = p9;

        //myAnneal.computeTours(2, points);

    }

}

