package algorithm.IA.ANN;

import java.util.Random;

/**
 * 简化的多层感知机神经网络
 */
public class NeuralNetwork {
    private int inputSize;          // 输入层神经元数
    private int hiddenSize;         // 隐藏层神经元数
    private int outputSize;         // 输出层神经元数
    private double[][] weightsIH;   // 输入层到隐藏层的权重
    private double[][] weightsHO;   // 隐藏层到输出层的权重
    private double[] biasH;         // 隐藏层偏置
    private double[] biasO;         // 输出层偏置
    private double learningRate;    // 学习率
    private Random random = new Random();

    public NeuralNetwork(int inputSize, int hiddenSize, int outputSize, double learningRate) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.learningRate = learningRate;

        // 初始化权重和偏置
        initializeWeights();
    }

    /**
     * 初始化权重和偏置
     */
    private void initializeWeights() {
        // 输入层到隐藏层权重
        weightsIH = new double[inputSize][hiddenSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsIH[i][j] = (random.nextDouble() - 0.5) * 0.1;
            }
        }

        // 隐藏层到输出层权重
        weightsHO = new double[hiddenSize][outputSize];
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightsHO[i][j] = (random.nextDouble() - 0.5) * 0.1;
            }
        }

        // 偏置初始化
        biasH = new double[hiddenSize];
        biasO = new double[outputSize];
    }

    /**
     * Sigmoid激活函数
     */
    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    /**
     * Sigmoid导数
     */
    private double sigmoidDerivative(double x) {
        return x * (1.0 - x);
    }

    /**
     * 前向传播
     */
    public double[] forward(double[] inputs) {
        // 计算隐藏层输出
        double[] hiddenOutputs = new double[hiddenSize];
        for (int j = 0; j < hiddenSize; j++) {
            double sum = biasH[j];
            for (int i = 0; i < inputSize; i++) {
                sum += inputs[i] * weightsIH[i][j];
            }
            hiddenOutputs[j] = sigmoid(sum);
        }

        // 计算输出层输出
        double[] outputs = new double[outputSize];
        for (int k = 0; k < outputSize; k++) {
            double sum = biasO[k];
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenOutputs[j] * weightsHO[j][k];
            }
            outputs[k] = sigmoid(sum);
        }

        return outputs;
    }

    /**
     * 反向传播训练
     */
    public void train(double[] inputs, double[] targets) {
        // 前向传播
        double[] hiddenOutputs = new double[hiddenSize];
        for (int j = 0; j < hiddenSize; j++) {
            double sum = biasH[j];
            for (int i = 0; i < inputSize; i++) {
                sum += inputs[i] * weightsIH[i][j];
            }
            hiddenOutputs[j] = sigmoid(sum);
        }

        double[] outputs = new double[outputSize];
        for (int k = 0; k < outputSize; k++) {
            double sum = biasO[k];
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenOutputs[j] * weightsHO[j][k];
            }
            outputs[k] = sigmoid(sum);
        }

        // 计算输出层误差
        double[] outputErrors = new double[outputSize];
        for (int k = 0; k < outputSize; k++) {
            outputErrors[k] = (targets[k] - outputs[k]) * sigmoidDerivative(outputs[k]);
        }

        // 计算隐藏层误差
        double[] hiddenErrors = new double[hiddenSize];
        for (int j = 0; j < hiddenSize; j++) {
            double error = 0.0;
            for (int k = 0; k < outputSize; k++) {
                error += outputErrors[k] * weightsHO[j][k];
            }
            hiddenErrors[j] = error * sigmoidDerivative(hiddenOutputs[j]);
        }

        // 更新隐藏层到输出层权重和偏置
        for (int j = 0; j < hiddenSize; j++) {
            for (int k = 0; k < outputSize; k++) {
                weightsHO[j][k] += learningRate * outputErrors[k] * hiddenOutputs[j];
            }
        }
        for (int k = 0; k < outputSize; k++) {
            biasO[k] += learningRate * outputErrors[k];
        }

        // 更新输入层到隐藏层权重和偏置
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsIH[i][j] += learningRate * hiddenErrors[j] * inputs[i];
            }
        }
        for (int j = 0; j < hiddenSize; j++) {
            biasH[j] += learningRate * hiddenErrors[j];
        }
    }

    /**
     * 计算均方误差
     */
    public double calculateMSE(double[][] inputs, double[][] targets) {
        double totalError = 0.0;
        for (int i = 0; i < inputs.length; i++) {
            double[] outputs = forward(inputs[i]);
            for (int j = 0; j < outputs.length; j++) {
                double error = targets[i][j] - outputs[j];
                totalError += error * error;
            }
        }
        return totalError / (inputs.length * outputSize);
    }
}
