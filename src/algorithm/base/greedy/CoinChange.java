package algorithm.base.greedy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 硬币找零问题 (Coin Change Problem)
 * 贪心策略：每次选择面值最大的可用硬币
 * 注意：这种贪心策略不是在所有货币体系下都正确
 * 对于美国货币体系(1,5,10,25美分)是正确的
 * 时间复杂度：O(n log n) 主要来自于排序
 */
public class CoinChange {

    /**
     * 使用贪心算法找零
     * @param coins 可用的硬币面值数组
     * @param amount 需要找零的金额
     * @return 使用的硬币数量和每种硬币的使用数量
     */
    public static Map<Integer, Integer> greedyCoinChange(int[] coins, int amount) {
        // 按面值从大到小排序
        Arrays.sort(coins);
        int[] sortedCoins = new int[coins.length];
        for (int i = 0; i < coins.length; i++) {
            sortedCoins[i] = coins[coins.length - 1 - i];
        }

        Map<Integer, Integer> coinCount = new HashMap<>();
        int remaining = amount;

        // 从面值最大的硬币开始尝试
        for (int coin : sortedCoins) {
            if (remaining >= coin) {
                int count = remaining / coin;
                coinCount.put(coin, count);
                remaining %= coin;
            }
        }

        // 如果还有剩余金额无法找零，返回null表示贪心失败
        if (remaining > 0) {
            return null;
        }

        return coinCount;
    }

    /**
     * 计算使用的硬币总数
     */
    public static int getTotalCoins(Map<Integer, Integer> coinCount) {
        if (coinCount == null) return -1;
        return coinCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) {
        // 测试用例1：美国货币体系
        int[] usCoins = {1, 5, 10, 25};
        int amount = 63; // 63美分 = 2*25 + 1*10 + 3*1

        System.out.println("美国货币体系测试：");
        System.out.println("可用硬币：" + Arrays.toString(usCoins));
        System.out.println("需要找零：" + amount + " 美分");

        Map<Integer, Integer> result = greedyCoinChange(usCoins, amount);
        if (result != null) {
            System.out.println("找零结果：");
            for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                System.out.println(entry.getValue() + " 个 " + entry.getKey() + " 美分硬币");
            }
            System.out.println("总硬币数：" + getTotalCoins(result));
        } else {
            System.out.println("无法用贪心算法完成找零");
        }

        // 测试用例2：可能导致贪心失败的货币体系
        int[] problemCoins = {1, 3, 4};
        amount = 6;

        System.out.println("\n问题货币体系测试：");
        System.out.println("可用硬币：" + Arrays.toString(problemCoins));
        System.out.println("需要找零：" + amount);

        result = greedyCoinChange(problemCoins, amount);
        if (result != null) {
            System.out.println("贪心找零结果：");
            for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                System.out.println(entry.getValue() + " 个 " + entry.getKey() + " 硬币");
            }
            System.out.println("总硬币数：" + getTotalCoins(result));
        } else {
            System.out.println("贪心算法失败，无法完成找零");
        }
        System.out.println("正确的最少硬币数应该是：2个3硬币");
    }
}
