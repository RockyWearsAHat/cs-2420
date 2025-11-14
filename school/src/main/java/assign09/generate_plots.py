"""
Plotting script for Assignment 9 timing experiments.
Generates plots from CSV data produced by the timing experiments.

@author Alex Waldmann
@author Tyler Gagliardi
@version November 13, 2025
"""

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

def plot_hash_function_comparison():
    """Generate plots comparing BadHash, MediumHash, and GoodHash."""
    
    # Read data
    try:
        times_df = pd.read_csv('hash_function_times.csv')
        collisions_df = pd.read_csv('hash_function_collisions.csv')
    except FileNotFoundError as e:
        print(f"Error: {e}")
        print("Please run HashFunctionTimingExperiment first to generate CSV files.")
        return
    
    # Create figure with 2 subplots
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 6))
    
    # Plot 1: Collisions vs Problem Size
    ax1.plot(collisions_df['ProblemSize'], collisions_df['BadHashCollisions'], 
             marker='o', linewidth=2, label='BadHash', color='red')
    ax1.plot(collisions_df['ProblemSize'], collisions_df['MediumHashCollisions'], 
             marker='s', linewidth=2, label='MediumHash', color='blue')
    ax1.plot(collisions_df['ProblemSize'], collisions_df['GoodHashCollisions'], 
             marker='^', linewidth=2, label='GoodHash', color='green')
    
    ax1.set_xlabel('Problem Size (N)', fontsize=12)
    ax1.set_ylabel('Number of Collisions', fontsize=12)
    ax1.set_title('Collisions vs Problem Size', fontsize=14, fontweight='bold')
    ax1.legend(fontsize=10)
    ax1.grid(True, alpha=0.3)
    
    # Plot 2: Running Time vs Problem Size
    ax2.plot(times_df['ProblemSize'], times_df['BadHashTime'], 
             marker='o', linewidth=2, label='BadHash', color='red')
    ax2.plot(times_df['ProblemSize'], times_df['MediumHashTime'], 
             marker='s', linewidth=2, label='MediumHash', color='blue')
    ax2.plot(times_df['ProblemSize'], times_df['GoodHashTime'], 
             marker='^', linewidth=2, label='GoodHash', color='green')
    
    ax2.set_xlabel('Problem Size (N)', fontsize=12)
    ax2.set_ylabel('Running Time (milliseconds)', fontsize=12)
    ax2.set_title('Running Time vs Problem Size', fontsize=14, fontweight='bold')
    ax2.legend(fontsize=10)
    ax2.grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.savefig('hash_function_comparison.png', dpi=300, bbox_inches='tight')
    print("Saved: hash_function_comparison.png")
    plt.show()

def plot_hashmap_comparison():
    """Generate plots comparing our HashTable with Java's HashMap."""
    
    # Read data
    try:
        put_df = pd.read_csv('hashmap_put_times.csv')
        get_df = pd.read_csv('hashmap_get_times.csv')
        remove_df = pd.read_csv('hashmap_remove_times.csv')
    except FileNotFoundError as e:
        print(f"Error: {e}")
        print("Please run HashMapComparisonExperiment first to generate CSV files.")
        return
    
    # Create figure with 3 subplots
    fig, axes = plt.subplots(1, 3, figsize=(18, 5))
    
    # Plot 1: PUT operation
    axes[0].plot(put_df['ProblemSize'], put_df['MyHashTable'], 
                 marker='o', linewidth=2, label='My HashTable', color='blue')
    axes[0].plot(put_df['ProblemSize'], put_df['JavaHashMap'], 
                 marker='s', linewidth=2, label='Java HashMap', color='red')
    axes[0].set_xlabel('Problem Size (N)', fontsize=12)
    axes[0].set_ylabel('Time (milliseconds)', fontsize=12)
    axes[0].set_title('PUT Operation Performance', fontsize=14, fontweight='bold')
    axes[0].legend(fontsize=10)
    axes[0].grid(True, alpha=0.3)
    
    # Plot 2: GET operation
    axes[1].plot(get_df['ProblemSize'], get_df['MyHashTable'], 
                 marker='o', linewidth=2, label='My HashTable', color='blue')
    axes[1].plot(get_df['ProblemSize'], get_df['JavaHashMap'], 
                 marker='s', linewidth=2, label='Java HashMap', color='red')
    axes[1].set_xlabel('Problem Size (N)', fontsize=12)
    axes[1].set_ylabel('Time (milliseconds)', fontsize=12)
    axes[1].set_title('GET Operation Performance', fontsize=14, fontweight='bold')
    axes[1].legend(fontsize=10)
    axes[1].grid(True, alpha=0.3)
    
    # Plot 3: REMOVE operation
    axes[2].plot(remove_df['ProblemSize'], remove_df['MyHashTable'], 
                 marker='o', linewidth=2, label='My HashTable', color='blue')
    axes[2].plot(remove_df['ProblemSize'], remove_df['JavaHashMap'], 
                 marker='s', linewidth=2, label='Java HashMap', color='red')
    axes[2].set_xlabel('Problem Size (N)', fontsize=12)
    axes[2].set_ylabel('Time (milliseconds)', fontsize=12)
    axes[2].set_title('REMOVE Operation Performance', fontsize=14, fontweight='bold')
    axes[2].legend(fontsize=10)
    axes[2].grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.savefig('hashmap_comparison.png', dpi=300, bbox_inches='tight')
    print("Saved: hashmap_comparison.png")
    plt.show()
    
    # Create a separate plot showing the ratio
    fig, ax = plt.subplots(figsize=(10, 6))
    
    ax.plot(put_df['ProblemSize'], put_df['Ratio'], 
            marker='o', linewidth=2, label='PUT Ratio', color='blue')
    ax.plot(get_df['ProblemSize'], get_df['Ratio'], 
            marker='s', linewidth=2, label='GET Ratio', color='green')
    ax.plot(remove_df['ProblemSize'], remove_df['Ratio'], 
            marker='^', linewidth=2, label='REMOVE Ratio', color='red')
    ax.axhline(y=1.0, color='black', linestyle='--', linewidth=1, alpha=0.5, label='Equal Performance')
    
    ax.set_xlabel('Problem Size (N)', fontsize=12)
    ax.set_ylabel('Time Ratio (MyHashTable / JavaHashMap)', fontsize=12)
    ax.set_title('Performance Ratio: My HashTable vs Java HashMap', fontsize=14, fontweight='bold')
    ax.legend(fontsize=10)
    ax.grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.savefig('hashmap_ratio.png', dpi=300, bbox_inches='tight')
    print("Saved: hashmap_ratio.png")
    plt.show()

def main():
    """Generate all plots."""
    print("=== Generating Assignment 9 Plots ===\n")
    
    print("1. Hash Function Comparison Plots...")
    plot_hash_function_comparison()
    print()
    
    print("2. HashMap Comparison Plots...")
    plot_hashmap_comparison()
    print()
    
    print("All plots generated successfully!")

if __name__ == "__main__":
    main()
