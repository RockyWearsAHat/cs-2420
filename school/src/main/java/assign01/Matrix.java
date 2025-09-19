package assign01;

/**
 * This class represents a matrix of numbers. It has a set number of rows and
 * columns. A vector is a special case in which the number of columns or rows in
 * one. (A column vector has one column. A row vector has one row).
 *
 * @author Eric Heisler & Alex Waldmann
 * @version May 1, 2023
 */
public class Matrix {

    // 2D array to hold the numbers of the matrix
    protected double[][] data;
    // The number of columns and rows
    protected int columns;
    protected int rows;

    /**
     * Creates a new matrix by copying in a 2D array of numbers. Input data is a
     * row-major 2D array. That means the top left corner is data[0][0], and the
     * number to its right is data[0][1], while the number below it is
     * data[1][0].
     *
     * @param data - a 2D array holding the numbers of the matrix
     * @throws IllegalArgumentException if the number of rows or columns is
     * zero, or if not all rows have the same length.
     */
    public Matrix(double[][] data) {
        // Check number of rows
        if (data.length == 0) {
            throw new IllegalArgumentException("Number of rows must be positive.");
        }
        rows = data.length;

        // Check number of columns
        columns = data[0].length;
        for (int i = 0; i < rows; i++) {
            if (data[i].length == 0) {
                throw new IllegalArgumentException("Number of columns must be positive.");
            }
            if (data[i].length != columns) {
                throw new IllegalArgumentException("Number of columns must be equal for all rows.");
            }
        }

        // STUDENT: Add code to allocate a new array of the right size
        //          and fill it with copies of the data from the parameter.
        //          Do not set the instance variable equal to the parameter array.
        //Create the space for new array and copy data. 
        //Thanks VS code for recommending arraycopy
        this.data = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, columns);
        }
    }

    /**
     * Determines whether this matrix is "equal to" another matrix, where
     * equality is defined as both having the same number of rows, same number
     * of columns, and containing the same numbers in the same positions.
     *
     * @param other - another Matrix to compare
     * @return true if this Matrix is equal to the other
     */
    @Override //Override default equals method
    public boolean equals(Object other) {
        if (!(other instanceof Matrix)) {
            return false;
        }

        // STUDENT: Fill in remaining code to determine equality of two 
        //          matrix objects, do not just return false.
        //If same object, equals true
        if (this == other) {
            return true;
        }

        //If not same object type not equal
        if (getClass() != other.getClass()) {
            return false;
        }

        //Cast to matrix
        Matrix m = (Matrix) other;

        //Check if dimensions are equal
        if (this.rows != m.rows || this.columns != m.columns) {
            return false;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Bitwise equality: NaNs compare equal; +0.0 equals -0.0
                if (Double.doubleToLongBits(this.data[i][j])
                        != Double.doubleToLongBits(m.data[i][j])) {
                    // If not equal, return false
                    return false;
                }
            }
        }

        //If all above checks pass, the matrices are equal
        return true;
    }

    //Hashcode to remove compiler error/hint
    @Override
    public int hashCode() {
        //Honestly I don't even know what this does apparently it's
        //to classify objects and it's what chat reccomended to me when
        //I told it the compiler error
        int result = 17;
        result = 31 * result + rows;
        result = 31 * result + columns;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                long bits = Double.doubleToLongBits(data[i][j]);
                result = 31 * result + (int) (bits ^ (bits >>> 32));
            }
        }
        return result;
    }

    /**
     * Returns a new matrix that is the sum of this and another matrix.
     *
     * @param other - another matrix to be added to this matrix
     * @return a new matrix that is the sum of this and other
     * @throws IllegalArgumentException if this and the other matrix do not have
     * the same number of rows or columns.
     */
    public Matrix add(Matrix other) {
        // STUDENT: Fill in with code to fulfill the method contract above.
        //          Do not return null.
        //Null/type checks
        if (other == null || !(other instanceof Matrix)) {
            throw new IllegalArgumentException("Incompatible matrix types.");
        }

        //Check for size differences
        if (rows != other.rows || columns != other.columns) {
            throw new IllegalArgumentException("Incompatible matrix dimensions.");
        }

        //Create memory to write result matrix to
        double[][] result = new double[rows][columns];

        //Loop through and add all entries
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = this.data[i][j] + other.data[i][j];
            }
        }

        //Return the result matrix by converting the result array
        //a matrix object
        return new Matrix(result);
    }

    /**
     * Returns the product of this and another matrix. The order of
     * multiplication is (this * other).
     *
     * @param other - another matrix to multiply this one
     * @return a new matrix that is the product of this and other
     * @throws IllegalArgumentException if the number of rows in the other
     * matrix does not equal the number of columns in this.
     */
    public Matrix multiply(Matrix other) {
        // STUDENT: Fill in with code to fulfill the method contract above.
        //          Do not return null.
        //Null/type check
        if (other == null || !(other instanceof Matrix)) {
            throw new IllegalArgumentException("Incompatible matrix types.");
        }

        //Size check - number of columns in this must equal number of rows in other
        if (this.columns != other.rows) {
            throw new IllegalArgumentException("Incompatible matrix dimensions.");
        }

        //Create array to hold product - result is this.rows × other.columns
        double[][] productArr = new double[this.rows][other.columns];

        //Product calculated row * column, so loop whole array 3 times
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.columns; j++) {
                for (int k = 0; k < this.columns; k++) {
                    productArr[i][j] += this.data[i][k] * other.data[k][j];
                }
            }
        }

        //Return the product matrix by converting the product array to a matrix object
        return new Matrix(productArr);
    }

    /**
     * Generates and returns a text representation of this matrix. For example,
     * this is a 3-row, 2-column matrix: "1.0 2.0 2.0 4.0 3.0 6.0"
     *
     * Each line ends with a newline "\n" and no space after the last number.
     * Notice there is no newline or space at the end of the string.
     *
     * @return a string representation of this matrix
     */
    @Override //Mark as override for the compiler warning
    public String toString() {
        // STUDENT: Fill in with code to fulfill the method contract above.
        //          Do not return null.

        //Create StringBuilder for efficient string concatenation
        StringBuilder sb = new StringBuilder();

        //Loop
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //Append the current number to the string
                sb.append(data[i][j]);
                //If not end of row add a space
                if (j < columns - 1) {
                    sb.append(" ");
                }
            }
            //After the end of the row, add a newline
            sb.append("\n");
        }

        //Convert StringBuilder to string and remove trailing whitespace/newlines
        return sb.toString().trim();
    }
}

//Passes all written tests, matrix multiplication was kinda confusing 
//until I looked it up
