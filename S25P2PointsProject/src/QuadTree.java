/**
 * Implements a PR Quadtree for storing points by location
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class QuadTree {

    private QuadNode root;
    private final int worldSize = 1024;

    /**
     * Creates a new empty Quadtree
     */
    public QuadTree() {
        root = EmptyNode.getInstance();
    }


    /**
     * Inserts a point into the tree
     * 
     * @param point
     *            The point to insert
     * @return True if the point was inserted, false otherwise
     */
    public boolean insert(Point point) {
        // Validate point coordinates
        if (point.getX() < 0 || point.getY() < 0 || point.getX() >= worldSize
            || point.getY() >= worldSize) {
            return false;
        }

        root = root.insert(point, 0, 0, worldSize);
        return true;
    }


    /**
     * Removes a point at the specified coordinates
     * 
     * @param x
     *            The x-coordinate
     * @param y
     *            The y-coordinate
     * @return The removed point, or null if not found
     */
    public Point remove(int x, int y) {
        // Validate coordinates
        if (x < 0 || y < 0 || x >= worldSize || y >= worldSize) {
            return null;
        }

        KVPair<QuadNode, Point> result = root.remove(x, y, 0, 0, worldSize);
        root = result.key();
        return result.value();
    }


    /**
     * Removes a point with the specified name
     * 
     * @param name
     *            The name of the point to remove
     * @return The removed point, or null if not found
     */
    public Point remove(String name) {
        KVPair<QuadNode, Point> result = root.remove(name, 0, 0, worldSize);
        root = result.key();
        return result.value();
    }


    /**
     * Searches for points within a query rectangle
     * 
     * @param x
     *            The x-coordinate of the query rectangle
     * @param y
     *            The y-coordinate of the query rectangle
     * @param w
     *            The width of the query rectangle
     * @param h
     *            The height of the query rectangle
     * @return A result containing the list of found points and the number of
     *         nodes visited
     */
    public RegionSearchResult regionsearch(int x, int y, int w, int h) {
        PointList results = new PointList();
        int nodesVisited = root.regionsearch(x, y, w, h, 0, 0, worldSize,
            results);
        return new RegionSearchResult(results, nodesVisited);
    }


    /**
     * Finds locations with duplicate points
     * 
     * @return A result containing the list of duplicate locations and the
     *         number of nodes visited
     */
    public DuplicatesResult findDuplicates() {
        CoordinateList duplicates = new CoordinateList();
        int nodesVisited = root.findDuplicates(0, 0, worldSize, duplicates);
        return new DuplicatesResult(duplicates, nodesVisited);
    }


    /**
     * Dumps the Quadtree structure with modified format
     */
    public void dump() {
        System.out.println("QuadTree dump:");
        int nodeCount = root.dump(0, 0, worldSize, 0);
        System.out.println(nodeCount + " quadtree nodes printed");
    }
}