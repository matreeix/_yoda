package algorithm.graph.BFS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

/**
 * @Description: 3905. 多源图像渲染
 * 给你两个整数 n 和 m，分别表示一个网格的行数和列数。
 * 同时给你一个二维整数数组 sources，其中 sources[i] = [ri, ci, colori] 表示单元格 (ri, ci) 初始被涂上颜色 colori。所有其他单元格初始均未着色，用 0 表示。
 * 在每一单位时间中，所有当前已着色的单元格都会将其颜色向上下左右四个方向扩散到所有相邻的 未着色 单元格。所有扩散同时发生。
 * 如果 多个 颜色在同一时间步到达同一个未着色单元格，该单元格将采用具有 最大 值的颜色。
 * 这个过程持续进行，直到没有更多的单元格可以被着色。
 * 返回一个二维整数数组，表示网格的最终状态，其中每个单元格包含其最终的颜色。
 * @Date: 2026/4/20
 */

public class MultiSourceImageRenderer {
    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public int[][] colorGrid(int n, int m, int[][] sources) {
        List<int[][]> frames = simulateFrames(n, m, sources);
        return frames.get(frames.size() - 1);
    }

    /**
     * 逐时间步模拟渲染，返回每一步的网格快照（包含初始状态）。
     */
    public List<int[][]> simulateFrames(int n, int m, int[][] sources) {
        int[][] grid = new int[n][m];
        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] inQueue = new boolean[n][m];

        // 同一坐标可能出现多个 source，初始颜色取最大值。
        for (int[] source : sources) {
            int r = source[0];
            int c = source[1];
            int color = source[2];
            if (color > grid[r][c]) {
                grid[r][c] = color;
            }
        }

