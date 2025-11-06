package assign07;

import java.util.ArrayList;

/**
 * This class contains a method that uses a BST to sort a duplicate-free
 * collection of elements.
 *
 * @author CS 2420 course staff
 * @version March 6, 2025
 */
public class SortViaBST {

    /**
     * Sorts the given list of elements from smallest to largest.
     *
     * @param list - duplicate-free collection of elements to be sorted
     * @throws NoSuchElementException if the given list contains duplicates
     */
    public static <E extends Comparable<? super E>> void sort(ArrayList<E> list) {
        BinarySearchTree<E> bst = new BinarySearchTree<E>();
        bst.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            E element = bst.first();
            list.set(i, element);
            bst.remove(element);
        }
    }
}
