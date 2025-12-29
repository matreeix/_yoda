package algorithm.IA.ANN;

/**
 * 人工神经网络应用示例：XOR问题
 */
public class XORExample {

    public static void main(String[] args) {
        // XOR问题训练数据
        double[][] inputs = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };

        double[][] targets = {
            {0},
            {1},
            {1},
            {0}
        };

        // 神经网络参数
        int inputSize = 2;          // 输入层神经元数
        int hiddenSize = 4;         // 隐藏层神经元数
        int outputSize = 1;         // 输出层神经元数
        double learningRate = 0.1;  // 学习率
        int maxEpochs = 10000;      // 最大训练轮数

        System.out.println("=== 神经网络解决XOR问题 ===");
        System.out.println("XOR函数真值表:");
        System.out.println("0 XOR 0 = 0");
        System.out.println("0 XOR 1 = 1");
        System.out.println("1 XOR 0 = 1");
        System.out.println("1 XOR 1 = 0");
        System.out.println();

        // 创建神经网络
        NeuralNetwork nn = new NeuralNetwork(inputSize, hiddenSize, outputSize, learningRate);

        // 训练网络
        System.out.println("开始训练...");
        long startTime = System.currentTimeMillis();

        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            // 训练所有样本
            for (int i = 0; i < inputs.length; i++) {
                nn.train(inputs[i], targets[i]);
            }

            // 每1000轮输出一次误差
            if (epoch % 1000 == 0) {
                double mse = nn.calculateMSE(inputs, targets);
                System.out.printf("Epoch %d: MSE = %.6f%n", epoch, mse);
            }
        }

        long endTime = System.currentTimeMillis();

        // 测试网络
        System.out.println("\n=== 测试结果 ===");
        for (int i = 0; i < inputs.length; i++) {
            double[] output = nn.forward(inputs[i]);
            System.out.printf("输入: %.0f, %.0f -> 输出: %.4f (期望: %.0f)%n",
                inputs[i][0], inputs[i][1], output[0], targets[i][0]);
        }

        double finalMSE = nn.calculateMSE(inputs, targets);
        System.out.printf("最终MSE: %.6f%n", finalMSE);
        System.out.printf("训练时间: %d ms%n", (endTime - startTime));
    }
}
