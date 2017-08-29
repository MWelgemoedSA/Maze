package io.github.mwelgemoedsa;

import java.awt.*;
import javax.swing.*;

class Main extends JFrame {
    private Main() {
        initUI();
    }

    private void initUI() {
        MazeHandler mazeHandler = new MazeHandler();
        MazeDrawingSurface mazeSurface = new MazeDrawingSurface(mazeHandler);
        MazeControls mazeControls = new MazeControls(mazeSurface);
        mazeControls.newMaze();
        add(mazeSurface);

        setTitle("Maze drawer");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(mazeControls.getPnlOptions(), BorderLayout.LINE_END);
        this.pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}