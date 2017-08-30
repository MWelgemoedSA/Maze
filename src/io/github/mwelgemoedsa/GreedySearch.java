package io.github.mwelgemoedsa;

import java.util.Comparator;
import java.util.ListIterator;

class GreedySearch extends PathfindingAlgorithm {
    GreedySearch(MazeHandler maze, Coordinate start, Coordinate goal) {
        super(maze, start, goal);
    }

    @Override
    void addNode(GraphNode node) {

        ListIterator<GraphNode> itr = openList.listIterator();
        while(itr.hasNext()) {
            GraphNode current = itr.next();
            if (node.getHeuristicAtNode() < current.getHeuristicAtNode()) {
                break;
            }
        }
        itr.add(node);
    }
}
