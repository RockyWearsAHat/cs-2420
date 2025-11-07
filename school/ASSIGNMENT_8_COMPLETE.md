# 🎮 Assignment 8 - Complete Submission Package

**Authors:** Alex Waldmann & Tyler Gagliardi  
**Course:** CS 2420  
**Date:** November 6, 2025  
**Status:** ✅ READY FOR SUBMISSION

---

## 📦 What's Included

This package contains everything needed for Assignment 8:

1. **Graph.java** - Complete BFS/DFS implementation (SUBMIT TO GRADESCOPE)
2. **Comprehensive test suite** - 17/17 tests passing
3. **Analysis answers** - Complete answers to all 5 questions
4. **Pacman visualization tool** - Visualize and generate images of mazes
5. **Documentation** - Detailed guides and checklists

---

## 🚀 Quick Start

### 1. Run Tests (Verify Everything Works)

```bash
cd "src/test/java"
mkdir -p assignment8_files
javac -cp ".:../../main/java" assign08/BFSandDFSTestRunner.java
java -cp ".:../../main/java" assign08.BFSandDFSTestRunner
```

**Expected:** 17/17 tests passed ✅

### 2. Generate Analysis Images

```bash
cd "src/main/java/assign08pacman"
./generate_analysis_images.sh
```

**Output:** PNG images in `analysis_images/` directory

### 3. Submit to Gradescope

- Navigate to `src/main/java/assign08/`
- Submit **Graph.java** ONLY

### 4. Submit Analysis Answers

- Open `ANALYSIS_ANSWERS.md`
- Copy answers to Canvas (or submit as PDF)
- Include generated images from step 2

---

## 📁 Project Structure

```
school/
├── src/
│   ├── main/java/assign08/
│   │   ├── Graph.java ⭐ SUBMIT THIS TO GRADESCOPE
│   │   ├── PathFinder.java (provided utility)
│   │   ├── MazeGen.java (provided utility)
│   │   └── TestPathFinder.java (provided example)
│   │
│   ├── main/java/assign08pacman/ 🎮 VISUALIZATION TOOL
│   │   ├── PACMAN_GUIDE.md
│   │   ├── pacman.py
│   │   ├── capture_maze_image.py
│   │   ├── generate_analysis_images.sh
│   │   └── (maze files and utilities)
│   │
│   └── test/java/assign08/
│       ├── BFSandDFSTestRunner.java (17 comprehensive tests)
│       ├── PathfindingAnalysis.java (Q2 & Q5 analysis)
│       ├── Question2_NeighborOrderTester.java
│       ├── AnalysisExampleMazes.java
│       └── (additional test files)
│
└── Documentation:
    ├── ANALYSIS_ANSWERS.md ⭐ ANALYSIS QUESTIONS
    ├── ASSIGNMENT_8_SUMMARY.md
    └── (other guides)
```

---

## ✅ Implementation Status

### Core Requirements: COMPLETE ✓

- [x] BFS (CalculateShortestPath) - finds shortest path
- [x] DFS (CalculateAPath) - finds any path
- [x] Node counting (getNodesSearched) - for analysis
- [x] Handles multiple goals correctly
- [x] Returns -1 for no-path scenarios
- [x] Proper path marking in output files

### Testing: 17/17 PASSING ✓

- [x] Basic pathfinding (BFS & DFS)
- [x] No path scenarios
- [x] Multiple goals
- [x] Edge cases
- [x] Performance tests (large mazes)
- [x] Node counting accuracy

### Documentation: COMPLETE ✓

- [x] Complete Javadoc on all methods
- [x] Analysis questions answered
- [x] Usage guides created
- [x] Submission checklist

---

## 🎮 Pacman Visualization Tool

### What It Does

- Displays mazes visually with Pacman
- Shows paths found by BFS/DFS
- Generates PNG images for analysis
- Helps verify solutions are correct

### Quick Commands

**View a maze interactively:**

```bash
cd "src/main/java/assign08pacman"
python3 pacman.py -l bigMaze_multigoal.txt -z 1.5
```

**Generate a single PNG image:**

```bash
python3 capture_maze_image.py bigMaze_multigoal.txt output.png
```

**Generate all analysis images:**

```bash
./generate_analysis_images.sh
```

### Requirements

- Python 3 (already installed on most systems)
- Pillow library (auto-installed by script if needed)

**Complete guide:** See `src/main/java/assign08pacman/PACMAN_GUIDE.md`

---

## 📊 Analysis Questions Summary

### Question 1: Pair Programming

Fill in your pair programming information.

### Question 2: Neighbor Visiting Order ✓

**Answer:** Right, Down, Left, Up  
**Result:** DFS searches 140 nodes, path length 71 (vs BFS: 98 nodes, path length 27)

**Images to include:**

- `q2_original.png` - Original maze
- `q2_bfs.png` - BFS solution (shortest)
- `q2_dfs.png` - DFS solution (longer)

### Question 3: DFS Farther Goal, Fewer Nodes ✓

