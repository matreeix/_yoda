# 推箱子（Sokoban）— 游戏分析与经典解法

## 一、游戏简介

**推箱子**（Sokoban，日语「倉庫番」）是一款 1981 年由日本 Thinking Rabbit 公司推出的益智游戏。玩家在仓库迷宫中控制工人，将所有箱子推到指定目标格上，完成关卡。

与「走迷宫」不同，推箱子的核心难点在于：

- **动作不可逆**：箱子只能推、不能拉，很多错误推法会永久锁死局面；
- **状态空间爆炸**：玩家位置 × 各箱子位置 × 各箱子排列顺序，组合数随箱子数指数增长；
- **全局约束**：必须同时考虑所有箱子的协调移动，局部最优常导致全局无解。

推箱子已被证明是 **PSPACE-complete** 问题——比 NP-complete 更难，属于「多项式空间可解，但很可能不存在多项式时间算法」的类别。工业级求解器对中等关卡也可能需要数秒到数分钟。

---

## 二、地图元素与规则

### 2.1 标准符号

| 符号 | 含义 |
|------|------|
| `#` | 墙（不可通过） |
| `@` | 玩家 |
| `$` | 箱子 |
| `.` | 目标点 |
| ` `（空格） | 可行走地面 |
| `*` | 玩家在目标点上 |
| `+` | 箱子在目标点上 |
| `X` | 墙（部分关卡格式） |

### 2.2 合法操作

玩家在上下左右四方向移动，规则如下：

1. 目标格为**空格或目标点** → 玩家移动过去；
2. 目标格为**箱子**，且箱子后方一格为空格或目标点 → 玩家推箱子并移动；
3. 目标格为**墙**，或箱子后方被墙/另一箱子挡住 → 不可移动。

### 2.3 胜利条件

所有箱子都位于目标点上（`+`），玩家位置不限。

### 2.4 经典关卡示例

```
  #####
  #   #
  #$  #
  #  .#
  # @ #
  #####
```

目标：将 `$` 推到 `.` 上。

---

## 三、问题形式化

### 3.1 状态表示

一个局面可用以下信息唯一描述：

```
State = (playerPos, boxPositions)
```

- `playerPos`：玩家坐标 `(x, y)`
- `boxPositions`：所有箱子坐标集合，通常按字典序排序后编码，以消除排列顺序带来的冗余

**注意**：玩家从 A 空走到 B 空、中间不推箱子的路径，在推箱子语义下属于**同一状态**。因此搜索时应做 **路径压缩（Move Compression）**：一次扩展只记录「推箱动作」，中间纯行走合并处理。

### 3.2 动作空间

每一步有效动作 = 四个方向中，能推动某个箱子的方向。纯行走不单独作为搜索层，而是在推箱前用 BFS 计算玩家可达区域。

### 3.3 复杂度

| 指标 | 典型规模 |
|------|----------|
| 地图大小 | 7×7 ～ 30×30 |
| 箱子数 k | 1 ～ 40+ |
| 分支因子 | 1 ～ 4（每步最多推一个方向） |
| 状态数 | O(n² × C(n², k))，n 为地图边长 |

Sokoban 是 **PSPACE-complete**（Culberson, 1997），意味着：

- 存在使用多项式空间的算法（BFS/DFS 配合合适剪枝）；
- 不太可能有多项式时间的精确求解算法；
- 实际求解高度依赖**剪枝**与**启发式**。

---

## 四、无解局面：死锁（Deadlock）

剪枝是推箱子求解的核心。大量局面看起来还能动，实际上已永远无法完成，称为**死锁**。

### 4.1 角落死锁（Corner Deadlock）

箱子在角落且该角不是目标点 → 永远无法推出。

```
#$
#    ← 箱子被两面墙夹住
```

### 4.2 墙边死锁（Wall Deadlock）

箱子贴墙且沿墙方向不存在目标点，且无法离开墙边 → 死锁。

```
#####
# $ .    ← 若目标点不在同一行，且上方有墙，箱子只能沿墙滑动
#####
```

### 4.3 冻结死锁（Freeze Deadlock）

一组箱子与墙形成连通块，块内无目标点，且整体无法移动 → 死锁。

### 4.4 简单死锁检测算法

