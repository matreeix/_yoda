package algorithm.IA.AStar;

import java.util.*;

/**
 * A*搜索算法
 */
public class AStarSearch {
    static class Node implements Comparable<Node> {
        int x, y;
        double gCost;    // 从起点到当前节点的代价
        double hCost;    // 从当前节点到终点的启发式估计代价
        double fCost;    // fCost = gCost + hCost
        Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fCost, other.fCost);
        }
    }

    private int[][] grid;           // 网格地图，0表示可通行，1表示障碍
    private int startX, startY;     // 起点坐标
    private int goalX, goalY;       // 终点坐标
    private int rows, cols;

    public AStarSearch(int[][] grid, int startX, int startY, int goalX, int goalY) {
        this.grid = grid;
        this.startX = startX;
        this.startY = startY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.rows = grid.length;
        this.cols = grid[0].length;
    }

    /**
     * 执行A*搜索
     */
    public List<Node> search() {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<String> closedSet = new HashSet<>();

        Node startNode = new Node(startX, startY);
        startNode.gCost = 0;
        startNode.hCost = heuristic(startX, startY);
        startNode.fCost = startNode.gCost + startNode.hCost;

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            String currentKey = current.x + "," + current.y;

            if (current.x == goalX && current.y == goalY) {
                return reconstructPath(current);
            }

            closedSet.add(currentKey);

            // 检查邻居节点
            for (int[] direction : new int[][]{{0,1}, {1,0}, {0,-1}, {-1,0}}) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];
                String neighborKey = newX + "," + newY;

                if (isValid(newX, newY) && !closedSet.contains(neighborKey)) {
                    double tentativeGCost = current.gCost + 1; // 假设每步代价为1

                    Node neighbor = new Node(newX, newY);
                    neighbor.gCost = tentativeGCost;
                    neighbor.hCost = heuristic(newX, newY);
                    neighbor.fCost = neighbor.gCost + neighbor.hCost;
                    neighbor.parent = current;

                    openSet.add(neighbor);
                }
            }
        }

        return null; // 没有找到路径
    }

    /**
     * 曼哈顿距离启发函数
     */
    private double heuristic(int x, int y) {
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }

    /**
     * 检查节点是否有效
     */
    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y] == 0;
    }

    /**
     * 重建路径
     */
    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * 打印网格和路径
     */
    public void printGridWithPath(List<Node> path) {
        char[][] display = new char[rows][cols];

        // 初始化显示网格
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                display[i][j] = grid[i][j] == 0 ? '.' : '#';
            }
        }

        // 标记路径
        if (path != null) {
            for (Node node : path) {
                if ((node.x == startX && node.y == startY) ||
                    (node.x == goalX && node.y == goalY)) {
                    display[node.x][node.y] = 'S';
                } else {
                    display[node.x][node.y] = '*';
                }
            }
            display[goalX][goalY] = 'G';
        }

        // 打印网格
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(display[i][j] + " ");
            }
            System.out.println();
        }
    }
}
