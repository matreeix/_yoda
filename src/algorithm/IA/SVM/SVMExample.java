package algorithm.IA.SVM;

/**
 * 支持向量机应用示例：二分类问题
 */
public class SVMExample {

    public static void main(String[] args) {
        // 简单线性可分数据集
        double[][] X = {
            {2, 3}, {3, 3}, {3, 4}, {1, 1}, {1, 2}, {2, 1}
        };
        int[] y = {1, 1, 1, -1, -1, -1};

        // SVM参数
        double learningRate = 0.01;
        double lambda = 0.01;
        int maxIterations = 1000;

        System.out.println("=== 支持向量机二分类示例 ===");
        System.out.println("数据集:");
        for (int i = 0; i < X.length; i++) {
            System.out.printf("样本 %d: (%.1f, %.1f) -> 类 %d%n",
                i + 1, X[i][0], X[i][1], y[i]);
        }
        System.out.println();

        // 创建并训练SVM
        SimpleSVM svm = new SimpleSVM(learningRate, lambda, maxIterations);

        long startTime = System.currentTimeMillis();
        svm.train(X, y);
        long endTime = System.currentTimeMillis();

        // 测试准确率
        double accuracy = svm.calculateAccuracy(X, y);
        System.out.printf("训练准确率: %.2f%%%n", accuracy * 100);

        // 显示模型参数
        double[] weights = svm.getWeights();
        double bias = svm.getBias();
        System.out.printf("模型参数: w = [%.4f, %.4f], b = %.4f%n",
            weights[0], weights[1], bias);
        System.out.printf("决策函数: %.4f*x + %.4f*y + %.4f = 0%n",
            weights[0], weights[1], bias);
        System.out.printf("运行时间: %d ms%n", (endTime - startTime));
    }
}
