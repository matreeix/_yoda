package algorithm.IA.ACO;

import java.util.List;

/**
 * 蚁群算法应用示例：旅行商问题 (TSP)
 */
public class TSPExample {

    public static void main(String[] args) {
        // 创建一个简单的TSP实例 (5个城市)
        int numCities = 5;
        double[][] distanceMatrix = {
            {0, 10, 15, 20, 25},
            {10, 0, 35, 25, 30},
            {15, 35, 0, 30, 20},
            {20, 25, 30, 0, 15},
            {25, 30, 20, 15, 0}
        };

        // 蚁群算法参数
        int numAnts = 20;                 // 蚂蚁数量
        double alpha = 1.0;              // 信息素重要程度因子
        double beta = 2.0;               // 启发式因子
        double rho = 0.5;                // 信息素蒸发率
        double Q = 100.0;                // 信息素常量
        int maxIterations = 100;         // 最大迭代次数

        System.out.println("=== 蚁群算法解决TSP问题 ===");
        System.out.println("城市数量: " + numCities);
        System.out.println("距离矩阵:");
        printDistanceMatrix(distanceMatrix);

        // 创建蚁群优化实例
        AntColonyOptimization aco = new AntColonyOptimization(numCities, numAnts, distanceMatrix,
                alpha, beta, rho, Q, maxIterations);

        // 运行算法
        long startTime = System.currentTimeMillis();
        List<Integer> bestTour = aco.run();
        long endTime = System.currentTimeMillis();

        // 输出结果
        System.out.println("\n=== 优化结果 ===");
        System.out.print("最优路径: ");
        for (int i = 0; i < bestTour.size(); i++) {
            System.out.print((bestTour.get(i) + 1)); // 城市编号从1开始
            if (i < bestTour.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" -> " + (bestTour.get(0) + 1)); // 返回起始城市

        double tourLength = calculateTourLength(bestTour, distanceMatrix);
        System.out.printf("路径长度: %.2f%n", tourLength);
        System.out.printf("运行时间: %d ms%n", (endTime - startTime));
    }

    /**
     * 计算路径长度
     */
    private static double calculateTourLength(List<Integer> tour, double[][] distanceMatrix) {
        double length = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            length += distanceMatrix[city1][city2];
        }
        return length;
    }

    /**
     * 打印距离矩阵
     */
    private static void printDistanceMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%6.1f ", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
