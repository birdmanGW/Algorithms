import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;
public class Naive{
	

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

		  int[][] A = new int[m][];//rows = which salesman so row [0] is the first sales man
		  Pointd[] B;
		  ArrayList<Integer> temp=new ArrayList<Integer>();
		  temp[] store= new temp[m];

		  int count =0;
		 	for (int j=0; j<points.length;j++){
		  		if (count==m){//resets when it would cause the numberto go over
		  			count==0;//resets the count so it can be divided evenly
		  		}
		  		store[count].add(j);//adds value to the arraylist
		  		
		  }
		 for (int i=0;i<m;i++){
		 	A[i] = new int[store[i].length];//seets size of columns	
		 	for (int j=0;j<store[i].length;j++){
		 		A[i][j]=store[i].get(j);//sets each value
		 	}
		 }
		  	
		  return A;
	
}
public java.lang.String getName(){
return "Jacobs' implementation of Naive algorithm"; 
}
public void setPropertyExtractor(int algID,edu.gwu.util.PropertyExtractor prop){

}

]