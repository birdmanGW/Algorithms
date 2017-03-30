Algorithm: computeC (i, j, probs)
Input: range limits i and j, access probabilities

     // Check whether sub-problem has been solved before. 
     // If so, return the optimal cost. This is an O(1) computation. 
1.   if (C[i][j] >= 0)
2.       return C[i][j]
3.   endif

     // The sum of access probabilities used in the recurrence relation. 
4.   sum = probs[i] + ... + probs[j];

     // Now search possible roots of the tree. 
5.   min = infinity
6.   for k=i to j
         // Optimal cost of the left subtree (for this value of k). 
6.1.     if (k-1 < i)
6.2.         Cleft = computeC (i, i+1)
6.3.     else
7.           Cleft = computeC (i, k-1)
         // Optimal cost of the right subtree. 
8.       Cright = computeC (k+1, j)
         // Record optimal solution. 
9.       if Cleft + Cright < min
10.          min = Cleft + Cright
11.      endif
12.  endfor

13.  return min

Output: the optimal cost of a binary tree for the sub-range keys[i], ..., keys[j].