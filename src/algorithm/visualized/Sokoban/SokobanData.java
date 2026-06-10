package algorithm.visualized.Sokoban;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 推箱子关卡数据：静态地图（墙、目标点）与初始动态元素（玩家、箱子）。
 * <p>
 * 支持标准 Sokoban 文本格式解析，符号含义：
 * <pre>
 *   # 墙    @ 玩家    $ 箱子    . 目标
 *   * 玩家在目标上    + 箱子在目标上    空格 可行走地面
 * </pre>
 * <p>
 * 经典 XSB 关卡（如 Microban）各行宽度不一，短行右侧补空格；部分 ASCII 排版还会在墙内
 * 留下与主战场不连通的「装饰空地」。解析时会标记这些格子不在 UI 中绘制，但不改碰撞与坐标，
 * 预存解答不受影响。
 */
public class SokobanData {

    /**
     * 单元格：不可通过的墙
     */
    public static final char WALL = '#';

    /**
     * 单元格：普通地面（可行走）
     */
    public static final char FLOOR = ' ';

    /**
     * 单元格：目标点（可行走，需放置箱子）
     */
    public static final char TARGET = '.';

    /**
     * 行数
     */
    private final int rows;

    /**
     * 列数（取各行最大宽度，短行右侧视为墙外不可达）
     */
    private final int cols;

    /**
     * 静态地形：{@link #WALL} / {@link #FLOOR} / {@link #TARGET}。
     * 不包含玩家与箱子的动态信息。
     */
    private final char[][] terrain;

    /**
     * 所有目标点坐标（用于胜利判定与启发式）
     */
    private final List<Position> targets;

    /**
     * 关卡初始状态
     */
    private final SokobanState initialState;

    /**
     * 关卡显示名称
     */
    private final String name;

    /**
     * 是否绘制该格（墙内纯装饰空地不绘制，预存解答坐标不变）。
     */
    private final boolean[][] visible;

    /**
     * 从多行文本解析关卡。
     *
     * @param name      关卡名称（可为 null）
     * @param levelText 标准 Sokoban 地图文本，行之间用 \n 分隔
     */
    public SokobanData(String name, String levelText) {
        this(name, levelText, true);
    }

    /**
     * @param requireBalancedBoxes 为 false 时允许箱子数与目标数不一致（少数经典关卡原文如此）
     */
    public SokobanData(String name, String levelText, boolean requireBalancedBoxes) {
        this.name = name != null ? name : "未命名关卡";
        String[] lines = levelText.trim().split("\n");
        MapDraft draft = parseLines(lines, requireBalancedBoxes);
        this.rows = draft.rows;
        this.cols = draft.cols;
        this.terrain = draft.terrain;
        this.targets = draft.targets;
        this.visible = computeVisibleCells(draft);
        this.initialState = draft.toInitialState();
    }

    private static MapDraft parseLines(String[] lines, boolean requireBalancedBoxes) {
        int rows = lines.length;
        int maxCols = 0;
        for (String line : lines) {
            maxCols = Math.max(maxCols, line.length());
        }
        int cols = maxCols;

        char[][] terrain = new char[rows][cols];
        List<Position> targets = new ArrayList<>();

        int playerRow = -1;
        int playerCol = -1;
        List<Integer> boxRowList = new ArrayList<>();
        List<Integer> boxColList = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            String line = lines[r];
            for (int c = 0; c < cols; c++) {
                char ch = c < line.length() ? line.charAt(c) : ' ';
                switch (ch) {
                    case '#':
                    case 'X':
                        terrain[r][c] = WALL;
                        break;
                    case '.':
                        terrain[r][c] = TARGET;
                        targets.add(new Position(r, c));
                        break;
                    case '@':
                        terrain[r][c] = FLOOR;
                        playerRow = r;
                        playerCol = c;
                        break;
                    case '*':
                        terrain[r][c] = TARGET;
                        targets.add(new Position(r, c));
                        boxRowList.add(r);
                        boxColList.add(c);
                        break;
                    case '$':
                        terrain[r][c] = FLOOR;
                        boxRowList.add(r);
                        boxColList.add(c);
                        break;
                    case '+':
                        terrain[r][c] = TARGET;
                        targets.add(new Position(r, c));
                        playerRow = r;
                        playerCol = c;
                        break;
                    case ' ':
                        terrain[r][c] = FLOOR;
                        break;
                    default:
                        terrain[r][c] = WALL;
                        break;
                }
            }
        }

        if (playerRow < 0) {
            throw new IllegalArgumentException("地图中缺少玩家 '@' 或 '+'");
        }
        if (boxRowList.isEmpty()) {
            throw new IllegalArgumentException("地图中缺少箱子 '$' 或 '+'");
        }
        if (targets.isEmpty()) {
            throw new IllegalArgumentException("地图中缺少目标点 '.' 或 '+' / '*'");
        }
        if (requireBalancedBoxes && boxRowList.size() != targets.size()) {
            throw new IllegalArgumentException(
                    "箱子数(" + boxRowList.size() + ")与目标点数(" + targets.size() + ")不一致");
        }

