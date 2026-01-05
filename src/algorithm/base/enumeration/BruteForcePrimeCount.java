package algorithm.base.enumeration;

/**
 * 暴力枚举法质数计数
 * 时间复杂度: O(n * sqrt(n))
 * 对于每个数从2到n-1，检查是否为质数
 */
public class BruteForcePrimeCount {

    /**
     * 检查一个数是否为质数
     * @param num 要检查的数
     * @return true 如果是质数，false 否则
     */
    private static boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;

        // 检查从3到sqrt(num)的奇数
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 暴力枚举法统计小于n的质数数量
     * @param n 上限（不包含n）
     * @return 小于n的质数数量
     */
    public static int countPrimes(int n) {
        if (n <= 2) return 0;

        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }
}
