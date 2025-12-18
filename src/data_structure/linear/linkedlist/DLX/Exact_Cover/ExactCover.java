package data_structure.linear.linkedlist.DLX.Exact_Cover;

/**
 * @Description: 精确覆盖问题（Exact Cover）——DLX 模版
 * 精确覆盖的定义：一个全集 S 有若干个子集 S1, S2, ……, Sn，选取其中若干个子集，使得这些集合中出现了 S 中每个元素各一次。
 * 举个例子：全集 S = {1,2,3,4,5,6,7}，子集
 * S1 = {1,2,3}，S2 = {3,4,5,7}，S3 = {4,5,6,7}，S4 = {1,5,6,7}，S5 = {4,5}，
 * 其中选取 S1 和 S3 即为精确覆盖。
 *
 * @linked： https://www.acwing.com/solution/acwing/content/3843/
 * @linked： https://www.luogu.com.cn/blog/ONE-PIECE/qian-tan-dlx
 * @Date: 2021/12/23
 */
public class ExactCover {

    /**
     * 精确覆盖问题（Exact Cover）——DLX 模板（Algorithm X + Dancing Links）
     *
     * 使用说明（典型流程，适合作为模板直接复制使用）：
     * 1. 准备一个 0/1 矩阵 matrix，matrix[row][col] == 1 表示第 row 行选择包含第 col 列这个约束。
     * 2. 调用 {@link #solve(int[][])}，返回所有解；每个解是一组“被选中的行下标”。
     *
     * 约定：
     * - 行：一个“选择”（例如“在格子 (i,j) 填数字 d”）
     * - 列：一个“约束”（例如“第 i 行数字 d 恰好出现一次”）
     *
     * 该类实现的是纯模板，不绑定具体题意，只负责：
     *   0/1 矩阵  ->  构建 DLX 十字链表  ->  回溯搜索（Algorithm X） ->  返回所有解
     */

    /**
     * 内部节点类：十字双向链表节点
     *
     * 每个 1 对应一个节点，节点通过 L/R/U/D 四个指针在行、列上互相连接。
     * 列头节点的 column 字段指向自己，普通节点的 column 指向所在列的列头。
     */
    private static class Node {
        Node left, right, up, down;
        ColumnNode column;   // 所属列头
        int rowIndex;        // 对应原矩阵的行号（方便回溯输出）

        Node() {
            this.left = this.right = this.up = this.down = this;
        }

        Node(ColumnNode column, int rowIndex) {
            this();
            this.column = column;
            this.rowIndex = rowIndex;
        }
    }

    /**
     * 列头节点：在 Node 的基础上，多了 size 和 name。
     * size 表示该列当前剩余的节点数量（用于启发式选择“最少 1 的列”）。
     */
    private static class ColumnNode extends Node {
        int size;     // 该列中节点数量
        String name;  // 可选：列名（调试/打印用）

        ColumnNode(String name) {
            super();
            this.column = this;
            this.name = name;
            this.size = 0;
        }
    }

    // DLX 根结点（头结点），所有列头通过 root.left/right 串成一个循环链表
    private ColumnNode root;

    // 暂存当前递归路径上的行（节点），用于输出一个解
    private final java.util.List<Node> solution = new java.util.ArrayList<>();

    // 存所有解。每个解是一组“行下标”的 list
    private final java.util.List<java.util.List<Integer>> allSolutions = new java.util.ArrayList<>();

    /**
     * 对外主接口：给定 0/1 矩阵，返回所有精确覆盖解。
     *
     * @param matrix 二维 0/1 数组，matrix[r][c] ∈ {0,1}
     * @return 所有解；每个解是一个整数列表，保存被选中的行下标。
     */
    public java.util.List<java.util.List<Integer>> solve(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return java.util.Collections.emptyList();
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        buildDLX(matrix, rows, cols);
        search();

        return allSolutions;
    }

    /**
     * 构建 DLX 十字链表。
     *
     * root <-> C0 <-> C1 <-> ... <-> C(cols-1)
     * 每个 matrix[r][c] == 1 会在列 Cc 下挂一个节点。
     */
    private void buildDLX(int[][] matrix, int rows, int cols) {
        // 1. 创建根节点和所有列头
        root = new ColumnNode("root");

        ColumnNode[] columnNodes = new ColumnNode[cols];
        ColumnNode prev = root;
        for (int c = 0; c < cols; c++) {
            ColumnNode col = new ColumnNode("C" + c);
            columnNodes[c] = col;

            // 列头与列头之间左右相连
            col.left = prev;
            col.right = root;
            prev.right = col;
            root.left = col;

            // 上下初始化为自己
            col.up = col.down = col;

            prev = col;
        }

        // 2. 对每一行，建立行内节点并插入到对应的列中
        for (int r = 0; r < rows; r++) {
            Node firstInRow = null; // 该行的第一个节点（用于行内循环）

            for (int c = 0; c < cols; c++) {
                if (matrix[r][c] == 1) {
                    ColumnNode col = columnNodes[c];
                    Node newNode = new Node(col, r);

                    // 垂直方向：把 newNode 插入到列底部（col.up 与 col 之间）
                    newNode.down = col;
                    newNode.up = col.up;
                    col.up.down = newNode;
                    col.up = newNode;
                    col.size++;

                    // 水平方向：把本行的所有 1 连接成循环双向链表
                    if (firstInRow == null) {
                        firstInRow = newNode;
                        newNode.left = newNode.right = newNode;
                    } else {
                        newNode.right = firstInRow;
                        newNode.left = firstInRow.left;
                        firstInRow.left.right = newNode;
                        firstInRow.left = newNode;
                    }
                }
            }
        }
    }

