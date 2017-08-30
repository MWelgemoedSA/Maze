package io.github.mwelgemoedsa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;

class MazeDrawingSurface extends JPanel implements ActionListener {
    private final Timer tickTimer;
    private PathfindingAlgorithm algorithm;
    private final MazeHandler mazeHandler;

    MazeDrawingSurface(MazeHandler mazeHandler) {
        this.mazeHandler = mazeHandler;
        tickTimer = new Timer(30, actionEvent -> step());
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension2D size = this.getSize();

        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (int x = 0; x < mazeHandler.getXSize(); x++) {
            for (int y = 0; y < mazeHandler.getYSize(); y++) {
                Coordinate here = new Coordinate(x, y);

                Obstacle obstacle = mazeHandler.getObstacle(here);

                if (obstacle == Obstacle.wall) {
                    drawBlock(g2d, x, y, Color.BLACK);
                } else {
                    drawBlock(g2d, x, y, null);
                }
            }
        }

        Coordinate goal = algorithm.getGoal();
        drawBlock(g2d, goal.getX(), goal.getY(), Color.GREEN);

        for (GraphNode node: algorithm.getVisitedSet()) {
            Coordinate coordinate = node.getCoordinate();
            drawBlock(g2d, coordinate.getX(), coordinate.getY(), Color.BLUE);
        }

        for (GraphNode node: algorithm.getOpenList()) {
            Coordinate coordinate = node.getCoordinate();
            drawBlock(g2d, coordinate.getX(), coordinate.getY(), Color.CYAN);
        }

        GraphNode currentNode = algorithm.getCurrent();
        while (currentNode != null) {
            Coordinate coordinate = currentNode.getCoordinate();
            drawBlock(g2d, coordinate.getX(), coordinate.getY(), Color.YELLOW);

            currentNode = currentNode.getPrevious();
        }

        Coordinate coordinate = algorithm.getCurrent().getCoordinate();
        drawBlock(g2d, coordinate.getX(), coordinate.getY(), Color.RED);
    }

    //If fillColour is null just draw the outlines
    private void drawBlock(Graphics2D g2d, int x, int y, Color fillColour) {
        Dimension2D size = this.getSize();

        //Calculate the maximum block size to fit all blocks in the panel
        int xBlockPixels = (int)(size.getWidth() / mazeHandler.getXSize());
        int yBlockPixels = (int)(size.getHeight() / mazeHandler.getYSize());

        //blocks must be square
        int blockPixels = xBlockPixels;
        if (xBlockPixels > yBlockPixels) {
            blockPixels = yBlockPixels;
        }

        //Calculate square start point
        int startX = x * blockPixels;
        int startY = y * blockPixels;

        g2d.setColor(Color.BLACK);
        g2d.drawRect(startX, startY, blockPixels, blockPixels);

        if (fillColour != null) {
            g2d.setColor(fillColour);
            g2d.fillRect(startX+1, startY+1, blockPixels-1, blockPixels-1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.step();
    }

    PathfindingAlgorithm getAlgorithm() {
        return algorithm;
    }

    void setAlgorithm(PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
        mazeHandler.clearBlock(algorithm.getGoal());
    }

    void step() {
        if (algorithm == null) return;

        algorithm.step();
        repaint();

        if (algorithm.isFinished()) {
            tickTimer.stop();
        }
    }

    void startTimer() {
        tickTimer.start();
    }

    MazeHandler getMazeHandler() {
        return mazeHandler;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(mazeHandler.getXSize() * 10, mazeHandler.getYSize() * 10);
    }
}
