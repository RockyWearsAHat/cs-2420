package comprehensive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates random phrases based on a context-free grammar.
 * 
 * Uses a hybrid approach:
 * - For small grammars: precomputes all possible phrases for O(1) generation
 * - For large grammars: uses stack-based generation
 *
 * @author Alex Waldmann
 * @author Tyler Gagliardi
 * @version November 28, 2025
 */
public class RandomPhraseGenerator {

    // Maximum phrases to precompute before falling back to stack-based
    private static final int MAX_PRECOMPUTED = 100000;
    private static final byte NEWLINE = '\n';

    // Grammar storage: grammar[nonTerminalId][productionIndex][symbolIndex]
    // Non-terminals: >= 0, Terminals: < 0 (use ~id to get terminal index)
    private static int[][][] grammar;
    private static byte[][] terminals;  // Pre-encoded as bytes
    private static int startSymbol;
    
    // Precomputed phrases as bytes (if grammar is small enough)
    private static byte[][] precomputedPhrases;
    private static int phraseMask; // For fast modulo with power of 2
    
    // Output stream (for benchmarking)
    private static OutputStream outputStream;

    /**
     * Sets a custom output stream for benchmarking.
     * @param out the output stream to use
     */
    public static void setOutputStream(OutputStream out) {
        outputStream = out;
    }

    /**
     * Main entry point.
     * @param args args[0] = grammar file, args[1] = number of phrases
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java RandomPhraseGenerator <grammar_file> <num_phrases>");
            return;
        }

        String grammarFile = args[0];
        int numPhrases = Integer.parseInt(args[1]);

        try {
            parseGrammar(grammarFile);
        } catch (IOException e) {
            System.err.println("Error reading grammar: " + e.getMessage());
            return;
        }

        // Use custom output stream if set, otherwise System.out
        OutputStream out = (outputStream != null) ? outputStream : System.out;

        try {
            if (precomputedPhrases != null) {
                generateFromPrecomputed(out, numPhrases);
            } else {
                generateWithStack(out, numPhrases);
            }
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }
    }

    /**
     * Fast path: generate from precomputed phrases (with newlines already included).
     */
    private static void generateFromPrecomputed(OutputStream out, int numPhrases) throws IOException {
        byte[] buffer = new byte[1024 * 1024]; // 1MB buffer
        int pos = 0;
        int mask = phraseMask;
        
        // XorShift random for speed
        long seed = System.nanoTime();
        
        // Process in groups of 8 for better pipelining
        int groups = numPhrases / 8;
        int remainder = numPhrases % 8;
        
        for (int g = 0; g < groups; g++) {
            // Generate 8 random indices using bitwise AND (much faster than modulo)
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i0 = (int)(seed) & mask;
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i1 = (int)(seed) & mask;
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i2 = (int)(seed) & mask;
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i3 = (int)(seed) & mask;
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i4 = (int)(seed) & mask;
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i5 = (int)(seed) & mask;
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i6 = (int)(seed) & mask;
            seed ^= (seed << 21); seed ^= (seed >>> 35); seed ^= (seed << 4);
            int i7 = (int)(seed) & mask;
            
            byte[] p0 = precomputedPhrases[i0];
            byte[] p1 = precomputedPhrases[i1];
            byte[] p2 = precomputedPhrases[i2];
            byte[] p3 = precomputedPhrases[i3];
            byte[] p4 = precomputedPhrases[i4];
            byte[] p5 = precomputedPhrases[i5];
            byte[] p6 = precomputedPhrases[i6];
            byte[] p7 = precomputedPhrases[i7];
            
            int needed = p0.length + p1.length + p2.length + p3.length + 
                         p4.length + p5.length + p6.length + p7.length;
            
            if (pos + needed > buffer.length) {
                out.write(buffer, 0, pos);
                pos = 0;
            }
            
            System.arraycopy(p0, 0, buffer, pos, p0.length); pos += p0.length;
            System.arraycopy(p1, 0, buffer, pos, p1.length); pos += p1.length;
            System.arraycopy(p2, 0, buffer, pos, p2.length); pos += p2.length;
            System.arraycopy(p3, 0, buffer, pos, p3.length); pos += p3.length;
            System.arraycopy(p4, 0, buffer, pos, p4.length); pos += p4.length;
            System.arraycopy(p5, 0, buffer, pos, p5.length); pos += p5.length;
            System.arraycopy(p6, 0, buffer, pos, p6.length); pos += p6.length;
            System.arraycopy(p7, 0, buffer, pos, p7.length); pos += p7.length;
        }
        
        // Handle remainder
        for (int i = 0; i < remainder; i++) {
            seed ^= (seed << 21);
            seed ^= (seed >>> 35);
            seed ^= (seed << 4);
            
            byte[] phrase = precomputedPhrases[(int)(seed) & mask];
            int len = phrase.length;
            
            if (pos + len > buffer.length) {
                out.write(buffer, 0, pos);
                pos = 0;
            }
            
            System.arraycopy(phrase, 0, buffer, pos, len);
            pos += len;
        }
        
        if (pos > 0) {
            out.write(buffer, 0, pos);
        }
    }

