import java.util.*;

public class SnbbrbrdAlgorithm implements CameraPlacementAlgorithm {

    // Algorithm should solve and return list of Camera instances
    // as camera locations and orientations.

	//Optimal Placement dir
	public static ArrayList<int[]> optimalDir;

	//list of cameras
	public static ArrayList<Camera> cameraList;

	//Incrementation of angle and position (WIll change for large problems)
	public  int angleIncre = 10, posIncre = 1;

	public ArrayList<Camera> solve (CameraPlacementProblem problem)
    {

    	int maxX = problem.getMaxX();
    	int maxY = problem.getMaxY();
    	int i, j, pos, angle, angleBest, maxTiles;

		CameraPlacementResult finalResult = null;

		//Create camera list
    	cameraList = new ArrayList<Camera>();

    	//Create optimalDir
    	optimalDir = new ArrayList<int[]>();


		//Evaluate empty camera list... 100% uncovered tiles
		finalResult = CameraPlacement.evaluatePlacement(problem, cameraList);

		int needToCover =  finalResult.numNotCovered;
		//System.out.println ("Start: needToCover: " + needToCover);

		//If large test case, lets cheat a bit and shorten all the for loops
		if (needToCover > 10000)
		{
			//System.out.println("FLOODGATES [OPEN]");
			angleIncre = 30;
			posIncre = 2;
		}

    	//Compute initial optimalDir
		computeOptimalDir(problem);

		//While all tiles still uncovered
		while (finalResult.numNotCovered != 0)
		{
			//System.out.println("--> # tiles covered: " + finalResult.numCovered);

			//Add most optimal, non overlapping camera from just computed optimalDir
			performOptimalPlacement(problem);

			//Re compute total coverage
			finalResult = CameraPlacement.evaluatePlacement(problem, cameraList);

    		//printOptimalDir();

			//System.out.println("cameraList.size(): " + cameraList.size() + " # tiles covered: " + finalResult.numCovered);

			//Re compute optimalDir
			computeOptimalDir(problem);
		}



		return cameraList;

    }

