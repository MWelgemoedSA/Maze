package io.github.mwelgemoedsa;

public class GraphNode {
    private Coordinate coordinate;
    private Double totalCost;
    private Double heuristicAtNode;
    private GraphNode previous;

    public GraphNode(Coordinate coordinate, double totalCost, double heuristicAtNode, GraphNode previous) {
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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Double getHeuristicAtNode() {
        return heuristicAtNode;
    }

    public GraphNode getPrevious() {
        return previous;
    }
}