    /**
     * Stack-based generation for large grammars.
     */
    private static void generateWithStack(OutputStream out, int numPhrases) throws IOException {
        byte[] buffer = new byte[1024 * 1024];
        int pos = 0;
        int[] stack = new int[256];
        
        long seed = System.nanoTime();

        for (int i = 0; i < numPhrases; i++) {
            int top = 0;
            stack[top++] = startSymbol;

            while (top > 0) {
                int symbol = stack[--top];

                if (symbol < 0) {
                    byte[] term = terminals[~symbol];
                    int len = term.length;
                    
                    if (pos + len > buffer.length) {
                        out.write(buffer, 0, pos);
                        pos = 0;
                    }
                    
                    System.arraycopy(term, 0, buffer, pos, len);
                    pos += len;
                } else {
                    int[][] productions = grammar[symbol];
                    
                    // XorShift64
                    seed ^= (seed << 21);
                    seed ^= (seed >>> 35);
                    seed ^= (seed << 4);
                    
                    int[] production = productions[(int)((seed & 0x7FFFFFFFL) % productions.length)];
                    
                    for (int j = production.length - 1; j >= 0; j--) {
                        stack[top++] = production[j];
                    }
                }
            }
            
            // Newline after each phrase
            if (pos >= buffer.length - 1) {
                out.write(buffer, 0, pos);
                pos = 0;
            }
            buffer[pos++] = NEWLINE;
        }
        
        if (pos > 0) {
            out.write(buffer, 0, pos);
        }
    }

    /**
     * Tries to precompute all possible phrases.
     */
    private static void tryPrecompute() {
        try {
            ArrayList<byte[]> phrases = expand(startSymbol, 0);
            if (phrases.size() <= MAX_PRECOMPUTED) {
                // Pad to power of 2 for fast modulo using bitwise AND
                int size = phrases.size();
                int powerOf2 = Integer.highestOneBit(size);
                if (powerOf2 < size) powerOf2 <<= 1;
                
                precomputedPhrases = new byte[powerOf2][];
                phraseMask = powerOf2 - 1;
                
                // Append newline to each phrase for faster output
                for (int i = 0; i < size; i++) {
                    byte[] orig = phrases.get(i);
                    byte[] withNewline = new byte[orig.length + 1];
                    System.arraycopy(orig, 0, withNewline, 0, orig.length);
                    withNewline[orig.length] = NEWLINE;
                    precomputedPhrases[i] = withNewline;
                }
                
                // Fill remaining slots by duplicating existing phrases
                for (int i = size; i < powerOf2; i++) {
                    precomputedPhrases[i] = precomputedPhrases[i % size];
                }
            }
        } catch (RuntimeException e) {
            // Grammar too large - use stack-based generation
            precomputedPhrases = null;
        }
    }

