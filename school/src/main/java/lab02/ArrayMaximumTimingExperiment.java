// package lab02;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Random;

// /**
//  * Timing experiment to measure the time it takes to find the maximum integer in
//  * arrays of various sizes.
//  *
//  * @author CS 2420 Course Staff
//  * @version 2025-08-29
//  */
// public class ArrayMaximumTimingExperiment {

//     private int[] array;
//     // Random number generator to populate the array
//     private final Random rng = new Random();

//     private int experimentIterationCount;
//     private List<Integer> arraySizes;

//     public static void main(String[] args) {
//         // Instantiate the timing experiment
//         // Build the array sizes
//         List<Integer> arraySizes = buildArraySizes(200000, 10000, 30);
//         int iterationCount = 50;
//         // Instantiate the timing experiment
//         ArrayMaximumTimingExperiment timingExperiment = new ArrayMaximumTimingExperiment(arraySizes, iterationCount);

//         // Run the experiment and print the results.
//         timingExperiment.printResults();
//     }

//     /**
//      * Compute the time elapsed to find the maximum of an array.
//      *
//      * @param arraySize - size of the array
//      * @return the time elapsed
//      */
//     private long computeElapsedTime(int arraySize) {
//         setupArray(arraySize);

//         long startTime = System.nanoTime();
//         runComputation();
//         long endTime = System.nanoTime();

//         return endTime - startTime;
//     }

//     /**
//      * Constructor to build a timing experiment for computing the maximum
//      * integer in an array
//      *
//      * @param arraySizes - the array sizes to run the experiment for
//      * @param iterationCount - Number of times to run computation for a given
//      * array size
//      */
//     public ArrayMaximumTimingExperiment(List<Integer> arraySizes, int iterationCount) {
//         this.arraySizes = arraySizes;
//         this.experimentIterationCount = iterationCount;
//     }

//     /**
//      * Run the timing experiment and print the results.
//      */
//     public void printResults() {
//         int arraySize = 200000;

//         // Number of times to repeat the same experiment
//         int experimentIterationCount = 50;

//         // Compute the time elapsed experimentIterationCount number of times
//         long[] elapsedTimes = new long[experimentIterationCount];
//         for (int i = 0; i < experimentIterationCount; i++) {
//             elapsedTimes[i] = computeElapsedTime(arraySize);
//         }

//         // Print all of the elapsed times
//         System.out.println("Elapsed times: " + Arrays.toString(elapsedTimes));

//         // Compute and print the average elapsed time
//         double averageElapsedTime = 0;
//         for (long elapsedTime : elapsedTimes) {
//             averageElapsedTime += elapsedTime;
//         }
//         averageElapsedTime /= experimentIterationCount;
//         System.out.println("average elapsed time: " + averageElapsedTime);

//         // Sort the elapsed times and print out the min, max, and median
//         Arrays.sort(elapsedTimes);

//         // Print out the min, max, median, and the first and third quartile values for the elapsed times
//         System.out.println("minimum elapsed time: " + elapsedTimes[0]);
//         System.out.println("median  elapsed time: " + elapsedTimes[experimentIterationCount / 2]);
//         System.out.println("maximum elapsed time: " + elapsedTimes[experimentIterationCount - 1]);
//     }

//     /**
//      * Populate the array with random integers.
//      *
//      * @param arraySize - size of the array
//      */
//     private void setupArray(int arraySize) {
//         array = new int[arraySize];
//         for (int i = 0; i < arraySize; i++) {
//             array[i] = rng.nextInt();
//         }
//     }

//     /**
//      * Run the computeMaximum function on the array.
//      */
//     private void runComputation() {
//         ArrayUtility.computeMaximum(array);
//     }

//     /**
//      * Construct a list of array sizes.
//      *
//      * @param arraySizeMin - The minimum array size
//      * @param arraySizeStep - The step between consecutive array sizes
//      * @param arraySizeCount - The number of array sizes in the list
//      * @return the list of array sizes
//      */
//     public static List<Integer> buildArraySizes(int sizeMin, int sizeStep, int sizeCount) {
//         int size = sizeMin;
//         List<Integer> arraySizes = new ArrayList<>();
//         for (int i = 0; i < sizeCount; i++) {
//             arraySizes.add(size);
//             size += sizeStep;
//         }
//         return arraySizes;
//     }
// }
