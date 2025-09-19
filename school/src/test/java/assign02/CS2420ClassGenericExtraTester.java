package assign02;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Additional tests for the generic CS2420ClassGeneric to exercise generic
 * contact types (String, MailingAddress, PhoneNumber, Integer) and behavior
 * edge cases.
 */
public class CS2420ClassGenericExtraTester {

    private CS2420ClassGeneric<String> stringClass; // using simple String contact info
    private CS2420ClassGeneric<PhoneNumber> phoneClass; // phone numbers

    @BeforeEach
    void setUp() {
        stringClass = new CS2420ClassGeneric<>();
        stringClass.addStudent(new CS2420StudentGeneric<>("A", "A", 1, "alpha"));
        stringClass.addStudent(new CS2420StudentGeneric<>("B", "B", 2, "beta"));
        stringClass.addStudent(new CS2420StudentGeneric<>("C", "C", 3, "alpha")); // duplicate contact

        phoneClass = new CS2420ClassGeneric<>();
        phoneClass.addStudent(new CS2420StudentGeneric<>("P", "One", 10, new PhoneNumber("801-555-1234")));
        phoneClass.addStudent(new CS2420StudentGeneric<>("P", "Two", 11, new PhoneNumber("801-555-5678")));
    }

    @Test
    public void testLookupStringContactMultipleMatches() {
        ArrayList<CS2420StudentGeneric<String>> results = stringClass.lookup("alpha");
        assertEquals(2, results.size());
        assertTrue(results.contains(new CS2420StudentGeneric<>("A", "A", 1, "alpha")));
        assertTrue(results.contains(new CS2420StudentGeneric<>("C", "C", 3, "alpha")));
    }

    @Test
    public void testLookupStringContactNoMatch() {
        assertEquals(0, stringClass.lookup("gamma").size());
    }

    @Test
    public void testContactListEliminatesDuplicatesGeneric() {
        ArrayList<String> contacts = stringClass.getContactList();
        // Expect only two unique strings: alpha, beta
        assertEquals(2, contacts.size());
        assertTrue(contacts.contains("alpha"));
        assertTrue(contacts.contains("beta"));
    }

    @Test
    public void testAddScoreInfluencesFinalScoreGeneric() {
        CS2420StudentGeneric<String> s = stringClass.lookup(1);
        s.addScore(100, "assignment");
        s.addScore(100, "exam");
        s.addScore(100, "lab");
        s.addScore(100, "quiz");
        s.addScore(100, "poll");
        assertEquals(100.0, s.computeFinalScore(), 1e-9);
        assertEquals("A", s.computeFinalGrade());
    }

    @Test
    public void testPartialCategoriesYieldNA() {
        CS2420StudentGeneric<String> s = stringClass.lookup(2);
        s.addScore(90, "assignment");
        s.addScore(80, "exam");
        // missing lab/quiz/poll -> finalScore returns 0 and grade N/A per implementation
        assertEquals(0.0, s.computeFinalScore(), 1e-9);
        assertEquals("N/A", s.computeFinalGrade());
    }

    @Test
    public void testPhoneNumberLookupSingle() {
        ArrayList<CS2420StudentGeneric<PhoneNumber>> results = phoneClass.lookup(new PhoneNumber("801-555-1234"));
        assertEquals(1, results.size());
        assertEquals(new CS2420StudentGeneric<>("P", "One", 10, new PhoneNumber("801-555-1234")), results.get(0));
    }

    @Test
    public void testComputeClassAverageGeneric() {
        // Provide all categories for both phoneClass students
        phoneClass.addScore(10, 80, "assignment");
        phoneClass.addScore(10, 90, "exam");
        phoneClass.addScore(10, 100, "lab");
        phoneClass.addScore(10, 70, "quiz");
        phoneClass.addScore(10, 60, "poll");

        phoneClass.addScore(11, 100, "assignment");
        phoneClass.addScore(11, 80, "exam");
        phoneClass.addScore(11, 90, "lab");
        phoneClass.addScore(11, 90, "quiz");
        phoneClass.addScore(11, 80, "poll");

        double s1 = phoneClass.lookup(10).computeFinalScore();
        double s2 = phoneClass.lookup(11).computeFinalScore();
        double expected = (s1 + s2) / 2.0;
        assertEquals(expected, phoneClass.computeClassAverage(), 1e-9);
    }

    @Test
    public void testLookupNullContactReturnsEmptyList() {
        ArrayList<CS2420StudentGeneric<String>> nullLookup = stringClass.lookup((String) null);
        assertNotNull(nullLookup);
        assertTrue(nullLookup.isEmpty());
    }
}
