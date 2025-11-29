package comprehensive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

/**
 * This class generates random phrases based on a context-free grammar provided
 * in a file. The generator uses an optimized iterative approach with primitive
 * arrays for maximum performance.
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

    // Maps non-terminal names to unique integer IDs (0, 1, 2...)
    private static Map<String, Integer> nonTerminalMap;

    // Maps terminal strings to unique negative integer IDs (-1, -2, -3...)
    private static ArrayList<String> terminals;
    private static Map<String, Integer> terminalMap;

    // Optimized terminal storage - store as char[][] for fast copying
    private static char[][] terminalChars;

    // Stores the grammar: grammar[nonTerminalID][productionIndex][symbolIndex]
    private static int[][][] grammar;

    // Temporary storage during parsing
    private static ArrayList<ArrayList<int[]>> tempGrammar;

    // Reusable buffer for parsing productions
    private static final int[] productionBuffer = new int[100];

    // SplittableRandom is faster than ThreadLocalRandom for single-threaded use
    private static final SplittableRandom rand = new SplittableRandom();

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

        // Stack for generation
        int[] stack = new int[256];

        // Large output buffer for batched writing (512KB)
        char[] outputBuffer = new char[524288];
        int outputPos = 0;

        // Cache static fields as locals for speed
        final int[][][] g = grammar;
        final char[][] tc = terminalChars;
        final SplittableRandom r = rand;

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out), 524288)) {
            for (int p = 0; p < numPhrases; p++) {
                // Generate phrase directly into output buffer
                int top = 0;
                stack[top++] = startId;

                while (top > 0) {
                    int symbol = stack[--top];

                    if (symbol < 0) {
                        // Terminal - copy chars directly to output buffer
                        char[] chars = tc[~symbol];
                        int len = chars.length;
                        // Check if we need to flush
                        if (outputPos + len + 1 > 524288) {
                            out.write(outputBuffer, 0, outputPos);
                            outputPos = 0;
                        }
                        System.arraycopy(chars, 0, outputBuffer, outputPos, len);
                        outputPos += len;
                    } else {
                        // Non-terminal - push production symbols in reverse order
                        int[][] productions = g[symbol];
                        int[] production = productions[r.nextInt(productions.length)];

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

        // Convert terminals to char[][] for fast copying
        int numTerminals = terminals.size();
        terminalChars = new char[numTerminals][];
        for (int i = 0; i < numTerminals; i++) {
            terminalChars[i] = terminals.get(i).toCharArray();
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
