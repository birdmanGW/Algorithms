import edu.gwu.algtest.*;
import edu.gwu.util.*;
import java.util.*;

public class UndirectedDepthFirstAdjList extends UniformRandom implements UndirectedGraphSearchAlgorithm {

	LinkedList<GraphEdge>[] adjList;

	boolean isWeighted;

	int[] componentLabels, visitOrder, completionOrder;
	static int numVertices, numEdges, i, visitCount, completionCount, currComponentLabel;

	GraphEdge testEdge = new GraphEdge (-1, -1, 0);


	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of UndirectedDepthFirstAdjList";
    }

    //Setting that property extractor since '95'
    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }

	public GraphEdge[] articulationEdges() {
		return null;
	}

	public int[] articulationVertices() {
		return null;
	}
	
	/*
	 * componentLabels should return the component 
	 * label for each vertex for an undirected graph.
	 */
	public int[] componentLabels() {
		return componentLabels;
	}

	/*
	 * depthFirstCompletionOrder should return an array 
	 * where the i-th entry is the completion time (order) 
	 * of vertex i in the depth first search.
	 */
	public int[] depthFirstCompletionOrder() {
		return completionOrder;
	}

	/*
	 * depthFirstVisitOrder should return an array of vertices, 
	 * of exactly the same length as the number of vertices.
	 */
	public int[] depthFirstVisitOrder() {

		//Initialize all counts to -1
		visitCount = -1;
		completionCount = -1;
		currComponentLabel = -1;

		//Initialize visit and completeionOrder arrays
		visitOrder = new int[numVertices];
		completionOrder = new int[numVertices];

		//Intiialize values of visit and completionOrder arrays to -1
		for (i = 0; i < numVertices; i++) {
			visitOrder[i] = -1;
			completionOrder[i] = -1;
		}


		//Need to iterate through each vertex to check if visited
		for (i = 0; i < numVertices; i++) {
			//If vertex unvisited
			if (visitOrder[i] < 0) {
				currComponentLabel  = currComponentLabel + 1;
				depthFirstAdjacencyRecursive(i);
			}
		}
		

		return visitOrder;
	}

	/*
	 * Recursive portion of visit order
	 */
	public void depthFirstAdjacencyRecursive(int v) {

		//Mark vertex v as visited
		visitCount = visitCount + 1;
		visitOrder[v] = visitCount;

		//Mark the component label
		componentLabels[v] = currComponentLabel;

		//Iterate through vertex v connected edges
		for (int j = 0; j < adjList[v].size(); j++) {

			//Get ith edge in linkedList
			GraphEdge nextEdge = adjList[v].get(j);

			//If nextEdge not null
			if (nextEdge != null) {
				//Check if nextEdge unvisited
				if (visitOrder[nextEdge.endVertex] < 0) {
					depthFirstAdjacencyRecursive(nextEdge.endVertex);
				}
			}
		}

		//Done with current recursive call, update completion count and mark in completeionOrder array
		completionCount = completionCount + 1;
		completionOrder[v] = completionCount;

	}

	public int[] breadthFirstVisitOrder() {
		return null;
	}

	public boolean existsCycle() {
		return false;
	}

	public boolean existsOddCycle() {
		return false;
	}

	/*
	 * initialize is given the number of vertices, 
	 * which are assumed to be numbered 0, 1, 2, ...,numVertices-1, 
	 * and is informed whether the graph edges will carry weights.
	 */
	public void initialize(int numVertices, boolean isWeighted) {

		//visitOrder = new int[numVertices];
		//completionOrder = new int[numVertices];

		//Create AdjList for graph vertices, if graph wont be empty
		if (numVertices > 0) 
			adjList = new LinkedList[numVertices];
		else 
			return;

		//Initialize compnonetLables array and set global Verticies count
		componentLabels = new int[numVertices];
		this.numVertices = numVertices;

		//Initialize visitOrder and completeionOrder
		for (i = 0; i < this.numVertices; i++) {
			adjList[i] = new LinkedList<GraphEdge>();
		}

		//Set global isWeighted variable
		this.isWeighted = isWeighted;
	}

	/*
	 * insertUndirectedEdge is called with the endpoints of the edge.
	 */
	public void insertUndirectedEdge(int startVertex, int endVertex, double weight) {

		//If not weighted, set default weight value to 1.0
		if (!isWeighted)
			weight = 1.0;

		//Set testEdge start and end vertices
		testEdge.startVertex = startVertex;
		testEdge.endVertex = endVertex;

		//Check is edge already exists in adjList
		if (adjList[startVertex].contains(testEdge)) {
			return;
		}

		//Create start->end edge
		GraphEdge newEdge = new GraphEdge(startVertex, endVertex, weight);
		adjList[startVertex].addLast(newEdge);

		//Next, create end<-start edge
		GraphEdge newEdge2 = new GraphEdge(endVertex, startVertex, weight);
		adjList[endVertex].addLast(newEdge2);

		numEdges++;

	}

	/*
	 * numConnectedComponents should return the 
	 * number of of connected components for an undirected graph.
	 */
	public int numConnectedComponents() {


		//Create an arrayList to store connectedComponents
		ArrayList<Integer> arrList = new ArrayList<Integer>();

		//For every component in componentLabels
 		for(int i = 0; i < componentLabels.length; i++)
 			//If component does not exist in arrList, add it! (removes duplicates from componentLabels)
    		if(!arrList.contains(componentLabels[i]))
        		arrList.add(componentLabels[i]);

		return arrList.size();
	}

	public static boolean randomCoinFlip(double p) {
		if (UniformRandom.uniform() < p)
	    	return true;
	  	else
	    	return false;
    }

	
	public static void main(String args[]) {

		UndirectedDepthFirstAdjList test = new UndirectedDepthFirstAdjList();

		/*
		 * Edit this p value to change edge creation
		 * probability.
		 */
		double p = .00;
		int i, j;

		int avg10 = 0, avg20 = 0;

		//21 runs of 50 graphs with constant p values, incrementing by .05 (10 vertices)
		for (int z = 0; z < 21; z++) {

			for (int y = 0; y < 50; y++) {

				test.initialize(10, false);

				//Create the random edges
				for (i = 0; i < 10; i++) {
					for (j = 0; j < 10; j++) {
						//If two edges are not equal, and the honorable 
						//coin flipping gods have deemed them worthy of an edge
						//grant that edge, and fight the good fight!
						if (i != j && test.randomCoinFlip(p)) {
							test.insertUndirectedEdge(i, j, 1.0);
						}

					}
				}
				//Graph complete..
				
				//Perform DFS test
				int[] arr = test.depthFirstVisitOrder();
				int[] arr2 = test.depthFirstCompletionOrder();
				int[] arr3 = test.componentLabels();

				avg10 = avg10 + test.numConnectedComponents();

			}
			avg10 = avg10 / 50;

			System.out.println("50 random tests completed for 10 vertices. P = " + p + " | AVG # Components: " + avg10);

			p = p + .05;
			avg10 = 0;

		}

		System.out.println();
		p = 0;

		//21 runs of 50 graphs with constant p values, incrementing by .05 (20 vertices)
		for (int z = 0; z < 21; z++) {

			for (int y = 0; y < 50; y++) {

				test.initialize(20, false);

				//Create the random edges
				for (i = 0; i < 20; i++) {
					for (j = 0; j < 20; j++) {
						//If two edges are not equal, and the honorable 
						//coin flipping gods have deemed them worthy of an edge
						//grant that edge, and fight the good fight!
						if (i != j && test.randomCoinFlip(p)) {
							test.insertUndirectedEdge(i, j, 1.0);
						}

					}
				}
				//Graph complete..
				
				//Perform DFS test
				int[] arr = test.depthFirstVisitOrder();
				int[] arr2 = test.depthFirstCompletionOrder();
				int[] arr3 = test.componentLabels();

				avg20 = avg20 + test.numConnectedComponents();

			}
			avg20 = avg20 / 50;

			System.out.println("50 random tests completed for 20 vertices. P = " + p + " | AVG # Components: " + avg20);

			p = p + .05;
			avg20 = 0;

		}
	}
	
}