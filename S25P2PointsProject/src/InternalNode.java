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
     * Directly inserts a point into a child without going through the full insert process
     * This helps break the recursion cycle
     * 
     * @param point The point to insert
     * @param quadrant The quadrant to insert into (0=NW, 1=NE, 2=SW, 3=SE)
     * @param childX The x-coordinate of the child region
     * @param childY The y-coordinate of the child region
     * @param childSize The size of the child region
     */
    public void insertIntoChild(Point point, int quadrant, int childX, int childY, int childSize) {
        if (children[quadrant] instanceof EmptyNode) {
            // Create a new leaf node for the first point in this quadrant
            LeafNode leaf = new LeafNode();
            leaf.getPoints().add(point);
            children[quadrant] = leaf;
        }
        else if (children[quadrant] instanceof LeafNode) {
            // Add to existing leaf node
            LeafNode leaf = (LeafNode)children[quadrant];
            leaf.getPoints().add(point);
            
            // Check if we need to split this leaf node
            if (leaf.getPoints().size() > 3) {
                // Check if all points are at the same location
                boolean allSameLocation = true;
                Point firstPoint = leaf.getPoints().get(0);
                for (int i = 1; i < leaf.getPoints().size(); i++) {
                    if (!firstPoint.sameLocation(leaf.getPoints().get(i))) {
                        allSameLocation = false;
                        break;
                    }
                }
                
                // Only split if points are at different locations
                if (!allSameLocation) {
                    // Create a new internal node
                    InternalNode newInternal = new InternalNode();
                    
                    // Distribute all points from the leaf to the new internal node
                    PointList points = leaf.getPoints().copy();
                    for (int i = 0; i < points.size(); i++) {
                        Point p = points.get(i);
                        int subQuadrant = getQuadrant(p, childX, childY, childSize);
                        
                        int halfSize = childSize / 2;
                        int subX = childX;
                        int subY = childY;
                        if (subQuadrant == 1 || subQuadrant == 3) subX += halfSize;
                        if (subQuadrant == 2 || subQuadrant == 3) subY += halfSize;
                        
                        newInternal.insertIntoChild(p, subQuadrant, subX, subY, halfSize);
                    }
                    
                    // Replace the leaf with the new internal node
                    children[quadrant] = newInternal;
                }
            }
        }
        else if (children[quadrant] instanceof InternalNode) {
            // Pass to the internal node
            InternalNode internal = (InternalNode)children[quadrant];
            int subQuadrant = getQuadrant(point, childX, childY, childSize);
            
            int halfSize = childSize / 2;
            int subX = childX;
            int subY = childY;
            if (subQuadrant == 1 || subQuadrant == 3) subX += halfSize;
            if (subQuadrant == 2 || subQuadrant == 3) subY += halfSize;
            
            internal.insertIntoChild(point, subQuadrant, subX, subY, halfSize);
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
        int emptyCount = 0;
        int leafCount = 0;
        int totalPoints = 0;
        PointList allPoints = new PointList();

        // Count different node types and collect points
        for (int i = 0; i < 4; i++) {
            if (children[i] instanceof EmptyNode) {
                emptyCount++;
            }
            else if (children[i] instanceof LeafNode) {
                leafCount++;
                LeafNode leaf = (LeafNode)children[i];
                int leafSize = leaf.getPoints().size();
                totalPoints += leafSize;
                
                // Collect points for checking if all have same coordinates
                for (int j = 0; j < leafSize; j++) {
                    allPoints.add(leaf.getPoints().get(j));
                }
            }
            else {
                // If any child is not a leaf or empty, we can't merge
                return false;
            }
        }

        // If all children are empty, this should become an empty node
        if (emptyCount == 4) {
            return true;
        }
        
        // If all nodes are either empty or leaf nodes
        if (emptyCount + leafCount == 4) {
            // If total points <= 3, we should merge
            if (totalPoints <= 3) {
                return true;
            }
            
            // Check if all points have the same coordinates
            if (totalPoints > 0) {
                Point first = allPoints.get(0);
                boolean allSame = true;
                
                for (int i = 1; i < allPoints.size(); i++) {
                    if (!first.sameLocation(allPoints.get(i))) {
                        allSame = false;
                        break;
                    }
                }
                
                return allSame;
            }
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
                LeafNode leaf = (LeafNode)children[i];
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
        children[quadrant] = children[quadrant].insert(point, childCoords[0],
            childCoords[1], childCoords[2]);

        // Check if we should merge after insertion
        if (shouldMerge()) {
            return mergeChildren();
        }

        return this;
    }


    @Override
    public KVPair<QuadNode, Point> remove(
        int x,
        int y,
        int regionX,
        int regionY,
        int size) {
        // Determine which quadrant contains the point
        Point mockPoint = new Point("", x, y);
        int quadrant = getQuadrant(mockPoint, regionX, regionY, size);

        // Get coordinates of the child quadrant
        int[] childCoords = getChildCoordinates(quadrant, regionX, regionY,
            size);

        // Try to remove from the appropriate child
        KVPair<QuadNode, Point> result = children[quadrant].remove(x, y,
            childCoords[0], childCoords[1], childCoords[2]);

        // Update the child with the result
        children[quadrant] = result.key();

        // If a point was removed, check if we should merge
        if (result.value() != null && shouldMerge()) {
            return new KVPair<>(mergeChildren(), result.value());
        }

        return new KVPair<>(this, result.value());
    }


    @Override
    public KVPair<QuadNode, Point> remove(
        String name,
        int regionX,
        int regionY,
        int size) {
        Point removedPoint = null;

        // Try to remove from each child until found
        for (int i = 0; i < 4; i++) {
            // Get coordinates of the child quadrant
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);

            // Try to remove from this child
            KVPair<QuadNode, Point> result = children[i].remove(name,
                childCoords[0], childCoords[1], childCoords[2]);

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
    public int regionsearch(
        int x,
        int y,
        int w,
        int h,
        int regionX,
        int regionY,
        int size,
        PointList results) {
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
            nodesVisited += children[i].regionsearch(x, y, w, h, childCoords[0],
                childCoords[1], childCoords[2], results);
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
    private boolean isIntersecting(
        int queryX,
        int queryY,
        int queryW,
        int queryH,
        int regionX,
        int regionY,
        int regionSize) {
        return (queryX < regionX + regionSize && queryX + queryW > regionX
            && queryY < regionY + regionSize && queryY + queryH > regionY);
    }


    @Override
    public int findDuplicates(
        int regionX,
        int regionY,
        int size,
        CoordinateList duplicates) {
        int nodesVisited = 1; // Count this node

        // Search all children
        for (int i = 0; i < 4; i++) {
            // Get coordinates of the child quadrant
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);

            // Search this child
            nodesVisited += children[i].findDuplicates(childCoords[0],
                childCoords[1], childCoords[2], duplicates);
        }

        return nodesVisited;
    }


    @Override
    public int dump(int regionX, int regionY, int size, int indent) {
        // Print indentation
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }

        System.out.println("Node at " + regionX + " " + regionY + " " + size + " Internal");
        
        int nodesCount = 1; // Count this node

        // Print each child with additional indentation
        int halfSize = size / 2;
        
        // NW (0)
        nodesCount += children[0].dump(regionX, regionY, halfSize, indent + 1);
        
        // NE (1)
        nodesCount += children[1].dump(regionX + halfSize, regionY, halfSize, indent + 1);
        
        // SW (2)
        nodesCount += children[2].dump(regionX, regionY + halfSize, halfSize, indent + 1);
        
        // SE (3)
        nodesCount += children[3].dump(regionX + halfSize, regionY + halfSize, halfSize, indent + 1);
        
        return nodesCount;
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