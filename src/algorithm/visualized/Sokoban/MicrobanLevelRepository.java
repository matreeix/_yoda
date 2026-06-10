package algorithm.visualized.Sokoban;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 经典 Microban 关卡集（David W. Skinner，155 关）。
 * <p>
 * 关卡数据内嵌于 {@link MicrobanEmbeddedData}，运行时无需 classpath 资源文件。
 */
public final class MicrobanLevelRepository {

    private static final Pattern SEPARATOR = Pattern.compile("^;\\s*\\d+\\s*$", Pattern.MULTILINE);
    private static final List<SokobanData> LEVELS = parseLevels(MicrobanEmbeddedData.XSB);

    private MicrobanLevelRepository() {
    }

    public static int count() {
        return LEVELS.size();
    }

    public static SokobanData get(int index) {
        if (index < 0 || index >= LEVELS.size()) {
            return LEVELS.get(0);
        }
        return LEVELS.get(index);
    }

    public static boolean hasSolution(int index) {
        return SokobanSolutionBank.hasSolution(index);
    }

    private static List<SokobanData> parseLevels(String xsb) {
        String[] blocks = SEPARATOR.split(xsb);
        List<SokobanData> levels = new ArrayList<>();
        int index = 1;
        for (String block : blocks) {
            String trimmed = block.trim();
            if (trimmed.isEmpty() || !trimmed.contains("#")) {
                continue;
            }
            String name = "Microban " + index;
            try {
                levels.add(new SokobanData(name, trimmed, false));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("解析 " + name + " 失败: " + ex.getMessage(), ex);
            }
            index++;
        }
        if (levels.isEmpty()) {
            throw new IllegalStateException("Microban 关卡解析结果为空");
        }
        if (levels.size() != MicrobanEmbeddedData.LEVEL_COUNT) {
            throw new IllegalStateException(
                    "内嵌关卡数 " + MicrobanEmbeddedData.LEVEL_COUNT + " 与解析结果 " + levels.size() + " 不一致");
        }
        return Collections.unmodifiableList(levels);
    }
}
