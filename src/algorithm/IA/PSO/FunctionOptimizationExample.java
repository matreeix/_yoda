package algorithm.IA.PSO;

/**
 * 粒子群优化应用示例：函数优化问题
 */
public class FunctionOptimizationExample {

    public static void main(String[] args) {
        // PSO算法参数
        int swarmSize = 30;              // 种群大小
        int dimension = 2;               // 问题维度 (二维函数优化)
        int maxIterations = 100;         // 最大迭代次数
        double inertiaWeight = 0.7;      // 惯性权重
        double cognitiveWeight = 1.4;    // 个体认知权重
        double socialWeight = 1.4;       // 社会认知权重
        double maxVelocity = 2.0;        // 最大速度

        System.out.println("=== 粒子群优化函数优化示例 ===");
        System.out.println("目标函数: f(x,y) = x^2 + y^2 (Sphere函数)");
        System.out.println("理论最优解: x=0, y=0, f(0,0)=0");
        System.out.println();

        // 创建PSO实例
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization(
            swarmSize, dimension, maxIterations,
            inertiaWeight, cognitiveWeight, socialWeight, maxVelocity
        );

        // 设置自定义适应度函数 (也可以使用默认的Sphere函数)
        pso.setFitnessFunction(position -> {
            double x = position[0];
            double y = position[1];
            return x * x + y * y; // Sphere函数
        });

        // 运行算法
        long startTime = System.currentTimeMillis();
        double[] bestSolution = pso.run();
        long endTime = System.currentTimeMillis();

        // 输出结果
        System.out.println("\n=== 优化结果 ===");
        System.out.printf("最优解: x=%.6f, y=%.6f%n", bestSolution[0], bestSolution[1]);
        double fitness = bestSolution[0] * bestSolution[0] + bestSolution[1] * bestSolution[1];
        System.out.printf("目标函数值: %.6f%n", fitness);
        System.out.printf("理论误差: %.6f%n", Math.abs(bestSolution[0]) + Math.abs(bestSolution[1]));
        System.out.printf("运行时间: %d ms%n", (endTime - startTime));

        // 运行多个不同的适应度函数示例
        runRastriginExample();
    }

    /**
     * Rastrigin函数优化示例
     */
    private static void runRastriginExample() {
        System.out.println("\n=== Rastrigin函数优化示例 ===");
        System.out.println("目标函数: f(x,y) = 20 + x^2 + y^2 - 10*(cos(2πx) + cos(2πy))");
        System.out.println("理论最优解: x=0, y=0, f(0,0)=0");

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization(30, 2, 100, 0.7, 1.4, 1.4, 2.0);

        // Rastrigin函数
        pso.setFitnessFunction(position -> {
            double x = position[0];
            double y = position[1];
            return 20 + x*x + y*y - 10*(Math.cos(2*Math.PI*x) + Math.cos(2*Math.PI*y));
        });

        double[] bestSolution = pso.run();
        System.out.printf("最优解: x=%.6f, y=%.6f%n", bestSolution[0], bestSolution[1]);
        double fitness = 20 + bestSolution[0]*bestSolution[0] + bestSolution[1]*bestSolution[1]
                        - 10*(Math.cos(2*Math.PI*bestSolution[0]) + Math.cos(2*Math.PI*bestSolution[1]));
        System.out.printf("目标函数值: %.6f%n", fitness);
    }
}
