# 贪心算法 (Greedy Algorithm)

贪心算法是一种在每一步选择中都采取在当前状态下最好或最优的选择，从而希望导致结果是全局最好或最优的算法。

## 经典问题与实现

### 1. 活动选择问题 (Activity Selection)
**文件**: `ActivitySelection.java`
- **问题**: 从多个有冲突的活动中选择最多的活动
- **贪心策略**: 总是选择结束时间最早的活动
- **时间复杂度**: O(n log n)
- **应用场景**: 会议室安排、任务调度等

### 2. 硬币找零问题 (Coin Change)
**文件**: `CoinChange.java`
- **问题**: 用最少的硬币凑齐指定金额
- **贪心策略**: 每次选择面值最大的可用硬币
- **时间复杂度**: O(n log n)
- **注意**: 不是在所有货币体系下都正确（如{1,3,4}无法正确找零6）
- **应用场景**: 货币找零、资源分配

### 3. 部分背包问题 (Fractional Knapsack)
**文件**: `FractionalKnapsack.java`
- **问题**: 背包能装部分物品，最大化总价值
- **贪心策略**: 按单位重量价值降序选择，可取部分物品
- **时间复杂度**: O(n log n)
- **应用场景**: 资源分配、投资组合优化

### 4. 区间覆盖问题 (Interval Covering)
**文件**: `IntervalCovering.java`
- **问题**: 用最少的区间覆盖给定的点集合
- **贪心策略**: 每次选择能够覆盖当前最远点的区间
- **时间复杂度**: O(n log n)
- **应用场景**: 无线网络覆盖、传感器部署

### 5. 0/1背包问题的贪心近似 (0/1 Knapsack Approximation)
**文件**: `ZeroOneKnapsack.java`
- **问题**: 物品不可分割的最大化背包价值
- **贪心策略**: 按单位重量价值降序选择（近似解）
- **时间复杂度**: O(n log n)
- **注意**: 这不是最优解，动态规划可以获得最优解
- **对比**: 提供贪心解与动态规划最优解的对比

## 贪心算法的特点

### 优点
- 简单直观，易于实现
- 通常时间复杂度较低
- 对于某些问题能获得最优解

### 局限性
- 不保证全局最优解
- 对问题的贪心选择性质有严格要求
- 可能需要数学证明贪心策略的正确性

### 适用条件
- 贪心选择性质：每一步的局部最优选择能导致全局最优
- 最优子结构：问题的最优解包含子问题的最优解

## 运行测试

每个Java文件都包含main方法，可以直接运行查看测试结果：

```bash
javac ActivitySelection.java && java ActivitySelection
javac CoinChange.java && java CoinChange
javac FractionalKnapsack.java && java FractionalKnapsack
javac IntervalCovering.java && java IntervalCovering
javac ZeroOneKnapsack.java && java ZeroOneKnapsack
```
