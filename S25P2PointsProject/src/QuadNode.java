/**
 * Interface for QuadTree nodes that defines methods implemented by
 * all node types (internal, leaf, empty)
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public interface QuadNode extends Comparable<QuadNode> {

    /**
     * Inserts a point into the quadtree
     * 
     * @param point
     *            The point to insert
     * @param x
     *            The x-coordinate of the current region
     * @param y
     *            The y-coordinate of the current region
     * @param size
     *            The size of the current region
     * @return The node after insertion
     */
    public QuadNode insert(Point point, int x, int y, int size);


    /**
     * Removes a point at the specified coordinates
     * 
     * @param x
     *            The x-coordinate
     * @param y
     *            The y-coordinate
     * @param regionX
     *            The x-coordinate of the current region
     * @param regionY
     *            The y-coordinate of the current region
     * @param size
     *            The size of the current region
     * @return The node after removal and a removed point if successful
     */
    public KVPair<QuadNode, Point> remove(
        int x,
        int y,
        int regionX,
        int regionY,
        int size);


    /**
     * Removes a point with the specified name
     * 
     * @param name
     *            The name of the point to remove
     * @param regionX
     *            The x-coordinate of the current region
     * @param regionY
     *            The y-coordinate of the current region
     * @param size
     *            The size of the current region
     * @return The node after removal and a removed point if successful
     */
    public KVPair<QuadNode, Point> remove(
        String name,
        int regionX,
        int regionY,
        int size);


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
     * @param regionX
     *            The x-coordinate of the current region
     * @param regionY
     *            The y-coordinate of the current region
     * @param size
     *            The size of the current region
     * @param results
     *            List to store found points
     * @return Number of nodes visited during the search
     */
    public int regionsearch(
        int x,
        int y,
        int w,
        int h,
        int regionX,
        int regionY,
        int size,
        PointList results);


    /**
     * Finds duplicate points in the quadtree
     * 
     * @param regionX
     *            The x-coordinate of the current region
     * @param regionY
     *            The y-coordinate of the current region
     * @param size
     *            The size of the current region
     * @param duplicates
     *            List to store coordinates with duplicate points
     * @return Number of nodes visited
     */
    public int findDuplicates(
        int regionX,
        int regionY,
        int size,
        CoordinateList duplicates);


    /**
     * Prints the node contents for dumping the quadtree with coordinates
     * 
     * @param regionX
     *            The x-coordinate of the current region
     * @param regionY
     *            The y-coordinate of the current region
     * @param size
     *            The size of the current region
     * @param indent
     *            Current indentation level
     * @return Number of nodes printed
     */
    public int dump(int regionX, int regionY, int size, int indent);


    /**
     * Checks if this node is an empty leaf
     * 
     * @return True if this is an empty leaf, false otherwise
     */
    public boolean isEmpty();
}