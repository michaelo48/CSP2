import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents an internal node in the PR Quadtree.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class InternalNode implements QuadNode {
    // Children nodes: NW, NE, SW, SE
    private QuadNode nw;
    private QuadNode ne;
    private QuadNode sw;
    private QuadNode se;
    
    /**
     * Creates a new InternalNode with empty children.
     */
    public InternalNode() {
        nw = EmptyNode.getInstance();
        ne = EmptyNode.getInstance();
        sw = EmptyNode.getInstance();
        se = EmptyNode.getInstance();
    }
    
    /**
     * Determines which quadrant a point belongs to.
     * 
     * @param point The point
     * @param x The x-coordinate of the current quadrant
     * @param y The y-coordinate of the current quadrant
     * @param size The size of the current quadrant
     * @return 0 for NW, 1 for NE, 2 for SW, 3 for SE
     */
    private int getQuadrant(Point point, int x, int y, int size) {
        int halfSize = size / 2;
        int midX = x + halfSize;
        int midY = y + halfSize;
        
        if (point.getX() < midX) {
            if (point.getY() < midY) {
                return 0; // NW
            } else {
                return 2; // SW
            }
        } else {
            if (point.getY() < midY) {
                return 1; // NE
            } else {
                return 3; // SE
            }
        }
    }
    
    /**
     * Gets the coordinates and size of a child quadrant.
     * 
     * @param quadrant The quadrant (0 for NW, 1 for NE, 2 for SW, 3 for SE)
     * @param x The x-coordinate of the current quadrant
     * @param y The y-coordinate of the current quadrant
     * @param size The size of the current quadrant
     * @return An array [childX, childY, childSize]
     */
    private int[] getChildParameters(int quadrant, int x, int y, int size) {
        int halfSize = size / 2;
        int childX = x;
        int childY = y;
        
        switch (quadrant) {
            case 0: // NW
                break;
            case 1: // NE
                childX = x + halfSize;
                break;
            case 2: // SW
                childY = y + halfSize;
                break;
            case 3: // SE
                childX = x + halfSize;
                childY = y + halfSize;
                break;
        }
        
        return new int[] {childX, childY, halfSize};
    }
    
    /**
     * Checks if all children are leaf nodes with a total of 3 or fewer points.
     * 
     * @return true if the node should be merged, false otherwise
     */
    private boolean shouldMerge() {
        // Check if any child is an internal node
        if (nw instanceof InternalNode || ne instanceof InternalNode || 
            sw instanceof InternalNode || se instanceof InternalNode) {
            return false;
        }
        
        // Count total points in all children
        int totalPoints = 0;
        
        if (nw instanceof LeafNode) {
            totalPoints += ((LeafNode) nw).getPoints().size();
        }
        if (ne instanceof LeafNode) {
            totalPoints += ((LeafNode) ne).getPoints().size();
        }
        if (sw instanceof LeafNode) {
            totalPoints += ((LeafNode) sw).getPoints().size();
        }
        if (se instanceof LeafNode) {
            totalPoints += ((LeafNode) se).getPoints().size();
        }
        
        // If total points <= 3, we should merge
        return totalPoints <= 3;
    }
    
    /**
     * Merges all children into a single leaf node.
     * 
     * @return The merged leaf node
     */
    private LeafNode mergeChildren() {
        ArrayList<Point> mergedPoints = new ArrayList<>();
        
        // Collect points from all children
        if (nw instanceof LeafNode) {
            mergedPoints.addAll(((LeafNode) nw).getPoints());
        }
        if (ne instanceof LeafNode) {
            mergedPoints.addAll(((LeafNode) ne).getPoints());
        }
        if (sw instanceof LeafNode) {
            mergedPoints.addAll(((LeafNode) sw).getPoints());
        }
        if (se instanceof LeafNode) {
            mergedPoints.addAll(((LeafNode) se).getPoints());
        }
        
        return new LeafNode(mergedPoints);
    }
    
    @Override
    public QuadNode insert(Point point, int x, int y, int size) {
        int quadrant = getQuadrant(point, x, y, size);
        int[] childParams = getChildParameters(quadrant, x, y, size);
        
        // Insert the point into the appropriate child
        switch (quadrant) {
            case 0: // NW
                nw = nw.insert(point, childParams[0], childParams[1], childParams[2]);
                break;
            case 1: // NE
                ne = ne.insert(point, childParams[0], childParams[1], childParams[2]);
                break;
            case 2: // SW
                sw = sw.insert(point, childParams[0], childParams[1], childParams[2]);
                break;
            case 3: // SE
                se = se.insert(point, childParams[0], childParams[1], childParams[2]);
                break;
        }
        
        // After insertion, check if we should merge
        if (shouldMerge()) {
            return mergeChildren();
        }
        
        return this;
    }
    
    @Override
    public RemoveResult removeByName(String name, int x, int y, int size) {
        // Try to remove from each child
        RemoveResult result;
        Point removedPoint = null;
        
        // NW
        int[] nwParams = getChildParameters(0, x, y, size);
        result = nw.removeByName(name, nwParams[0], nwParams[1], nwParams[2]);
        nw = result.getNode();
        if (result.getRemovedPoint() != null) {
            removedPoint = result.getRemovedPoint();
        }
        
        // If point already removed, just check if we should merge
        if (removedPoint != null) {
            if (shouldMerge()) {
                return new RemoveResult(mergeChildren(), removedPoint);
            }
            return new RemoveResult(this, removedPoint);
        }
        
        // NE
        int[] neParams = getChildParameters(1, x, y, size);
        result = ne.removeByName(name, neParams[0], neParams[1], neParams[2]);
        ne = result.getNode();
        if (result.getRemovedPoint() != null) {
            removedPoint = result.getRemovedPoint();
        }
        
        // If point already removed, just check if we should merge
        if (removedPoint != null) {
            if (shouldMerge()) {
                return new RemoveResult(mergeChildren(), removedPoint);
            }
            return new RemoveResult(this, removedPoint);
        }
        
        // SW
        int[] swParams = getChildParameters(2, x, y, size);
        result = sw.removeByName(name, swParams[0], swParams[1], swParams[2]);
        sw = result.getNode();
        if (result.getRemovedPoint() != null) {
            removedPoint = result.getRemovedPoint();
        }
        
        // If point already removed, just check if we should merge
        if (removedPoint != null) {
            if (shouldMerge()) {
                return new RemoveResult(mergeChildren(), removedPoint);
            }
            return new RemoveResult(this, removedPoint);
        }
        
        // SE
        int[] seParams = getChildParameters(3, x, y, size);
        result = se.removeByName(name, seParams[0], seParams[1], seParams[2]);
        se = result.getNode();
        if (result.getRemovedPoint() != null) {
            removedPoint = result.getRemovedPoint();
        }
        
        // Check if we should merge after removal
        if (removedPoint != null && shouldMerge()) {
            return new RemoveResult(mergeChildren(), removedPoint);
        }
        
        return new RemoveResult(this, removedPoint);
    }
    
    @Override
    public RemoveResult removeByPosition(int xPos, int yPos, int x, int y, int size) {
        // Determine which quadrant the position is in
        int quadrant;
        int halfSize = size / 2;
        int midX = x + halfSize;
        int midY = y + halfSize;
        
        if (xPos < midX) {
            if (yPos < midY) {
                quadrant = 0; // NW
            } else {
                quadrant = 2; // SW
            }
        } else {
            if (yPos < midY) {
                quadrant = 1; // NE
            } else {
                quadrant = 3; // SE
            }
        }
        
        // Remove from the appropriate quadrant
        int[] childParams = getChildParameters(quadrant, x, y, size);
        RemoveResult result;
        
        switch (quadrant) {
            case 0: // NW
                result = nw.removeByPosition(xPos, yPos, childParams[0], 
                        childParams[1], childParams[2]);
                nw = result.getNode();
                break;
            case 1: // NE
                result = ne.removeByPosition(xPos, yPos, childParams[0], 
                        childParams[1], childParams[2]);
                ne = result.getNode();
                break;
            case 2: // SW
                result = sw.removeByPosition(xPos, yPos, childParams[0], 
                        childParams[1], childParams[2]);
                sw = result.getNode();
                break;
            case 3: // SE
                result = se.removeByPosition(xPos, yPos, childParams[0], 
                        childParams[1], childParams[2]);
                se = result.getNode();
                break;
            default:
                return new RemoveResult(this, null);
        }
        
        // Check if we should merge after removal
        if (result.getRemovedPoint() != null && shouldMerge()) {
            return new RemoveResult(mergeChildren(), result.getRemovedPoint());
        }
        
        return new RemoveResult(this, result.getRemovedPoint());
    }
    
    @Override
    public int regionSearch(int x1, int y1, int w, int h, int x, int y, int size, 
            List<Point> results) {
        int nodesVisited = 1; // Count this node
        
        // Check if the search region intersects with each quadrant
        // NW
        int[] nwParams = getChildParameters(0, x, y, size);
        if (intersects(x1, y1, w, h, nwParams[0], nwParams[1], nwParams[2])) {
            nodesVisited += nw.regionSearch(x1, y1, w, h, nwParams[0], 
                    nwParams[1], nwParams[2], results);
        }
        
        // NE
        int[] neParams = getChildParameters(1, x, y, size);
        if (intersects(x1, y1, w, h, neParams[0], neParams[1], neParams[2])) {
            nodesVisited += ne.regionSearch(x1, y1, w, h, neParams[0], 
                    neParams[1], neParams[2], results);
        }
        
        // SW
        int[] swParams = getChildParameters(2, x, y, size);
        if (intersects(x1, y1, w, h, swParams[0], swParams[1], swParams[2])) {
            nodesVisited += sw.regionSearch(x1, y1, w, h, swParams[0], 
                    swParams[1], swParams[2], results);
        }
        
        // SE
        int[] seParams = getChildParameters(3, x, y, size);
        if (intersects(x1, y1, w, h, seParams[0], seParams[1], seParams[2])) {
            nodesVisited += se.regionSearch(x1, y1, w, h, seParams[0], 
                    seParams[1], seParams[2], results);
        }
        
        return nodesVisited;
    }
    
    /**
     * Checks if a search region intersects with a quadrant.
     * 
     * @param x1 The x-coordinate of the search region
     * @param y1 The y-coordinate of the search region
     * @param w The width of the search region
     * @param h The height of the search region
     * @param qx The x-coordinate of the quadrant
     * @param qy The y-coordinate of the quadrant
     * @param qsize The size of the quadrant
     * @return true if they intersect, false otherwise
     */
    private boolean intersects(int x1, int y1, int w, int h, int qx, int qy, int qsize) {
        // Check if the search region intersects with the quadrant
        return x1 < qx + qsize && x1 + w > qx && y1 < qy + qsize && y1 + h > qy;
    }
    
    @Override
    public void findDuplicates(Set<Position> duplicates) {
        // Forward to all children
        nw.findDuplicates(duplicates);
        ne.findDuplicates(duplicates);
        sw.findDuplicates(duplicates);
        se.findDuplicates(duplicates);
    }
    
    @Override
    public void dump(int level, int x, int y, int size, StringBuilder sb) {
        // Indent based on level
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        
        sb.append("Node at ").append(x).append(", ").append(y);
        sb.append(", size ").append(size).append(": Internal\n");
        
        // Dump children in order: NW, NE, SW, SE
        int[] nwParams = getChildParameters(0, x, y, size);
        nw.dump(level + 1, nwParams[0], nwParams[1], nwParams[2], sb);
        
        int[] neParams = getChildParameters(1, x, y, size);
        ne.dump(level + 1, neParams[0], neParams[1], neParams[2], sb);
        
        int[] swParams = getChildParameters(2, x, y, size);
        sw.dump(level + 1, swParams[0], swParams[1], swParams[2], sb);
        
        int[] seParams = getChildParameters(3, x, y, size);
        se.dump(level + 1, seParams[0], seParams[1], seParams[2], sb);
    }
}
