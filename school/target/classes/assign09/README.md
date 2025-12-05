# Assignment 9 Timing Experiments

## Overview

This package contains comprehensive timing experiments to evaluate hash table performance.

## Experiments Included

### 1. HashFunctionTimingExperiment.java

**Purpose**: Compare the quality of three hash functions (BadHash, MediumHash, GoodHash).

**Metrics**:

- Number of collisions for varying problem sizes
- Running time for insertion operations

**Output Files**:

- `hash_function_times.csv`: Time measurements for each hash function
- `hash_function_collisions.csv`: Collision counts for each hash function

**How to Run**:

```bash
cd school/src/main/java
javac assign09/*.java
java assign09.HashFunctionTimingExperiment
```

### 2. HashMapComparisonExperiment.java

**Purpose**: Compare our HashTable implementation with Java's HashMap.

**Operations Tested**:

- PUT: Insert N key-value pairs
- GET: Retrieve N keys
- REMOVE: Delete N keys

**Output Files**:

- `hashmap_put_times.csv`: PUT operation timing comparison
- `hashmap_get_times.csv`: GET operation timing comparison
- `hashmap_remove_times.csv`: REMOVE operation timing comparison

**How to Run**:

```bash
cd school/src/main/java
javac assign09/*.java
java assign09.HashMapComparisonExperiment
```

### 3. StudentHashDemo.java

**Purpose**: Demonstrate HashTable usage with various key types.

**Demonstrations**:

- StudentBadHash, StudentMediumHash, StudentGoodHash as keys
- String keys with Integer values
- Integer keys with String values
- All HashTable operations (put, get, remove, containsKey, etc.)

**How to Run**:

```bash
cd school/src/main/java
javac assign09/*.java
java assign09.StudentHashDemo
```

## Generating Plots

After running the timing experiments, use the Python script to generate plots:

### Prerequisites

```bash
pip install pandas matplotlib numpy
```

### Generate All Plots

```bash
cd school/src/main/java/assign09
python3 generate_plots.py
```

### Output Images

- `hash_function_comparison.png`: 2 plots showing collisions and running time
- `hashmap_comparison.png`: 3 plots showing PUT, GET, REMOVE performance
- `hashmap_ratio.png`: Performance ratio comparison

## Experiment Parameters

### Hash Function Experiment

- **Problem Sizes**: 100, 500, 1000, 2500, 5000, 10000 students
- **Warmup Iterations**: 5
- **Timing Iterations**: 20 (averaged)
- **Random Seed**: 42 (for reproducibility)

### HashMap Comparison Experiment

- **Problem Sizes**: 1000, 5000, 10000, 50000, 100000 entries
- **Warmup Iterations**: 5
- **Timing Iterations**: 20 (averaged)
- **Key Type**: String (well-distributed hash function)
- **Random Seed**: 42 (for reproducibility)

## Expected Results

### Hash Function Quality

- **BadHash**: O(N²) behavior, N-1 collisions (all entries in one bucket)
- **MediumHash**: O(N) behavior, moderate collisions (~N/M where M=capacity)
- **GoodHash**: O(N) behavior, fewest collisions, best distribution

### HashMap Comparison

- **Both implementations**: O(N) linear scaling
- **Performance Ratio**: My HashTable ~1.5-2.0x slower than Java HashMap
- **Reason**: Java's optimizations (tree-based buckets, JVM optimizations)

## Analysis Document

All results, plots, and interpretations are documented in:

- `assign09_analysis.md`

## Authors

- Alex Waldmann
- Tyler Gagliardi

## Date

November 13, 2025
