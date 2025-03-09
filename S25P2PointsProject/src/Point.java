/**
 * Represents a point with a name and (x,y) coordinates
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class Point {
    private String name;
    private int x;
    private int y;

    /**
     * Constructor for creating a Point
     * 
     * @param name
     *            The name of the point
     * @param x
     *            The x-coordinate
     * @param y
     *            The y-coordinate
     */
    public Point(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }


    /**
     * Gets the name of the point
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }


    /**
     * Gets the x-coordinate
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }


    /**
     * Gets the y-coordinate
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }


    /**
     * Checks if this point is at the same location as another point
     * 
     * @param other
     *            The other point to compare with
     * @return True if at the same location, false otherwise
     */
    public boolean sameLocation(Point other) {
        return this.x == other.getX() && this.y == other.getY();
    }


    /**
     * Checks if this point is within a rectangle
     * 
     * @param x0
     *            The x-coordinate of the rectangle's top-left corner
     * @param y0
     *            The y-coordinate of the rectangle's top-left corner
     * @param w
     *            The width of the rectangle
     * @param h
     *            The height of the rectangle
     * @return True if the point is within the rectangle, false otherwise
     */
    public boolean isInRectangle(int x0, int y0, int w, int h) {
        return this.x >= x0 && this.x <= (x0 + w) && this.y >= y0
            && this.y <= (y0 + h);
    }


    /**
     * String representation of the point
     * 
     * @return String in the format "name x y"
     */
    @Override
    public String toString() {
        return name + " " + x + " " + y;
    }
}