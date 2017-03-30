import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;


public class NaiveRectIntersection implements RectangleSetIntersectionAlgorithm{

	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of NaiveRectIntersection";
    }

    //Setting that property extractor since '95'
    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }

    public IntPair[] findIntersections(IntRectangle[] rectSet1, IntRectangle[] rectSet2) {

    	/*  
    	 *  Need to iterate through both sets of Rectangles, comparing 
    	 *  Rectangles only from different sets
    	 */

    	//Initialize ip to null
    	IntPair ip = null;

    	//Create an arrayList to hold the initially recorded intersection
    	ArrayList<IntPair> ipArrayList = new ArrayList<IntPair>();

    	//System.out.println("Set1.length: " + rectSet1.length);
    	//System.out.println("Set2.length: " + rectSet2.length);

    	//Iterate through rectSet1 | i
    	for (int i = 0; i < rectSet1.length; i++) {

    		//System.out.println("\t Set1 BR: " + rectSet1[i].bottomRight + " | Set1 TL: " + rectSet1[i].topLeft + " | id: " + rectSet1[i].ID);

    		//Iterate through rectSet2 | j
    		for (int j = 0; j < rectSet2.length; j++) {

    			//System.out.println("Set2 BR: " + rectSet2[j].bottomRight + " | Set2 TL: " + rectSet2[j].topLeft + " | id: " + rectSet2[j].ID);

    			//Case 1: Compare bottomRight of rectSet1 with x and y ranges of rectSet2
    			if ( ((rectSet2[j].topLeft.x <= rectSet1[i].bottomRight.x) && (rectSet1[i].bottomRight.x <= rectSet2[j].bottomRight.x)) &&
    				    	((rectSet2[j].bottomRight.y <= rectSet1[i].bottomRight.y) && (rectSet1[i].bottomRight.y <= rectSet2[j].topLeft.y)) ) {
    							ip = new IntPair(rectSet1[i].ID, rectSet2[j].ID);
    							//System.out.println("Case1: " + ip.i + " | " + ip.j);
    							ipArrayList.add(ip);
    			}
    			//Case 2: Compare topLeft of rectSet1 with x and y ranges of rectSet2
    			else if ( ((rectSet2[j].topLeft.x <= rectSet1[i].topLeft.x) && (rectSet1[i].topLeft.x <= rectSet2[j].bottomRight.x)) &&
    				 	    ((rectSet2[j].bottomRight.y <= rectSet1[i].topLeft.y) && (rectSet1[i].topLeft.y <= rectSet2[j].topLeft.y)) ) {
    							ip = new IntPair(rectSet1[i].ID, rectSet2[j].ID);
    							//System.out.println("Case2: " + ip.i + " | " + ip.j);
    							ipArrayList.add(ip);
    			}
    			//Case 3: Compare x of topLeft and y of bottomRight (rectSet1) with x and y ranges of rectSet2
    			else if ( ((rectSet2[j].topLeft.x <= rectSet1[i].topLeft.x) && (rectSet1[i].topLeft.x <= rectSet2[j].bottomRight.x)) &&
    				 		((rectSet2[j].bottomRight.y <= rectSet1[i].bottomRight.y) && (rectSet1[i].bottomRight.y <= rectSet2[j].topLeft.y)) ) {
    							ip = new IntPair(rectSet1[i].ID, rectSet2[j].ID);
    							//System.out.println("Case3: " + ip.i + " | " + ip.j);
    							ipArrayList.add(ip);
    			}
    			//Case 4: Compare x of bottomRight and y of topLeft (rectSet1) with x and y ranges of rectSet2
    			else if ( ((rectSet2[j].topLeft.x <= rectSet1[i].bottomRight.x) && (rectSet1[i].bottomRight.x <= rectSet2[j].bottomRight.x)) &&
    				 		((rectSet2[j].bottomRight.y <= rectSet1[i].topLeft.y) && (rectSet1[i].topLeft.y <= rectSet2[j].topLeft.y)) ) {
    							ip = new IntPair(rectSet1[i].ID, rectSet2[j].ID);
    							//System.out.println("Case4: " + ip.i + " | " + ip.j);
    							ipArrayList.add(ip);
    			}
    			//Case 5: Compare x and y of bottomRight of rectSet1 with x and y of rectSet2 topLeft and bottomRight
    			else if ( ((rectSet1[i].topLeft.x <= rectSet2[j].bottomRight.x) && (rectSet2[j].bottomRight.x <= rectSet1[i].bottomRight.x)) &&
    						((rectSet1[i].bottomRight.y <= rectSet2[j].bottomRight.y) && (rectSet2[j].bottomRight.y <= rectSet1[i].topLeft.y)) ) {
    							ip = new IntPair(rectSet1[i].ID, rectSet2[j].ID);
    							//System.out.println("Case5: " + ip.i + " | " + ip.j);
    							ipArrayList.add(ip);
    			}
    			//Case 6: Compare topLeft of rectSet2 with x and y of rectSet1 topLeft and bottomRigtht 
    			else if ( ((rectSet1[i].topLeft.x <= rectSet2[j].topLeft.x) && (rectSet2[j].topLeft.x <= rectSet1[i].bottomRight.x)) &&
    						((rectSet1[i].bottomRight.y <= rectSet2[j].topLeft.y) && (rectSet2[j].topLeft.y <= rectSet1[i].topLeft.y)) ) {
     							ip = new IntPair(rectSet1[i].ID, rectSet2[j].ID);
    							//System.out.println("Case6: " + ip.i + " | " + ip.j);
    							ipArrayList.add(ip);   				
    			}
    		}
    	}

    	//Create an IntPair array to hold and return all recorded intersections
    	IntPair[] ipArray = new IntPair[ipArrayList.size()];

    	//Iterate through arraylist, copying all recorded intersections into ipArray[]
    	for (int c = 0; c < ipArrayList.size(); c++) 
    		ipArray[c] = ipArrayList.get(c);

    	/*
    	for (int c = 0; c < ipArray.length; c++) 
    		System.out.print(ipArray[c] + " ");
    	*/

    	//Check if ipArray is empty
    	if (isEmpty(ipArray))
    		return null;
    	//If not empty, return ipArray!
    	else
    		return ipArray;
    }

    public boolean equals(Object obj) {
    	return false;
    }

    //Quick method to determine if ipArray is empty
    public boolean isEmpty(IntPair[] ipArray) {
    	for (int i = 0; i < ipArray.length; i++) {
    		if (ipArray[i] != null)
    			return false;
    	}
    	return true;

    }

    public String toString() {
    	return null;
    }

    public int getID() {
    	return 0;
    }

    public static void resetIDs() {

    }

    public int hashCode() {
    	return 0;
    }
  
}