# BST Sort vs Collections.sort() - Comparative Analysis

## Executive Summary

This document presents a comparative timing study of two sorting approaches:

1. **SortViaBST.sort()**: Sorting using a Binary Search Tree (BST)
2. **Collections.sort()**: Java's built-in sorting (Timsort)

Both experiments used **permuted ArrayLists without duplicates** of sizes ranging from 1,000 to 20,000 elements (in steps of 1,000). Each data point represents the median of 20 iterations.

---

## Theoretical Background

### SortViaBST Algorithm

```java
1. Create an empty BST
2. Add all N elements to the BST  → O(N log N) average case, O(N²) worst case
3. Repeatedly remove first() element → O(N log N) average case, O(N²) worst case
```

**Time Complexity:**

- **Average Case**: O(N log N) - when BST is relatively balanced
- **Worst Case**: O(N²) - when BST becomes degenerate (like a linked list)
- **Critical Factor**: Depends heavily on input order

### Collections.sort() (Timsort)

```java
1. Adaptive merge sort with optimizations for partially sorted data
2. Uses insertion sort for small subarrays
3. Merges runs efficiently
```

**Time Complexity:**

- **All Cases**: O(N log N) guaranteed
- **Space Complexity**: O(N) auxiliary space
- **Critical Factor**: Stable, adaptive, independent of input order

---

## Experimental Results

### BST Sort Performance

```
N       Time (ns)    Time/N (ns)    Time/(N log N) (ns)
---------------------------------------------------------
1000    83,084       83.08          8.34
2000    181,709      90.85          8.29
3000    308,625      102.88         8.91
4000    459,583      114.90         9.60
5000    518,041      103.61         8.43
10000   1,205,625    120.56         9.07
15000   1,955,500    130.37         9.40
20000   2,812,792    140.64         9.84
```

**Key Observations:**

- **Time/N**: Increases from ~83 ns to ~141 ns → NOT constant → NOT O(N)
- **Time/(N log N)**: Stays roughly constant around 8-10 ns → Confirms O(N log N)
- **Absolute Performance**: ~2.81 ms to sort 20,000 elements

### Collections.sort() Performance

```
N       Time (ns)    Time/N (ns)    Time/(N log N) (ns)
---------------------------------------------------------
1000    93,333       93.33          9.37
2000    191,208      95.60          8.72
3000    303,541      101.18         8.76
4000    336,041      84.01          7.02
5000    436,250      87.25          7.10
10000   774,500      77.45          5.83
15000   1,200,500    80.03          5.77
20000   1,684,166    84.21          5.89
```

**Key Observations:**

- **Time/N**: Decreases from ~93 ns to ~84 ns → NOT constant → NOT O(N)
- **Time/(N log N)**: Roughly constant around 6-9 ns → Confirms O(N log N)
- **Absolute Performance**: ~1.68 ms to sort 20,000 elements
- **Efficiency**: ~40% faster than BST sort for large N

---

## Comparative Analysis

### Speed Comparison (20,000 elements)

| Algorithm          | Time (ms) | Time/(N log N) (ns) | Relative Speed |
| ------------------ | --------- | ------------------- | -------------- |
| Collections.sort() | 1.68      | 5.89                | 1.00x (faster) |
| BST Sort           | 2.81      | 9.84                | 0.60x (slower) |

**Collections.sort() is ~67% faster than BST sort for this dataset.**

### Growth Rate Verification

Both algorithms show **O(N log N) behavior** for permuted input:

- BST Sort: Time/(N log N) ≈ 8-10 ns (relatively constant)
- Collections.sort(): Time/(N log N) ≈ 6-9 ns (relatively constant, slightly decreasing)

The slight decrease in Collections.sort()'s Time/(N log N) ratio suggests **cache efficiency improvements** as N increases - larger arrays benefit more from Timsort's optimized memory access patterns.

### Why Collections.sort() is Faster

1. **Highly Optimized Implementation**

   - Written in highly optimized C/native code in the JVM
   - Decades of performance tuning
   - Cache-friendly memory access patterns

2. **Adaptive Behavior**

   - Exploits any existing order in the data
   - Uses insertion sort for small runs (< 32 elements)
   - Efficient merging strategy

3. **Better Locality of Reference**

   - Works directly on array/list indices
   - Fewer pointer dereferences than BST
   - Better CPU cache utilization

4. **No Tree Overhead**
   - No node allocation/deallocation
   - No tree traversal overhead
   - Minimal object creation

### Why BST Sort Still Shows O(N log N)

The permuted input is crucial:

- Random insertion order keeps the BST **relatively balanced**
- Height remains O(log N) on average
- Each insertion and removal takes O(log N) time

**What would happen with sorted input?**

- BST would degenerate into a linked list (height = N)
- Performance would degrade to O(N²)
- Collections.sort() would remain O(N log N)

---

## Conclusions

### 1. Both Algorithms are O(N log N) for Permuted Input

The check analysis confirms that both show logarithmic growth patterns:

- **BST Sort**: Reliable O(N log N) with random/permuted data
- **Collections.sort()**: Guaranteed O(N log N) regardless of input

### 2. Collections.sort() is Superior in Practice

- **67% faster** for the tested dataset
- **Guaranteed performance** independent of input order
- **Production-ready** with extensive optimizations

### 3. BST Sort is Input-Dependent

- **Good**: O(N log N) with permuted/random data
- **Bad**: O(N²) with sorted or nearly-sorted data
- **Critical Weakness**: Performance depends on input characteristics

### 4. When to Use Each Algorithm

| Use BST Sort When...                    | Use Collections.sort() When...         |
| --------------------------------------- | -------------------------------------- |
| ✓ You need a sorted collection to query | ✓ You just need to sort once           |
| ✓ Input is random/unpredictable         | ✓ You need guaranteed performance      |
| ✓ You'll add/remove elements frequently | ✓ Input order is unknown               |
| ✗ NOT for production sorting tasks      | ✓ **Always for general sorting tasks** |

### 5. Educational Value

This experiment demonstrates:

- The importance of **average vs. worst case** analysis
- How **input characteristics** affect BST-based algorithms
- Why **production libraries** use sophisticated algorithms like Timsort
- The power of **empirical verification** of theoretical complexity

---

## Recommendations

**For CS 2420 Students:**

1. Use these results to understand how theoretical complexity (O(N log N)) manifests in practice
2. Note the importance of **constants** - both are O(N log N), but Collections.sort() has better constants
3. Understand that BST sort's performance depends on **input order** - a critical weakness
4. Appreciate why Java uses Timsort instead of simple BST-based sorting

**For Production Code:**

- **Always use Collections.sort()** or Arrays.sort() for sorting tasks
- Only use BST when you need a **sorted collection** with frequent insertions/deletions
- Be aware of input characteristics when using data structures like BST

---

## Appendix: Raw Data Files

- **BST Sort Results**: `bst_sort_timing.txt`
- **Java Sort Results**: `java_sort_timing.txt`

Both files contain complete timing data for all problem sizes (1,000 to 20,000 in steps of 1,000).

---

## Experiment Details

- **Language**: Java 21
- **Input**: Permuted ArrayLists without duplicates
- **Iterations**: 20 per problem size (median reported)
- **Warm-up**: 3 warm-up runs before measurements
- **Environment**: macOS, Desktop CS 2420 workspace
- **Date**: 2025

---

_Generated by Alex Waldmann - CS 2420 Timing Experiments_
