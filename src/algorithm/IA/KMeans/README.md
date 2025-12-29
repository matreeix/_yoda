# K-means聚类算法

## 算法概述
K-means是一种经典的无监督聚类算法，通过迭代优化将数据点分配到k个聚类中。

## 核心原理
1. **初始化**: 随机选择k个聚类中心
2. **分配**: 将每个点分配到最近的聚类中心
3. **更新**: 重新计算聚类中心
4. **迭代**: 重复直到收敛

## 经典应用
- 客户细分
- 图像压缩
- 文档聚类
- 异常检测

## 优势
- 算法简单高效
- 易于理解和实现
- 计算复杂度低
- 对大数据集有效

```java
// 使用示例
KMeans kmeans = new KMeans(k, maxIterations);
kmeans.fit(data);
int[] labels = kmeans.getLabels();
double[][] centroids = kmeans.getCentroids();
```
