
import java.util.*;

public class MapSetExample {
    public static void main (String[] argv)

    {
        setExample ();
        mapExample ();
    }

    static void setExample ()
    {
        // Example: use strings or numbers.

        // INSERT YOUR CODE HERE.

        Set setA = new HashSet();
        String element1 = "element 1";
        String element2 = "element 2";
        String element3 = "element 3";

        setA.add(element1);
        setA.add(element2);
        setA.add(element3);

        System.out.println(setA.contains(element1));
        System.out.println(setA.contains(element2));
        System.out.println(setA.contains(element3));


    }

    static void mapExample ()
    {
        // Example: use a map from numbers to strings.

        // INSERT YOUR CODE HERE.

        Map mapA = new hashMap();
        mapA.put("BMW", 5);
        mapA.put("Mercedes", 3);
        mapA.put("Audi", 9);

        for(String key: vehicles.keySet())
            System.out.println(key + " - " + vehicles.get(key));
        
        System.out.println();



    }

}
