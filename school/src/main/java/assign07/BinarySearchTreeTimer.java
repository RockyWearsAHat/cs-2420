package assign07;

import java.util.ArrayList;
import java.util.Random;

/**
 * Simple timing experiments for BinarySearchTree operations. Tests performance
 * to verify O(log N) behavior for balanced trees and O(N) behavior for skewed
 * trees.
 *
 * @author Alex Waldmann
 * @author Tyler Gagliardi
 * @version October 23, 2025
 */
public class BinarySearchTreeTimer {

    // Different iteration counts for different complexity classes
    private static final int WARMUP_ITERATIONS_LOG_N = 10;
    private static final int TIMING_ITERATIONS_LOG_N = 200;
    private static final int OPERATIONS_PER_TRIAL_LOG_N = 1000;

    private static final int WARMUP_ITERATIONS_LINEAR = 3;
    private static final int TIMING_ITERATIONS_LINEAR = 10;
    private static final int OPERATIONS_PER_TRIAL_LINEAR = 100;

    /**
     * Times how long it takes to perform operations, with proper setup
     * separation. Returns average time per operation in nanoseconds.
     */
    private static double timeOperationLogN(java.util.function.Supplier<Runnable> setupAndGetOperation) {
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS_LOG_N; i++) {
            Runnable op = setupAndGetOperation.get();
            op.run();
        }

        // Actual timing
        long totalTime = 0;
        for (int i = 0; i < TIMING_ITERATIONS_LOG_N; i++) {
            Runnable op = setupAndGetOperation.get(); // Setup happens here (not timed)
            long startTime = System.nanoTime();
            op.run(); // Only the operation is timed
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }

