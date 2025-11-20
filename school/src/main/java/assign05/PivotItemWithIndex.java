package assign05;

/**
 * Helper class to store an element with its original index for pivot selection.
 */
class PivotItemWithIndex<E> {

    /**
     * The element value
     */
    E item;

    /**
     * The index of this element in the original list
     */
    int index;

    /**
     * Constructs a new PivotItemWithIndex.
     *
     * @param item the element value
     * @param index the index of this element in the original list
     */
    PivotItemWithIndex(E item, int index) {
        this.item = item;
        this.index = index;
    }

    /**
     * Gets the element value.
     *
     * @return the element value
     */
    public E getItem() {
        return item;
    }

    /**
     * Gets the index of this element in the original list.
     *
     * @return the original index
     */
    public int getIndex() {
        return index;
    }
}
