package algorithm.base.greedy;

import java.util.Arrays;

/**
 * 部分背包问题 (Fractional Knapsack Problem)
 * 贪心策略：优先选择单位重量价值最高的物品，可以取部分物品
 * 时间复杂度：O(n log n) 主要来自于排序
 */
public class FractionalKnapsack {

    static class Item {
        int weight;
        int value;
        String name;
        double valuePerWeight; // 单位重量价值

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
        double totalValue;
        java.util.List<String> selectedItems;

        Result(double totalValue, java.util.List<String> selectedItems) {
            this.totalValue = totalValue;
            this.selectedItems = selectedItems;
        }
    }

    /**
     * 贪心解决部分背包问题
     * @param items 可选物品数组
     * @param capacity 背包容量
     * @return 最大价值和选择的物品
     */
    public static Result fractionalKnapsack(Item[] items, int capacity) {
        if (items == null || items.length == 0 || capacity <= 0) {
            return new Result(0, new java.util.ArrayList<>());
        }

        // 按单位重量价值降序排序
        Arrays.sort(items, (a, b) -> Double.compare(b.valuePerWeight, a.valuePerWeight));

        double totalValue = 0.0;
        java.util.List<String> selectedItems = new java.util.ArrayList<>();
        int remainingCapacity = capacity;

        for (Item item : items) {
            if (remainingCapacity <= 0) break;

            if (item.weight <= remainingCapacity) {
                // 完全放入物品
                totalValue += item.value;
                remainingCapacity -= item.weight;
                selectedItems.add(item.name + "(完整)");
            } else {
                // 部分放入物品
                double fraction = (double) remainingCapacity / item.weight;
                totalValue += item.value * fraction;
                selectedItems.add(item.name + "(部分:" + String.format("%.2f", fraction * 100) + "%)");
                remainingCapacity = 0;
            }
        }

        return new Result(totalValue, selectedItems);
    }

    public static void main(String[] args) {
        // 测试用例
        Item[] items = {
            new Item(10, 60, "黄金"),
            new Item(20, 100, "白银"),
            new Item(30, 120, "钻石"),
            new Item(15, 45, "珠宝")
        };

        int capacity = 50;

        System.out.println("部分背包问题测试：");
        System.out.println("背包容量：" + capacity);
        System.out.println("可用物品：");
        for (Item item : items) {
            System.out.println("  " + item);
        }

        Result result = fractionalKnapsack(items, capacity);

        System.out.println("\n贪心选择结果：");
        System.out.println("最大价值：" + String.format("%.2f", result.totalValue));
        System.out.println("选择的物品：");
        for (String item : result.selectedItems) {
            System.out.println("  " + item);
        }

        // 验证结果：黄金(10,60)+白银(20,100)+珠宝部分(20/15*45=60) = 60+100+60=220
        System.out.println("\n验证：预期最大价值应该为220.00");
    }
}