    /**
     * Recursively expands a symbol into all possible byte arrays.
     */
    private static ArrayList<byte[]> expand(int symbol, int depth) {
        if (depth > 50) throw new RuntimeException("Too deep");

        ArrayList<byte[]> results = new ArrayList<>();

        if (symbol < 0) {
            results.add(terminals[~symbol]);
            return results;
        }

        for (int[] production : grammar[symbol]) {
            ArrayList<byte[]> current = new ArrayList<>();
            current.add(new byte[0]);

            for (int s : production) {
                ArrayList<byte[]> expansions = expand(s, depth + 1);
                ArrayList<byte[]> next = new ArrayList<>();
                
                for (byte[] prefix : current) {
                    for (byte[] suffix : expansions) {
                        byte[] combined = new byte[prefix.length + suffix.length];
                        System.arraycopy(prefix, 0, combined, 0, prefix.length);
                        System.arraycopy(suffix, 0, combined, prefix.length, suffix.length);
                        next.add(combined);
                        if (next.size() > MAX_PRECOMPUTED) {
                            throw new RuntimeException("Too many phrases");
                        }
                    }
                }
                current = next;
            }
            
            results.addAll(current);
            if (results.size() > MAX_PRECOMPUTED) {
                throw new RuntimeException("Too many phrases");
            }
        }
        
        return results;
    }

    /**
     * Parses a grammar file.
     */
    private static void parseGrammar(String filePath) throws IOException {
        Map<String, Integer> nonTerminalMap = new HashMap<>();
        ArrayList<byte[]> terminalList = new ArrayList<>();
        Map<String, Integer> terminalMap = new HashMap<>();
        ArrayList<ArrayList<int[]>> grammarBuilder = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty() || line.charAt(0) != '{') continue;

                String ntName = br.readLine();
                if (ntName == null) break;

                int ntId = getNonTerminalId(ntName, nonTerminalMap, grammarBuilder);
                ArrayList<int[]> productions = grammarBuilder.get(ntId);

                while ((line = br.readLine()) != null && !line.startsWith("}")) {
                    if (!line.isEmpty()) {
                        productions.add(parseProduction(line, nonTerminalMap, 
                                                         terminalList, terminalMap, grammarBuilder));
                    }
                }
            }
        }

        // Convert to arrays
        grammar = new int[grammarBuilder.size()][][];
        for (int i = 0; i < grammarBuilder.size(); i++) {
            ArrayList<int[]> prods = grammarBuilder.get(i);
            grammar[i] = prods.toArray(new int[prods.size()][]);
        }
        
        terminals = terminalList.toArray(new byte[0][]);
        startSymbol = nonTerminalMap.get("<start>");
        
        // Try to precompute all phrases
        tryPrecompute();
    }

    private static int getNonTerminalId(String name, Map<String, Integer> map, 
                                         ArrayList<ArrayList<int[]>> builder) {
        Integer id = map.get(name);
        if (id != null) return id;
        
        id = map.size();
        map.put(name, id);
        builder.add(new ArrayList<>());
        return id;
    }

    private static int getTerminalId(String value, ArrayList<byte[]> list, Map<String, Integer> map) {
        Integer id = map.get(value);
        if (id != null) return id;
        
        int index = list.size();
        list.add(value.getBytes());
        id = ~index;  // Encode as negative
        map.put(value, id);
        return id;
    }

    private static int[] parseProduction(String line, Map<String, Integer> ntMap,
                                          ArrayList<byte[]> termList, Map<String, Integer> termMap,
                                          ArrayList<ArrayList<int[]>> builder) {
        ArrayList<Integer> symbols = new ArrayList<>();
        int i = 0;
        
        while (i < line.length()) {
            int open = line.indexOf('<', i);
            
            if (open == -1) {
                if (i < line.length()) {
                    symbols.add(getTerminalId(line.substring(i), termList, termMap));
                }
                break;
            }
            
            if (open > i) {
                symbols.add(getTerminalId(line.substring(i, open), termList, termMap));
            }
            
            int close = line.indexOf('>', open);
            String ntName = line.substring(open, close + 1);
            symbols.add(getNonTerminalId(ntName, ntMap, builder));
            i = close + 1;
        }
        
        int[] result = new int[symbols.size()];
        for (int j = 0; j < symbols.size(); j++) {
            result[j] = symbols.get(j);
        }
        return result;
    }
}
