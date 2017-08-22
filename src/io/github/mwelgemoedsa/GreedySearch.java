package io.github.mwelgemoedsa;

import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.util.Collections;

public class GreedySearch extends PathfindingAlgorithm {
    GreedySearch(Surface surface, Coordinate start, Coordinate goal) {
        super(surface, start, goal);
    }

    @Override
    void addNode(GraphNode node) {
        openList.add(node);
    }

    @Override
    void sortOpenList() {
        Collections.sort(openList, (p1, p2) -> p1.getHeuristicAtNode().compareTo(p2.getHeuristicAtNode()));
    } //Take the node with the best heuristic
}