```java
// 伪代码：检测箱子是否在非目标角落
boolean isCornerDeadlock(int bx, int by, char[][] map) {
    if (map[bx][by] == '+') return false; // 已在目标上
    boolean wallUp    = map[bx-1][by] == '#';
    boolean wallDown  = map[bx+1][by] == '#';
    boolean wallLeft  = map[bx][by-1] == '#';
    boolean wallRight = map[bx][by+1] == '#';
    return (wallUp && wallLeft) || (wallUp && wallRight)
        || (wallDown && wallLeft) || (wallDown && wallRight);
}
```

工业求解器还会使用 **Deadlock Table**、**Pattern Database** 等预计算结构，在搜索前或扩展时过滤大量无效状态。

---

## 五、经典解法概览

```
                    ┌─────────────────────────────────────┐
                    │           推箱子求解策略            │
                    └─────────────────────────────────────┘
                                      │
          ┌───────────────────────────┼───────────────────────────┐
          ▼                           ▼                           ▼
   ┌─────────────┐            ┌─────────────┐            ┌─────────────┐
   │  暴力搜索    │            │  启发式搜索  │            │  逆向搜索    │
   │ BFS / DFS   │            │ A* / IDA*   │            │ Pull / BFS  │
   └─────────────┘            └─────────────┘            └─────────────┘
          │                           │                           │
    小关卡、教学用              最常用、平衡效果          某些关卡更快
```

---

## 六、解法一：广度优先搜索（BFS）

### 6.1 思路

从初始状态出发，逐层扩展所有合法推箱动作，第一次到达「全部箱子上靶」的状态即为**最短推箱步数**解（若只计推箱次数）。

### 6.2 关键实现细节

1. **状态去重**：`HashSet<State>` 或位压缩哈希；
2. **玩家可达性**：每次扩展前，对当前箱子布局做 BFS，求玩家在不推箱情况下的可达区域；
3. **死锁剪枝**：扩展前检测，无效状态不入队。

### 6.3 伪代码

```java
Queue<State> queue = new LinkedList<>();
Set<State> visited = new HashSet<>();

queue.offer(initialState);
visited.add(initialState);

while (!queue.isEmpty()) {
    State cur = queue.poll();
    if (cur.allBoxesOnTargets()) return cur.path;

    Set<Position> reachable = bfsPlayerReachable(cur);

    for (Direction dir : Direction.values()) {
        State next = tryPush(cur, dir, reachable);
        if (next != null && !visited.contains(next) && !hasDeadlock(next)) {
            visited.add(next);
            queue.offer(next);
        }
    }
}
return null; // 无解
```

### 6.4 优缺点

| 优点 | 缺点 |
|------|------|
| 保证最短解（推箱步数意义下） | 空间 O(状态数)，稍大即爆内存 |
| 实现直观，适合教学 | 无启发式时扩展极慢 |

**适用**：箱子数 ≤ 3、地图 ≤ 10×10 的小关卡。

---

## 七、解法二：A* 搜索

### 7.1 思路

在 BFS 基础上引入启发函数 `h(s)`，评估当前状态到目标的「估计代价」：

```
f(s) = g(s) + h(s)
```

- `g(s)`：从起点到当前状态已发生的推箱步数；
- `h(s)`：估计还需多少步（必须**可采纳**：不高估真实代价，才能保证最优）。

使用**优先队列**每次取出 `f` 最小的状态扩展。

### 7.2 常用启发函数

#### （1）曼哈顿距离之和（最常用）

```java
int h(State s) {
    int sum = 0;
    for (Position box : s.boxes) {
        sum += minManhattan(box, targets); // 每个箱子到最近目标的曼哈顿距离
    }
    return sum;
}
```

- 忽略箱子之间的阻挡，一定不高估 → **可采纳**；
- 计算快，效果中等。

#### （2）最小费用匹配（Hungarian / 贪心匹配）

将每个箱子匹配一个不同目标，使总曼哈顿距离最小。比简单求和更紧，但仍可采纳（因忽略障碍）。

#### （3）不相交启发式（Disjoint Pattern Heuristics）

预计算「某几个箱子+目标子问题」的最优代价表，搜索时查表相加。效果强，是高性能求解器的核心之一。

### 7.3 伪代码

```java
PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
Set<State> closed = new HashSet<>();

open.offer(new Node(initialState, 0, h(initialState), null));

while (!open.isEmpty()) {
    Node cur = open.poll();
    if (cur.state.isGoal()) return reconstruct(cur);

    if (closed.contains(cur.state)) continue;
    closed.add(cur.state);

    for (State next : expand(cur.state)) {
        if (closed.contains(next) || hasDeadlock(next)) continue;
        int g = cur.g + 1;
        open.offer(new Node(next, g, h(next), cur));
    }
}
```

