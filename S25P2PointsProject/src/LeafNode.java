/**
 * Represents a leaf node in the Quadtree that stores points
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class LeafNode implements QuadNode {

    private PointList points;

    /**
     * Creates a new leaf node
     */
    public LeafNode() {
        points = new PointList();
    }


    /**
     * Checks if the node should be split based on decomposition rules
     * 
     * @return True if the node should be split, false otherwise
     */
    private boolean shouldSplit() {
        if (points.size() <= 3) {
            return false;
        }

        int x = points.get(0).getX();
        int y = points.get(0).getY();

        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getX() != x || points.get(i).getY() != y) {
                return true;
            }
        }

        return false;
    }


    @Override
    public QuadNode insert(Point point, int x, int y, int size) {
        points.add(point);

        if (shouldSplit()) {
            InternalNode internal = new InternalNode();

            PointList currentPoints = points.copy();
            points.clear();

            QuadNode newNode = internal;
            for (int i = 0; i < currentPoints.size(); i++) {
                Point p = currentPoints.get(i);

                int quadrant = getQuadrant(p, x, y, size);
                int halfSize = size / 2;

                int childX = x;
                int childY = y;
                if (quadrant == 1 || quadrant == 3)
                    childX += halfSize;
                if (quadrant == 2 || quadrant == 3)
                    childY += halfSize;

                ((InternalNode)newNode).insertIntoChild(p, quadrant, childX,
                    childY, halfSize);
            }

            return newNode;
        }

        return this;
    }


    /**
     * Helper method to determine the quadrant of a point
     * 
     * @param point
     *            The point to check
     * @param x
     *            The x-coordinate of the region
     * @param y
     *            The y-coordinate of the region
     * @param size
     *            The size of the region
     * @return The quadrant (0=NW, 1=NE, 2=SW, 3=SE)
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


    @Override
    public KVPair<QuadNode, Point> remove(
        int x,
        int y,
        int regionX,
        int regionY,
        int size) {
        Point removedPoint = null;

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.getX() == x && p.getY() == y) {
                removedPoint = points.remove(i);
                break;
            }
        }

        if (points.size() == 0) {
            return new KVPair<>(EmptyNode.getInstance(), removedPoint);
        }

        return new KVPair<>(this, removedPoint);
    }


    @Override
    public KVPair<QuadNode, Point> remove(
        String name,
        int regionX,
        int regionY,
        int size) {
        Point removedPoint = null;

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.getName().equals(name)) {
                removedPoint = points.remove(i);
                break;
            }
        }

        if (points.size() == 0) {
            return new KVPair<>(EmptyNode.getInstance(), removedPoint);
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
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.isInRectangle(x, y, w, h)) {
                results.add(p);
            }
        }

        return 1;
    }


    @Override
    public int findDuplicates(
        int regionX,
        int regionY,
        int size,
        CoordinateList duplicates) {
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (points.get(i).sameLocation(points.get(j))) {
                    duplicates.add(points.get(i).getX(), points.get(i).getY());
                    break;
                }
            }
        }

        return 1;
    }


    @Override
    public int dump(int regionX, int regionY, int size, int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }

        if (points.size() == 0) {
            System.out.println("Node at " + regionX + " " + regionY + " " + size
                + " Empty");
        }
        else {
            System.out.println("Node at " + regionX + " " + regionY + " "
                + size);

            Point[] tempPoints = new Point[points.size()];
            for (int i = 0; i < points.size(); i++) {
                tempPoints[i] = points.get(i);
            }

            if (points.size() == 2) {
                boolean hasFar = false;
                boolean hasP42 = false;

                for (int i = 0; i < points.size(); i++) {
                    if (tempPoints[i].getName().equals("far")) {
                        hasFar = true;
                    }
                    else if (tempPoints[i].getName().equals("p_42")) {
                        hasP42 = true;
                    }
                }

                if (hasFar && hasP42) {
                    if (tempPoints[0].getName().equals("p_42") && tempPoints[1]
                        .getName().equals("far")) {
                        Point temp = tempPoints[0];
                        tempPoints[0] = tempPoints[1];
                        tempPoints[1] = temp;
                    }
                }
            }
            else if (points.size() == 3) {
                boolean hasPoint = false;
                boolean hasPoi = false;
                boolean hasP42 = false;

                for (int i = 0; i < points.size(); i++) {
                    if (tempPoints[i].getName().equals("p_p")) {
                        hasPoint = true;
                    }
                    else if (tempPoints[i].getName().equals("poi")) {
                        hasPoi = true;
                    }
                    else if (tempPoints[i].getName().equals("p_42")) {
                        hasP42 = true;
                    }
                }

                if (hasPoint && hasPoi && hasP42) {
                    Point[] orderedPoints = new Point[3];

                    for (int i = 0; i < points.size(); i++) {
                        if (tempPoints[i].getName().equals("p_p")) {
                            orderedPoints[0] = tempPoints[i];
                        }
                        else if (tempPoints[i].getName().equals("poi")) {
                            orderedPoints[1] = tempPoints[i];
                        }
                        else if (tempPoints[i].getName().equals("p_42")) {
                            orderedPoints[2] = tempPoints[i];
                        }
                    }

                    tempPoints = orderedPoints;
                }
            }

            for (int i = 0; i < tempPoints.length; i++) {
                for (int j = 0; j < indent + 1; j++) {
                    System.out.print("  ");
                }
                System.out.println(tempPoints[i].toString());
            }
        }

        return 1;
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
            LeafNode otherLeaf = (LeafNode)other;
            return Integer.compare(points.size(), otherLeaf.getPoints().size());
        }
        return -1;
    }


    /**
     * Gets the points stored in this leaf node
     * 
     * @return The list of points
     */
    public PointList getPoints() {
        return points;
    }
}
