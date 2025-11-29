package comprehensive;

/**
 * This class runs a comprehensive performance benchmark of the
 * RandomPhraseGenerator. It generates a large number of phrases (1 million by
 * default) and measures the average execution time across multiple runs to
 * ensure consistent results.
 *
 * <p>
 * The experiment includes JVM warmup runs and garbage collection between test
 * runs to minimize variance.
 *
 * @author Alex Waldmann
 * @author Tyler Gagliardi
 * @version November 28, 2025
 */
public class FinalComparisonExperiment {

    /**
     * Main entry point. Runs the performance benchmark.
     *
     * @param args optional:
     * <ul>
     * <li>args[0] - alternate grammar</li>
     * <li>args[1] - number of phrases to generate (default 1,000,000)</li>
     * </ul>
     *
     */
    public static void main(String[] args) {
        String grammarFile = args.length > 0 ? args[0] : "./school/src/main/java/comprehensive/poetic_sentence.g";
        int numPhrases = args.length > 1 ? Integer.parseInt(args[1]) : 10000000; // 1 billion ran in ~96,000 ms (~1.6 minutes)

        //Warmup
        System.out.println("Warming up...");
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            public void write(int b) {
            }
        }));

        for (int i = 0; i < 10; i++) {
            RandomPhraseGenerator.main(new String[]{grammarFile, "1000"});
        }

        System.setOut(originalOut);

        //Begin experiment
        System.out.println("Running experiment...");

        long totalTime = 0;
        int runs = 20;

        for (int i = 0; i < runs; i++) {
            System.gc();
            long startTime = System.nanoTime();

            System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
                public void write(int b) {
                }
            }));

            RandomPhraseGenerator.main(new String[]{grammarFile, String.valueOf(numPhrases)});

            long endTime = System.nanoTime();
            System.setOut(originalOut);

            long duration = (endTime - startTime) / 1_000_000; // ms
            System.out.println("Run " + i + ": " + duration + " ms");
            totalTime += duration;
        }

        System.out.println("Average time: " + (totalTime / runs) + " ms");
    }
}