### 7.4 优缺点

| 优点 | 缺点 |
|------|------|
| 有良好启发式时远快于 BFS | 启发式设计决定性能 |
| 可采纳启发式保证最优 | 仍要存储大量 Open/Close 状态 |

**适用**：中等规模关卡（5～15 个箱子），是工程实践的首选框架。

---

## 八、解法三：迭代加深 A*（IDA*）

### 8.1 思路

A* 的主要内存开销来自 Open 表。IDA* 用**迭代加深**替代：

1. 设阈值 `limit = h(初始状态)`；
2. 深度优先搜索，只扩展 `f = g + h ≤ limit` 的节点；
3. 若未找到解，令 `limit = 本次搜索遇到的最小超额 f 值`，重复；
4. 直到找到目标或证明无解。

### 8.2 伪代码

```java
int idaStar(State start) {
    int limit = h(start);
    while (true) {
        int[] nextLimit = {Integer.MAX_VALUE};
        if (dfs(start, 0, limit, nextLimit)) return foundSteps;
        if (nextLimit[0] == Integer.MAX_VALUE) return -1; // 无解
        limit = nextLimit[0];
    }
}

boolean dfs(State s, int g, int limit, int[] nextLimit) {
    int f = g + h(s);
    if (f > limit) { nextLimit[0] = min(nextLimit[0], f); return false; }
    if (s.isGoal()) return true;
    for (State next : expand(s)) {
        if (dfs(next, g + 1, limit, nextLimit)) return true;
    }
    return false;
}
```

### 8.3 优缺点

| 优点 | 缺点 |
|------|------|
| 内存 O(深度)，适合大状态空间 | 重复扩展节点，时间可能更长 |
| 与强启发式配合效果极佳 | 启发式弱时不如 A* |

**适用**：内存受限、启发式较强、或箱子较多的关卡。Microban、XSokoban 等求解器常用 IDA*。

---

## 九、解法四：逆向搜索（Reverse / Pull Search）

### 9.1 思路

正向推箱：玩家推箱子，分支多、易死锁。

**逆向**思考：从「全部箱子已在目标点」的**结束状态**出发，执行**拉箱子**（Pull）操作——若玩家站在箱子某一侧的空格，且箱子另一侧为空，则可以把箱子拉向玩家。

逆向搜索的状态空间往往更小，因为：

- 结束状态唯一（或很少）；
- Pull 操作等价于撤销 Push，某些关卡逆向 BFS 更快。

### 9.2 Pull 规则

```
玩家位置 P，箱子 B，B 的反方向下一格 D：
若 D 为空或目标，且 P 与 B 相邻 → 可将 B 拉至 P 原方向的对侧
（等价于正向「从 P 推 B 离开 P」的逆操作）
```

### 9.3 双向搜索

结合正向 Push 与逆向 Pull，当两侧搜索 frontier 相遇时得到解。适合对称性较好的关卡。

### 9.4 优缺点

| 优点 | 缺点 |
|------|------|
| 某些关卡状态空间大幅缩小 | 实现比正向搜索复杂 |
| 可与其他方法组合 | 并非对所有关卡都更快 |

---

## 十、解法五：模式数据库（Pattern Database）

### 10.1 思路

将 k 个箱子拆成若干**不相交子集**（如 2+2 或 3+3），对每个子集离线 BFS 预计算：

```
PD[子集布局] = 该子集箱子到达各自目标的最少推箱步数（忽略其他箱子，仅作障碍）
```

搜索时：

```java
h(s) = PD1(s 的子布局1) + PD2(s 的子布局2) + ...
```

各子问题**不相交** → 启发值相加仍**可采纳**，且比曼哈顿求和更紧。

### 10.2 流程

```
离线阶段：
  对子集 S（如 2 个箱子）枚举所有可达布局
  BFS 求每个布局到「S 中箱子各就各位」的最短步数
  存入 PD_S

在线搜索：
  当前状态 s → 提取各子集布局 → 查表求和 → 作为 h(s)
```

### 10.3 效果

这是**竞赛级 / 工业级** Sokoban 求解器的标配，可将 A*/IDA* 速度提升数个数量级，但预计算与存储开销较大。

---

## 十一、解法六：宏动作与抽象（Macro Moves / Abstraction）

### 11.1 宏推（Macro Push）

