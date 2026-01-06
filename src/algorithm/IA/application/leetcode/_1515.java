package algorithm.IA.application.leetcode;

/**
 * @Description: 1515. 服务中心的最佳位置
 * 一家快递公司希望在新城市建立新的服务中心。公司统计了该城市所有客户在二维地图上的坐标，并希望能够以此为依据为新的服务中心选址：使服务中心 到所有客户的欧几里得距离的总和最小 。
 * 给你一个数组 positions ，其中 positions[i] = [xi, yi] 表示第 i 个客户在二维地图上的位置，返回到所有客户的 欧几里得距离的最小总和 。
 * 换句话说，请你为服务中心选址，该位置的坐标 [xcentre, ycentre] 需要使下面的公式取到最小值：
 * Σ(i=0,n-1) √[(xc-xi)²+(yc-yi)²]
 * 与真实值误差在 10-5之内的答案将被视作正确答案。
 * @Date: 2026/1/5
 */

public class _1515 {

    /**
     * 计算点(x,y)到所有客户位置的欧几里得距离总和
     */
    private double calculateTotalDistance(int[][] positions, double x, double y) {
        double totalDistance = 0.0;
        for (int[] pos : positions) {
            double dx = x - pos[0];
            double dy = y - pos[1];
            totalDistance += Math.sqrt(dx * dx + dy * dy);
        }
        return totalDistance;
    }

    /**
     * 计算梯度：距离函数对x和y的偏导数
     * ∂f/∂x = Σ(xi - x) / sqrt((xi-x)^2 + (yi-y)^2)
     * ∂f/∂y = Σ(yi - y) / sqrt((xi-x)^2 + (yi-y)^2)
     */
    private double[] calculateGradient(int[][] positions, double x, double y) {
        double gradX = 0.0;
        double gradY = 0.0;

        for (int[] pos : positions) {
            double dx = x - pos[0];
            double dy = y - pos[1];
            double distance = Math.sqrt(dx * dx + dy * dy);

            // 避免除零错误，如果距离为0，梯度为0
            if (distance > 1e-10) {
                gradX += dx / distance;
                gradY += dy / distance;
            }
        }

        return new double[]{gradX, gradY};
    }

    /**
     * 方法1：网格搜索法
     * 在坐标范围内进行网格化搜索，找到最小距离的点
     * 时间复杂度：O(gridSize^2 * n)，其中n是客户数量
     */
    public double getMinDistSumGridSearch(int[][] positions) {
        if (positions.length == 0) return 0.0;
        if (positions.length == 1) {
            return 0.0; // 只有一个点，最优位置就是该点本身
        }

        // 找到坐标范围
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        for (int[] pos : positions) {
            minX = Math.min(minX, pos[0]);
            maxX = Math.max(maxX, pos[0]);
            minY = Math.min(minY, pos[1]);
            maxY = Math.max(maxY, pos[1]);
        }

        // 扩展搜索范围，避免最优解在边界上
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        minX -= rangeX * 0.1;
        maxX += rangeX * 0.1;
        minY -= rangeY * 0.1;
        maxY += rangeY * 0.1;

        // 网格搜索参数
        final int GRID_SIZE = 200; // 200x200的网格
        double stepX = (maxX - minX) / GRID_SIZE;
        double stepY = (maxY - minY) / GRID_SIZE;

        double minDistance = Double.MAX_VALUE;

        // 在网格上搜索
        for (int i = 0; i <= GRID_SIZE; i++) {
            for (int j = 0; j <= GRID_SIZE; j++) {
                double x = minX + i * stepX;
                double y = minY + j * stepY;
                double distance = calculateTotalDistance(positions, x, y);

                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }

        return minDistance;
    }

    /**
     * 方法2：梯度下降法
     * 使用梯度下降优化算法寻找最小值
     * 时间复杂度：O(iterations * n)，收敛速度快
     */
    public double getMinDistSumGradientDescent(int[][] positions) {
        if (positions.length == 0) return 0.0;
        if (positions.length == 1) return 0.0;

        // 初始化：使用质心作为起始点
        double centerX = 0.0, centerY = 0.0;
        for (int[] pos : positions) {
            centerX += pos[0];
            centerY += pos[1];
        }
        centerX /= positions.length;
        centerY /= positions.length;

        double currentX = centerX;
        double currentY = centerY;
        double learningRate = 0.1; // 学习率
        final int MAX_ITERATIONS = 10000;
        final double TOLERANCE = 1e-7; // 收敛阈值

        double prevDistance = calculateTotalDistance(positions, currentX, currentY);

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            // 计算梯度
            double[] gradient = calculateGradient(positions, currentX, currentY);

            // 更新位置
            currentX -= learningRate * gradient[0];
            currentY -= learningRate * gradient[1];

            // 计算新距离
            double newDistance = calculateTotalDistance(positions, currentX, currentY);

            // 自适应学习率：如果距离增加，减小学习率
            if (newDistance > prevDistance) {
                learningRate *= 0.5;
                // 回退一步
                currentX += learningRate * gradient[0];
                currentY += learningRate * gradient[1];
                newDistance = prevDistance;
            }

            // 检查收敛
            if (Math.abs(newDistance - prevDistance) < TOLERANCE) {
                break;
            }

            prevDistance = newDistance;
        }

        return prevDistance;
    }

