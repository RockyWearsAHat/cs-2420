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
 * This class generates random phrases based on a context-free grammar provided
 * in a file. The generator uses a hybrid approach:
 * <ul>
 * <li>For small grammars: precomputes all possible phrases for O(1)
 * generation</li>
 * <li>For large grammars: uses optimized stack-based generation</li>
 * </ul>
 *
 * <p>
 * The grammar is stored as a 3D primitive int array where:
 * <ul>
 * <li>Non-terminals are represented as non-negative integers (0, 1, 2...)</li>
 * <li>Terminals are represented as negative integers (-1, -2, -3...)</li>
 * </ul>
 *
 * <p>
 * Usage: java comprehensive.RandomPhraseGenerator &lt;grammar_file&gt;
 * &lt;number_of_phrases&gt;
 *
 * @author Alex Waldmann
 * @author Tyler Gagliardi
 * @version November 28, 2025
 */
public class RandomPhraseGenerator {

    // Maximum number of precomputed phrases before falling back to stack-based
    private static final int MAX_PRECOMPUTED_PHRASES = 100000;

    // Maps non-terminal names to unique integer IDs (0, 1, 2...)
    private static Map<String, Integer> nonTerminalMap;

    // Maps terminal strings to unique negative integer IDs (-1, -2, -3...)
    private static ArrayList<String> terminals;
    private static Map<String, Integer> terminalMap;

    // Optimized terminal storage - store as byte[][] for direct output
    private static byte[][] terminalBytes;

    // Precomputed phrases (if grammar is small enough)
    private static byte[][] precomputedPhrases;

    // Stores the grammar: grammar[nonTerminalID][productionIndex][symbolIndex]
    private static int[][][] grammar;

    // Temporary storage during parsing
    private static ArrayList<ArrayList<int[]>> tempGrammar;

    // Reusable buffer for parsing productions
    private static final int[] productionBuffer = new int[100];

    // XorShift random state
    private static long randState = System.nanoTime() ^ 0x5DEECE66DL;

    /**
     * Main entry point for the RandomPhraseGenerator.
     *
     * @param args command line arguments: args[0] is the grammar file path,
     * args[1] is the number of phrases to generate
     */
    public static void main(String[] args) {
        // Reset/reinitialize static state for repeated runs
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

        // Try to precompute all phrases (much faster for small grammars)
        ArrayList<String> allPhrases = tryPrecompute(startId);

        if (allPhrases != null) {
            // Use precomputed phrases - O(1) per phrase!
            generateFromPrecomputed(allPhrases, numPhrases);
        } else {
            // Fall back to stack-based generation
            generateStackBased(startId, numPhrases);
        }
    }

    /**
     * Tries to precompute all possible phrases. Returns null if grammar is too
     * large.
     */
    private static ArrayList<String> tryPrecompute(int startId) {
        try {
            ArrayList<String> phrases = expand(startId, 0);
            if (phrases.size() <= MAX_PRECOMPUTED_PHRASES) {
                return phrases;
            }
        } catch (RuntimeException e) {
            // Grammar too large, fall through
        }
        return null;
    }

    /**
     * Recursively expands a symbol into all possible strings. Throws if result
     * would be too large.
     */
    private static ArrayList<String> expand(int symbolId, int depth) {
        if (depth > 50) {
            throw new RuntimeException("Grammar too deep");
        }

        ArrayList<String> results = new ArrayList<>();

        if (symbolId < 0) {
            results.add(terminals.get(~symbolId));
            return results;
        }

        int[][] productions = grammar[symbolId];
        for (int[] production : productions) {
            ArrayList<String> current = new ArrayList<>();
            current.add("");

            for (int symbol : production) {
                ArrayList<String> expansions = expand(symbol, depth + 1);
                ArrayList<String> newCurrent = new ArrayList<>();
                for (String prefix : current) {
                    for (String suffix : expansions) {
                        newCurrent.add(prefix + suffix);
                        if (newCurrent.size() > MAX_PRECOMPUTED_PHRASES) {
                            throw new RuntimeException("Too many phrases");
                        }
                    }
                }
                current = newCurrent;
            }
            results.addAll(current);
            if (results.size() > MAX_PRECOMPUTED_PHRASES) {
                throw new RuntimeException("Too many phrases");
            }
        }
        return results;
    }