    /*
     * Using either an empty cameraList (or one with x number of already optimal cams)
     * Generate the optimalCameraDir
     * coverageMatrix unused... never returned
     */
    public void computeOptimalDir(CameraPlacementProblem problem)
    {
    	int i, maxX = problem.getMaxX();
    	int j, maxY = problem.getMaxY();
    	int pos, maxTiles, angle, angleBest, goodCov;

    	ArrayList<Camera> cameraTestList = new ArrayList<Camera>();

    	//Clear previous list
    	optimalDir.clear();

    	CameraPlacementResult result = null;
    	CameraPlacementResult testResult = null;

    	//Result of all previous cameras together in cameraList
		result = CameraPlacement.evaluatePlacement(problem, cameraList);

    	// iteratively walk along bottom wall (Must do all ext/int walls respectively)
		for (pos = 1; pos < maxX; pos += posIncre)
		{
			//Quick check if current location is valid (saves time)
			if (!isValid(pos, 1))
				break;

			//int[][] coverageMatrix = preCompute(problem);
			maxTiles = 0;
			angleBest = 0;

			// Now iteratively through each possible angle 
			//Increments of: 10 degrees
			for (angle = 0; angle < 360; angle += angleIncre)
			{
				//Create new testCamera
				Camera testCamera = new Camera(new Pointd(pos, 1), angle);
				
				//Initialize goodCov
				goodCov = 0;
				
				cameraTestList.add(testCamera);

				//Result of newly created camera, alone
				testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

				//Compute total good Coverage by camera (# tiles covered that were prev uncovered)
				goodCov = computeGoodCoverage(result, testResult, problem);


				// If current cam angles covers ANY tiles add to dummy matrix
				if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
				{
					//update maxTiles (minus any overlap present), and angleBest
					maxTiles = goodCov;
					angleBest = angle;
				}

				//And clear single camera from testList
				cameraTestList.clear();
			}

			if (maxTiles > 0)
			{
				//Concatentate bestInfo into double array
				int[] info = new int[4];
				info[0] = angleBest;
				info[1] = maxTiles;
				info[2] = pos;
				info[3] = 1;

				//Add tp optimalDir
				optimalDir.add(info);
			}
			//System.out.println("fin. with pos: " + pos);
		}

		// iteratively walk along top wall (Must do all ext/int walls respectively)
		for (pos = 1; pos < maxX; pos += posIncre)
		{
			if (!isValid(pos, maxY - 1))
				break;

			//int[][] coverageMatrix = preCompute(problem);
			maxTiles = 0;
			angleBest = 0;

			// Now iteratively through each possible angle 
			//Increments of: 10 degrees
			for (angle = 0; angle < 360; angle += angleIncre)
			{
				Camera testCamera = new Camera(new Pointd(pos, maxY - 1), angle);
				
				//Initialize goodCov
				goodCov = 0;

				cameraTestList.add(testCamera);

				testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

				goodCov = computeGoodCoverage(result, testResult, problem);

				// If current cam angles covers ANY tiles add to dummy matrix
				if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
				{

					//update maxTiles (minus any overlap present), and angleBest
					maxTiles = goodCov;
					angleBest = angle;
				}

				//And clear single camera from testList
				cameraTestList.clear();
			}

			if (maxTiles > 0)
			{
				int[] info = new int[4];
				info[0] = angleBest;
				info[1] = maxTiles;
				info[2] = pos;
				info[3] = maxY - 1;

				//Add tp optimalDir
				optimalDir.add(info);
			}


			//System.out.println("fin. with pos: " + pos);
		}
		
		// iteratively walk along left wall (Must do all ext/int walls respectively)
		for (pos = 1; pos < maxY; pos += posIncre)
		{

			if (!isValid(1, pos))
				break;

			//int[][] coverageMatrix = preCompute(problem);
			maxTiles = 0;
			angleBest = 0;
			// Now iteratively through each possible angle 
			//Increments of: 10 degrees
			for (angle = 0; angle < 360; angle += angleIncre)
			{
				//Create new testCamera
				Camera testCamera = new Camera(new Pointd(1, pos), angle);
				
				//Initialize goodCov
				goodCov = 0;

				cameraTestList.add(testCamera);

				testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

				goodCov = computeGoodCoverage(result, testResult, problem);

				// If current cam angles covers ANY tiles add to dummy matrix
				if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
				{

					//update maxTiles (minus any overlap present), and angleBest
					maxTiles = goodCov;
					angleBest = angle;	

				}

				//And clear single camera from testList
				cameraTestList.clear();
			}

			//coverageDir.add(coverageMatrix);

			if (maxTiles > 0)
			{
				int[] info = new int[4];
				info[0] = angleBest;
				info[1] = maxTiles;
				info[2] = 1;
				info[3] = pos;

				//Add tp optimalDir
				optimalDir.add(info);
			}

			//System.out.println("fin. with pos: " + pos);
		}

		// iteratively walk along right wall (Must do all ext/int walls respectively)
		for (pos = 1; pos < maxY; pos += posIncre)
		{

			if (!isValid(maxX - 1, pos))
				break;

			//int[][] coverageMatrix = preCompute(problem);
			maxTiles = 0;
			angleBest = 0;
			// Now iteratively through each possible angle 
			//Increments of: 10 degrees
			for (angle = 0; angle < 360; angle += angleIncre)
			{
				//Create new testCamera
				Camera testCamera = new Camera(new Pointd(maxX - 1, pos), angle);
				
				//Initialize goodCov
				goodCov = 0;

				cameraTestList.add(testCamera);

				testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

				goodCov = computeGoodCoverage(result, testResult, problem);

				// If current cam angles covers ANY tiles add to dummy matrix
				if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
				{
					//update maxTiles (minus any overlap present), and angleBest
					maxTiles = goodCov;
					angleBest = angle;
				}

				//And clear single camera from testList
				cameraTestList.clear();
			}

			//coverageDir.add(coverageMatrix);

			if (maxTiles > 0)
			{
				int[] info = new int[4];
				info[0] = angleBest;
				info[1] = maxTiles;
				info[2] = maxX - 1;
				info[3] = pos;

				//Add tp optimalDir
				optimalDir.add(info);
			}

			//System.out.println("fin. with pos: " + pos);
		}

		//================================================
		// Now need to do interior walls:/
		//================================================
		
		ArrayList<Wall> interiorWalls = problem.getInteriorWalls();
		Iterator intWallItr = interiorWalls.iterator();

		while (intWallItr.hasNext())
		{

			Wall wall = (Wall) intWallItr.next();

			//If vertical int wall
			if (wall.start.x == wall.end.x) 
			{
				//For left side of wall
				for (pos = (int) wall.start.y; pos <= wall.end.y; pos += posIncre)
				{

					//int[][] coverageMatrix = preCompute(problem);
					maxTiles = 0;
					angleBest = 0;
					// Now iteratively through each possible angle 
					//Increments of: 10 degrees
					for (angle = 0; angle < 360; angle += angleIncre)
					{
						//Create new testCamera
						Camera testCamera = new Camera(new Pointd(wall.start.x, pos), angle);
						
						//Initialize goodCov
						goodCov = 0;

						cameraTestList.add(testCamera);

						testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

						goodCov = computeGoodCoverage(result, testResult, problem);

						// If current cam angles covers ANY tiles add to dummy matrix
						if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
						{
							//update maxTiles (minus any overlap present), and angleBest
							maxTiles = goodCov;
							angleBest = angle;
						}

						//And clear single camera from testList
						cameraTestList.clear();
					}

					//coverageDir.add(coverageMatrix);

					if (maxTiles > 0)
					{
						int[] info = new int[4];
						info[0] = angleBest;
						info[1] = maxTiles;
						info[2] = (int) wall.start.x;
						info[3] = pos;

						optimalDir.add(info);
					}


					//Add tp optimalDir
					//System.out.println("fin. with pos: " + pos);
				}

				//Now for right side of wall
				for (pos = (int) wall.start.y; pos <= wall.end.y; pos += posIncre)
				{

					//int[][] coverageMatrix = preCompute(problem);
					maxTiles = 0;
					angleBest = 0;
					// Now iteratively through each possible angle 
					//Increments of: 10 degrees
					for (angle = 0; angle < 360; angle += angleIncre)
					{
						//Create new testCamera
						Camera testCamera = new Camera(new Pointd(wall.start.x + 1, pos), angle);
						
						//Initialize goodCov
						goodCov = 0;

						cameraTestList.add(testCamera);

						testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

						goodCov = computeGoodCoverage(result, testResult, problem);

						// If current cam angles covers ANY tiles add to dummy matrix
						if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
						{
							//update maxTiles (minus any overlap present), and angleBest
							maxTiles = goodCov;
							angleBest = angle;

						}

						//And clear single camera from testList
						cameraTestList.clear();
					}

					//coverageDir.add(coverageMatrix);

					if (maxTiles > 0)
					{
						int[] info = new int[4];
						info[0] = angleBest;
						info[1] = maxTiles;
						info[2] = (int) wall.start.x + 1;
						info[3] = pos;

						//Add tp optimalDir
						optimalDir.add(info);
					}


			
					//System.out.println("fin. with pos: " + pos);
				}
			}
			//Else horizontal wall
			else
			{
				//For bottom side of wall
				for (pos = (int) wall.start.x; pos <= wall.end.x; pos += posIncre)
				{

					//int[][] coverageMatrix = preCompute(problem);
					maxTiles = 0;
					angleBest = 0;
					// Now iteratively through each possible angle 
					//Increments of: 10 degrees
					for (angle = 0; angle < 360; angle += angleIncre)
					{
						//Create new testCamera
						Camera testCamera = new Camera(new Pointd(pos, wall.start.y), angle);
						
						//Initialize goodCov
						goodCov = 0;

						cameraTestList.add(testCamera);

						testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

						goodCov = computeGoodCoverage(result, testResult, problem);

						// If current cam angles covers ANY tiles add to dummy matrix
						if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
						{
							//update maxTiles (minus any overlap present), and angleBest
							maxTiles = goodCov;
							angleBest = angle;

						}

						//And clear single camera from testList
						cameraTestList.clear();
					}

					//coverageDir.add(coverageMatrix);

					if (maxTiles > 0)
					{
						int[] info = new int[4];
						info[0] = angleBest;
						info[1] = maxTiles;
						info[2] = pos;
						info[3] = (int) wall.start.y;

						//Add tp optimalDir
						optimalDir.add(info);
					}
					//System.out.println("fin. with pos: " + pos);
				}

				//Now for top side of wall
				for (pos = (int) wall.start.x; pos <= wall.end.x; pos += posIncre)
				{

					//int[][] coverageMatrix = preCompute(problem);
					maxTiles = 0;
					angleBest = 0;
					// Now iteratively through each possible angle 
					//Increments of: 10 degrees
					for (angle = 0; angle < 360; angle += angleIncre)
					{
						//Create new testCamera
						Camera testCamera = new Camera(new Pointd(pos, wall.start.y + 1), angle);
						
						//Initialize goodCov
						goodCov = 0;

						cameraTestList.add(testCamera);

						testResult = CameraPlacement.evaluatePlacement(problem, cameraTestList);

						goodCov = computeGoodCoverage(result, testResult, problem);

						// If current cam angles covers ANY tiles add to dummy matrix
						if (goodCov > 0 && testResult.numIllegalPlacements == 0 && goodCov > maxTiles) 
						{

							//update maxTiles (minus any overlap present), and angleBest
							maxTiles = goodCov;
							angleBest = angle;

						}

						//And clear single camera from testList
						cameraTestList.clear();
					}

					//coverageDir.add(coverageMatrix);

				if (maxTiles > 0)
				{
					int[] info = new int[4];
					info[0] = angleBest;
					info[1] = maxTiles;
					info[2] = pos;
					info[3] = (int) wall.start.y + 1;

					//Add tp optimalDir
					optimalDir.add(info);
				}

					//System.out.println("fin. with pos: " + pos);
				}
			}

		}
		

		//for (j = 0; j < optimalDir.size(); j++)
		//	System.out.println("[ " + optimalDir.get(j)[0] + " " + optimalDir.get(j)[1] + " " + optimalDir.get(j)[2] + " " + optimalDir.get(j)[3] + " ]");
    }


