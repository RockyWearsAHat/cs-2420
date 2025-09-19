package assign02;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains tests for CS2420Class.
 *
 * @author Prof. Parker & Alex Waldmann
 * @version August 31, 2023
 */
public class CS2420ClassTester {

    private CS2420Class emptyClass, verySmallClass, smallClass;

    @BeforeEach
    void setUp() throws Exception {
        emptyClass = new CS2420Class();

        verySmallClass = new CS2420Class();
        verySmallClass.addStudent(new CS2420Student("Jane", "Doe", 1010101, new EmailAddress("hi", "gmail.com")));
        verySmallClass.addStudent(new CS2420Student("Drew", "Hall", 2323232, new EmailAddress("howdy", "gmail.com")));
        verySmallClass.addStudent(new CS2420Student("Riley", "Nguyen", 4545454, new EmailAddress("hello", "gmail.com")));

        smallClass = new CS2420Class();
        smallClass.addAll("./src/main/java/assign02/a_small_2420_class.txt");

        // FILL IN -- Extend this tester to add more tests for the CS 2420 classes above, as well as to
        // create and test larger CS 2420 classes.
        // (HINT: For larger CS 2420 classes, generate random names, uNIDs, contact info, and scores in a 
        // loop, instead of typing one at a time.)
    }

    // Empty CS 2420 class tests --------------------------------------------------------------------------
    @Test
    public void testEmptyLookupUNID() {
        assertNull(emptyClass.lookup(1234567));
    }

    @Test
    public void testEmptyLookupContactInfo() {
        ArrayList<CS2420Student> students = emptyClass.lookup(new EmailAddress("hello", "gmail.com"));
        assertEquals(0, students.size());
    }

    @Test
    public void testEmptyAddScore() {
        // ensure no exceptions thrown
        emptyClass.addScore(1234567, 100, "assignment");
    }

    @Test
    public void testEmptyClassAverage() {
        assertEquals(0, emptyClass.computeClassAverage(), 0);
    }

    @Test
    public void testEmptyContactList() {
        ArrayList<EmailAddress> contactList = emptyClass.getContactList();
        assertEquals(0, contactList.size());
    }

    // Very small CS 2420 class tests --------------------------------------------------------------------
    @Test
    public void testVerySmallLookupUNID() {
        UofUStudent expected = new UofUStudent("Drew", "Hall", 2323232);
        CS2420Student actual = verySmallClass.lookup(2323232);
        assertEquals(expected, actual);
    }

    @Test
    public void testVerySmallLookupContactInfo() {
        UofUStudent expectedStudent = new UofUStudent("Riley", "Nguyen", 4545454);
        ArrayList<CS2420Student> actualStudents = verySmallClass.lookup(new EmailAddress("hello", "gmail.com"));
        assertEquals(1, actualStudents.size());
        assertEquals(expectedStudent, actualStudents.get(0));
    }

    @Test
    public void testVerySmallAddDuplicateStudent() {
        boolean actual = verySmallClass.addStudent(new CS2420Student("Jane", "Doe", 1010101,
                new EmailAddress("hi", "gmail.com")));
        assertFalse(actual);
    }

    @Test
    public void testVerySmallAddNewStudent() {
        boolean actual = verySmallClass.addStudent(new CS2420Student("Jane", "Doe", 1010100,
                new EmailAddress("hi", "gmail.com")));
        assertTrue(actual);
    }

    @Test
    public void testVerySmallStudentFinalScore0() {
        CS2420Student student = verySmallClass.lookup(2323232);
        student.addScore(86.5, "assignment");
        student.addScore(75, "exam");
        student.addScore(89.2, "quiz");
        assertEquals(0, student.computeFinalScore(), 0);
    }

    @Test
    public void testVerySmallStudentFinalGradeNA() {
        CS2420Student student = verySmallClass.lookup(2323232);
        student.addScore(86.5, "assignment");
        student.addScore(75, "exam");
        student.addScore(100, "lab");
        assertEquals("N/A", student.computeFinalGrade());
    }

    @Test
    public void testVerySmallStudentFinalScore() {
        CS2420Student student = verySmallClass.lookup(2323232);
        student.addScore(86.5, "assignment");
        student.addScore(55, "exam");
        student.addScore(90, "lab");
        student.addScore(89.2, "quiz");
        student.addScore(99, "assignment");
        student.addScore(80, "lab");
        student.addScore(77.7, "quiz");
        student.addScore(50, "poll");
        assertEquals(69.5975, student.computeFinalScore(), 0.001);
    }

    @Test
    public void testVerySmallStudentFinalGrade() {
        CS2420Student student = verySmallClass.lookup(2323232);
        student.addScore(86.5, "assignment");
        student.addScore(75, "exam");
        student.addScore(90, "lab");
        student.addScore(89.2, "quiz");
        student.addScore(99, "assignment");
        student.addScore(80, "lab");
        student.addScore(77.7, "quiz");
        student.addScore(55.5, "poll");
        assertEquals("B-", student.computeFinalGrade());
    }

