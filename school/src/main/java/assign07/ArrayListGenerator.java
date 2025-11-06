package assign07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * This class contains methods for generating ArrayLists of various sizes and
 * orderings.
 *
 * @author CS 2420 course staff
 * @version 2025-6-25
 */
public class ArrayListGenerator {

    private static final Random rng = new Random();

    /**
     * Generates an ArrayList of problemSize random integers in nearly ascending
     * order, with each element unique.
     *
     * @implNote calls generateAscendingArrayListWithoutDuplicates and then
     * swaps a small number of random pairs of nearby elements
     * @param problemSize - size of the list
     */
    public static ArrayList<Integer> generateNearlyAscendingArrayListWithoutDuplicates(int problemSize) {
        ArrayList<Integer> list = generateAscendingArrayListWithoutDuplicates(problemSize);
        slightlyShuffleArrayList(list);
        return list;
    }

    /**
     * Generates an ArrayList of problemSize random integers in a permuted
     * order, with each element unique.
     *
     * @implNote calls generateAscendingArrayListWithoutDuplicates and then
     * shuffles the contents of the list
     * @param problemSize - size of the list
     */
    public static ArrayList<Integer> generatePermutedArrayListWithoutDuplicates(int problemSize) {
        ArrayList<Integer> list = generateAscendingArrayListWithoutDuplicates(problemSize);
        Collections.shuffle(list);
        return list;
    }

    /**
     * Generates an ArrayList of problemSize random integers in ascending order,
     * with each element unique.
     *
     * @param problemSize - size of the list
     */
    private static ArrayList<Integer> generateAscendingArrayListWithoutDuplicates(int problemSize) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int currentElement = rng.nextInt(20);
        for (int i = 0; i < problemSize; i++) {
            list.add(currentElement);
            currentElement += rng.nextInt(1, 10);
        }
        return list;
    }

    /**
     * Slightly shuffles the contents of the given ArrayList, such that it is in
     * nearly ascending order, by swapping a small number of random pairs of
     * nearby elements. The number of swaps is small, assuming the array
     * contains a large number elements.
     *
     * @param array to be shuffled slightly
     */
    private static void slightlyShuffleArrayList(ArrayList<Integer> array) {
        // Choose a random number of pairs to swap, between 5 and 19
        int swapCount = 5 + rng.nextInt(15);
        for (int i = 0; i < swapCount; i++) {
            // Choose a random index, excluding the final 11 indices
            int idx1 = rng.nextInt(array.size() - 11);
            // Choose an index between 1 and 10 to the right of idx1
            int idx2 = idx1 + 1 + rng.nextInt(10);
            // Swap the entries at those two indices
            swapArrayListElements(array, idx1, idx2);
        }
    }

    /**
     * Swaps two elements in the given ArrayList.
     *
     * @param array with elements to be swapped
     * @param firstIndex to swap
     * @param secondIndex to swap
     * @throws IndexOutOfBoundsException if either index is out of bounds
     */
    private static void swapArrayListElements(ArrayList<Integer> array, int firstIndex, int secondIndex) {
        if (firstIndex < 0 || firstIndex >= array.size() || secondIndex < 0 || secondIndex >= array.size()) {
            throw new IndexOutOfBoundsException();
        }
        int temp = array.get(firstIndex);
        array.set(firstIndex, array.get(secondIndex));
        array.set(secondIndex, temp);
    }
}
