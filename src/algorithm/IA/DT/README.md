# 决策树 (Decision Tree)

## 算法概述
决策树是一种树状结构分类器，通过学习简单的决策规则从数据特征推断目标值。

## 核心原理
1. **递归分割**: 递归地将数据分割成子集
2. **特征选择**: 选择最优分割特征
3. **停止条件**: 确定树停止生长的条件

## 经典应用
- 分类问题
- 回归问题
- 特征选择

## 优势
- 易于理解和解释
- 处理混合类型数据
- 计算复杂度低

```java
// 使用示例
DecisionTree dt = new DecisionTree();
dt.train(data, attributes, targetAttribute);
String prediction = dt.predict(sample, attributes);
```