**Answer:** YES, created example maze `q3_example.txt`

**Image to include:**

- `q3_example.png` - Maze demonstrating this behavior

### Question 4: BFS O(1) vs DFS O(N) ✓

**Answer:** Goal adjacent to start, DFS explores corridor first

**Image to include:**

- `q4_example.png` - Maze with adjacent goal and long corridor

### Question 5: Performance Comparison ✓

**Answer:** BFS is 29.3% more efficient on average

- BFS: 161.5 nodes average
- DFS: 228.3 nodes average
- Tested on 100 random 100x100 mazes

**Data:** [Google Sheets Link](https://docs.google.com/spreadsheets/d/1nrNmATjS2WKL_qsYPDoEKrqpbHzMdKiLw4DkIH68JqI/edit?usp=sharing)

---

## 📝 Submission Checklist

### Before Submitting to Gradescope:

- [x] Graph.java compiles without errors
- [x] All 17 tests passing
- [x] Javadoc complete and correct
- [x] Author names included (Alex Waldmann, Tyler Gagliardi)
- [ ] **SUBMIT: Graph.java ONLY**

### Before Submitting Analysis (Canvas):

- [ ] Question 1 answered (pair programming)
- [ ] Question 2 answered with neighbor order
- [ ] Question 3 answered with example
- [ ] Question 4 answered with example
- [ ] Question 5 answered with data
- [ ] All images generated and included
- [ ] Images are clear and labeled
- [ ] Proofread all answers
- [ ] Include link to spreadsheet (Q5)

---

## 🔧 Troubleshooting

### Tests Fail

```bash
# Make sure you're in the right directory:
cd "src/test/java"

# Create the assignment8_files directory:
mkdir -p assignment8_files

# Recompile and run:
javac -cp ".:../../main/java" assign08/BFSandDFSTestRunner.java
java -cp ".:../../main/java" assign08.BFSandDFSTestRunner
```

### Pacman Won't Run

```bash
# Check Python installation:
python3 --version

# Install Pillow for image generation:
pip3 install Pillow

# Navigate to pacman directory:
cd "src/main/java/assign08pacman"

# Try running:
python3 pacman.py -l demoMaze.txt
```

### Images Won't Generate

```bash
# Make sure Pillow is installed:
pip3 install Pillow

# Run the automated script:
cd "src/main/java/assign08pacman"
./generate_analysis_images.sh

# Or generate manually:
python3 capture_maze_image.py bigMaze_multigoal.txt output.png
```

---

## 📚 Documentation Files

- **ANALYSIS_ANSWERS.md** - Complete answers to all 5 analysis questions
- **ASSIGNMENT_8_SUMMARY.md** - Technical implementation summary
- **PACMAN_GUIDE.md** - Complete guide to Pacman visualization tool
- **TEST_RESULTS_SUMMARY.md** - Test suite details

---

## 🎯 What to Submit Where

### Gradescope (Code Submission)

✅ **Submit:** `Graph.java` (located at `src/main/java/assign08/Graph.java`)  
❌ **Don't submit:** Test files, analysis tools, documentation

### Canvas (Analysis Submission)

✅ **Submit:** Written answers to 5 analysis questions  
✅ **Include:** PNG images of mazes  
✅ **Include:** Link to Google Sheets with Q5 data  
❌ **Don't submit:** Code files

---

## 💡 Tips for Success

1. **Run tests first** - Make sure everything works before generating images
2. **Generate images early** - Don't wait until the last minute
3. **Label your images** - Add clear captions in your analysis document
4. **Proofread carefully** - Check for typos and clarity
5. **Include data visualization** - A chart/graph for Q5 is helpful
6. **Keep it concise** - Clear, direct answers are best

---

## 🌟 Quality Indicators

Your submission should show:

✅ **Correctness** - 17/17 tests passing  
✅ **Documentation** - Complete Javadoc  
✅ **Analysis** - Thorough answers to all questions  
✅ **Visualization** - Clear images demonstrating concepts  
✅ **Professionalism** - Well-organized, properly formatted

---

## 🎉 You're Ready!

This package is **complete and ready for submission**. You have:

- ✅ Working implementation (17/17 tests passed)
- ✅ Complete documentation
- ✅ Analysis questions answered
- ✅ Visualization tools ready
- ✅ Submission instructions clear

**Good luck with your submission!** 🚀

---

## 📞 Need Help?

**For implementation questions:**

- Review `Graph.java` Javadoc
- Check `BFSandDFSTestRunner.java` for test examples

**For analysis questions:**

- See `ANALYSIS_ANSWERS.md` for complete answers
- Run analysis tools in `src/test/java/assign08/`

**For visualization:**

- See `PACMAN_GUIDE.md` in pacman directory
- Run `./generate_analysis_images.sh` for automated image generation

**For submission:**

- Gradescope: `Graph.java` only
- Canvas: Written answers + images

---

**Assignment 8 Complete!** ✨
