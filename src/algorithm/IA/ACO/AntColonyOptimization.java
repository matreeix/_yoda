package algorithm.IA.ACO;

import java.util.Arrays;
import java.util.List;

/**
 * 蚁群优化算法主类
 */
public class AntColonyOptimization {
    private int numCities;                // 城市数量
    private int numAnts;                  // 蚂蚁数量
    private double[][] distanceMatrix;    // 距离矩阵
    private double[][] pheromoneMatrix;   // 信息素矩阵
    private double alpha;                 // 信息素重要程度因子
    private double beta;                  // 启发式因子
    private double rho;                   // 信息素蒸发率
    private double Q;                     // 信息素常量
    private int maxIterations;            // 最大迭代次数
    private AntColonyAnt[] ants;          // 蚂蚁数组

    public AntColonyOptimization(int numCities, int numAnts, double[][] distanceMatrix,
                               double alpha, double beta, double rho, double Q, int maxIterations) {
        this.numCities = numCities;
        this.numAnts = numAnts;
        this.distanceMatrix = distanceMatrix;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.Q = Q;
        this.maxIterations = maxIterations;

        this.pheromoneMatrix = new double[numCities][numCities];
        this.ants = new AntColonyAnt[numAnts];

        initializePheromoneMatrix();
        initializeAnts();
    }

    /**
     * 初始化信息素矩阵
     */
    private void initializePheromoneMatrix() {
        double initialPheromone = 1.0 / numCities;
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] = initialPheromone;
            }
        }
    }

    /**
     * 初始化蚂蚁
     */
    private void initializeAnts() {
        for (int i = 0; i < numAnts; i++) {
            ants[i] = new AntColonyAnt(numCities);
        }
    }

    /**
     * 蚁群算法主循环
     */
    public List<Integer> run() {
        List<Integer> bestTour = null;
        double bestTourLength = Double.MAX_VALUE;

        System.out.println("=== 蚁群优化算法开始运行 ===");

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 每只蚂蚁构建路径
            constructSolutions();

            // 更新信息素
            updatePheromoneMatrix();

            // 找到本代最优解
            List<Integer> currentBestTour = getBestTourInIteration();
            double currentBestLength = calculateTourLength(currentBestTour);

            if (currentBestLength < bestTourLength) {
                bestTourLength = currentBestLength;
                bestTour = new java.util.ArrayList<>(currentBestTour);
            }

            if (iteration % 10 == 0) {
                System.out.printf("Iteration %d: Best Tour Length = %.2f%n",
                    iteration, bestTourLength);
            }
        }

        System.out.printf("最终最优路径长度: %.2f%n", bestTourLength);
        return bestTour;
    }

    /**
     * 构建所有蚂蚁的路径
     */
    private void constructSolutions() {
        for (AntColonyAnt ant : ants) {
            ant.reset();

            // 为每只蚂蚁构建完整路径
            for (int step = 1; step < numCities; step++) {
                int nextCity = ant.selectNextCity(pheromoneMatrix, distanceMatrix, alpha, beta);
                if (nextCity != -1) {
                    ant.moveToCity(nextCity, distanceMatrix);
                }
            }

            // 返回起始城市
            ant.returnToStart(distanceMatrix);
        }
    }

    /**
     * 更新信息素矩阵
     */
    private void updatePheromoneMatrix() {
        // 信息素蒸发
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] *= (1.0 - rho);
            }
        }

        // 信息素增强
        for (AntColonyAnt ant : ants) {
            double tourLength = ant.getTourLength();
            List<Integer> tour = ant.getTour();

            for (int i = 0; i < tour.size() - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                pheromoneMatrix[city1][city2] += Q / tourLength;
                pheromoneMatrix[city2][city1] += Q / tourLength; // 对称矩阵
            }
        }
    }

    /**
     * 获取当前迭代中的最优路径
     */
    private List<Integer> getBestTourInIteration() {
        List<Integer> bestTour = null;
        double bestLength = Double.MAX_VALUE;

        for (AntColonyAnt ant : ants) {
            double tourLength = ant.getTourLength();
            if (tourLength < bestLength) {
                bestLength = tourLength;
                bestTour = ant.getTour();
            }
        }

        return bestTour;
    }

    /**
     * 计算路径长度
     */
    private double calculateTourLength(List<Integer> tour) {
        double length = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            length += distanceMatrix[city1][city2];
        }
        return length;
    }

    // Getters
    public double[][] getPheromoneMatrix() {
        double[][] copy = new double[numCities][numCities];
        for (int i = 0; i < numCities; i++) {
            copy[i] = Arrays.copyOf(pheromoneMatrix[i], numCities);
        }
        return copy;
    }
}
