package lab03;

/**
 * This class contains code for completing the Lab 3: Debugging activities.
 *
 * @author CS 2420 course staff
 * @version January 28, 2022
 */
public class Part3 {

    public static boolean checkPassword(String password) {
        String[] tokens = password.split(" ");
        int a = 0, b = 1;
        for (int i = 0; i < tokens.length; i++) {
            a += b; // 1, 4, 9, 16, 25
            int currentToken = Integer.parseInt(tokens[i]);
            if (a != currentToken) {
                return false;
            }
            b += 2; // 3, 5, 7, 9, 11
        }
        return b == 11;
    }
}
