import java.util.*;

public class Hull {

  // Maintain hull points internally: 
  private Pointd[] hull;

  // The data set is given to the constructor. 

  public Hull (Pointd[] points)
  {
    Pointd[] vertices;
    int i, n;

    // Record the number of vertices: 
    n = points.length;

    // 1. Make a copy of the vertices because we'll need to sort: 
    vertices = new Pointd[n];
    System.arraycopy (points, 0, vertices, 0, n);

    // 2. Find the rightmost, lowest point (it's on the hull). 
    int low = findLowest (vertices);

    // 3. Put that in the first position and sort the rest by 
    //    angle made from the horizontal line through "low". 
    swap (vertices, 0, low);	
    HullSortComparator comp = new HullSortComparator (vertices[0]);
    Arrays.sort (vertices, 1, vertices.length-1, comp);

    // 4. Remove collinear points: 
    n = removeCollinearPoints (vertices);		

    // 5. Now compute the hull. 
    hull = grahamScan (vertices, n);
  }

  public Pointd[] getPoints()
  {
    return hull;
  }

  
  Pointd[] grahamScan (Pointd[] p, int numPoints)
  {
    // 1. Create a stack and initialize with first two points: 
    HullStack hstack = new HullStack (numPoints);
    hstack.push (p[0]);
    hstack.push (p[1]);

    // 2. Start scanning points. 
    int i = 2;

    // 3. While scan not complete: 
    while (i < numPoints)
    {
      // 3.1 If the current point is on the hull, push next one. 
      //     We know a point is potentially on the hull if the 
      //     the angle is convex (a left turn). 
      if ( hstack.isHull (p[i]) )
        hstack.push (p[i++]);
      //     Else remove it. 
      else
        hstack.pop ();

      // NOTE: the isHull() method looks for "left" and "right" turns. 
    }

    // 4. Return all points still on the stack. 
    return hstack.hullArray();
  }

  private int findLowest (Pointd[] v)
  {
    // 1. Scan through points: 
    //    1.1 If y-value is lower, the point is lower. If the y-values 
    //        are the same, check that the x value is further to the right: 
    // 2. Return lowest point found: 
  }
  
  int removeCollinearPoints (Pointd[] p)
  {
    // Not shown 
  }
  
  
  void swap (Pointd[] data, int i, int j)
  {
    // Not shown 
  }

}