package io.github.mwelgemoedsa;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Dimension2D;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.event.ActionListener;

class Surface extends JPanel implements ActionListener {

    private final int xSize;
    private final int ySize;
    private final HashMap<Coordinate, Obstacle> obstaclesMap;
    private final Algorithm algorithm;
    private final Coordinate goal;
    private final Coordinate start;

    Surface(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.obstaclesMap = new HashMap<>();

        this.start = new Coordinate(0, 0);
        this.goal = new Coordinate(xSize-1, ySize-1);

        this.algorithm = new Algorithm(this, start, goal);

        this.fillMaze();

        Timer timer = new Timer(500, this);
        timer.start();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        Dimension2D size = this.getSize();

        g.clearRect(0, 0, (int)size.getHeight(), (int)size.getHeight());

        //Calculate the maximum block size to fit all blocks in the panel
        int yBlockPixels = (int)(size.getHeight() / this.ySize);
        int xBlockPixels = (int)(size.getWidth() / this.xSize);

        //blocks must be square
        int blockPixels = xBlockPixels;
        if (xBlockPixels > yBlockPixels) {
            blockPixels = yBlockPixels;
        }

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
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

        for (Coordinate coordinate : algorithm.getVisitedList()) {
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(coordinate.getX() * blockPixels, coordinate.getY()*blockPixels, blockPixels, blockPixels);
        }

        for (Coordinate coordinate : algorithm.getOpenList()) {
            g2d.setColor(Color.BLUE);
            g2d.fillRect(coordinate.getX() * blockPixels, coordinate.getY()*blockPixels, blockPixels, blockPixels);
        }

        g2d.setColor(Color.CYAN);
        Coordinate current = algorithm.getCurrent();
        g2d.fillRect(current.getX() * blockPixels, current.getY()*blockPixels, blockPixels, blockPixels);
    }

    AbstractList<Coordinate> getNeighbours(Coordinate coordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (j != 0 || i != 0) {
                    int newX = coordinate.getX() + i;
                    int newY = coordinate.getY() + j;

                    if (newX >= 0 && newX < this.xSize && newY >= 0 && newY < this.ySize) {
                        Coordinate newCoordinate = new Coordinate(newX, newY);
                        if (obstaclesMap.get(newCoordinate) != Obstacle.wall)
                            coordinates.add(newCoordinate);
                    }
                }
            }
        }

        return coordinates;
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
        this.algorithm.step();
        this.repaint();
    }

    void fillMaze() {
        clear();

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                addObstacle(x, y, Obstacle.nothing);
            }
        }

        Random randomGenerator = new Random();

        for(int i = 0; i != 10; i++) {
            int x = randomGenerator.nextInt(xSize);
            int y = randomGenerator.nextInt(ySize);
            this.addObstacle(x, y, Obstacle.wall);
        }

        this.addObstacle(start.getX(), start.getY(), Obstacle.start);
        this.addObstacle(goal.getX(), goal.getY(), Obstacle.goal);
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
        Surface mazeSurface = new Surface(5, 5);
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