package algorithm.IA.PSO;

import java.util.Arrays;

/**
 * 粒子群优化算法主类
 */
public class ParticleSwarmOptimization {
    private int swarmSize;                    // 种群大小
    private int dimension;                    // 问题维度
    private int maxIterations;                // 最大迭代次数
    private double inertiaWeight;             // 惯性权重
    private double cognitiveWeight;           // 个体认知权重
    private double socialWeight;              // 社会认知权重
    private double maxVelocity;               // 最大速度
    private Particle[] swarm;                 // 粒子群
    private double[] globalBest;              // 全局最优位置
    private double globalBestFitness;         // 全局最优适应度
    private FitnessFunction fitnessFunction;  // 适应度函数

    public ParticleSwarmOptimization(int swarmSize, int dimension, int maxIterations,
                                   double inertiaWeight, double cognitiveWeight,
                                   double socialWeight, double maxVelocity) {
        this.swarmSize = swarmSize;
        this.dimension = dimension;
        this.maxIterations = maxIterations;
        this.inertiaWeight = inertiaWeight;
        this.cognitiveWeight = cognitiveWeight;
        this.socialWeight = socialWeight;
        this.maxVelocity = maxVelocity;

        this.swarm = new Particle[swarmSize];
        this.globalBest = new double[dimension];
        this.globalBestFitness = Double.MAX_VALUE;

        initializeSwarm();
    }

    /**
     * 设置适应度函数
     */
    public void setFitnessFunction(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    /**
     * 初始化粒子群
     */
    private void initializeSwarm() {
        for (int i = 0; i < swarmSize; i++) {
            swarm[i] = new Particle(dimension);
        }
    }

    /**
     * 运行PSO算法
     */
    public double[] run() {
        System.out.println("=== 粒子群优化算法开始运行 ===");

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 评估所有粒子
            evaluateSwarm();

            // 更新全局最优
            updateGlobalBest();

            // 更新所有粒子
            updateSwarm();

            if (iteration % 10 == 0) {
                System.out.printf("Iteration %d: Global Best Fitness = %.6f%n",
                    iteration, globalBestFitness);
            }
        }

        System.out.printf("最终全局最优适应度: %.6f%n", globalBestFitness);
        return Arrays.copyOf(globalBest, dimension);
    }

    /**
     * 评估粒子群适应度
     */
    private void evaluateSwarm() {
        for (Particle particle : swarm) {
            if (fitnessFunction != null) {
                particle.evaluateFitness(fitnessFunction);
            } else {
                particle.evaluateFitness(); // 使用默认的Sphere函数
            }
        }
    }

    /**
     * 更新全局最优解
     */
    private void updateGlobalBest() {
        for (Particle particle : swarm) {
            if (particle.getPersonalBestFitness() < globalBestFitness) {
                globalBestFitness = particle.getPersonalBestFitness();
                globalBest = particle.getPersonalBest();
            }
        }
    }

    /**
     * 更新所有粒子
     */
    private void updateSwarm() {
        for (Particle particle : swarm) {
            particle.update(globalBest, inertiaWeight, cognitiveWeight,
                          socialWeight, maxVelocity);
        }
    }

    // Getters
    public double[] getGlobalBest() {
        return Arrays.copyOf(globalBest, dimension);
    }

    public double getGlobalBestFitness() {
        return globalBestFitness;
    }

    public Particle[] getSwarm() {
        return Arrays.copyOf(swarm, swarmSize);
    }

    /**
     * 适应度函数接口
     */
    public interface FitnessFunction {
        double evaluate(double[] position);
    }
}
