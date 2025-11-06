package assign07;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simple timing experiments for SpellChecker operations. Tests performance to
 * verify O(log N) behavior for dictionary operations.
 *
 * @author Alex Waldmann
 * @author Tyler Gagliardi
 * @version October 23, 2025
 */
public class SpellCheckerTimer {

    // Different iteration counts for different complexity classes
    private static final int WARMUP_ITERATIONS_LOG_N = 10;
    private static final int TIMING_ITERATIONS_LOG_N = 200;
    private static final int OPERATIONS_PER_TRIAL_LOG_N = 1000;

    private static final int WARMUP_ITERATIONS_LINEAR = 3;
    private static final int TIMING_ITERATIONS_LINEAR = 10;
    private static final int OPERATIONS_PER_TRIAL_LINEAR = 100;

    /**
     * Times O(log N) operations with proper setup separation. Returns average
     * time per operation in nanoseconds.
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
     * Times O(N) or O(M) operations with fewer iterations.
     */
    private static double timeOperationLinear(java.util.function.Supplier<Runnable> setupAndGetOperation, int operationsCount) {
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

        return (double) totalTime / TIMING_ITERATIONS_LINEAR / operationsCount;
    }

    /**
     * Generate random words for testing.
     */
    private static List<String> generateWords(int count, int maxLength, Random rand) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int length = 3 + rand.nextInt(maxLength - 2);
            StringBuilder word = new StringBuilder();
            for (int j = 0; j < length; j++) {
                word.append((char) ('a' + rand.nextInt(26)));
            }
            words.add(word.toString());
        }
        return words;
    }

    /**
     * Test addToDictionary() with varying dictionary sizes (should be O(log N)
     * per add).
     */
    private static void testAddToDictionary() {
        System.out.println("\n=== Testing addToDictionary() (Expected: O(log N)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "N", "Time (ns)", "Time/log(N) (ns)");
        System.out.println("-".repeat(50));

        int[] sizes = {10000, 20000, 40000, 60000, 80000, 100000, 140000, 180000, 220000, 260000, 300000, 350000};
        Random rand = new Random(42);

        for (int n : sizes) {
            // Time individual addToDictionary() operations
            double avgTimeNs = timeOperationLogN(() -> {
                // Setup: Build dictionary to size n-1000, prepare words to add
                SpellChecker checker = new SpellChecker();
                List<String> wordsToAdd = new ArrayList<>();

                List<String> allWords = generateWords(n, 10, rand);
                for (int i = 0; i < n - OPERATIONS_PER_TRIAL_LOG_N; i++) {
                    checker.addToDictionary(allWords.get(i));
                }

                for (int i = n - OPERATIONS_PER_TRIAL_LOG_N; i < n; i++) {
                    wordsToAdd.add(allWords.get(i));
                }

                // Return operation to time: add 1000 more words
                return () -> {
                    for (String word : wordsToAdd) {
                        checker.addToDictionary(word);
                    }
                };
            });

            double timePerLogN = avgTimeNs / Math.log(n);
            System.out.printf("%-10d %-20.2f %-20.2f%n", n, avgTimeNs, timePerLogN);
        }
    }

    /**
     * Test spellCheck() with varying dictionary sizes (should be O(log N) per
     * lookup).
     */
    private static void testSpellCheckVaryingDictionary() {
        System.out.println("\n=== Testing spellCheck() with Varying Dictionary Size (Expected: O(log N)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "Dict Size", "Time (ns)", "Time/log(N) (ns)");
        System.out.println("-".repeat(50));

        int[] dictSizes = {10000, 20000, 40000, 60000, 80000, 100000, 140000, 180000, 220000, 260000, 300000, 350000};
        Random rand = new Random(42);

        // Prepare a fixed document file to check
        try {
            File docFile = File.createTempFile("spellcheck_doc_", ".txt");
            docFile.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(docFile)) {
                List<String> wordsToCheck = generateWords(OPERATIONS_PER_TRIAL_LOG_N, 10, rand);
                for (String word : wordsToCheck) {
                    writer.print(word + " ");
                }
            }

            for (int dictSize : dictSizes) {
                // Time spellCheck operations
                double avgTimeNs = timeOperationLogN(() -> {
                    // Setup: Build dictionary
                    SpellChecker checker = new SpellChecker();
                    List<String> dictWords = generateWords(dictSize, 10, rand);
                    for (String word : dictWords) {
                        checker.addToDictionary(word);
                    }

                    // Return operation to time: spellCheck the document
                    return () -> {
                        try {
                            checker.spellCheck(docFile);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                });

                double timePerLogN = avgTimeNs / Math.log(dictSize);
                System.out.printf("%-10d %-20.2f %-20.2f%n", dictSize, avgTimeNs, timePerLogN);
            }
        } catch (Exception e) {
            System.err.println("Error in testSpellCheckVaryingDictionary: " + e.getMessage());
        }
    }

    /**
     * Test spellCheck() with varying document sizes (should be O(M log N),
     * linear in doc size). Note: This still includes some file I/O overhead,
     * but it's amortized over multiple runs.
     */
    private static void testSpellCheckVaryingDocument() {
        System.out.println("\n=== Testing spellCheck() with Varying Document Size (Expected: O(M)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "Doc Size", "Time (ns)", "Time/M (ns)");
        System.out.println("-".repeat(50));

        int[] docSizes = {5000, 10000, 20000, 30000, 40000, 50000, 65000, 80000, 95000, 110000, 125000, 150000};
        Random rand = new Random(42);

        // Build fixed-size dictionary
        SpellChecker checker = new SpellChecker();
        List<String> dictWords = generateWords(20000, 10, rand);
        for (String word : dictWords) {
            checker.addToDictionary(word);
        }

        try {
            for (int docSize : docSizes) {
                // Create the document file ONCE in setup, time spellCheck multiple times
                File docFile = File.createTempFile("spellcheck_doc_", ".txt");
                docFile.deleteOnExit();

                // Write document file (this happens in setup, not timed)
                try (PrintWriter writer = new PrintWriter(docFile)) {
                    for (int i = 0; i < docSize; i++) {
                        if (rand.nextBoolean() && !dictWords.isEmpty()) {
                            writer.print(dictWords.get(rand.nextInt(dictWords.size())) + " ");
                        } else {
                            writer.print(generateWords(1, 10, rand).get(0) + " ");
                        }
                    }
                }

                // Now time just the spellCheck operation (file exists, we're just reading/checking)
                // Warmup
                for (int i = 0; i < WARMUP_ITERATIONS_LINEAR; i++) {
                    checker.spellCheck(docFile);
                }

                // Actual timing
                long totalTime = 0;
                for (int i = 0; i < TIMING_ITERATIONS_LINEAR; i++) {
                    long startTime = System.nanoTime();
                    checker.spellCheck(docFile);
                    long endTime = System.nanoTime();
                    totalTime += (endTime - startTime);
                }

                double avgTimeNs = (double) totalTime / TIMING_ITERATIONS_LINEAR;
                double timePerM = avgTimeNs / docSize;
                System.out.printf("%-10d %-20.2f %-20.4f%n", docSize, avgTimeNs, timePerM);
            }
        } catch (Exception e) {
            System.err.println("Error in testSpellCheckVaryingDocument: " + e.getMessage());
        }
    }

    /**
     * Test removeFromDictionary() (should be O(log N) per removal).
     */
    private static void testRemoveFromDictionary() {
        System.out.println("\n=== Testing removeFromDictionary() (Expected: O(log N)) ===");
        System.out.printf("%-10s %-20s %-20s%n", "Dict Size", "Time (ns)", "Time/log(N) (ns)");
        System.out.println("-".repeat(50));

        int[] sizes = {10000, 20000, 40000, 60000, 80000, 100000, 140000, 180000, 220000, 260000, 300000, 350000};
        Random rand = new Random(42);

        for (int n : sizes) {
            // Time individual removeFromDictionary() operations
            double avgTimeNs = timeOperationLogN(() -> {
                // Setup: Build dictionary and prepare words to remove
                SpellChecker checker = new SpellChecker();
                List<String> words = generateWords(n, 10, rand);
                for (String word : words) {
                    checker.addToDictionary(word);
                }

                List<String> wordsToRemove = new ArrayList<>();
                for (int i = 0; i < OPERATIONS_PER_TRIAL_LOG_N && i < words.size(); i++) {
                    wordsToRemove.add(words.get(i));
                }

                // Return operation to time: remove 1000 words
                return () -> {
                    for (String word : wordsToRemove) {
                        checker.removeFromDictionary(word);
                    }
                };
            });

            double timePerLogN = avgTimeNs / Math.log(n);
            System.out.printf("%-10d %-20.2f %-20.2f%n", n, avgTimeNs, timePerLogN);
        }
    }

    /**
     * Main method to run all timing experiments.
     */
    public static void main(String[] args) {
        System.out.println("SpellChecker Timing Experiments");
        System.out.println("================================");

        testAddToDictionary();
        testSpellCheckVaryingDictionary();
        testSpellCheckVaryingDocument();
        testRemoveFromDictionary();

        System.out.println("\n=== Analysis ===");
        System.out.println("Dictionary operations (add, remove, lookup) should be O(log N)");
        System.out.println("  - Time/log(N) should remain roughly constant as N grows");
        System.out.println("\nSpell checking M words with N-word dictionary should be O(M log N)");
        System.out.println("  - Linear in document size M");
        System.out.println("  - Logarithmic in dictionary size N");
        System.out.println("\nNote: O(log N) tests perform " + OPERATIONS_PER_TRIAL_LOG_N + " operations per trial");
        System.out.println("      O(M) tests vary with document size");
        System.out.println("      Times shown are average nanoseconds per operation");
        System.out.println("      Setup time (dictionary/tree construction) is NOT included in measurements");
    }
}
