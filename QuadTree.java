import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PR Quadtree implementation for points.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class QuadTree {
    private QuadNode root;
    private final int worldSize;
    
    /**
     * Creates a new QuadTree with the given world size.
     * 
     * @param worldSize The size of the world (both width and height)
     */
    public QuadTree(int worldSize) {
        root = EmptyNode.getInstance();
        this.worldSize = worldSize;
    }
    
    /**
     * Inserts a point into the quadtree.
     * 
     * @param point The point to insert
     */
    public void insert(Point point) {
        root = root.insert(point, 0, 0, worldSize);
    }
    
    /**
     * Removes a point by name.
     * 
     * @param name The name of the point to remove
     * @return The removed point, or null if no point was removed
     */
    public Point removeByName(String name) {
        RemoveResult result = root.removeByName(name, 0, 0, worldSize);
        root = result.getNode();
        return result.getRemovedPoint();
    }
    
    /**
     * Removes a point by position.
     * 
     * @param x The x-coordinate of the point to remove
     * @param y The y-coordinate of the point to remove
     * @return The removed point, or null if no point was removed
     */
    public Point removeByPosition(int x, int y) {
        RemoveResult result = root.removeByPosition(x, y, 0, 0, worldSize);
        root = result.getNode();
        return result.getRemovedPoint();
    }
    
    /**
     * Searches for points within a given region.
     * 
     * @param x The x-coordinate of the region's top-left corner
     * @param y The y-coordinate of the region's top-left corner
     * @param width The width of the region
     * @param height The height of the region
     * @return A list of points within the region and the number of nodes visited
     */
    public RegionSearchResult regionSearch(int x, int y, int width, int height) {
        ArrayList<Point> results = new ArrayList<>();
        int nodesVisited = root.regionSearch(x, y, width, height, 0, 0, worldSize, results);
        return new RegionSearchResult(results, nodesVisited);
    }
    
    /**
     * Finds positions with duplicate points.
     * 
     * @return A set of positions with duplicates
     */
    public Set<Position> findDuplicates() {
        Set<Position> duplicates = new HashSet<>();
        root.findDuplicates(duplicates);
        return duplicates;
    }
    
    /**
     * Gets a string representation of the quadtree.
     * 
     * @return A string representing the quadtree
     */
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("QuadTree dump:\n");
        root.dump(0, 0, 0, worldSize, sb);
        return sb.toString();
    }
    
    /**
     * Holds the result of a region search.
     */
    public static class RegionSearchResult {
        private List<Point> points;
        private int nodesVisited;
        
        /**
         * Creates a new RegionSearchResult.
         * 
         * @param points The found points
         * @param nodesVisited The number of nodes visited
         */
        public RegionSearchResult(List<Point> points, int nodesVisited) {
            this.points = points;
            this.nodesVisited = nodesVisited;
        }
        
        /**
         * Gets the found points.
         * 
         * @return The list of points
         */
        public List<Point> getPoints() {
            return points;
        }
        
        /**
         * Gets the number of nodes visited.
         * 
         * @return The node count
         */
        public int getNodesVisited() {
            return nodesVisited;
        }
    }
}
