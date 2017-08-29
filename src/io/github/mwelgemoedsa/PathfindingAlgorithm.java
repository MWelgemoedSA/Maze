package io.github.mwelgemoedsa;

import java.util.ArrayList;

abstract class PathfindingAlgorithm {
    private Coordinate goal;

    private final MazeHandler maze;
    final ArrayList<GraphNode> openList;
    private final ArrayList<GraphNode> visitedList;
    private GraphNode current;

    PathfindingAlgorithm(MazeHandler maze, Coordinate start, Coordinate goal) {
        this.goal = goal;

        this.maze = maze;
        this.openList = new ArrayList<>();
        this.visitedList = new ArrayList<>();
        this.current = new GraphNode(start, 0, heuristicValue(start), null);
        this.openList.add(this.current);
    }

    private double heuristicValue(Coordinate start) {
        return start.distTo(goal);
    }

    GraphNode getCurrent() {
        return current;
    }

    void step() {
        if (isFinished()) return;

        this.sortOpenList();

        current = openList.remove(0);

        if (current.getCoordinate().equals(goal)) { //We found it
            return;
        }

        visitedList.add(current);

        for (Coordinate coordinate : maze.getNeighbours(current.getCoordinate())) {
            double stepSize = coordinate.distTo(current.getCoordinate());
            GraphNode node = new GraphNode(coordinate, current.getTotalCost() + stepSize, heuristicValue(coordinate), current);

            if (!visitedList.contains(node) && !openList.contains(node)) {
                this.addNode(node);
            }
        }
    }

    abstract void addNode(GraphNode node);
    abstract void sortOpenList();

    ArrayList<GraphNode> getOpenList() {
        return openList;
    }

    ArrayList<GraphNode> getVisitedList() {
        return visitedList;
    }

    Coordinate getGoal() {
        return goal;
    }

    void setGoal(Coordinate goal) {
        this.goal = goal;
    }

    boolean isFinished() {
        return current.getCoordinate().equals(goal) || openList.isEmpty();
    }
}
