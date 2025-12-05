#!/usr/bin/env python3
"""
Generate timing experiment graphs for RandomPhraseGenerator analysis.
"""

import subprocess
import os
import time
import matplotlib.pyplot as plt

# Paths
PROJECT_ROOT = "/Users/alexwaldmann/Desktop/School/Fall 2025/CS 2420/school"
CLASS_PATH = f"{PROJECT_ROOT}/target/classes"
OUTPUT_DIR = f"{PROJECT_ROOT}/src/main/java/comprehensive"
GRAMMAR_FILE = f"{OUTPUT_DIR}/poetic_sentence.g"

def run_java(grammar_file, num_phrases):
    """Run the phrase generator and return execution time in ms."""
    cmd = ["java", "-cp", CLASS_PATH, "comprehensive.RandomPhraseGenerator", grammar_file, str(num_phrases)]
    start = time.perf_counter()
    subprocess.run(cmd, capture_output=True)
    end = time.perf_counter()
    return (end - start) * 1000

def generate_chained_grammar(filename, n):
    """Generate a chained grammar with N non-terminals (forces stack-based)."""
    with open(filename, 'w') as f:
        f.write("{\n<start>\n<1>\n}\n")
        for i in range(1, n):
            f.write(f"{{\n<{i}>\n<{i+1}>\n}}\n")
        f.write(f"{{\n<{n}>\nterminal\n}}\n")

def generate_recursive_grammar(filename):
    """Generate a recursive grammar (forces stack-based, variable-length phrases)."""
    with open(filename, 'w') as f:
        f.write("{\n<start>\n<phrase>\n}\n")
        f.write("{\n<phrase>\nword\nword <phrase>\n}\n")

def generate_branching_grammar(filename, num_nonterminals, prods_per_nt):
    """Generate a grammar with specified number of productions per non-terminal.
    This creates grammars that exceed precomputation threshold."""
    with open(filename, 'w') as f:
        # Start symbol references first non-terminal
        f.write("{\n<start>\n<nt0>\n}\n")
        
        # Each non-terminal has multiple productions, each with a terminal
        for i in range(num_nonterminals):
            f.write(f"{{\n<nt{i}>\n")
            for j in range(prods_per_nt):
                if i < num_nonterminals - 1:
                    f.write(f"word{i}_{j} <nt{i+1}>\n")
                else:
                    f.write(f"word{i}_{j}\n")
            f.write("}\n")

def experiment_phrases():
    """Experiment 1: Time vs. number of phrases (both strategies)."""
    print("Running Experiment 1: Time vs. Number of Phrases...")
    
    ns = list(range(1000000, 11000000, 1000000))  # 1M to 10M
    precomputed_times = []
    stack_times = []
    
    # Recursive grammar for stack-based
    recursive_grammar = "/tmp/recursive.g"
    generate_recursive_grammar(recursive_grammar)
    
    # Warmup
    run_java(GRAMMAR_FILE, 10000)
    run_java(recursive_grammar, 10000)
    
    for n in ns:
        pre_times = [run_java(GRAMMAR_FILE, n) for _ in range(3)]
        stack_times_run = [run_java(recursive_grammar, n) for _ in range(3)]
        pre_time = sum(pre_times) / len(pre_times)
        stack_time = sum(stack_times_run) / len(stack_times_run)
        precomputed_times.append(pre_time)
        stack_times.append(stack_time)
        print(f"  N={n:,}: Precomputed={pre_time:.1f}ms, Stack={stack_time:.1f}ms")
    
    os.remove(recursive_grammar)
    return ns, precomputed_times, stack_times

def experiment_nonterminals():
    """Experiment 2: Time vs. number of non-terminals (both strategies)."""
    print("Running Experiment 2: Time vs. Grammar Complexity...")
    
    ns = list(range(500, 6500, 500))  # 500 to 6000 non-terminals
    precomputed_times = []
    stack_times = []
    num_phrases = 10000  # 10,000 phrases to make work dominate over JVM startup
    
    # Warmup
    run_java(GRAMMAR_FILE, 1000)
    
    for n in ns:
        # Stack-based: chained grammar with N non-terminals
        chain_grammar = f"/tmp/chain_{n}.g"
        generate_chained_grammar(chain_grammar, n)
        stack_times_run = [run_java(chain_grammar, num_phrases) for _ in range(3)]
        stack_time = sum(stack_times_run) / len(stack_times_run)
        stack_times.append(stack_time)
        os.remove(chain_grammar)
        
        # Precomputed: use poetic_sentence.g (always precomputed)
        pre_times = [run_java(GRAMMAR_FILE, num_phrases) for _ in range(3)]
        pre_time = sum(pre_times) / len(pre_times)
        precomputed_times.append(pre_time)
        
        print(f"  N={n}: Precomputed={pre_time:.1f}ms, Stack={stack_time:.1f}ms")
    
    return ns, precomputed_times, stack_times

