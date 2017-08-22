package io.github.mwelgemoedsa;

import java.util.Collections;

public class AStarSearch extends PathfindingAlgorithm {
    AStarSearch(Surface surface, Coordinate start, Coordinate goal) {
        super(surface, start, goal);
    }

    @Override
    void addNode(GraphNode node) {
        openList.add(node);
    }

    @Override
    void sortOpenList() {
        Collections.sort(openList, (p1, p2) -> new Double(p1.getHeuristicAtNode() + p1.getTotalCost()).compareTo(p2.getHeuristicAtNode() + p2.getTotalCost()));
    }
}
