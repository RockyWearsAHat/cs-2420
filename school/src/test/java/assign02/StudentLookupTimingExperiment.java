package assign02;

import java.util.List;
import java.util.Random;

import timing.TimingExperiment;

/**
 * Experiment to measure the running time for the lookup method in libraries of
 * various sizes.
 *
 * @author CS 2420 course staff and ??
 * @version 2025-08-29
 */
public class StudentLookupTimingExperiment extends TimingExperiment {

    // member variables that need to be setup for each experiment
    private CS2420Class randomClass;
    private int lookForUNID;

    private Random rng = new Random();

    public static void main(String[] args) {
        String problemSizeDescription = ""; // TODO: fill in string appropriately
        int iterationCount = 200;

        int problemSizeMin = 1000; // TODO: initialize appropriately (do not use 0)
        int problemSizeStep = 1000; // TODO: initialize appropriately (do not use 0)
        int problemSizeCount = 30; // TODO: initialize appropriately (do not use 0)
        List<Integer> problemSizes = buildProblemSizes(problemSizeMin, problemSizeStep, problemSizeCount);

        TimingExperiment timingExperiment = new StudentLookupTimingExperiment(problemSizeDescription, problemSizes,
                iterationCount);

        // TODO: choose a number of times for warmup (could be 0)
        //       warming up can help eliminate timing spikes for the
        //       first value of N
        timingExperiment.warmup(10);
        timingExperiment.run();
        timingExperiment.print();

        // optional: write the results to a file if desired
        // timingExperiment.write(<choose a filename>);
    }

    public StudentLookupTimingExperiment(String problemSizeDescription, List<Integer> problemSizes,
            int iterationCount) {
        super(problemSizeDescription, problemSizes, iterationCount);
    }

    /**
     * Populates the library with random library books.
     *
     * @param problemSize - the size of the library
     */
    @Override
    protected void setupExperiment(int problemSize) {
        // create uNIDs 0 through N-1 for test data
        int uNIDs[] = new int[problemSize];
        for (int i = 0; i < problemSize; i++) {
            uNIDs[i] = i;
        }

        // randomly shuffle the uNIDs
        for (int i = 0; i < problemSize; i++) {
            int randomIndex = rng.nextInt(problemSize);
            int temp = uNIDs[i];
            uNIDs[i] = uNIDs[randomIndex];
            uNIDs[randomIndex] = temp;
        }

        // fill in the class
        randomClass = new CS2420Class();
        for (int i = 0; i < problemSize; i++) {
            randomClass.addStudent(new CS2420Student("first", "last", uNIDs[i], new EmailAddress("username", "domain")));

        }
        lookForUNID = -1;
    }

    /**
     * Runs the lookup (uNID) method for the class.
     */
    @Override
    protected void runComputation() {
        randomClass.lookup(lookForUNID);
    }

}
