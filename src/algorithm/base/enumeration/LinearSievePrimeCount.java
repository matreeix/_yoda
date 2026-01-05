package algorithm.base.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * 线性筛法（欧拉筛法）质数计数
 * 时间复杂度: O(n)
 * 确保每个合数只被其最小质因子筛除一次
 */
public class LinearSievePrimeCount {

    /**
     * 线性筛法统计小于n的质数数量
     * @param n 上限（不包含n）
     * @return 小于n的质数数量
     */
    public static int countPrimes(int n) {
        if (n <= 2) return 0;

        boolean[] isPrime = new boolean[n];
        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i < n; i++) {
            isPrime[i] = true;
        }

        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                // i是质数，加入质数列表
                primes.add(i);
            }

            // 用已找到的质数筛除
            for (int prime : primes) {
                // 避免越界
                if (i * prime >= n) break;

                isPrime[i * prime] = false;

                // 如果i能被当前质数整除，说明i*prime的最小质因子是prime
                // 此时应该停止，避免重复筛除
                if (i % prime == 0) {
                    break;
                }
            }
        }

        return primes.size();
    }

    /**
     * 另一种线性筛法的实现方式（更简洁）
     * 返回质数列表的同时计算数量
     */
    public static int countPrimesAlternative(int n) {
        if (n <= 2) return 0;

        boolean[] isComposite = new boolean[n];
        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i < n; i++) {
            if (!isComposite[i]) {
                primes.add(i);
            }

            // 对于每个已知的质数，标记其倍数
            for (int prime : primes) {
                if (i * prime >= n) break;

                isComposite[i * prime] = true;

                // 关键优化：如果i是prime的倍数，停止
                // 这样确保每个合数只被其最小质因子标记
                if (i % prime == 0) {
                    break;
                }
            }
        }

        return primes.size();
    }

    /**
     * 获取小于n的所有质数列表
     * @param n 上限
     * @return 质数列表
     */
    public static List<Integer> getPrimes(int n) {
        List<Integer> primes = new ArrayList<>();
        if (n <= 2) return primes;

        boolean[] isComposite = new boolean[n];

        for (int i = 2; i < n; i++) {
            if (!isComposite[i]) {
                primes.add(i);
            }

            for (int prime : primes) {
                if (i * prime >= n) break;
                isComposite[i * prime] = true;
                if (i % prime == 0) break;
            }
        }

        return primes;
    }
}
