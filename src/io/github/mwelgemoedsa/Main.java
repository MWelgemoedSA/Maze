package io.github.mwelgemoedsa;

import com.sun.org.apache.bcel.internal.generic.ObjectType;

import java.awt.*;
import javax.swing.JFrame;
import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JPanel;

class Surface extends JPanel {

    private final int xsize;
    private final int ysize;
    private final HashMap<Coordinate, Obstacle> obstaclesMap;

    Surface(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
        obstaclesMap = new HashMap<Coordinate,Obstacle>();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;


        Dimension2D size = this.getSize();

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

                int startx = x * blockPixels;
                int starty = y * blockPixels;
                if (obstaclesMap.containsKey(here)) {
                    g2d.fillRect(startx, starty, blockPixels, blockPixels);
                } else {
                g2d.drawRect(startx, starty, blockPixels-1, blockPixels-1);
            }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    void addObstacle(int x, int y, Obstacle obstacle) {
        Coordinate coordinate = new Coordinate(x, y);
        System.out.println("New obstacle " + coordinate.toString());
        obstaclesMap.put(coordinate, Obstacle.wall);
    }
}

public class Main extends JFrame {

    Main() {
        initUI();
    }

    private void initUI() {
        Surface mazeSurface = new Surface(10, 10);
        add(mazeSurface);

        Random randomGenerator = new Random();

        for(int i = 0; i != 10; i++) {
            int x = randomGenerator.nextInt(10);
            int y = randomGenerator.nextInt(10);
            mazeSurface.addObstacle(x, y, Obstacle.wall);
        }


        setTitle("Maze drawer");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Main ex = new Main();
                ex.setVisible(true);
            }
        });
    }
}