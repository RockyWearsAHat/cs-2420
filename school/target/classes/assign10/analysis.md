Assignment 10 Analysis Document

Author: Alex Waldmann and Tyler Gagliardi
Date: November 20, 2025

════════════════════════════════════════════════════════════════════════════════

QUESTION 1: Pair Programming

We are not using a pair programming exception on this assignment. My programming partner is Tyler Gagliardi, and Alex Waldmann submitted the program files to Gradescope.

════════════════════════════════════════════════════════════════════════════════

QUESTION 2: Experiment Design - Basic Priority Queue Methods

Experimental Setup

Experimental Setup

To assess the efficiency of the basic priority queue methods (add, peek, and extractMax), I designed three separate timing experiments:

Common Setup:
• Created timing experiments that extend the TimingExperiment framework
• Problem sizes (N): Started at 10,000 elements and doubled up to 160,000,000 elements (10 data points)
• Each measurement was repeated 5 times and averaged to reduce variance
• Included a warmup phase (10 iterations) to allow JVM JIT compilation to stabilize
• Used randomized Integer data to avoid best-case or worst-case scenarios

Individual Method Tests:

1. AddTimingExperiment:
   • Created an empty BinaryMaxHeap in setup
   • Measured time to add N random integers to the heap
   • Each add() operation potentially triggers array resizing (worst case) or simple percolate-up (average case)

2. PeekTimingExperiment:
   • Pre-built a heap with N elements during setup
   • Measured time for a single peek() operation
   • This isolates the O(1) behavior of accessing the root element

3. ExtractMaxTimingExperiment:
   • Pre-built a heap with N elements during setup
   • Measured time to extract all N elements one by one
   • Each extractMax() operation involves removing the root and percolating down

Replication: To replicate these results, compile the Java files in assign10 package, ensure the timing package is available, and run:

    java assign10.AddTimingExperiment
    java assign10.PeekTimingExperiment
    java assign10.ExtractMaxTimingExperiment

════════════════════════════════════════════════════════════════════════════════

QUESTION 3: Plot of Basic Priority Queue Methods

QUESTION 3: Plot of Basic Priority Queue Methods

[Insert image: Running Times of .add(), .peek(), and .extractMax() vs. Heap Size (N)]

The plot shows three distinct curves:

• Blue line (AddTiming): Shows mostly flat, very low timing (under 500 ns) across all N values, with a slight upward trend. This represents the amortized O(1) average-case behavior of add().

• Red line (PeekTiming): Shows a perfectly flat horizontal line around 1000-1500 ns across all heap sizes. This confirms the O(1) constant-time behavior of peek().

• Yellow line (ExtractMaxTiming): Shows a clear upward trend from ~2000 ns to ~4500 ns as N increases. This represents the O(log N) behavior of each extractMax() operation, though the plot shows the cumulative time for extracting all N elements, giving us O(N log N) total time.

════════════════════════════════════════════════════════════════════════════════

QUESTION 4: Analysis of Observed vs. Expected Big-O Behaviors

Comparison with Expected Big-O:

add() - Average Case O(1), Worst Case O(log N):
Expected: Average case should be constant time (amortized) because most insertions only require placing an element at the end and potentially percolating up a few levels. Worst case occurs when the heap needs resizing.

Observed: The nearly flat blue line with minimal growth confirms the O(1) amortized behavior. The slight upward trend is likely due to: 1. Occasional array resizing operations (amortized across many adds) 2. Cache effects as the heap grows larger than CPU cache 3. Percolate-up operations on average travel log(N) levels, but the constant factor is very small

Conclusion: ✓ Matches expected behavior

peek() - O(1):
Expected: Should be perfectly constant time regardless of heap size, as it simply returns heap[0].

Observed: The perfectly flat red line at ~1000-1500 ns confirms true O(1) behavior with no dependence on N.

Conclusion: ✓ Matches expected behavior perfectly

extractMax() - O(log N) per operation:
Expected: Each extraction should take O(log N) time because it involves percolating down from root to leaf. Extracting all N elements should take O(N log N) total time.

Observed: The yellow line shows clear upward growth. While the plot measures cumulative time for N extractions, the growth rate is sublinear on this scale (not exponential), which is consistent with O(N log N) behavior.

Conclusion: ✓ Matches expected behavior

Why the observations match:

