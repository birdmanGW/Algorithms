
import java.util.*;
import java.io.*;

public class ex1 {

    public static void main (String[] argv) 
    {
        String[] words = getDictionary ("words");
        // Now words[i] has the i-th word in the dictionary.

        // Hint: you can call the hashCode() method in String to
        // get the hashvalue of any string.
        
        // INSERT YOUR CODE HERE
    }


    /////////////////////////////////////////////////////////////////
    // You don't need to read below this line.

    static boolean isValidWord (String str)
    {
        for (int i=0; i < str.length(); i++)
            if (! (Character.isLetter(str.charAt(i))) )
                return false;
        return true;
    }
    
    
    static String[] getDictionary (String fullPathName)
    {
        try {
            File f = new File (fullPathName);
            LineNumberReader lnr = new LineNumberReader (new FileReader (f));
            String line = lnr.readLine();
            int count = 0;
            while (line != null) {
                String str = line.trim().toLowerCase();
                if (isValidWord (str))
                    count++;
                line = lnr.readLine();
            }
            lnr.close();
            
            System.out.println ("Number of words: " + count);
            // OK, now make the space.
            String[] strArray = new String [count];
            lnr = new LineNumberReader (new FileReader (f));
            count = 0;
            line = lnr.readLine();
            while (line != null) {
                String str = line.trim().toLowerCase();
                if (isValidWord (str)) {
                    strArray [count] = line.trim().toLowerCase();
                    count++;
                }
                line = lnr.readLine();
            }
            lnr.close();
            return strArray;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
