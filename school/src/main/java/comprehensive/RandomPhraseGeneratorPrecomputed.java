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
 * Precomputes all possible phrases, then randomly selects from them. This
 * eliminates stack operations during generation.
 */
public class RandomPhraseGeneratorPrecomputed {

    private static Map<String, Integer> nonTerminalMap;
    private static ArrayList<String> terminals;
    private static Map<String, Integer> terminalMap;
    private static int[][][] grammar;
    private static ArrayList<ArrayList<int[]>> tempGrammar;
    private static final int[] productionBuffer = new int[100];
    private static long randState = System.nanoTime() ^ 0x5DEECE66DL;

    // Precomputed phrases as byte arrays
    private static byte[][] allPhrases;

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

        // Precompute all possible phrases
        int startId = nonTerminalMap.get("<start>");
        ArrayList<String> phrases = expand(startId);

        // Convert to byte arrays
        allPhrases = new byte[phrases.size()][];
        for (int i = 0; i < phrases.size(); i++) {
            allPhrases[i] = (phrases.get(i) + "\n").getBytes(StandardCharsets.UTF_8);
        }

        System.err.println("Precomputed " + allPhrases.length + " unique phrases");

        // Now generate by random selection
        byte[] outputBuffer = new byte[1048576];
        int outputPos = 0;
        final OutputStream out = System.out;
        final int phraseCount = allPhrases.length;
        long rs = randState;

        try {
            for (int p = 0; p < numPhrases; p++) {
                // Inline XorShift random
                rs ^= (rs << 21);
                rs ^= (rs >>> 35);
                rs ^= (rs << 4);
                int idx = (int) (((rs >>> 33) * phraseCount) >>> 31);

                byte[] phrase = allPhrases[idx];
                int len = phrase.length;

                if (outputPos + len > 1048576) {
                    out.write(outputBuffer, 0, outputPos);
                    outputPos = 0;
                }
                System.arraycopy(phrase, 0, outputBuffer, outputPos, len);
                outputPos += len;
            }

            if (outputPos > 0) {
                out.write(outputBuffer, 0, outputPos);
            }
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }
    }

    // Returns all possible expansions of this symbol
    private static ArrayList<String> expand(int symbolId) {
        ArrayList<String> results = new ArrayList<>();

        if (symbolId < 0) {
            // Terminal - single result
            results.add(terminals.get(~symbolId));
            return results;
        }

        // Non-terminal - expand each production
        int[][] productions = grammar[symbolId];
        for (int[] production : productions) {
            // Get all ways to expand this production
            ArrayList<String> current = new ArrayList<>();
            current.add("");

            for (int symbol : production) {
                ArrayList<String> expansions = expand(symbol);
                ArrayList<String> newCurrent = new ArrayList<>();
                for (String prefix : current) {
                    for (String suffix : expansions) {
                        newCurrent.add(prefix + suffix);
                    }
                }
                current = newCurrent;
            }
            results.addAll(current);
        }
        return results;
    }

    @SuppressWarnings("unused")
    private static void generateAllPhrases(int symbolId, StringBuilder current, ArrayList<String> results) {
        // Not used - replaced by expand()
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
            grammar[i] = tempGrammar.get(i).toArray(new int[0][]);
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
