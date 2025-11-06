package assign07;

import java.util.ArrayList;
import java.util.List;

import timing.TimingExperiment;

/**
 * Timing experiment to measure the performance of sorting using a Binary Search
 * Tree. This experiment uses the SortViaBST.sort() method to sort permuted
 * ArrayLists.
 *
 * @author Alex Waldmann
 * @author Tyler Gagliardi
 * @version October 23, 2025
 */
public class BSTSortTimingExperiment extends TimingExperiment {

    private ArrayList<Integer> listToSort;

    /**
     * Construct a BST sort timing experiment.
     *
     * @param problemSizeName - the name of the problem size (e.g., "N")
     * @param problemSizes - the list of problem sizes to test
     * @param iterationCount - the number of iterations per problem size
     */
    public BSTSortTimingExperiment(String problemSizeName, List<Integer> problemSizes, int iterationCount) {
        super(problemSizeName, problemSizes, iterationCount);
    }

    /**
     * Set up the experiment by generating a permuted ArrayList without
     * duplicates.
     *
     * @param problemSize - the size of the list to generate
     */
    @Override
    protected void setupExperiment(int problemSize) {
        listToSort = ArrayListGenerator.generatePermutedArrayListWithoutDuplicates(problemSize);
    }

    /**
     * Run the BST-based sort computation.
     */
    @Override
    protected void runComputation() {
        SortViaBST.sort(listToSort);
    }

    /**
     * Main method to run the BST sort timing experiment.
     */
    public static void main(String[] args) {
        // Build problem sizes from 1000 to 20000 in steps of 1000
        List<Integer> problemSizes = TimingExperiment.buildProblemSizes(1000, 1000, 20);

        // Create the experiment with 20 iterations per problem size
        BSTSortTimingExperiment experiment = new BSTSortTimingExperiment("N", problemSizes, 20);

        // Warm up the JVM
        System.out.println("Warming up...");
        experiment.warmup(5);

        // Run the experiment
        System.out.println("\nRunning BST Sort Timing Experiment...");
        experiment.run();

        // Print and write results
        System.out.println("\nResults:");
        experiment.print();
        experiment.write("bst_sort_timing.txt");

        // Perform "check analysis" - calculate Time/N and Time/(N log N)
        System.out.println("\n=== Check Analysis ===");
        System.out.println("N\t\tTime (ns)\tTime/N (ns)\tTime/(N log N) (ns)");
        System.out.println("-".repeat(70));

        List<Long> times = experiment.getMedianTimes();
        for (int i = 0; i < problemSizes.size(); i++) {
            int n = problemSizes.get(i);
            long time = times.get(i);
            double timePerN = (double) time / n;
            double logN = Math.log(n) / Math.log(2);
            double timePerNLogN = (double) time / (n * logN);

            System.out.printf("%d\t\t%d\t\t%.2f\t\t%.2f%n", n, time, timePerN, timePerNLogN);
        }

        System.out.println("\nAnalysis:");
        System.out.println("- If Time/N is roughly constant, the algorithm is O(N)");
        System.out.println("- If Time/(N log N) is roughly constant, the algorithm is O(N log N)");
        System.out.println("\nExpected: O(N log N) for average case (balanced BST)");
        System.out.println("          O(N²) for worst case (degenerate BST)");
        System.out.println("\nNote: With permuted input, the BST should be relatively balanced,");
        System.out.println("      so we expect O(N log N) behavior.");
    }
}
