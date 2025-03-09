import student.TestCase;

/**
 * Tests the Point class using mutation testing techniques.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class PointTest extends TestCase {

    private Point point;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        point = new Point("TestPoint", 100, 200);
    }


    /**
     * Tests the constructor and getters.
     */
    public void testConstructorAndGetters() {
        assertEquals("TestPoint", point.getName());
        assertEquals(100, point.getX());
        assertEquals(200, point.getY());
    }


    /**
     * Tests the sameLocation method with points at the same location.
     */
    public void testSameLocationTrue() {
        Point other = new Point("OtherPoint", 100, 200);
        assertTrue(point.sameLocation(other));
    }


    /**
     * Tests the sameLocation method with points at different x-coordinates.
     */
    public void testSameLocationDifferentX() {
        Point other = new Point("OtherPoint", 101, 200);
        assertFalse(point.sameLocation(other));
    }


    /**
     * Tests the sameLocation method with points at different y-coordinates.
     */
    public void testSameLocationDifferentY() {
        Point other = new Point("OtherPoint", 100, 201);
        assertFalse(point.sameLocation(other));
    }


    /**
     * Tests the sameLocation method with points at different x and y
     * coordinates.
     */
    public void testSameLocationDifferentXY() {
        Point other = new Point("OtherPoint", 101, 201);
        assertFalse(point.sameLocation(other));
    }


    /**
     * Tests the isInRectangle method with the point inside the rectangle.
     */
    public void testIsInRectangleInside() {
        assertTrue(point.isInRectangle(50, 150, 100, 100));
    }


    /**
     * Tests the isInRectangle method with the point on the left edge.
     */
    public void testIsInRectangleLeftEdge() {
        assertTrue(point.isInRectangle(100, 150, 100, 100));
    }


    /**
     * Tests the isInRectangle method with the point on the right edge.
     */
    public void testIsInRectangleRightEdge() {
        assertTrue(point.isInRectangle(50, 150, 50, 100));
    }


    /**
     * Tests the isInRectangle method with the point on the top edge.
     */
    public void testIsInRectangleTopEdge() {
        assertTrue(point.isInRectangle(50, 200, 100, 100));
    }


    /**
     * Tests the isInRectangle method with the point on the bottom edge.
     */
    public void testIsInRectangleBottomEdge() {
        assertTrue(point.isInRectangle(50, 150, 100, 50));
    }


    /**
     * Tests the isInRectangle method with the point outside to the left.
     */
    public void testIsInRectangleOutsideLeft() {
        assertFalse(point.isInRectangle(101, 150, 100, 100));
    }


    /**
     * Tests the isInRectangle method with the point outside to the right.
     */
    public void testIsInRectangleOutsideRight() {
        assertFalse(point.isInRectangle(0, 150, 99, 100));
    }


    /**
     * Tests the isInRectangle method with the point outside above.
     */
    public void testIsInRectangleOutsideAbove() {
        assertFalse(point.isInRectangle(50, 201, 100, 100));
    }


    /**
     * Tests the isInRectangle method with the point outside below.
     */
    public void testIsInRectangleOutsideBelow() {
        assertFalse(point.isInRectangle(50, 100, 100, 99));
    }


    /**
     * Tests the toString method.
     */
    public void testToString() {
        assertEquals("TestPoint 100 200", point.toString());
    }


    /**
     * Tests edge cases with zero coordinates.
     */
    public void testZeroCoordinates() {
        Point zeroPoint = new Point("ZeroPoint", 0, 0);
        assertEquals(0, zeroPoint.getX());
        assertEquals(0, zeroPoint.getY());
        assertTrue(zeroPoint.isInRectangle(0, 0, 10, 10));
        assertFalse(zeroPoint.isInRectangle(1, 1, 10, 10));
    }


    /**
     * Tests edge cases with negative coordinates.
     */
    public void testNegativeCoordinates() {
        Point negativePoint = new Point("NegativePoint", -10, -20);
        assertEquals(-10, negativePoint.getX());
        assertEquals(-20, negativePoint.getY());
        assertTrue(negativePoint.isInRectangle(-15, -25, 10, 10));
        assertFalse(negativePoint.isInRectangle(0, 0, 10, 10));
    }


    /**
     * Tests edge cases with a rectangle of negative width and height.
     */
    public void testNegativeSizeRectangle() {
        Point testPoint = new Point("TestPoint", 5, 5);
        assertFalse(testPoint.isInRectangle(5, 5, -1, -1));
    }


    /**
     * Tests exactly matching point and rectangle coordinates.
     */
    public void testExactMatch() {
        assertTrue(point.isInRectangle(100, 200, 0, 0));
    }


    /**
     * Tests boundary conditions for rectangle containment.
     */
    public void testRectangleBoundaries() {

        Point p = new Point("P", 10, 20);

        assertTrue(p.isInRectangle(10, 20, 5, 5));

        assertTrue(p.isInRectangle(5, 20, 5, 5));

        assertTrue(p.isInRectangle(10, 15, 5, 5));

        assertTrue(p.isInRectangle(5, 15, 5, 5));
    }
}
