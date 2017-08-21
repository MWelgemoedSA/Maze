package io.github.mwelgemoedsa;

import java.util.Collections;

public class GreedySearch extends PathfindingAlgorithm {
    GreedySearch(Surface surface, Coordinate start, Coordinate goal) {
        super(surface, start, goal);
    }

    @Override
    void addNode(Coordinate coordinate) {
        openList.add(coordinate);
    }

    @Override
    void sortOpenList() {
        Coordinate goal = this.getGoal();
        Collections.sort(openList, (p1, p2) -> p1.distTo(goal).compareTo(p2.distTo(goal)));
    } //Take the node with the best heuristic
}
