package io.github.mwelgemoedsa;

public class DepthFirstSearch extends PathfindingAlgorithm {
    DepthFirstSearch(Surface surface, Coordinate start, Coordinate goal) {
        super(surface, start, goal);
    }

    @Override
    void addNode(Coordinate coordinate) {
        openList.add(0, coordinate);
    } //Adding node at the start of the open list makes it a depth first search

    @Override
    //No heuristic
    void sortOpenList() {
        //Pass
    }
}
