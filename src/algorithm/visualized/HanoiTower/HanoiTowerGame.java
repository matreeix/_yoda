package algorithm.visualized.HanoiTower;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayDeque;
import java.util.List;

/**
 * 可视化汉诺塔：手动移盘、递归自动求解、逐步演示。
 */
public class HanoiTowerGame {

    private static final Color BG = new Color(28, 32, 40);
    private static final Color PANEL_BG = new Color(38, 44, 56);
    private static final Color BASE = new Color(92, 78, 62);
    private static final Color PEG = new Color(180, 150, 110);
    private static final Color PEG_HIGHLIGHT = new Color(120, 200, 255);
    private static final Color TEXT = new Color(230, 234, 242);
    private static final Color TEXT_DIM = new Color(150, 158, 172);

    private static final Color[] DISK_COLORS = {
            new Color(255, 107, 107),
            new Color(255, 159, 67),
            new Color(255, 211, 42),
            new Color(46, 213, 115),
            new Color(30, 144, 255),
            new Color(95, 39, 205),
            new Color(255, 94, 171),
            new Color(72, 219, 251),
            new Color(162, 155, 254),
            new Color(255, 121, 121),
            new Color(129, 236, 236),
            new Color(253, 203, 110),
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HanoiTowerGame::launch);
    }

    public static void launch() {
        JFrame frame = new JFrame("汉诺塔");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new GameRoot());
        Dimension size = new Dimension(960, 640);
        frame.setMinimumSize(size);
        frame.setSize(size);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static final class GameRoot extends JPanel {

        private final BoardPanel board = new BoardPanel();
        private final JLabel statusLabel = new JLabel(" ");
        private final JLabel statsLabel = new JLabel(" ");
        private final JSpinner diskSpinner = new JSpinner(new SpinnerNumberModel(5, 3, 10, 1));
        private final JSlider speedSlider = new JSlider(20, 800, 280);

        private HanoiTowerEngine engine = new HanoiTowerEngine(5);
        private int selectedPeg = -1;
        private boolean animating;
        private Timer animTimer;
        private ArrayDeque<HanoiTowerEngine.Move> pendingMoves;

        GameRoot() {
            setLayout(new BorderLayout(0, 10));
            setBackground(BG);
            setBorder(new EmptyBorder(14, 14, 14, 14));

            add(buildTopBar(), BorderLayout.NORTH);
            add(board, BorderLayout.CENTER);
            add(buildBottomBar(), BorderLayout.SOUTH);

            bindKeys();
            refreshStats();
            updateStatus("点击柱子选中圆盘，再点目标柱移动 · 目标：全部移到右柱");
        }

        private JPanel buildTopBar() {
            JPanel bar = new JPanel(new BorderLayout());
            bar.setOpaque(false);

            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            left.setOpaque(false);

            JLabel title = new JLabel("汉诺塔");
            title.setForeground(TEXT);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            left.add(title);

            left.add(label("圆盘数:"));
            diskSpinner.setEnabled(!animating);
            diskSpinner.addChangeListener(e -> {
                if (!animating) {
                    int n = (Integer) diskSpinner.getValue();
                    engine.reset(n);
                    selectedPeg = -1;
                    board.repaint();
                    refreshStats();
                    updateStatus("已重置为 " + n + " 层，最少 " + engine.minMoves() + " 步");
                }
            });
            left.add(diskSpinner);

            bar.add(left, BorderLayout.WEST);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
            buttons.setOpaque(false);
            buttons.add(actionButton("重置", e -> doReset()));
            buttons.add(actionButton("撤销", e -> doUndo()));
            buttons.add(actionButton("自动求解", e -> doAutoSolve()));
            buttons.add(actionButton("单步演示", e -> doStepDemo()));
            bar.add(buttons, BorderLayout.EAST);

            return bar;
        }

        private JPanel buildBottomBar() {
            JPanel bar = new JPanel(new BorderLayout(12, 0));
            bar.setOpaque(false);

            statusLabel.setForeground(TEXT);
            statsLabel.setForeground(TEXT_DIM);
            bar.add(statusLabel, BorderLayout.CENTER);
            bar.add(statsLabel, BorderLayout.EAST);

            JPanel speedRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            speedRow.setOpaque(false);
            JLabel speedLbl = label("演示速度:");
            speedLbl.setForeground(TEXT_DIM);
            speedRow.add(speedLbl);
            speedSlider.setPreferredSize(new Dimension(180, 28));
            speedSlider.setInverted(true);
            speedSlider.setMajorTickSpacing(200);
            speedSlider.setPaintTicks(true);
            speedRow.add(speedSlider);

            JPanel south = new JPanel(new BorderLayout());
            south.setOpaque(false);
            south.add(speedRow, BorderLayout.WEST);
            JLabel hint = new JLabel("快捷键 1/2/3 选柱 · R 重置 · Z 撤销");
            hint.setForeground(TEXT_DIM);
            hint.setFont(hint.getFont().deriveFont(11f));
            south.add(hint, BorderLayout.EAST);
            bar.add(south, BorderLayout.SOUTH);
            return bar;
        }

        private JLabel label(String text) {
            JLabel lbl = new JLabel(text);
            lbl.setForeground(TEXT_DIM);
            return lbl;
        }

        private JButton actionButton(String text, java.awt.event.ActionListener action) {
            JButton btn = new JButton(text);
            btn.addActionListener(action);
            return btn;
        }

        private void bindKeys() {
            InputMap im = board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = board.getActionMap();

            bind(im, am, KeyEvent.VK_1, e -> selectPeg(0));
            bind(im, am, KeyEvent.VK_2, e -> selectPeg(1));
            bind(im, am, KeyEvent.VK_3, e -> selectPeg(2));
            bind(im, am, KeyEvent.VK_R, e -> doReset());
            bind(im, am, KeyEvent.VK_Z, e -> doUndo());
        }

        private void bind(InputMap im, ActionMap am, int key, java.awt.event.ActionListener action) {
            String id = "key_" + key;
            im.put(KeyStroke.getKeyStroke(key, 0), id);
            am.put(id, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    action.actionPerformed(e);
                }
            });
        }

        private void doReset() {
            stopAnimation();
            int n = (Integer) diskSpinner.getValue();
            engine.reset(n);
            selectedPeg = -1;
            undoStack.clear();
            board.repaint();
            refreshStats();
            updateStatus("已重置");
        }

        private final ArrayDeque<HanoiTowerEngine.Move> undoStack = new ArrayDeque<>();

        private void doUndo() {
            if (animating || undoStack.isEmpty()) {
                return;
            }
            HanoiTowerEngine.Move last = undoStack.removeLast();
            if (engine.undo(last)) {
                selectedPeg = -1;
                pendingMoves = null;
                board.repaint();
                refreshStats();
                updateStatus("已撤销上一步");
            }
        }

        private void selectPeg(int peg) {
            if (animating) {
                return;
            }
            if (selectedPeg < 0) {
                if (engine.topDisk(peg) == 0) {
                    updateStatus("该柱为空");
                    return;
                }
                selectedPeg = peg;
                board.repaint();
                updateStatus("已选中 " + pegName(peg) + "，请选择目标柱");
                return;
            }
            if (selectedPeg == peg) {
                selectedPeg = -1;
                board.repaint();
                updateStatus("已取消选择");
                return;
            }
            tryMove(selectedPeg, peg);
        }

        private void tryMove(int from, int to) {
            int disk = engine.topDisk(from);
            if (engine.move(from, to)) {
                undoStack.addLast(new HanoiTowerEngine.Move(from, to, disk));
                selectedPeg = -1;
                board.repaint();
                refreshStats();
                if (engine.isSolved()) {
                    updateStatus("完成！共 " + engine.getMoveCount() + " 步（最优 " + engine.minMoves() + " 步）");
                    JOptionPane.showMessageDialog(this, "恭喜，汉诺塔完成！", "完成", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    updateStatus("移动 " + disk + " 号盘：" + pegName(from) + " → " + pegName(to));
                }
            } else {
                selectedPeg = -1;
                board.repaint();
                updateStatus("非法移动：大盘不能压小盘");
            }
        }

        private void doAutoSolve() {
            if (animating) {
                stopAnimation();
                updateStatus("演示已停止");
                return;
            }
            if (engine.isSolved()) {
                updateStatus("已经完成");
                return;
            }
            doReset();
            List<HanoiTowerEngine.Move> moves = engine.solve();
            startAnimation(moves, "自动求解演示中…（再次点击可停止）");
        }

        private void doStepDemo() {
            if (animating) {
                return;
            }
            if (pendingMoves != null && !pendingMoves.isEmpty()) {
                stepOne();
                return;
            }
            if (engine.isSolved()) {
                updateStatus("已经完成");
                return;
            }
            doReset();
            pendingMoves = new ArrayDeque<>(engine.solve());
            updateStatus("单步演示：点击「单步演示」逐步执行，共 " + pendingMoves.size() + " 步");
        }

        private void stepOne() {
            if (pendingMoves == null || pendingMoves.isEmpty()) {
                updateStatus("演示结束");
                pendingMoves = null;
                return;
            }
            HanoiTowerEngine.Move move = pendingMoves.removeFirst();
            engine.applyMove(move);
            undoStack.addLast(move);
            board.highlightFrom = move.from;
            board.highlightTo = move.to;
            board.repaint();
            refreshStats();
            if (engine.isSolved()) {
                pendingMoves = null;
                updateStatus("演示完成！共 " + engine.getMoveCount() + " 步");
                JOptionPane.showMessageDialog(this, "演示完成！", "完成", JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateStatus("第 " + engine.getMoveCount() + " 步：" + pegName(move.from) + " → " + pegName(move.to)
                        + "，剩余 " + pendingMoves.size() + " 步");
            }
        }

        private void startAnimation(List<HanoiTowerEngine.Move> moves, String runningMsg) {
            pendingMoves = null;
            undoStack.clear();
            animating = true;
            diskSpinner.setEnabled(false);
            ArrayDeque<HanoiTowerEngine.Move> queue = new ArrayDeque<>(moves);
            updateStatus(runningMsg);

            animTimer = new Timer(speedSlider.getValue(), null);
            animTimer.addActionListener(e -> {
                animTimer.setDelay(speedSlider.getValue());
                if (queue.isEmpty()) {
                    stopAnimation();
                    board.highlightFrom = -1;
                    board.highlightTo = -1;
                    board.repaint();
                    refreshStats();
                    updateStatus("求解完成！共 " + engine.getMoveCount() + " 步（理论最少 " + engine.minMoves() + " 步）");
                    if (engine.isSolved()) {
                        JOptionPane.showMessageDialog(GameRoot.this, "自动求解完成！", "完成", JOptionPane.INFORMATION_MESSAGE);
                    }
                    return;
                }
                HanoiTowerEngine.Move move = queue.removeFirst();
                engine.applyMove(move);
                board.highlightFrom = move.from;
                board.highlightTo = move.to;
                board.repaint();
                refreshStats();
            });
            animTimer.start();
        }

        private void stopAnimation() {
            animating = false;
            if (animTimer != null) {
                animTimer.stop();
                animTimer = null;
            }
            diskSpinner.setEnabled(true);
            board.highlightFrom = -1;
            board.highlightTo = -1;
        }

        private void refreshStats() {
            statsLabel.setText(String.format("步数 %d / 最少 %d", engine.getMoveCount(), engine.minMoves()));
        }

        private void updateStatus(String text) {
            statusLabel.setText(text);
        }

        private static String pegName(int peg) {
            switch (peg) {
                case HanoiTowerEngine.SOURCE:
                    return "左柱";
                case HanoiTowerEngine.AUX:
                    return "中柱";
                case HanoiTowerEngine.TARGET:
                    return "右柱";
                default:
                    return "柱" + (peg + 1);
            }
        }

        private final class BoardPanel extends JPanel {

            private int highlightFrom = -1;
            private int highlightTo = -1;

            BoardPanel() {
                setBackground(PANEL_BG);
                setPreferredSize(new Dimension(900, 460));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        int peg = pegAt(e.getX());
                        if (peg >= 0) {
                            selectPeg(peg);
                            requestFocusInWindow();
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Layout layout = layoutMetrics();
                drawBase(g2, layout);
                drawPegs(g2, layout);
                drawDisks(g2, layout);
                drawLabels(g2, layout);

                g2.dispose();
            }

            private void drawBase(Graphics2D g2, Layout layout) {
                g2.setColor(BASE);
                g2.fillRoundRect(layout.baseX, layout.baseY, layout.baseW, layout.baseH, 12, 12);
                g2.setColor(BASE.darker());
                g2.drawRoundRect(layout.baseX, layout.baseY, layout.baseW, layout.baseH, 12, 12);
            }

            private void drawPegs(Graphics2D g2, Layout layout) {
                for (int p = 0; p < HanoiTowerEngine.PEG_COUNT; p++) {
                    int cx = layout.pegCenterX(p);
                    boolean hot = p == selectedPeg || p == highlightFrom || p == highlightTo;
                    g2.setColor(hot ? PEG_HIGHLIGHT : PEG);
                    g2.setStroke(new BasicStroke(hot ? 10f : 8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx, layout.pegTop, cx, layout.baseY);
                }
            }

            private void drawDisks(Graphics2D g2, Layout layout) {
                int diskCount = engine.getDiskCount();
                for (int p = 0; p < HanoiTowerEngine.PEG_COUNT; p++) {
                    int height = engine.stackHeight(p);
                    for (int i = 0; i < height; i++) {
                        int disk = engine.diskAt(p, i);
                        int diskW = layout.diskWidth(disk, diskCount);
                        int diskH = layout.diskH;
                        int cx = layout.pegCenterX(p);
                        int x = cx - diskW / 2;
                        int y = layout.baseY - (i + 1) * (layout.diskH + layout.gap);
                        Color color = DISK_COLORS[(disk - 1) % DISK_COLORS.length];
                        g2.setColor(color);
                        g2.fill(new RoundRectangle2D.Float(x, y, diskW, diskH, 10, 10));
                        g2.setColor(color.darker());
                        g2.draw(new RoundRectangle2D.Float(x, y, diskW, diskH, 10, 10));
                        g2.setColor(Color.WHITE);
                        g2.setFont(getFont().deriveFont(Font.BOLD, layout.diskH * 0.55f));
                        String s = String.valueOf(disk);
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(s, cx - fm.stringWidth(s) / 2, y + (diskH + fm.getAscent()) / 2 - 2);
                    }
                }
            }

            private void drawLabels(Graphics2D g2, Layout layout) {
                String[] names = {"左柱 (起点)", "中柱 (辅助)", "右柱 (目标)"};
                g2.setColor(TEXT_DIM);
                g2.setFont(getFont().deriveFont(Font.PLAIN, 13f));
                FontMetrics fm = g2.getFontMetrics();
                for (int p = 0; p < HanoiTowerEngine.PEG_COUNT; p++) {
                    String name = names[p];
                    int cx = layout.pegCenterX(p);
                    g2.drawString(name, cx - fm.stringWidth(name) / 2, layout.baseY + layout.baseH + 22);
                }
            }

            private int pegAt(int mouseX) {
                Layout layout = layoutMetrics();
                int best = -1;
                int bestDist = Integer.MAX_VALUE;
                for (int p = 0; p < HanoiTowerEngine.PEG_COUNT; p++) {
                    int dist = Math.abs(mouseX - layout.pegCenterX(p));
                    if (dist < layout.pegHitRadius && dist < bestDist) {
                        bestDist = dist;
                        best = p;
                    }
                }
                return best;
            }

            private Layout layoutMetrics() {
                int w = getWidth();
                int h = getHeight();
                int pad = 40;
                int baseH = Math.max(18, h / 18);
                int baseY = h - pad - 36;
                int baseX = pad;
                int baseW = w - 2 * pad;
                int pegTop = pad + 20;
                int diskH = Math.max(14, (baseY - pegTop) / (engine.getDiskCount() + 2));
                int gap = Math.max(2, diskH / 8);
                int minDiskW = baseW / (engine.getDiskCount() + 6);
                int maxDiskW = baseW / 3 - 20;
                return new Layout(baseX, baseY, baseW, baseH, pegTop, diskH, gap, minDiskW, maxDiskW);
            }

            private final class Layout {
                final int baseX, baseY, baseW, baseH, pegTop, diskH, gap, minDiskW, maxDiskW;
                final int pegHitRadius = 80;

                Layout(int baseX, int baseY, int baseW, int baseH, int pegTop,
                       int diskH, int gap, int minDiskW, int maxDiskW) {
                    this.baseX = baseX;
                    this.baseY = baseY;
                    this.baseW = baseW;
                    this.baseH = baseH;
                    this.pegTop = pegTop;
                    this.diskH = diskH;
                    this.gap = gap;
                    this.minDiskW = minDiskW;
                    this.maxDiskW = maxDiskW;
                }

                int pegCenterX(int peg) {
                    double section = baseW / 3.0;
                    return (int) (baseX + section * peg + section / 2);
                }

                int diskWidth(int disk, int diskCount) {
                    return minDiskW + (maxDiskW - minDiskW) * disk / diskCount;
                }
            }
        }
    }
}
