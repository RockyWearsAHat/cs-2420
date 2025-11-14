package assign09;

/**
 * This class demonstrates how to use a hash table to store key-value pairs.
 * Includes examples with different key types (StudentXHash, String, Integer).
 *
 * @author Erin Parker
 * @author Alex Waldmann
 * @author Tyler Gagliardi
 * @version November 13, 2025
 */
public class StudentHashDemo {

    public static void main(String[] args) {
        System.out.println("=== HashTable Demo ===\n");

        // Demo 1: StudentBadHash with GPA values
        demonstrateStudentBadHash();

        // Demo 2: StudentMediumHash with course enrollment
        demonstrateStudentMediumHash();

        // Demo 3: StudentGoodHash with grades
        demonstrateStudentGoodHash();

        // Demo 4: String keys with Integer values
        demonstrateStringKeys();

        // Demo 5: Integer keys with String values
        demonstrateIntegerKeys();

        // Demo 6: HashTable operations (get, remove, containsKey, etc.)
        demonstrateOperations();
    }

    /**
     * Demonstrates using StudentBadHash as keys.
     */
    private static void demonstrateStudentBadHash() {
        System.out.println("--- Demo 1: StudentBadHash with GPA values ---");

        StudentBadHash alan = new StudentBadHash(1019999, "Alan", "Turing");
        StudentBadHash ada = new StudentBadHash(1004203, "Ada", "Lovelace");
        StudentBadHash edsger = new StudentBadHash(1010661, "Edsger", "Dijkstra");
        StudentBadHash grace = new StudentBadHash(1019941, "Grace", "Hopper");

        HashTable<StudentBadHash, Double> studentGpaTable = new HashTable<>();
        studentGpaTable.put(alan, 3.2);
        studentGpaTable.put(ada, 3.5);
        studentGpaTable.put(edsger, 3.8);
        studentGpaTable.put(grace, 4.0);

        for (MapEntry<StudentBadHash, Double> e : studentGpaTable.entries()) {
            System.out.println("Student " + e.getKey() + " has GPA " + e.getValue() + ".");
        }
        System.out.println("Total students: " + studentGpaTable.size());
        System.out.println();
    }

    /**
     * Demonstrates using StudentMediumHash as keys.
     */
    private static void demonstrateStudentMediumHash() {
        System.out.println("--- Demo 2: StudentMediumHash with course enrollment ---");

        StudentMediumHash alice = new StudentMediumHash(1234567, "Alice", "Smith");
        StudentMediumHash bob = new StudentMediumHash(2345678, "Bob", "Johnson");
        StudentMediumHash charlie = new StudentMediumHash(3456789, "Charlie", "Williams");

        HashTable<StudentMediumHash, String> courseTable = new HashTable<>();
        courseTable.put(alice, "CS 2420");
        courseTable.put(bob, "CS 3500");
        courseTable.put(charlie, "CS 4400");

        for (MapEntry<StudentMediumHash, String> e : courseTable.entries()) {
            System.out.println(e.getKey() + " is enrolled in " + e.getValue());
        }
        System.out.println("Total enrollments: " + courseTable.size());
        System.out.println();
    }

    /**
     * Demonstrates using StudentGoodHash as keys.
     */
    private static void demonstrateStudentGoodHash() {
        System.out.println("--- Demo 3: StudentGoodHash with grades ---");

        StudentGoodHash emma = new StudentGoodHash(4567890, "Emma", "Davis");
        StudentGoodHash frank = new StudentGoodHash(5678901, "Frank", "Miller");
        StudentGoodHash grace = new StudentGoodHash(6789012, "Grace", "Wilson");

        HashTable<StudentGoodHash, Character> gradeTable = new HashTable<>();
        gradeTable.put(emma, 'A');
        gradeTable.put(frank, 'B');
        gradeTable.put(grace, 'A');

        for (MapEntry<StudentGoodHash, Character> e : gradeTable.entries()) {
            System.out.println(e.getKey() + " received grade " + e.getValue());
        }
        System.out.println("Total grades: " + gradeTable.size());
        System.out.println();
    }

    /**
     * Demonstrates using String keys.
     */
    private static void demonstrateStringKeys() {
        System.out.println("--- Demo 4: String keys with Integer values ---");

        HashTable<String, Integer> inventory = new HashTable<>();
        inventory.put("Apples", 50);
        inventory.put("Bananas", 75);
        inventory.put("Oranges", 30);
        inventory.put("Grapes", 100);

        for (MapEntry<String, Integer> e : inventory.entries()) {
            System.out.println(e.getKey() + ": " + e.getValue() + " units");
        }
        System.out.println("Total items in inventory: " + inventory.size());
        System.out.println();
    }

    /**
     * Demonstrates using Integer keys.
     */
    private static void demonstrateIntegerKeys() {
        System.out.println("--- Demo 5: Integer keys with String values ---");

        HashTable<Integer, String> phoneBook = new HashTable<>();
        phoneBook.put(1001, "Alice");
        phoneBook.put(1002, "Bob");
        phoneBook.put(1003, "Charlie");
        phoneBook.put(1004, "Diana");

        for (MapEntry<Integer, String> e : phoneBook.entries()) {
            System.out.println("ID " + e.getKey() + ": " + e.getValue());
        }
        System.out.println("Total contacts: " + phoneBook.size());
        System.out.println();
    }

    /**
     * Demonstrates various HashTable operations.
     */
    private static void demonstrateOperations() {
        System.out.println("--- Demo 6: HashTable operations ---");

        HashTable<String, Integer> scores = new HashTable<>();

        // Put operation
        System.out.println("Adding scores...");
        scores.put("Math", 95);
        scores.put("Science", 88);
        scores.put("English", 92);
        scores.put("History", 85);
        System.out.println("Size after adding: " + scores.size());

        // Get operation
        System.out.println("\nRetrieving scores:");
        System.out.println("Math score: " + scores.get("Math"));
        System.out.println("Science score: " + scores.get("Science"));

        // ContainsKey operation
        System.out.println("\nChecking if keys exist:");
        System.out.println("Contains 'Math'? " + scores.containsKey("Math"));
        System.out.println("Contains 'Art'? " + scores.containsKey("Art"));

        // ContainsValue operation
        System.out.println("\nChecking if values exist:");
        System.out.println("Contains score 92? " + scores.containsValue(92));
        System.out.println("Contains score 100? " + scores.containsValue(100));

        // Update existing key
        System.out.println("\nUpdating Math score to 98...");
        Integer oldMathScore = scores.put("Math", 98);
        System.out.println("Old Math score: " + oldMathScore);
        System.out.println("New Math score: " + scores.get("Math"));

        // Remove operation
        System.out.println("\nRemoving English...");
        Integer removedScore = scores.remove("English");
        System.out.println("Removed score: " + removedScore);
        System.out.println("Size after removal: " + scores.size());
        System.out.println("Contains 'English'? " + scores.containsKey("English"));

        // IsEmpty operation
        System.out.println("\nIs table empty? " + scores.isEmpty());

        // Clear operation
        System.out.println("\nClearing all scores...");
        scores.clear();
        System.out.println("Size after clear: " + scores.size());
        System.out.println("Is table empty? " + scores.isEmpty());

        System.out.println();
    }
}
