package maze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.*;

public class Maze extends JPanel {

    private static final long serialVersionUID = 1L;

    // size input in pixels


    public static genAndSolve maze;

    public Maze(int width, int height) {
        canvas(width, height);
        init(width, height);

    }

    public void canvas(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setFocusable(true);
    }


    public void init(int width, int height) {
        maze = new genAndSolve(width, height, UserGUI.returnSize());
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0, 400, 400);
        try {
            maze.draw(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
