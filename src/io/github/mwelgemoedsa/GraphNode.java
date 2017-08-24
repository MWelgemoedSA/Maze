package io.github.mwelgemoedsa;

public class GraphNode {
    private final Coordinate coordinate;
    private final Double totalCost;
    private final Double heuristicAtNode;
    private final GraphNode previous;

    GraphNode(Coordinate coordinate, double totalCost, double heuristicAtNode, GraphNode previous) {
        this.coordinate = coordinate;
        this.totalCost = totalCost;
        this.heuristicAtNode = heuristicAtNode;
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphNode graphNode = (GraphNode) o;

        return getCoordinate().equals(graphNode.getCoordinate());
    }

    @Override
    public int hashCode() {
        return getCoordinate().hashCode();
    }

    Coordinate getCoordinate() {
        return coordinate;
    }

    Double getTotalCost() {
        return totalCost;
    }

    Double getHeuristicAtNode() {
        return heuristicAtNode;
    }

    GraphNode getPrevious() {
        return previous;
    }
}
