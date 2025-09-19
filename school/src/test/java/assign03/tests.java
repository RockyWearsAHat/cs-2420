package assign03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for ArrayCollection and SearchUtil classes.
 *
 * @author Alex Waldmann
 */
public class tests {

    private ArrayCollection<Integer> collection;
    private ArrayCollection<String> stringCollection;

    @BeforeEach
    void setUp() {
        collection = new ArrayCollection<>();
        stringCollection = new ArrayCollection<>();
    }

    // ArrayCollection Constructor Tests
    @Test
    void testConstructor() {
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
    }

    // Add method tests
    @Test
    void testAddSingleElement() {
        assertTrue(collection.add(5));
        assertEquals(1, collection.size());
        assertTrue(collection.contains(5));
    }

    @Test
    void testAddDuplicateElement() {
        assertTrue(collection.add(5));
        assertFalse(collection.add(5)); // Should not add duplicate
        assertEquals(1, collection.size());
    }

    @Test
    void testAddMultipleElements() {
        assertTrue(collection.add(1));
        assertTrue(collection.add(2));
        assertTrue(collection.add(3));
        assertEquals(3, collection.size());
    }

    @Test
    void testAddNullElement() {
        assertTrue(collection.add(null));
        assertTrue(collection.contains(null));
        assertFalse(collection.add(null)); // No duplicate nulls
        assertEquals(1, collection.size());
    }

    // Growth testing
    @Test
    void testGrowthWhenCapacityExceeded() {
        // Add more than initial capacity (10)
        for (int i = 0; i < 15; i++) {
            assertTrue(collection.add(i));
        }
        assertEquals(15, collection.size());
        for (int i = 0; i < 15; i++) {
            assertTrue(collection.contains(i));
        }
    }

    // AddAll method tests
    @Test
    void testAddAllEmptyCollection() {
        ArrayList<Integer> emptyList = new ArrayList<>();
        assertFalse(collection.addAll(emptyList));
        assertEquals(0, collection.size());
    }

