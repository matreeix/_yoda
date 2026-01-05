package algorithm.math.geometry;

import java.util.*;

/**
 * @Description: 149. 直线上最多的点数
 * 给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。求最多有多少个点在同一条直线上。
 *
 * 算法分析：
 * - 时间复杂度：O(n²)，这是理论最优，因为最坏情况下所有点共线，需要检查所有点对
 * - 空间复杂度：O(n)，用于存储斜率计数
 * - 关键优化：使用GCD约分斜率，避免浮点精度问题；处理重复点和垂直线情况
 *
 * @Date: 2026/1/5
 */

public class PointsOnLine {

    /**
     * 计算直线上最多的点数
     * @param points 二维数组，每个元素为[x,y]坐标
     * @return 最多共线点数
     */
    public static int maxPoints(int[][] points) {
        // 处理边界情况
        if (points == null) return 0;
        int n = points.length;
        if (n <= 2) return n;

        int maxCount = 2; // 最少有2个点

        // 遍历每个基准点
        for (int i = 0; i < n; i++) {
            // 使用HashMap统计不同斜率的点数
            // key: 斜率的标准化字符串表示(dx/dy)
            // value: 该斜率下的点数
            Map<String, Integer> slopeCount = new HashMap<>();

            // duplicate记录与基准点相同的重复点数量
            int duplicate = 1;

            // 检查基准点i与其他所有点的关系
            for (int j = i + 1; j < n; j++) {
                int dx = points[j][0] - points[i][0]; // x坐标差
                int dy = points[j][1] - points[i][1]; // y坐标差

                // 处理重复点：如果dx和dy都为0，表示是同一个点
                if (dx == 0 && dy == 0) {
                    duplicate++;
                    continue; // 跳过重复点，不计入斜率统计
                }

                // 计算dx和dy的最大公约数，用于约分斜率
                int g = gcd(Math.abs(dx), Math.abs(dy));
                dx /= g; // 约分后的x差
                dy /= g; // 约分后的y差

                // 标准化斜率表示：确保相同的斜率有相同的字符串表示
                // 规则：dx >= 0，或者dx=0时dy >= 0
                if (dx < 0 || (dx == 0 && dy < 0)) {
                    dx = -dx;
                    dy = -dy;
                }

                // 生成斜率的唯一字符串表示
                String key = dx + "/" + dy;
                slopeCount.put(key, slopeCount.getOrDefault(key, 0) + 1);
            }

            // 计算当前基准点的最大共线点数
            // 1. 考虑不同斜率的情况：斜率点数 + 重复点数
            for (int count : slopeCount.values()) {
                maxCount = Math.max(maxCount, count + duplicate);
            }
            // 2. 考虑只有重复点的情况
            maxCount = Math.max(maxCount, duplicate);
        }

        return maxCount;
    }

    /**
     * 计算两个数的最大公约数
     * 使用欧几里得算法的递归实现
     * @param a 第一个数
     * @param b 第二个数
     * @return 最大公约数
     */
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：三个点完全共线
        int[][] points = {{1, 1}, {2, 2}, {3, 3}};
        System.out.println("测试用例1结果: " + maxPoints(points)); // 预期输出: 3

        // 测试用例2：六个点，其中四个共线
        int[][] points1 = {{1, 1}, {3, 2}, {5, 3}, {4, 1}, {2, 3}, {1, 4}};
        System.out.println("测试用例2结果: " + maxPoints(points1)); // 预期输出: 4

        // 测试用例3：包含重复点
        int[][] points2 = {{0, 0}, {1, 1}, {0, 0}, {2, 2}};
        System.out.println("测试用例3结果: " + maxPoints(points2)); // 预期输出: 3

        // 测试用例4：垂直线
        int[][] points3 = {{0, 0}, {0, 1}, {0, 2}, {1, 0}};
        System.out.println("测试用例4结果: " + maxPoints(points3)); // 预期输出: 3
    }
}
