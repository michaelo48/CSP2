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
        // Rule: If more than 3 points and not all at the same location, split
        if (points.size() <= 3) {
            return false;
        }

        // Check if all points are at the same location
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

        // Check if we need to split based on decomposition rules
        if (shouldSplit()) {
            // Create internal node and distribute points
            InternalNode internal = new InternalNode();

            // Save current points and clear the list
            PointList currentPoints = points.copy();
            points.clear();

            // Insert all points into the new internal node
            QuadNode newNode = internal;
            for (int i = 0; i < currentPoints.size(); i++) {
                newNode = newNode.insert(currentPoints.get(i), x, y, size);
            }

            return newNode;
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
        Point removedPoint = null;

        // Find a point at the specified location
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.getX() == x && p.getY() == y) {
                removedPoint = points.remove(i);
                break;
            }
        }

        // If the node is now empty, return an EmptyNode
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

        // Find a point with the specified name
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.getName().equals(name)) {
                removedPoint = points.remove(i);
                break;
            }
        }

        // If the node is now empty, return an EmptyNode
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
        // Add points that are within the query rectangle
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.isInRectangle(x, y, w, h)) {
                results.add(p);
            }
        }

        return 1; // Count this node as visited
    }


    @Override
    public int findDuplicates(
        int regionX,
        int regionY,
        int size,
        CoordinateList duplicates) {
        // Check for duplicate locations
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (points.get(i).sameLocation(points.get(j))) {
                    // Add the duplicate location to the list
                    duplicates.add(points.get(i).getX(), points.get(i).getY());
                    break; // Found a duplicate for this point, move to next
                           // point
                }
            }
        }

        return 1; // Count this node as visited
    }


    @Override
    public void dump(int indent) {
        // Print indentation
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }

        System.out.println("Leaf with " + points.size() + " points:");

        // Print each point with additional indentation
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < indent + 1; j++) {
                System.out.print("  ");
            }
            System.out.println(points.get(i).toString());
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
        // Leaf nodes with points compare based on number of points
        if (other instanceof LeafNode) {
            LeafNode otherLeaf = (LeafNode)other;
            return Integer.compare(points.size(), otherLeaf.getPoints().size());
        }
        // Leaf nodes come before internal nodes
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