        MapDraft draft = new MapDraft();
        draft.rows = rows;
        draft.cols = cols;
        draft.terrain = terrain;
        draft.targets = targets;
        draft.playerRow = playerRow;
        draft.playerCol = playerCol;
        draft.boxRows = boxRowList.stream().mapToInt(Integer::intValue).toArray();
        draft.boxCols = boxColList.stream().mapToInt(Integer::intValue).toArray();
        return draft;
    }

    /**
     * 标记需要在 UI 中绘制的格子：墙、玩家/箱子/目标所在连通区域；墙内纯装饰空地不绘制。
     */
    private static boolean[][] computeVisibleCells(MapDraft draft) {
        int[][] component = labelPassableComponents(draft);
        boolean[] componentUsed = markUsedComponents(draft, component);
        boolean[][] visible = new boolean[draft.rows][draft.cols];
        for (int r = 0; r < draft.rows; r++) {
            for (int c = 0; c < draft.cols; c++) {
                if (draft.terrain[r][c] == WALL) {
                    visible[r][c] = true;
                } else {
                    int id = component[r][c];
                    visible[r][c] = id >= 0 && componentUsed[id];
                }
            }
        }
        return visible;
    }

    /** 对可行走格（含目标）做四连通分量，箱子不占格（仅用于识别装饰性空腔）。 */
    private static int[][] labelPassableComponents(MapDraft draft) {
        int[][] component = new int[draft.rows][draft.cols];
        for (int r = 0; r < draft.rows; r++) {
            for (int c = 0; c < draft.cols; c++) {
                component[r][c] = -1;
            }
        }
        int nextId = 0;
        Deque<Position> queue = new ArrayDeque<>();
        for (int r = 0; r < draft.rows; r++) {
            for (int c = 0; c < draft.cols; c++) {
                if (draft.terrain[r][c] == WALL || component[r][c] >= 0) {
                    continue;
                }
                queue.offer(new Position(r, c));
                component[r][c] = nextId;
                while (!queue.isEmpty()) {
                    Position p = queue.poll();
                    for (Direction dir : Direction.values()) {
                        int nr = p.row + dir.dr;
                        int nc = p.col + dir.dc;
                        if (!inDraftBounds(draft, nr, nc) || draft.terrain[nr][nc] == WALL
                                || component[nr][nc] >= 0) {
                            continue;
                        }
                        component[nr][nc] = nextId;
                        queue.offer(new Position(nr, nc));
                    }
                }
                nextId++;
            }
        }
        return component;
    }

    private static boolean[] markUsedComponents(MapDraft draft, int[][] component) {
        int count = 0;
        for (int r = 0; r < draft.rows; r++) {
            for (int c = 0; c < draft.cols; c++) {
                count = Math.max(count, component[r][c] + 1);
            }
        }
        boolean[] used = new boolean[count];
        markComponent(used, component, draft.playerRow, draft.playerCol);
        for (Position t : draft.targets) {
            markComponent(used, component, t.row, t.col);
        }
        for (int i = 0; i < draft.boxRows.length; i++) {
            markComponent(used, component, draft.boxRows[i], draft.boxCols[i]);
        }
        return used;
    }

    private static void markComponent(boolean[] used, int[][] component, int row, int col) {
        if (row < 0 || col < 0 || row >= component.length || col >= component[0].length) {
            return;
        }
        int id = component[row][col];
        if (id >= 0) {
            used[id] = true;
        }
    }

    private static boolean inDraftBounds(MapDraft draft, int row, int col) {
        return row >= 0 && row < draft.rows && col >= 0 && col < draft.cols;
    }

    /**
     * 该格是否参与绘制（装饰性空腔内的地面返回 false，碰撞逻辑不变）。
     */
    public boolean isVisible(int row, int col) {
        if (!inBounds(row, col)) {
            return false;
        }
        return visible[row][col];
    }

    private static final class MapDraft {
        int rows;
        int cols;
        char[][] terrain;
        List<Position> targets;
        int playerRow;
        int playerCol;
        int[] boxRows;
        int[] boxCols;

        SokobanState toInitialState() {
            return new SokobanState(playerRow, playerCol, boxRows, boxCols);
        }
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    public String name() {
        return name;
    }

    public SokobanState initialState() {
        return initialState.copy();
    }

    public List<Position> targets() {
        return targets;
    }

    public char terrainAt(int row, int col) {
        if (!inBounds(row, col)) {
            return WALL;
        }
        return terrain[row][col];
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isWall(int row, int col) {
        return !inBounds(row, col) || terrain[row][col] == WALL;
    }

    public boolean isTarget(int row, int col) {
        return inBounds(row, col) && terrain[row][col] == TARGET;
    }

    /**
     * 玩家或箱子能否进入该格（非墙）。
     */
    public boolean isPassable(int row, int col) {
        return inBounds(row, col) && terrain[row][col] != WALL;
    }

    /**
     * 根据当前状态渲染完整地图字符（用于调试或文本输出）。
     */
    public char[][] render(SokobanState state) {
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (terrain[r][c] == WALL) {
                    grid[r][c] = WALL;
                } else if (terrain[r][c] == TARGET) {
                    grid[r][c] = TARGET;
                } else {
                    grid[r][c] = FLOOR;
                }
            }
        }

        for (Position t : targets) {
            grid[t.row][t.col] = TARGET;
        }

        for (int i = 0; i < state.boxCount(); i++) {
            int br = state.boxRows[i];
            int bc = state.boxCols[i];
            grid[br][bc] = isTarget(br, bc) ? '+' : '$';
        }

        int pr = state.playerRow;
        int pc = state.playerCol;
        if (!(state.hasBoxAt(pr, pc))) {
            grid[pr][pc] = isTarget(pr, pc) ? '*' : '@';
        }

        return grid;
    }

    @Override
    public String toString() {
        return name + " (" + rows + "x" + cols + ", " + initialState.boxCount() + " 箱)";
    }
}
