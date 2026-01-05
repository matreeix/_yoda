package algorithm.base.greedy;

import java.util.Arrays;

/**
 * 0/1背包问题的贪心近似方案 (0/1 Knapsack Greedy Approximation)
 * 注意：这不是最优解，只是近似解
 * 贪心策略：按单位重量价值降序选择物品
 * 对比：动态规划可以获得最优解，但时间复杂度更高
 */
public class ZeroOneKnapsack {

    static class Item {
        int weight;
        int value;
        String name;
        double valuePerWeight;

        Item(int weight, int value, String name) {
            this.weight = weight;
            this.value = value;
            this.name = name;
            this.valuePerWeight = (double) value / weight;
        }

        @Override
        public String toString() {
            return name + "(重量:" + weight + ", 价值:" + value + ", 单位价值:" +
                   String.format("%.2f", valuePerWeight) + ")";
        }
    }

    static class Result {
        int totalValue;
        int totalWeight;
        java.util.List<String> selectedItems;

        Result(int totalValue, int totalWeight, java.util.List<String> selectedItems) {
            this.totalValue = totalValue;
            this.totalWeight = totalWeight;
            this.selectedItems = selectedItems;
        }
    }

    /**
     * 贪心近似解决0/1背包问题
     * @param items 可选物品数组
     * @param capacity 背包容量
     * @return 贪心选择的物品和总价值
     */
    public static Result greedyKnapsack(Item[] items, int capacity) {
        if (items == null || items.length == 0 || capacity <= 0) {
            return new Result(0, 0, new java.util.ArrayList<>());
        }

        // 按单位重量价值降序排序
        Arrays.sort(items, (a, b) -> Double.compare(b.valuePerWeight, a.valuePerWeight));

        int totalValue = 0;
        int totalWeight = 0;
        java.util.List<String> selectedItems = new java.util.ArrayList<>();

        for (Item item : items) {
            // 如果物品重量不超过剩余容量，则完全放入
            if (totalWeight + item.weight <= capacity) {
                totalValue += item.value;
                totalWeight += item.weight;
                selectedItems.add(item.name + "(完整,价值:" + item.value + ")");
            }
            // 0/1背包不能部分放入，所以跳过
        }

        return new Result(totalValue, totalWeight, selectedItems);
    }

    /**
     * 动态规划最优解（用于对比）
     */
    public static Result dpKnapsack(Item[] items, int capacity) {
        if (items == null || items.length == 0 || capacity <= 0) {
            return new Result(0, 0, new java.util.ArrayList<>());
        }

        int n = items.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (items[i-1].weight <= w) {
                    dp[i][w] = Math.max(dp[i-1][w],
                                       dp[i-1][w - items[i-1].weight] + items[i-1].value);
                } else {
                    dp[i][w] = dp[i-1][w];
                }
            }
        }

        // 回溯找到选择的物品
        java.util.List<String> selectedItems = new java.util.ArrayList<>();
        int w = capacity;
        int v = dp[n][capacity];

        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i-1][w]) {
                selectedItems.add(0, items[i-1].name + "(完整,价值:" + items[i-1].value + ")");
                w -= items[i-1].weight;
            }
        }

        return new Result(v, capacity - w, selectedItems);
    }

    public static void main(String[] args) {
        // 测试用例
        Item[] items = {
            new Item(10, 60, "黄金"),
            new Item(20, 100, "白银"),
            new Item(30, 120, "钻石"),
            new Item(15, 45, "珠宝"),
            new Item(25, 80, "古董")
        };

        int capacity = 50;

        System.out.println("0/1背包问题对比测试：");
        System.out.println("背包容量：" + capacity);
        System.out.println("可用物品：");
        for (Item item : items) {
            System.out.println("  " + item);
        }

        // 贪心近似解
        Result greedyResult = greedyKnapsack(items, capacity);
        System.out.println("\n贪心近似解：");
        System.out.println("总价值：" + greedyResult.totalValue);
        System.out.println("总重量：" + greedyResult.totalWeight);
        System.out.println("选择的物品：");
        for (String item : greedyResult.selectedItems) {
            System.out.println("  " + item);
        }

        // 动态规划最优解
        Result dpResult = dpKnapsack(items, capacity);
        System.out.println("\n动态规划最优解：");
        System.out.println("总价值：" + dpResult.totalValue);
        System.out.println("总重量：" + dpResult.totalWeight);
        System.out.println("选择的物品：");
        for (String item : dpResult.selectedItems) {
            System.out.println("  " + item);
        }

        System.out.println("\n贪心解与最优解的差距：" +
                          (dpResult.totalValue - greedyResult.totalValue));
        System.out.println("贪心解相对最优解的比率：" +
                          String.format("%.2f", (double)greedyResult.totalValue / dpResult.totalValue));
    }
}
