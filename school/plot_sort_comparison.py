import matplotlib.pyplot as plt
import numpy as np

# BST Sort data
bst_n = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 
         11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000, 19000, 20000]
bst_time = [83084, 181709, 308625, 459583, 518041, 622292, 748542, 936250, 1022917, 1205625,
            1365209, 1517208, 1652541, 1814833, 1955500, 2102083, 2288000, 2415958, 2624000, 2812792]

# Java Sort data  
java_n = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000,
          11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000, 19000, 20000]
java_time = [93333, 191208, 303541, 336041, 436250, 552666, 577542, 591083, 686417, 774500,
             856000, 936834, 1011250, 1109042, 1200500, 1364250, 1486416, 1495667, 1591875, 1684166]

# Convert to milliseconds for easier reading
bst_time_ms = [t / 1e6 for t in bst_time]
java_time_ms = [t / 1e6 for t in java_time]

# Create figure with 3 subplots
fig, axes = plt.subplots(1, 3, figsize=(18, 5))

# Plot 1: Absolute time comparison
axes[0].plot(bst_n, bst_time_ms, 'o-', label='BST Sort', linewidth=2, markersize=6, color='#e74c3c')
axes[0].plot(java_n, java_time_ms, 's-', label='Collections.sort()', linewidth=2, markersize=6, color='#3498db')
axes[0].set_xlabel('Problem Size (N)', fontsize=12)
axes[0].set_ylabel('Time (milliseconds)', fontsize=12)
axes[0].set_title('Sorting Time Comparison', fontsize=14, fontweight='bold')
axes[0].legend(fontsize=11)
axes[0].grid(True, alpha=0.3)

# Plot 2: Time/N (should increase for O(N log N))
bst_time_per_n = [t/n for t, n in zip(bst_time, bst_n)]
java_time_per_n = [t/n for t, n in zip(java_time, java_n)]
axes[1].plot(bst_n, bst_time_per_n, 'o-', label='BST Sort', linewidth=2, markersize=6, color='#e74c3c')
axes[1].plot(java_n, java_time_per_n, 's-', label='Collections.sort()', linewidth=2, markersize=6, color='#3498db')
axes[1].set_xlabel('Problem Size (N)', fontsize=12)
axes[1].set_ylabel('Time/N (nanoseconds)', fontsize=12)
axes[1].set_title('Time/N Ratio (should increase for O(N log N))', fontsize=14, fontweight='bold')
axes[1].legend(fontsize=11)
axes[1].grid(True, alpha=0.3)

# Plot 3: Time/(N log N) (should be constant for O(N log N))
bst_time_per_nlogn = [t/(n * np.log2(n)) for t, n in zip(bst_time, bst_n)]
java_time_per_nlogn = [t/(n * np.log2(n)) for t, n in zip(java_time, java_n)]
axes[2].plot(bst_n, bst_time_per_nlogn, 'o-', label='BST Sort', linewidth=2, markersize=6, color='#e74c3c')
axes[2].plot(java_n, java_time_per_nlogn, 's-', label='Collections.sort()', linewidth=2, markersize=6, color='#3498db')
axes[2].set_xlabel('Problem Size (N)', fontsize=12)
axes[2].set_ylabel('Time/(N log N) (nanoseconds)', fontsize=12)
axes[2].set_title('Time/(N log N) Ratio (should be constant for O(N log N))', fontsize=14, fontweight='bold')
axes[2].legend(fontsize=11)
axes[2].grid(True, alpha=0.3)

plt.tight_layout()
plt.savefig('sort_comparison.png', dpi=300, bbox_inches='tight')
print("✓ Plot saved to sort_comparison.png")

# Print summary statistics
print("\n" + "="*60)
print("SUMMARY STATISTICS")
print("="*60)
print(f"\nFor N=20,000:")
print(f"  BST Sort:          {bst_time_ms[-1]:.2f} ms")
print(f"  Collections.sort(): {java_time_ms[-1]:.2f} ms")
print(f"  Speedup:           {bst_time_ms[-1]/java_time_ms[-1]:.2f}x")
print(f"\nAverage Time/(N log N) constants:")
print(f"  BST Sort:          {np.mean(bst_time_per_nlogn):.2f} ns")
print(f"  Collections.sort(): {np.mean(java_time_per_nlogn):.2f} ns")
print("\nBoth algorithms confirm O(N log N) behavior!")
print("Collections.sort() is ~67% faster due to better constants and optimization.")
