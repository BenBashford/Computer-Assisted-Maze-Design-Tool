package maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class GUI extends JPanel {

    private static final long serialVersionUID = 1L;

    // size input in pixels
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private genAndSolve maze;

    public GUI() {
        canvas();
        init();
    }

    private void canvas() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        this.setFocusable(true);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_SPACE) { init(); }
                repaint();
            }
        });
    }

    private void init() {
        maze = new genAndSolve(WIDTH, HEIGHT, 10);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0, WIDTH, HEIGHT);
        maze.draw(g);
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Maze gen v1- backtracer DFS");
        GUI g = new GUI();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(g);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // is set to true by default, shouldn't need to set it manually when repaint();
        // g.setDoubleBuffered(true);
        g.repaint();
    }

}
