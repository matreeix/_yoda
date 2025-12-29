package algorithm.IA.Tabu;

/**
 * 禁忌搜索算法应用示例：旅行商问题 (TSP)
 */
public class TSPTabuExample {

    public static void main(String[] args) {
        // 创建一个简单的TSP实例 (6个城市)
        int numCities = 6;
        double[][] distanceMatrix = {
            {0, 10, 15, 20, 25, 30},
            {10, 0, 35, 25, 30, 15},
            {15, 35, 0, 30, 20, 25},
            {20, 25, 30, 0, 15, 20},
            {25, 30, 20, 15, 0, 10},
            {30, 15, 25, 20, 10, 0}
        };

        // 禁忌搜索算法参数
        int tabuTenure = 5;                 // 禁忌期限
        int maxIterations = 100;            // 最大迭代次数

        System.out.println("=== 禁忌搜索解决TSP问题 ===");
        System.out.println("城市数量: " + numCities);
        System.out.println("距离矩阵:");
        printDistanceMatrix(distanceMatrix);

        // 创建禁忌搜索实例
        TabuSearch tabuSearch = new TabuSearch(numCities, tabuTenure, maxIterations);

        // 设置代价函数 (TSP路径长度)
        tabuSearch.setCostFunction(solution -> calculateTourLength(solution, distanceMatrix));

        // 运行算法
        long startTime = System.currentTimeMillis();
        int[] bestTour = tabuSearch.run();
        long endTime = System.currentTimeMillis();

        // 输出结果
        System.out.println("\n=== 优化结果 ===");
        System.out.print("最优路径: ");
        for (int i = 0; i < bestTour.length; i++) {
            System.out.print(bestTour[i]);
            if (i < bestTour.length - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" -> " + bestTour[0]); // 返回起始城市

        double tourLength = calculateTourLength(bestTour, distanceMatrix);
        System.out.printf("路径长度: %.2f%n", tourLength);
        System.out.printf("运行时间: %d ms%n", (endTime - startTime));
    }

    /**
     * 计算TSP路径长度
     */
    private static double calculateTourLength(int[] tour, double[][] distanceMatrix) {
        double length = 0.0;
        for (int i = 0; i < tour.length - 1; i++) {
            length += distanceMatrix[tour[i]][tour[i + 1]];
        }
        // 返回起始城市
        length += distanceMatrix[tour[tour.length - 1]][tour[0]];
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
