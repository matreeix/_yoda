# 遗传算法 (Genetic Algorithm)

## 算法概述

遗传算法是一种模拟生物进化过程的优化算法，通过模拟自然选择、交叉和变异等遗传机制，在解空间中搜索最优解。

## 核心原理

1. **编码**：将问题解表示为染色体（基因序列）
2. **适应度评估**：计算每个个体的适应度值
3. **选择**：根据适应度选择优秀的个体
4. **交叉**：两个父代个体产生子代个体
5. **变异**：随机改变个体的基因
6. **迭代**：重复以上过程直到满足停止条件

## 算法流程

```
初始化种群 → 评估适应度 → 选择操作 → 交叉操作 → 变异操作 → 新一代种群 → 评估适应度 → ...
```

## 代码结构

- `Chromosome.java`: 染色体类，实现个体的基因表示和遗传操作
- `GeneticAlgorithm.java`: 遗传算法主类，实现算法流程控制
- `FunctionOptimization.java`: 函数优化应用示例

## 经典应用

### 1. 函数优化
- 单峰/多峰函数优化
- 多目标优化问题
- 约束优化问题

### 2. 组合优化
- 旅行商问题 (TSP)
- 背包问题
- 调度问题

### 3. 机器学习
- 特征选择
- 神经网络权重优化
- 超参数调优

### 4. 工程应用
- 结构优化设计
- 路径规划
- 资源分配

## 参数说明

- **种群大小 (populationSize)**: 影响算法的搜索空间，通常50-200
- **变异率 (mutationRate)**: 控制基因变异的概率，通常0.001-0.1
- **交叉率 (crossoverRate)**: 控制交叉操作的概率，通常0.6-0.9
- **最大代数 (maxGenerations)**: 算法停止条件，通常100-1000

## 优势

- 全局搜索能力强
- 并行性好，适合大规模问题
- 对问题要求低，无需连续可导
- 鲁棒性强，对初始解不敏感

## 局限性

- 收敛速度慢
- 参数选择困难
- 容易陷入局部最优
- 对编码方式敏感

## 使用建议

1. 合理设置算法参数
2. 选择合适的编码方式
3. 结合其他算法使用（如局部搜索）
4. 适当增大种群规模以提高搜索质量

## 运行示例

```bash
cd src/algorithm/IA/GA
javac *.java
java FunctionOptimization
```

## 参考文献

1. Holland, J. H. (1975). Adaptation in Natural and Artificial Systems.
2. Goldberg, D. E. (1989). Genetic Algorithms in Search, Optimization and Machine Learning.
3. Mitchell, M. (1998). An Introduction to Genetic Engineering.
