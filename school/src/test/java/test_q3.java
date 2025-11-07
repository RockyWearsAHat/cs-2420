import assign08.Graph;
public class test_q3 {
    public static void main(String[] args) {
        try {
            System.out.println("Testing simple 7x7 maze:");
            System.out.println("Goal 1: Above start (blocked by wall)");
            System.out.println("Goal 2: Below start (open path)");
            System.out.println();
            
            Graph gBFS = new Graph("assignment8_files/q3_simple.txt");
            int bfsPath = gBFS.CalculateShortestPath();
            int bfsNodes = gBFS.getNodesSearched();
            gBFS.printGraph("assignment8_files/q3_simple_BFS.txt");
            System.out.println("BFS: path=" + bfsPath + ", nodes=" + bfsNodes);
            
            Graph gDFS = new Graph("assignment8_files/q3_simple.txt");
            int dfsPath = gDFS.CalculateAPath();
            int dfsNodes = gDFS.getNodesSearched();
            gDFS.printGraph("assignment8_files/q3_simple_DFS.txt");
            System.out.println("DFS: path=" + dfsPath + ", nodes=" + dfsNodes);
            
            if (dfsNodes < bfsNodes) {
                System.out.println("\n✓ DFS searched " + (bfsNodes - dfsNodes) + " fewer nodes!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
