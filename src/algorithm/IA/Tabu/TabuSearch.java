package algorithm.IA.Tabu;

import java.util.*;

/**
 * 禁忌搜索算法主类
 */
public class TabuSearch {
    private int[] currentSolution;          // 当前解
    private int[] bestSolution;             // 最好解
    private double currentCost;             // 当前解代价
    private double bestCost;                // 最好解代价
    private int tabuTenure;                 // 禁忌 Tenure长度
    private int maxIterations;              // 最大迭代次数
    private int problemSize;                // 问题规模
    private TabuList tabuList;              // 禁忌表
    private Random random = new Random();
    private CostFunction costFunction;      // 代价函数

    public TabuSearch(int problemSize, int tabuTenure, int maxIterations) {
        this.problemSize = problemSize;
        this.tabuTenure = tabuTenure;
        this.maxIterations = maxIterations;

        this.currentSolution = new int[problemSize];
        this.bestSolution = new int[problemSize];
        this.tabuList = new TabuList(tabuTenure);
        this.bestCost = Double.MAX_VALUE;
    }

    /**
     * 设置代价函数
     */
    public void setCostFunction(CostFunction costFunction) {
        this.costFunction = costFunction;
    }

    /**
     * 初始化解
     */
    public void initializeSolution() {
        // 生成随机排列作为初始解
        List<Integer> cities = new ArrayList<>();
        for (int i = 0; i < problemSize; i++) {
            cities.add(i);
        }
        Collections.shuffle(cities, random);
        for (int i = 0; i < problemSize; i++) {
            currentSolution[i] = cities.get(i);
        }

        currentCost = costFunction.evaluate(currentSolution);
        System.arraycopy(currentSolution, 0, bestSolution, 0, problemSize);
        bestCost = currentCost;
    }

    /**
     * 运行禁忌搜索算法
     */
    public int[] run() {
        initializeSolution();

        System.out.println("=== 禁忌搜索算法开始运行 ===");
        System.out.printf("初始解代价: %.2f%n", currentCost);

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 生成候选解集合
            List<Move> candidates = generateCandidates();

            // 选择最好的非禁忌候选解
            Move bestMove = selectBestMove(candidates);

            if (bestMove != null) {
                // 执行移动
                performMove(bestMove);

                // 更新禁忌表
                tabuList.addMove(bestMove, iteration + tabuTenure);

                // 更新最好解
                if (currentCost < bestCost) {
                    System.arraycopy(currentSolution, 0, bestSolution, 0, problemSize);
                    bestCost = currentCost;
                }
            }

            if (iteration % 50 == 0) {
                System.out.printf("Iteration %d: Best Cost = %.2f, Current Cost = %.2f%n",
                    iteration, bestCost, currentCost);
            }
        }

        System.out.printf("最终最优代价: %.2f%n", bestCost);
        return Arrays.copyOf(bestSolution, problemSize);
    }

    /**
     * 生成候选解集合 (2-opt邻域)
     */
    private List<Move> generateCandidates() {
        List<Move> candidates = new ArrayList<>();

        // 生成所有可能的2-opt交换
        for (int i = 0; i < problemSize - 1; i++) {
            for (int j = i + 1; j < problemSize; j++) {
                Move move = new Move(i, j);
                candidates.add(move);
            }
        }

        return candidates;
    }

    /**
     * 选择最好的非禁忌候选移动
     */
    private Move selectBestMove(List<Move> candidates) {
        Move bestMove = null;
        double bestMoveCost = Double.MAX_VALUE;

        for (Move move : candidates) {
            // 检查是否为禁忌移动
            if (!tabuList.isTabu(move)) {
                // 计算移动后的代价
                double moveCost = evaluateMove(move);

                // 应用特赦准则 (aspiration criterion)
                if (moveCost < bestCost || moveCost < bestMoveCost) {
                    bestMove = move;
                    bestMoveCost = moveCost;
                }
            }
        }

        return bestMove;
    }

    /**
     * 评估移动的代价
     */
    private double evaluateMove(Move move) {
        // 这里简化为交换两个城市后的代价差
        // 实际应用中可能需要更精确的增量计算
        int[] tempSolution = Arrays.copyOf(currentSolution, problemSize);

        // 执行交换
        int temp = tempSolution[move.i];
        tempSolution[move.i] = tempSolution[move.j];
        tempSolution[move.j] = temp;

        return costFunction.evaluate(tempSolution);
    }

    /**
     * 执行移动
     */
    private void performMove(Move move) {
        // 执行交换
        int temp = currentSolution[move.i];
        currentSolution[move.i] = currentSolution[move.j];
        currentSolution[move.j] = temp;

        // 更新当前代价
        currentCost = costFunction.evaluate(currentSolution);
    }

    // Getters
    public int[] getBestSolution() {
        return Arrays.copyOf(bestSolution, problemSize);
    }

    public double getBestCost() {
        return bestCost;
    }

    /**
     * 移动类 - 表示TSP中的2-opt交换
     */
    public static class Move {
        public int i, j;

        public Move(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Move move = (Move) obj;
            return (i == move.i && j == move.j) || (i == move.j && j == move.i);
        }

        @Override
        public int hashCode() {
            return i < j ? Objects.hash(i, j) : Objects.hash(j, i);
        }

        @Override
        public String toString() {
            return "(" + i + "," + j + ")";
        }
    }

    /**
     * 禁忌表类
     */
    private static class TabuList {
        private Map<Move, Integer> tabuMoves;
        private int maxTenure;

        public TabuList(int maxTenure) {
            this.maxTenure = maxTenure;
            this.tabuMoves = new HashMap<>();
        }

        public void addMove(Move move, int tenure) {
            tabuMoves.put(move, tenure);
        }

        public boolean isTabu(Move move) {
            Integer tenure = tabuMoves.get(move);
            if (tenure == null) return false;

            // 清理过期的禁忌
            if (tenure <= 0) {
                tabuMoves.remove(move);
                return false;
            }

            return true;
        }

        public void decrementTenure() {
            // 每次迭代减少禁忌期限
            tabuMoves.replaceAll((move, tenure) -> tenure - 1);
            // 移除过期禁忌
            tabuMoves.entrySet().removeIf(entry -> entry.getValue() <= 0);
        }
    }

    /**
     * 代价函数接口
     */
    public interface CostFunction {
        double evaluate(int[] solution);
    }
}
