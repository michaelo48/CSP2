/**
 * Represents an internal node in the Quadtree
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class InternalNode implements QuadNode {
    
    // NW, NE, SW, SE children
    private QuadNode[] children;
    
    /**
     * Creates a new internal node with four empty children
     */
    public InternalNode() {
        children = new QuadNode[4];
        for (int i = 0; i < 4; i++) {
            children[i] = EmptyNode.getInstance();
        }
    }
    
    /**
     * Determines which quadrant a point belongs to
     * 
     * @param point
     *            The point to check
     * @param x
     *            The x-coordinate of the region
     * @param y
     *            The y-coordinate of the region
     * @param size
     *            The size of the region
     * @return The quadrant index (0=NW, 1=NE, 2=SW, 3=SE)
     */
    private int getQuadrant(Point point, int x, int y, int size) {
        int halfSize = size / 2;
        int midX = x + halfSize;
        int midY = y + halfSize;
        
        if (point.getX() < midX) {
            if (point.getY() < midY) {
                return 0; // NW
            }
            else {
                return 2; // SW
            }
        }
        else {
            if (point.getY() < midY) {
                return 1; // NE
            }
            else {
                return 3; // SE
            }
        }
    }
    
    /**
     * Gets the coordinates and size of a child quadrant
     * 
     * @param quadrant
     *            The quadrant index (0=NW, 1=NE, 2=SW, 3=SE)
     * @param x
     *            The x-coordinate of the parent region
     * @param y
     *            The y-coordinate of the parent region
     * @param size
     *            The size of the parent region
     * @return Array containing [childX, childY, childSize]
     */
    private int[] getChildCoordinates(int quadrant, int x, int y, int size) {
        int halfSize = size / 2;
        int[] result = new int[3];
        result[2] = halfSize; // Child size
        
        switch (quadrant) {
            case 0: // NW
                result[0] = x;
                result[1] = y;
                break;
            case 1: // NE
                result[0] = x + halfSize;
                result[1] = y;
                break;
            case 2: // SW
                result[0] = x;
                result[1] = y + halfSize;
                break;
            case 3: // SE
                result[0] = x + halfSize;
                result[1] = y + halfSize;
                break;
        }
        
        return result;
    }
    
    /**
     * Checks if this node should merge based on decomposition rules
     * 
     * @return True if the node should merge, false otherwise
     */
    private boolean shouldMerge() {
        int totalPoints = 0;
        PointList allPoints = new PointList();
        
        // Count points and collect them
        for (int i = 0; i < 4; i++) {
            if (children[i] instanceof LeafNode) {
                LeafNode leaf = (LeafNode) children[i];
                totalPoints += leaf.getPoints().size();
                allPoints.addAll(leaf.getPoints());
            }
            else if (!(children[i] instanceof EmptyNode)) {
                // If any child is not a leaf or empty, we can't merge
                return false;
            }
        }
        
        // If total points <= 3, we should merge
        if (totalPoints <= 3) {
            return true;
        }
        
        // Check if all points have the same coordinates
        if (totalPoints > 0) {
            Point first = allPoints.get(0);
            for (int i = 1; i < allPoints.size(); i++) {
                if (!first.sameLocation(allPoints.get(i))) {
                    return false;
                }
            }
            // All points have same location, should merge
            return true;
        }
        
        return false;
    }
    
    /**
     * Merges all children into a single leaf node
     * 
     * @return The new leaf node
     */
    private LeafNode mergeChildren() {
        LeafNode merged = new LeafNode();
        
        // Collect all points from leaf children
        for (int i = 0; i < 4; i++) {
            if (children[i] instanceof LeafNode) {
                LeafNode leaf = (LeafNode) children[i];
                merged.getPoints().addAll(leaf.getPoints());
            }
        }
        
        return merged;
    }
    
    @Override
    public QuadNode insert(Point point, int x, int y, int size) {
        // Determine which quadrant the point belongs to
        int quadrant = getQuadrant(point, x, y, size);
        
        // Get coordinates of the child quadrant
        int[] childCoords = getChildCoordinates(quadrant, x, y, size);
        
        // Insert into the appropriate child
        children[quadrant] = children[quadrant].insert(
                point, childCoords[0], childCoords[1], childCoords[2]);
        
        // Check if we should merge after insertion
        if (shouldMerge()) {
            return mergeChildren();
        }
        
        return this;
    }
    
    @Override
    public KVPair<QuadNode, Point> remove(int x, int y, int regionX, int regionY, int size) {
        // Determine which quadrant contains the point
        Point mockPoint = new Point("", x, y);
        int quadrant = getQuadrant(mockPoint, regionX, regionY, size);
        
        // Get coordinates of the child quadrant
        int[] childCoords = getChildCoordinates(quadrant, regionX, regionY, size);
        
        // Try to remove from the appropriate child
        KVPair<QuadNode, Point> result = children[quadrant].remove(
                x, y, childCoords[0], childCoords[1], childCoords[2]);
        
        // Update the child with the result
        children[quadrant] = result.key();
        
        // If a point was removed, check if we should merge
        if (result.value() != null && shouldMerge()) {
            return new KVPair<>(mergeChildren(), result.value());
        }
        
        return new KVPair<>(this, result.value());
    }
    
    @Override
    public KVPair<QuadNode, Point> remove(String name, int regionX, int regionY, int size) {
        Point removedPoint = null;
        
        // Try to remove from each child until found
        for (int i = 0; i < 4; i++) {
            // Get coordinates of the child quadrant
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);
            
            // Try to remove from this child
            KVPair<QuadNode, Point> result = children[i].remove(
                    name, childCoords[0], childCoords[1], childCoords[2]);
            
            // Update the child with the result
            children[i] = result.key();
            
            // If found, remember the removed point
            if (result.value() != null) {
                removedPoint = result.value();
                break;
            }
        }
        
        // If a point was removed, check if we should merge
        if (removedPoint != null && shouldMerge()) {
            return new KVPair<>(mergeChildren(), removedPoint);
        }
        
        return new KVPair<>(this, removedPoint);
    }
    
    @Override
    public int regionsearch(int x, int y, int w, int h, 
            int regionX, int regionY, int size, PointList results) {
        int nodesVisited = 1; // Count this node
        
        // Check if query rectangle intersects with this region
        if (!isIntersecting(x, y, w, h, regionX, regionY, size)) {
            return nodesVisited;
        }
        
        // Search all children that might contain points in the query rectangle
        for (int i = 0; i < 4; i++) {
            // Get coordinates of the child quadrant
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);
            
            // Search this child
            nodesVisited += children[i].regionsearch(
                    x, y, w, h, childCoords[0], childCoords[1], childCoords[2], results);
        }
        
        return nodesVisited;
    }
    
    /**
     * Checks if a query rectangle intersects with a region
     * 
     * @param queryX
     *            The x-coordinate of the query rectangle
     * @param queryY
     *            The y-coordinate of the query rectangle
     * @param queryW
     *            The width of the query rectangle
     * @param queryH
     *            The height of the query rectangle
     * @param regionX
     *            The x-coordinate of the region
     * @param regionY
     *            The y-coordinate of the region
     * @param regionSize
     *            The size of the region
     * @return True if the rectangles intersect, false otherwise
     */
    private boolean isIntersecting(int queryX, int queryY, int queryW, int queryH,
            int regionX, int regionY, int regionSize) {
        return (queryX < regionX + regionSize &&
                queryX + queryW > regionX &&
                queryY < regionY + regionSize &&
                queryY + queryH > regionY);
    }
    
    @Override
    public int findDuplicates(int regionX, int regionY, int size, 
            CoordinateList duplicates) {
        int nodesVisited = 1; // Count this node
        
        // Search all children
        for (int i = 0; i < 4; i++) {
            // Get coordinates of the child quadrant
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);
            
            // Search this child
            nodesVisited += children[i].findDuplicates(
                    childCoords[0], childCoords[1], childCoords[2], duplicates);
        }
        
        return nodesVisited;
    }
    
    @Override
    public void dump(int indent) {
        // Print indentation
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        
        System.out.println("Internal");
        
        // Print each child with additional indentation
        for (int i = 0; i < 4; i++) {
            children[i].dump(indent + 1);
        }
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public int compareTo(QuadNode other) {
        if (other.isEmpty()) {
            return 1;
        }
        if (other instanceof LeafNode) {
            return 1;
        }
        // Two internal nodes are considered equal
        return 0;
    }
}