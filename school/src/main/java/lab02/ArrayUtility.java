package lab02;

/**
 * Class with static method to compute the maximum element in an integer array.
 *
 * @author CS 2420 Course Staff
 * @version 2025-08-29
 */
public class ArrayUtility {

  /**
   * Compute the maximum integer in an array.
   * @param array - array of integers
   * @return - maximum integer in the array
   * @throws IllegalArgumentException if array is empty.
   */
  public static int computeMaximum(int[] array) throws IllegalArgumentException {
    if (array.length == 0) {
      throw new IllegalArgumentException("Maximum of empty array is not defined");
    }

    int maximum = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] > maximum) {
        maximum = array[i];
      }
    }

    return maximum;
  }
}
