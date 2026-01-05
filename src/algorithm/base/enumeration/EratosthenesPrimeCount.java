package algorithm.base.enumeration;

/**
 * 埃拉托斯特尼筛法质数计数
 * 时间复杂度: O(n log log n)
 * 通过标记合数来筛选质数
 */
public class EratosthenesPrimeCount {

    /**
     * 埃拉托斯特尼筛法统计小于n的质数数量
     * @param n 上限（不包含n）
     * @return 小于n的质数数量
     */
    public static int countPrimes(int n) {
        if (n <= 2) return 0;

        // 创建布尔数组，true表示可能是质数
        boolean[] isPrime = new boolean[n];
        // 初始化所有数为true（2到n-1）
        for (int i = 2; i < n; i++) {
            isPrime[i] = true;
        }

        // 从2开始筛除
        for (int i = 2; i * i < n; i++) {
            if (isPrime[i]) {
                // 标记i的倍数为合数
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        // 统计质数数量
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                count++;
            }
        }
        return count;
    }

    /**
     * 优化版本：使用位运算的空间优化版本（如果n很大可以使用）
     * 但为了清晰起见，这里使用标准实现
     */
    public static int countPrimesOptimized(int n) {
        if (n <= 2) return 0;

        boolean[] isPrime = new boolean[n];
        for (int i = 2; i < n; i++) {
            isPrime[i] = true;
        }

        // 只遍历到sqrt(n)
        for (int i = 2; i * i < n; i++) {
            if (isPrime[i]) {
                // 从i*i开始，避免重复标记
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime[i]) count++;
        }
        return count;
    }
}