    @Test
    public void testVerySmallStudentComputeScoreTwice() {
        CS2420Student student = verySmallClass.lookup(2323232);
        student.addScore(86.5, "assignment");
        student.addScore(75, "exam");
        student.addScore(90, "lab");
        student.addScore(89.2, "quiz");
        student.addScore(99, "assignment");
        student.addScore(80, "lab");
        student.addScore(77.7, "quiz");
        student.addScore(22.2, "poll");
        student.computeFinalScore();
        student.addScore(70, "lab");
        student.addScore(54.5, "exam");
        assertEquals(73.025, student.computeFinalScore(), 0.001);
    }

    @Test
    public void testVerySmallUpdateName() {
        verySmallClass.lookup(1010101).updateName("John", "Doe");
        ArrayList<CS2420Student> students = verySmallClass.lookup(new EmailAddress("hi", "gmail.com"));
        assertEquals("John", students.get(0).getFirstName());
        assertEquals("Doe", students.get(0).getLastName());
    }

    // Small CS 2420 class tests -------------------------------------------------------------------------
    @Test
    public void testSmallLookupContactInfo() {
        UofUStudent expectedStudent1 = new UofUStudent("Kennedy", "Miller", 888888);
        UofUStudent expectedStudent2 = new UofUStudent("Taylor", "Miller", 999999);

        ArrayList<CS2420Student> actualStudents = smallClass.lookup(new EmailAddress("we_love_puppies", "geemail.com"));
        assertEquals(2, actualStudents.size());
        assertTrue(actualStudents.contains(expectedStudent1));
        assertTrue(actualStudents.contains(expectedStudent2));
    }

    @Test
    public void testSmallGetContactList() {
        ArrayList<EmailAddress> actual = smallClass.getContactList();
        assertEquals(9, actual.size());
    }

    @Test
    public void testSmallStudentFinalScore() {
        CS2420Student student = smallClass.lookup(333333);
        assertEquals(93.917576, student.computeFinalScore(), 0.001);
    }

    @Test
    public void testSmallComputeClassAverage() {
        assertEquals(76.61665, smallClass.computeClassAverage(), 0.001);
    }

    // Additional tests -------------------------------------------------------------------------------
    @Test
    public void testAddDuplicateUNIDDifferentContactInfo() {
        // Attempt to add a student with an existing uNID but different email
        boolean added = verySmallClass.addStudent(new CS2420Student("Riley", "Nguyen", 4545454,
                new EmailAddress("different", "gmail.com")));
        assertFalse(added);
        // Lookup should still return original contact info
        ArrayList<CS2420Student> students = verySmallClass.lookup(new EmailAddress("hello", "gmail.com"));
        assertEquals(1, students.size());
    }

    @Test
    public void testContactListNoDuplicatesAfterAddingDuplicateEmail() {
        int before = verySmallClass.getContactList().size();
        // Add new student with duplicate email of existing student (different uNID)
        verySmallClass.addStudent(new CS2420Student("Alex", "Smith", 9999999, new EmailAddress("hi", "gmail.com")));
        // Contact list size should not increase because email already present
        int after = verySmallClass.getContactList().size();
        assertEquals(before, after);
    }

    @Test
    public void testClassAverageMultipleStudentsWithScores() {
        // Use a fresh class to avoid side-effects from earlier tests adding scores
        CS2420Class tempClass = new CS2420Class();
        tempClass.addStudent(new CS2420Student("Jane", "Doe", 1010101, new EmailAddress("hi", "gmail.com")));
        tempClass.addStudent(new CS2420Student("Drew", "Hall", 2323232, new EmailAddress("howdy", "gmail.com")));

        CS2420Student s1 = tempClass.lookup(1010101);
        CS2420Student s2 = tempClass.lookup(2323232);

        // Provide all categories for s1
        s1.addScore(80, "assignment");
        s1.addScore(90, "exam");
        s1.addScore(100, "lab");
        s1.addScore(70, "quiz");
        s1.addScore(60, "poll");
        // Provide all categories for s2
        s2.addScore(100, "assignment");
        s2.addScore(80, "exam");
        s2.addScore(100, "lab");
        s2.addScore(90, "quiz");
        s2.addScore(80, "poll");

        double s1Final = s1.computeFinalScore(); // 85.2
        double s2Final = s2.computeFinalScore(); // 87.9
        double expectedAvg = (s1Final + s2Final) / 2.0; // 86.55
        assertEquals(expectedAvg, tempClass.computeClassAverage(), 1e-9);
    }

    @Test
    public void testFinalScoreUnchangedAfterReaddingSameScores() {
        CS2420Student s = verySmallClass.lookup(4545454); // Riley
        s.addScore(90, "assignment");
        s.addScore(90, "exam");
        s.addScore(90, "lab");
        s.addScore(90, "quiz");
        s.addScore(90, "poll");
        double initial = s.computeFinalScore();
        // Add another identical set (averages should remain 90 in each category)
        s.addScore(90, "assignment");
        s.addScore(90, "exam");
        s.addScore(90, "lab");
        s.addScore(90, "quiz");
        s.addScore(90, "poll");
        double after = s.computeFinalScore();
        assertEquals(initial, after, 1e-9);
    }
}
