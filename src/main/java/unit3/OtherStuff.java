package unit3;

import java.util.*;

public class OtherStuff {
    public static void main(String[] args) {

        // Use the Random class to generate a random numbers
        Random r = new Random();
        int n = r.nextInt(0, 100);  // There are variety of next*() methods that can be used to generate random values

        // Use the List interface to create a list of integers
        // Remember that List is an interface, so we can't instantiate it directly
        // We need to use a class that implements the List interface, such as ArrayList
        List<Integer> L = new ArrayList<Integer>();
        L.add(1);

        // Use the for-each loop to iterate over the list
        for ( int i : L ) {
            System.out.println(i);
        }

        // Use the Map interface to create a map (dictionary) of keys associated with values
        // Remember that Map is an interface, so we can't instantiate it directly
        // We need to use a class that implements the Map interface, such as HashMap
        // Note that we must specify the both type of the key AND the type of the value on maps
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("one", 1);
        m.put("two", 2);
        m.put("three", 3);

        var m2 = new HashMap<Integer, Integer>();
        m2.put(1, 23);
        m2.put(34834, 1232);
        m2.get(34834);

        // Using for-each loop to iterate over the map's keys
        for ( String key : m.keySet() ) {
            System.out.println(key + ": " + m.get(key));
        }

        // Using for-each loop to iterate over the map's entries
        for ( var entry : m.entrySet() ) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Using for-each loop to iterate over the map's values
        for ( int i : m.values() ) {
            System.out.println(i);
        }

    }
}
