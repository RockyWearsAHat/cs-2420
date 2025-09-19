package assign03;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import timing.TimingExperiment;

/**
 * Experiment to measure the running time for the binarySearch method of
 * SearchUtil.
 *
 * @author Alex Waldmann
 * @version 2025-09-10
 */
public class BinarySearchTimingExperiment extends TimingExperiment {

    private ArrayList<Integer> sortedList;
    private Integer searchItem;
    private IntegerComparator cmp;
    private final Random rng = new Random();

    public static void main(String[] args) {
        String problemSizeDescription = "SearchUtil.binarySearch() - Binary Search";
        int iterationCount = 10000; // Much higher since binary search is very fast

        int problemSizeMin = 1000;
        int problemSizeStep = 1000;
        int problemSizeCount = 20;
        List<Integer> problemSizes = buildProblemSizes(problemSizeMin, problemSizeStep, problemSizeCount);

        TimingExperiment timingExperiment = new BinarySearchTimingExperiment(
                problemSizeDescription, problemSizes, iterationCount);

        timingExperiment.warmup(10);
        timingExperiment.run();
        timingExperiment.print();
    }

    public BinarySearchTimingExperiment(String problemSizeDescription, List<Integer> problemSizes,
            int iterationCount) {
        super(problemSizeDescription, problemSizes, iterationCount);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        sortedList = new ArrayList<>();
        cmp = new IntegerComparator();

        // Add numbers 0 through problemSize-1 to the sorted list (already in order)
        for (int i = 0; i < problemSize; i++) {
            sortedList.add(i);
        }

        // Choose a random item that exists in the list to search for
        searchItem = rng.nextInt(problemSize);
    }

    @Override
    protected void runComputation() {
        SearchUtil.binarySearch(sortedList, searchItem, cmp);
    }
}