    /**
     * Algorithm X 的递归搜索主体（无显式参数版本，使用成员变量保存结果）。
     */
    private void search() {
        // 如果 root 的右指针指向自己，说明没有剩余列了 => 找到一个完整解
        if (root.right == root) {
            java.util.List<Integer> oneSolution = new java.util.ArrayList<>(solution.size());
            for (Node node : solution) {
                oneSolution.add(node.rowIndex);
            }
            allSolutions.add(oneSolution);
            return;
        }

        // 1. 选择一个列（启发式：选择 size 最小的列可以显著剪枝）
        ColumnNode column = chooseColumn();
        if (column == null) {
            return;
        }

        // 2. 覆盖该列
        cover(column);

        // 3. 枚举该列中的每一行（即该列中的每一个节点 node 所在的行）
        for (Node rowNode = column.down; rowNode != column; rowNode = rowNode.down) {
            // 选中这一行
            solution.add(rowNode);

            // 将该行中其它列也一并覆盖（因为一旦选中这一行，这些列的约束也被满足了）
            for (Node rightNode = rowNode.right; rightNode != rowNode; rightNode = rightNode.right) {
                cover(rightNode.column);
            }

            // 递归
            search();

            // 回溯：撤销对该行的选择
            // 自右向左依次撤销 cover
            for (Node leftNode = rowNode.left; leftNode != rowNode; leftNode = leftNode.left) {
                uncover(leftNode.column);
            }

            solution.remove(solution.size() - 1);
        }

        // 4. 恢复当前列
        uncover(column);
    }

    /**
     * 选择 size 最小的列（Knuth 建议的启发式，有助于减少搜索树规模）。
     */
    private ColumnNode chooseColumn() {
        ColumnNode best = null;
        int minSize = Integer.MAX_VALUE;

        for (Node c = root.right; c != root; c = c.right) {
            ColumnNode col = (ColumnNode) c;
            if (col.size < minSize) {
                minSize = col.size;
                best = col;
                if (minSize == 0) {
                    // 有列已经没有任何节点了，不可能有解，直接剪枝。
                    break;
                }
            }
        }
        return best;
    }

    /**
     * cover 操作：从列链表中移除 column，并对该列下方所有行，移除它们在其它列中的节点。
     *
     * 这是 DLX 的核心操作之一。
     */
    private void cover(ColumnNode column) {
        // 1. 从列链表中移除 column
        column.right.left = column.left;
        column.left.right = column.right;

        // 2. 遍历该列下方的每一行，将这些行在其它列中的节点从各自列中移除
        for (Node row = column.down; row != column; row = row.down) {
            for (Node rightNode = row.right; rightNode != row; rightNode = rightNode.right) {
                rightNode.down.up = rightNode.up;
                rightNode.up.down = rightNode.down;
                rightNode.column.size--;
            }
        }
    }

    /**
     * uncover 操作：与 cover 完全相反，用于在回溯时恢复链表结构。
     *
     * 实现顺序需与 cover 中的循环顺序严格相反，才能正确恢复。
     */
    private void uncover(ColumnNode column) {
        // 1. 反向遍历该列下方的每一行，恢复这些行在其它列中的节点
        for (Node row = column.up; row != column; row = row.up) {
            for (Node leftNode = row.left; leftNode != row; leftNode = leftNode.left) {
                leftNode.column.size++;
                leftNode.down.up = leftNode;
                leftNode.up.down = leftNode;
            }
        }

        // 2. 将 column 自身重新插回列链表
        column.right.left = column;
        column.left.right = column;
    }

    // ==========================
    // 演示用 main（可选）
    // ==========================

    /**
     * 简单示例：
     * 精确覆盖的经典小例子：
     * S = {1,2,3,4,5,6,7}
     * S1 = {1,2,3}
     * S2 = {3,4,5,7}
     * S3 = {4,5,6,7}
     * S4 = {1,5,6,7}
     * S5 = {4,5}
     *
     * 0/1 矩阵按列 {1,2,3,4,5,6,7} 排列，行分别是 S1..S5
     * 期望解：选择 S1 + S3。
     */
    public static void main(String[] args) {
        int[][] matrix = {
                // 1 2 3 4 5 6 7
                {1, 1, 1, 0, 0, 0, 0}, // S1
                {0, 0, 1, 1, 1, 0, 1}, // S2
                {0, 0, 0, 1, 1, 1, 1}, // S3
                {1, 0, 0, 0, 1, 1, 1}, // S4
                {0, 0, 0, 1, 1, 0, 0}  // S5
        };

        ExactCover dlx = new ExactCover();
        java.util.List<java.util.List<Integer>> solutions = dlx.solve(matrix);

        System.out.println("解的数量: " + solutions.size());
        for (java.util.List<Integer> sol : solutions) {
            System.out.println("选中的行: " + sol);
        }
    }
}
