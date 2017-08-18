package io.github.mwelgemoedsa;

import com.sun.org.apache.bcel.internal.generic.ObjectType;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Random;
import java.awt.event.ActionListener;

class Surface extends JPanel implements ActionListener {

    private final int xsize;
    private final int ysize;
    private final HashMap<Coordinate, Obstacle> obstaclesMap;

    Surface(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
        obstaclesMap = new HashMap<>();

        Timer timer = new Timer(3000, this);
        timer.start();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        Dimension2D size = this.getSize();

        g.clearRect(0, 0, (int)size.getHeight(), (int)size.getHeight());

        //Calculate the maximum block size to fit all blocks in the panel
        int yBlockPixels = (int)(size.getHeight() / this.ysize);
        int xBlockPixels = (int)(size.getWidth() / this.xsize);

        //blocks must be square
        int blockPixels = xBlockPixels;
        if (xBlockPixels > yBlockPixels) {
            blockPixels = yBlockPixels;
        }

        for (int x = 0; x < xsize; x++) {
            for (int y = 0; y < ysize; y++) {
                Coordinate here = new Coordinate(x, y);

                int startX = x * blockPixels;
                int startY = y * blockPixels;

                Obstacle obstacle = obstaclesMap.get(here);
                if (obstacle != null) {
                    if (obstacle == Obstacle.wall) {
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(startX, startY, blockPixels, blockPixels);
                    } else if (obstacle == Obstacle.start) {
                        g2d.setColor(Color.RED);
                        g2d.fillRect(startX, startY, blockPixels, blockPixels);
                    } else if (obstacle == Obstacle.goal) {
                        g2d.setColor(Color.GREEN);
                        g2d.fillRect(startX, startY, blockPixels, blockPixels);
                    }

                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(startX, startY, blockPixels, blockPixels);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private void addObstacle(int x, int y, Obstacle obstacle) {
        Coordinate coordinate = new Coordinate(x, y);
        System.out.println("New obstacle " + coordinate.toString());
        obstaclesMap.put(coordinate, obstacle);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.fillMaze();
    }

    void fillMaze() {
        clear();

        for (int x = 0; x < xsize; x++) {
            for (int y = 0; y < ysize; y++) {
                addObstacle(x, y, Obstacle.nothing);
            }
        }

        Random randomGenerator = new Random();

        for(int i = 0; i != 10; i++) {
            int x = randomGenerator.nextInt(10);
            int y = randomGenerator.nextInt(10);
            this.addObstacle(x, y, Obstacle.wall);
        }

        this.addObstacle(0, 0, Obstacle.start);
        this.addObstacle(xsize-1, ysize-1, Obstacle.goal);

        this.repaint();
    }

    private void clear() {
        obstaclesMap.clear();
    }
}

public class Main extends JFrame {

    private Main() {
        initUI();
    }

    private void initUI() {
        Surface mazeSurface = new Surface(10, 10);
        add(mazeSurface);

        mazeSurface.fillMaze();

        setTitle("Maze drawer");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}