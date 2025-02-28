/**
 * Represents a position in the 2D plane.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class Position {
    private int x;
    private int y;
    
    /**
     * Creates a new Position.
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the x-coordinate.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the y-coordinate.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }
    
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
