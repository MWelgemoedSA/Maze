package io.github.mwelgemoedsa;

class BreadthFirstSearch extends PathfindingAlgorithm {
    BreadthFirstSearch(MazeHandler maze, Coordinate start, Coordinate goal) {
        super(maze, start, goal);
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
