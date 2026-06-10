package algorithm.visualized.Sokoban;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * 经典推箱子游戏 — 深色主题，支持手动游玩、回退、解答、关卡选择。
 */
public class SokobanGame {

    // ---------- 配色（高对比，贴近经典手游） ----------
    private static final Color BG_VOID = new Color(18, 20, 24);
    private static final Color BG_PANEL = new Color(32, 34, 40);
    private static final Color TEXT_LIGHT = new Color(220, 222, 228);

    /**
     * 可行走地面：偏亮灰，与墙形成明显反差
     */
    private static final Color TILE_FLOOR = new Color(128, 133, 142);
    private static final Color TILE_FLOOR_EDGE = new Color(95, 100, 108);

    /**
     * 墙体：深灰 + 立体边
     */
    private static final Color TILE_WALL = new Color(52, 56, 64);
    private static final Color TILE_WALL_HI = new Color(98, 103, 112);
    private static final Color TILE_WALL_LO = new Color(28, 30, 36);

    /**
     * 目标点：深色圆环
     */
    private static final Color TARGET_STROKE = new Color(38, 42, 50);

    /**
     * 箱子：高饱和金黄
     */
    private static final Color BOX_FILL = new Color(248, 200, 38);
    private static final Color BOX_X = new Color(55, 40, 8);

    /**
     * 玩家：深灰胶囊
     */
    private static final Color PLAYER_FILL = new Color(42, 46, 54);
    private static final Color PLAYER_OUTLINE = new Color(28, 30, 36);

