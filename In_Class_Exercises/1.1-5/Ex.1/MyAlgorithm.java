
public class MyAlgorithm {

  static double distance (Pointd p, Pointd q)
  {
    // Return the Euclidean distance between the two points. 
    return Math.sqrt ( (p.x-q.x)*(p.x-q.x) + (p.y-q.y)*(p.y-q.y) );
  }

  static double findMaxDistance3 (Pointd[] points, int a, int b, int c)
  {
    // Compute the distance between each of ab, ac and bc,  
    // and return the largest. 

    double dist_ab = distance (points[a], points[b]);
    double dist_bc = distance (points[c], points[b]);
    double dist_ac = distance (points[a], points[c]);
    double maxDist = dist_ab;
    if (dist_bc > maxDist)
      maxDist = dist_bc;
    if (dist_ac > maxDist)
      maxDist = dist_ac;
    return maxDist;
  }

  boolean turnComplete = false;

  int nextCounterClockwise (Pointd[] points, int i)
  {
    // Find the next point in the list going counterclockwise, 
    // that is, in increasing order. Go back to zero if needed. 
    // Return the array index. 
    if (i >= points.length-1) {
      turnComplete = true;
      return 0;
    }
    else
      return (i+1);

  }

  int prevCounterClockwise (Pointd[] points, int i)
  {
    if (i <= 0)
      return points.length-1;
    else
      return (i - 1);
  }
  

  int findAntiPodalIndex (Pointd[] hullPoints, int currentIndex, int startAntiPodalIndex)
  {
    // Given a convex polygon, an index into the vertex array (a particular vertex), 
    // find it's antipodal vertex, the one farthest away. Start the search from 
    // a specified vertex, the "startAntiPodalIndex" 

    // Short forms:
    int b = currentIndex;
    int c = startAntiPodalIndex;

    // 1. Find the point preceding b:
    int a = prevCounterClockwise (hullPoints, b);

    // 2. We start just behind the starting index:
    int current = prevCounterClockwise (hullPoints, c);

    // 3. Start with area computations, to use in lieu of distance comparisons.
    double area_abc = Math.abs (Geometry.twice_area (hullPoints[a], hullPoints[b], hullPoints[c]));
    double area_abcurrent = Math.abs (Geometry.twice_area (hullPoints[a], hullPoints[b], hullPoints[current]));

    // 3. While the current point (current) is closer than c:
    while (area_abc >= area_abcurrent) {

      // 3.1 Move current up:
      current = c;

      // 3.2 Move c up:
      c = nextCounterClockwise (hullPoints, c);

      // 3.3 Compare distances again:
      area_abc = Math.abs (Geometry.twice_area (hullPoints[a], hullPoints[b], hullPoints[c]));
      area_abcurrent = Math.abs (Geometry.twice_area (hullPoints[a], hullPoints[b], hullPoints[current]));
    }

    // 4. When distances start decreasing, return the currently largest one:
    return current;
  }

  public double findLargestDistance (Pointd[] points)
  {
    // 1. Compute the convex hull: 
    Hull hull = new Hull (points);

    // 2. Extract the hull points: 
    Pointd[] hullPoints = hull.getPoints();

    // 3. If it's exactly three points, we have a method just for that: 
    if (hullPoints.length == 3)
      return findMaxDistance3 (hullPoints, 0, 1, 2);
    
    // Otherwise, we start an antipodal scan. 
    boolean over = false;

    // 4. Start the scan at vertex 0, using the edge ending at 0: 
    int currentIndex = 0;
    int prevIndex = prevCounterClockwise (hullPoints, currentIndex);

    // 5. Find the antipodal vertex for edge (n-1,0): 
    int antiPodalIndex = findAntiPodalIndex (hullPoints, currentIndex, 1);

    // 6. Set the current largest distance: 
    double maxDist = findMaxDistance3 (hullPoints, currentIndex, prevIndex, antiPodalIndex);
    // We'll stop once we've gone around and come back to vertex 0. 
    double dist = 0;
    turnComplete = false;

    // 7. While the turn is not complete: 
    while (! over) {

      // 7.1 Find the next edge: 
      prevIndex = currentIndex;
      currentIndex = nextCounterClockwise (hullPoints, currentIndex);

      // 7.2 Get its antipodal vertex: 
      antiPodalIndex = findAntiPodalIndex (hullPoints, currentIndex, antiPodalIndex);

      // 7.3 Compute the distance: 
      dist = findMaxDistance3 (hullPoints, currentIndex, prevIndex, antiPodalIndex);

      // 7.4 Record maximum: 
      if (dist > maxDist)
        maxDist = dist;

      // 7.5 Check whether turn is complete: 
      if (turnComplete)
        over = true;
    } // end-while 

    // 8. Return largest distance found. 
    return maxDist;

  }

}
