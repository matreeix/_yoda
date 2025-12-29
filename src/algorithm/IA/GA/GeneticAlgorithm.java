package algorithm.IA.GA;

import java.util.Arrays;
import java.util.Random;

/**
 * 遗传算法主类
 */
public class GeneticAlgorithm {
    private int populationSize;      // 种群大小
    private int geneLength;          // 基因长度
    private double mutationRate;     // 变异率
    private double crossoverRate;    // 交叉率
    private int maxGenerations;      // 最大代数
    private Chromosome[] population; // 当前种群
    private Random random = new Random();

    public GeneticAlgorithm(int populationSize, int geneLength, double mutationRate,
                          double crossoverRate, int maxGenerations) {
        this.populationSize = populationSize;
        this.geneLength = geneLength;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.maxGenerations = maxGenerations;
        this.population = new Chromosome[populationSize];
    }

    /**
     * 初始化种群
     */
    public void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Chromosome(geneLength);
        }
    }

    /**
     * 计算种群适应度
     */
    public void evaluatePopulation() {
        for (Chromosome chromosome : population) {
            chromosome.calculateFitness();
        }
        Arrays.sort(population); // 按适应度降序排序
    }

    /**
     * 选择操作 - 轮盘赌选择
     */
    public Chromosome selectParent() {
        double totalFitness = Arrays.stream(population)
                .mapToDouble(Chromosome::getFitness)
                .sum();

        double randomValue = random.nextDouble() * totalFitness;
        double cumulativeFitness = 0;

        for (Chromosome chromosome : population) {
            cumulativeFitness += chromosome.getFitness();
            if (cumulativeFitness >= randomValue) {
                return chromosome;
            }
        }

        return population[population.length - 1]; // 返回最后一个作为fallback
    }

    /**
     * 创建下一代种群
     */
    public void createNextGeneration() {
        Chromosome[] newPopulation = new Chromosome[populationSize];

        // 精英保留策略 - 保留最优个体
        int eliteCount = (int) (populationSize * 0.1);
        for (int i = 0; i < eliteCount; i++) {
            newPopulation[i] = population[i];
        }

        // 生成剩余个体
        for (int i = eliteCount; i < populationSize; i += 2) {
            Chromosome parent1 = selectParent();
            Chromosome parent2 = selectParent();

            if (random.nextDouble() < crossoverRate) {
                Chromosome[] offspring = Chromosome.crossover(parent1, parent2);
                offspring[0].mutate(mutationRate);
                offspring[1].mutate(mutationRate);

                newPopulation[i] = offspring[0];
                if (i + 1 < populationSize) {
                    newPopulation[i + 1] = offspring[1];
                }
            } else {
                parent1.mutate(mutationRate);
                parent2.mutate(mutationRate);
                newPopulation[i] = parent1;
                if (i + 1 < populationSize) {
                    newPopulation[i + 1] = parent2;
                }
            }
        }

        population = newPopulation;
    }

    /**
     * 获取最优解
     */
    public Chromosome getBestSolution() {
        return population[0];
    }

    /**
     * 运行遗传算法
     */
    public Chromosome run() {
        initializePopulation();

        for (int generation = 0; generation < maxGenerations; generation++) {
            evaluatePopulation();

            if (generation % 10 == 0) {
                System.out.printf("Generation %d: Best Fitness = %.4f%n",
                    generation, population[0].getFitness());
            }

            if (generation < maxGenerations - 1) {
                createNextGeneration();
            }
        }

        evaluatePopulation();
        return getBestSolution();
    }

    // Getter for population (for testing/debugging)
    public Chromosome[] getPopulation() {
        return Arrays.copyOf(population, population.length);
    }
}
