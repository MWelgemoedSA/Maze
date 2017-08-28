package io.github.mwelgemoedsa;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MazeControls {
    private final Surface mazeSurface;
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

    MazeControls(Surface mazeSurface) {
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
        Coordinate start = new Coordinate(0, 0);
        Coordinate goal = new Coordinate(mazeSurface.getXSize()-1, mazeSurface.getYSize()-1);

        PathfindingAlgorithm algorithm = null;

        if (rdbGreedySearch.isSelected())
                algorithm = new GreedySearch(mazeSurface, start, goal);

        if (rdbDepthFirstSearch.isSelected())
                algorithm = new DepthFirstSearch(mazeSurface, start, goal);

        if (rdbBreadthFirstSearch.isSelected())
                algorithm = new BreadthFirstSearch(mazeSurface, start, goal);

        if (rdbAStar.isSelected())
                algorithm = new AStarSearch(mazeSurface, start, goal);

        mazeSurface.setAlgorithm(algorithm);
    }

    void newMaze() {
        mazeSurface.setXSize((int)spnXSize.getValue());
        mazeSurface.setYSize((int)spnYSize.getValue());
        mazeSurface.setCenterDivisionPoint(chbCenterDivisionPoint.isSelected());
        mazeSurface.setForceSingleSolution(chbForceSingleSolution.isSelected());
        this.setAlgorithm();
        mazeSurface.getAlgorithm().setGoal(new Coordinate(mazeSurface.getXSize()-1, mazeSurface.getYSize()-1));
        mazeSurface.fillMaze();
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
