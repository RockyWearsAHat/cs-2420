# BST Sort vs Collections.sort() - Experiment Complete! ✓

## 📊 Results Summary

Your BST sort comparison study is **complete**! Both timing experiments have been successfully executed and analyzed.

### Key Findings

**Collections.sort() is 67% faster than BST sort** for permuted input of 20,000 elements:

- **BST Sort**: 2.81 ms
- **Collections.sort()**: 1.68 ms

**Both algorithms confirmed O(N log N) behavior:**

- BST Sort: Time/(N log N) ≈ 9.08 ns (constant)
- Collections.sort(): Time/(N log N) ≈ 6.54 ns (constant)

---

## 📁 Generated Files

### Timing Experiment Classes

✓ `BSTSortTimingExperiment.java` - Times SortViaBST.sort()
✓ `JavaSortTimingExperiment.java` - Times Collections.sort()

### Data Files

✓ `bst_sort_timing.txt` - Raw BST sort timing data
✓ `java_sort_timing.txt` - Raw Java sort timing data

### Analysis Documents

✓ `SORT_COMPARISON_ANALYSIS.md` - Comprehensive written analysis
✓ `plot_sort_comparison.py` - Python plotting script
✓ `sort_comparison.png` - Visual comparison charts

---

## 🎯 How to Use These Files

### Running the Experiments Again

```bash
# Compile (already done)
javac -d target/classes -cp target/classes src/main/java/assign07/*.java

# Run BST sort experiment
java -cp target/classes assign07.BSTSortTimingExperiment

# Run Java sort experiment
java -cp target/classes assign07.JavaSortTimingExperiment
```

### Generating the Plots

```bash
python3 plot_sort_comparison.py
```

### Viewing Results

- **Written Analysis**: Open `SORT_COMPARISON_ANALYSIS.md`
- **Visual Comparison**: Open `sort_comparison.png`
- **Raw Data**: Open `bst_sort_timing.txt` and `java_sort_timing.txt`

---

## 📈 What the Plots Show

The generated `sort_comparison.png` contains 3 subplots:

1. **Absolute Time Comparison** (left)

   - Shows Collections.sort() is consistently faster
   - Both curves show similar O(N log N) growth shape

2. **Time/N Ratio** (middle)

   - Both increase with N (not constant)
   - Confirms algorithms are NOT O(N)

3. **Time/(N log N) Ratio** (right)
   - Both roughly constant around 6-9 ns
   - Confirms both algorithms ARE O(N log N) ✓

---

## 🔬 Technical Notes

### Fixed Issues During Development

1. **Package Declaration Error**: ArrayListGenerator had wrong package (timing vs assign07)

   - Fixed using sed to correct package declaration
   - Recompiled successfully

2. **Compilation Dependencies**: Required proper compilation order
   - SortedSet → BinarySearchTree → ArrayListGenerator → SortViaBST → Timing classes

### Experiment Parameters

- **Problem Sizes**: 1,000 to 20,000 (step: 1,000)
- **Iterations**: 20 per problem size (median reported)
- **Warm-up**: 3 warm-up iterations before timing
- **Input Type**: Permuted ArrayLists without duplicates
- **Java Version**: Java 21

---

## 💡 Key Insights for Your Assignment

### Why Collections.sort() is Faster

1. **Highly optimized native implementation** (decades of tuning)
2. **Adaptive behavior** (exploits existing order)
3. **Better cache locality** (array-based, not pointer-chasing)
4. **No object allocation overhead** (no tree nodes)

### Why BST Sort Still Works Well

- **Permuted input keeps BST balanced** → O(log N) height
- Each insertion/removal is O(log N)
- Total: N insertions + N removals = O(N log N)

### Critical Weakness of BST Sort

- **Input-dependent performance**
- Sorted input → degenerate tree → O(N²) disaster!
- Collections.sort() → O(N log N) always, regardless of input

---

## 🎓 Learning Outcomes

This experiment demonstrates:
✓ Empirical verification of theoretical complexity
✓ Importance of constant factors (both O(N log N), different speeds)
✓ Input-dependent vs. guaranteed performance
✓ Why production code uses sophisticated algorithms
✓ The power of "check analysis" (Time/N vs Time/(N log N))

---

## 📝 For Your Report/Assignment

You now have:

- **Empirical data** showing O(N log N) behavior for both algorithms
- **Comparative analysis** explaining why Collections.sort() is faster
- **Visual plots** demonstrating growth rates
- **Check analysis** verifying complexity claims
- **Discussion of trade-offs** between the two approaches

**Recommendation**: Use the comprehensive analysis in `SORT_COMPARISON_ANALYSIS.md` as the basis for your report. It includes all the key points, data, and explanations.

---

## ✅ Experiment Status

| Task                            | Status     | File                          |
| ------------------------------- | ---------- | ----------------------------- |
| Create BSTSortTimingExperiment  | ✓ Done     | BSTSortTimingExperiment.java  |
| Create JavaSortTimingExperiment | ✓ Done     | JavaSortTimingExperiment.java |
| Fix ArrayListGenerator package  | ✓ Fixed    | ArrayListGenerator.java       |
| Run BST sort timing             | ✓ Complete | bst_sort_timing.txt           |
| Run Java sort timing            | ✓ Complete | java_sort_timing.txt          |
| Generate comparison plots       | ✓ Complete | sort_comparison.png           |
| Write comprehensive analysis    | ✓ Complete | SORT_COMPARISON_ANALYSIS.md   |

---

**All timing experiments complete! 🎉**

_Generated: 2025 - CS 2420 Assignment_
