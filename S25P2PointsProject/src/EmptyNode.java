/**
 * Represents an empty leaf node in the Quadtree (flyweight pattern)
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class EmptyNode implements QuadNode {

    private static EmptyNode instance;

    /**
     * Private constructor for the flyweight pattern
     */
    private EmptyNode() {
        // No initialization needed
    }


    /**
     * Gets the singleton instance of EmptyNode
     * 
     * @return The singleton EmptyNode instance
     */
    public static EmptyNode getInstance() {
        if (instance == null) {
            instance = new EmptyNode();
        }
        return instance;
    }


    @Override
    public QuadNode insert(Point point, int x, int y, int size) {
        // Convert to a leaf node containing the point
        LeafNode leaf = new LeafNode();
        return leaf.insert(point, x, y, size);
    }


    @Override
    public KVPair<QuadNode, Point> remove(
        int x,
        int y,
        int regionX,
        int regionY,
        int size) {
        // Nothing to remove from an empty node
        return new KVPair<>(this, null);
    }


    @Override
    public KVPair<QuadNode, Point> remove(
        String name,
        int regionX,
        int regionY,
        int size) {
        // Nothing to remove from an empty node
        return new KVPair<>(this, null);
    }


    @Override
    public int regionsearch(
        int x,
        int y,
        int w,
        int h,
        int regionX,
        int regionY,
        int size,
        PointList results) {
        // No points to find in an empty node
        return 1; // Count this node as visited
    }


    @Override
    public int findDuplicates(
        int regionX,
        int regionY,
        int size,
        CoordinateList duplicates) {
        // No duplicates in an empty node
        return 1; // Count this node as visited
    }


    @Override
    public int dump(int regionX, int regionY, int size, int indent) {
        // Print indentation
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        System.out.println("Node at " + regionX + " " + regionY + " " + size
            + " Empty");
        return 1; // Count this node as printed
    }


    @Override
    public boolean isEmpty() {
        return true;
    }


    @Override
    public int compareTo(QuadNode other) {
        // Empty nodes come before non-empty nodes
        if (other.isEmpty()) {
            return 0;
        }
        return -1;
    }
}
