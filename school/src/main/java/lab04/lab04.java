package lab04;

import java.util.Arrays;

public class lab04 {

    public static <T extends Comparable<T>> T median(T[] arr) {
        Arrays.sort(arr);
        if (arr.length % 2 == 1) {
            return arr[arr.length / 2];
        } else {
            T first = arr[(arr.length / 2) - 1];
            T second = arr[arr.length / 2];
            return (first.compareTo(second) < 0) ? first : second; // Return the smaller of the two middle elements
        }
    }

    public static void main(String[] args) {
        Integer[] intArray = {3, 1, 4, 1, 5, 9};
        String[] strArray = {"banana", "apple", "cherry", "date"};

        System.out.println("Median of intArray: " + median(intArray)); // Should print 3
        System.out.println("Median of strArray: " + median(strArray)); // Should print "banana"
    }
}
