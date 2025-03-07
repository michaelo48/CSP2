
public class coordinatetest {

}
import student.TestCase;

/**
 * Tests the Coordinate class by verifying the functionality
 * of all its methods.
 * 
 * @author <your_name>
 * @version 03.07.2025
 */
public class CoordinateTest extends TestCase {
    
    private Coordinate coord1;
    private Coordinate coord2;
    private Coordinate coord3;
    
    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        coord1 = new Coordinate(10, 20);
        coord2 = new Coordinate(10, 20);
        coord3 = new Coordinate(30, 40);
    }
    
    /**
     * Tests the getX method.
     */
    public void testGetX() {
        assertEquals(10, coord1.getX());
        assertEquals(30, coord3.getX());
        
        Coordinate negCoord = new Coordinate(-5, 15);
        assertEquals(-5, negCoord.getX());
    }
    
    /**
     * Tests the getY method.
     */
    public void testGetY() {
        assertEquals(20, coord1.getY());
        assertEquals(40, coord3.getY());
        
        Coordinate negCoord = new Coordinate(15, -10);
        assertEquals(-10, negCoord.getY());
    }
    
    /**
     * Tests the equals method.
     */
    public void testEquals() {
        assertTrue(coord1.equals(coord2));
        assertTrue(coord2.equals(coord1));
        
        assertFalse(coord1.equals(coord3));
        assertFalse(coord3.equals(coord1));
        
        assertTrue(coord1.equals(coord1));
        
        assertFalse(coord1.equals(null));
        
        assertFalse(coord1.equals("Not a Coordinate"));
        
        Coordinate halfMatch1 = new Coordinate(10, 30);
        Coordinate halfMatch2 = new Coordinate(20, 20);
        assertFalse(coord1.equals(halfMatch1));
        assertFalse(coord1.equals(halfMatch2));
    }
    
    /**
     * Tests the hashCode method.
     */
    public void testHashCode() {
        assertEquals(coord1.hashCode(), coord2.hashCode());
        
        
        int expectedHash1 = 31 * 10 + 20;
        assertEquals(expectedHash1, coord1.hashCode());
        
        int expectedHash3 = 31 * 30 + 40;
        assertEquals(expectedHash3, coord3.hashCode());
        
        Coordinate negCoord = new Coordinate(-5, -10);
        int expectedNegHash = 31 * (-5) + (-10);
        assertEquals(expectedNegHash, negCoord.hashCode());
    }
    
    /**
     * Tests the toString method.
     */
    public void testToString() {
        assertEquals("(10, 20)", coord1.toString());
        assertEquals("(30, 40)", coord3.toString());
        
        Coordinate negCoord = new Coordinate(-5, -10);
        assertEquals("(-5, -10)", negCoord.toString());
        
        Coordinate zeroCoord = new Coordinate(0, 0);
        assertEquals("(0, 0)", zeroCoord.toString());
    }
    
    /**
     * Tests the constructor with various inputs.
     */
    public void testConstructor() {
        Coordinate largeCoord = new Coordinate(Integer.MAX_VALUE, Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, largeCoord.getX());
        assertEquals(Integer.MIN_VALUE, largeCoord.getY());
        
        Coordinate zeroCoord = new Coordinate(0, 0);
        assertEquals(0, zeroCoord.getX());
        assertEquals(0, zeroCoord.getY());
    }
}