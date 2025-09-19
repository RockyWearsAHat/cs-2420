package assign03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Daniel Kopta and ?? Implements the Collection interface using an
 * array as storage. The array must grow as needed. An ArrayCollection can not
 * contain duplicates. All methods should be implemented as defined by the Java
 * API, unless otherwise specified.
 *
 * It is your job to fill in the missing implementations and to comment this
 * class. Every method should have comments (Javadoc-style prefered). The
 * comments should at least indicate what the method does, what the arguments
 * mean, and what the returned value is. It should specify any special cases
 * that the method handles. Any code that is not self-explanatory should be
 * commented.
 *
 * @param <T> - generic type placeholder
 */
public class ArrayCollection<T> implements Collection<T> {

    private T data[]; // Storage for the items in the collection
    private int size; // Keep track of how many items this collection holds

    // There is no clean way to convert between T and Object, so we suppress the warning.
    @SuppressWarnings("unchecked")
    public ArrayCollection() {
        size = 0;
        // We can't instantiate an array of unknown type T, so we must create an Object array, and typecast
        data = (T[]) new Object[10]; // Start with an initial capacity of 10
    }

    /**
     * This is a helper method specific to ArrayCollection that doubles the size
     * of the data storage array, retaining its current contents.
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        // Create a new array with double the capacity
        T newData[] = (T[]) new Object[data.length * 2];
        // Copy existing elements to the new array
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
        // Note: size remains unchanged as it represents the number of elements, not capacity
    }

    /**
     * Adds the specified element to this collection if it is not already
     * present.
     *
     * @param arg0 element to be added to this collection
     * @return false if the item being added is already in the collection,
     * otherwise adds the item and returns true
     */
    @Override
    public boolean add(T arg0) {
        // Check if the item is already in the collection (no duplicates allowed)
        if (contains(arg0)) {
            return false;
        }

        // Grow the array if needed
        if (size == data.length) {
            grow();
        }

        // Add the item to the first empty spot
        data[size] = arg0;
        size++;
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this collection,
     * but only if they are not already present.
     *
     * @param arg0 collection containing elements to be added to this collection
     * @return true if any items were added, false otherwise
     */
    @Override
    public boolean addAll(Collection<? extends T> arg0) {
        boolean modified = false;
        // Use iterator to go through all items in the input collection
        for (T item : arg0) {
            // Only add items that don't already exist
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes all elements from this collection. The result of this operation
     * is that the size of the collection is 0.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        // Reset the array to initial state with capacity of 10
        data = (T[]) new Object[10];
        size = 0;
    }

    /**
     * Returns true if this collection contains the specified element.
     *
     * @param arg0 element whose presence in this collection is to be tested
     * @return true if this collection contains the specified element
     */
    @Override
    public boolean contains(Object arg0) {
        for (int i = 0; i < size; i++) {
            // Handle null elements properly
            if (arg0 == null && data[i] == null) {
                return true;
            }
            if (arg0 != null && data[i] != null && data[i].equals(arg0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this collection contains all of the elements in the
     * specified collection.
     *
     * @param arg0 collection to be checked for containment in this collection
     * @return true if this collection contains all elements in the specified
     * collection
     */
    @Override
    @SuppressWarnings("all")
    public boolean containsAll(Collection<?> arg0) {
        for (Object item : arg0) {
            //Icky try catch block
            try {
                if (!contains((T) item)) {
                    return false;
                }
            } catch (ClassCastException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if this collection contains no elements.
     *
     * @return true if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an iterator over the elements in this collection.
     *
     * @return an Iterator over the elements in this collection
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayCollectionIterator();
    }

    /**
     * Removes a single instance of the specified element from this collection,
     * if it is present.
     *
     * @param arg0 element to be removed from this collection, if present
     * @return true if an element was removed as a result of this call
     */
    @Override
    public boolean remove(Object arg0) {
        for (int i = 0; i < size; i++) {
            // Handle null elements properly
            boolean match = (arg0 == null && data[i] == null)
                    || (arg0 != null && data[i] != null && data[i].equals(arg0));

            if (match) {
                // Shift everything down to fill the gap
                for (int j = i; j < size - 1; j++) {
                    data[j] = data[j + 1];
                }
                data[size - 1] = null; // Clear the last element
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all of this collection's elements that are also contained in the
     * specified collection.
     *
     * @param arg0 collection containing elements to be removed from this
     * collection
     * @return true if any items were removed, false otherwise
     */
    @Override
    @SuppressWarnings("all")
    public boolean removeAll(Collection<?> arg0) {
        boolean modified = false;
        for (Object item : arg0) {
            // Keep removing while the item exists (in case of multiple occurrences)
            try {
                while (remove((T) item)) {
                    modified = true;
                }
            } catch (ClassCastException e) {
                // If the item is not of type T, we can't remove it
                // so return if it was modified to this point (I think java
                // is type limited to one type per collection/array so I
                // think this will always return false if the exception is hit,
                // but just in case, return modified)
                return modified;
            }
        }
        return modified;
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection. In other words, removes from this collection all of
     * its elements that are not contained in the specified collection.
     *
     * @param arg0 collection containing elements to be retained in this
     * collection
     * @return true if any items were removed, false otherwise
     */
    @Override
    public boolean retainAll(Collection<?> arg0) {
        boolean modified = false;
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (!arg0.contains(item)) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns an array containing all of the elements in this collection. The
     * returned array is a new copy with exact size of the collection.
     *
     * @return an array containing all of the elements in this collection
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }

    /* 
	 * Don't implement this method (unless you want to).
	 * It must be here to complete the Collection interface.
	 * We will not test this method.
     */
    @Override
    @SuppressWarnings({"unchecked", "hiding"})
    public <T> T[] toArray(T[] arg0) {
        if (arg0.length < size) {
            return (T[]) Arrays.copyOf(data, size, arg0.getClass());
        }
        System.arraycopy(data, 0, arg0, 0, size);
        if (arg0.length > size) {
            arg0[size] = null;
        }
        return arg0;
    }

    /*
     
     */
    /**
     * Sorting method specific to ArrayCollection - not part of the Collection
     * interface. Uses selection sort to sort the elements. Must not modify this
     * ArrayCollection.
     *
     * @param cmp - the comparator that defines item ordering
     * @return - the sorted list
     */
    public ArrayList<T> toSortedList(Comparator<? super T> cmp) {
        ArrayList<T> sortedList = new ArrayList<>();
        // Copy all elements to the new list
        for (int i = 0; i < size; i++) {
            sortedList.add(data[i]);
        }

        // Implement selection sort
        for (int i = 0; i < sortedList.size() - 1; i++) {
            int minIndex = i;
            // Find the minimum element in the remaining unsorted portion
            for (int j = i + 1; j < sortedList.size(); j++) {
                if (cmp.compare(sortedList.get(j), sortedList.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            // Swap the found minimum element with the first element
            if (minIndex != i) {
                T temp = sortedList.get(i);
                sortedList.set(i, sortedList.get(minIndex));
                sortedList.set(minIndex, temp);
            }
        }

        return sortedList;
    }

    /**
     * ArrayCollectionIterator implements the Iterator interface for
     * ArrayCollection. This iterator traverses the collection in sequential
     * order (index 0 to size-1).
     *
     * @author Alex Waldmann
     */
    private class ArrayCollectionIterator implements Iterator<T> {

        private int currentIndex; // The index of the last element returned by next()
        private boolean removable; // Whether remove() can be called
        private int expectedSize; // Track expected size for concurrent modification detection

        /**
         * Constructs a new iterator for this ArrayCollection.
         */
        public ArrayCollectionIterator() {
            expectedSize = size;
            currentIndex = -1; // Start before the first element
            removable = false; // Cannot remove until next() is called
        }

        /**
         * Returns true if there are more elements to iterate through.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return currentIndex + 1 < expectedSize;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if there are no more elements
         */
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the collection");
            }
            currentIndex++;
            removable = true; // Now remove() can be called
            return data[currentIndex];
        }

        /**
         * Removes the last element returned by next() from the collection. Can
         * only be called once per call to next().
         *
         * @throws IllegalStateException if next() has not been called or if
         * remove() has already been called after the last call to next()
         */
        @Override
        public void remove() {
            if (!removable) {
                throw new IllegalStateException("remove() can only be called once per call to next()");
            }

            // Remove the element at currentIndex from the ArrayCollection
            ArrayCollection.this.remove(data[currentIndex]);

            // Update iterator state
            expectedSize--;
            currentIndex--; // Adjust index since elements shifted left
            removable = false; // Cannot call remove() again until next() is called
        }
    }

}
