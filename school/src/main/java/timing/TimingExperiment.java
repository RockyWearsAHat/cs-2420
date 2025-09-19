package timing;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for running timing experiments.
 *
 * @author CS 2420 course staff
 * @version 2025-08-22
 */
public abstract class TimingExperiment {

    protected String problemSizeName;
    protected List<Integer> problemSizes;
    protected int iterationCount;

    protected NumberFormat formatter;
    private ProgressTracker progressTracker;
    private List<Long> medianTimes;

    /**
     * Construct an array list of problem sizes.
     *
     * @param sizeMin - The minimum problem size
     * @param sizeStep - The step between consecutive problem sizes
     * @param sizeCount - The number of problem sizes in the list
     * @return the list of problem sizes
     */
    public static List<Integer> buildProblemSizes(
            int sizeMin,
            int sizeStep,
            int sizeCount
    ) {
        List<Integer> problemSizes = new ArrayList<Integer>();
        for (int i = 0; i < sizeCount; i++) {
            problemSizes.add(sizeMin);
            sizeMin += sizeStep;
        }
        return problemSizes;
    }

    /**
     * Construct a timing experiment.
     *
     * @param problemSizeName - the name of the problem size in the experiment
     * @param problemSizes - the list of problem sizes on which to run the
     * experiment
     * @param iterationCount - the number of times to run the experiment for
     * each problem size
     */
    public TimingExperiment(
            String problemSizeName,
            List<Integer> problemSizes,
            int iterationCount
    ) {
        this.problemSizeName = problemSizeName;
        this.problemSizes = problemSizes;
        this.iterationCount = iterationCount;
        this.formatter = new DecimalFormat("0.00000E0");
        this.progressTracker = new ProgressTracker(problemSizes.size());
        this.medianTimes = new ArrayList<Long>();
    }

    /**
     * Abstract method for setting up the experiment infrastructure for a given
     * problem size.
     *
     * @param problemSize - the problem size for one experiment
     */
    protected abstract void setupExperiment(int problemSize);

    /**
     * Abstract method to run the computation that will be timed.
     */
    protected abstract void runComputation();

    /**
     * Run the experiment on the given problem sizes and record the results.
     */
    public void run() {
        for (int i = 0; i < this.problemSizes.size(); i++) {
            this.progressTracker.printProgress(i);
            long medianTime = this.computeMedianElapsedTime(this.problemSizes.get(i));
            this.medianTimes.add(medianTime);
        }
    }

    /**
     * Attempt to warm up the JRE by running some unrecorded experiments.
     *
     * @param count - the number of times to compute a median elapsed time
     */
    public void warmup(int count) {
        while (count > 0) {
            this.computeMedianElapsedTime(this.problemSizes.get(0) + count--);
        }
    }

    /**
     * Convert the timing experiment results to a string.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.problemSizeName + "\ttime (ns)");
        for (int i = 0; i < this.problemSizes.size(); i++) {
            builder.append('\n');
            builder.append(this.problemSizes.get(i));
            builder.append('\t');
            builder.append(this.formatter.format(this.medianTimes.get(i)));
        }
        return builder.toString();
    }

    /**
     * Print the timing experiment results.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Write the timing experiment results to a file.
     *
     * @param filename - the filename to which the results will be written
     */
    public void write(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(this.toString());
            writer.close();
            System.out.println("Results written to " + filename);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Get the median elapsed times of the timing experiment.
     *
     * @return the list of median times corresponding to each problem size
     */
    public List<Long> getMedianTimes() {
        return this.medianTimes;
    }

    /**
     * Get the problem sizes of the timing experiment.
     *
     * @return the list of problem sizes for which the timing experiment is run
     */
    public List<Integer> getProblemSizes() {
        return this.problemSizes;
    }

    /**
     * Compute and return the median time elapsed to run the computation for a
     * problem size.
     *
     * @param problemSize - the problem size for one experiment
     * @return the median elapsed time of the experiment iterations
     */
    protected long computeMedianElapsedTime(int problemSize) {
        long[] elapsedTimes = new long[this.iterationCount];
        for (int i = 0; i < this.iterationCount; i++) {
            elapsedTimes[i] = this.computeElapsedTime(problemSize);
        }
        Arrays.sort(elapsedTimes);
        return elapsedTimes[iterationCount / 2];
    }

    /**
     * Compute and return the elapsed time to run the computation for a problem
     * size.
     *
     * @param problemSize - the problem size for one experiment
     * @return the elapsed time for one iteration of one experiment
     */
    protected long computeElapsedTime(int problemSize) {
        this.setupExperiment(problemSize);
        long startTime = System.nanoTime();
        this.runComputation();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    /**
     * Class to track the progress of a timing experiment.
     */
    protected static class ProgressTracker {

        private final int count;
        private int current;

        /**
         * Construct a progress tracker.
         *
         * @param count - the number of problem sizes to track
         */
        public ProgressTracker(int count) {
            this.count = count;
            this.current = 0;
        }

        /**
         * Print the progress for a given index.
         *
         * @param index - the index of the current experiment
         */
        public void printProgress(int index) {
            if (index == 0) {
                System.out.println("\t\tprogress");
                System.out.println("+--------------------------------------+");
            }
            int progress = (40 * index) / this.count;
            while (this.current <= progress) {
                this.current++;
                System.out.print('>');
            }
            if (index == this.count - 1) {
                System.out.println('\n');
            }
        }
    }
}