        // 初始已染色点全部入队，作为 t=0 的前沿层。
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (grid[r][c] != 0) {
                    queue.offer(new int[]{r, c});
                    inQueue[r][c] = true;
                }
            }
        }

        List<int[][]> frames = new ArrayList<>();
        frames.add(copyGrid(grid));

        while (!queue.isEmpty()) {
            int layerSize = queue.size();
            Map<Integer, Integer> proposals = new HashMap<>();

            for (int i = 0; i < layerSize; i++) {
                int[] cur = queue.poll();
                int r = cur[0];
                int c = cur[1];
                int color = grid[r][c];

                for (int[] dir : DIRS) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    if (nr < 0 || nr >= n || nc < 0 || nc >= m) {
                        continue;
                    }
                    if (grid[nr][nc] != 0) {
                        continue;
                    }
                    int key = nr * m + nc;
                    proposals.merge(key, color, Math::max);
                }
            }

            if (proposals.isEmpty()) {
                break;
            }

            for (Map.Entry<Integer, Integer> entry : proposals.entrySet()) {
                int key = entry.getKey();
                int nr = key / m;
                int nc = key % m;
                grid[nr][nc] = entry.getValue();
                if (!inQueue[nr][nc]) {
                    queue.offer(new int[]{nr, nc});
                    inQueue[nr][nc] = true;
                }
            }

            frames.add(copyGrid(grid));
        }

        return frames;
    }

    private int[][] copyGrid(int[][] grid) {
        int[][] snapshot = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            snapshot[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        return snapshot;
    }

    /**
     * 启动动态渲染 UI。
     */
    public void showAnimation(int n, int m, int[][] sources) {
        showAnimation(n, m, sources, 900, 700);
    }

    /**
     * 启动动态渲染 UI，支持自定义窗口宽高。
     */
    public void showAnimation(int n, int m, int[][] sources, int windowWidth, int windowHeight) {
        List<int[][]> frames = simulateFrames(n, m, sources);
        SwingUtilities.invokeLater(() -> {
            RendererFrame frame = new RendererFrame(frames, windowWidth, windowHeight);
            frame.setVisible(true);
        });
    }

    /**
     * 随机生成 sources。
     *
     * @param zeroCount  初始为 0 的单元格个数
     * @param colorKinds 颜色总数，颜色值范围为 [1, colorKinds]
     */
    public int[][] generateRandomSources(int n, int m, int zeroCount, int colorKinds) {
        int total = n * m;
        int safeZeroCount = Math.max(0, Math.min(zeroCount, total));
        int sourceCount = total - safeZeroCount;
        int safeColorKinds = Math.max(1, colorKinds);

        List<Integer> positions = new ArrayList<>(total);
        for (int idx = 0; idx < total; idx++) {
            positions.add(idx);
        }
        Collections.shuffle(positions);

        Random random = new Random();
        int[][] sources = new int[sourceCount][3];
        for (int i = 0; i < sourceCount; i++) {
            int pos = positions.get(i);
            int r = pos / m;
            int c = pos % m;
            int color = random.nextInt(safeColorKinds) + 1;
            sources[i] = new int[]{r, c, color};
        }
        return sources;
    }


    private static class RendererFrame extends JFrame {
        RendererFrame(List<int[][]> frames, int windowWidth, int windowHeight) {
            setTitle("Multi-Source Image Renderer");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setContentPane(new RenderPanel(frames));
            setSize(windowWidth, windowHeight);
            setMinimumSize(new Dimension(420, 320));
            setResizable(true);
            setLocationRelativeTo(null);
        }
    }

    private static class RenderPanel extends JPanel {
        private static final int PADDING = 24;
        private static final int LABEL_AREA_HEIGHT = 60;
        private static final int STEP_MS = 700;

        private final List<int[][]> frames;
        private final int rows;
        private final int cols;
        private int frameIndex = 0;

        RenderPanel(List<int[][]> frames) {
            this.frames = frames;
            this.rows = frames.get(0).length;
            this.cols = frames.get(0)[0].length;
            setBackground(new Color(245, 245, 245));

            Timer timer = new Timer(STEP_MS, e -> {
                if (frameIndex < frames.size() - 1) {
                    frameIndex++;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            });
            timer.setInitialDelay(600);
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                drawGrid(g2d);
                drawLabel(g2d);
            } finally {
                g2d.dispose();
            }
        }

        private void drawGrid(Graphics2D g2d) {
            int[][] cur = frames.get(frameIndex);
            int[] layout = computeLayout();
            int cellSize = layout[0];
            int startX = layout[1];
            int startY = layout[2];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int x = startX + c * cellSize;
                    int y = startY + r * cellSize;
                    int colorValue = cur[r][c];

                    g2d.setColor(toAwtColor(colorValue));
                    g2d.fillRoundRect(x, y, cellSize - 2, cellSize - 2, 12, 12);

                    g2d.setColor(new Color(60, 60, 60));
                    g2d.drawRoundRect(x, y, cellSize - 2, cellSize - 2, 12, 12);

                    int fontSize = Math.max(12, Math.min(22, cellSize / 3));
                    g2d.setFont(new Font("Consolas", Font.BOLD, fontSize));
                    String text = String.valueOf(colorValue);
                    FontMetrics fm = g2d.getFontMetrics();
                    int tx = x + (cellSize - fm.stringWidth(text)) / 2;
                    int ty = y + (cellSize + fm.getAscent() - fm.getDescent()) / 2 - 2;
                    g2d.drawString(text, tx, ty);
                }
            }
        }

        private void drawLabel(Graphics2D g2d) {
            String label = "Time Step: " + frameIndex + " / " + (frames.size() - 1);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(new Color(40, 40, 40));
            g2d.drawString(label, PADDING, getHeight() - 20);
        }

        private int[] computeLayout() {
            int drawableWidth = Math.max(100, getWidth() - PADDING * 2);
            int drawableHeight = Math.max(100, getHeight() - PADDING * 2 - LABEL_AREA_HEIGHT);

            int cellSize = Math.max(20, Math.min(drawableWidth / cols, drawableHeight / rows));
            int gridWidth = cellSize * cols;
            int gridHeight = cellSize * rows;
            int startX = (getWidth() - gridWidth) / 2;
            int startY = PADDING + Math.max(0, (drawableHeight - gridHeight) / 2);
            return new int[]{cellSize, startX, startY};
        }

        private Color toAwtColor(int value) {
            if (value == 0) {
                return new Color(232, 232, 232);
            }
            float hue = (value * 37 % 360) / 360f;
            return Color.getHSBColor(hue, 0.65f, 0.93f);
        }

        public static void main(String[] args) {
            MultiSourceImageRenderer renderer = new MultiSourceImageRenderer();

            int n = 50;
            int m = 50;
            int zeroCount = 2400;   // 控制初始 0 的个数
            int colorKinds = 10;   // 控制颜色总数（1~colorKinds）
            int[][] sources = renderer.generateRandomSources(n, m, zeroCount, colorKinds);

            int[][] result = renderer.colorGrid(n, m, sources);
            System.out.println("Sources:");
            for (int[] source : sources) {
                System.out.println(Arrays.toString(source));
            }
            System.out.println("Final Grid:");
            for (int[] row : result) {
                System.out.println(Arrays.toString(row));
            }

            // 这里可调节窗口大小（width, height）。
            renderer.showAnimation(n, m, sources, 1000, 760);
        }
    }
}
