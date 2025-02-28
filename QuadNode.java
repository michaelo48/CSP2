import java.util.List;
import java.util.Set;

/**
 * Interface for nodes in the PR Quadtree.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public interface QuadNode {
    /**
     * Inserts a point into the quadtree.
     * 
     * @param point The point to insert
     * @param x The x-coordinate of the current quadrant
     * @param y The y-coordinate of the current quadrant
     * @param size The size of the current quadrant
     * @return The updated node after insertion
     */
    QuadNode insert(Point point, int x, int y, int size);
    
    /**
     * Removes a point by name.
     * 
     * @param name The name of the point to remove
     * @param x The x-coordinate of the current quadrant
     * @param y The y-coordinate of the current quadrant
     * @param size The size of the current quadrant
     * @return The updated node after removal and a removed point
     */
    RemoveResult removeByName(String name, int x, int y, int size);
    
    /**
     * Removes a point by position.
     * 
     * @param xPos The x-coordinate of the point to remove
     * @param yPos The y-coordinate of the point to remove
     * @param x The x-coordinate of the current quadrant
     * @param y The y-coordinate of the current quadrant
     * @param size The size of the current quadrant
     * @return The updated node after removal and a removed point
     */
    RemoveResult removeByPosition(int xPos, int yPos, int x, int y, int size);
    
    /**
     * Searches for points within a given region.
     * 
     * @param x1 The x-coordinate of the region's top-left corner
     * @param y1 The y-coordinate of the region's top-left corner
     * @param w The width of the region
     * @param h The height of the region
     * @param x The x-coordinate of the current quadrant
     * @param y The y-coordinate of the current quadrant
     * @param size The size of the current quadrant
     * @param results The list to store found points
     * @return The number of nodes visited
     */
    int regionSearch(int x1, int y1, int w, int h, int x, int y, int size, 
            List<Point> results);
    
    /**
     * Finds duplicate points (points with the same position).
     * 
     * @param duplicates The set to store positions of duplicate points
     */
    void findDuplicates(Set<Position> duplicates);
    
    /**
     * Dumps the quadtree node contents.
     * 
     * @param level The level of the node in the tree
     * @param x The x-coordinate of the current quadrant
     * @param y The y-coordinate of the current quadrant
     * @param size The size of the current quadrant
     * @param sb The StringBuilder to append the dump to
     */
    void dump(int level, int x, int y, int size, StringBuilder sb);
}
