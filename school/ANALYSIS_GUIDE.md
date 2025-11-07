# Assignment 8 Analysis - Question-by-Question Guide

## Question 1: Pair Programming Info

**Simple answer:** Just state if you worked alone or with a partner.

Example answer:

- "I am using a pair programming exception on this assignment."
  OR
- "My programming partner is [First Last]. I submitted the program files to Gradescope."

---

## Question 2: DFS Neighbor Order on bigMaze_multigoal

**What you need to do:**

1. You need to find/create the `bigMaze_multigoal.txt` file
2. Test different neighbor visiting orders in the `neighbors()` method
3. Find an order where DFS finds a LONGER path than BFS (meaning it found a farther goal)
4. Report the order and how many nodes DFS searched

**Current neighbor order in Graph.java:**

```java
Up, Left, Down, Right
```

**How to change it:**
Edit the `neighbors()` method in Graph.java and reorder these four lines:

```java
addIfOpen(out, r - 1, c); // Up
addIfOpen(out, r, c - 1); // Left
addIfOpen(out, r + 1, c); // Down
addIfOpen(out, r, c + 1); // Right
```

**Try these orders:**

- Down, Right, Up, Left
- Right, Down, Left, Up
- Left, Down, Right, Up

**What to report:**

- "The neighbor visiting order [order] resulted in DFS finding a non-closest goal"
- "DFS searched [X] nodes to find this path"
- Include the path lengths: BFS found path of length [Y], DFS found path of length [Z]

---

## Question 3: Can DFS Search Fewer Nodes Than BFS?

**Answer: YES, it's possible!**

**Conditions needed:**

1. **Multiple goals in the maze**
2. **DFS neighbor order aligns with the path to a farther goal**
3. **BFS must explore many directions before finding closest goal**
4. **Layout:** The farther goal is in DFS's "preferred" direction, while the closest goal requires BFS to explore many branches

**Example 5x5 maze:**

```
XXXXX
XS  X
X X X
X X X
XG GX
XXXXX
```

- Start: (1,1)
- Goal 1: (4,1) - closer, distance ~3
- Goal 2: (4,3) - farther, distance ~5
- If DFS visits Down first, it goes straight down to Goal 1 with minimal exploration
- BFS must explore all directions evenly, visiting many nodes before finding Goal 1

**What to write:**

- Explain the conditions (DFS direction, maze layout, goal positions)
- Draw your 5x5 maze clearly
- Explain why DFS searches fewer nodes even though it finds a farther goal

---

## Question 4: BFS O(1) vs DFS O(N)

**Conditions:**

1. **Goal is immediately adjacent to Start** (BFS finds in 1-2 nodes)
2. **DFS neighbor order goes AWAY from the goal**
3. **Long corridor in DFS's direction** (forces DFS to explore entire path before backtracking)

**Example 5x5 maze:**

```
XXXXX
XSGXX
X   X
X   X
XXXXX
```

- Start: (1,1)
- Goal: (1,2) - right next to start
- If BFS visits: BFS finds goal in 2 nodes (start + goal)
- If DFS visits Down first: DFS goes all the way down, then must backtrack up

**Better example with longer corridor:**

```
XXXXXXX
XS    X
XXXXXXX
XG    X
XXXXXXX
```

- BFS: O(1) - finds goal in 2 nodes
- DFS with Right-first order: explores entire right corridor before backtracking to find goal

**What to write:**

- Explain: Goal adjacent to start (BFS finds immediately)
- Explain: DFS neighbor order leads away from goal
- Explain: Long path before DFS can backtrack
- Draw your maze clearly

---

## Question 5: Experiment Comparing BFS vs DFS

**Good news: I already created the tool and ran it for you!**

Your `PathfindingAnalysis.java` already did this experiment:

- 10 random mazes, 50x50, 30% walls, 5 goals
- Results: BFS searched 161.5 nodes on average, DFS searched 228.3 nodes
- BFS was 29.3% more efficient

**What to write:**

**Experiment Design:**
"I generated 10 random mazes of size 50x50 with 30% wall density and 5 goals using the provided MazeGen utility. For each maze, I ran both BFS and DFS pathfinding algorithms and counted the number of nodes searched (nodes pulled off the queue for BFS, nodes popped off the stack for DFS). The results were recorded in a CSV file for analysis."

**Results:**
"BFS searched an average of [X] nodes across the 10 trials, while DFS searched an average of [Y] nodes. BFS found the path with fewer nodes searched in [Z] out of 10 trials."

**Discussion:**
"BFS generally requires searching fewer nodes because it explores nodes level-by-level, guaranteeing it finds the shortest path to the closest goal. DFS may explore deeper paths first before finding any goal, leading to more nodes being searched on average. However, DFS can occasionally outperform BFS when the goal happens to be in the direction DFS explores first.

The results show that BFS is more efficient on average for finding paths in random mazes with multiple goals. BFS's guarantee of finding the shortest path means it naturally searches fewer nodes in most cases. DFS's performance is more variable and depends heavily on the relationship between the neighbor visiting order and the actual goal locations."

---

## Summary of What You Need to Do:

1. ✓ **Question 1:** Just write who your partner is (or if working alone)

2. **Question 2:**

   - Need to get/create bigMaze_multigoal.txt
   - Test different neighbor orders
   - Find one where DFS path > BFS path
   - Report the order and node count

3. **Question 3:**

   - Draw a 5x5 maze
   - Explain how DFS can search fewer nodes while finding farther goal
   - Key: DFS goes straight to farther goal, BFS explores many directions

4. **Question 4:**

   - Draw a 5x5 maze with goal adjacent to start
   - Explain how BFS finds it immediately (O(1))
   - Explain how DFS explores away first (O(N))

5. ✓ **Question 5:** Already done! Just write up the results from PathfindingAnalysis

Let me help you with Question 2 specifically...
