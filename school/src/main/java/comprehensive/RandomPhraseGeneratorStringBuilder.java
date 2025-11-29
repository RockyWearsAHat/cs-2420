package comprehensive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * StringBuilder version - builds entire output as one string, single print.
 */
public class RandomPhraseGeneratorStringBuilder {

    private static Map<String, Integer> nonTerminalMap;
    private static ArrayList<String> terminals;
    private static Map<String, Integer> terminalMap;
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

        // Use char[] buffer instead of StringBuilder for speed
        final int[][][] g = grammar;
        final String[] t = terminals.toArray(new String[0]);

        // Estimate ~100 chars per phrase on average, pre-allocate
        char[] buffer = new char[numPhrases * 100];
        int bufPos = 0;

        for (int p = 0; p < numPhrases; p++) {
            int top = 0;
            stack[top++] = startId;

            while (top > 0) {
                int symbol = stack[--top];

                if (symbol < 0) {
                    // Terminal - append to buffer
                    String term = t[~symbol];
                    int len = term.length();
                    // Grow buffer if needed
                    if (bufPos + len + 1 >= buffer.length) {
                        char[] newBuf = new char[buffer.length * 2];
                        System.arraycopy(buffer, 0, newBuf, 0, bufPos);
                        buffer = newBuf;
                    }
                    term.getChars(0, len, buffer, bufPos);
                    bufPos += len;
                } else {
                    int[][] productions = g[symbol];
                    int[] production = productions[nextInt(productions.length)];

                    switch (production.length) {
                        case 5:
                            stack[top++] = production[4];
                        case 4:
                            stack[top++] = production[3];
                        case 3:
                            stack[top++] = production[2];
                        case 2:
                            stack[top++] = production[1];
                        case 1:
                            stack[top++] = production[0];
                            break;
                        default:
                            for (int i = production.length - 1; i >= 0; i--) {
                                stack[top++] = production[i];
                            }
                    }
                }
            }
            buffer[bufPos++] = '\n';
        }

        // Single print
        System.out.print(new String(buffer, 0, bufPos));
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
