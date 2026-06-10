package algorithm.visualized.Sokoban;

import java.util.Arrays;

/**
 * 推箱子搜索状态：玩家位置 + 所有箱子位置。
 * <p>
 * 设计要点：
 * <ul>
 *   <li>箱子坐标按 (row, col) 字典序排序存储，消除同一布局的不同排列顺序；</li>
 *   <li>玩家从 A 走到 B 且不推箱的中间路径不计入搜索层（路径压缩）；</li>
 *   <li>每次扩展只产生「推箱一步」后的新状态。</li>
 * </ul>
 * 因此 {@link #equals} / {@link #hashCode} 仅依赖玩家坐标与排序后的箱子坐标。
 */
public final class SokobanState {

    /**
     * 玩家所在行
     */
    public final int playerRow;

    /**
     * 玩家所在列
     */
    public final int playerCol;

    /**
     * 各箱子行坐标（已排序）
     */
    public final int[] boxRows;

    /**
     * 各箱子列坐标（与 boxRows 一一对应，已排序）
     */
    public final int[] boxCols;

    public SokobanState(int playerRow, int playerCol, int[] boxRows, int[] boxCols) {
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.boxRows = Arrays.copyOf(boxRows, boxRows.length);
        this.boxCols = Arrays.copyOf(boxCols, boxCols.length);
        sortBoxes();
    }

    /**
     * 按 (row, col) 字典序对箱子坐标排序，保证状态表示唯一。
     */
    private void sortBoxes() {
        int n = boxRows.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (boxRows[i] > boxRows[j]
                        || (boxRows[i] == boxRows[j] && boxCols[i] > boxCols[j])) {
                    swap(i, j);
                }
            }
        }
    }

    private void swap(int i, int j) {
        int tr = boxRows[i];
        boxRows[i] = boxRows[j];
        boxRows[j] = tr;
        int tc = boxCols[i];
        boxCols[i] = boxCols[j];
        boxCols[j] = tc;
    }

    public int boxCount() {
        return boxRows.length;
    }

    /**
     * 判断第 i 个箱子是否已在目标点上。
     */
    public boolean isBoxOnTarget(SokobanData map, int index) {
        return map.isTarget(boxRows[index], boxCols[index]);
    }

    /**
     * 胜利条件：每个箱子都位于某个目标格上。
     */
    public boolean isGoal(SokobanData map) {
        for (int i = 0; i < boxRows.length; i++) {
            if (!map.isTarget(boxRows[i], boxCols[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断 (row, col) 是否被任意箱子占据。
     */
    public boolean hasBoxAt(int row, int col) {
        for (int i = 0; i < boxRows.length; i++) {
            if (boxRows[i] == row && boxCols[i] == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * 深拷贝当前状态（用于可视化回放或分支搜索）。
     */
    public SokobanState copy() {
        return new SokobanState(playerRow, playerCol, boxRows, boxCols);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SokobanState)) {
            return false;
        }
        SokobanState that = (SokobanState) o;
        return playerRow == that.playerRow
                && playerCol == that.playerCol
                && Arrays.equals(boxRows, that.boxRows)
                && Arrays.equals(boxCols, that.boxCols);
    }

    @Override
    public int hashCode() {
        int h = 31 * (31 + playerRow) + playerCol;
        h = 31 * h + Arrays.hashCode(boxRows);
        h = 31 * h + Arrays.hashCode(boxCols);
        return h;
    }

    @Override
    public String toString() {
        return "SokobanState{player=(" + playerRow + "," + playerCol + "), boxes=" + boxRows.length + "}";
    }
}
