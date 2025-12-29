# A*搜索算法

## 算法概述
A*是一种启发式搜索算法，通过评估每个节点到起点的代价和到终点的估计代价来寻找最优路径。

## 核心原理
1. **启发函数**: 估计从当前节点到目标节点的代价
2. **优先队列**: 按f(n) = g(n) + h(n)排序
3. **最优性**: 在可接受的启发函数下保证找到最优路径

## 经典应用
- 路径规划
- 游戏AI
- 机器人导航
- 路由算法

## 优势
- 保证找到最优路径
- 比Dijkstra算法更高效
- 灵活的启发函数设计

```java
// 使用示例
AStarSearch astar = new AStarSearch(grid, startX, startY, goalX, goalY);
List<Node> path = astar.search();
astar.printGridWithPath(path);
```
