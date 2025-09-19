package assign01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This tester class assesses the correctness of the Matrix and Vector classes.
 *
 * IMPORTANT NOTE: The tests provided to get you started rely heavily on a
 * correctly implemented equals method. Be careful of false positives (i.e.,
 * tests that pass because your equals method incorrectly returns true).
 *
 * @author Eric Heisler & Alex Waldmann
 * @version May 1, 2023
 */
class MatrixVectorTest {

    private Matrix squareMat, twoTwoMat;
    private Vector threeVec, fiveVec, twoVec;

    @BeforeEach
    void setUp() throws Exception {
        // Creates a 3x3 matrix
        squareMat = new Matrix(new double[][]{{1, 2, 3}, {7, 8, 9}, {4, 5, 6}});
        // Creates a 2x5 matrix
        // twoFiveMat = new Matrix(new double[][]{{-4, 7, 1, 0, 2}, {6, 3, 9, 1, 5}});
        // Creates a 2x2 matrix
        twoTwoMat = new Matrix(new double[][]{{1.7, -22.4}, {2.718, 55}});
        // Creates a 5x5 identity matrix
        // identityMat = new Matrix(new double[][]{{1, 0, 0, 0, 0}, {0, 1, 0, 0, 0}, {0, 0, 1, 0, 0}, {0, 0, 0, 1, 0}, {0, 0, 0, 0, 1}});

        // Creates vectors
        threeVec = new Vector(new double[]{2.1, 4.7, -0.8});
        fiveVec = new Vector(new double[]{10, 1.5, 200, 17.2, 3.14});
        twoVec = new Vector(new double[]{1000, 1});
    }

    @Test
    void matrixEquality() {
        assertTrue(squareMat.equals(new Matrix(new double[][]{{1, 2, 3}, {7, 8, 9}, {4, 5, 6}})));
    }

    @Test
    void matrixInequalityDifferentValues() {
        assertFalse(squareMat.equals(new Matrix(new double[][]{{1, 2, 3}, {7, 5, 9}, {4, 5, 6}})));
    }

    @Test
    void vectorInequalityDifferentSizes() {
        assertFalse(threeVec.equals(fiveVec));
    }

    @Test
    public void createMatrixFromBadArray() {
        // double arr[][] = {{1, 2}, {3}};
        // assertThrows(IllegalArgumentException.class, () -> {
        //     new Matrix(arr);
        // });
        // NOTE: The code above is an example of a lambda expression.
    }

    @Test
    void matrixToString() {
        String resultStr = "1.7 -22.4\n2.718 55.0";
        assertEquals(resultStr, twoTwoMat.toString());
    }

    @Test
    void matrixAdd() {
        assertTrue((twoTwoMat.add(twoTwoMat)).equals(new Matrix(new double[][]{{1.7 * 2, -22.4 * 2}, {2.718 * 2, 55 * 2}})));
    }

    @Test
    void matrixMultiply() {
        double product[][] = {{1.7 * 1.7 - 22.4 * 2.718, -22.4 * 1.7 - 22.4 * 55}, {2.718 * 1.7 + 55 * 2.718, -22.4 * 2.718 + 55 * 55}};
        assertTrue((twoTwoMat.multiply(twoTwoMat)).equals(new Matrix(product)));
    }

    @Test
    void vectorDotProduct() {
        double dotProdResult = threeVec.dotProduct(threeVec);
        assertEquals(2.1 * 2.1 + 4.7 * 4.7 + (-0.8) * (-0.8), dotProdResult, (2.1 * 2.1 + 4.7 * 4.7 + (-0.8) * (-0.8)) * 1e-14);
    }

    @Test
    void vectorMagnitude() {
        double mag = twoVec.magnitude();
        assertEquals(Math.sqrt(1000001), mag, Math.sqrt(1000001) * 1e-14);
    }

    // STUDENT: Add more unit tests to completely test both classes.
    // Recall that the methods from Matrix are also inherited by Vector.
    // For testing exceptions, use the pattern from createMatrixFromBadArray() above:
    //   assertThrows(IllegalArgumentException.class, () -> { [statement]; });
    // replacing [statement] with the code you expect to throw an exception
}
