package io.github.mwelgemoedsa;

import java.util.Comparator;

class AStarSearch extends PathfindingAlgorithm {
    AStarSearch(Surface surface, Coordinate start, Coordinate goal) {
        super(surface, start, goal);
    }

    @Override
    void addNode(GraphNode node) {
        openList.add(node);
    }

    @Override
    void sortOpenList() {
        openList.sort(Comparator.comparingDouble(p -> p.getHeuristicAtNode() + p.getTotalCost()));
    }
}
