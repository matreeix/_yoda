package algorithm.visualized.Sudoku;

import algorithm.visualized.Sudoku.SudokuData;
import data_structure.linear.linkedlist.DLX.Sudoku.Sudoku1;
import data_structure.linear.linkedlist.DLX.Sudoku.Sudoku2;
import data_structure.linear.linkedlist.DLX.Sudoku.Sudoku3;

import java.util.function.Consumer;

/**
 * 数独游戏逻辑：填数校验、完成判定、三种求解器调度与逐步可视化回溯。
 */
public class SudokuEngine {

    public enum SolverMethod {
        BACKTRACK("回溯法"),
        BITMAP_MRV("位图 + MRV"),
        DLX("舞蹈链 DLX");

        public final String label;

        SolverMethod(String label) {
            this.label = label;
        }
    }

    public enum StepAction {
        TRY, BACKTRACK, DONE
    }

    public static final class SolveStep {
        public final int row;
        public final int col;
        public final char digit;
        public final StepAction action;

        public SolveStep(int row, int col, char digit, StepAction action) {
            this.row = row;
            this.col = col;
            this.digit = digit;
            this.action = action;
        }
    }

    private final SudokuData data;

    public SudokuEngine(SudokuData data) {
        this.data = data;
    }

    public SudokuData getData() {
        return data;
    }

    public boolean placeDigit(int row, int col, char digit) {
        if (!inBounds(row, col) || data.isFixed(row, col)) {
            return false;
        }
        if (digit != SudokuData.EMPTY && (digit < '1' || digit > '9')) {
            return false;
        }
        data.set(row, col, digit);
        return true;
    }

    public boolean isValidPlacement(char[][] board, int row, int col, char num) {
        int blkRow = (row / 3) * 3;
        int blkCol = (col / 3) * 3;
        for (int i = 0; i < SudokuData.SIZE; i++) {
            if (board[i][col] == num || board[row][i] == num
                    || board[blkRow + i / 3][blkCol + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    public boolean hasConflict(int row, int col) {
        char[][] board = data.getBoard();
        char value = board[row][col];
        if (value == SudokuData.EMPTY) {
            return false;
        }
        int blkRow = (row / 3) * 3;
        int blkCol = (col / 3) * 3;
        for (int i = 0; i < SudokuData.SIZE; i++) {
            if (i != col && board[row][i] == value) {
                return true;
            }
            if (i != row && board[i][col] == value) {
                return true;
            }
            int br = blkRow + i / 3;
            int bc = blkCol + i % 3;
            if ((br != row || bc != col) && board[br][bc] == value) {
                return true;
            }
        }
        return false;
    }

    public boolean isComplete() {
        char[][] board = data.getBoard();
        for (int r = 0; r < SudokuData.SIZE; r++) {
            for (int c = 0; c < SudokuData.SIZE; c++) {
                if (board[r][c] == SudokuData.EMPTY || hasConflict(r, c)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int countEmptyCells() {
        int count = 0;
        char[][] board = data.getBoard();
        for (int r = 0; r < SudokuData.SIZE; r++) {
            for (int c = 0; c < SudokuData.SIZE; c++) {
                if (board[r][c] == SudokuData.EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }

    public long solveInstant(SolverMethod method) {
        char[][] board = data.getBoard();
        char[][] work = SudokuData.copyBoard(board);
        long start = System.nanoTime();
        switch (method) {
            case BACKTRACK:
                new Sudoku1().solveSudoku(work);
                break;
            case BITMAP_MRV:
                new Sudoku2().solveSudoku(work);
                break;
            case DLX:
                new Sudoku3().solveSudoku(work);
                break;
            default:
                throw new IllegalArgumentException("Unknown solver: " + method);
        }
        applySolution(work);
        return System.nanoTime() - start;
    }

    private void applySolution(char[][] solved) {
        char[][] board = data.getBoard();
        for (int r = 0; r < SudokuData.SIZE; r++) {
            for (int c = 0; c < SudokuData.SIZE; c++) {
                if (!data.isFixed(r, c)) {
                    board[r][c] = solved[r][c];
                }
            }
        }
    }

    /**
     * 在副本上回溯，仅通过回调输出步骤，不修改当前盘面。
     */
    public boolean solveAnimated(Consumer<SolveStep> onStep) {
        char[][] work = SudokuData.copyBoard(data.getBoard());
        boolean ok = backtrackVisual(work, 0, 0, onStep);
        if (ok) {
            onStep.accept(new SolveStep(-1, -1, SudokuData.EMPTY, StepAction.DONE));
        }
        return ok;
    }

    private boolean backtrackVisual(char[][] board, int row, int col, Consumer<SolveStep> onStep) {
        for (int r = row; r < SudokuData.SIZE; r++, col = 0) {
            for (int c = col; c < SudokuData.SIZE; c++) {
                if (board[r][c] != SudokuData.EMPTY) {
                    continue;
                }
                for (char num = '1'; num <= '9'; num++) {
                    if (!isValidPlacement(board, r, c, num)) {
                        continue;
                    }
                    board[r][c] = num;
                    onStep.accept(new SolveStep(r, c, num, StepAction.TRY));
                    if (backtrackVisual(board, r, c + 1, onStep)) {
                        return true;
                    }
                    board[r][c] = SudokuData.EMPTY;
                    onStep.accept(new SolveStep(r, c, SudokuData.EMPTY, StepAction.BACKTRACK));
                }
                return false;
            }
        }
        return true;
    }

    public boolean hintOne() {
        char[][] board = data.getBoard();
        char[][] work = SudokuData.copyBoard(board);
        new Sudoku1().solveSudoku(work);
        for (int r = 0; r < SudokuData.SIZE; r++) {
            for (int c = 0; c < SudokuData.SIZE; c++) {
                if (!data.isFixed(r, c) && board[r][c] == SudokuData.EMPTY) {
                    data.set(r, c, work[r][c]);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean inBounds(int row, int col) {
        return row >= 0 && row < SudokuData.SIZE && col >= 0 && col < SudokuData.SIZE;
    }
}
