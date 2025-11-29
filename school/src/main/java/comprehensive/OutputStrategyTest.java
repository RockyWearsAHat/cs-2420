package comprehensive;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Tests different output strategies to find the fastest approach.
 */
public class OutputStrategyTest {

    public static void main(String[] args) throws Exception {
        PrintStream originalOut = System.out;
        PrintStream nullOut = new PrintStream(new OutputStream() {
            public void write(int b) {
            }

            public void write(byte[] b, int off, int len) {
            }
        });

        int numPhrases = 10_000_000;
        String grammarFile = "src/main/java/comprehensive/poetic_sentence.g";

        // Warmup
        System.setOut(nullOut);
        for (int i = 0; i < 5; i++) {
            RandomPhraseGenerator.main(new String[]{grammarFile, "1000"});
            RandomPhraseGeneratorPrecomputed.main(new String[]{grammarFile, "1000"});
        }

        System.setOut(originalOut);
        System.out.println("Testing approaches for " + numPhrases + " phrases...\n");

        // Test current approach
        long[] byteTimes = new long[10];
        for (int i = 0; i < 10; i++) {
            System.gc();
            System.setOut(nullOut);
            long start = System.nanoTime();
            RandomPhraseGenerator.main(new String[]{grammarFile, String.valueOf(numPhrases)});
            byteTimes[i] = (System.nanoTime() - start) / 1_000_000;
            System.setOut(originalOut);
            System.out.println("Stack-based run " + i + ": " + byteTimes[i] + " ms");
        }

        long byteAvg = 0;
        for (long t : byteTimes) {
            byteAvg += t;
        }
        byteAvg /= byteTimes.length;
        System.out.println("Stack-based average: " + byteAvg + " ms\n");

        // Test precomputed approach  
        long[] preTimes = new long[10];
        for (int i = 0; i < 10; i++) {
            System.gc();
            System.setOut(nullOut);
            long start = System.nanoTime();
            RandomPhraseGeneratorPrecomputed.main(new String[]{grammarFile, String.valueOf(numPhrases)});
            preTimes[i] = (System.nanoTime() - start) / 1_000_000;
            System.setOut(originalOut);
            System.out.println("Precomputed run " + i + ": " + preTimes[i] + " ms");
        }

        long preAvg = 0;
        for (long t : preTimes) {
            preAvg += t;
        }
        preAvg /= preTimes.length;
        System.out.println("Precomputed average: " + preAvg + " ms\n");

        System.out.println("Winner: " + (byteAvg < preAvg ? "Stack-based" : "Precomputed")
                + " by " + Math.abs(byteAvg - preAvg) + " ms");
    }
}
