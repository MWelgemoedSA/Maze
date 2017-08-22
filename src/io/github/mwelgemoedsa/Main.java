package io.github.mwelgemoedsa;

import com.sun.corba.se.impl.orbutil.graph.Graph;

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

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    private final int ySize;
    private final HashMap<Coordinate, Obstacle> obstaclesMap;
    private final Timer tickTimer;

    private PathfindingAlgorithm algorithm;

    Surface(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.obstaclesMap = new HashMap<>();

        this.fillMaze();

        tickTimer = new Timer(150, this);
        //timer.start();
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
                    }

                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(startX, startY, blockPixels, blockPixels);
                }
            }
        }

        for (GraphNode node: algorithm.getVisitedList()) {
            Coordinate coordinate = node.getCoordinate();
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(coordinate.getX() * blockPixels, coordinate.getY()*blockPixels, blockPixels, blockPixels);
        }

        for (GraphNode node: algorithm.getOpenList()) {
            Coordinate coordinate = node.getCoordinate();

            g2d.setColor(Color.BLUE);
            g2d.fillRect(coordinate.getX() * blockPixels, coordinate.getY()*blockPixels, blockPixels, blockPixels);
        }

        GraphNode currentNode = algorithm.getCurrent();
        while (currentNode != null) {
            Coordinate current = currentNode.getCoordinate();
            g2d.setColor(Color.CYAN);
            g2d.fillRect(current.getX() * blockPixels, current.getY()*blockPixels, blockPixels, blockPixels);

            currentNode = currentNode.getPrevious();
        }

        g2d.setColor(Color.RED);
        Coordinate current = algorithm.getCurrent().getCoordinate();
        g2d.fillRect(current.getX() * blockPixels, current.getY()*blockPixels, blockPixels, blockPixels);

        g2d.setColor(Color.GREEN);
        Coordinate goal = algorithm.getGoal();
        g2d.fillRect(goal.getX() * blockPixels, goal.getY()*blockPixels, blockPixels, blockPixels);
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
        this.step();
    }

    public void setAlgorithm(PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void step() {
        if (this.algorithm == null) return;

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

        for(int i = 0; i != 300; i++) {
            int x = randomGenerator.nextInt(xSize);
            int y = randomGenerator.nextInt(ySize);
            this.addObstacle(x, y, Obstacle.wall);
        }
    }

    private void clear() {
        obstaclesMap.clear();
    }

    public void startTimer() {
        tickTimer.start();
    }
}

public class Main extends JFrame implements ActionListener {
    private Surface mazeSurface;
    private JPanel ctrls;

    private Main() {
        initUI();
    }

    private void initUI() {
        mazeSurface = new Surface(25, 25);
        //mazeSurface.setPreferredSize(new Dimension(500, 500));
        add(mazeSurface);

        mazeSurface.fillMaze();

        setTitle("Maze drawer");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ctrls = new JPanel();
        ctrls.setLayout(new BoxLayout(ctrls, BoxLayout.PAGE_AXIS));
        addButton("Start");
        addButton("Step");

        add(ctrls, BorderLayout.LINE_END);

        boolean first = true;
        ButtonGroup algorithmGroup = new ButtonGroup();
        for (String algorithmStr : getAlgorithms()) {
            JRadioButton algorithmRadioButton = new JRadioButton(algorithmStr);
            algorithmGroup.add(algorithmRadioButton);
            algorithmRadioButton.addActionListener(this);
            ctrls.add(algorithmRadioButton);
            if (first) {
                first = false;
                algorithmRadioButton.setSelected(true);
                setAlgorithm(algorithmStr);
            }
        }
    }

    private void addButton(String text) {
        JButton btnNew = new JButton(text);
        ctrls.add(btnNew);
        btnNew.addActionListener(this);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println(actionEvent);
        if (actionEvent.getActionCommand().equals("Start")) {
            mazeSurface.startTimer();
        }
        if (actionEvent.getActionCommand().equals("Step")) {
            mazeSurface.step();
        }

        if (actionEvent.getSource() instanceof JRadioButton) {
            JRadioButton source = (JRadioButton)actionEvent.getSource();
            setAlgorithm(source.getText());
        }
     }

    public String[] getAlgorithms() {
        return new String[]{"A*", "Greedy Search", "Depth First Search", "Breadth First Search"};
    }

    private void setAlgorithm(String algorithmStr) {
        System.out.println("New algorithm " + algorithmStr);

        Coordinate start = new Coordinate(0, 0);
        Coordinate goal = new Coordinate(mazeSurface.getxSize()-1, mazeSurface.getySize()-1);

        PathfindingAlgorithm algorithm = null;
        if (algorithmStr.equals("Greedy Search")) {
            algorithm = new GreedySearch(mazeSurface, start, goal);
        } else if (algorithmStr.equals("Depth First Search")) {
            algorithm = new DepthFirstSearch(mazeSurface, start, goal);
        } else if (algorithmStr.equals("Breadth First Search")) {
            algorithm = new BreadthFirstSearch(mazeSurface, start, goal);
        } else if (algorithmStr.equals("A*")) {
            algorithm = new AStarSearch(mazeSurface, start, goal);
        }

        mazeSurface.setAlgorithm(algorithm);
    }


}