package algorithm.base.enumeration;

import java.util.Scanner;

/**
 * 质数计数算法测试类
 * 比较不同时间复杂度的算法性能
 */
public class PrimeCountTest {

    public static void main(String[] args) {
        System.out.println("质数计数算法性能比较");
        System.out.println("====================");

        // 测试用例
        int[] testCases = {10, 100, 1000, 10000, 50000};

        System.out.printf("%-8s %-15s %-20s %-15s %-15s%n",
                         "n", "暴力枚举", "埃拉托斯特尼筛法", "线性筛法", "结果验证");
        System.out.println("-------- --------------- -------------------- --------------- ---------------");

        for (int n : testCases) {
            // 暴力枚举法
            long startTime = System.nanoTime();
            int bruteForceResult = BruteForcePrimeCount.countPrimes(n);
            long bruteForceTime = System.nanoTime() - startTime;

            // 埃拉托斯特尼筛法
            startTime = System.nanoTime();
            int eratosthenesResult = EratosthenesPrimeCount.countPrimes(n);
            long eratosthenesTime = System.nanoTime() - startTime;

            // 线性筛法
            startTime = System.nanoTime();
            int linearSieveResult = LinearSievePrimeCount.countPrimes(n);
            long linearSieveTime = System.nanoTime() - startTime;

            // 验证结果是否一致
            boolean resultsMatch = (bruteForceResult == eratosthenesResult) &&
                                  (eratosthenesResult == linearSieveResult);

            System.out.printf("%-8d %-15s %-20s %-15s %-15s%n",
                             n,
                             formatTime(bruteForceTime),
                             formatTime(eratosthenesTime),
                             formatTime(linearSieveTime),
                             resultsMatch ? "✓" : "✗");
        }

        // 交互式测试
        System.out.println("\n交互式测试模式");
        System.out.println("输入一个正整数n（建议n <= 1000000），程序将计算小于n的质数数量：");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入n (输入0退出): ");
            try {
                int n = scanner.nextInt();
                if (n == 0) break;
                if (n < 0) {
                    System.out.println("请输入正整数！");
                    continue;
                }

                System.out.println("选择算法：");
                System.out.println("1. 暴力枚举法 (O(n*sqrt(n)))");
                System.out.println("2. 埃拉托斯特尼筛法 (O(n log log n))");
                System.out.println("3. 线性筛法 (O(n))");
                System.out.println("4. 比较所有算法性能");
                System.out.print("请选择 (1-4): ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        testBruteForce(n);
                        break;
                    case 2:
                        testEratosthenes(n);
                        break;
                    case 3:
                        testLinearSieve(n);
                        break;
                    case 4:
                        compareAll(n);
                        break;
                    default:
                        System.out.println("无效选择，请重新输入！");
                }

            } catch (Exception e) {
                System.out.println("输入无效，请输入整数！");
                scanner.nextLine(); // 清除缓冲区
            }
        }

        scanner.close();
        System.out.println("程序结束！");
    }

    private static void testBruteForce(int n) {
        System.out.println("\n=== 暴力枚举法测试 ===");
        long startTime = System.nanoTime();
        int result = BruteForcePrimeCount.countPrimes(n);
        long time = System.nanoTime() - startTime;

        System.out.println("小于 " + n + " 的质数数量: " + result);
        System.out.println("执行时间: " + formatTime(time));
    }

    private static void testEratosthenes(int n) {
        System.out.println("\n=== 埃拉托斯特尼筛法测试 ===");
        long startTime = System.nanoTime();
        int result = EratosthenesPrimeCount.countPrimes(n);
        long time = System.nanoTime() - startTime;

        System.out.println("小于 " + n + " 的质数数量: " + result);
        System.out.println("执行时间: " + formatTime(time));
    }

    private static void testLinearSieve(int n) {
        System.out.println("\n=== 线性筛法测试 ===");
        long startTime = System.nanoTime();
        int result = LinearSievePrimeCount.countPrimes(n);
        long time = System.nanoTime() - startTime;

        System.out.println("小于 " + n + " 的质数数量: " + result);
        System.out.println("执行时间: " + formatTime(time));
    }

    private static void compareAll(int n) {
        System.out.println("\n=== 性能比较 (n = " + n + ") ===");

        // 暴力枚举法
        long startTime = System.nanoTime();
        int bruteResult = BruteForcePrimeCount.countPrimes(n);
        long bruteTime = System.nanoTime() - startTime;

        // 埃拉托斯特尼筛法
        startTime = System.nanoTime();
        int eraResult = EratosthenesPrimeCount.countPrimes(n);
        long eraTime = System.nanoTime() - startTime;

        // 线性筛法
        startTime = System.nanoTime();
        int linearResult = LinearSievePrimeCount.countPrimes(n);
        long linearTime = System.nanoTime() - startTime;

        System.out.printf("%-20s %-10s %-15s%n", "算法", "结果", "时间");
        System.out.println("-------------------- ---------- ---------------");
        System.out.printf("%-20s %-10d %-15s%n", "暴力枚举法", bruteResult, formatTime(bruteTime));
        System.out.printf("%-20s %-10d %-15s%n", "埃拉托斯特尼筛法", eraResult, formatTime(eraTime));
        System.out.printf("%-20s %-10d %-15s%n", "线性筛法", linearResult, formatTime(linearTime));

        boolean consistent = (bruteResult == eraResult) && (eraResult == linearResult);
        System.out.println("\n结果一致性: " + (consistent ? "✓ 所有算法结果相同" : "✗ 算法结果不一致"));
    }

    /**
     * 格式化时间显示
     */
    private static String formatTime(long nanoTime) {
        if (nanoTime < 1000) {
            return nanoTime + " ns";
        } else if (nanoTime < 1000000) {
            return String.format("%.2f μs", nanoTime / 1000.0);
        } else if (nanoTime < 1000000000) {
            return String.format("%.2f ms", nanoTime / 1000000.0);
        } else {
            return String.format("%.2f s", nanoTime / 1000000000.0);
        }
    }
}
