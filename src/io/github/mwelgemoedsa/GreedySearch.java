package io.github.mwelgemoedsa;

import java.util.Comparator;

class GreedySearch extends PathfindingAlgorithm {
    GreedySearch(Surface surface, Coordinate start, Coordinate goal) {
        super(surface, start, goal);
    }

    @Override
    void addNode(GraphNode node) {
        openList.add(node);
    }

    @Override
    void sortOpenList() {
        openList.sort(Comparator.comparing(GraphNode::getHeuristicAtNode));
    } //Take the node with the best heuristic
}
