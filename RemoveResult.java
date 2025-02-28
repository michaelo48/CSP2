/**
 * Represents the result of a removal operation.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class RemoveResult {
    private QuadNode node;
    private Point removedPoint;
    
    /**
     * Creates a new RemoveResult.
     * 
     * @param node The updated node after removal
     * @param removedPoint The removed point (null if no point was removed)
     */
    public RemoveResult(QuadNode node, Point removedPoint) {
        this.node = node;
        this.removedPoint = removedPoint;
    }
    
    /**
     * Gets the updated node.
     * 
     * @return The updated node
     */
    public QuadNode getNode() {
        return node;
    }
    
    /**
     * Gets the removed point.
     * 
     * @return The removed point (null if no point was removed)
     */
    public Point getRemovedPoint() {
        return removedPoint;
    }
}
