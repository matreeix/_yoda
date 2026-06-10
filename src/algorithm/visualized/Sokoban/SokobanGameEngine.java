package algorithm.visualized.Sokoban;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 推箱子游戏引擎：负责手动游玩、回退、计数与胜负判定。
 * <p>
 * 计数规则（与经典手游一致）：
 * <ul>
 *   <li>{@link #moveCount}「移动」— 玩家每移动一格 +1（含纯行走与推箱时的玩家位移）；</li>
 *   <li>{@link #pushCount}「步数」— 仅推动箱子时 +1。</li>
 * </ul>
 */
public class SokobanGameEngine {

    /**
     * 单次移动结果
     */
    public static final class MoveResult {
        public final boolean success;
        public final boolean pushedBox;
        public final SokobanState newState;

        MoveResult(boolean success, boolean pushedBox, SokobanState newState) {
            this.success = success;
            this.pushedBox = pushedBox;
            this.newState = newState;
        }

        static MoveResult fail() {
            return new MoveResult(false, false, null);
        }

        static MoveResult ok(SokobanState state, boolean pushed) {
            return new MoveResult(true, pushed, state);
        }
    }

    /**
     * 用于回退的历史快照
     */
    private static final class Snapshot {
        final SokobanState state;
        final int moveCount;
        final int pushCount;

        Snapshot(SokobanState state, int moveCount, int pushCount) {
            this.state = state.copy();
            this.moveCount = moveCount;
            this.pushCount = pushCount;
        }
    }

    private SokobanData level;
    private SokobanState state;

    /**
     * 玩家总移动次数
     */
    private int moveCount;

    /**
     * 推箱次数
     */
    private int pushCount;

    /**
     * 回退栈：每次成功移动前压入上一局面
     */
    private final Deque<Snapshot> undoStack = new ArrayDeque<>();

    public SokobanGameEngine(SokobanData level) {
        loadLevel(level);
    }

    /**
     * 加载关卡并重置计数与回退栈。
     */
    public void loadLevel(SokobanData level) {
        this.level = level;
        this.state = level.initialState();
        this.moveCount = 0;
        this.pushCount = 0;
        undoStack.clear();
    }

    /**
     * 重置当前关卡到初始状态。
     */
    public void resetLevel() {
        loadLevel(level);
    }

    public SokobanData level() {
        return level;
    }

    public SokobanState state() {
        return state;
    }

    public int moveCount() {
        return moveCount;
    }

    public int pushCount() {
        return pushCount;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean isWon() {
        return state.isGoal(level);
    }

    /**
     * 尝试向指定方向移动玩家。
     *
     * @return 移动结果；失败时不修改局面
     */
    public MoveResult tryMove(Direction dir) {
        if (isWon()) {
            return MoveResult.fail();
        }

        int pr = state.playerRow;
        int pc = state.playerCol;
        int nr = pr + dir.dr;
        int nc = pc + dir.dc;

        if (!level.isPassable(nr, nc)) {
            return MoveResult.fail();
        }

        // 前方无箱：纯行走
        if (!state.hasBoxAt(nr, nc)) {
            undoStack.push(new Snapshot(state, moveCount, pushCount));
            state = new SokobanState(nr, nc, state.boxRows, state.boxCols);
            moveCount++;
            return MoveResult.ok(state, false);
        }

        // 前方有箱：尝试推动
        int nnr = nr + dir.dr;
        int nnc = nc + dir.dc;
        if (!level.isPassable(nnr, nnc) || state.hasBoxAt(nnr, nnc)) {
            return MoveResult.fail();
        }

        int boxIndex = findBoxIndex(nr, nc);
        if (boxIndex < 0) {
            return MoveResult.fail();
        }

        int n = state.boxCount();
        int[] newRows = new int[n];
        int[] newCols = new int[n];
        System.arraycopy(state.boxRows, 0, newRows, 0, n);
        System.arraycopy(state.boxCols, 0, newCols, 0, n);
        newRows[boxIndex] = nnr;
        newCols[boxIndex] = nnc;

        undoStack.push(new Snapshot(state, moveCount, pushCount));
        state = new SokobanState(nr, nc, newRows, newCols);
        moveCount++;
        pushCount++;
        return MoveResult.ok(state, true);
    }

    /**
     * 回退一步：恢复上一个局面与计数。
     *
     * @return 是否成功回退
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }
        Snapshot snap = undoStack.pop();
        state = snap.state.copy();
        moveCount = snap.moveCount;
        pushCount = snap.pushCount;
        return true;
    }

    /**
     * 直接设置局面（用于解答回放），并清空回退栈。
     */
    public void setStateForPlayback(SokobanState newState, int moves, int pushes) {
        this.state = newState.copy();
        this.moveCount = moves;
        this.pushCount = pushes;
        undoStack.clear();
    }

    /**
     * 解答回放结束后，将计数同步为回放终态（不保留中间回退）。
     */
    public void syncCountersAfterPlayback(int moves, int pushes) {
        this.moveCount = moves;
        this.pushCount = pushes;
        undoStack.clear();
    }

    private int findBoxIndex(int row, int col) {
        for (int i = 0; i < state.boxCount(); i++) {
            if (state.boxRows[i] == row && state.boxCols[i] == col) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将 LURD 完整移动串（小写 u/d/l/r）展开为逐格回放帧。
     * <p>
     * 预计算解答来自成熟求解器，已包含最优行走与推箱的每一步。
     */
    public static List<PlaybackFrame> buildPlaybackFromLurd(SokobanData map, String lurd) {
        List<PlaybackFrame> frames = new ArrayList<>();
        SokobanGameEngine engine = new SokobanGameEngine(map);
        frames.add(new PlaybackFrame(engine.state().copy(), 0, 0));

        if (lurd == null || lurd.isEmpty()) {
            return frames;
        }

        for (int i = 0; i < lurd.length(); i++) {
            Direction dir = directionFromLurd(lurd.charAt(i));
            if (dir == null) {
                break;
            }
            if (!engine.tryMove(dir).success) {
                break;
            }
            frames.add(new PlaybackFrame(
                    engine.state().copy(), engine.moveCount(), engine.pushCount()));
        }
        return frames;
    }

    private static Direction directionFromLurd(char ch) {
        switch (ch) {
            case 'u':
            case 'U':
                return Direction.UP;
            case 'd':
            case 'D':
                return Direction.DOWN;
            case 'l':
            case 'L':
                return Direction.LEFT;
            case 'r':
            case 'R':
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    /**
     * 回放帧：局面 + 当时的移动/推箱计数
     */
    public static final class PlaybackFrame {
        public final SokobanState state;
        public final int moveCount;
        public final int pushCount;

        PlaybackFrame(SokobanState state, int moveCount, int pushCount) {
            this.state = state;
            this.moveCount = moveCount;
            this.pushCount = pushCount;
        }
    }
}