    /*
     * Compute the percentage overlap
     * between two different CameraPlacementResult's
     */
    public int computeGoodCoverage(CameraPlacementResult result, CameraPlacementResult testResult, CameraPlacementProblem problem)
    {
    	int i, j, goodCoverage = 0;

    	int[][] totalCoverage = result.cover;
    	int[][] testCoverage = testResult.cover;

    	for (i = 0; i < problem.getMaxY(); i++)
    	{
    		for (j = 0; j < problem.getMaxX(); j++)
    		{

    			//If totalCoverage[j][i] uncovered but testCoverage covered, + 1
    			if ( totalCoverage[j][i] == 0 && testCoverage[j][i] == 1)
    				goodCoverage++;
    		}
    	}

    	return goodCoverage;

    }

    /*
     * Will take an ArrayList of optimal CameraPlacements, then
     * perform a greedy based placement of the given locations
     */
    public void performOptimalPlacement(CameraPlacementProblem problem)
    {
    	int i, j, index = 0, maxTiles = 0;
    	//ArrayList<Camera> cameraList = new ArrayList<Camera>();

    	//Iterate through all optimal amera placements, and select
    	//the best one (Highest covered tiles)
    	for (i = 0; i < optimalDir.size(); i++)
    	{
    		if (optimalDir.get(i)[1] > maxTiles && isValid(optimalDir.get(i)[2], optimalDir.get(i)[3]))
    		{
    			maxTiles = optimalDir.get(i)[1];
    			index = i;
    		}
    	}

    	Camera camera = new Camera(new Pointd(optimalDir.get(index)[2], optimalDir.get(index)[3]), optimalDir.get(index)[0]);

    	//System.out.println("Removing: [ " + optimalDir.get(index)[0] + " " + optimalDir.get(index)[1] + " " + optimalDir.get(index)[2] + " " + optimalDir.get(index)[3] + " ]");

    	//Now remove camera from optimalDir (because currently in cameraList)
    	//optimalDir.remove(index);

    	cameraList.add(camera);
    }

    /*
     * Check if given coordinate has a camera or not
     */
    public boolean isValid(int x, int y)
    {
		Iterator cameraIt = cameraList.iterator();	

		while (cameraIt.hasNext())
		{
			Camera camera = (Camera) cameraIt.next();

			if (camera.location.x == x &&
					camera.location.y == y)
						return false;
		}

		return true;
    }

    public void printOptimalDir()
    {
    	for (int i = 0; i < optimalDir.size(); i++)
    		System.out.println("[ " + optimalDir.get(i)[0] + " " + optimalDir.get(i)[1] + " " + optimalDir.get(i)[2] + " " + optimalDir.get(i)[3] + " ]");
    }

}