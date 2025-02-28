import java.util.List;
import java.util.Set;

/**
 * Represents an empty leaf node in the PR Quadtree.
 * Implements the Flyweight pattern.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class EmptyNode implements QuadNode {
    // The single instance of EmptyNode (Flyweight pattern)
    private static final EmptyNode INSTANCE = new EmptyNode();
    
    /**
     * Private constructor to prevent instantiation.
     */
    private EmptyNode() {
        // Nothing to do
    }
    
    /**
     * Gets the single instance of EmptyNode.
     * 
     * @return The EmptyNode instance
     */
    public static EmptyNode getInstance() {
        return INSTANCE;
    }
    
    @Override
    public QuadNode insert(Point point, int x, int y, int size) {
        // When inserting into an empty node, create a leaf node with the point
        return new LeafNode(point);
    }
    
    @Override
    public RemoveResult removeByName(String name, int x, int y, int size) {
        // No points to remove in an empty node
        return new RemoveResult(this, null);
    }
    
    @Override
    public RemoveResult removeByPosition(int xPos, int yPos, int x, int y, int size) {
        // No points to remove in an empty node
        return new RemoveResult(this, null);
    }
    
    @Override
    public int regionSearch(int x1, int y1, int w, int h, int x, int y, int size, 
            List<Point> results) {
        // Empty node, just count as visited
        return 1;
    }
    
    @Override
    public void findDuplicates(Set<Position> duplicates) {
        // No points in an empty node, nothing to do
    }
    
    @Override
    public void dump(int level, int x, int y, int size, StringBuilder sb) {
        // Indent based on level
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append("Node at ").append(x).append(", ").append(y);
        sb.append(", size ").append(size).append(": Empty\n");
    }
}
