package assign03;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Daniel Kopta and ?? A utility class for searching, containing just
 * one method (see below).
 *
 */
public class SearchUtil {

    /**
     * Performs a binary search on a sorted ArrayList to determine if an item
     * exists.
     *
     * @param <T>	The type of elements contained in the list
     * @param list	An ArrayList to search through. This ArrayList is assumed to
     * be sorted according to the supplied comparator. This method has undefined
     * behavior if the list is not sorted.
     * @param item	The item to search for
     * @param cmp	Comparator for comparing Ts or a super class of T
     * @return	true if the item exists in the list, false otherwise
     */
    public static <T> boolean binarySearch(ArrayList<T> list, T item, Comparator<? super T> cmp) {
        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2; // Prevent integer overflow
            int comparison = cmp.compare(item, list.get(middle));

            if (comparison == 0) {
                return true; // Found the item
            } else if (comparison < 0) {
                right = middle - 1; // Search left half
            } else {
                left = middle + 1; // Search right half
            }
        }

        return false; // Item not found
    }
}