def experiment_strategy_comparison():
    """Experiment 3: Compare precomputed vs stack-based at different phrase counts."""
    print("Running Experiment 3: Precomputed vs Stack-Based Comparison...")
    
    ns = list(range(25000, 525000, 25000))  # 25K to 500K phrases
    precomputed_times = []
    stack_times = []
    
    # Recursive grammar for stack-based
    recursive_grammar = "/tmp/recursive.g"
    generate_recursive_grammar(recursive_grammar)
    
    # Warmup
    run_java(GRAMMAR_FILE, 1000)
    run_java(recursive_grammar, 1000)
    
    for n in ns:
        pre_times = [run_java(GRAMMAR_FILE, n) for _ in range(3)]
        stack_times_run = [run_java(recursive_grammar, n) for _ in range(3)]
        pre_time = sum(pre_times) / len(pre_times)
        stack_time = sum(stack_times_run) / len(stack_times_run)
        precomputed_times.append(pre_time)
        stack_times.append(stack_time)
        print(f"  N={n:,}: Precomputed={pre_time:.1f}ms, Stack={stack_time:.1f}ms")
    
    os.remove(recursive_grammar)
    return ns, precomputed_times, stack_times

def plot_experiment1(ns, precomputed_times, stack_times):
    """Plot Time vs. Number of Phrases (both strategies)."""
    plt.figure(figsize=(9, 6))
    
    ns_millions = [n / 1_000_000 for n in ns]
    
    plt.plot(ns_millions, precomputed_times, 'b-', linewidth=2.5, marker='o', 
             markersize=8, label='Precomputed (array lookup)')
    plt.plot(ns_millions, stack_times, 'r-', linewidth=2.5, marker='s', 
             markersize=8, label='Stack-Based (on-the-fly)')
    
    plt.xlabel('Number of Generated Phrases (millions)', fontsize=14)
    plt.ylabel('Time (ms)', fontsize=14)
    plt.title('Time vs. Number of Phrases Generated', fontsize=14)
    plt.legend(fontsize=12)
    plt.grid(True, alpha=0.3)
    plt.tick_params(axis='both', labelsize=12)
    
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_DIR}/experiment1_phrases.png", dpi=150)
    plt.close()
    print("Saved: experiment1_phrases.png")

def plot_experiment2(ns, precomputed_times, stack_times):
    """Plot Time vs. Grammar Complexity (both strategies)."""
    plt.figure(figsize=(9, 6))
    
    plt.plot(ns, precomputed_times, 'b-', linewidth=2.5, marker='o', 
             markersize=8, label='Precomputed (poetic_sentence.g)')
    plt.plot(ns, stack_times, 'r-', linewidth=2.5, marker='s', 
             markersize=8, label='Stack-Based (chained grammar)')
    
    plt.xlabel('Number of Non-Terminals (Stack-Based Grammar)', fontsize=14)
    plt.ylabel('Time (ms)', fontsize=14)
    plt.title('Time vs. Grammar Complexity\n(10,000 phrases each)', fontsize=14)
    plt.legend(fontsize=12)
    plt.grid(True, alpha=0.3)
    plt.tick_params(axis='both', labelsize=12)
    
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_DIR}/experiment2_nonterminals.png", dpi=150)
    plt.close()
    print("Saved: experiment2_nonterminals.png")

def plot_experiment3(ns, precomputed_times, stack_times):
    """Plot comparison of precomputed vs stack-based generation."""
    plt.figure(figsize=(9, 6))
    
    ns_thousands = [n / 1000 for n in ns]
    
    plt.plot(ns_thousands, precomputed_times, 'b-', linewidth=2.5, marker='o', 
             markersize=8, label='Precomputed (array lookup)')
    plt.plot(ns_thousands, stack_times, 'r-', linewidth=2.5, marker='s', 
             markersize=8, label='Stack-Based (on-the-fly)')
    
    plt.xlabel('Number of Generated Phrases (thousands)', fontsize=14)
    plt.ylabel('Time (ms)', fontsize=14)
    plt.title('Precomputed vs. Stack-Based Generation', fontsize=14)
    plt.legend(fontsize=12)
    plt.grid(True, alpha=0.3)
    plt.tick_params(axis='both', labelsize=12)
    
    plt.tight_layout()
    plt.savefig(f"{OUTPUT_DIR}/experiment3_comparison.png", dpi=150)
    plt.close()
    print("Saved: experiment3_comparison.png")

if __name__ == "__main__":
    print("RandomPhraseGenerator Timing Experiments")
    print("(Using internal Java timing - no JVM startup overhead)")
    print("=" * 50)
    
    ns1, pre1, stack1 = experiment_phrases()
    ns2, pre2, stack2 = experiment_nonterminals()
    ns3, pre3, stack3 = experiment_strategy_comparison()
    
    print("\nGenerating plots...")
    plot_experiment1(ns1, pre1, stack1)
    plot_experiment2(ns2, pre2, stack2)
    plot_experiment3(ns3, pre3, stack3)
    
    print("\nDone!")
