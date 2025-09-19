package assign01;

/**
 * This class represents a vector of numbers. Our column Vector can be
 * considered a more specific kind of Matrix, so this is a subclass of Matrix.
 * It has a length that is the number of columns.
 *
 * @author Eric Heisler & Alex Waldmann
 * @version May 1, 2023
 */
public class Vector extends Matrix {

    // Instance variables for data and size are inherited from Matrix
    /**
     * Creates a new vector by copying in an array of numbers. The number of
     * columns in the vector matches the length of the array.
     *
     * @param data - an array holding the numbers of the vector
     * @throws IllegalArgumentException if the length of the input array is
     * zero.
     */
    public Vector(double[] data) {
        // Call the super constructor in Matrix
        // It is expecting a 2D array, so first convert it.
        super(vectorToColumnMatrix(data));
    }

    /**
     * Returns the dot product of this vector and another vector.
     *
     * @param other - another vector to be combined with this vector in the dot
     * product
     * @return the dot product
     * @throws IllegalArgumentException if the other vector and this vector are
     * not the same size.
     */
    public double dotProduct(Vector other) {
        // STUDENT: Fill in with code to fulfill the method contract above.
        //          Do not just return 0.

        //Null check || if vector sizes differ
        if (other == null || this.columns != other.columns) {
            throw new IllegalArgumentException("Incompatible vector sizes");
        }

        //Create a var to store dot product
        double dotProduct = 0;

        for (int i = 0; i < this.rows; i++) {
            //Loop through array and calculate dot product 
            //a: [a1, a2] b: [b1, b2] // a * b => a1 * b1 + a2 * b2
            dotProduct += this.data[i][0] * other.data[i][0];
        }
        return dotProduct;
    }

    /**
     * Computes and returns this vector's magnitude (also known as a vector's
     * length). This is the square root of the dot product of this vector with
     * itself.
     *
     * @return the vector magnitude
     */
    public double magnitude() {
        // STUDENT: Fill in with code to fulfill the method contract above.
        //          Do not just return 0.
        //Sqrt dot product returns double
        double mag = this.dotProduct(this);
        return Math.sqrt(mag);
    }

    /**
     * Converts a 1D array of vector data to a 2D array for a 1-column matrix.
     * This is private because it is only intended for use in the constructor.
     *
     * @param data - the 1D data array
     * @return a 2D array holding the column vector
     */
    private static double[][] vectorToColumnMatrix(double[] data) {
        double[][] matData = new double[data.length][1];
        for (int i = 0; i < data.length; i++) {
            matData[i][0] = data[i];
        }
        return matData;
    }
}
