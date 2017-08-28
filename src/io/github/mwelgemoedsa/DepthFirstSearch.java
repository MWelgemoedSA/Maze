package io.github.mwelgemoedsa;

class DepthFirstSearch extends PathfindingAlgorithm {
    DepthFirstSearch(MazeHandler maze, Coordinate start, Coordinate goal) {
        super(maze, start, goal);
    }

    @Override
    void addNode(GraphNode node) {
        openList.add(0, node);
    } //Adding node at the start of the open list makes it a depth first search

    @Override
    //No heuristic
    void sortOpenList() {
        //Pass
    }
}
