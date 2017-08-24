package io.github.mwelgemoedsa;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.geom.Dimension2D;
import java.util.*;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

class Surface extends JPanel implements ActionListener {

    PathfindingAlgorithm getAlgorithm() {
        return algorithm;
    }

    private final int xSize;

    int getXSize() {
        return xSize;
    }

    int getYSize() {
        return ySize;
    }

    private final int ySize;
    private final HashMap<Coordinate, Obstacle> obstaclesMap;
    private final Timer tickTimer;

    private PathfindingAlgorithm algorithm;

    private boolean centerDivisionPoint = true;

    boolean isCenterDivisionPoint() {
        return centerDivisionPoint;
    }

    void setCenterDivisionPoint(boolean centerDivisionPoint) {
        this.centerDivisionPoint = centerDivisionPoint;
    }

    Surface(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.obstaclesMap = new HashMap<>();

        this.fillMaze();

        tickTimer = new Timer(30, this);
        //timer.start();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Dimension2D size = this.getSize();
        g2d.clearRect(0, 0, (int)size.getWidth(), (int)size.getHeight());

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                Coordinate here = new Coordinate(x, y);

                Obstacle obstacle = obstaclesMap.get(here);

                if (obstacle == Obstacle.wall) {
                    drawBlock(g2d, x, y, Color.BLACK);
                } else {
                    drawBlock(g2d, x, y, null);
                }
            }
        }

        Coordinate goal = algorithm.getGoal();
        drawBlock(g2d, goal.getX(), goal.getY(), Color.GREEN);

        for (GraphNode node: algorithm.getVisitedList()) {
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
        int yBlockPixels = (int)(size.getHeight() / this.ySize);
        int xBlockPixels = (int)(size.getWidth() / this.xSize);

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

    private boolean withinMaze(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < xSize &&
                coordinate.getY() >= 0 && coordinate.getY() < ySize;
    }

    AbstractList<Coordinate> getNeighbours(Coordinate coordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        coordinates.add(new Coordinate(coordinate.getX(), coordinate.getY()-1));
        coordinates.add(new Coordinate(coordinate.getX()-1, coordinate.getY()));
        coordinates.add(new Coordinate(coordinate.getX()+1, coordinate.getY()));
        coordinates.add(new Coordinate(coordinate.getX(), coordinate.getY()+1));

        coordinates.removeIf(c -> !withinMaze(c));
        coordinates.removeIf(c -> obstaclesMap.get(c) == Obstacle.wall);

        return coordinates;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private void addObstacle(int x, int y, Obstacle obstacle) {
        Coordinate coordinate = new Coordinate(x, y);
        addObstacle(coordinate, obstacle);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.step();
    }

    void setAlgorithm(PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
        obstaclesMap.remove(algorithm.getGoal());
    }

    void step() {
        if (this.algorithm == null) return;

        this.algorithm.step();
        this.repaint();
    }

    //Uses the recursive division method
    void fillMaze() {
        clear();

        divideChamber(0, xSize-1,0, ySize-1);
        this.repaint();
    }

    private void divideChamber(int startX, int stopX, int startY, int stopY) {
        int minimumWidth = 2;
        if (stopX - startX +1 <= minimumWidth || stopY - startY+1 <= minimumWidth) { //Too small to divide
            return;
        }

        //Make four perpendicular walls that meet perpendicularly at a random point
        int divideX = startX+1;
        int divideY = startY+1;

        if (centerDivisionPoint) {
            divideX = startX + (stopX - startX)/2;
            divideY = startY + (stopY - startY)/2;
        } else {
            if (startX + 1 < stopX - 1) {
                divideX = ThreadLocalRandom.current().nextInt(startX + 1, stopX - 1);
            }
            if (startY + 1 < stopY - 1) {
                divideY = ThreadLocalRandom.current().nextInt(startY + 1, stopY - 1);
            }
        }

        //Must divide on an odd coordinate to make the maze look nicer
        if (divideX % 2 == 0) {
            divideX--;
        }

        if (divideY %2 == 0) {
            divideY--;
        }
        Coordinate divisionPoint = new Coordinate(divideX, divideY);

        for (int x = startX; x <= stopX; x++) {
            for (int y = startY; y <= stopY; y++) {
                if (x == divisionPoint.getX() || y == divisionPoint.getY()) {
                    addObstacle(x, y, Obstacle.wall);
                }
            }
        }

        //Calculate a random break in each wall
        ArrayList<Coordinate> breakPoints = new ArrayList<>();
        breakPoints.add(new Coordinate(
                ThreadLocalRandom.current().nextInt(startX, divisionPoint.getX()),
                divisionPoint.getY())
        );

        breakPoints.add(new Coordinate(
                divisionPoint.getX(),
                ThreadLocalRandom.current().nextInt(startY, divisionPoint.getY()))
        );

        breakPoints.add(new Coordinate(
                ThreadLocalRandom.current().nextInt(divisionPoint.getX()+1, stopX+1),
                divisionPoint.getY())
        );

        breakPoints.add(new Coordinate(
                divisionPoint.getX(),
                ThreadLocalRandom.current().nextInt(divisionPoint.getY()+1, stopY+1))
        );

        //Take a random three
        Collections.shuffle(breakPoints);
        for (int i = 0; i < 3; i++) {
            Coordinate breakPoint = breakPoints.get(i);

            int breakX = breakPoint.getX();
            if (breakX % 2 != 0 && breakX != divisionPoint.getX()) {
                breakX--;
            }
            int breakY = breakPoint.getY();
            if (breakY % 2 != 0 && breakY != divisionPoint.getY()) {
                breakY--;
            }
            addObstacle(breakX, breakY, Obstacle.nothing);
        }

        //Continue the process with the four new chambers created
        divideChamber(startX, divisionPoint.getX()-1, startY, divisionPoint.getY()-1);
        divideChamber(divisionPoint.getX()+1, stopX, startY, divisionPoint.getY()-1);
        divideChamber(startX, divisionPoint.getX()-1, divisionPoint.getY()+1, stopY);
        divideChamber(divisionPoint.getX()+1, stopX, divisionPoint.getY()+1, stopY);
    }

    private void addObstacle(Coordinate coordinate, Obstacle obstacle) {
        //System.out.println("New obstacle " + coordinate.toString() + " " + obstacle);
        obstaclesMap.put(coordinate, obstacle);
    }

    private void clear() {
        obstaclesMap.clear();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                addObstacle(x, y, Obstacle.nothing);
            }
        }
    }

    void startTimer() {
        tickTimer.start();
    }
}

