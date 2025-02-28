import java.util.List;
import java.util.Set;

/**
 * Database class that manages points using both SkipList and QuadTree.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class Database {
    private SkipList skipList;
    private QuadTree quadTree;
    private static final int WORLD_SIZE = 1024;

    /**
     * Creates a new Database.
     */
    public Database() {
        skipList = new SkipList();
        quadTree = new QuadTree(WORLD_SIZE);
    }

    /**
     * Validates if a point is within the world boundaries.
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return true if valid, false otherwise
     */
    private boolean isValidPoint(int x, int y) {
        return x >= 0 && x < WORLD_SIZE && y >= 0 && y < WORLD_SIZE;
    }

    /**
     * Inserts a point into the database.
     * 
     * @param name The name of the point
     * @param x    The x-coordinate
     * @param y    The y-coordinate
     * @return true if inserted, false if coordinates are invalid
     */
    public boolean insert(String name, int x, int y) {
        if (!isValidPoint(x, y)) {
            return false;
        }

        Point point = new Point(name, x, y);
        skipList.insert(point);
        quadTree.insert(point);
        return true;
    }

    /**
     * Removes a point by name.
     * 
     * @param name The name of the point to remove
     * @return The removed point, or null if not found
     */
    public Point removeByName(String name) {
        Point removedPoint = skipList.remove(name);
        if (removedPoint != null) {
            quadTree.removeByName(name);
        }
        return removedPoint;
    }

    /**
     * Removes a point by position.
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The removed point, or null if not found
     */
    public Point removeByPosition(int x, int y) {
        if (!isValidPoint(x, y)) {
            return null;
        }

        Point removedPoint = quadTree.removeByPosition(x, y);
        if (removedPoint != null) {
            skipList.remove(removedPoint.getName());
        }
        return removedPoint;
    }

    /**
     * Searches for points by name.
     * 
     * @param name The name to search for
     * @return A list of points with the given name
     */
    public List<Point> search(String name) {
        return skipList.search(name);
    }

    /**
     * Searches for points within a region.
     * 
     * @param x      The x-coordinate of the region's top-left corner
     * @param y      The y-coordinate of the region's top-left corner
     * @param width  The width of the region
     * @param height The height of the region
     * @return The result of the region search
     */
    public QuadTree.RegionSearchResult regionSearch(int x, int y, int width, int height) {
        return quadTree.regionSearch(x, y, width, height);
    }

    /**
     * Finds positions with duplicate points.
     * 
     * @return A set of positions with duplicates
     */
    public Set<Position> findDuplicates() {
        return quadTree.findDuplicates();
    }

    /**
     * Gets a string representation of the database.
     * 
     * @return A string representing both the SkipList and QuadTree
     */
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append(skipList.dump());
        sb.append(quadTree.dump());
        return sb.toString();
    }
}