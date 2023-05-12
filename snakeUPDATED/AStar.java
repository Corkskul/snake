import java.awt.Point;
import java.util.*;

public class AStar {
    private static final int[] DX = {0, 0, -1, 1};
    private static final int[] DY = {-1, 1, 0, 0};

    private int boardWidth;
    private int boardHeight;

    public AStar(int boardWidth, int boardHeight, int gridSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

    }

    public List<Point> findPath(Point start, Point goal, Set<Point> obstacles) {
        Set<Point> closedSet = new HashSet<>();
        Set<Point> openSet = new HashSet<>();
        openSet.add(start);

        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        Map<Point, Double> fScore = new HashMap<>();
        fScore.put(start, heuristicCostEstimate(start, goal));

        while (!openSet.isEmpty()) {
            Point current = getLowestFScore(openSet, fScore);
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Point neighbor : getNeighbors(current, obstacles)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + 1;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + heuristicCostEstimate(neighbor, goal));
            }
        }

        return null;
    }

    private Point getLowestFScore(Set<Point> openSet, Map<Point, Double> fScore) {
        double minFScore = Double.MAX_VALUE;
        Point minPoint = null;

        for (Point point : openSet) {
            double score = fScore.getOrDefault(point, Double.MAX_VALUE);
            if (score < minFScore) {
                minFScore = score;
                minPoint = point;
            }
        }

        return minPoint;
    }

    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }

    private double heuristicCostEstimate(Point start, Point goal) {
        return Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y);
    }

    private Collection<Point> getNeighbors(Point current, Set<Point> obstacles) {
        List<Point> neighbors = new ArrayList<>();

        for (int i = 0; i < DX.length; i++) {
            int newX = current.x + DX[i];
            int newY = current.y + DY[i];

            if (isValid(newX, newY) && !obstacles.contains(new Point(newX, newY))) {
                neighbors.add(new Point(newX, newY));
            }
        }

        return neighbors;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < boardWidth && y >= 0 && y < boardHeight;
    }
}
