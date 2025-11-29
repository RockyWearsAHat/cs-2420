package comprehensive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    private static final Map<String, Integer> nonTerminalMap = new HashMap<>();

    // Maps terminal strings to unique negative integer IDs (-1, -2, -3...)
    private static final ArrayList<String> terminals = new ArrayList<>();
    private static final Map<String, Integer> terminalMap = new HashMap<>();
    // Optimized terminal storage for generation
    private static String[] terminalArray;

    // Stores the grammar: grammar[nonTerminalID][productionIndex][symbolIndex]
    private static int[][][] grammar;

    // Temporary storage during parsing
    // Optimized to store int[] directly to avoid Integer boxing and extra ArrayList overhead
    private static final ArrayList<ArrayList<int[]>> tempGrammar = new ArrayList<>();

    // Reusable buffer for parsing productions to avoid repeated allocation
    private static int[] productionBuffer = new int[100];

    /**
     * Main entry point for the RandomPhraseGenerator.
     *
     * @param args command line arguments: args[0] is the grammar file path,
     * args[1] is the number of phrases to generate
     */
    public static void main(String[] args) {
        // Reset static state for repeated runs in experiments
        nonTerminalMap.clear();
        terminals.clear();
        terminalMap.clear();
        tempGrammar.clear();
        grammar = null;
        terminalArray = null;

        String grammarFile = args[0];
        int numPhrases = Integer.parseInt(args[1]);

        try {
            parseGrammar(grammarFile);
        } catch (IOException e) {
            System.err.println("Error reading grammar file: " + e.getMessage());
            return;
        }

        // Convert tempGrammar to int[][][] for speed
        finalizeGrammar();

        int startId = nonTerminalMap.get("<start>");

        // Use BufferedWriter for fast I/O
        // Reusing StringBuilder to reduce allocation
        // Initial capacity 256 to avoid resizing for typical phrases
        StringBuilder sb = new StringBuilder(256);

        // Custom stack for generation to avoid allocation in loop
        int[] stack = new int[128];

        try (java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(System.out), 65536)) {
            for (int i = 0; i < numPhrases; i++) {
                sb.setLength(0);
                // Pass stack to avoid allocation
                stack = generateIterative(startId, sb, stack);
                out.write(sb.toString());
                out.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }
    }

    /**
     * Parses the grammar file and populates the temporary grammar structure.
     * Assumes the grammar file is well-formed.
     *
     * @param filePath the path to the grammar file
     * @throws IOException if the file cannot be read
     */
    private static void parseGrammar(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("{")) {
                    String nonTerminalName = br.readLine();
                    if (nonTerminalName == null) {
                        break;
                    }

                    int id = getOrCreateNonTerminalIndex(nonTerminalName);

                    ArrayList<int[]> productions = tempGrammar.get(id);

                    while ((line = br.readLine()) != null && !line.equals("}")) {
                        productions.add(parseProduction(line));
                    }
                }
            }
        }
    }

    /**
     * Converts the temporary ArrayList-based grammar to a primitive 3D array
     * for optimal access speed during generation.
     */
    private static void finalizeGrammar() {
        int numNonTerminals = tempGrammar.size();
        grammar = new int[numNonTerminals][][];

        for (int i = 0; i < numNonTerminals; i++) {
            ArrayList<int[]> productionsList = tempGrammar.get(i);
            // Convert ArrayList<int[]> to int[][]
            grammar[i] = productionsList.toArray(new int[0][]);
        }

        // Convert terminals ArrayList to array for faster access
        terminalArray = terminals.toArray(new String[0]);
    }

    /**
     * Gets the existing ID for a non-terminal, or creates a new one if not
     * found.
     *
     * @param name the non-terminal name (e.g., "&lt;start&gt;")
     * @return the integer ID for this non-terminal
     */
    private static int getOrCreateNonTerminalIndex(String name) {
        if (!nonTerminalMap.containsKey(name)) {
            int id = nonTerminalMap.size();
            nonTerminalMap.put(name, id);
            tempGrammar.add(new ArrayList<>());
        }
        return nonTerminalMap.get(name);
    }

    /**
     * Gets the existing ID for a terminal, or creates a new one if not found.
     * Terminals are stored as negative integers: -1, -2, -3, etc.
     *
     * @param value the terminal string value
     * @return the negative integer ID for this terminal
     */
    private static int getOrCreateTerminalIndex(String value) {
        if (!terminalMap.containsKey(value)) {
            int index = terminals.size();
            terminals.add(value);
            // Store as negative ID: -1, -2, -3...
            // ID = -(index + 1)
            terminalMap.put(value, -(index + 1));
        }
        return terminalMap.get(value);
    }

    /**
     * Parses a single production rule line into an array of symbol IDs.
     * Non-terminals (enclosed in angle brackets) become non-negative IDs,
     * terminals become negative IDs.
     *
     * @param line the production rule line to parse
     * @return an array of symbol IDs representing this production
     */
    private static int[] parseProduction(String line) {
        int count = 0;
        int index = 0;
        int length = line.length();

        while (index < length) {
            int open = line.indexOf('<', index);

            if (open == -1) {
                addSymbolToBuffer(getOrCreateTerminalIndex(line.substring(index)), count++);
                break;
            }

            if (open > index) {
                addSymbolToBuffer(getOrCreateTerminalIndex(line.substring(index, open)), count++);
            }

            int close = line.indexOf('>', open);
            if (close == -1) {
                addSymbolToBuffer(getOrCreateTerminalIndex(line.substring(index)), count++);
                break;
            }

            String nonTerminalName = line.substring(open, close + 1);
            addSymbolToBuffer(getOrCreateNonTerminalIndex(nonTerminalName), count++);
            index = close + 1;
        }
        return Arrays.copyOf(productionBuffer, count);
    }

    /**
     * Adds a symbol to the reusable production buffer, expanding if necessary.
     *
     * @param symbol the symbol ID to add
     * @param index the index in the buffer to add the symbol
     */
    private static void addSymbolToBuffer(int symbol, int index) {
        if (index >= productionBuffer.length) {
            productionBuffer = Arrays.copyOf(productionBuffer, productionBuffer.length * 2);
        }
        productionBuffer[index] = symbol;
    }

    /**
     * Iteratively generates a random phrase starting from the given
     * non-terminal. Uses a primitive int array as a stack to avoid recursion
     * overhead.
     *
     * @param startId the ID of the starting non-terminal
     * @param sb the StringBuilder to append the generated phrase to
     * @param stack a reusable int array used as a stack
     * @return the stack array (may be resized if capacity was exceeded)
     */
    private static int[] generateIterative(int startId, StringBuilder sb, int[] stack) {
        int top = 0;
        stack[top++] = startId;

        while (top > 0) {
            int symbol = stack[--top];

            if (symbol >= 0) {
                // Non-terminal - directly access productions (grammar is assumed valid)
                int[][] productions = grammar[symbol];

                // Use ThreadLocalRandom for better performance than Random (which is synchronized)
                int[] production = productions[java.util.concurrent.ThreadLocalRandom.current().nextInt(productions.length)];

                // Ensure stack capacity
                if (top + production.length > stack.length) {
                    stack = Arrays.copyOf(stack, Math.max(stack.length * 2, top + production.length));
                }

                // Push in reverse order
                for (int i = production.length - 1; i >= 0; i--) {
                    stack[top++] = production[i];
                }
            } else {
                // Terminal
                // Index is -(symbol + 1) -> -symbol - 1
                sb.append(terminalArray[-symbol - 1]);
            }
        }
        return stack;
    }
}
