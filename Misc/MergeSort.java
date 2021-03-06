import edu.gwu.util.*;
import edu.gwu.algtest.*;

public class MergeSort implements SortingAlgorithm {

    public java.lang.String getName() {
  		return "\n snbbrbrd's implementation of MergeSort";
    }

    public void setPropertyExtractor(int algID, PropertyExtractor prop){}

    public int[] createSortIndex(int[] data){return null;}

    public int[] createSortIndex(java.lang.Comparable[] data){return null;}
 	
	public void sortInPlace (java.lang.Comparable[] data) {

	    // Sort data[0:n-1] using merge sort.
	    int s = 1;   // segment size
	    int n = data.length;
	    Comparable[] temp = new Comparable[n];

	    while (s < n) 
	    {
	        mergePass(data, temp, s, n); // merge from data to temp
	        s += s;                      // double the segment size
	        mergePass(temp, data, s, n); // merge from temp to data
	        s += s;                      // again, double the segment size
	    } 
    }


	public static void mergePass (Comparable[] data, Comparable[] temp, int s, int n)
	{
	    // Merge adjacent segments of size s.
	    int i = 0;

	    while (i <= n - 2 * s)
	    {
	    	//Merge two adjacent segments of size s
	        mergeSort(data, i, i+s-1, i+2*s-1, temp);
	        i = i + 2*s;
	    }
	    // fewer than 2s elements remain
	    if (i + s < n)
	        mergeSort(data, i, i+s-1, n-1, temp);
	    else
	        for (int j = i; j <= n-1; j++)
	            temp[j] = data[j];   // copy last segment to temp
	} 

	public static void mergeSort (Comparable[] data, int left, int middle, int right, Comparable[] temp)
	{
    	// Merge data[left:middle] and data[middle+1:right] to temp[left:right]
	    int i = left,         // cursor for first segment
	        j = middle+1,     // cursor for second
	        k = left;         // cursor for result

	    // merge until i or j exits its segment
	    while ( (i <= middle) && (j <= right) )
	        if (data[i].compareTo(data[j]) <= 0)  
	        	temp[k++] = data[i++];
	        else 
	            temp[k++] = data[j++];

	    // take care of left overs 
	    while ( i <= middle )
	        temp[k++] = data[i++];
	    while ( j <= right )
	        temp[k++] = data[j++];

	}  


    public static void display(Comparable[] data) {

    	System.out.print("{");
    	for (int i = 0; i < data.length; i++)
			System.out.print(data[i] + ", ");

		System.out.println("}");
    }
	
	
	public void sortInPlace (int[] data) {

	    // Sort data[0:n-1] using merge sort.
	    int s = 1;   // segment size
	    int n = data.length;
	    int[] temp = new int[n];

	    while (s < n) 
	    {
	        mergePass(data, temp, s, n); // merge from data to temp
	        s += s;                      // double the segment size
	        mergePass(temp, data, s, n); // merge from temp to data
	        s += s;                      // again, double the segment size
	    } 
	}


	public static void mergePass (int data[], int temp[], int s, int n)
	{
	    // Merge adjacent segments of size s.
	    int i = 0;

	    while (i <= n - 2 * s)
	    {
	    	//Merge two adjacent segments of size s
	        mergeSort(data, i, i+s-1, i+2*s-1, temp);
	        i = i + 2*s;
	    }
	    // fewer than 2s elements remain
	    if (i + s < n)
	        mergeSort(data, i, i+s-1, n-1, temp);
	    else
	        for (int j = i; j <= n-1; j++)
	            temp[j] = data[j];   // copy last segment to temp
	} 

	public static void mergeSort (int[] data, int left, int middle, int right, int[] temp)
	{
	    // Merge data[left:middle] and data[middle+1:right] to temp[left:right]
	    int i = left,         // cursor for first segment
	        j = middle+1,     // cursor for second
	        k = left;         // cursor for result

	    // merge until i or j exits its segment
	    while ( (i <= middle) && (j <= right) )
	        if (data[i] <= data[j])  
	        	temp[k++] = data[i++];
	        else 
	            temp[k++] = data[j++];

	    // take care of left overs 
	    while ( i <= middle )
	        temp[k++] = data[i++];
	    while ( j <= right )
	        temp[k++] = data[j++];
	}  



    public static void display(int[] data) {

    	System.out.print("{");
    	for (int i = 0; i < data.length; i++)
			System.out.print(data[i] + ", ");

		System.out.println("}");
    }
    
	    
    public static void main(String[] args) {

    	MergeSort myMerge = new MergeSort();

    	String[] input = {"F", "A", "A",  "E", "J", "K", "I", "C", "G", "D"};
    	int[] input2 = {2, 9, 67, 45, 12, 98, 54, 109, 23, 1, 85, 345, 3, 0};
    	System.out.println("Before sort: ");
    	display(input);
    	display(input2);

    	System.out.println("After sort: ");
    	myMerge.sortInPlace(input);
    	myMerge.sortInPlace(input2);
    	display(input);
    	display(input2);
    }
    
}

