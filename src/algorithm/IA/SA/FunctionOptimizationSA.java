package algorithm.IA.SA;

/**
 * 模拟退火算法应用示例：函数优化问题
 */
public class FunctionOptimizationSA {

    public static void main(String[] args) {
        // 模拟退火算法参数
        int dimension = 2;                    // 问题维度
        double initialTemperature = 100.0;    // 初始温度
        double finalTemperature = 1e-6;       // 终止温度
        double coolingRate = 0.99;            // 冷却率

        System.out.println("=== 模拟退火函数优化示例 ===");
        System.out.println("目标函数: f(x,y) = x^2 + y^2 (Sphere函数)");
        System.out.println("理论最优解: x=0, y=0, f(0,0)=0");
        System.out.println();

        // 创建模拟退火实例
        SimulatedAnnealing sa = new SimulatedAnnealing(dimension, initialTemperature,
                                                      finalTemperature, coolingRate);

        // 设置能量函数 (最小化问题)
        sa.setEnergyFunction(solution -> {
            double x = solution[0];
            double y = solution[1];
            return x * x + y * y; // Sphere函数
        });

        // 运行算法
        long startTime = System.currentTimeMillis();
        double[] bestSolution = sa.run();
        long endTime = System.currentTimeMillis();

        // 输出结果
        System.out.println("\n=== 优化结果 ===");
        System.out.printf("最优解: x=%.6f, y=%.6f%n", bestSolution[0], bestSolution[1]);
        double fitness = bestSolution[0] * bestSolution[0] + bestSolution[1] * bestSolution[1];
        System.out.printf("目标函数值: %.6f%n", fitness);
        System.out.printf("理论误差: %.6f%n", Math.abs(bestSolution[0]) + Math.abs(bestSolution[1]));
        System.out.printf("运行时间: %d ms%n", (endTime - startTime));

        // 测试Rastrigin函数
        runRastriginExample();
    }

    /**
     * Rastrigin函数优化示例
     */
    private static void runRastriginExample() {
        System.out.println("\n=== Rastrigin函数优化示例 ===");
        System.out.println("目标函数: f(x,y) = 20 + x^2 + y^2 - 10*(cos(2πx) + cos(2πy))");
        System.out.println("理论最优解: x=0, y=0, f(0,0)=0");

        SimulatedAnnealing sa = new SimulatedAnnealing(2, 100.0, 1e-6, 0.99);

        sa.setEnergyFunction(solution -> {
            double x = solution[0];
            double y = solution[1];
            return 20 + x*x + y*y - 10*(Math.cos(2*Math.PI*x) + Math.cos(2*Math.PI*y));
        });

        double[] bestSolution = sa.run();
        System.out.printf("最优解: x=%.6f, y=%.6f%n", bestSolution[0], bestSolution[1]);
        double fitness = 20 + bestSolution[0]*bestSolution[0] + bestSolution[1]*bestSolution[1]
                        - 10*(Math.cos(2*Math.PI*bestSolution[0]) + Math.cos(2*Math.PI*bestSolution[1]));
        System.out.printf("目标函数值: %.6f%n", fitness);
    }
}
