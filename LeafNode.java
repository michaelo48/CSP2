import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a leaf node in the PR Quadtree.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class LeafNode implements QuadNode {
    private ArrayList<Point> points;
    
    /**
     * Creates a new LeafNode with a single point.
     * 
     * @param point The point to store
     */
    public LeafNode(Point point) {
        points = new ArrayList<>();
        points.add(point);
    }
    
    /**
     * Creates a new LeafNode with a list of points.
     * 
     * @param points The list of points to store
     */
    public LeafNode(ArrayList<Point> points) {
        this.points = points;
    }
    
    /**
     * Gets the list of points in this node.
     * 
     * @return The list of points
     */
    public ArrayList<Point> getPoints() {
        return points;
    }
    
    @Override
    public QuadNode insert(Point point, int x, int y, int size) {
        // Check if we need to split according to decomposition rules
        
        // First, check if the new point is at the same location as existing points
        boolean sameLocation = false;
        if (!points.isEmpty()) {
            Point firstPoint = points.get(0);
            sameLocation = point.getX() == firstPoint.getX() && 
                           point.getY() == firstPoint.getY();
        }
        
        // If all points are at the same location, just add the new point
        if (sameLocation) {
            points.add(point);
            return this;
        }
        
        // If we have 3 or fewer points, add the new point
        if (points.size() <= 2) {
            points.add(point);
            return this;
        }
        
        // Otherwise, we need to split
        // Add the new point to our existing points
        points.add(point);
        
        // Create an internal node
        InternalNode internalNode = new InternalNode();
        
        // Insert all points into the internal node
        for (Point p : points) {
            internalNode = (InternalNode) internalNode.insert(p, x, y, size);
        }
        
        return internalNode;
    }
    
    @Override
    public RemoveResult removeByName(String name, int x, int y, int size) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getName().equals(name)) {
                Point removedPoint = points.remove(i);
                
                // If no points remain, return an empty node
                if (points.isEmpty()) {
                    return new RemoveResult(EmptyNode.getInstance(), removedPoint);
                }
                
                return new RemoveResult(this, removedPoint);
            }
        }
        
        // No matching point found
        return new RemoveResult(this, null);
    }
    
    @Override
    public RemoveResult removeByPosition(int xPos, int yPos, int x, int y, int size) {
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.getX() == xPos && p.getY() == yPos) {
                Point removedPoint = points.remove(i);
                
                // If no points remain, return an empty node
                if (points.isEmpty()) {
                    return new RemoveResult(EmptyNode.getInstance(), removedPoint);
                }
                
                return new RemoveResult(this, removedPoint);
            }
        }
        
        // No matching point found
        return new RemoveResult(this, null);
    }
    
    @Override
    public int regionSearch(int x1, int y1, int w, int h, int x, int y, int size, 
            List<Point> results) {
        // Check each point to see if it's in the search region
        for (Point p : points) {
            if (p.getX() >= x1 && p.getX() <= x1 + w && 
                p.getY() >= y1 && p.getY() <= y1 + h) {
                results.add(p);
            }
        }
        
        // Count as one node visited
        return 1;
    }
    
    @Override
    public void findDuplicates(Set<Position> duplicates) {
        // Create a map to count points at each position
        Map<Position, Integer> positionCounts = new HashMap<>();
        
        for (Point p : points) {
            Position pos = new Position(p.getX(), p.getY());
            positionCounts.put(pos, positionCounts.getOrDefault(pos, 0) + 1);
        }
        
        // Add positions with counts > 1 to the duplicates set
        for (Map.Entry<Position, Integer> entry : positionCounts.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.add(entry.getKey());
            }
        }
    }
    
    @Override
    public void dump(int level, int x, int y, int size, StringBuilder sb) {
        // Indent based on level
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        
        sb.append("Node at ").append(x).append(", ").append(y);
        sb.append(", size ").append(size).append(":\n");
        
        // Indent for point list
        for (int i = 0; i < level + 1; i++) {
            sb.append("  ");
        }
        
        // List points
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            sb.append(p.toString());
            if (i < points.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("\n");
    }
}
