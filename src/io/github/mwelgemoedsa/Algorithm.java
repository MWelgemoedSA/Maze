package io.github.mwelgemoedsa;

import java.util.ArrayList;
import java.util.Collections;

class Algorithm {
    private final Coordinate goal;
    private Surface surface;
    private ArrayList<Coordinate> openList;
    private ArrayList<Coordinate> visitedList;
    private Coordinate current;

    Algorithm(Surface surface, Coordinate start, Coordinate goal) {
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

        //DFS
        Collections.sort(openList, (p1, p2) -> p1.distTo(goal).compareTo(p2.distTo(goal)));

        current = openList.remove(0);

        visitedList.add(current);

        for (Coordinate coordinate : surface.getNeighbours(current)) {
            if (!visitedList.contains(coordinate) && !openList.contains(coordinate)) {
                if (coordinate.equals(goal)) { //We found it
                    current = coordinate;
                    return;
                }
                openList.add(coordinate);
            }
        }
    }

    public ArrayList<Coordinate> getOpenList() {
        return openList;
    }

    public ArrayList<Coordinate> getVisitedList() {
        return visitedList;
    }
}
