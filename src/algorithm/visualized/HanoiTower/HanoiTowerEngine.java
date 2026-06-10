package algorithm.visualized.HanoiTower;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 汉诺塔游戏逻辑与递归求解。
 */
public class HanoiTowerEngine {

    public static final int PEG_COUNT = 3;
    public static final int SOURCE = 0;
    public static final int AUX = 1;
    public static final int TARGET = 2;

    public static final class Move {
        public final int from;
        public final int to;
        public final int disk;

        public Move(int from, int to, int disk) {
            this.from = from;
            this.to = to;
            this.disk = disk;
        }
    }

    private final Deque<Integer>[] pegs;
    private int diskCount;
    private int moveCount;

    @SuppressWarnings("unchecked")
    public HanoiTowerEngine(int diskCount) {
        pegs = new ArrayDeque[PEG_COUNT];
        for (int i = 0; i < PEG_COUNT; i++) {
            pegs[i] = new ArrayDeque<>();
        }
        reset(diskCount);
    }

    public void reset(int diskCount) {
        if (diskCount < 1 || diskCount > 12) {
            throw new IllegalArgumentException("disk count must be 1..12");
        }
        this.diskCount = diskCount;
        moveCount = 0;
        for (Deque<Integer> peg : pegs) {
            peg.clear();
        }
        for (int d = diskCount; d >= 1; d--) {
            pegs[SOURCE].addLast(d);
        }
    }

    public int getDiskCount() {
        return diskCount;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int minMoves() {
        return (1 << diskCount) - 1;
    }

    public int topDisk(int peg) {
        Integer top = pegs[peg].peekLast();
        return top == null ? 0 : top;
    }

    public int stackHeight(int peg) {
        return pegs[peg].size();
    }

    /**
     * 从下往上第 index 个圆盘，0 为最底层。
     */
    public int diskAt(int peg, int index) {
        if (index < 0 || index >= pegs[peg].size()) {
            return 0;
        }
        Integer[] arr = pegs[peg].toArray(new Integer[0]);
        return arr[index];
    }

    public boolean canMove(int from, int to) {
        if (from == to || from < 0 || from >= PEG_COUNT || to < 0 || to >= PEG_COUNT) {
            return false;
        }
        if (pegs[from].isEmpty()) {
            return false;
        }
        if (pegs[to].isEmpty()) {
            return true;
        }
        return pegs[from].peekLast() < pegs[to].peekLast();
    }

    public boolean move(int from, int to) {
        if (!canMove(from, to)) {
            return false;
        }
        pegs[to].addLast(pegs[from].removeLast());
        moveCount++;
        return true;
    }

    public boolean isSolved() {
        return pegs[TARGET].size() == diskCount;
    }

    /**
     * 经典递归：将 n 个盘从 from 移到 to，借助 aux。
     */
    public List<Move> solve() {
        List<Move> moves = new ArrayList<>();
        collectMoves(diskCount, SOURCE, TARGET, AUX, moves);
        return moves;
    }

    private void collectMoves(int n, int from, int to, int aux, List<Move> moves) {
        if (n == 0) {
            return;
        }
        collectMoves(n - 1, from, aux, to, moves);
        moves.add(new Move(from, to, n));
        collectMoves(n - 1, aux, to, from, moves);
    }

    public void applyMove(Move move) {
        if (!canMove(move.from, move.to)) {
            throw new IllegalStateException("invalid move: " + move.from + " -> " + move.to);
        }
        move(move.from, move.to);
    }

    /**
     * 撤销一步：将盘从 to 移回 from。
     */
    public boolean undo(Move move) {
        if (!canMove(move.to, move.from)) {
            return false;
        }
        pegs[move.from].addLast(pegs[move.to].removeLast());
        moveCount--;
        return true;
    }
}
