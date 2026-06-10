package algorithm.visualized.Sokoban;

import java.util.Objects;

/**
 * 地图上的整数格点坐标。
 * <p>
 * 使用 row/col 而非 x/y，与 Java 二维数组 {@code map[row][col]} 保持一致。
 */
public final class Position {

    public final int row;
    public final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * 沿指定方向移动一格后的新坐标（不检查边界与障碍）。
     */
    public Position move(Direction dir) {
        return new Position(row + dir.dr, col + dir.dc);
    }

    /**
     * 将坐标压缩为一个整数，便于 BFS 可达性集合存取。
     */
    public int encode(int cols) {
        return row * cols + col;
    }

    public static Position decode(int code, int cols) {
        return new Position(code / cols, code % cols);
    }

    /**
     * 两格之间的曼哈顿距离（启发式函数的基础）。
     */
    public int manhattan(Position other) {
        return Math.abs(row - other.row) + Math.abs(col - other.col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position that = (Position) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
