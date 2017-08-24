package io.github.mwelgemoedsa;

import java.util.ArrayList;

abstract class PathfindingAlgorithm {
    private final Coordinate goal;
    private final Coordinate start;

    private final Surface surface;
    ArrayList<GraphNode> openList;
    private ArrayList<GraphNode> visitedList;
    private GraphNode current;

    PathfindingAlgorithm(Surface surface, Coordinate start, Coordinate goal) {
        this.goal = goal;
        this.start = start;

        this.surface = surface;
        this.openList = new ArrayList<>();
        this.visitedList = new ArrayList<>();
        this.current = new GraphNode(start, 0, heuristicValue(start), null);
        this.openList.add(this.current);
    }

    void reset() {
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
        if (current.getCoordinate().equals(goal)) return; //Nothing more to do

        if (openList.isEmpty()) return; //Also nothing to do, unable to find it

        this.sortOpenList();

        current = openList.remove(0);

        if (current.getCoordinate().equals(goal)) { //We found it
            return;
        }

        visitedList.add(current);

        for (Coordinate coordinate : surface.getNeighbours(current.getCoordinate())) {
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
}
