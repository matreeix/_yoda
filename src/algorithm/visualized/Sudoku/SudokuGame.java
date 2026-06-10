package algorithm.visualized.Sudoku;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 可视化数独游戏：手动游玩、冲突提示、三种算法一键求解、回溯逐步演示。
 */
public class SudokuGame {

    private static final Color BG = new Color(245, 247, 250);
    private static final Color PANEL_BG = new Color(255, 255, 255);
    private static final Color GRID_LINE = new Color(180, 186, 198);
    private static final Color BOX_LINE = new Color(55, 62, 78);
    private static final Color CELL_BG = new Color(252, 252, 254);
    private static final Color FIXED_BG = new Color(232, 236, 244);
    private static final Color SELECT_BG = new Color(210, 228, 255);
    private static final Color PEER_BG = new Color(235, 242, 252);
    private static final Color CONFLICT_BG = new Color(255, 210, 210);
    private static final Color TRY_BG = new Color(210, 255, 210);
    private static final Color BACKTRACK_BG = new Color(255, 240, 200);
    private static final Color TEXT_FIXED = new Color(30, 40, 60);
    private static final Color TEXT_USER = new Color(40, 90, 200);
    private static final Color TEXT_CONFLICT = new Color(200, 40, 40);

    private static final int DEFAULT_CELL = 54;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::launch);
    }

    public static void launch() {
        JFrame frame = new JFrame("数独");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new GameRoot());
        Dimension windowSize = new Dimension(1020, 920);
        frame.setMinimumSize(windowSize);
        frame.setSize(windowSize);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static final class GameRoot extends JPanel {

        private final BoardPanel boardPanel = new BoardPanel();
        private final JLabel statusLabel = new JLabel(" ");
        private final JComboBox<SudokuEngine.SolverMethod> solverBox = new JComboBox<>(SudokuEngine.SolverMethod.values());
        private final JComboBox<String> puzzleBox = new JComboBox<>();

        private SudokuData data = new SudokuData(0);
        private SudokuEngine engine = new SudokuEngine(data);
        private int puzzleIndex;
        private int selRow = 0;
        private int selCol = 0;
        private boolean animating;
        private Timer animTimer;

        GameRoot() {
            setLayout(new BorderLayout(12, 12));
            setBackground(BG);
            setBorder(new EmptyBorder(14, 14, 14, 14));

            add(buildTopBar(), BorderLayout.NORTH);
            add(buildCenter(), BorderLayout.CENTER);
            add(buildBottomBar(), BorderLayout.SOUTH);

            refreshPuzzleBox();
            bindKeys();
            updateStatus("点击格子选中，按 1-9 填数，Delete 清除");
        }

        private JPanel buildTopBar() {
            JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
            bar.setOpaque(false);

            JLabel title = new JLabel("数独");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            bar.add(title);

            puzzleBox.addActionListener(e -> {
                if (animating) {
                    return;
                }
                int idx = puzzleBox.getSelectedIndex();
                if (idx >= 0 && idx != puzzleIndex) {
                    loadPuzzle(idx);
                }
            });
            bar.add(puzzleBox);

            bar.add(new JLabel("求解算法:"));
            solverBox.setSelectedItem(SudokuEngine.SolverMethod.BITMAP_MRV);
            bar.add(solverBox);

            return bar;
        }

        private JPanel buildCenter() {
            JPanel center = new JPanel(new BorderLayout(16, 0));
            center.setOpaque(false);
            center.add(boardPanel, BorderLayout.CENTER);
            center.add(buildSidePanel(), BorderLayout.EAST);
            return center;
        }

        private JPanel buildSidePanel() {
            JPanel side = new JPanel();
            side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
            side.setOpaque(false);
            side.setPreferredSize(new Dimension(180, 10));

            side.add(makeButton("上一题", e -> changePuzzle(-1)));
            side.add(Box.createVerticalStrut(8));
            side.add(makeButton("下一题", e -> changePuzzle(1)));
            side.add(Box.createVerticalStrut(8));
            side.add(makeButton("重置", e -> doReset()));
            side.add(Box.createVerticalStrut(8));
            side.add(makeButton("检查", e -> doCheck()));
            side.add(Box.createVerticalStrut(8));
            side.add(makeButton("提示 (1格)", e -> doHint()));
            side.add(Box.createVerticalStrut(8));
            side.add(makeButton("一键求解", e -> doSolveInstant()));
            side.add(Box.createVerticalStrut(8));
            side.add(makeButton("逐步演示", e -> doSolveAnimated()));

            side.add(Box.createVerticalGlue());

            JPanel pad = new JPanel(new GridLayout(3, 3, 4, 4));
            pad.setOpaque(false);
            pad.setMaximumSize(new Dimension(170, 170));
            pad.setAlignmentX(Component.CENTER_ALIGNMENT);
            for (int d = 1; d <= 9; d++) {
                final char digit = (char) ('0' + d);
                pad.add(makePadButton(String.valueOf(d), digit));
            }
            side.add(pad);

            return side;
        }

        private JButton makeButton(String text, java.awt.event.ActionListener action) {
            JButton btn = new JButton(text);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(160, 36));
            btn.addActionListener(action);
            return btn;
        }

        private JButton makePadButton(String text, char digit) {
            JButton btn = new JButton(text);
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 16f));
            btn.addActionListener(e -> placeDigit(digit));
            return btn;
        }

        private JPanel buildBottomBar() {
            JPanel bar = new JPanel(new BorderLayout());
            bar.setOpaque(false);
            statusLabel.setBorder(new EmptyBorder(4, 4, 0, 4));
            bar.add(statusLabel, BorderLayout.CENTER);

            JLabel hint = new JLabel("方向键移动 · 1-9 填数 · Delete 清除 · 双击清除");
            hint.setForeground(new Color(120, 125, 135));
            hint.setFont(hint.getFont().deriveFont(11f));
            bar.add(hint, BorderLayout.EAST);
            return bar;
        }

        private void refreshPuzzleBox() {
            puzzleBox.removeAllItems();
            for (int i = 0; i < SudokuData.puzzleCount(); i++) {
                puzzleBox.addItem(SudokuData.puzzleName(i));
            }
            puzzleBox.setSelectedIndex(puzzleIndex);
        }

        private void bindKeys() {
            InputMap im = boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = boardPanel.getActionMap();

            bindDigit(im, am, KeyEvent.VK_1, '1');
            bindDigit(im, am, KeyEvent.VK_2, '2');
            bindDigit(im, am, KeyEvent.VK_3, '3');
            bindDigit(im, am, KeyEvent.VK_4, '4');
            bindDigit(im, am, KeyEvent.VK_5, '5');
            bindDigit(im, am, KeyEvent.VK_6, '6');
            bindDigit(im, am, KeyEvent.VK_7, '7');
            bindDigit(im, am, KeyEvent.VK_8, '8');
            bindDigit(im, am, KeyEvent.VK_9, '9');
            bindDigit(im, am, KeyEvent.VK_NUMPAD1, '1');
            bindDigit(im, am, KeyEvent.VK_NUMPAD2, '2');
            bindDigit(im, am, KeyEvent.VK_NUMPAD3, '3');
            bindDigit(im, am, KeyEvent.VK_NUMPAD4, '4');
            bindDigit(im, am, KeyEvent.VK_NUMPAD5, '5');
            bindDigit(im, am, KeyEvent.VK_NUMPAD6, '6');
            bindDigit(im, am, KeyEvent.VK_NUMPAD7, '7');
            bindDigit(im, am, KeyEvent.VK_NUMPAD8, '8');
            bindDigit(im, am, KeyEvent.VK_NUMPAD9, '9');

            bindAction(im, am, KeyEvent.VK_DELETE, "clear", e -> placeDigit(SudokuData.EMPTY));
            bindAction(im, am, KeyEvent.VK_BACK_SPACE, "backspace", e -> placeDigit(SudokuData.EMPTY));
            bindAction(im, am, KeyEvent.VK_UP, "up", e -> moveSelection(-1, 0));
            bindAction(im, am, KeyEvent.VK_DOWN, "down", e -> moveSelection(1, 0));
            bindAction(im, am, KeyEvent.VK_LEFT, "left", e -> moveSelection(0, -1));
            bindAction(im, am, KeyEvent.VK_RIGHT, "right", e -> moveSelection(0, 1));
        }

        private void bindDigit(InputMap im, ActionMap am, int key, char digit) {
            String id = "digit_" + digit;
            im.put(KeyStroke.getKeyStroke(key, 0), id);
            am.put(id, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    placeDigit(digit);
                }
            });
        }

        private void bindAction(InputMap im, ActionMap am, int key, String id, java.awt.event.ActionListener listener) {
            im.put(KeyStroke.getKeyStroke(key, 0), id);
            am.put(id, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listener.actionPerformed(e);
                }
            });
        }

        private void loadPuzzle(int index) {
            stopAnimation();
            puzzleIndex = index;
            data.loadPuzzle(index);
            selRow = 0;
            selCol = 0;
            boardPanel.repaint();
            updateStatus("已加载：" + SudokuData.puzzleName(index));
        }

        private void changePuzzle(int delta) {
            loadPuzzle((puzzleIndex + delta + SudokuData.puzzleCount()) % SudokuData.puzzleCount());
            puzzleBox.setSelectedIndex(puzzleIndex);
        }

        private void doReset() {
            if (animating) {
                return;
            }
            data.reset();
            boardPanel.repaint();
            updateStatus("已重置到题目初始状态");
        }

        private void doCheck() {
            if (engine.isComplete()) {
                updateStatus("恭喜，数独已完成且全部正确！");
                JOptionPane.showMessageDialog(this, "完成！所有数字均正确。", "检查", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int conflicts = countConflicts();
            int empty = engine.countEmptyCells();
            if (conflicts > 0) {
                updateStatus("发现 " + conflicts + " 处冲突，请修正（红色高亮）");
            } else if (empty > 0) {
                updateStatus("暂无冲突，还剩 " + empty + " 个空格");
            }
            boardPanel.repaint();
        }

        private int countConflicts() {
            int n = 0;
            for (int r = 0; r < SudokuData.SIZE; r++) {
                for (int c = 0; c < SudokuData.SIZE; c++) {
                    if (engine.hasConflict(r, c)) {
                        n++;
                    }
                }
            }
            return n;
        }

        private void doHint() {
            if (animating || data.isFixed(selRow, selCol) || !data.isEmpty(selRow, selCol)) {
                if (!engine.hintOne()) {
                    updateStatus("无法提示：题目可能无解或已完成");
                } else {
                    boardPanel.repaint();
                    afterMove("已填入一格提示");
                }
                return;
            }
            if (!engine.hintOne()) {
                updateStatus("无法提示：题目可能无解或已完成");
            } else {
                boardPanel.repaint();
                afterMove("已填入一格提示");
            }
        }

        private void doSolveInstant() {
            if (animating) {
                return;
            }
            SudokuEngine.SolverMethod method = (SudokuEngine.SolverMethod) solverBox.getSelectedItem();
            if (method == null) {
                method = SudokuEngine.SolverMethod.BITMAP_MRV;
            }
            long ns = engine.solveInstant(method);
            boardPanel.repaint();
            double ms = ns / 1_000_000.0;
            updateStatus(String.format("已用「%s」求解完成，耗时 %.3f ms", method.label, ms));
            if (engine.isComplete()) {
                JOptionPane.showMessageDialog(this,
                        String.format("求解完成！\n算法：%s\n耗时：%.3f ms", method.label, ms),
                        "求解", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void doSolveAnimated() {
            if (animating) {
                stopAnimation();
                return;
            }
            data.reset();
            boardPanel.repaint();

            animating = true;
            updateStatus("逐步演示回溯法求解中…（再次点击可停止）");

            java.util.ArrayDeque<SudokuEngine.SolveStep> steps = new java.util.ArrayDeque<>();
            char[][] snapshot = SudokuData.copyBoard(data.getBoard());

            boolean ok = engine.solveAnimated(step -> {
                if (step.action != SudokuEngine.StepAction.DONE) {
                    steps.addLast(step);
                }
            });

            if (!ok) {
                animating = false;
                for (int r = 0; r < SudokuData.SIZE; r++) {
                    System.arraycopy(snapshot[r], 0, data.getBoard()[r], 0, SudokuData.SIZE);
                }
                boardPanel.repaint();
                updateStatus("题目无解，演示中止");
                return;
            }

            if (steps.isEmpty()) {
                animating = false;
                boardPanel.repaint();
                updateStatus("题目已全部填满");
                return;
            }

            final int delayMs = 35;
            animTimer = new Timer(delayMs, null);
            animTimer.addActionListener(e -> {
                if (steps.isEmpty()) {
                    stopAnimation();
                    boardPanel.repaint();
                    updateStatus("回溯演示完成");
                    if (engine.isComplete()) {
                        JOptionPane.showMessageDialog(GameRoot.this, "演示完成，数独已解出！", "演示", JOptionPane.INFORMATION_MESSAGE);
                    }
                    return;
                }
                SudokuEngine.SolveStep step = steps.removeFirst();
                char[][] board = data.getBoard();
                if (!data.isFixed(step.row, step.col)) {
                    board[step.row][step.col] = step.digit;
                }
                boardPanel.highlightRow = step.row;
                boardPanel.highlightCol = step.col;
                boardPanel.stepAction = step.action;
                boardPanel.repaint();
            });
            animTimer.start();
        }

        private void stopAnimation() {
            animating = false;
            if (animTimer != null) {
                animTimer.stop();
                animTimer = null;
            }
            boardPanel.highlightRow = -1;
            boardPanel.highlightCol = -1;
            boardPanel.stepAction = null;
        }

        private void placeDigit(char digit) {
            if (animating || data.isFixed(selRow, selCol)) {
                return;
            }
            engine.placeDigit(selRow, selCol, digit);
            boardPanel.repaint();
            afterMove(digit == SudokuData.EMPTY ? "已清除" : "填入 " + digit);
        }

        private void afterMove(String msg) {
            if (engine.isComplete()) {
                updateStatus("恭喜完成！ " + msg);
                JOptionPane.showMessageDialog(this, "你完成了这道数独！", "完成", JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateStatus(msg);
            }
        }

        private void moveSelection(int dr, int dc) {
            if (animating) {
                return;
            }
            selRow = (selRow + dr + SudokuData.SIZE) % SudokuData.SIZE;
            selCol = (selCol + dc + SudokuData.SIZE) % SudokuData.SIZE;
            boardPanel.repaint();
        }

        private void updateStatus(String text) {
            statusLabel.setText(text);
        }

        private final class BoardPanel extends JPanel {

            private int highlightRow = -1;
            private int highlightCol = -1;
            private SudokuEngine.StepAction stepAction;

            BoardPanel() {
                int defaultGrid = DEFAULT_CELL * SudokuData.SIZE;
                setPreferredSize(new Dimension(defaultGrid, defaultGrid));
                setBackground(PANEL_BG);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (animating) {
                            return;
                        }
                        GridLayout layout = layoutMetrics();
                        int c = (e.getX() - layout.ox) / layout.cell;
                        int r = (e.getY() - layout.oy) / layout.cell;
                        if (r >= 0 && r < SudokuData.SIZE && c >= 0 && c < SudokuData.SIZE) {
                            selRow = r;
                            selCol = c;
                            if (e.getClickCount() >= 2 && !data.isFixed(r, c)) {
                                engine.placeDigit(r, c, SudokuData.EMPTY);
                                boardPanel.repaint();
                            } else {
                                repaint();
                            }
                            requestFocusInWindow();
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                GridLayout layout = layoutMetrics();
                char[][] board = data.getBoard();

                for (int r = 0; r < SudokuData.SIZE; r++) {
                    for (int c = 0; c < SudokuData.SIZE; c++) {
                        int x = layout.cellX(c);
                        int y = layout.cellY(r);
                        g2.setColor(cellBackground(r, c));
                        g2.fillRect(x, y, layout.cell, layout.cell);

                        char ch = board[r][c];
                        if (ch != SudokuData.EMPTY) {
                            boolean conflict = engine.hasConflict(r, c);
                            boolean fixed = data.isFixed(r, c);
                            g2.setColor(conflict ? TEXT_CONFLICT : (fixed ? TEXT_FIXED : TEXT_USER));
                            float fontSize = layout.cell * (fixed ? 0.52f : 0.48f);
                            g2.setFont(getFont().deriveFont(Font.BOLD, fontSize));
                            FontMetrics fm = g2.getFontMetrics();
                            String s = String.valueOf(ch);
                            int tx = x + (layout.cell - fm.stringWidth(s)) / 2;
                            int ty = y + (layout.cell - fm.getHeight()) / 2 + fm.getAscent();
                            g2.drawString(s, tx, ty);
                        }
                    }
                }

                drawGridLines(g2, layout);

                int pad = Math.max(1, layout.cell / 14);
                g2.setColor(new Color(70, 120, 220));
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g2.setStroke(new BasicStroke(Math.max(2f, layout.cell * 0.045f)));
                g2.drawRect(
                        layout.cellX(selCol) + pad,
                        layout.cellY(selRow) + pad,
                        layout.cell - 2 * pad,
                        layout.cell - 2 * pad);

                g2.dispose();
            }

            private void drawGridLines(Graphics2D g2, GridLayout layout) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                g2.setColor(GRID_LINE);
                for (int i = 1; i < SudokuData.SIZE; i++) {
                    if (i % 3 == 0) {
                        continue;
                    }
                    int px = layout.ox + i * layout.cell;
                    int py = layout.oy + i * layout.cell;
                    g2.fillRect(px, layout.oy, layout.thin, layout.grid);
                    g2.fillRect(layout.ox, py, layout.grid, layout.thin);
                }

                g2.setColor(BOX_LINE);
                for (int block = 0; block <= 3; block++) {
                    int vx = layout.ox + block * 3 * layout.cell;
                    int hy = layout.oy + block * 3 * layout.cell;
                    if (block == 0) {
                        g2.fillRect(layout.ox, layout.oy, layout.thick, layout.grid);
                        g2.fillRect(layout.ox, layout.oy, layout.grid, layout.thick);
                    } else if (block == 3) {
                        g2.fillRect(layout.ox + layout.grid - layout.thick, layout.oy, layout.thick, layout.grid);
                        g2.fillRect(layout.ox, layout.oy + layout.grid - layout.thick, layout.grid, layout.thick);
                    } else {
                        g2.fillRect(vx - layout.thick / 2, layout.oy, layout.thick, layout.grid);
                        g2.fillRect(layout.ox, hy - layout.thick / 2, layout.grid, layout.thick);
                    }
                }
            }

            private GridLayout layoutMetrics() {
                int w = getWidth();
                int h = getHeight();
                int avail = Math.min(w, h);
                int cell = avail > 0 ? Math.max(1, avail / SudokuData.SIZE) : DEFAULT_CELL;
                int grid = cell * SudokuData.SIZE;
                int ox = Math.max(0, (w - grid) / 2);
                int oy = Math.max(0, (h - grid) / 2);
                int thick = Math.max(2, cell / 14);
                return new GridLayout(cell, grid, ox, oy, thick);
            }

            private final class GridLayout {
                final int cell;
                final int grid;
                final int ox;
                final int oy;
                final int thick;
                final int thin = 1;

                GridLayout(int cell, int grid, int ox, int oy, int thick) {
                    this.cell = cell;
                    this.grid = grid;
                    this.ox = ox;
                    this.oy = oy;
                    this.thick = thick;
                }

                int cellX(int col) {
                    return ox + col * cell;
                }

                int cellY(int row) {
                    return oy + row * cell;
                }
            }

            private Color cellBackground(int r, int c) {
                if (highlightRow == r && highlightCol == c && stepAction != null) {
                    if (stepAction == SudokuEngine.StepAction.TRY) {
                        return TRY_BG;
                    }
                    if (stepAction == SudokuEngine.StepAction.BACKTRACK) {
                        return BACKTRACK_BG;
                    }
                }
                if (engine.hasConflict(r, c)) {
                    return CONFLICT_BG;
                }
                if (r == selRow && c == selCol) {
                    return SELECT_BG;
                }
                if (r == selRow || c == selCol || sameBox(r, c, selRow, selCol)) {
                    return PEER_BG;
                }
                if (data.isFixed(r, c)) {
                    return FIXED_BG;
                }
                return CELL_BG;
            }

            private boolean sameBox(int r1, int c1, int r2, int c2) {
                return r1 / 3 == r2 / 3 && c1 / 3 == c2 / 3;
            }
        }
    }
}
