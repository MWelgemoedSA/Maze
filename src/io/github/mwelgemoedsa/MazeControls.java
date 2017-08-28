package io.github.mwelgemoedsa;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MazeControls {
    private final MazeDrawingSurface mazeSurface;
    private JPanel pnlOptions;
    private JPanel pnlAlgorithms;
    private JPanel pnlSize;
    private JCheckBox chbCenterDivisionPoint;
    private JSpinner spnXSize;
    private JSpinner spnYSize;
    private JRadioButton rdbAStar;
    private JRadioButton rdbGreedySearch;
    private JRadioButton rdbDepthFirstSearch;
    private JRadioButton rdbBreadthFirstSearch;
    private JButton btnStart;
    private JButton btnStep;
    private JButton btnNewMaze;
    private JCheckBox chbForceSingleSolution;

    MazeControls(MazeDrawingSurface mazeSurface) {
        this.mazeSurface = mazeSurface;
        btnNewMaze.addActionListener(actionEvent -> newMaze());

        btnStart.addActionListener(actionEvent -> mazeSurface.startTimer());

        btnStep.addActionListener(actionEvent -> mazeSurface.step());

        ActionListener listener = actionEvent -> setAlgorithm();

        rdbAStar.addActionListener(listener);
        rdbGreedySearch.addActionListener(listener);
        rdbDepthFirstSearch.addActionListener(listener);
        rdbBreadthFirstSearch.addActionListener(listener);
    }

    private void setAlgorithm() {
        MazeHandler mazeHandler = mazeSurface.getMazeHandler();

        Coordinate start = new Coordinate(0, 0);
        Coordinate goal = new Coordinate(mazeHandler.getXSize()-1, mazeHandler.getYSize()-1);

        PathfindingAlgorithm algorithm = null;

        if (rdbGreedySearch.isSelected())
                algorithm = new GreedySearch(mazeHandler, start, goal);

        if (rdbDepthFirstSearch.isSelected())
                algorithm = new DepthFirstSearch(mazeHandler, start, goal);

        if (rdbBreadthFirstSearch.isSelected())
                algorithm = new BreadthFirstSearch(mazeHandler, start, goal);

        if (rdbAStar.isSelected())
                algorithm = new AStarSearch(mazeHandler, start, goal);

        mazeSurface.setAlgorithm(algorithm);
    }

    void newMaze() {
        MazeHandler mazeHandler = mazeSurface.getMazeHandler();

        mazeHandler.setXSize((int)spnXSize.getValue());
        mazeHandler.setYSize((int)spnYSize.getValue());
        mazeHandler.setCenterDivisionPoint(chbCenterDivisionPoint.isSelected());
        mazeHandler.setForceSingleSolution(chbForceSingleSolution.isSelected());
        this.setAlgorithm();
        mazeSurface.getAlgorithm().setGoal(new Coordinate(mazeHandler.getXSize()-1, mazeHandler.getYSize()-1));
        mazeHandler.fillMaze();
        mazeSurface.repaint();
    }

    JPanel getPnlOptions() {
        return pnlOptions;
    }

    private void createUIComponents() {
        int min = 5;
        int max = 1001;
        int step = 2; //Maze must be uneven
        int current = 31; //Good default

        spnXSize = new JSpinner(new SpinnerNumberModel(current, min, max, step));
        spnYSize = new JSpinner(new SpinnerNumberModel(current, min, max, step));
    }
}
