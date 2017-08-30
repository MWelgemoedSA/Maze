package io.github.mwelgemoedsa;

import java.util.ListIterator;

class AStarSearch extends PathfindingAlgorithm {
    AStarSearch(MazeHandler maze, Coordinate start, Coordinate goal) {
        super(maze, start, goal);
    }

    @Override
    void addNode(GraphNode node) {
        ListIterator<GraphNode> itr = openList.listIterator();
        while(itr.hasNext()) {
            GraphNode current = itr.next();
            double myAStar = node.getHeuristicAtNode() + node.getTotalCost();
            double nodeAStar = current.getHeuristicAtNode() + current.getTotalCost();
            if (myAStar < nodeAStar ||
                    (myAStar == nodeAStar && node.getTotalCost() > current.getTotalCost())) {
                break;
            }
        }
        itr.add(node);
    }
}
