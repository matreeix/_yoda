package algorithm.IA.SA;

import java.util.Random;

/**
 * 模拟退火算法主类
 */
public class SimulatedAnnealing {
    private double[] currentSolution;      // 当前解
    private double[] bestSolution;         // 最好解
    private double currentEnergy;          // 当前能量值
    private double bestEnergy;             // 最好能量值
    private double temperature;            // 当前温度
    private double initialTemperature;     // 初始温度
    private double finalTemperature;       // 终止温度
    private double coolingRate;            // 冷却率
    private int dimension;                 // 问题维度
    private Random random = new Random();
    private EnergyFunction energyFunction; // 能量函数

    public SimulatedAnnealing(int dimension, double initialTemperature,
                             double finalTemperature, double coolingRate) {
        this.dimension = dimension;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;
        this.coolingRate = coolingRate;
        this.temperature = initialTemperature;

        this.currentSolution = new double[dimension];
        this.bestSolution = new double[dimension];
        this.bestEnergy = Double.MAX_VALUE;
    }

    /**
     * 设置能量函数
     */
    public void setEnergyFunction(EnergyFunction energyFunction) {
        this.energyFunction = energyFunction;
    }

    /**
     * 初始化解
     */
    public void initializeSolution() {
        for (int i = 0; i < dimension; i++) {
            // 初始化在[-5, 5]范围内
            currentSolution[i] = (random.nextDouble() - 0.5) * 10;
        }
        currentEnergy = energyFunction.evaluate(currentSolution);

        // 初始最好解为当前解
        System.arraycopy(currentSolution, 0, bestSolution, 0, dimension);
        bestEnergy = currentEnergy;
    }

    /**
     * 运行模拟退火算法
     */
    public double[] run() {
        initializeSolution();

        System.out.println("=== 模拟退火算法开始运行 ===");
        System.out.printf("初始温度: %.2f, 终止温度: %.6f%n", initialTemperature, finalTemperature);
        System.out.printf("初始解能量: %.6f%n", currentEnergy);

        int iteration = 0;
        while (temperature > finalTemperature) {
            // 生成邻域解
            double[] newSolution = generateNeighborSolution();

            // 计算新解能量
            double newEnergy = energyFunction.evaluate(newSolution);

            // 判断是否接受新解
            if (acceptSolution(currentEnergy, newEnergy, temperature)) {
                System.arraycopy(newSolution, 0, currentSolution, 0, dimension);
                currentEnergy = newEnergy;

                // 更新最好解
                if (newEnergy < bestEnergy) {
                    System.arraycopy(newSolution, 0, bestSolution, 0, dimension);
                    bestEnergy = newEnergy;
                }
            }

            // 降低温度
            temperature *= coolingRate;

            if (iteration % 100 == 0) {
                System.out.printf("Iteration %d: Temperature = %.6f, Best Energy = %.6f%n",
                    iteration, temperature, bestEnergy);
            }
            iteration++;
        }

        System.out.printf("最终最优能量: %.6f%n", bestEnergy);
        return bestSolution.clone();
    }

    /**
     * 生成邻域解
     */
    private double[] generateNeighborSolution() {
        double[] neighbor = currentSolution.clone();

        // 随机选择一个维度进行扰动
        int randomDimension = random.nextInt(dimension);
        // 扰动范围与温度相关
        double perturbation = (random.nextDouble() - 0.5) * temperature * 0.1;

        neighbor[randomDimension] += perturbation;

        // 边界处理 (可选)
        // neighbor[randomDimension] = Math.max(-5.0, Math.min(5.0, neighbor[randomDimension]));

        return neighbor;
    }

    /**
     * Metropolis准则判断是否接受新解
     */
    private boolean acceptSolution(double currentEnergy, double newEnergy, double temperature) {
        // 如果新解更好，直接接受
        if (newEnergy < currentEnergy) {
            return true;
        }

        // 如果新解更差，按照Metropolis准则概率接受
        double acceptanceProbability = Math.exp((currentEnergy - newEnergy) / temperature);
        return random.nextDouble() < acceptanceProbability;
    }

    // Getters
    public double[] getBestSolution() {
        return bestSolution.clone();
    }

    public double getBestEnergy() {
        return bestEnergy;
    }

    public double getCurrentTemperature() {
        return temperature;
    }

    /**
     * 能量函数接口
     */
    public interface EnergyFunction {
        double evaluate(double[] solution);
    }
}
