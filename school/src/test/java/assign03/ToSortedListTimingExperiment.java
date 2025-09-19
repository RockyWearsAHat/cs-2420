package assign03;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import timing.TimingExperiment;

/**
 * Experiment to measure the running time for the contains method of
 * ArrayCollection
 *
 * @author CS 2420 course staff and ??
 * @version 2025-09-05
 */
public class ToSortedListTimingExperiment extends TimingExperiment {

    // member variables for the experiment
    ArrayCollection<Integer> randomCollection;
    IntegerComparator cmp = new IntegerComparator();

    private final Random rng = new Random();

    public static void main(String[] args) {
        String problemSizeDescription = "ArrayCollection.toSortedList()";
        int iterationCount = 10;

        int problemSizeMin = 1000; // TODO: initialize appropriately (do not use 0)
        int problemSizeStep = 1000; // TODO: initialize appropriately (do not use 0)
        int problemSizeCount = 20; // TODO: initialize appropriately (do not use 0)
        List<Integer> problemSizes = buildProblemSizes(problemSizeMin, problemSizeStep, problemSizeCount);

        TimingExperiment timingExperiment = new ToSortedListTimingExperiment(problemSizeDescription, problemSizes,
                iterationCount);

        timingExperiment.warmup(10); // TODO: choose a number of times for warmup (could be 0)
        timingExperiment.run();
        timingExperiment.print();

        // optional: write the results to a file if desired
        // timingExperiment.write(<choose a filename>);
    }

    public ToSortedListTimingExperiment(String problemSizeDescription, List<Integer> problemSizes,
            int iterationCount) {
        super(problemSizeDescription, problemSizes, iterationCount);
    }

    /**
     * Populates the collection with randomly-ordered inputs.
     *
     * @param problemSize - the size of the collection
     */
    @Override
    protected void setupExperiment(int problemSize) {
        // IMPORTANT: we need to reinitialize the collection
        //            for every experiment, or it would keep growing
        randomCollection = new ArrayCollection<>();

        // TODO: populate randomCollection with the values
        //       0 through problemSize - 1, in random order
        Integer[] nums = new Integer[problemSize];
        for (int i = 0; i < problemSize; i++) {
            nums[i] = i;
        }
        //shuffle the array
        for (int i = nums.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            // Swap nums[i] and nums[j]
            Integer temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        randomCollection.addAll(Arrays.asList(nums));
    }

    /**
     * Runs the toSortedList method for the ArrayCollection.
     */
    @Override
    protected void runComputation() {
        // note that the only thing in here a call to the method 
        // we are timing 
        // (not creating the comparator)
        randomCollection.toSortedList(cmp);
    }

}
