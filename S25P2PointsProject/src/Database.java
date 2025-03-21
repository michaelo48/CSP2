/**
 * This class is responsible for interfacing between the command processor and
 * the SkipList and QuadTree.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class Database {

    // The SkipList organizes points by name
    private SkipList<String, Point> skipList;

    // The QuadTree organizes points by position
    private QuadTree quadTree;

    // World boundaries
    private final int worldSize = 1024;

    /**
     * The constructor for this class initializes both a SkipList
     * and a QuadTree.
     */
    public Database() {
        skipList = new SkipList<String, Point>();
        quadTree = new QuadTree();
    }


    /**
     * Inserts a point into the database
     * 
     * @param name
     *            The name of the point
     * @param x
     *            The x-coordinate
     * @param y
     *            The y-coordinate
     */
    public void insert(String name, int x, int y) {
        // Validate coordinates
        if (x < 0 || y < 0 || x >= worldSize || y >= worldSize) {
            System.out.println("Point rejected: " + name + " " + x + " " + y);
            return;
        }

        // Create the point
        Point point = new Point(name, x, y);

        // Insert into both data structures
        skipList.insert(new KVPair<>(name, point));
        quadTree.insert(point);

        System.out.println("Point inserted: " + name + " " + x + " " + y);
    }


    /**
     * Removes a point with the specified name
     * 
     * @param name
     *            The name of the point to remove
     */
    public void remove(String name) {
        // Remove from the SkipList
        KVPair<String, Point> removed = skipList.remove(name);

        if (removed == null) {
            System.out.println("Point not removed: " + name);
            return;
        }

        // Also remove from the QuadTree
        Point point = removed.value();
        quadTree.remove(point.getX(), point.getY());

        System.out.println("Point removed: " + name + " " + point.getX() + " "
            + point.getY());
    }


    /**
     * Removes a point at the specified coordinates
     * 
     * @param x
     *            The x-coordinate
     * @param y
     *            The y-coordinate
     */
    public void remove(int x, int y) {
        // Validate coordinates
        if (x < 0 || y < 0) {
            System.out.println("Point rejected: " + x + " " + y);
            return;
        }

        if (x >= worldSize || y >= worldSize) {
            System.out.println("Point rejected: " + x + " " + y);
            return;
        }

        // Remove from the QuadTree
        Point removed = quadTree.remove(x, y);

        if (removed == null) {
            System.out.println("Point not found: " + x + " " + y);
            return;
        }

        // Also remove from the SkipList
        skipList.removeByValue(removed);

        System.out.println("Point removed: " + removed.getName() + " " + x + " "
            + y);
    }


    /**
     * Searches for points within a rectangle
     * 
     * @param x
     *            The x-coordinate of the rectangle
     * @param y
     *            The y-coordinate of the rectangle
     * @param w
     *            The width of the rectangle
     * @param h
     *            The height of the rectangle
     */
    public void regionsearch(int x, int y, int w, int h) {
        // Validate width and height
        if (w <= 0 || h <= 0) {
            System.out.println("Rectangle rejected: " + x + " " + y + " " + w
                + " " + h);
            return;
        }

        // Perform search using QuadTree
        RegionSearchResult result = quadTree.regionsearch(x, y, w, h);

        System.out.println("Points intersecting region " + x + " " + y + " " + w
            + " " + h + ":");
        PointList points = result.getPoints();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            System.out.println("Point found " + p.getName() + " " + p.getX()
                + " " + p.getY());
        }
        System.out.println(result.getNodesVisited()
            + " quadtree nodes visited");
    }


    /**
     * Finds points with duplicate locations
     */
    public void duplicates() {
        DuplicatesResult result = quadTree.findDuplicates();

        System.out.println("Duplicate points:");
        CoordinateList duplicates = result.getDuplicates();
        for (int i = 0; i < duplicates.size(); i++) {
            Coordinate coord = duplicates.get(i);
            System.out.println(coord.getX() + " " + coord.getY());
        }
    }


    /**
     * Searches for points with the specified name
     * 
     * @param name
     *            The name to search for
     */
    public void search(String name) {
        java.util.ArrayList<KVPair<String, Point>> results = skipList.search(
            name);

        if (results.isEmpty()) {
            System.out.println("Point not found: " + name);
            return;
        }

        for (int i = 0; i < results.size(); i++) {
            KVPair<String, Point> pair = results.get(i);
            Point p = pair.value();
            System.out.println("Found " + p.getName() + " " + p.getX() + " " + p
                .getY());
        }
    }


    /**
     * Dumps both the SkipList and QuadTree structures
     */
    public void dump() {
        skipList.dump();
        quadTree.dump();
    }
}
