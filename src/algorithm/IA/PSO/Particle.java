package algorithm.IA.PSO;

import java.util.Arrays;
import java.util.Random;

/**
 * 粒子类 - 表示粒子群优化中的单个粒子
 */
public class Particle {
    private double[] position;        // 当前位置
    private double[] velocity;        // 当前速度
    private double[] personalBest;    // 个体最优位置
    private double personalBestFitness; // 个体最优适应度
    private double fitness;           // 当前适应度
    private int dimension;            // 问题维度
    private Random random = new Random();

    public Particle(int dimension) {
        this.dimension = dimension;
        this.position = new double[dimension];
        this.velocity = new double[dimension];
        this.personalBest = new double[dimension];

        initialize();
    }

    /**
     * 初始化粒子
     */
    private void initialize() {
        for (int i = 0; i < dimension; i++) {
            // 初始化位置在[-5, 5]范围内
            position[i] = (random.nextDouble() - 0.5) * 10;
            // 初始化速度在[-1, 1]范围内
            velocity[i] = (random.nextDouble() - 0.5) * 2;
            // 初始个体最优位置为当前位置
            personalBest[i] = position[i];
        }
        personalBestFitness = Double.MAX_VALUE;
    }

    /**
     * 更新粒子速度和位置
     */
    public void update(double[] globalBest, double inertiaWeight, double cognitiveWeight,
                      double socialWeight, double maxVelocity) {
        for (int i = 0; i < dimension; i++) {
            // 计算随机因子
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();

            // 更新速度
            double cognitiveComponent = cognitiveWeight * r1 * (personalBest[i] - position[i]);
            double socialComponent = socialWeight * r2 * (globalBest[i] - position[i]);

            velocity[i] = inertiaWeight * velocity[i] + cognitiveComponent + socialComponent;

            // 限制速度范围
            if (velocity[i] > maxVelocity) {
                velocity[i] = maxVelocity;
            } else if (velocity[i] < -maxVelocity) {
                velocity[i] = -maxVelocity;
            }

            // 更新位置
            position[i] += velocity[i];
        }
    }

    /**
     * 计算适应度
     */
    public void evaluateFitness() {
        // 示例目标函数: Sphere函数 f(x) = Σ(x_i^2)
        fitness = 0.0;
        for (double x : position) {
            fitness += x * x;
        }

        // 更新个体最优
        if (fitness < personalBestFitness) {
            personalBestFitness = fitness;
            System.arraycopy(position, 0, personalBest, 0, dimension);
        }
    }

    /**
     * 自定义适应度函数
     */
    public void evaluateFitness(FitnessFunction fitnessFunction) {
        fitness = fitnessFunction.evaluate(position);

        // 更新个体最优
        if (fitness < personalBestFitness) {
            personalBestFitness = fitness;
            System.arraycopy(position, 0, personalBest, 0, dimension);
        }
    }

    // Getters and Setters
    public double[] getPosition() {
        return Arrays.copyOf(position, dimension);
    }

    public double[] getVelocity() {
        return Arrays.copyOf(velocity, dimension);
    }

    public double[] getPersonalBest() {
        return Arrays.copyOf(personalBest, dimension);
    }

    public double getFitness() {
        return fitness;
    }

    public double getPersonalBestFitness() {
        return personalBestFitness;
    }

    public void setPosition(double[] position) {
        System.arraycopy(position, 0, this.position, 0, dimension);
    }

    public void setVelocity(double[] velocity) {
        System.arraycopy(velocity, 0, this.velocity, 0, dimension);
    }

    @Override
    public String toString() {
        return String.format("Position: %s, Fitness: %.4f, PersonalBest: %.4f",
            Arrays.toString(position), fitness, personalBestFitness);
    }
}
