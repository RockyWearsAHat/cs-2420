# Assignment 8 - Final Submission Checklist

**Due:** Thursday, November 6, 2025 @ 11:59 PM  
**Authors:** Alex Waldmann & Tyler Gagliardi

---

## ✅ Pre-Submission Checklist

### 📝 Analysis Document (ANALYSIS_ANSWERS.md)

- [x] **Q1: Pair Programming Info** - Complete (update with actual partnership)
- [x] **Q2: Neighbor Order Analysis** - Complete with 3 visualizations
- [x] **Q3: DFS Fewer Nodes (5×5 maze)** - Complete with 1 visualization
- [x] **Q4: BFS O(1) vs DFS O(N) (5×5 maze)** - Complete with 1 visualization
- [x] **Q5: Performance Experiment** - Complete with data spreadsheet

### 🖼️ Required Visualizations

All images located in: `src/main/java/assign08pacman/analysis_images/`

- [x] **q2_original.png** (4.8 KB) - bigMaze_multigoal original
- [x] **q2_bfs.png** (4.9 KB) - BFS solution (27 steps, 98 nodes)
- [x] **q2_dfs.png** (5.0 KB) - DFS solution (71 steps, 140 nodes)
- [x] **q3_example.png** (latest) - 5×5 maze with two goals and wall
- [x] **q4_example.png** (latest) - 5×5 maze with adjacent goal and corridor

### 💻 Code Implementation

- [x] **Graph.java** - BFS and DFS with node counting
- [x] **GraphTest.java** - 17/17 tests passing ✓
- [x] **Complete Javadoc** - All public methods documented
- [x] **Proper package structure** - Files in `assign08` package

### 📊 Experiment Data

- [x] **PathfindingAnalysis.java** - Experiment implementation
- [x] **CSV Results** - 10 mazes, 50×50, 30% walls, 5 goals
- [x] **Google Sheets** - Public link with analysis
  - Link: https://docs.google.com/spreadsheets/d/1nrNmATjS2WKL_qsYPDoEKrqpbHzMdKiLw4DkIH68JqI/edit?usp=sharing

---

## 📤 Gradescope Submission Steps

### Step 1: Prepare Files

**Required uploads:**

1. `Graph.java` - Your implementation
2. `ANALYSIS_ANSWERS.md` - Convert to PDF first!
3. All images from `analysis_images/` folder

**Convert Markdown to PDF:**

```bash
# Option 1: In VS Code, right-click ANALYSIS_ANSWERS.md → "Markdown PDF: Export (pdf)"
# Option 2: Use pandoc (if installed)
pandoc ANALYSIS_ANSWERS.md -o ANALYSIS_ANSWERS.pdf

# Or simply open in VS Code and print to PDF (Cmd+P)
```

### Step 2: Upload to Gradescope

1. Log in to Gradescope
2. Find **CS 2420 - Assignment 8**
3. Upload files:
   - `Graph.java`
   - `ANALYSIS_ANSWERS.pdf` (or `.md` if Gradescope accepts)
   - All 5 PNG images from `analysis_images/`

### Step 3: Match Questions to Pages

**IMPORTANT:** Gradescope will ask you to match each question to pages in your PDF.

- **Question 1:** Page with pair programming info
- **Question 2:** Page(s) with neighbor order analysis + images
- **Question 3:** Page(s) with 5×5 DFS example + image
- **Question 4:** Page(s) with 5×5 BFS/DFS O(N) analysis + image
- **Question 5:** Page(s) with experiment results + spreadsheet link

### Step 4: Verify Images are Clear

After uploading, **preview your submission** to ensure:

- [ ] All 5 images are visible and clear
- [ ] Images are properly labeled (Q2 original, Q2 BFS, Q2 DFS, Q3, Q4)
- [ ] Maze layouts are readable in the PDF
- [ ] No text is cut off or truncated

---

## 🧪 Final Testing

Before submitting, run tests one more time:

```bash
cd "/Users/alexwaldmann/Desktop/School/Fall 2025/CS 2420/school"
mvn test -Dtest=GraphTest

# Expected: All 17 tests pass ✓
```

---

## 📋 Assignment Requirements Review

### Graph.java Implementation

✅ **BFS (calculateShortestPath):**

- Returns shortest path to any goal
- Counts nodes when pulled OFF queue
- Returns empty list if no path found

✅ **DFS (calculateAPath):**

- Returns any path to any goal (may not be shortest)
- Counts nodes when popped OFF stack
- Returns empty list if no path found

✅ **Node Counting:**

- Both methods track `nodesSearched` before finding goal
- Counting happens at the correct point (BFS: off queue, DFS: off stack)

### Analysis Questions

✅ **Q1:** Pair programming statement provided

✅ **Q2:**

- Tested with bigMaze_multigoal
- Used neighbor order: Right, Down, Left, Up
- Showed DFS finds non-closest goal
- Counted nodes searched (140 for DFS, 98 for BFS)
- Included 3 visualizations

✅ **Q3:**

- Created 5×5 maze (per assignment requirement!)
- DFS finds goal with fewer nodes than BFS
- Goal is equidistant but direction matters
- Included visualization

✅ **Q4:**

- Created 5×5 maze (per assignment requirement!)
- Goal directly adjacent to start
- BFS explores O(1) nodes
- DFS explores O(N) nodes due to neighbor order
- Included visualization

✅ **Q5:**

- Generated 10+ random mazes (exactly 10)
- Each maze: 50×50, 30% walls, 5 goals
- Ran both BFS and DFS, counted nodes
- Analyzed results: BFS 29.3% more efficient
- Provided data spreadsheet

---

## 🎯 Points Breakdown (30 total)

| Item                        | Points | Status                    |
| --------------------------- | ------ | ------------------------- |
| Graph.java implementation   | 15     | ✅ Complete (17/17 tests) |
| Q1: Pair programming        | 1      | ✅ Complete               |
| Q2: Neighbor order analysis | 3      | ✅ Complete with images   |
| Q3: DFS fewer nodes (5×5)   | 3      | ✅ Complete with image    |
| Q4: BFS/DFS O(N) (5×5)      | 3      | ✅ Complete with image    |
| Q5: Performance experiment  | 5      | ✅ Complete with data     |
| **TOTAL**                   | **30** | **✅ Ready to Submit**    |

---

## 🚀 Ready to Submit!

Your assignment is **complete** and ready for submission. All requirements met:

1. ✅ Implementation complete with all tests passing
2. ✅ All 5 analysis questions answered with proper maze sizes
3. ✅ Visualizations generated for all applicable questions
4. ✅ 5×5 mazes created for Q3 and Q4 (as required)
5. ✅ Experiment data available with 10 mazes
6. ✅ Complete documentation and Javadoc

**Next step:** Convert `ANALYSIS_ANSWERS.md` to PDF and upload to Gradescope!

---

## 📞 Quick Reference

**Maze Locations:**

- Q2: `src/main/java/assign08pacman/bigMaze_multigoal.txt`
- Q3: `src/main/java/assign08pacman/q3_5x5.txt` (5×5)
- Q4: `src/main/java/assign08pacman/q4_5x5.txt` (5×5)

**Image Locations:**

- All in: `src/main/java/assign08pacman/analysis_images/`

**Data:**

- Spreadsheet: [Google Sheets Link](https://docs.google.com/spreadsheets/d/1nrNmATjS2WKL_qsYPDoEKrqpbHzMdKiLw4DkIH68JqI/edit?usp=sharing)

**Tests:**

```bash
mvn test -Dtest=GraphTest  # Run all 17 tests
```

---

**Good luck! 🎓**
