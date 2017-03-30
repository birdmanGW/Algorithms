import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;


public class FilterTreeRectIntersection implements RectangleSetIntersectionAlgorithm{

    //Global static variables root, and newNode
    static FilterTreeNode newNode,  root = null;

    //Global LinkedList hold intersections until copied to ipArray
    LinkedList intersectionSet;

	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of FilterTreeRectIntersection";
    }

    //Setting that property extractor since '95'
    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }

    public IntPair[] findIntersections(IntRectangle[] rectSet1, IntRectangle[] rectSet2) {

        IntPair[] ipArray;

        //First, construct filter Tree from rectSet1
        initialize(rectSet1);
        makeFilterTree(rectSet1);

        intersectionSet = new LinkedList();

        //Iterate through given set of rectangles
        //Query against rectangles in FilterTree's LinkedLists
        for (int i = 0; i < rectSet2.length; i++) 
            filterTreeSearch(rectSet2[i], root);

        //Check if intersectionSet is not null
        if (intersectionSet.size() != 0)
            //Make IntPair[] of size intersectionSet.size()
            ipArray = new IntPair[intersectionSet.size()];
        else
            return null;


        for (int i = 0; i < intersectionSet.size(); i++) 
            ipArray[i] = (IntPair) intersectionSet.get(i);

        return ipArray;

    }

    //Searches constrcuted FilterTree against single rectangle
    //Recursively searches each quadrant rectangle might intersect with
    //And checks if an intersection is made, if so, add to global list
    public void filterTreeSearch(IntRectangle rect, FilterTreeNode node) {

        IntPair intersection;
        int quadrant, realQuadrant;


        //If node LinkedList is not empty
        if (node.rectList.size() != 0) {
            //Iterate through LinkedList's rectangles
            for (int i = 0; i < node.rectList.size(); i++) {

                //Determine if rect intersects current LinkedList rectangle
                intersection = findIntersection(rect, (IntRectangle) node.rectList.get(i));

                //If intersection exists, populate intersectionSet3 LinkedList
                if (intersection != null) {
                    intersectionSet.add(intersection);
                    //System.out.println("\tADDING: " + intersection);
                }
            }
        }
        
        //Check if a node for quadrant 0 exists
        if (node.quadrants[0] != null) {
            //If so, check if rect intersects nodes bisectors
            if (rectNodeIntersection(rect, node.quadrants[0])) {
                //If so, recursievely search that node
                filterTreeSearch(rect, node.quadrants[0]);
            }
        }
        //Check if a node for quadrant 0 exists
        if (node.quadrants[1] != null) {
            //If so, check if rect intersects nodes bisectors
            if (rectNodeIntersection(rect, node.quadrants[1])) {
                //If so, recursievely search that node
                filterTreeSearch(rect, node.quadrants[1]);
            }
        }
        //Check if a node for quadrant 0 exists
        if (node.quadrants[2] != null) {
            //If so, check if rect intersects nodes bisectors
            if (rectNodeIntersection(rect, node.quadrants[2])) {
                //If so, recursievely search that node
                filterTreeSearch(rect, node.quadrants[2]);
            }
        }
        //Check if a node for quadrant 0 exists
        if (node.quadrants[3] != null) {
            //If so, check if rect intersects nodes bisectors
            if (rectNodeIntersection(rect, node.quadrants[3])) {
                //If so, recursievely search that node
                filterTreeSearch(rect, node.quadrants[3]);
            }
        }
        //Else return
        return;
        
    }

    //Determines if two rectangles intersect one another (Taken from Naive)
    public IntPair findIntersection(IntRectangle rect2, IntRectangle rect1) {

        /*  
         *  Compares if two distint rectangles intersect
         *  Accounts for Rectangle inside another rectangle
         */

        //Initialize ip to null
        IntPair ip = null;

                //Case 1: Compare bottomRight of rectSet1 with x and y ranges of rectSet2
                if ( ((rect2.topLeft.x <= rect1.bottomRight.x) && (rect1.bottomRight.x <= rect2.bottomRight.x)) &&
                            ((rect2.bottomRight.y <= rect1.bottomRight.y) && (rect1.bottomRight.y <= rect2.topLeft.y)) ) {
                                ip = new IntPair(rect1.ID, rect2.ID);
                                //System.out.println("Case1: " + ip.i + " | " + ip.j);
                                return ip;
                }
                //Case 2: Compare topLeft of rectSet1 with x and y ranges of rectSet2
                else if ( ((rect2.topLeft.x <= rect1.topLeft.x) && (rect1.topLeft.x <= rect2.bottomRight.x)) &&
                            ((rect2.bottomRight.y <= rect1.topLeft.y) && (rect1.topLeft.y <= rect2.topLeft.y)) ) {
                                ip = new IntPair(rect1.ID, rect2.ID);
                                //System.out.println("Case2: " + ip.i + " | " + ip.j);
                                return ip;
                }
                //Case 3: Compare x of topLeft and y of bottomRight (rectSet1) with x and y ranges of rectSet2
                else if ( ((rect2.topLeft.x <= rect1.topLeft.x) && (rect1.topLeft.x <= rect2.bottomRight.x)) &&
                            ((rect2.bottomRight.y <= rect1.bottomRight.y) && (rect1.bottomRight.y <= rect2.topLeft.y)) ) {
                                ip = new IntPair(rect1.ID, rect2.ID);
                                //System.out.println("Case3: " + ip.i + " | " + ip.j);
                                return ip;
                }
                //Case 4: Compare x of bottomRight and y of topLeft (rectSet1) with x and y ranges of rectSet2
                else if ( ((rect2.topLeft.x <= rect1.bottomRight.x) && (rect1.bottomRight.x <= rect2.bottomRight.x)) &&
                            ((rect2.bottomRight.y <= rect1.topLeft.y) && (rect1.topLeft.y <= rect2.topLeft.y)) ) {
                                ip = new IntPair(rect1.ID, rect2.ID);
                                //System.out.println("Case4: " + ip.i + " | " + ip.j);
                                return ip;
                }
                //Case 5: Compare x and y of bottomRight of rectSet1 with x and y of rectSet2 topLeft and bottomRight
                else if ( ((rect1.topLeft.x <= rect2.bottomRight.x) && (rect2.bottomRight.x <= rect1.bottomRight.x)) &&
                            ((rect1.bottomRight.y <= rect2.bottomRight.y) && (rect2.bottomRight.y <= rect1.topLeft.y)) ) {
                                ip = new IntPair(rect1.ID, rect2.ID);
                                //System.out.println("Case5: " + ip.i + " | " + ip.j);
                                return ip;
                }
                //Case 6: Compare topLeft of rectSet2 with x and y of rectSet1 topLeft and bottomRigtht 
                else if ( ((rect1.topLeft.x <= rect2.topLeft.x) && (rect2.topLeft.x <= rect1.bottomRight.x)) &&
                            ((rect1.bottomRight.y <= rect2.topLeft.y) && (rect2.topLeft.y <= rect1.topLeft.y)) ) {
                                ip = new IntPair(rect1.ID, rect2.ID);
                                //System.out.println("Case6: " + ip.i + " | " + ip.j);
                                return ip;                
                }
                else
                    return null;

    }

    //Initialize root and recursively build tree
    public void makeFilterTree(IntRectangle[] rectSet) {

        //For each rectangle in rectSet, call makeFilterTree and
        //check intersection with current nodes bisector
        for (int i = 0; i < rectSet.length; i++) {
                makeFilterTree(rectSet[i], root);
        }

    }

    //Recursively builds tree by checking if rectangle intersects current
    //nodes bisector, if not determines quadrant rectangle falls into
    //Checks if quadrant is null (if so, create newNode) else calls
    //MakeFilterTree on quadrant found, repeat
    private void makeFilterTree(IntRectangle rect, FilterTreeNode node) {

        /*
        //Check if current rectangle intersects nodes bisectors
        if (rectNodeIntersection(rect, node)) {
            //System.out.println("Intersection found: " + rect + " | " + node);
            //If so, add to node.linkedlist and return
            node.rectList.add(rect);
            return;
        }


        //determine quadrant q of rectSet[i]
        int quadrant = findQuadrant(rect, node);
        int realQuadrant = quadrant;

        if (quadrant == 3)
            realQuadrant = 1;
        else if (quadrant == 1)
            realQuadrant = 3;

        //If no node currently resides in node.quadrants[quadrant], make newNode
        if (node.quadrants[realQuadrant] == null) {

            //create newNode off of q from current 
            //Quadrant HAS to be 3 to create bisectors in quadrant 1
            //Quadrant HAS to be 1 to create bisectors in quadrant 3
            newNode = new FilterTreeNode(quadrant, node);

            node.quadrants[realQuadrant] = newNode;

            makeFilterTree(rect, newNode);
        } else {

        //Recursively call itself until node is bisected
        makeFilterTree(rect, node.quadrants[realQuadrant]);

        }
        */

        //Graph Quadrants are as follows
        // | 0 | 1 |
        // | 2 | 3 |
        //Attempted to at first use the first constructor for FilterTreeNode, but after dealing with the swapping of quadrants, 
        //and the professors advise to use the second constructor, found it easier to abide by this standard

        //In adittion, I thought the first constructor was gthe cause for my slow and freezing TestAlgorithm


        /* Create four seperate quadrants with bounds of initial node */

        
        FilterTreeNode quad0 = new FilterTreeNode(node.leftX, node.midX, node.midY, node.topY, node.level+1);

        FilterTreeNode quad1 = new FilterTreeNode(node.midX, node.rightX,  node.midY, node.topY, node.level+1);

        FilterTreeNode quad2 = new FilterTreeNode(node.leftX, node.midX, node.botY, node.midY, node.level+1);

        FilterTreeNode quad3 = new FilterTreeNode(node.midX, node.rightX, node.botY,node.midY, node.level+1);

        //If rect falls in quad0
        if (rectNodeIntersection(rect, quad0)) {
            //If rect falls in quad0 mesh
            if (fallInMesh(rect, quad0)){

                    //If quad0 does not exist, create one
                    if (node.quadrants[0] == null){
                        node.quadrants[0] = quad0;
                        makeFilterTree(rect, node.quadrants[0]);
                    }
                    else
                        makeFilterTree(rect, node.quadrants[0]);
            }
            else
                node.rectList.add(rect);
        }

        else if (rectNodeIntersection(rect, quad1)) {
            //If rect falls in quad1 mesh
            if (fallInMesh(rect, quad1)){

                    //If quad1 does not exist, create one
                    if (node.quadrants[1] == null){
                        node.quadrants[1] = quad1;
                        makeFilterTree(rect, node.quadrants[1]);
                    }
                    else
                        makeFilterTree(rect, node.quadrants[1]);     
            }
            else
                node.rectList.add(rect);
        }

        else if (rectNodeIntersection(rect, quad2)) {
            //If rect falls in quad2 mesh
            if (fallInMesh(rect, quad2)){

                    //If quad2 does not exist, create one
                    if (node.quadrants[2] == null){
                        node.quadrants[2] = quad2;
                        makeFilterTree(rect, node.quadrants[2]);
                    }
                    else
                        makeFilterTree(rect, node.quadrants[2]);
            }
            else
                node.rectList.add(rect);
        }

        else if (rectNodeIntersection(rect, quad3)) {
            //If rect falls in quad3 mesh
            if (fallInMesh(rect, quad3)){

                    //If quad3 does not exist, create one
                    if (node.quadrants[3] == null){
                        node.quadrants[3] = quad3;
                        makeFilterTree(rect, node.quadrants[3]);
                    }

                    else
                        makeFilterTree(rect, node.quadrants[3]);
            }
            else
                node.rectList.add(rect);
        }

        return;
        

        
    }

    //Given a rectangle and FilterTreeNode, determine if rect falls in node's mesh
    public boolean fallInMesh(IntRectangle rect, FilterTreeNode node) {


        if (node.leftX < rect.topLeft.x && node.topY > rect.topLeft.y &&
            node.rightX > rect.bottomRight.x && node.botY < rect.bottomRight.y)
                return true;
        else
                return false;
        
    }
    


    //Determines if a rectangle falls inside the bounds of a node
    public boolean rectNodeIntersection(IntRectangle rect, FilterTreeNode node) {

        int top = node.topY;
        int bot = node.botY;
        int left = node.leftX;
        int right = node.rightX;

        //Compare rect x and y point coordinates fall within the bounds of node
        if ( (left <= rect.bottomRight.x && rect.topLeft.x <= right) &&
              (bot <= rect.topLeft.y && rect.bottomRight.y <= top) )
                    return true;
        else
                    return false;
        
        
    }

    //InOrder print of FIlterTree
    public void printFilterTree(FilterTreeNode node) {


        if (node != null) {
            System.out.println("Node: " + node + " Node.quadrant: ");
            
            for (int i = 0; i <= 3; i++)
            System.out.println("\t Quadrant: " + i + " | " + node.quadrants[i]);
            
            System.out.println("Rectangle Linked List: ");

            for (int i = 0; i < node.rectList.size(); i++)
                System.out.println(i + ": " + node.rectList.get(i));

        }

        if (node.quadrants[0] != null)
            printFilterTree(node.quadrants[0]);
        if (node.quadrants[1] != null)
            printFilterTree(node.quadrants[1]);
        if (node.quadrants[2] != null)
            printFilterTree(node.quadrants[2]);
        if (node.quadrants[3] != null)
            printFilterTree(node.quadrants[3]);
    }

    //Initialze root to first level of tree
    public void initialize(IntRectangle[] rectSet) {
        //System.out.println("Root initialized");

        int maxX = 0;
        int maxY = 0;

        for (int i = 0; i < rectSet.length; i++) {

            if (rectSet[i].bottomRight.x > maxX)
                maxX = rectSet[i].bottomRight.x;
            if (rectSet[i].topLeft.y > maxY)
                maxY = rectSet[i].topLeft.y;
        }

        root = new FilterTreeNode(0, maxX, 0, maxY, 1);
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

    
    public static void main (String args[]) {

        FilterTreeRectIntersection FTRI = new FilterTreeRectIntersection();

        //Make a bunch of rectangles
        IntRectangle r1 = new IntRectangle(4, 9, 6, 7);
        IntRectangle r2 = new IntRectangle(4, 7, 6, 5);
        IntRectangle r3 = new IntRectangle(4, 5, 6, 3);
        IntRectangle r4 = new IntRectangle(7, 5, 8, 4);
        IntRectangle r5 = new IntRectangle(2, 5, 3, 4);

        //Throw em all in a set
        IntRectangle[] rectSet1 = new IntRectangle[5];

        //Fill dat set
        rectSet1[0] = r1;
        rectSet1[1] = r2;
        rectSet1[2] = r3;
        rectSet1[3] = r4;
        rectSet1[4] = r5;

        //Make a bunch of rectangles
        IntRectangle r6 = new IntRectangle(89, 57, 91, 37);
        IntRectangle r7 = new IntRectangle(12, 80, 45, 66);
        IntRectangle r8 = new IntRectangle(28, 40, 39, 29);
        IntRectangle r9 = new IntRectangle(18, 70, 32, 60);
        IntRectangle r10 = new IntRectangle(79, 94, 82, 90);

        //Throw em all in a set
        IntRectangle[] rectSet2 = new IntRectangle[5];

        //Fill dat set
        rectSet2[0] = r6;
        rectSet2[1] = r7;
        rectSet2[2] = r8;
        rectSet2[3] = r9;
        rectSet2[4] = r5;

        FTRI.initialize(rectSet2);

        //make dat tree
        FTRI.makeFilterTree(rectSet2);

        //Print dat tree
        FTRI.printFilterTree(root);

        //Do this if you want
        //FTRI.findIntersections(rectSet1, rectSet2);
    } 
  
}