一次「宏动作」= 玩家先走到某推箱位，再推箱子连续滑动直到停下（撞墙或另一箱）。

```
普通搜索：推 1 格 = 1 步
宏搜索：  一次滑动到底 = 1 步
```

减少搜索深度，适合「长通道推箱」关卡。

### 11.2 隧道 / 走廊识别

预处理地图，识别**走廊**（两侧为墙、宽度 1 的通道），在走廊内箱子只能单向滑动，将整段抽象为一个宏动作。

### 11.3 优缺点

| 优点 | 缺点 |
|------|------|
| 显著降低有效深度 | 预处理逻辑复杂 |
| 更符合人类解题思维 | 宏动作生成需额外计算 |

---

## 十二、其他思路（了解）

| 方法 | 说明 |
|------|------|
| **SAT / ASP 编码** | 将推箱编码为布尔可满足性，用 SAT 求解器求步数最少的解 |
| **Planning（PDDL）** | 作为经典 AI Planning 基准问题 |
| **强化学习** | 端到端学策略，泛化性有限，工业求解仍以搜索为主 |
| **人类启发式规则** | 「先推离角落的箱」「避免把箱推上非目标墙边」——用于手动解题，难以形式化 |

---

## 十三、算法选型建议

| 场景 | 推荐方法 |
|------|----------|
| 学习/小关卡（≤3 箱） | BFS + 角落死锁剪枝 |
| 一般关卡（5～10 箱） | A* + 曼哈顿匹配 + 死锁检测 |
| 大关卡 / 内存受限 | IDA* + 强启发式 |
| 高性能求解器 | IDA* + Pattern Database + 宏动作 + 全套死锁检测 |
| 特定关卡优化 | 尝试逆向 Pull 或双向搜索 |

---

## 十四、与本项目 visualized 模块的关系

当前 `visualized` 目录已有：

- **MazeGeneralization**：迷宫生成与 DFS 路径可视化 — 对应推箱子中的「玩家纯行走可达区域」子问题；
- **A*（graph 模块）**：最短路径启发式搜索 — 可迁移到推箱子 A* 框架；
- **MineSweeper / ConwayGameOfLife**：网格状态演化 — 与推箱子「离散状态 + 规则转移」类似。

## 可运行程序

运行 **`SokobanGame`** 即可游玩：方向键/WASD 操作，支持回退、解答、关卡选择、重置、设置。

```bash
java -cp out algorithm.visualized.Sokoban.SokobanGame
```

## 代码结构

```
Sokoban/
├── README.md
├── SokobanGame.java              ← 游戏主入口（UI + 交互）
├── SokobanGameEngine.java        ← 移动、回退、计数、解答回放
├── MicrobanLevelRepository.java  ← Microban 155 关加载
├── MicrobanEmbeddedData.java     ← 内嵌关卡与预存最优解答（运行时数据源）
├── SokobanSolutionBank.java      ← 解答查询
├── SokobanData.java / SokobanState.java / Direction.java / Position.java
└── resources/                    ← 仅用于 scripts/ 重新生成内嵌数据
    ├── microban.xsb
    └── microbanSolutions.json
```

---

## 十五、参考资源

- [Sokoban Wiki](https://sokoban.info/) — 关卡、术语、求解器
- Culberson, J. C. *Sokoban is PSPACE-complete* (1997)
- Junghanns, D. & Schaeffer, J. *Domain-Specific Single-Agent Search Architectures for Sokoban* (2001) — Pattern Database 经典论文
- [Microban](http://www.sokobano.de/wiki/index.php?title=Microban) — 轻量 Sokoban 求解器
- [BoxWorld / XSokoban](https://github.com/nosubstance/xsokoban) — 开源求解器实现参考

---

## 十六、小结

推箱子表面规则简单，本质是**高分支、强约束、易死锁**的组合搜索问题。经典解法脉络清晰：

1. **BFS** — 保证最优，适合入门与小图；
2. **A\*** — 启发式加速，工程首选；
3. **IDA\*** — 省内存，配合强启发式应对大图；
4. **逆向 Pull** — 缩小状态空间，特定关卡有效；
5. **Pattern Database** — 预计算子问题，竞赛级性能；
6. **宏动作** — 抽象连续推箱，降低搜索深度。

无论哪种方法，**死锁检测**都是必不可少的剪枝手段。三者结合（搜索框架 + 启发式 + 剪枝）构成了现代 Sokoban 求解器的标准架构。
