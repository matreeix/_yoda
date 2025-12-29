package algorithm.IA.GA;

/**
 * 遗传算法应用示例：函数优化问题
 * 目标函数: f(x,y) = -(x^2 + y^2)
 * 目标: 找到使f(x,y)最大化的x,y值（相当于最小化x^2+y^2）
 */
public class FunctionOptimization {

    public static void main(String[] args) {
        // 遗传算法参数设置
        int populationSize = 100;      // 种群大小
        int geneLength = 2;            // 基因长度（x,y两个变量）
        double mutationRate = 0.01;    // 变异率
        double crossoverRate = 0.8;    // 交叉率
        int maxGenerations = 100;      // 最大代数

        // 创建遗传算法实例
        GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, geneLength,
                mutationRate, crossoverRate, maxGenerations);

        System.out.println("=== 遗传算法函数优化示例 ===");
        System.out.println("目标函数: f(x,y) = -(x^2 + y^2)");
        System.out.println("目标: 最大化f(x,y)，相当于最小化x^2+y^2");
        System.out.println("理论最优解: x=0, y=0, f(0,0)=0\n");

        // 运行遗传算法
        long startTime = System.currentTimeMillis();
        Chromosome bestSolution = ga.run();
        long endTime = System.currentTimeMillis();

        // 输出结果
        double[] genes = bestSolution.getGenes();
        System.out.println("\n=== 优化结果 ===");
        System.out.printf("最优解: x=%.4f, y=%.4f%n", genes[0], genes[1]);
        System.out.printf("目标函数值: %.4f%n", bestSolution.getFitness());
        System.out.printf("理论误差: %.4f%n", Math.abs(genes[0]) + Math.abs(genes[1]));
        System.out.printf("运行时间: %d ms%n", (endTime - startTime));
    }
}
