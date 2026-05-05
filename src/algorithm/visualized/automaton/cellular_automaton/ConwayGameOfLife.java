package algorithm.visualized.automaton.cellular_automaton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

/**
 * @Description: 康威生命游戏 — 环面拓扑（边界环绕），Swing 实时可视化
 * @Date: 2026/5/5
 */
public class ConwayGameOfLife {

    /**
     * 推进一代（标准 B3/S23 规则）。
     */
    public static void step(boolean[][] grid, boolean[][] buffer, boolean toroidal) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int n = countNeighbors(grid, r, c, toroidal);
                boolean alive = grid[r][c];
                buffer[r][c] = alive ? (n == 2 || n == 3) : (n == 3);
            }
        }
        copyInto(buffer, grid);
    }

    private static int countNeighbors(boolean[][] grid, int r, int c, boolean toroidal) {
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int nr = r + dr;
                int nc = c + dc;
                if (toroidal) {
                    nr = (nr + rows) % rows;
                    nc = (nc + cols) % cols;
                } else if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (grid[nr][nc]) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void copyInto(boolean[][] src, boolean[][] dst) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }
    }

    public static void randomFill(boolean[][] grid, double density, Random random) {
        for (boolean[] row : grid) {
            for (int c = 0; c < row.length; c++) {
                row[c] = random.nextDouble() < density;
            }
        }
    }

    public static void clear(boolean[][] grid) {
        for (boolean[] row : grid) {
            Arrays.fill(row, false);
        }
    }

    /**
     * Gosper 滑翔机枪 — 需网格至少约 40×40，置于左上角偏移处
     */
    public static void placeGosperGliderGun(boolean[][] grid, int topRow, int leftCol) {
        int[][] cells = {
                {0, 24}, {1, 22}, {1, 24}, {2, 12}, {2, 13}, {2, 20}, {2, 21}, {2, 34}, {2, 35},
                {3, 11}, {3, 15}, {3, 20}, {3, 21}, {3, 34}, {3, 35}, {4, 0}, {4, 1}, {4, 10},
                {4, 16}, {4, 20}, {4, 21}, {5, 0}, {5, 1}, {5, 10}, {5, 14}, {5, 16}, {5, 22}, {5, 24},
                {6, 10}, {6, 16}, {6, 24}, {7, 11}, {7, 15}, {8, 12}, {8, 13}, {9, 13}
        };
        for (int[] cell : cells) {
            int r = topRow + cell[0];
            int c = leftCol + cell[1];
            if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length) {
                grid[r][c] = true;
            }
        }
    }

    public static void showVisualization(int rows, int cols, int tickMs, boolean toroidal) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("康威生命游戏");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            LifePanel panel = new LifePanel(rows, cols, tickMs, toroidal);
            JPanel south = buildControlBar(panel);

            JPanel root = new JPanel(new BorderLayout());
            root.add(panel, BorderLayout.CENTER);
            root.add(south, BorderLayout.SOUTH);

            frame.setContentPane(root);
            frame.pack();
            frame.setMinimumSize(new Dimension(520, 420));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.requestFocusInWindow();
        });
    }

    private static JPanel buildControlBar(LifePanel panel) {
        JButton pause = new JButton("暂停");
        JButton step = new JButton("单步");
        JButton random = new JButton("随机");
        JButton gun = new JButton("滑翔机枪");
        JButton clear = new JButton("清空");
        JCheckBox torus = new JCheckBox("环面边界", panel.toroidal);
        JSlider speed = new JSlider(10, 400, panel.tickMs);
        speed.setMajorTickSpacing(100);
        speed.setPaintTicks(true);

        pause.addActionListener(e -> panel.togglePause(pause));
        step.addActionListener(e -> panel.singleStep());
        random.addActionListener(e -> panel.randomize());
        gun.addActionListener(e -> panel.placeGunPattern());
        clear.addActionListener(e -> panel.clearBoard());
        torus.addActionListener(e -> panel.setToroidal(torus.isSelected()));
        speed.addChangeListener(e -> {
            if (!speed.getValueIsAdjusting()) {
                panel.setTickMs(speed.getValue());
            }
        });

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        bar.add(pause);
        bar.add(step);
        bar.add(random);
        bar.add(gun);
        bar.add(clear);
        bar.add(torus);
        bar.add(new JLabel("速度(ms):"));
        bar.add(speed);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBorder(new EmptyBorder(0, 8, 8, 8));
        wrap.add(bar, BorderLayout.CENTER);
        return wrap;
    }

    private static final class LifePanel extends JPanel {
        private static final int PADDING = 20;
        private static final int LABEL_AREA = 36;

        private final boolean[][] grid;
        private final boolean[][] buffer;
        private final Timer timer;
        private long generation;
        private boolean paused;
        private boolean toroidal;
        private int tickMs;

        LifePanel(int rows, int cols, int tickMs, boolean toroidal) {
            this.grid = new boolean[rows][cols];
            this.buffer = new boolean[rows][cols];
            this.tickMs = tickMs;
            this.toroidal = toroidal;
            setBackground(new Color(248, 248, 248));
            setFocusable(true);

            Random rnd = new Random();
            randomFill(grid, 0.34, rnd);

            timer = new Timer(tickMs, e -> {
                if (!paused) {
                    advance();
                }
            });
            timer.start();

            bindKeys();

            setPreferredSize(new Dimension(960, 720));
        }

        private void bindKeys() {
            InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "togglePause");
            am.put("togglePause", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    togglePause(null);
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "step");
            am.put("step", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    singleStep();
                }
            });
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "random");
            am.put("random", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    randomize();
                }
            });
        }

        void setToroidal(boolean value) {
            this.toroidal = value;
        }

        void setTickMs(int ms) {
            this.tickMs = ms;
            timer.setDelay(ms);
        }

        void togglePause(JButton pauseButton) {
            paused = !paused;
            if (pauseButton != null) {
                pauseButton.setText(paused ? "继续" : "暂停");
            }
            repaint();
        }

        void singleStep() {
            advance();
            repaint();
        }

        void randomize() {
            randomFill(grid, 0.34, new Random());
            generation = 0;
            repaint();
        }

        void clearBoard() {
            clear(grid);
            generation = 0;
            repaint();
        }

        void placeGunPattern() {
            clear(grid);
            int margin = 4;
            placeGosperGliderGun(grid, margin, margin);
            generation = 0;
            repaint();
        }

        private void advance() {
            step(grid, buffer, toroidal);
            generation++;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int rows = grid.length;
                int cols = grid[0].length;
                int[] layout = computeLayout(rows, cols);
                int cell = layout[0];
                int sx = layout[1];
                int sy = layout[2];

                Color dead = new Color(235, 235, 238);
                Color alive = new Color(35, 120, 85);
                Color line = new Color(210, 210, 215);

                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        int x = sx + c * cell;
                        int y = sy + r * cell;
                        g2.setColor(grid[r][c] ? alive : dead);
                        g2.fillRect(x, y, cell, cell);
                        if (cell >= 4) {
                            g2.setColor(line);
                            g2.drawRect(x, y, cell - 1, cell - 1);
                        }
                    }
                }

                String status = String.format(
                        "代数: %d   %s   空格:暂停/继续  S:单步  R:随机",
                        generation,
                        paused ? "已暂停" : "运行中");
                g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                g2.setColor(new Color(45, 45, 48));
                g2.drawString(status, PADDING, getHeight() - 12);
            } finally {
                g2.dispose();
            }
        }

        private int[] computeLayout(int rows, int cols) {
            int dw = Math.max(80, getWidth() - PADDING * 2);
            int dh = Math.max(80, getHeight() - PADDING * 2 - LABEL_AREA);
            int cell = Math.max(2, Math.min(dw / cols, dh / rows));
            int gw = cell * cols;
            int gh = cell * rows;
            int startX = (getWidth() - gw) / 2;
            int startY = PADDING + Math.max(0, (dh - gh) / 2);
            return new int[]{cell, startX, startY};
        }
    }

    public static void main(String[] args) {
        int rows = 72;
        int cols = 96;
        int tickMs = 90;
        boolean toroidal = true;
        showVisualization(rows, cols, tickMs, toroidal);
    }
}