    @Test
    void testAddAllNewElements() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertTrue(collection.addAll(list));
        assertEquals(3, collection.size());
        assertTrue(collection.containsAll(list));
    }

    @Test
    void testAddAllWithDuplicates() {
        collection.add(1);
        collection.add(2);
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2, 3, 4));
        assertTrue(collection.addAll(list)); // Should add 3 and 4, but not 2
        assertEquals(4, collection.size());
        assertTrue(collection.contains(3));
        assertTrue(collection.contains(4));
    }

    // Clear method tests
    @Test
    void testClear() {
        collection.add(1);
        collection.add(2);
        collection.clear();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
    }

    // Contains method tests
    @Test
    void testContains() {
        collection.add(1);
        collection.add(2);
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertFalse(collection.contains(3));
    }

    @Test
    void testContainsNull() {
        collection.add(null);
        assertTrue(collection.contains(null));
    }

    @Test
    void testContainsWithNullInCollection() {
        collection.add(1);
        collection.add(null);
        collection.add(2);
        assertTrue(collection.contains(null));
        assertTrue(collection.contains(1));
        assertFalse(collection.contains(3));
    }

    // ContainsAll method tests
    @Test
    void testContainsAllEmpty() {
        ArrayList<Integer> emptyList = new ArrayList<>();
        assertTrue(collection.containsAll(emptyList));
    }

    @Test
    void testContainsAllTrue() {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2));
        assertTrue(collection.containsAll(list));
    }

    @Test
    void testContainsAllFalse() {
        collection.add(1);
        collection.add(2);
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 3));
        assertFalse(collection.containsAll(list));
    }

    // IsEmpty method tests
    @Test
    void testIsEmptyTrue() {
        assertTrue(collection.isEmpty());
    }

    @Test
    void testIsEmptyFalse() {
        collection.add(1);
        assertFalse(collection.isEmpty());
    }

    // Remove method tests
    @Test
    void testRemoveExistingElement() {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        assertTrue(collection.remove(2));
        assertEquals(2, collection.size());
        assertFalse(collection.contains(2));
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(3));
    }

    @Test
    void testRemoveNonExistingElement() {
        collection.add(1);
        assertFalse(collection.remove(2));
        assertEquals(1, collection.size());
    }

    @Test
    void testRemoveNull() {
        collection.add(null);
        collection.add(1);
        assertTrue(collection.remove(null));
        assertEquals(1, collection.size());
        assertFalse(collection.contains(null));
    }

    // RemoveAll method tests
    @Test
    void testRemoveAllSomeElements() {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        ArrayList<Integer> toRemove = new ArrayList<>(Arrays.asList(1, 3, 4));
        assertTrue(collection.removeAll(toRemove));
        assertEquals(1, collection.size());
        assertTrue(collection.contains(2));
    }

    @Test
    void testRemoveAllNoElements() {
        collection.add(1);
        collection.add(2);
        ArrayList<Integer> toRemove = new ArrayList<>(Arrays.asList(3, 4));
        assertFalse(collection.removeAll(toRemove));
        assertEquals(2, collection.size());
    }

    // RetainAll method tests
    @Test
    void testRetainAllSomeElements() {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        ArrayList<Integer> toRetain = new ArrayList<>(Arrays.asList(1, 3, 4));
        assertTrue(collection.retainAll(toRetain));
        assertEquals(2, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(3));
        assertFalse(collection.contains(2));
    }

    @Test
    void testRetainAllNoChange() {
        collection.add(1);
        collection.add(2);
        ArrayList<Integer> toRetain = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertFalse(collection.retainAll(toRetain));
        assertEquals(2, collection.size());
    }

    // Size method tests
    @Test
    void testSize() {
        assertEquals(0, collection.size());
        collection.add(1);
        assertEquals(1, collection.size());
        collection.add(2);
        assertEquals(2, collection.size());
        collection.remove(1);
        assertEquals(1, collection.size());
    }

    // ToArray method tests
    @Test
    void testToArray() {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        Object[] array = collection.toArray();
        assertEquals(3, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
        assertEquals(3, array[2]);
    }

    @Test
    void testToArrayEmpty() {
        Object[] array = collection.toArray();
        assertEquals(0, array.length);
    }

    // ToSortedList method tests
    @Test
    void testToSortedListInteger() {
        collection.add(3);
        collection.add(1);
        collection.add(4);
        collection.add(2);

        ArrayList<Integer> sorted = collection.toSortedList(Integer::compareTo);
        assertEquals(Arrays.asList(1, 2, 3, 4), sorted);

        // Original collection should remain unchanged
        assertTrue(collection.contains(3));
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(4));
        assertTrue(collection.contains(2));
    }

    @Test
    void testToSortedListString() {
        stringCollection.add("charlie");
        stringCollection.add("alpha");
        stringCollection.add("bravo");

        ArrayList<String> sorted = stringCollection.toSortedList(String::compareTo);
        assertEquals(Arrays.asList("alpha", "bravo", "charlie"), sorted);
    }

    @Test
    void testToSortedListCustomComparator() {
        collection.add(1);
        collection.add(2);
        collection.add(3);

        // Reverse order comparator
        ArrayList<Integer> sorted = collection.toSortedList((a, b) -> b.compareTo(a));
        assertEquals(Arrays.asList(3, 2, 1), sorted);
    }

    // Iterator tests
    @Test
    void testIteratorHasNext() {
        collection.add(1);
        collection.add(2);
        Iterator<Integer> iter = collection.iterator();
        assertTrue(iter.hasNext());
        iter.next();
        assertTrue(iter.hasNext());
        iter.next();
        assertFalse(iter.hasNext());
    }

    @Test
    void testIteratorNext() {
        collection.add(1);
        collection.add(2);
        Iterator<Integer> iter = collection.iterator();
        assertEquals(Integer.valueOf(1), iter.next());
        assertEquals(Integer.valueOf(2), iter.next());
    }

    @Test
    void testIteratorNextThrowsException() {
        Iterator<Integer> iter = collection.iterator();
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    void testIteratorRemove() {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        Iterator<Integer> iter = collection.iterator();
        iter.next(); // 1
        iter.remove();
        assertEquals(2, collection.size());
        assertFalse(collection.contains(1));

        assertEquals(Integer.valueOf(2), iter.next());
        iter.remove();
        assertEquals(1, collection.size());
        assertFalse(collection.contains(2));
    }

    @Test
    void testIteratorRemoveWithoutNext() {
        collection.add(1);
        Iterator<Integer> iter = collection.iterator();
        assertThrows(IllegalStateException.class, iter::remove);
    }

    @Test
    void testIteratorRemoveTwice() {
        collection.add(1);
        Iterator<Integer> iter = collection.iterator();
        iter.next();
        iter.remove();
        assertThrows(IllegalStateException.class, iter::remove);
    }

    // SearchUtil tests
    @Test
    void testBinarySearchFound() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
        assertTrue(SearchUtil.binarySearch(list, 5, Integer::compareTo));
        assertTrue(SearchUtil.binarySearch(list, 1, Integer::compareTo));
        assertTrue(SearchUtil.binarySearch(list, 9, Integer::compareTo));
    }

    @Test
    void testBinarySearchNotFound() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
        assertFalse(SearchUtil.binarySearch(list, 4, Integer::compareTo));
        assertFalse(SearchUtil.binarySearch(list, 0, Integer::compareTo));
        assertFalse(SearchUtil.binarySearch(list, 10, Integer::compareTo));
    }

    @Test
    void testBinarySearchEmptyList() {
        ArrayList<Integer> list = new ArrayList<>();
        assertFalse(SearchUtil.binarySearch(list, 1, Integer::compareTo));
    }

    @Test
    void testBinarySearchSingleElement() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(5));
        assertTrue(SearchUtil.binarySearch(list, 5, Integer::compareTo));
        assertFalse(SearchUtil.binarySearch(list, 3, Integer::compareTo));
    }

    @Test
    void testBinarySearchStrings() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "date"));
        assertTrue(SearchUtil.binarySearch(list, "banana", String::compareTo));
        assertFalse(SearchUtil.binarySearch(list, "grape", String::compareTo));
    }

    @Test
    void testBinarySearchCustomComparator() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1)); // Reverse sorted
        Comparator<Integer> reverseComparator = (a, b) -> b.compareTo(a);
        assertTrue(SearchUtil.binarySearch(list, 5, reverseComparator));
        assertFalse(SearchUtil.binarySearch(list, 4, reverseComparator));
    }

    // Integration tests
    @Test
    void testArrayCollectionWithSearchUtil() {
        collection.add(3);
        collection.add(1);
        collection.add(4);
        collection.add(2);

        ArrayList<Integer> sorted = collection.toSortedList(Integer::compareTo);
        assertTrue(SearchUtil.binarySearch(sorted, 2, Integer::compareTo));
        assertFalse(SearchUtil.binarySearch(sorted, 5, Integer::compareTo));
    }

    @Test
    void testLargeDataSet() {
        // Test with larger dataset to ensure efficiency
        for (int i = 0; i < 1000; i += 2) { // Add even numbers
            collection.add(i);
        }

        assertEquals(500, collection.size());

        ArrayList<Integer> sorted = collection.toSortedList(Integer::compareTo);
        assertEquals(500, sorted.size());

        // Test binary search on sorted list
        assertTrue(SearchUtil.binarySearch(sorted, 500, Integer::compareTo));
        assertFalse(SearchUtil.binarySearch(sorted, 501, Integer::compareTo)); // Odd number not in collection
    }
}
