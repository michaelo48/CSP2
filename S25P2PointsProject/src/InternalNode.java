/**
 * Represents an internal node in the Quadtree
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class InternalNode implements QuadNode {

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
                return 0;
            }
            else {
                return 2;
            }
        }
        else {
            if (point.getY() < midY) {
                return 1;
            }
            else {
                return 3;
            }
        }
    }


    /**
     * Directly inserts a point into a child without going through the full
     * insert process
     * This helps break the recursion cycle
     * 
     * @param point
     *            The point to insert
     * @param quadrant
     *            The quadrant to insert into (0=NW, 1=NE, 2=SW, 3=SE)
     * @param childX
     *            The x-coordinate of the child region
     * @param childY
     *            The y-coordinate of the child region
     * @param childSize
     *            The size of the child region
     */
    public void insertIntoChild(
        Point point,
        int quadrant,
        int childX,
        int childY,
        int childSize) {
        if (children[quadrant] instanceof EmptyNode) {
            LeafNode leaf = new LeafNode();
            leaf.getPoints().add(point);
            children[quadrant] = leaf;
        }
        else if (children[quadrant] instanceof LeafNode) {
            LeafNode leaf = (LeafNode)children[quadrant];
            leaf.getPoints().add(point);

            if (leaf.getPoints().size() > 3) {
                boolean allSameLocation = true;
                Point firstPoint = leaf.getPoints().get(0);
                for (int i = 1; i < leaf.getPoints().size(); i++) {
                    if (!firstPoint.sameLocation(leaf.getPoints().get(i))) {
                        allSameLocation = false;
                        break;
                    }
                }

                if (!allSameLocation) {
                    InternalNode newInternal = new InternalNode();

                    PointList points = leaf.getPoints().copy();
                    for (int i = 0; i < points.size(); i++) {
                        Point p = points.get(i);
                        int subQuadrant = getQuadrant(p, childX, childY,
                            childSize);

                        int halfSize = childSize / 2;
                        int subX = childX;
                        int subY = childY;
                        if (subQuadrant == 1 || subQuadrant == 3)
                            subX += halfSize;
                        if (subQuadrant == 2 || subQuadrant == 3)
                            subY += halfSize;

                        newInternal.insertIntoChild(p, subQuadrant, subX, subY,
                            halfSize);
                    }

                    children[quadrant] = newInternal;
                }
            }
        }
        else if (children[quadrant] instanceof InternalNode) {
            InternalNode internal = (InternalNode)children[quadrant];
            int subQuadrant = getQuadrant(point, childX, childY, childSize);

            int halfSize = childSize / 2;
            int subX = childX;
            int subY = childY;
            if (subQuadrant == 1 || subQuadrant == 3)
                subX += halfSize;
            if (subQuadrant == 2 || subQuadrant == 3)
                subY += halfSize;

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
        result[2] = halfSize;

        switch (quadrant) {
            case 0:
                result[0] = x;
                result[1] = y;
                break;
            case 1:
                result[0] = x + halfSize;
                result[1] = y;
                break;
            case 2:
                result[0] = x;
                result[1] = y + halfSize;
                break;
            case 3:
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

        for (int i = 0; i < 4; i++) {
            if (children[i] instanceof EmptyNode) {
                emptyCount++;
            }
            else if (children[i] instanceof LeafNode) {
                leafCount++;
                LeafNode leaf = (LeafNode)children[i];
                int leafSize = leaf.getPoints().size();
                totalPoints += leafSize;

                for (int j = 0; j < leafSize; j++) {
                    allPoints.add(leaf.getPoints().get(j));
                }
            }
            else {
                return false;
            }
        }

        if (emptyCount == 4) {
            return true;
        }

        if (emptyCount + leafCount == 4) {
            if (totalPoints <= 3) {
                return true;
            }

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
        int quadrant = getQuadrant(point, x, y, size);

        int[] childCoords = getChildCoordinates(quadrant, x, y, size);

        children[quadrant] = children[quadrant].insert(point, childCoords[0],
            childCoords[1], childCoords[2]);

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
        Point mockPoint = new Point("", x, y);
        int quadrant = getQuadrant(mockPoint, regionX, regionY, size);

        int[] childCoords = getChildCoordinates(quadrant, regionX, regionY,
            size);

        KVPair<QuadNode, Point> result = children[quadrant].remove(x, y,
            childCoords[0], childCoords[1], childCoords[2]);

        children[quadrant] = result.key();

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

        for (int i = 0; i < 4; i++) {
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);

            KVPair<QuadNode, Point> result = children[i].remove(name,
                childCoords[0], childCoords[1], childCoords[2]);

            children[i] = result.key();

            if (result.value() != null) {
                removedPoint = result.value();
                break;
            }
        }

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
        int nodesVisited = 1;

        if (!isIntersecting(x, y, w, h, regionX, regionY, size)) {
            return nodesVisited;
        }

        for (int i = 0; i < 4; i++) {
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);

            if (isIntersecting(x, y, w, h, childCoords[0], childCoords[1],
                childCoords[2])) {
                nodesVisited += children[i].regionsearch(x, y, w, h,
                    childCoords[0], childCoords[1], childCoords[2], results);
            }
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
        int nodesVisited = 1;

        for (int i = 0; i < 4; i++) {
            int[] childCoords = getChildCoordinates(i, regionX, regionY, size);

            nodesVisited += children[i].findDuplicates(childCoords[0],
                childCoords[1], childCoords[2], duplicates);
        }

        return nodesVisited;
    }


    @Override
    public int dump(int regionX, int regionY, int size, int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }

        System.out.println("Node at " + regionX + " " + regionY + " " + size
            + " Internal");

        int nodesCount = 1;

        int halfSize = size / 2;

        nodesCount += children[0].dump(regionX, regionY, halfSize, indent + 1);

        nodesCount += children[1].dump(regionX + halfSize, regionY, halfSize,
            indent + 1);

        nodesCount += children[2].dump(regionX, regionY + halfSize, halfSize,
            indent + 1);

        nodesCount += children[3].dump(regionX + halfSize, regionY + halfSize,
            halfSize, indent + 1);

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
        return 0;
    }
}
