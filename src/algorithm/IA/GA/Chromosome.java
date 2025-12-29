package algorithm.IA.GA;

import java.util.Arrays;
import java.util.Random;

/**
 * 染色体类 - 表示遗传算法中的个体
 */
public class Chromosome implements Comparable<Chromosome> {
    private double[] genes;     // 基因数组
    private double fitness;     // 适应度值
    private Random random = new Random();

    public Chromosome(int geneLength) {
        this.genes = new double[geneLength];
        initializeGenes();
    }

    public Chromosome(double[] genes) {
        this.genes = Arrays.copyOf(genes, genes.length);
    }

    /**
     * 初始化基因（随机初始化）
     */
    private void initializeGenes() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = random.nextDouble() * 10 - 5; // 初始化在[-5, 5]范围内
        }
    }

    /**
     * 计算适应度值 - 这里使用简单的函数优化示例
     * 目标函数: f(x,y) = -(x^2 + y^2) (求最大值)
     */
    public void calculateFitness() {
        if (genes.length >= 2) {
            double x = genes[0];
            double y = genes[1];
            // 示例目标函数: 最大化 -(x^2 + y^2)，相当于最小化 x^2 + y^2
            this.fitness = -(x * x + y * y);
        }
    }

    /**
     * 变异操作
     */
    public void mutate(double mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                genes[i] += (random.nextDouble() - 0.5) * 2; // 变异范围[-1, 1]
            }
        }
    }

    /**
     * 交叉操作
     */
    public static Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
        int geneLength = parent1.genes.length;
        int crossoverPoint = parent1.random.nextInt(geneLength);

        double[] child1Genes = new double[geneLength];
        double[] child2Genes = new double[geneLength];

        for (int i = 0; i < geneLength; i++) {
            if (i < crossoverPoint) {
                child1Genes[i] = parent1.genes[i];
                child2Genes[i] = parent2.genes[i];
            } else {
                child1Genes[i] = parent2.genes[i];
                child2Genes[i] = parent1.genes[i];
            }
        }

        return new Chromosome[]{new Chromosome(child1Genes), new Chromosome(child2Genes)};
    }

    // Getters and Setters
    public double[] getGenes() {
        return Arrays.copyOf(genes, genes.length);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Chromosome other) {
        return Double.compare(other.fitness, this.fitness); // 降序排序
    }

    @Override
    public String toString() {
        return String.format("Genes: %s, Fitness: %.4f",
            Arrays.toString(genes), fitness);
    }
}
