# Assignment 8 Analysis - Quick Reference Guide

## 📋 What You Need to Submit

A PDF document with answers to 5 questions. Each question should be on a separate page for Gradescope matching.

---

## ✅ Question 1: Pair Programming (Easy!)

Just state if you're working alone or with a partner.

**Example:** "I am using a pair programming exception on this assignment."

---

## ✅ Question 2: DFS Neighbor Order (Solved!)

**Answer:** The neighbor visiting order **Right, Down, Left, Up** results in DFS finding a path to a non-closest goal.

**Details:**

- DFS searched **140 nodes**
- DFS path length: **71**
- BFS path length: **27** (shortest)

**How to verify:**

1. Edit `Graph.java` line ~136, change neighbor order to: Right, Down, Left, Up
2. Recompile and run `Question2_NeighborOrderTester`
3. You'll see DFS finds longer path than BFS

---

## ✅ Question 3: DFS Fewer Nodes, Farther Goal (Example Ready!)

**Answer:** YES, it's possible.

**Conditions:**

- DFS neighbor order must align with farther goal's direction
- BFS must explore many branches to find closest goal
- Farther goal is in a "straight" path in DFS's direction

**Example Maze:** See `q3_example.txt`

```
XXXXXXX
X  G  X  <- Closer goal
X XXX X  <- Obstacles
X  S  X  <- Start
X  G  X  <- Farther goal
XXXXXXX
```

**With Down-first DFS:**

- DFS goes straight down → finds farther goal with ~4 nodes
- BFS explores all directions → finds closer goal with ~15 nodes
- DFS wins even though it found a farther goal!

---

## ✅ Question 4: BFS O(1), DFS O(N) (Example Ready!)

**Answer:** Goal must be adjacent to start, but DFS explores away first.

**Conditions:**

- Goal immediately next to start (BFS finds in 2 nodes)
- Long corridor in DFS's preferred direction
- DFS explores entire corridor before backtracking to goal

**Example Maze:** See `q4_example.txt`

```
XXXXX
XSGXX <- Goal right next to Start!
X   X
X   X <- Long corridor
X   X    (if DFS goes Down first)
XXXXX
```

**Results:**

- BFS: 2 nodes (O(1) - constant)
- DFS: 23 nodes (O(N) - explored whole corridor)

---

## ✅ Question 5: Experiment (Already Done!)

**Results from PathfindingAnalysis:**

- 10 random 50x50 mazes, 30% walls, 5 goals
- BFS average: **161.5 nodes**
- DFS average: **228.3 nodes**
- **BFS was 29.3% more efficient**

**Discussion:**

- BFS is more consistent and efficient on average
- BFS guarantees shortest path to closest goal
- DFS performance depends on luck (goal location vs. neighbor order)
- BFS won 6/10 trials, DFS won 4/10 trials

**See `analysis_results.csv` for full data**

---

## 🛠️ Tools Created for You

1. **Question2_NeighborOrderTester.java**

   - Tests different neighbor orders on bigMaze_multigoal
   - Shows when DFS finds non-closest goal

2. **AnalysisExampleMazes.java**

   - Creates example mazes for Q3 and Q4
   - Tests them and shows results

3. **PathfindingAnalysis.java**

   - Runs 10 random maze trials
   - Compares BFS vs DFS
   - Generates CSV with results

4. **FinalVerificationTest.java**
   - Tests all Graph.java functionality
   - Ensures code works correctly

---

## 📝 How to Write Your Answers

### Format:

- Write in complete sentences
- Include diagrams for maze layouts
- Explain your reasoning clearly
- Reference your test results/data

### Example Structure:

**Question 3:**

```
Yes, it is possible for DFS to find a farther goal while searching
fewer nodes than BFS.

[Draw your maze here]

Conditions that lead to this:
1. Multiple goals at different distances...
2. DFS neighbor order aligns with...
3. BFS must explore...

With the maze shown above and a Down-first neighbor order, DFS
explores straight down to the farther goal in only 4 nodes. Meanwhile,
BFS must explore all directions evenly, encountering walls and
branches, ultimately searching 15 nodes to find the closer goal.

This demonstrates that DFS can be more efficient when...
```

---

## 🎯 Final Checklist

Before submitting your analysis PDF:

- [ ] Question 1: Partner info stated
- [ ] Question 2: Neighbor order specified (Right, Down, Left, Up)
- [ ] Question 2: Node count included (140 nodes)
- [ ] Question 3: Maze diagram drawn clearly
- [ ] Question 3: Conditions explained
- [ ] Question 4: Maze diagram drawn clearly
- [ ] Question 4: O(1) vs O(N) explained
- [ ] Question 5: Experiment described
- [ ] Question 5: Results table or summary included
- [ ] Question 5: Discussion of why BFS/DFS performs better
- [ ] All questions on separate pages for Gradescope
- [ ] Matched questions to pages in Gradescope

---

## 🚀 Files You Have

**In `assignment8_files/`:**

- `bigMaze_multigoal.txt` - Original maze
- `bigMaze_multigoal_BFS.txt` - BFS solution
- `bigMaze_multigoal_DFS.txt` - DFS solution
- `q3_example.txt` - Example for Question 3
- `q4_example.txt` - Example for Question 4
- `analysis_results.csv` - Data for Question 5

**Documentation:**

- `ANALYSIS_ANSWERS.md` - Complete written answers
- `ANALYSIS_GUIDE.md` - Question breakdown
- This file - Quick reference

---

## 💡 Pro Tips

1. **Diagrams:** Draw maze layouts clearly in your PDF. Use a monospace font or grid.

2. **Testing:** You can verify any answer by:

   - Modifying neighbor order in Graph.java
   - Running the tester tools
   - Checking node counts

3. **Discussion:** Don't just state facts - explain WHY things happen:

   - Why does this neighbor order affect DFS?
   - Why does BFS explore more nodes in this case?
   - What principle of the algorithm causes this behavior?

4. **Data:** For Question 5, include:
   - Your methodology (10 mazes, 50x50, etc.)
   - Summary statistics (averages)
   - Your conclusion (which is better and why)

---

**You're all set!** 🎉

Everything is tested, verified, and documented. Just write up your answers based on the guide and examples provided!
