package algorithm.visualized.Sokoban;

/**
 * Microban 预计算最优解答（按关卡索引 0..154）。
 * <p>
 * 数据内嵌于 {@link MicrobanEmbeddedData#SOLUTIONS}，来自 martin-t/sokoban-solver
 * 推箱最优解（完整 LURD 移动串）。重新生成：{@code python scripts/embed_microban_java.py}
 */
public final class SokobanSolutionBank {

    private static final String[] SOLUTIONS = MicrobanEmbeddedData.SOLUTIONS;

    private SokobanSolutionBank() {
    }

    public static int count() {
        int n = 0;
        for (String s : SOLUTIONS) {
            if (s != null && !s.isEmpty()) {
                n++;
            }
        }
        return n;
    }

    public static String lookup(int levelIndex) {
        if (levelIndex < 0 || levelIndex >= SOLUTIONS.length) {
            return null;
        }
        String s = SOLUTIONS[levelIndex];
        return (s == null || s.isEmpty()) ? null : s;
    }

    public static boolean hasSolution(int levelIndex) {
        return lookup(levelIndex) != null;
    }

}
