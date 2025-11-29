package comprehensive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Profiling version to understand where time is spent.
 */
public class RandomPhraseGeneratorProfiled {

    private static Map<String, Integer> nonTerminalMap;
    private static ArrayList<String> terminals;
    private static Map<String, Integer> terminalMap;
    private static byte[][] terminalBytes;
    private static int[][][] grammar;
    private static ArrayList<ArrayList<int[]>> tempGrammar;
    private static final int[] productionBuffer = new int[100];
    private static long randState = System.nanoTime() ^ 0x5DEECE66DL;

    public static void main(String[] args) {
        nonTerminalMap = new HashMap<>();
        terminals = new ArrayList<>();
        terminalMap = new HashMap<>();
        tempGrammar = new ArrayList<>();
        randState = System.nanoTime() ^ 0x5DEECE66DL;

        String grammarFile = args[0];
        int numPhrases = Integer.parseInt(args[1]);

        try {
            parseGrammar(grammarFile);
        } catch (IOException e) {
            System.err.println("Error reading grammar file: " + e.getMessage());
            return;
        }

        finalizeGrammar();

        int startId = nonTerminalMap.get("<start>");
        int[] stack = new int[256];
        byte[] outputBuffer = new byte[1048576];
        int outputPos = 0;

        final int[][][] g = grammar;
        final byte[][] tb = terminalBytes;
        final OutputStream out = System.out;

        // Counters for profiling
        long terminalHits = 0;
        long nonTerminalHits = 0;
        long stackOps = 0;
        long randomCalls = 0;
        long arrayCopies = 0;
        long flushes = 0;

        try {
            for (int p = 0; p < numPhrases; p++) {
                int top = 0;
                stack[top++] = startId;
                stackOps++;

                while (top > 0) {
                    int symbol = stack[--top];
                    stackOps++;

                    if (symbol < 0) {
                        terminalHits++;
                        byte[] bytes = tb[~symbol];
                        int len = bytes.length;
                        if (outputPos + len + 1 > 1048576) {
                            out.write(outputBuffer, 0, outputPos);
                            outputPos = 0;
                            flushes++;
                        }
                        System.arraycopy(bytes, 0, outputBuffer, outputPos, len);
                        arrayCopies++;
                        outputPos += len;
                    } else {
                        nonTerminalHits++;
                        int[][] productions = g[symbol];
                        randomCalls++;
                        int[] production = productions[nextInt(productions.length)];

                        for (int i = production.length - 1; i >= 0; i--) {
                            stack[top++] = production[i];
                            stackOps++;
                        }
                    }
                }
                outputBuffer[outputPos++] = '\n';
            }

            if (outputPos > 0) {
                out.write(outputBuffer, 0, outputPos);
                flushes++;
            }
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }

        System.err.println("\n=== PROFILE ===");
        System.err.println("Phrases: " + numPhrases);
        System.err.println("Terminal hits: " + terminalHits + " (" + (terminalHits / numPhrases) + " per phrase)");
        System.err.println("Non-terminal hits: " + nonTerminalHits + " (" + (nonTerminalHits / numPhrases) + " per phrase)");
        System.err.println("Stack operations: " + stackOps + " (" + (stackOps / numPhrases) + " per phrase)");
        System.err.println("Random calls: " + randomCalls + " (" + (randomCalls / numPhrases) + " per phrase)");
        System.err.println("Array copies: " + arrayCopies + " (" + (arrayCopies / numPhrases) + " per phrase)");
        System.err.println("Buffer flushes: " + flushes);
    }

    private static int nextInt(int bound) {
        long x = randState;
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        randState = x;
        return (int) (((x >>> 33) * bound) >>> 31);
    }

    private static void parseGrammar(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath), 8192)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0 && line.charAt(0) == '{') {
                    String nonTerminalName = br.readLine();
                    if (nonTerminalName == null) {
                        break;
                    }
                    int id = getOrCreateNonTerminalIndex(nonTerminalName);
                    ArrayList<int[]> productions = tempGrammar.get(id);
                    while ((line = br.readLine()) != null && (line.length() == 0 || line.charAt(0) != '}')) {
                        if (line.length() > 0) {
                            productions.add(parseProduction(line));
                        }
                    }
                }
            }
        }
    }

    private static void finalizeGrammar() {
        int numNonTerminals = tempGrammar.size();
        grammar = new int[numNonTerminals][][];
        for (int i = 0; i < numNonTerminals; i++) {
            ArrayList<int[]> productionsList = tempGrammar.get(i);
            grammar[i] = productionsList.toArray(new int[0][]);
        }
        int numTerminals = terminals.size();
        terminalBytes = new byte[numTerminals][];
        for (int i = 0; i < numTerminals; i++) {
            terminalBytes[i] = terminals.get(i).getBytes(StandardCharsets.UTF_8);
        }
    }

    private static int getOrCreateNonTerminalIndex(String name) {
        Integer existing = nonTerminalMap.get(name);
        if (existing != null) {
            return existing;
        }
        int id = nonTerminalMap.size();
        nonTerminalMap.put(name, id);
        tempGrammar.add(new ArrayList<>());
        return id;
    }

    private static int getOrCreateTerminalIndex(String value) {
        Integer existing = terminalMap.get(value);
        if (existing != null) {
            return existing;
        }
        int index = terminals.size();
        terminals.add(value);
        int id = ~index;
        terminalMap.put(value, id);
        return id;
    }

    private static int[] parseProduction(String line) {
        int count = 0;
        int index = 0;
        int length = line.length();
        while (index < length) {
            int open = line.indexOf('<', index);
            if (open == -1) {
                if (index < length) {
                    productionBuffer[count++] = getOrCreateTerminalIndex(line.substring(index));
                }
                break;
            }
            if (open > index) {
                productionBuffer[count++] = getOrCreateTerminalIndex(line.substring(index, open));
            }
            int close = line.indexOf('>', open);
            productionBuffer[count++] = getOrCreateNonTerminalIndex(line.substring(open, close + 1));
            index = close + 1;
        }
        int[] result = new int[count];
        System.arraycopy(productionBuffer, 0, result, 0, count);
        return result;
    }
}