class Main extends JFrame implements ActionListener {
    private Surface mazeSurface;
    private JPanel controlsPanel;
    private JCheckBox chbCenterBreakPoint;

    private Main() {
        initUI();
    }

    private void initUI() {
        mazeSurface = new Surface(51, 51);
        //mazeSurface.setPreferredSize(new Dimension(500, 500));
        add(mazeSurface);

        mazeSurface.fillMaze();

        setTitle("Maze drawer");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.PAGE_AXIS));
        addButton("Start");
        addButton("Step");
        addButton("New Maze");

        boolean first = true;
        ButtonGroup algorithmGroup = new ButtonGroup();
        for (String algorithmStr : getAlgorithms()) {
            JRadioButton algorithmRadioButton = new JRadioButton(algorithmStr);
            algorithmGroup.add(algorithmRadioButton);
            algorithmRadioButton.addActionListener(this);
            controlsPanel.add(algorithmRadioButton);
            if (first) {
                first = false;
                algorithmRadioButton.setSelected(true);
                setAlgorithm(algorithmStr);
            }
        }

        chbCenterBreakPoint = new JCheckBox("Center chamber division point");
        chbCenterBreakPoint.setSelected(mazeSurface.isCenterDivisionPoint());
        chbCenterBreakPoint.addActionListener(this);
        controlsPanel.add(chbCenterBreakPoint);

        add(controlsPanel, BorderLayout.LINE_END);
    }

    private void addButton(String text) {
        JButton btnNew = new JButton(text);
        controlsPanel.add(btnNew);
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

        if (actionEvent.getActionCommand().equals("New Maze")) {
            mazeSurface.fillMaze();
            mazeSurface.getAlgorithm().reset();
        }

        if (actionEvent.getSource() == chbCenterBreakPoint) {
            mazeSurface.setCenterDivisionPoint(chbCenterBreakPoint.isSelected());
        }

        if (actionEvent.getSource() instanceof JRadioButton) {
            JRadioButton source = (JRadioButton)actionEvent.getSource();
            setAlgorithm(source.getText());
        }
     }

    private String[] getAlgorithms() {
        return new String[]{"A*", "Greedy Search", "Depth First Search", "Breadth First Search"};
    }

    private void setAlgorithm(String algorithmStr) {
        System.out.println("New algorithm " + algorithmStr);

        Coordinate start = new Coordinate(0, 0);
        Coordinate goal = new Coordinate(mazeSurface.getXSize()-1, mazeSurface.getYSize()-1);

        PathfindingAlgorithm algorithm = null;
        switch (algorithmStr) {
            case "Greedy Search":
                algorithm = new GreedySearch(mazeSurface, start, goal);
                break;
            case "Depth First Search":
                algorithm = new DepthFirstSearch(mazeSurface, start, goal);
                break;
            case "Breadth First Search":
                algorithm = new BreadthFirstSearch(mazeSurface, start, goal);
                break;
            case "A*":
                algorithm = new AStarSearch(mazeSurface, start, goal);
                break;
        }

        mazeSurface.setAlgorithm(algorithm);
    }


}