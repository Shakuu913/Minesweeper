import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Minesweeper implements ActionListener {
    public static void main(String[] args) {
        new Minesweeper();
    }

    JPanel panel;
    JButton[][] buttons;
    int[][] matrix;
    boolean[][] opened;
    final int n = 20;
    final int buttonSize = 35;
    final int totalMines = 40;
    boolean first = true;
    int minesCounter = 0;

    Minesweeper() {
        JFrame frame = new JFrame();
        frame.setTitle("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(n * (buttonSize + 1), n * (buttonSize + 2));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        panel = new JPanel(null);
        frame.add(panel);

        matrix = new int[n][n];
        opened = new boolean[n][n];
        buttons = new JButton[n][n];
        newGame();

        frame.setVisible(true);
    }
    private void newGame() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 0;
            }
        }

        minesCounter = 0;
        while (minesCounter < totalMines) {
            putMine();
        }

        UpdateButtons();
    }
    private void putMine() {
        int x = (int) (Math.random() * n);
        int y = (int) (Math.random() * n);

        if (matrix[x][y] != -1) {
            matrix[x][y] = -1;
            minesCounter++;

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (x + i >= 0 && x + i < n && y + j >= 0 && y + j < n && matrix[x + i][y + j] != -1) {
                        matrix[x + i][y + j]++;
                    }
                }
            }
        } else {
            putMine();
        }
    }
    private void UpdateButtons() {
        panel.removeAll();
        ButtonMouseListener mouseListener = new ButtonMouseListener();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setSize(buttonSize, buttonSize);
                buttons[i][j].setBounds(i * buttonSize, j * buttonSize, buttonSize, buttonSize);

                if ((i + j) % 2 == 1)
                    buttons[i][j].setBackground(new Color(170, 215, 81, 255));
                else
                    buttons[i][j].setBackground(new Color(162, 209, 73, 255));

                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 12));
                buttons[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                buttons[i][j].setVerticalAlignment(SwingConstants.CENTER);
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].putClientProperty("position", new Point(i, j));
                buttons[i][j].addActionListener(this);
                buttons[i][j].addMouseListener(mouseListener);

                panel.add(buttons[i][j]);
            }
        }
        panel.revalidate();
        panel.repaint();
    }
    private void move(int x, int y) {
        if (first) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (x + i >= 0 && x + i < n && y + j >= 0 && y + j < n && matrix[x + i][y + j] == -1) {
                        matrix[x + i][y + j] = 0;
                        putMine();
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j] != -1) {
                        int total = 0;
                        for (int k = -1; k <= 1; k++) {
                            for (int l = -1; l <= 1; l++) {
                                if (i + k >= 0 && i + k < n && j + l >= 0 && j + l < n && matrix[i + k][j + l] == -1) {
                                    total++;
                                }
                            }
                        }
                        matrix[i][j] = total;
                    }
                }
            }

            first = false;
        }

        if (x < 0 || x >= n || y < 0 || y >= n || opened[x][y]) {
            return;
        }

        opened[x][y] = true;
        JButton btn = buttons[x][y];

        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);

        if (matrix[x][y] == -1) {
            openAll();
        } else {
            btn.setText(matrix[x][y] == 0 ? "" : Integer.toString(matrix[x][y]));

            if (matrix[x][y] == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            move(x + i, y + j);
                        }
                    }
                }
            }

            if (matrix[x][y] != -1) {
                if ((x + y) % 2 == 1) {
                    btn.setBackground(new Color(215, 184, 153, 255));
                } else {
                    btn.setBackground(new Color(229, 194, 159, 255));
                }

                switch (matrix[x][y]) {
                    case 1:
                        btn.setForeground(new Color(25, 118, 209));
                        break;  
                    case 2:
                        btn.setForeground(new Color(56, 142, 60));
                        break;
                    case 3:
                        btn.setForeground(new Color(211, 51, 50));
                        break;
                    case 4:
                        btn.setForeground(new Color(123, 32, 162));
                        break;
                    case 5:
                        btn.setForeground(new Color(255, 153, 0));
                        break;
                    case 6:
                        btn.setForeground(new Color(0, 255, 255));
                        break;
                    case 7:
                        btn.setForeground(new Color(255, 0, 255));
                        break;
                    case 8:
                        btn.setForeground(new Color(128, 128, 128));
                        break;
                    default:
                        btn.setForeground(Color.BLACK);
                        break;
                }
            }

            btn.setFocusable(false);
        }
    }
    private void openAll(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == -1) {
                    buttons[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                    buttons[i][j].setText("M");
                    buttons[i][j].setBackground(Color.RED);
                    buttons[i][j].setForeground(Color.WHITE);
                    buttons[i][j].setFocusable(false);
                }
            }
        }
    }
    private void putFlag(int x, int y) {
        JButton btn = buttons[x][y];
        if (!opened[x][y]) {
            if (btn.getIcon() == null) {
                ImageIcon Icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/flag.png")));
                Image img = Icon.getImage();

                Image rImg = img.getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
                Icon = new ImageIcon(rImg);

                btn.setIcon(Icon);
            } else {
                btn.setIcon(null);
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        Point p = (Point) btn.getClientProperty("position");

        move(p.x, p.y);
    }
    private class ButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                JButton btn = (JButton) e.getSource();
                Point p = (Point) btn.getClientProperty("position");
                putFlag(p.x, p.y);
            }
        }
    }
}

