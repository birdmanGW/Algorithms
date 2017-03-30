import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;
public class Greedy{
	

	int[][] computeTours(int m, edu.gwu.geometry.Pointd[] points){

		/*
		Method computeTours() is the only method in the interface.
		 The number of salesmen, m, and the the locations of the points (cities) 
		 are passed in as parameters. You need to return a 2D array of ints, 
		 one row for each of the m salesmen. 
		 Each row will list in an array the points (cities) assigned in tour-order
		  to that particular salesman. For example, suppose m=5, and array A 
		  (of type int[][]) is returned. Then, A[3] = {0, 4, 5} implies that 
		  cities 0, 4 and 5 were assigned to salesman 3 
		  (the fourth salesman, counting from 0 onwards).
		*/
		  int[] marked= new int[points.length];
		  boolean test=false;
		  int[][] A = new int[m][];//rows = which salesman so row [0] is the first sales man
		  ArrayList<Integer> temp=new ArrayList<Integer>();
		  temp[] store= new temp[m];
		  int save=-1;
		for (int i =0; i<m;i++){
			store[i].add(i);//this just selects the first m random points to begin finding the shortest path
			marked[i]=1;
		}

		int minpath=9,999;//sets a high number to check against paths
		int save=-1;//used to store which city is closests

		while (test==false){//repeat this till all nodes are added
			for (int i=0; i<m;i++){
				for (int j=0; j<points.length;j++){
					if (marked[j]!=1){
						//compare the values
						if (store.length!=0){
								Pointd temp= points[store[i].get(store[i].length-1)];
								//gets the last value of the arraylist which corresponds to a city in the points[] aray;
								//compare temp to the j value to see what is the closests node
								int value=Math.sqrt((temp.getx()-points[j].getx())*(temp.getx()-points[j].getx())
									+(temp.gety()-points[j].gety())*(temp.gety()-points[j].gety());
									//distance formula
								if (value<=minpath){
									save=j;
									minpath=value;
								}

								
						}
						

					}
				}
				if (save!=-1){
					store[i].add(save);//adds the city to the end of the list;
					marked[save]=1;
				}
				save=-1;
				minpath=9,999;//resets the values
			}
		}
		
	
}

public java.lang.String getName(){
return "Jacobs' implementation of Naive algorithm"; 
}
public void setPropertyExtractor(int algID,edu.gwu.util.PropertyExtractor prop){


}
}