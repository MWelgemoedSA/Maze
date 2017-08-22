package io.github.mwelgemoedsa;

import java.util.Collections;

public class BreadthFirstSearch extends PathfindingAlgorithm {
    BreadthFirstSearch(Surface surface, Coordinate start, Coordinate goal) {
        super(surface, start, goal);
    }

    //Adding a node at the end of the open list makes it breadth first search
    void addNode(GraphNode node) {
        this.openList.add(node);
    }

    //No heuristic
    void sortOpenList() {
        //Pass
    }
}
