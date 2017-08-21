package io.github.mwelgemoedsa;

import java.util.ArrayList;
import java.util.Collections;

abstract class PathfindingAlgorithm {
    private final Coordinate goal;

    private Surface surface;
    protected ArrayList<Coordinate> openList;
    private ArrayList<Coordinate> visitedList;
    private Coordinate current;

    PathfindingAlgorithm(Surface surface, Coordinate start, Coordinate goal) {
        this.goal = goal;

        this.surface = surface;
        this.openList = new ArrayList<>();
        this.visitedList = new ArrayList<>();
        this.openList.add(start);
        this.current = start;
    }

    Coordinate getCurrent() {
        return current;
    }

    void step() {
        if (current.equals(goal)) return; //Nothing more to do
        if (openList.isEmpty()) return; //Also nothing to do, unable to find it

        this.sortOpenList();

        current = openList.remove(0);

        visitedList.add(current);

        for (Coordinate coordinate : surface.getNeighbours(current)) {
            if (!visitedList.contains(coordinate) && !openList.contains(coordinate)) {
                if (coordinate.equals(goal)) { //We found it
                    current = coordinate;
                    return;
                }
                this.addNode(coordinate);
            }
        }
    }

    abstract void addNode(Coordinate coordinate);
    abstract void sortOpenList();

    public ArrayList<Coordinate> getOpenList() {
        return openList;
    }

    public ArrayList<Coordinate> getVisitedList() {
        return visitedList;
    }

    public Coordinate getGoal() {
        return goal;
    }
}
