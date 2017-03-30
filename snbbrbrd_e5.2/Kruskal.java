import edu.gwu.algtest.*;
import edu.gwu.util.*;
import java.util.*;

public class Kruskal extends UniformRandom implements SpanningTreeAlgorithm {


	int numVertices, i, j, place, count, saveI, saveJ;
	int[] set;

	GraphEdge[] sortMinEdge;
	GraphEdge testEdge, minEdge;

	double[] X, Y;
	double min, totalWeight;
	static double[][] adjMatrix, minST;

	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of Kruskal's Algorithm";
    }

    //Setting that property extractor since '95'
    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }

    /* 
     * Method getTreeWeight should return the weight of the 
     * minimum spanning tree that was computed most recently.
     */
    public double getTreeWeight() {
    	return totalWeight;
    }

    /* 
     * Method initialize will be given the number of vertices.
     */
    public void initialize(int numVertices) {

    	this.numVertices = numVertices;

    	
    	X = new double[numVertices];
    	Y = new double[numVertices];

    	//Fill X and Y arrays of random points
    	//System.out.print("X: [");
	    for (i = 0; i < numVertices; i++) {
	      X[i] = UniformRandom.uniform();
	      //System.out.print(X[i] + ", ");
	    }
	    //System.out.println("]");

	    //System.out.print("Y: [");
	    for (i = 0; i < numVertices; i++) {
	      Y[i] = UniformRandom.uniform();
	      //System.out.print(Y[i] + ", ");
	    }
	    //System.out.println("]");
	    

	    totalWeight = 0;


	  	//Create the Adjacency Matrix
	  	adjMatrix = new double[numVertices][numVertices];

	  	
	  	//Now iterate through matrix, computing distance between each point
	  	//And labeling their respective weights in the matrix accordingly
	  	for (i = 0; i < numVertices; i++) {
	  		for (j = 0; j < numVertices; j++) {
	  			if (i != j)
	  				adjMatrix[i][j] = computeDistance(X[i], Y[i], X[j], Y[j]);
	  			else
	  				adjMatrix[i][j] = 0;

	  			//System.out.print(adjMatrix[i][j] + " ");
	  		}
	  		//System.out.println();
	  	}
	  	

    }

    /*
     * Compute distance between two seperate points
     */
    public double computeDistance(double x1, double y1, double x2, double y2) {

    	return Math.sqrt ( ((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)) );
    
    }

    /*
     * Method minimumSpanningTree will be given an adjacency 
     * matrix of an undirected graph and will be expected to 
     * return the adjacency matrix corresponding to the minimum 
     * spanning tree. 
     */
    public double[][] minimumSpanningTree(double[][] adjMatrix) {

    	//Create minSpanningTree
    	minST = new double[numVertices][numVertices];

    	//Create Set to hold vertice groups
    	set = new int[numVertices];

    	//Initialize set; all vertices in seperate set
    	for (i = 0; i < numVertices; i++)
    		set[i] = i;

    	/*
    	for (i = 0; i < numVertices; i++) {
    		System.out.print("[");
    		for (j = 0; j < numVertices; j++) {
    			System.out.print(adjMatrix[i][j] + ", ");
    		}
    		System.out.println("]");
    	}
    	*/

    	//ArrayList<GraphEdge> sortMinEdge = new ArrayList<GraphEdge>();
    	sortMinEdge = new GraphEdge[(numVertices*(numVertices-1))/2];

    	//Sort edges of G in increasing order
    	for (count = 0; count < (numVertices*(numVertices-1))/2; count++) {

    		min = 100.0;
    		place = 1;

    		//Iterate through top half of matrix, finding the minimum 
    		//weight, then store in sortMinEdge
	    	for (i = 0; i < numVertices; i++) {

	    		for (j = place; j < numVertices; j++) {

	    			//Only consider minimum weights that are:
	    			//1. minimum &&
	    			//2. not contained in sortMinEdge

	    			testEdge = new GraphEdge(i, j, adjMatrix[i][j]);

	    			//System.out.println(adjMatrix[i][j] + " < " + min + " | " + testEdge.startVertex + " : " + testEdge.endVertex);
	    			if (adjMatrix[i][j] != 0 && adjMatrix[i][j] < min && !isContained(testEdge)) {
	    				min = adjMatrix[i][j];
	    				saveI = i;
	    				saveJ = j;
	    			}
	    		}

	    		place++;
	    	}

	    	minEdge = new GraphEdge(saveI, saveJ, min);

	    	//Add min edge to sortMinEdge list
	    	sortMinEdge[count] = minEdge;

	    	//System.out.println("\t ADDED: [" + minEdge.startVertex + ", " + minEdge.endVertex + "]");

	    }


	    for (i = 0; i < sortMinEdge.length; i++) {
	    	//System.out.println("[ " + sortMinEdge[i].startVertex + ", " + sortMinEdge[i].endVertex + ": " + sortMinEdge[i].weight + "]")
	    	

	    	//If u and v are not in the same set
	    	if (set[sortMinEdge[i].startVertex] != set[sortMinEdge[i].endVertex]) {


	    		union(sortMinEdge[i]);

	    		//Add edge to minST
	    		minST[sortMinEdge[i].startVertex][sortMinEdge[i].endVertex] = sortMinEdge[i].weight;
	    		minST[sortMinEdge[i].endVertex][sortMinEdge[i].startVertex] = sortMinEdge[i].weight;

	    		//System.out.println("Combining: " + sortMinEdge[i].startVertex + " and " + sortMinEdge[i].endVertex + " with weight: " + sortMinEdge[i].weight);

				totalWeight = totalWeight + sortMinEdge[i].weight;	    		

	    		/*
	    		System.out.print("\t Set: [");
	    		for (int y = 0; y < set.length; y++)
	    			System.out.print(set[y] + ", ");
	    		System.out.println("]");
	    		*/
	    		
	    	}
	    }

	    //System.out.println("Total MST weight: " + totalWeight);
    	return minST;


		/*     	
		1.   Initialize MST to be empty;
		2.   Place each vertex in its own set;
		3.   Sort edges of G in increasing-order;
		4.   for each edge e = (u,v) in order
		5.       if u and v are not in the same set
		6.           Add e to MST;
		7.           Compute the union of the two sets;
		8.       endif
		9.   endfor
		10.  return MST
		*/
    }

    public void union(GraphEdge minEdge) {
 

	    int saveEnd = set[minEdge.endVertex];

    	for (int z = 0; z < set.length; z++) {
    		//System.out.println("set[" + z + "] = " + set[z] + " | set[minEdge.endVertex]: " + set[minEdge.endVertex]);
    		if (set[z] == saveEnd)
    			set[z] = set[minEdge.startVertex];
    	}
    }

    public boolean isContained(GraphEdge testEdge) {

    	for (int q = 0; q < sortMinEdge.length; q++) {
    		if (sortMinEdge[q] != null) {
    			//System.out.println(sortMinEdge[q].startVertex + " : " + testEdge.startVertex + " | " + sortMinEdge[q].endVertex + " : " + testEdge.endVertex + " | " + sortMinEdge[q].weight + " : " + testEdge.weight);
	    		if (sortMinEdge[q].startVertex == testEdge.startVertex &&
	    				sortMinEdge[q].endVertex == testEdge.endVertex &&
	    					sortMinEdge[q].weight == testEdge.weight)
	    						return true;
    		}
    	}

    	return false;
    }

    /* 
     * Method minimumSpanningTree will be given an adjacency list 
     * of an undirected graph and will be expected to return the 
     * adjacency-list corresponding to the minimum spanning tree.
     */
    public GraphVertex[] minimumSpanningTree(GraphVertex[] adjList) {
    	return null;
    }

    
    public static void main(String args[]) {

    	Kruskal kMST = new Kruskal();

    	double avgWeight = 0.0;

    	/* 
    	 * 10 distinct tests of 100 iterations of MST graph creation,
    	 * averaged, and outputed to make a graph
    	 * avgWeight is reset inbetween each loop, vertices incremenet by 10
    	 */
    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(10);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(10): " + avgWeight / 100);

    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(20);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(20): " + avgWeight / 100);


    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(30);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(30): " + avgWeight / 100);

    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(40);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(40): " + avgWeight / 100);

    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(50);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(50): " + avgWeight / 100);

    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(60);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(60): " + avgWeight / 100);

     	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(70);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(70): " + avgWeight / 100);

    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(80);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(80): " + avgWeight / 100);

    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(90);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(90): " + avgWeight / 100);

    	avgWeight = 0.0;

    	for (int k = 0; k < 100; k++) {
    		kMST.initialize(100);
    		kMST.minimumSpanningTree(adjMatrix);

    		avgWeight = avgWeight + kMST.getTreeWeight();
    	}

    	System.out.println("MST Weight(100): " + avgWeight / 100);

    }
    

}