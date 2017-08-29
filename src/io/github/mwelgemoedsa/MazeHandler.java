package io.github.mwelgemoedsa;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

class MazeHandler {
    private int xSize;
    private int ySize;
    private boolean centerDivisionPoint = true;
    private boolean forceSingleSolution = true;

    private final HashMap<Coordinate, Obstacle> obstaclesMap;

    MazeHandler() {
        xSize = 0;
        ySize = 0;
        this.obstaclesMap = new HashMap<>();
    }

    private boolean withinMaze(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < xSize &&
                coordinate.getY() >= 0 && coordinate.getY() < ySize;
    }

    AbstractList<Coordinate> getNeighbours(Coordinate coordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        coordinates.add(new Coordinate(coordinate.getX(), coordinate.getY()-1));
        coordinates.add(new Coordinate(coordinate.getX()-1, coordinate.getY()));
        coordinates.add(new Coordinate(coordinate.getX()+1, coordinate.getY()));
        coordinates.add(new Coordinate(coordinate.getX(), coordinate.getY()+1));

        coordinates.removeIf(c -> !withinMaze(c));
        coordinates.removeIf(c -> obstaclesMap.get(c) == Obstacle.wall);

        return coordinates;
    }

    private void addObstacle(int x, int y, Obstacle obstacle) {
        Coordinate coordinate = new Coordinate(x, y);
        addObstacle(coordinate, obstacle);
    }

    //Uses the recursive division method
    void fillMaze() {
        clear();

        divideChamber(0, xSize-1,0, ySize-1);
    }

    private void divideChamber(int startX, int stopX, int startY, int stopY) {
        int minimumWidth = 2;
        if (stopX - startX +1 <= minimumWidth || stopY - startY+1 <= minimumWidth) { //Too small to divide
            return;
        }

        //Make four perpendicular walls that meet perpendicularly at a random point
        int divideX = startX+1;
        int divideY = startY+1;

        if (centerDivisionPoint) {
            divideX = startX + (stopX - startX)/2;
            divideY = startY + (stopY - startY)/2;
        } else {
            if (startX + 1 < stopX - 1) {
                divideX = ThreadLocalRandom.current().nextInt(startX + 1, stopX - 1);
            }
            if (startY + 1 < stopY - 1) {
                divideY = ThreadLocalRandom.current().nextInt(startY + 1, stopY - 1);
            }
        }

        //Must divide on an odd coordinate to make the maze look nicer
        if (divideX % 2 == 0) {
            divideX--;
        }

        if (divideY %2 == 0) {
            divideY--;
        }
        Coordinate divisionPoint = new Coordinate(divideX, divideY);

        for (int x = startX; x <= stopX; x++) {
            for (int y = startY; y <= stopY; y++) {
                if (x == divisionPoint.getX() || y == divisionPoint.getY()) {
                    addObstacle(x, y, Obstacle.wall);
                }
            }
        }

        //Calculate a random break in each wall
        ArrayList<Coordinate> breakPoints = new ArrayList<>();
        breakPoints.add(new Coordinate(
                ThreadLocalRandom.current().nextInt(startX, divisionPoint.getX()),
                divisionPoint.getY())
        );

        breakPoints.add(new Coordinate(
                divisionPoint.getX(),
                ThreadLocalRandom.current().nextInt(startY, divisionPoint.getY()))
        );

        breakPoints.add(new Coordinate(
                ThreadLocalRandom.current().nextInt(divisionPoint.getX()+1, stopX+1),
                divisionPoint.getY())
        );

        breakPoints.add(new Coordinate(
                divisionPoint.getX(),
                ThreadLocalRandom.current().nextInt(divisionPoint.getY()+1, stopY+1))
        );

        //Take a random three
        Collections.shuffle(breakPoints);
        int breakCount = 4;

        if (isForceSingleSolution())
            breakCount = 3;

        for (int i = 0; i < breakCount; i++) {
            Coordinate breakPoint = breakPoints.get(i);

            int breakX = breakPoint.getX();
            if (breakX % 2 != 0 && breakX != divisionPoint.getX()) {
                breakX--;
            }
            int breakY = breakPoint.getY();
            if (breakY % 2 != 0 && breakY != divisionPoint.getY()) {
                breakY--;
            }
            addObstacle(breakX, breakY, Obstacle.nothing);
        }

        //Continue the process with the four new chambers created
        divideChamber(startX, divisionPoint.getX()-1, startY, divisionPoint.getY()-1);
        divideChamber(divisionPoint.getX()+1, stopX, startY, divisionPoint.getY()-1);
        divideChamber(startX, divisionPoint.getX()-1, divisionPoint.getY()+1, stopY);
        divideChamber(divisionPoint.getX()+1, stopX, divisionPoint.getY()+1, stopY);
    }

    private void addObstacle(Coordinate coordinate, Obstacle obstacle) {
        //System.out.println("New obstacle " + coordinate.toString() + " " + obstacle);
        obstaclesMap.put(coordinate, obstacle);
    }

    private void clear() {
        obstaclesMap.clear();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                addObstacle(x, y, Obstacle.nothing);
            }
        }
    }

    int getXSize() {
        return xSize;
    }

    int getYSize() {
        return ySize;
    }

    void setXSize(int xSize) {
        this.xSize = xSize;
    }

    void setYSize(int ySize) {
        this.ySize = ySize;
    }

    private boolean isForceSingleSolution() {
        return forceSingleSolution;
    }

    void setForceSingleSolution(boolean forceSingleSolution) {
        this.forceSingleSolution = forceSingleSolution;
    }

    void setCenterDivisionPoint(boolean centerDivisionPoint) {
        this.centerDivisionPoint = centerDivisionPoint;
    }

    Obstacle getObstacle(Coordinate here) {
        return obstaclesMap.get(here);
    }

    //Remove obstacles at certain coordinates, to ensure the goal itself isn't on top of a wall
    void clearBlock(Coordinate goal) {
        obstaclesMap.remove(goal);
    }
}