The binary max heap is correctly implemented with:
• Array-backed storage starting at index 0
• Proper parent-child relationships: parent at (i-1)/2, children at 2i+1 and 2i+2
• Efficient percolate-up and percolate-down operations that maintain heap property
• The buildHeap operation used in construction is O(N), not visible in these single-operation tests

════════════════════════════════════════════════════════════════════════════════

QUESTION 5: findKLargestHeap Complexity Analysis

Understanding the Parameters:

In findKLargestHeap(List<E> items, int k):
• N = the size of the input list (items.size())
• k = the number of largest elements to extract

Expected Complexity: O(N + k log N)

Breakdown:

1. O(N) - Building the heap from the input list using the buildHeap() operation (Floyd's method)
2. O(k log N) - Extracting k elements, where each extractMax() takes O(log N) time

Case Analysis:

When k << N (k is much smaller than N):
Example: Finding the top 10 elements from a list of 1,000,000

Complexity: O(N + k log N) ≈ O(N) (linear behavior)

The buildHeap phase dominates since k log N becomes negligible. This is the optimal scenario for the heap approach compared to sorting (which would be O(N log N)).

When k ≈ N (k is close to N):
Example: Finding the top 900,000 elements from a list of 1,000,000

Complexity: O(N + k log N) ≈ O(N + N log N) ≈ O(N log N)

The extraction phase dominates since we're extracting nearly all elements. This approaches the same complexity as full sorting, so the heap advantage disappears. In practice, sorting might be faster due to better cache locality and optimized library implementations.

════════════════════════════════════════════════════════════════════════════════

QUESTION 6: Experiment Design - Heap vs. Sort Comparison

Experimental Setup

I designed two comparative experiments to assess the performance difference between the heap-based and sort-based approaches:

Experiment 1: Fixed N, Varying k
Goal: Compare performance as k grows from small to large relative to a fixed list size

Setup:
• Fixed list size: N = 1,024,000 elements
• k values: 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 256000, 512000, 1024000
• k ranges from ~0.1% to 100% of N
• List contains randomized integers (shuffled after generation)
• 5 iterations per k value, averaged
• Warmup: 10 iterations before timing

Experiment 2: Fixed k, Varying N
Goal: Compare performance as list size grows with a small fixed k

Setup:
• Fixed k = 10 (finding top 10 elements)
• N values: 10000, 20000, 40000, 80000, 160000, 320000, 640000, 1280000, 2560000, 5120000
• N doubles each step (logarithmic progression)
• List contains randomized integers (shuffled after generation)
• 5 iterations per N value, averaged
• Warmup: 10 iterations before timing

Why Randomization Matters:
• Initially tested with sorted ascending data: IntStream.rangeClosed(1, n)
• This gave misleading results because Java's Timsort detected the sorted runs and achieved near-linear time
• After adding Collections.shuffle(list), we observed the true O(N log N) behavior of sorting
• Randomization ensures we measure typical-case performance, not best-case

Replication: To replicate these results:

    Modify FindKLargestHeapTimingExperiment.java and FindKLargestSortTimingExperiment.java
    to set desired N and k ranges in main() method, then compile and run:

    java assign10.FindKLargestHeapTimingExperiment
    java assign10.FindKLargestSortTimingExperiment

════════════════════════════════════════════════════════════════════════════════

QUESTION 7: Plot of Heap vs. Sort Comparison

Experiment 1 Results: k-Largest Performance (N = 1,024,000, k varies)

[Insert image: k-Largest Performance Comparison (Heap vs. Sort [k == n])]

Observations:
• Both methods show superlinear growth as k increases
• For small k (< 32,000), heap and sort have similar performance
• As k approaches N, both curves steepen significantly
• Heap (blue) grows faster than sort (red) for large k values
• At k = 1,024,000 (extracting all elements), heap takes ~8.5e9 ns while sort takes ~4.5e9 ns

Interpretation:
• Small k: Both methods have similar overhead from building/copying the data structure
• Large k: The heap's O(k log N) extraction phase becomes expensive when k ≈ N
• Sort maintains more consistent performance because it does all work upfront (O(N log N))
• The crossover suggests that for k > 50% of N, sorting becomes more efficient

Experiment 2 Results: k-Largest Performance (k = 10, N varies)

[Insert image: k-Largest Performance Comparison (Heap vs. Sort [k == 10])]

Observations:
• Both methods show growth as N increases, but at different rates
• Heap (blue) grows more slowly than sort (red)
• At small N (< 100,000), both methods are comparable
• At large N (5,120,000), heap takes ~1.5e9 ns while sort takes ~4.5e9 ns
• Heap is approximately 3x faster than sort at the largest N value

Interpretation:
• The heap's O(N + k log N) ≈ O(N) behavior (when k is small) clearly dominates
• Sort's O(N log N) behavior shows steeper growth on the logarithmic N scale
• This confirms the theoretical advantage of heaps for small-k scenarios
• The gap widens as N increases, validating that heap is the better choice for "top-k" queries

════════════════════════════════════════════════════════════════════════════════

QUESTION 8: Analysis of Observed vs. Expected Big-O Behaviors

Expected Complexities:

findKLargestHeap: O(N + k log N)
• Build heap: O(N) using Floyd's buildHeap
• Extract k times: O(k log N)

findKLargestSort: O(N log N)
• Copy list: O(N)
• Sort entire list: O(N log N) using Timsort
• Extract first k: O(k) ≈ O(1) for small k

Analysis of Experiment 1 (Fixed N, Varying k):

Expected:
• Heap should grow linearly with k: O(N + k log N) where N is constant
• Sort should remain constant: O(N log N) regardless of k

Observed:
• ❌ Sort grows with k (unexpected)
• ✓ Heap grows with k (expected)
• ❌ Heap grows faster than sort for large k (unexpected)

Explanation of Discrepancy:

1. Sort's growth: Though sorting takes O(N log N) regardless of k, the subList and ArrayList copy operations for extracting k elements add O(k) overhead. For large k, this copying becomes measurable.

2. Heap's steep growth: When k approaches N, we're essentially doing a heapsort (build + extract all), which is O(N log N). However, our implementation has higher constant factors than the optimized Timsort library:
   • Individual percolate operations have method call overhead
   • Generic comparisons require casting and interface calls
   • Timsort is highly optimized in the standard library

3. Why sort wins at large k: For k ≈ N, both are O(N log N), but Timsort's implementation is more efficient:
   • Better cache locality (sequential access patterns)
   • Fewer comparisons in practice
   • Native-quality compiled code

Analysis of Experiment 2 (Fixed k = 10, Varying N):

Expected:
• Heap: O(N + 10 log N) ≈ O(N) — should grow linearly
• Sort: O(N log N) — should grow superlinearly (N times the log factor)

Observed:
• ✓ Heap grows approximately linearly (flatter curve)
• ✓ Sort grows superlinearly (steeper curve)
• ✓ Heap is faster than sort, gap widens as N increases

Why observations match:

1. Heap's linear behavior: With k = 10 fixed, the extraction phase contributes only 10 log N ≈ 10 × 23 = 230 operations at N = 5,120,000. This is dwarfed by the O(N) buildHeap, so we see effectively linear growth.

2. Sort's superlinear behavior: The N log N term dominates. Even though log N grows slowly, multiplying by N creates noticeable curvature on the plot.

3. Performance gap: The constant factor difference (≈3x at large N) reflects that building a heap (O(N)) is fundamentally cheaper than sorting (O(N log N)) when we only need k << N elements.

Conclusion:

The experiments confirm the theoretical predictions:
✅ Heaps excel when k << N (Experiment 2)
✅ Sorting is competitive when k ≈ N (Experiment 1)
✅ The crossover point occurs around k ≈ N/2 based on our implementation

The minor discrepancies (sort's k-dependence in Experiment 1) are due to implementation details like list copying, not algorithmic complexity. The overall trends validate the Big-O analysis and demonstrate when each approach is preferable in practice.

════════════════════════════════════════════════════════════════════════════════

SUMMARY AND RECOMMENDATIONS

Based on the experimental results:

1. For small k relative to N: Use findKLargestHeap — it's approximately 3x faster at large N

2. For k > 50% of N: Use findKLargestSort — simpler and faster due to library optimizations

3. Basic heap operations: All behave as expected (add: amortized O(1), peek: O(1), extractMax: O(log N))

4. Implementation quality matters: Library implementations (Timsort) can outperform naive heap implementations even when Big-O suggests otherwise

The binary max heap implementation successfully meets the assignment requirements and demonstrates the expected asymptotic behaviors across all tested scenarios.
