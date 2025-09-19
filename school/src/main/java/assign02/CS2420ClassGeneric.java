package assign02;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This Java class represents an unordered collection of University of Utah
 * students enrolled in CS 2420.
 *
 * NOTE: The word "Class" in the name of this Java class means a collection of
 * students and should not be confused with the Java term class, which is a
 * blueprint for making objects.
 *
 * @author Erin Parker and ??
 * @version January 20, 2022
 */
public class CS2420ClassGeneric<Type> {

    private final ArrayList<CS2420StudentGeneric<Type>> studentList;

    /**
     * Creates an empty CS 2420 class.
     */
    public CS2420ClassGeneric() {
        this.studentList = new ArrayList<>();
    }

    /**
     * Adds the given student to the collection of students in CS 2420, avoiding
     * duplicates.
     *
     * @param student - student to be added to this CS 2420 class
     * @return true if the student was added, false if the student was not added
     * because they already exist in the collection
     */
    public boolean addStudent(CS2420StudentGeneric<Type> student) {
        // FILL IN -- do not return false unless appropriate
        if (lookup(student.getUNID()) == null) {
            studentList.add(student);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the CS 2420 student with the given uNID.
     *
     * @param uNID - uNID of student to be retrieved
     * @return the CS 2420 student with the given uNID, or null if no such
     * student exists in the collection
     */
    public CS2420StudentGeneric<Type> lookup(int uNID) {
        // FILL IN -- do not return null, unless appropriate
        for (CS2420StudentGeneric<Type> student : studentList) {
            if (student.getUNID() == uNID) {
                return student;
            }
        }
        return null;
    }

    /**
     * Retrieves the CS 2420 student(s) with the given contact information.
     *
     * @param contactInfo - contact information of student(s) to be retrieved
     * @return a list of the CS 2420 student(s) with the given contact
     * information (in any order), or an empty list if no such students exist in
     * the collection
     */
    public ArrayList<CS2420StudentGeneric<Type>> lookup(Type contactInfo) {
        // FILL IN -- do not return null
        ArrayList<CS2420StudentGeneric<Type>> results = new ArrayList<>();
        for (CS2420StudentGeneric<Type> student : studentList) {
            if (student.getContactInfo().equals(contactInfo)) {
                results.add(student);
            }
        }
        return results;
    }

    /**
     * Adds an assignment, exam, lab, quiz, or poll score for the CS 2420
     * student with the given uNID.
     *
     * NOTE: If the category string is not one of "assignment", "exam", "lab",
     * "quiz", or "poll", or if no student with the uNID exists in the
     * collection, then this method has no effect.
     *
     * @param uNID - uNID of student whose score is to be added
     * @param score - the score to be added
     * @param category - the category in which to add the score
     */
    public void addScore(int uNID, double score, String category) {
        // FILL IN
        CS2420StudentGeneric<Type> student = lookup(uNID);
        if (student != null) {
            student.addScore(score, category);
        }

    }

    /**
     * Computes the average score of all CS 2420 students in the collection.
     *
     * @return the average score, or 0 if there are no students in the
     * collection
     */
    public double computeClassAverage() {
        // FILL IN -- do not return 0, unless appropriate
        double total = 0;

        if (studentList.isEmpty()) {
            return 0;
        }

        for (CS2420StudentGeneric<Type> student : studentList) {
            total += student.computeFinalScore();
        }
        return total / studentList.size();
    }

    /**
     * Creates a list of contact information for all CS 2420 students in the
     * collection.
     *
     * @return the duplicate-free list of contact information, in any order
     */
    public ArrayList<Type> getContactList() {
        // FILL IN -- do not return null
        ArrayList<Type> contactList = new ArrayList<>();
        for (CS2420StudentGeneric<Type> student : studentList) {
            //Don't add contacts twice if two students share the same contact info
            if (!contactList.contains(student.getContactInfo())) {
                contactList.add(student.getContactInfo());
            }
        }
        return contactList;
    }

    /**
     * Returns the list of CS 2420 students in this class, sorted by uNID in
     * ascending order.
     */
    public ArrayList<CS2420StudentGeneric<Type>> getOrderedByUNID() {
        ArrayList<CS2420StudentGeneric<Type>> studentListCopy = new ArrayList<>();
        for (CS2420StudentGeneric<Type> student : studentList) {
            studentListCopy.add(student);
        }
        sort(studentListCopy, new OrderByUNID());
        return studentListCopy;
    }

    /**
     * Returns the list of CS 2420 students in this class, sorted by last name
     * in lexicographical order. Breaks ties in last names using first names
     * (lexicographical order). Breaks ties in first names using uNIDs
     * (ascending order).
     */
    public ArrayList<CS2420StudentGeneric<Type>> getOrderedByName() {
        ArrayList<CS2420StudentGeneric<Type>> studentListCopy = new ArrayList<>();
        for (CS2420StudentGeneric<Type> student : studentList) {
            studentListCopy.add(student);
        }
        sort(studentListCopy, new OrderByName());
        return studentListCopy;
    }

    /**
     * Returns the list of CS 2420 students in this class with a final score of
     * at least cutoffScore, sorted by final score in descending order. Breaks
     * ties in final scores using uNIDs (ascending order).
     *
     * @param cutoffScore - value that a student's final score must be at or
     * above to be included in the returned list
     */
    public ArrayList<CS2420StudentGeneric<Type>> getOrderedByScore(double cutoffScore) {
        // FILL IN — do not return null
        ArrayList<CS2420StudentGeneric<Type>> filteredList = new ArrayList<>();
        for (CS2420StudentGeneric<Type> student : studentList) {
            if (student.computeFinalScore() >= cutoffScore) {
                filteredList.add(student);
            }
        }

        filteredList.sort((s1, s2) -> {
            int scoreComparison = Double.compare(s2.computeFinalScore(), s1.computeFinalScore());
            if (scoreComparison != 0) {
                return scoreComparison; // Sort by final score in descending order
            } else {
                return Integer.compare(s1.getUNID(), s2.getUNID()); // Tie-breaker: sort by uNID in ascending order
            }
        });

        return filteredList;
    }

    /**
     * Performs a SELECTION SORT on the input ArrayList.
     *
     * 1. Finds the smallest item in the list. 2. Swaps the smallest item with
     * the first item in the list. 3. Reconsiders the list be the remaining
     * unsorted portion (second item to Nth item) and repeats steps 1, 2, and 3.
     */
    private static <ListType> void sort(ArrayList<ListType> list,
            Comparator<ListType> c) {
        for (int i = 0; i < list.size() - 1; i++) {
            int j, minIndex;
            for (j = i + 1, minIndex = i; j < list.size(); j++) {
                if (c.compare(list.get(j), list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            ListType temp = list.get(i);
            list.set(i, list.get(minIndex));
            list.set(minIndex, temp);
        }
    }

    /**
     * Comparator that defines an ordering among CS 2420 students using their
     * uNIDs. uNIDs are guaranteed to be unique, making a tie-breaker
     * unnecessary.
     */
    protected class OrderByUNID implements Comparator<CS2420StudentGeneric<Type>> {

        /**
         * Returns a negative value if lhs (left-hand side) is smaller than rhs
         * (right-hand side). Returns a positive value if lhs is larger than
         * rhs. Returns 0 if lhs and rhs are equal.
         */
        @Override
        public int compare(CS2420StudentGeneric<Type> lhs,
                CS2420StudentGeneric<Type> rhs) {
            return lhs.getUNID() - rhs.getUNID();
        }
    }

    /**
     * Comparator that defines an ordering among CS 2420 students using their
     * protected class OrderByName implements
     * Comparator<CS2420StudentGeneric<Type>> {
     *
     *      *@Override public int compare(CS2420StudentGeneric<Type> lhs,
     * CS2420StudentGeneric<Type> rhs) { int lastCmp =
     * lhs.getLastName().compareTo(rhs.getLastName()); if (lastCmp != 0) {
     * return lastCmp; }
     *
     *      *int firstCmp = lhs.getFirstName().compareTo(rhs.getFirstName()); if
     * (firstCmp != 0) { return firstCmp; }
     *
     *      *return lhs.getUNID() - rhs.getUNID(); } }
     */
    protected class OrderByName implements Comparator<CS2420StudentGeneric<Type>> {
        // FILL IN

        @Override
        public int compare(CS2420StudentGeneric<Type> lhs, CS2420StudentGeneric<Type> rhs) {
            int lastCmp = lhs.getLastName().compareTo(rhs.getLastName());
            if (lastCmp != 0) {
                return lastCmp;
            }

            int firstCmp = lhs.getFirstName().compareTo(rhs.getFirstName());
            if (firstCmp != 0) {
                return firstCmp;
            }

            return lhs.getUNID() - rhs.getUNID();
        }
    }
}