    /**
     * Generates phrases from precomputed list - extremely fast.
     */
    private static void generateFromPrecomputed(ArrayList<String> phrases, int numPhrases) {
        // Convert to byte arrays with newlines
        int phraseCount = phrases.size();
        precomputedPhrases = new byte[phraseCount][];
        for (int i = 0; i < phraseCount; i++) {
            precomputedPhrases[i] = (phrases.get(i) + "\n").getBytes(StandardCharsets.UTF_8);
        }

        byte[] outputBuffer = new byte[1048576];
        int outputPos = 0;
        final OutputStream out = System.out;
        long rs = randState;

        try {
            for (int p = 0; p < numPhrases; p++) {
                rs ^= (rs << 21);
                rs ^= (rs >>> 35);
                rs ^= (rs << 4);
                int idx = (int) (((rs >>> 33) * phraseCount) >>> 31);

                byte[] phrase = precomputedPhrases[idx];
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
            randState = rs;
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }
    }

    /**
     * Stack-based generation for large grammars.
     */
    private static void generateStackBased(int startId, int numPhrases) {
        int[] stack = new int[256];

        // Large output buffer (1MB bytes)
        byte[] outputBuffer = new byte[1048576];
        int outputPos = 0;

        // Cache static fields as locals for speed
        final int[][][] g = grammar;
        final byte[][] tb = terminalBytes;
        final OutputStream out = System.out;

        // Inline random state as local
        long rs = randState;

        try {
            for (int p = 0; p < numPhrases; p++) {
                // Generate phrase directly into output buffer
                int top = 0;
                stack[top++] = startId;

                while (top > 0) {
                    int symbol = stack[--top];

                    if (symbol < 0) {
                        // Terminal - copy bytes directly to output buffer
                        byte[] bytes = tb[~symbol];
                        int len = bytes.length;
                        // Check if we need to flush
                        if (outputPos + len + 1 > 1048576) {
                            out.write(outputBuffer, 0, outputPos);
                            outputPos = 0;
                        }
                        System.arraycopy(bytes, 0, outputBuffer, outputPos, len);
                        outputPos += len;
                    } else {
                        // Non-terminal - push production symbols in reverse order
                        int[][] productions = g[symbol];

                        // Inline XorShift random
                        rs ^= (rs << 21);
                        rs ^= (rs >>> 35);
                        rs ^= (rs << 4);
                        int idx = (int) (((rs >>> 33) * productions.length) >>> 31);

                        int[] production = productions[idx];

                        // Unroll for common production lengths
                        switch (production.length) {
                            case 5:
                                stack[top++] = production[4]; // fall through
                            case 4:
                                stack[top++] = production[3]; // fall through
                            case 3:
                                stack[top++] = production[2]; // fall through
                            case 2:
                                stack[top++] = production[1]; // fall through
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

                // Add newline
                outputBuffer[outputPos++] = '\n';
            }

            // Save random state back
            randState = rs;

            // Flush remaining
            if (outputPos > 0) {
                out.write(outputBuffer, 0, outputPos);
            }
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }
    }

    /**
     * Parses the grammar file and populates the temporary grammar structure.
     *
     * @param filePath the path to the grammar file
     * @throws IOException if the file cannot be read
     */
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

    /**
     * Converts the temporary grammar to optimized primitive arrays.
     */
    private static void finalizeGrammar() {
        int numNonTerminals = tempGrammar.size();
        grammar = new int[numNonTerminals][][];

        for (int i = 0; i < numNonTerminals; i++) {
            ArrayList<int[]> productionsList = tempGrammar.get(i);
            int size = productionsList.size();
            grammar[i] = new int[size][];
            for (int j = 0; j < size; j++) {
                grammar[i][j] = productionsList.get(j);
            }
        }

        // Convert terminals to byte[][] for direct output (UTF-8)
        int numTerminals = terminals.size();
        terminalBytes = new byte[numTerminals][];
        for (int i = 0; i < numTerminals; i++) {
            terminalBytes[i] = terminals.get(i).getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
     * Gets or creates an index for a non-terminal.
     */
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

    /**
     * Gets or creates an index for a terminal.
     */
    private static int getOrCreateTerminalIndex(String value) {
        Integer existing = terminalMap.get(value);
        if (existing != null) {
            return existing;
        }

        int index = terminals.size();
        terminals.add(value);
        int id = ~index;  // Bitwise NOT: index 0 -> -1, index 1 -> -2, etc.
        terminalMap.put(value, id);
        return id;
    }

    /**
     * Parses a production line into symbol IDs.
     */
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
            String nonTerminalName = line.substring(open, close + 1);
            productionBuffer[count++] = getOrCreateNonTerminalIndex(nonTerminalName);
            index = close + 1;
        }

        int[] result = new int[count];
        System.arraycopy(productionBuffer, 0, result, 0, count);
        return result;
    }
}
