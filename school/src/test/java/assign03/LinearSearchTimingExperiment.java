package assign03;

import java.util.List;
import java.util.Random;

import timing.TimingExperiment;

/**
 * Experiment to measure the running time for the contains method of
 * ArrayCollection (linear search).
 *
 * @author Alex Waldmann
 * @version 2025-09-10
 */
public class LinearSearchTimingExperiment extends TimingExperiment {

    private ArrayCollection<Integer> collection;
    private Integer searchItem;
    private final Random rng = new Random();

    public static void main(String[] args) {
        String problemSizeDescription = "ArrayCollection.contains() - Linear Search";
        int iterationCount = 1000; // Higher iteration count since contains is fast

        int problemSizeMin = 1000;
        int problemSizeStep = 1000;
        int problemSizeCount = 20;
        List<Integer> problemSizes = buildProblemSizes(problemSizeMin, problemSizeStep, problemSizeCount);

        TimingExperiment timingExperiment = new LinearSearchTimingExperiment(
                problemSizeDescription, problemSizes, iterationCount);

        timingExperiment.warmup(10);
        timingExperiment.run();
        timingExperiment.print();
    }

    public LinearSearchTimingExperiment(String problemSizeDescription, List<Integer> problemSizes,
            int iterationCount) {
        super(problemSizeDescription, problemSizes, iterationCount);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        collection = new ArrayCollection<>();

        // Add numbers 0 through problemSize-1 to the collection
        for (int i = 0; i < problemSize; i++) {
            collection.add(i);
        }

        // Choose a random item that exists in the collection to search for
        searchItem = rng.nextInt(problemSize);
    }

    @Override
    protected void runComputation() {
        collection.contains(searchItem);
    }
}