        return (double) totalTime / TIMING_ITERATIONS_LOG_N / OPERATIONS_PER_TRIAL_LOG_N;
    }

    /**
     * Times O(N) operations with fewer iterations.
     */
    private static double timeOperationLinear(java.util.function.Supplier<Runnable> setupAndGetOperation) {
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS_LINEAR; i++) {
            Runnable op = setupAndGetOperation.get();
            op.run();
        }

        // Actual timing
        long totalTime = 0;
        for (int i = 0; i < TIMING_ITERATIONS_LINEAR; i++) {
            Runnable op = setupAndGetOperation.get(); // Setup happens here (not timed)
            long startTime = System.nanoTime();
            op.run(); // Only the operation is timed
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }

        return (double) totalTime / TIMING_ITERATIONS_LINEAR / OPERATIONS_PER_TRIAL_LINEAR;
    }

    /**
     * Test add() with random insertion (should be O(log N) per operation).
     */
    private static void testAddRandom() {
        System.out.println("\n=== Testing add() with Random Insertion (Expected: O(log N)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "N", "Time (ns)", "Time/log(N) (ns)");
        System.out.println("-".repeat(50));

        int[] sizes = {10000, 20000, 40000, 60000, 80000, 100000, 140000, 180000, 220000, 260000, 300000, 350000};
        Random rand = new Random(42);

        for (int n : sizes) {
            // Time individual add() operations
            double avgTimeNs = timeOperationLogN(() -> {
                // Setup: Build tree to size n-1000, prepare values to add
                BinarySearchTree<Integer> tree = new BinarySearchTree<>();
                ArrayList<Integer> valuesToAdd = new ArrayList<>();

                for (int i = 0; i < n - OPERATIONS_PER_TRIAL_LOG_N; i++) {
                    tree.add(rand.nextInt(n * 2));
                }

                for (int i = 0; i < OPERATIONS_PER_TRIAL_LOG_N; i++) {
                    valuesToAdd.add(rand.nextInt(n * 2));
                }

                // Return operation to time: add 1000 more elements
                return () -> {
                    for (Integer value : valuesToAdd) {
                        tree.add(value);
                    }
                };
            });

            double timePerLogN = avgTimeNs / Math.log(n);
            System.out.printf("%-10d %-20.2f %-20.2f%n", n, avgTimeNs, timePerLogN);
        }
    }

    /**
     * Test add() with sorted insertion (should be O(N) per operation -
     * degenerate tree).
     */
    private static void testAddSorted() {
        System.out.println("\n=== Testing add() with Sorted Insertion (Expected: O(N)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "N", "Time (ns)", "Time/N (ns)");
        System.out.println("-".repeat(50));

        int[] sizes = {2000, 4000, 6000, 8000, 10000, 12000, 14000, 16000, 18000, 20000, 22000, 25000};

        for (int n : sizes) {
            // Time individual add() operations on degenerate tree
            double avgTimeNs = timeOperationLinear(() -> {
                // Setup: Build degenerate tree to size n-100
                BinarySearchTree<Integer> tree = new BinarySearchTree<>();
                for (int i = 0; i < n - OPERATIONS_PER_TRIAL_LINEAR; i++) {
                    tree.add(i);
                }

                int startVal = n - OPERATIONS_PER_TRIAL_LINEAR;

                // Return operation to time: add 100 more sorted elements
                return () -> {
                    for (int i = 0; i < OPERATIONS_PER_TRIAL_LINEAR; i++) {
                        tree.add(startVal + i);
                    }
                };
            });

            double timePerN = avgTimeNs / n;
            System.out.printf("%-10d %-20.2f %-20.4f%n", n, avgTimeNs, timePerN);
        }
    }

    /**
     * Test contains() on balanced tree (should be O(log N) per lookup).
     */
    private static void testContainsBalanced() {
        System.out.println("\n=== Testing contains() on Balanced Tree (Expected: O(log N)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "N", "Time (ns)", "Time/log(N) (ns)");
        System.out.println("-".repeat(50));

        int[] sizes = {10000, 20000, 40000, 60000, 80000, 100000, 140000, 180000, 220000, 260000, 300000, 350000};
        Random rand = new Random(42);

        for (int n : sizes) {
            // Time individual contains() operations
            double avgTimeNs = timeOperationLogN(() -> {
                // Setup: Build balanced tree and prepare search values
                BinarySearchTree<Integer> tree = new BinarySearchTree<>();
                ArrayList<Integer> searches = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    int value = rand.nextInt(n * 2);
                    tree.add(value);
                    if (i % (n / OPERATIONS_PER_TRIAL_LOG_N) == 0 && searches.size() < OPERATIONS_PER_TRIAL_LOG_N) {
                        searches.add(value);
                    }
                }

                // Fill remaining search values if needed
                while (searches.size() < OPERATIONS_PER_TRIAL_LOG_N) {
                    searches.add(rand.nextInt(n * 2));
                }

                // Return operation to time: perform 1000 lookups
                return () -> {
                    for (Integer value : searches) {
                        tree.contains(value);
                    }
                };
            });

            double timePerLogN = avgTimeNs / Math.log(n);
            System.out.printf("%-10d %-20.2f %-20.2f%n", n, avgTimeNs, timePerLogN);
        }
    }

    /**
     * Test toArrayList() (should be O(N) - visits every node once).
     */
    private static void testToArrayList() {
        System.out.println("\n=== Testing toArrayList() (Expected: O(N)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "N", "Time (ns)", "Time/N (ns)");
        System.out.println("-".repeat(50));

        int[] sizes = {10000, 20000, 40000, 60000, 80000, 100000, 140000, 180000, 220000, 260000, 300000, 350000};
        Random rand = new Random(42);

        for (int n : sizes) {
            // Time toArrayList() operations
            double avgTimeNs = timeOperationLinear(() -> {
                // Setup: Build tree
                BinarySearchTree<Integer> tree = new BinarySearchTree<>();
                for (int i = 0; i < n; i++) {
                    tree.add(rand.nextInt(n * 2));
                }

                // Return operation to time: call toArrayList() 100 times
                return () -> {
                    for (int i = 0; i < OPERATIONS_PER_TRIAL_LINEAR; i++) {
                        tree.toArrayList();
                    }
                };
            });

            double timePerN = avgTimeNs / n;
            System.out.printf("%-10d %-20.2f %-20.4f%n", n, avgTimeNs, timePerN);
        }
    }

    /**
     * Main method to run all timing experiments.
     */
    public static void main(String[] args) {
        System.out.println("BinarySearchTree Timing Experiments");
        System.out.println("====================================");

        testAddRandom();
        testAddSorted();
        testContainsBalanced();
        testToArrayList();

        System.out.println("\n=== Analysis ===");
        System.out.println("For O(log N) operations: Time/log(N) should remain roughly constant");
        System.out.println("For O(N) operations: Time/N should remain roughly constant");
        System.out.println("\nNote: O(log N) tests perform " + OPERATIONS_PER_TRIAL_LOG_N + " operations per trial");
        System.out.println("      O(N) tests perform " + OPERATIONS_PER_TRIAL_LINEAR + " operations per trial");
        System.out.println("      Times shown are average nanoseconds per operation");
        System.out.println("      Setup time (tree construction) is NOT included in measurements");
    }
}
