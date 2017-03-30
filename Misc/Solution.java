public class Solution {

    public static int removeDuplicates(int[] A) {
      int length=A.length; 
      if(length==0 || length==1) return length;
      int i=1; 
      for(int j=1; j<length; j++) 
      {
        if(A[j]!=A[j-1])
        {
          A[i]=A[j]; 
          i++; 
        }
      }
      //if(i<length) A[i]='\0';
      while (i < length) {
        A[i++] = 0;
      }
      return i; 
    }

    public static void main(String args[]) {

      int[] array = {0, 1, 1, 2, 3, 4, 4, 4, 5};

      int c = removeDuplicates(array);

      for (int k = 0; k<array.length; k++) 
        System.out.print(array[k] + ", ");

      System.out.println("count: " + c);
    }
}