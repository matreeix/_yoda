package algorithm.IA.KMeans;

import java.util.*;

/**
 * K-means聚类算法
 */
public class KMeans {
    private int k;                    // 聚类数
    private int maxIterations;        // 最大迭代次数
    private double[][] centroids;     // 聚类中心
    private int[] labels;             // 样本标签
    private Random random = new Random();

    public KMeans(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    /**
     * 训练K-means模型
     */
    public void fit(double[][] data) {
        int n = data.length;
        int d = data[0].length;

        // 初始化聚类中心
        initializeCentroids(data);

        labels = new int[n];

        System.out.println("=== K-means聚类训练 ===");

        for (int iter = 0; iter < maxIterations; iter++) {
            // 分配样本到最近的聚类中心
            boolean changed = assignClusters(data);

            // 更新聚类中心
            updateCentroids(data);

            if (!changed) {
                System.out.println("收敛于第" + (iter + 1) + "次迭代");
                break;
            }

            if (iter % 10 == 0) {
                double inertia = calculateInertia(data);
                System.out.printf("Iteration %d: Inertia = %.4f%n", iter, inertia);
            }
        }
    }

    /**
     * 初始化聚类中心 (随机选择)
     */
    private void initializeCentroids(double[][] data) {
        int n = data.length;
        int d = data[0].length;
        centroids = new double[k][d];

        Set<Integer> selected = new HashSet<>();
        for (int i = 0; i < k; i++) {
            int idx;
            do {
                idx = random.nextInt(n);
            } while (selected.contains(idx));

            selected.add(idx);
            System.arraycopy(data[idx], 0, centroids[i], 0, d);
        }
    }

    /**
     * 分配样本到最近的聚类中心
     */
    private boolean assignClusters(double[][] data) {
        boolean changed = false;
        int n = data.length;

        for (int i = 0; i < n; i++) {
            int closestCentroid = findClosestCentroid(data[i]);
            if (labels[i] != closestCentroid) {
                labels[i] = closestCentroid;
                changed = true;
            }
        }

        return changed;
    }

    /**
     * 找到最近的聚类中心
     */
    private int findClosestCentroid(double[] point) {
        int closest = 0;
        double minDistance = calculateDistance(point, centroids[0]);

        for (int i = 1; i < k; i++) {
            double distance = calculateDistance(point, centroids[i]);
            if (distance < minDistance) {
                minDistance = distance;
                closest = i;
            }
        }

        return closest;
    }

    /**
     * 更新聚类中心
     */
    private void updateCentroids(double[][] data) {
        int n = data.length;
        int d = data[0].length;

        int[] counts = new int[k];
        double[][] sums = new double[k][d];

        // 计算每个聚类的样本和
        for (int i = 0; i < n; i++) {
            int cluster = labels[i];
            counts[cluster]++;
            for (int j = 0; j < d; j++) {
                sums[cluster][j] += data[i][j];
            }
        }

        // 更新聚类中心
        for (int i = 0; i < k; i++) {
            if (counts[i] > 0) {
                for (int j = 0; j < d; j++) {
                    centroids[i][j] = sums[i][j] / counts[i];
                }
            }
        }
    }

    /**
     * 计算欧几里得距离
     */
    private double calculateDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * 计算惯性 (类内平方和)
     */
    private double calculateInertia(double[][] data) {
        double inertia = 0.0;
        int n = data.length;

        for (int i = 0; i < n; i++) {
            int cluster = labels[i];
            double distance = calculateDistance(data[i], centroids[cluster]);
            inertia += distance * distance;
        }

        return inertia;
    }

    /**
     * 预测新样本的聚类
     */
    public int[] predict(double[][] data) {
        int[] predictions = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            predictions[i] = findClosestCentroid(data[i]);
        }
        return predictions;
    }

    // Getters
    public double[][] getCentroids() {
        double[][] copy = new double[k][];
        for (int i = 0; i < k; i++) {
            copy[i] = Arrays.copyOf(centroids[i], centroids[i].length);
        }
        return copy;
    }

    public int[] getLabels() {
        return Arrays.copyOf(labels, labels.length);
    }
}
