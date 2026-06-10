package algorithm.visualized.Sokoban;

/**
 * 推箱子中的四个移动方向。
 * <p>
 * 坐标约定：row 向下递增，col 向右递增（与二维数组下标一致）。
 * {@code dr}/{@code dc} 表示在该方向上移动一格时 row/col 的变化量。
 */
public enum Direction {

    /**
     * 向上：row 减 1
     */
    UP(-1, 0, 'U'),

    /**
     * 向右：col 加 1
     */
    RIGHT(0, 1, 'R'),

    /**
     * 向下：row 加 1
     */
    DOWN(1, 0, 'D'),

    /**
     * 向左：col 减 1
     */
    LEFT(0, -1, 'L');

    /**
     * row 方向偏移
     */
    public final int dr;

    /**
     * col 方向偏移
     */
    public final int dc;

    /**
     * 解法路径中使用的方向字符
     */
    public final char symbol;

    Direction(int dr, int dc, char symbol) {
        this.dr = dr;
        this.dc = dc;
        this.symbol = symbol;
    }

    /**
     * @return 相反方向（用于判断「玩家在箱子哪一侧才能推」）
     */
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new IllegalStateException("未知方向: " + this);
        }
    }
}