    public static void main(String[] args) {
        launch();
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("推箱子");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new GameRoot());
            frame.pack();
            frame.setMinimumSize(new Dimension(900, 1000));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static final class GameRoot extends JPanel {

        private final TopBar topBar = new TopBar();
        private final BoardPanel board = new BoardPanel();
        private final BottomBar bottomBar = new BottomBar();
        private final SokobanGameEngine engine = new SokobanGameEngine(MicrobanLevelRepository.get(0));

        private int levelIndex;
        private boolean playbackMode;
        private int playbackIndex;
        private List<SokobanGameEngine.PlaybackFrame> playbackFrames;
        private Timer playbackTimer;
        private int playbackDelayMs = 140;

        GameRoot() {
            setLayout(new BorderLayout());
            setBackground(BG_VOID);

            add(topBar, BorderLayout.NORTH);
            add(board, BorderLayout.CENTER);
            add(bottomBar, BorderLayout.SOUTH);

            loadLevel(0);
            bindKeys();
            wireBottomBar();
        }

        private void bindKeys() {
            InputMap im = board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = board.getActionMap();

            bindMove(im, am, KeyEvent.VK_UP, Direction.UP);
            bindMove(im, am, KeyEvent.VK_W, Direction.UP);
            bindMove(im, am, KeyEvent.VK_DOWN, Direction.DOWN);
            bindMove(im, am, KeyEvent.VK_S, Direction.DOWN);
            bindMove(im, am, KeyEvent.VK_LEFT, Direction.LEFT);
            bindMove(im, am, KeyEvent.VK_A, Direction.LEFT);
            bindMove(im, am, KeyEvent.VK_RIGHT, Direction.RIGHT);
            bindMove(im, am, KeyEvent.VK_D, Direction.RIGHT);

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "undo");
            am.put("undo", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doUndo();
                }
            });
        }

        private void bindMove(InputMap im, ActionMap am, int key, Direction dir) {
            String id = "move_" + dir.name();
            im.put(KeyStroke.getKeyStroke(key, 0), id);
            am.put(id, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doPlayerMove(dir);
                }
            });
        }

        private void wireBottomBar() {
            bottomBar.undoBtn.addActionListener(e -> doUndo());
            bottomBar.solutionBtn.addActionListener(e -> showSolution());
            bottomBar.levelBtn.addActionListener(e -> openLevelDialog());
            bottomBar.resetBtn.addActionListener(e -> resetLevel());
            bottomBar.settingsBtn.addActionListener(e -> openSettingsDialog());
        }

        private void loadLevel(int index) {
            stopPlayback();
            levelIndex = index;
            engine.loadLevel(MicrobanLevelRepository.get(index));
            refreshTopBar();
            board.repaint();
            board.requestFocusInWindow();
        }

        private void resetLevel() {
            stopPlayback();
            engine.resetLevel();
            refreshTopBar();
            board.repaint();
        }

        private void refreshTopBar() {
            topBar.update(levelIndex, engine.pushCount(), engine.moveCount(), playbackMode);
        }

        private void doPlayerMove(Direction dir) {
            if (playbackMode) {
                return;
            }
            if (engine.tryMove(dir).success) {
                refreshTopBar();
                board.repaint();
                if (engine.isWon()) {
                    showWinDialog();
                }
            }
        }

        private void doUndo() {
            if (playbackMode) {
                stopPlayback();
                refreshTopBar();
                board.repaint();
                return;
            }
            if (engine.undo()) {
                refreshTopBar();
                board.repaint();
            }
        }

        private void showSolution() {
            if (playbackMode) {
                stopPlayback();
                refreshTopBar();
                board.repaint();
                return;
            }

            engine.resetLevel();
            refreshTopBar();
            board.repaint();

            String lurd = SokobanSolutionBank.lookup(levelIndex);
            if (lurd == null || lurd.isEmpty()) {
                showInfoDialog(
                        "本关（" + (levelIndex + 1) + "）暂无预存最优解答。\n"
                                + "全合集共 " + MicrobanLevelRepository.count() + " 关，已收录 "
                                + SokobanSolutionBank.count() + " 关最优解。\n"
                                + "缺失关卡：93、144、153（公开求解库未发布）。");
                return;
            }

            playbackFrames = SokobanGameEngine.buildPlaybackFromLurd(engine.level(), lurd);
            if (playbackFrames.size() <= 1) {
                showInfoDialog("预存解答与当前关卡不匹配（关卡 " + (levelIndex + 1) + "）。");
                return;
            }
            if (!playbackFrames.get(playbackFrames.size() - 1).state.isGoal(engine.level())) {
                showInfoDialog("预存解答未能完成本关（关卡 " + (levelIndex + 1) + "）。");
                return;
            }
            startPlayback();
        }

        private void showInfoDialog(String message) {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(GameRoot.this);
            JOptionPane.showMessageDialog(
                    owner != null ? owner : GameRoot.this,
                    message,
                    "解答",
                    JOptionPane.INFORMATION_MESSAGE);
            if (owner != null) {
                owner.toFront();
                owner.requestFocus();
            }
            board.requestFocusInWindow();
        }

        private void startPlayback() {
            playbackMode = true;
            playbackIndex = 0;
            bottomBar.solutionBtn.setEnabled(true);
            refreshTopBar();
            board.repaint();
            if (playbackTimer != null) {
                playbackTimer.stop();
            }
            playbackTimer = new Timer(playbackDelayMs, e -> advancePlayback());
            playbackTimer.setInitialDelay(80);
            playbackTimer.start();
        }

        private void advancePlayback() {
            if (playbackFrames == null || playbackIndex >= playbackFrames.size() - 1) {
                finishPlayback();
                return;
            }
            playbackIndex++;
            SokobanGameEngine.PlaybackFrame frame = playbackFrames.get(playbackIndex);
            engine.setStateForPlayback(frame.state, frame.moveCount, frame.pushCount);
            refreshTopBar();
            board.repaint();
            if (playbackIndex >= playbackFrames.size() - 1) {
                finishPlayback();
                if (engine.isWon()) {
                    showWinDialog();
                }
            }
        }

        private void finishPlayback() {
            stopPlayback();
            if (playbackFrames != null && !playbackFrames.isEmpty()) {
                SokobanGameEngine.PlaybackFrame last = playbackFrames.get(playbackFrames.size() - 1);
                engine.syncCountersAfterPlayback(last.moveCount, last.pushCount);
            }
            refreshTopBar();
            board.repaint();
        }

        private void stopPlayback() {
            playbackMode = false;
            if (playbackTimer != null) {
                playbackTimer.stop();
            }
            playbackFrames = null;
            playbackIndex = 0;
        }

        private void showWinDialog() {
            JOptionPane.showMessageDialog(this,
                    String.format("恭喜通关！\n推箱 %d 步，移动 %d 格。",
                            engine.pushCount(), engine.moveCount()),
                    "过关", JOptionPane.INFORMATION_MESSAGE);
        }

        private void openLevelDialog() {
            stopPlayback();
            int total = MicrobanLevelRepository.count();
            JDialog dialog = new JDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    "选择关卡 · Microban (" + total + ")", true);
            dialog.getContentPane().setBackground(BG_VOID);
            dialog.setLayout(new BorderLayout());

            JPanel grid = new JPanel(new GridLayout(0, 5, 8, 8));
            grid.setBackground(BG_VOID);
            grid.setBorder(new EmptyBorder(16, 16, 12, 16));

            for (int i = 0; i < total; i++) {
                final int idx = i;
                JButton btn = new JButton(String.valueOf(i + 1));
                btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                btn.setPreferredSize(new Dimension(52, 42));
                btn.setFocusPainted(false);
                btn.setForeground(TEXT_LIGHT);
                if (idx == levelIndex) {
                    btn.setBackground(new Color(70, 120, 200));
                } else if (MicrobanLevelRepository.hasSolution(idx)) {
                    btn.setBackground(new Color(45, 58, 48));
                } else {
                    btn.setBackground(BG_PANEL);
                }
                btn.setBorder(new LineBorder(new Color(55, 58, 66), 1, true));
                btn.addActionListener(e -> {
                    loadLevel(idx);
                    dialog.dispose();
                });
                grid.add(btn);
            }

            JScrollPane scroll = new JScrollPane(grid);
            scroll.setBorder(null);
            scroll.getViewport().setBackground(BG_VOID);
            scroll.setPreferredSize(new Dimension(320, 420));
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            JLabel hint = new JLabel("  深绿 = 有预存最优解答（" + SokobanSolutionBank.count()
                    + "/" + total + "）  |  共 " + total + " 关");
            hint.setForeground(new Color(140, 145, 155));
            hint.setBorder(new EmptyBorder(8, 12, 10, 12));

            dialog.add(scroll, BorderLayout.CENTER);
            dialog.add(hint, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }

        private void openSettingsDialog() {
            JDialog dialog = new JDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this), "设置", true);
            dialog.getContentPane().setBackground(BG_VOID);
            dialog.setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(BG_VOID);
            panel.setBorder(new EmptyBorder(20, 24, 12, 24));

            JLabel speedLabel = new JLabel("解答回放速度（毫秒/格）");
            speedLabel.setForeground(TEXT_LIGHT);
            speedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JSlider slider = new JSlider(40, 400, playbackDelayMs);
            slider.setBackground(BG_VOID);
            slider.setForeground(TEXT_LIGHT);
            slider.setAlignmentX(Component.LEFT_ALIGNMENT);
            slider.addChangeListener(e -> {
                playbackDelayMs = slider.getValue();
                if (playbackTimer != null) {
                    playbackTimer.setDelay(playbackDelayMs);
                }
            });

            JTextArea help = new JTextArea(
                    "方向键 / WASD — 移动\n"
                            + "Z — 回退\n"
                            + "底部按钮 — 回退 / 解答 / 关卡 / 重置 / 设置\n"
                            + "关卡集 — 经典 Microban 155 关（预存最优解答）");
            help.setEditable(false);
            help.setOpaque(false);
            help.setForeground(new Color(150, 155, 165));
            help.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
            help.setAlignmentX(Component.LEFT_ALIGNMENT);

            panel.add(speedLabel);
            panel.add(Box.createVerticalStrut(8));
            panel.add(slider);
            panel.add(Box.createVerticalStrut(16));
            panel.add(help);

            JButton close = new JButton("确定");
            close.addActionListener(e -> dialog.dispose());
            JPanel south = new JPanel();
            south.setBackground(BG_VOID);
            south.add(close);

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(south, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }

        // ======================== 顶栏 ========================

        private static final class TopBar extends JPanel {
            private final JLabel levelLabel = new JLabel();
            private final JLabel pushLabel = new JLabel();
            private final JLabel moveLabel = new JLabel();

            TopBar() {
                setLayout(new GridLayout(1, 3));
                setBackground(BG_VOID);
                setBorder(new EmptyBorder(16, 8, 12, 8));
                for (JLabel lb : new JLabel[]{levelLabel, pushLabel, moveLabel}) {
                    lb.setHorizontalAlignment(SwingConstants.CENTER);
                    lb.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    lb.setForeground(TEXT_LIGHT);
                    add(lb);
                }
            }

            void update(int level, int pushes, int moves, boolean solving) {
                if (solving) {
                    levelLabel.setText("关卡  " + (level + 1) + "  ◎ 回放中");
                    pushLabel.setText("步数  " + pushes);
                    moveLabel.setText("移动  " + moves);
                } else {
                    levelLabel.setText("关卡  " + (level + 1));
                    pushLabel.setText("步数  " + pushes);
                    moveLabel.setText("移动  " + moves);
                }
            }
        }

        // ======================== 棋盘 ========================

        private class BoardPanel extends JPanel {

            /**
             * 地板格之间的缝隙（露出深色底色，强化格子感）
             */
            private static final int TILE_GAP = 3;

            BoardPanel() {
                setBackground(BG_VOID);
                setFocusable(true);
                setPreferredSize(new Dimension(360, 360));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    SokobanData map = engine.level();
                    SokobanState state = engine.state();
                    int rows = map.rows();
                    int cols = map.cols();

                    int pad = 10;
                    int availW = getWidth() - pad * 2;
                    int availH = getHeight() - pad * 2;
                    int cell = Math.max(48, Math.min(availW / cols, availH / rows));
                    int gridW = cell * cols;
                    int gridH = cell * rows;
                    int startX = (getWidth() - gridW) / 2;
                    int startY = (getHeight() - gridH) / 2;

                    // 第一层：墙与可玩区域地板（装饰空腔不绘制）
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            if (!map.isVisible(r, c)) {
                                continue;
                            }
                            int x = startX + c * cell;
                            int y = startY + r * cell;
                            if (map.isWall(r, c)) {
                                drawWall(g2, x, y, cell);
                            } else {
                                drawFloor(g2, x, y, cell);
                            }
                        }
                    }

                    // 第二层：目标圆环（在箱子/玩家下方，无箱子时可见）
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            if (map.isTarget(r, c) && map.isVisible(r, c) && !state.hasBoxAt(r, c)) {
                                int x = startX + c * cell;
                                int y = startY + r * cell;
                                drawTarget(g2, x, y, cell);
                            }
                        }
                    }

                    // 第三层：箱子
                    for (int i = 0; i < state.boxCount(); i++) {
                        int r = state.boxRows[i];
                        int c = state.boxCols[i];
                        drawBox(g2, startX + c * cell, startY + r * cell, cell,
                                map.isTarget(r, c));
                    }

                    // 第四层：玩家
                    drawPlayer(g2,
                            startX + state.playerCol * cell,
                            startY + state.playerRow * cell,
                            cell);
                } finally {
                    g2.dispose();
                }
            }

            /**
             * 地板：亮灰方块，与深色缝隙形成对比
             */
            private void drawFloor(Graphics2D g2, int x, int y, int cell) {
                int g = TILE_GAP;
                int ix = x + g;
                int iy = y + g;
                int s = cell - g * 2;
                g2.setColor(TILE_FLOOR);
                g2.fillRect(ix, iy, s, s);
                g2.setColor(TILE_FLOOR_EDGE);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRect(ix, iy, s - 1, s - 1);
            }

            /**
             * 墙体：深灰立体砖，明显重于地板
             */
            private void drawWall(Graphics2D g2, int x, int y, int cell) {
                g2.setColor(TILE_WALL);
                g2.fillRect(x, y, cell, cell);
                g2.setColor(TILE_WALL_HI);
                g2.drawLine(x, y, x + cell - 1, y);
                g2.drawLine(x, y, x, y + cell - 1);
                g2.setColor(TILE_WALL_LO);
                g2.drawLine(x + 1, y + cell - 1, x + cell - 1, y + cell - 1);
                g2.drawLine(x + cell - 1, y + 1, x + cell - 1, y + cell - 1);
            }

            /**
             * 目标：深色空心圆
             */
            private void drawTarget(Graphics2D g2, int x, int y, int cell) {
                int g = TILE_GAP;
                int margin = cell / 5;
                float d = cell - g * 2 - margin * 2;
                float ox = x + g + margin;
                float oy = y + g + margin;
                g2.setColor(TARGET_STROKE);
                g2.setStroke(new BasicStroke(Math.max(2.5f, cell / 11f),
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(new Ellipse2D.Float(ox, oy, d, d));
            }

            /**
             * 箱子：亮黄方块。
             * 未到位 — 粗 X；已推到目标 — 粗圆圈（与空目标点圆环区分，画在箱子正中）。
             */
            private void drawBox(Graphics2D g2, int x, int y, int cell, boolean onTarget) {
                int g = TILE_GAP;
                int m = Math.max(2, cell / 14);
                int ix = x + g + m;
                int iy = y + g + m;
                int s = cell - (g + m) * 2;

                g2.setColor(onTarget ? new Color(230, 175, 30) : BOX_FILL);
                g2.fill(new RoundRectangle2D.Float(ix, iy, s, s, 4, 4));

                g2.setStroke(new BasicStroke(Math.max(3f, cell / 9f),
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(BOX_X);
                int cx = x + cell / 2;
                int cy = y + cell / 2;

                if (onTarget) {
                    int radius = cell / 4;
                    g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
                } else {
                    int arm = cell / 3;
                    g2.drawLine(cx - arm, cy - arm, cx + arm, cy + arm);
                    g2.drawLine(cx + arm, cy - arm, cx - arm, cy + arm);
                }
            }

            /**
             * 玩家：深灰竖胶囊
             */
            private void drawPlayer(Graphics2D g2, int x, int y, int cell) {
                int g = TILE_GAP;
                int cx = x + cell / 2;
                int cy = y + cell / 2;
                int bodyW = Math.max(10, (cell - g * 2) / 3);
                int bodyH = Math.max(14, (cell - g * 2) * 5 / 9);
                int top = cy - bodyH / 2 + 2;

                g2.setColor(PLAYER_FILL);
                g2.fill(new RoundRectangle2D.Float(
                        cx - bodyW / 2f, top, bodyW, bodyH, bodyW, bodyW));
                g2.setColor(PLAYER_OUTLINE);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(
                        cx - bodyW / 2f, top, bodyW, bodyH, bodyW, bodyW));
                // 头顶小圆点
                int dotR = Math.max(3, bodyW / 4);
                g2.fillOval(cx - dotR, top - dotR / 2, dotR * 2, dotR * 2);
            }
        }

        // ======================== 底栏 ========================

        private static final class BottomBar extends JPanel {
            final JButton undoBtn = toolButton("回退", "↶");
            final JButton solutionBtn = toolButton("解答", "◎");
            final JButton levelBtn = toolButton("关卡", "☰");
            final JButton resetBtn = toolButton("重置", "↺");
            final JButton settingsBtn = toolButton("设置", "⚙");

            BottomBar() {
                setLayout(new GridLayout(1, 5, 8, 0));
                setBackground(BG_VOID);
                setBorder(new EmptyBorder(8, 10, 14, 10));
                add(undoBtn);
                add(solutionBtn);
                add(levelBtn);
                add(resetBtn);
                add(settingsBtn);
            }

            private static JButton toolButton(String text, String icon) {
                JButton btn = new JButton(
                        "<html><center><span style='font-size:20px;color:#DCDEE4'>"
                                + icon + "</span><br>"
                                + "<span style='font-size:11px;color:#B0B4BC'>" + text + "</span>"
                                + "</center></html>");
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(new EmptyBorder(6, 4, 6, 4));
                btn.setPreferredSize(new Dimension(64, 62));
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return btn;
            }
        }
    }
}
