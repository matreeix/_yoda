package algorithm.visualized.Sudoku;

/**
 * 数独盘面状态与题库。
 */
public class SudokuData {

    public static final char EMPTY = '.';
    public static final int SIZE = 9;

    public static final class Puzzle {
        public final String name;
        public final char[][] board;

        public Puzzle(String name, char[][] board) {
            this.name = name;
            this.board = copyBoard(board);
        }
    }

    private static final Puzzle[] PUZZLES = {
            new Puzzle("入门 · 简单", new char[][]{
                    {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                    {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                    {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                    {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                    {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                    {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                    {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                    {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                    {'.', '.', '.', '.', '8', '.', '.', '7', '9'},
            }),
            new Puzzle("进阶 · 中等", new char[][]{
                    {'.', '2', '.', '6', '.', '8', '.', '.', '.'},
                    {'5', '8', '.', '.', '.', '9', '7', '.', '.'},
                    {'.', '.', '.', '.', '4', '.', '.', '.', '.'},
                    {'3', '7', '.', '.', '.', '.', '5', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '8', '.', '.', '.', '.', '1', '3'},
                    {'.', '.', '.', '.', '2', '.', '.', '.', '.'},
                    {'.', '.', '9', '8', '.', '.', '.', '3', '6'},
                    {'.', '.', '.', '3', '.', '6', '.', '9', '.'},
            }),
            new Puzzle("挑战 · 困难", new char[][]{
                    {'8', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '3', '6', '.', '.', '.', '.', '.'},
                    {'.', '7', '.', '.', '9', '.', '2', '.', '.'},
                    {'.', '5', '.', '.', '.', '7', '.', '.', '.'},
                    {'.', '.', '.', '.', '4', '5', '7', '.', '.'},
                    {'.', '.', '.', '1', '.', '.', '.', '3', '.'},
                    {'.', '.', '1', '.', '.', '.', '.', '6', '8'},
                    {'.', '.', '8', '5', '.', '.', '.', '1', '.'},
                    {'.', '9', '.', '.', '.', '.', '4', '.', '.'},
            }),
            new Puzzle("空白 · 自由填", new char[][]{
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                    {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            }),
    };

    private final char[][] initial;
    private final char[][] board;
    private final boolean[][] fixed;

    public SudokuData(int puzzleIndex) {
        Puzzle puzzle = PUZZLES[normalizeIndex(puzzleIndex)];
        initial = copyBoard(puzzle.board);
        board = copyBoard(initial);
        fixed = new boolean[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                fixed[r][c] = initial[r][c] != EMPTY;
            }
        }
    }

    public static int puzzleCount() {
        return PUZZLES.length;
    }

    public static String puzzleName(int index) {
        return PUZZLES[normalizeIndex(index)].name;
    }

    private static int normalizeIndex(int index) {
        if (PUZZLES.length == 0) {
            return 0;
        }
        int mod = index % PUZZLES.length;
        return mod < 0 ? mod + PUZZLES.length : mod;
    }

    public static char[][] copyBoard(char[][] src) {
        char[][] dst = new char[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(src[r], 0, dst[r], 0, SIZE);
        }
        return dst;
    }

    public char[][] getBoard() {
        return board;
    }

    public char[][] getInitial() {
        return initial;
    }

    public boolean isFixed(int row, int col) {
        return fixed[row][col];
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == EMPTY;
    }

    public char get(int row, int col) {
        return board[row][col];
    }

    public void set(int row, int col, char value) {
        if (!fixed[row][col]) {
            board[row][col] = value;
        }
    }

    public void reset() {
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(initial[r], 0, board[r], 0, SIZE);
        }
    }

    public void loadPuzzle(int puzzleIndex) {
        Puzzle puzzle = PUZZLES[normalizeIndex(puzzleIndex)];
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(puzzle.board[r], 0, initial[r], 0, SIZE);
            System.arraycopy(puzzle.board[r], 0, board[r], 0, SIZE);
            for (int c = 0; c < SIZE; c++) {
                fixed[r][c] = initial[r][c] != EMPTY;
            }
        }
    }
}
