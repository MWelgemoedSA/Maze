package io.github.mwelgemoedsa;

import java.util.*;

abstract class PathfindingAlgorithm {
    private Coordinate goal;

    private final MazeHandler maze;
    final LinkedList<GraphNode> openList;
    private final HashSet<GraphNode> visitedSet;
    private GraphNode current;

    PathfindingAlgorithm(MazeHandler maze, Coordinate start, Coordinate goal) {
        this.goal = goal;

        this.maze = maze;
        this.openList = new LinkedList<>();
        this.visitedSet = new HashSet<>();
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

        current = openList.remove();

        if (current.getCoordinate().equals(goal)) { //We found it
            return;
        }

        visitedSet.add(current);

        for (Coordinate coordinate : maze.getNeighbours(current.getCoordinate())) {
            double stepSize = coordinate.distTo(current.getCoordinate());
            GraphNode node = new GraphNode(coordinate, current.getTotalCost() + stepSize, heuristicValue(coordinate), current);

            if (!visitedSet.contains(node) && !openList.contains(node)) {
                this.addNode(node);
            }
        }
    }

    abstract void addNode(GraphNode node);

    List<GraphNode> getOpenList() {
        return openList;
    }

    Set<GraphNode> getVisitedSet() {
        return visitedSet;
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