    /**
     * 方法3：随机搜索法
     * 多次随机选择起点进行梯度下降，取最优结果
     * 适用于非凸函数，避免局部最优
     */
    public double getMinDistSumRandomSearch(int[][] positions) {
        if (positions.length == 0) return 0.0;
        if (positions.length == 1) return 0.0;

        // 找到坐标范围用于随机初始化
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        for (int[] pos : positions) {
            minX = Math.min(minX, pos[0]);
            maxX = Math.max(maxX, pos[0]);
            minY = Math.min(minY, pos[1]);
            maxY = Math.max(maxY, pos[1]);
        }

        final int RANDOM_STARTS = 20; // 随机起点数量
        double bestDistance = Double.MAX_VALUE;

        for (int start = 0; start < RANDOM_STARTS; start++) {
            // 随机选择起点
            double startX = minX + Math.random() * (maxX - minX);
            double startY = minY + Math.random() * (maxY - minY);

            // 从这个起点进行局部优化
            double currentX = startX;
            double currentY = startY;
            double learningRate = 0.1;

            final int LOCAL_ITERATIONS = 1000;
            double prevDistance = calculateTotalDistance(positions, currentX, currentY);

            for (int iter = 0; iter < LOCAL_ITERATIONS; iter++) {
                double[] gradient = calculateGradient(positions, currentX, currentY);

                double newX = currentX - learningRate * gradient[0];
                double newY = currentY - learningRate * gradient[1];
                double newDistance = calculateTotalDistance(positions, newX, newY);

                if (newDistance < prevDistance) {
                    currentX = newX;
                    currentY = newY;
                    prevDistance = newDistance;
                } else {
                    learningRate *= 0.9; // 逐渐减小学习率
                }

                if (learningRate < 1e-8) break; // 学习率太小，停止
            }

            if (prevDistance < bestDistance) {
                bestDistance = prevDistance;
            }
        }

        return bestDistance;
    }

    /**
     * 方法4：质心优化法
     * 从几何质心开始，使用坐标下降法优化
     * 结合了几何直觉和数值优化
     */
    public double getMinDistSumCentroidOptimization(int[][] positions) {
        if (positions.length == 0) return 0.0;
        if (positions.length == 1) return 0.0;

        // 计算几何质心
        double centerX = 0.0, centerY = 0.0;
        for (int[] pos : positions) {
            centerX += pos[0];
            centerY += pos[1];
        }
        centerX /= positions.length;
        centerY /= positions.length;

        double currentX = centerX;
        double currentY = centerY;

        final int MAX_ITERATIONS = 5000;
        double stepSize = 0.01; // 改为非final变量
        double prevDistance = calculateTotalDistance(positions, currentX, currentY);

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            boolean improved = false;

            // 沿x方向优化
            for (int direction = -1; direction <= 1; direction += 2) {
                double step = direction * stepSize;
                double testX = currentX + step;
                double testDistance = calculateTotalDistance(positions, testX, currentY);

                if (testDistance < prevDistance) {
                    currentX = testX;
                    prevDistance = testDistance;
                    improved = true;
                }
            }

            // 沿y方向优化
            for (int direction = -1; direction <= 1; direction += 2) {
                double step = direction * stepSize;
                double testY = currentY + step;
                double testDistance = calculateTotalDistance(positions, currentX, testY);

                if (testDistance < prevDistance) {
                    currentY = testY;
                    prevDistance = testDistance;
                    improved = true;
                }
            }

            // 如果没有改进，减小步长
            if (!improved) {
                stepSize *= 0.9;
                if (stepSize < 1e-8) {
                    break; // 步长太小，停止优化
                }
            }
        }

        return prevDistance;
    }

    /**
     * 主方法：综合多种方法，取最优结果
     * 使用梯度下降法作为主要方法，结合随机搜索提高鲁棒性
     */
    public double getMinDistSum(int[][] positions) {
        if (positions.length == 0) return 0.0;
        if (positions.length == 1) return 0.0;

        // 对于小规模问题，使用网格搜索
        if (positions.length <= 10) {
            return getMinDistSumGridSearch(positions);
        }

        // 对于大规模问题，使用随机搜索+梯度下降的组合
        double result1 = getMinDistSumGradientDescent(positions);
        double result2 = getMinDistSumRandomSearch(positions);
        double result3 = getMinDistSumCentroidOptimization(positions);

        // 返回最好的结果
        return Math.min(Math.min(result1, result2), result3);
    }

}
