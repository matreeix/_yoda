package algorithm.IA.SVM;

import java.util.Arrays;

/**
 * 简化的支持向量机实现 (线性可分情况)
 */
public class SimpleSVM {
    private double[] weights;       // 权重向量
    private double bias;           // 偏置
    private double learningRate;   // 学习率
    private double lambda;         // 正则化参数
    private int maxIterations;     // 最大迭代次数

    public SimpleSVM(double learningRate, double lambda, int maxIterations) {
        this.learningRate = learningRate;
        this.lambda = lambda;
        this.maxIterations = maxIterations;
    }

    /**
     * 训练SVM
     */
    public void train(double[][] X, int[] y) {
        int n = X.length;      // 样本数
        int d = X[0].length;   // 特征维度

        weights = new double[d];
        bias = 0.0;

        System.out.println("=== 训练支持向量机 ===");

        // 随机梯度下降
        for (int iter = 0; iter < maxIterations; iter++) {
            double totalLoss = 0.0;

            for (int i = 0; i < n; i++) {
                double prediction = predictSingle(X[i]);
                double loss = Math.max(0, 1 - y[i] * prediction);

                if (loss > 0) {
                    // 更新权重和偏置
                    for (int j = 0; j < d; j++) {
                        weights[j] += learningRate * (y[i] * X[i][j] - lambda * weights[j]);
                    }
                    bias += learningRate * y[i];
                } else {
                    // 正则化项
                    for (int j = 0; j < d; j++) {
                        weights[j] -= learningRate * lambda * weights[j];
                    }
                }

                totalLoss += loss;
            }

            if (iter % 100 == 0) {
                System.out.printf("Iteration %d: Average Loss = %.4f%n", iter, totalLoss / n);
            }
        }
    }

    /**
     * 单样本预测
     */
    private double predictSingle(double[] x) {
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * x[i];
        }
        return sum;
    }

    /**
     * 批量预测
     */
    public int[] predict(double[][] X) {
        int[] predictions = new int[X.length];
        for (int i = 0; i < X.length; i++) {
            predictions[i] = (int) Math.signum(predictSingle(X[i]));
        }
        return predictions;
    }

    /**
     * 计算准确率
     */
    public double calculateAccuracy(double[][] X, int[] y) {
        int[] predictions = predict(X);
        int correct = 0;
        for (int i = 0; i < y.length; i++) {
            if (predictions[i] == y[i]) {
                correct++;
            }
        }
        return (double) correct / y.length;
    }

    // Getters
    public double[] getWeights() {
        return Arrays.copyOf(weights, weights.length);
    }

    public double getBias() {
        return bias;
    }
}
