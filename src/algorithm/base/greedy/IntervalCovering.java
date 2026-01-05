package algorithm.base.greedy;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 区间覆盖问题 (Interval Covering Problem)
 * 问题：用最少的区间覆盖给定的点集合
 * 贪心策略：每次选择能够覆盖当前最远点的区间
 * 时间复杂度：O(n log n) 主要来自于排序
 */
public class IntervalCovering {

    static class Interval {
        int start;
        int end;
        String name;

        Interval(int start, int end, String name) {
            this.start = start;
            this.end = end;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + "[" + start + "," + end + "]";
        }

        /**
         * 检查点是否在区间内
         */
        boolean contains(int point) {
            return start <= point && point <= end;
        }
    }

    /**
     * 贪心解决区间覆盖问题
     * @param intervals 可选区间数组
     * @param points 需要覆盖的点集合
     * @return 最少的区间选择
     */
    public static Interval[] coverPoints(Interval[] intervals, int[] points) {
        if (intervals == null || intervals.length == 0 || points == null || points.length == 0) {
            return new Interval[0];
        }

        // 按区间起点排序
        Arrays.sort(intervals, Comparator.comparingInt(a -> a.start));

        java.util.List<Interval> selected = new java.util.ArrayList<>();
        int pointIndex = 0;
        int currentEnd = Integer.MIN_VALUE;

        while (pointIndex < points.length) {
            int currentPoint = points[pointIndex];

            // 找到所有能够覆盖当前点的区间中，结束点最远的
            Interval bestInterval = null;
            for (Interval interval : intervals) {
                if (interval.contains(currentPoint)) {
                    if (bestInterval == null || interval.end > bestInterval.end) {
                        bestInterval = interval;
                    }
                }
            }

            if (bestInterval == null) {
                // 无法覆盖当前点，返回null表示无解
                return null;
            }

            selected.add(bestInterval);
            currentEnd = bestInterval.end;

            // 跳过所有已经被当前区间覆盖的点
            while (pointIndex < points.length && points[pointIndex] <= currentEnd) {
                pointIndex++;
            }
        }

        return selected.toArray(new Interval[0]);
    }

    /**
     * 验证覆盖是否完整
     */
    public static boolean validateCoverage(Interval[] selected, int[] points) {
        if (selected == null || points == null) return false;

        for (int point : points) {
            boolean covered = false;
            for (Interval interval : selected) {
                if (interval.contains(point)) {
                    covered = true;
                    break;
                }
            }
            if (!covered) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // 测试用例
        Interval[] intervals = {
            new Interval(1, 4, "I1"),
            new Interval(2, 6, "I2"),
            new Interval(3, 5, "I3"),
            new Interval(4, 7, "I4"),
            new Interval(6, 8, "I5"),
            new Interval(7, 10, "I6")
        };

        int[] points = {1, 2, 3, 6, 7, 9};

        System.out.println("区间覆盖问题测试：");
        System.out.println("可用区间：");
        for (Interval interval : intervals) {
            System.out.println("  " + interval);
        }
        System.out.println("需要覆盖的点：" + Arrays.toString(points));

        Interval[] selected = coverPoints(intervals, points);

        if (selected != null) {
            System.out.println("\n选择的区间：");
            for (Interval interval : selected) {
                System.out.println("  " + interval);
            }
            System.out.println("总区间数：" + selected.length);

            // 验证覆盖
            boolean valid = validateCoverage(selected, points);
            System.out.println("覆盖验证：" + (valid ? "成功" : "失败"));
        } else {
            System.out.println("无法完全覆盖所有点");
        }
    }
}
