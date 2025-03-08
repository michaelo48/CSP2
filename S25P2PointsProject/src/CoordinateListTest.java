import student.TestCase;

/**
 * This class tests the CoordinateList class.
 * Tests are designed to kill mutation operators.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class CoordinateListTest extends TestCase {
    
    private CoordinateList list;
    
    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        list = new CoordinateList();
    }
    
    /**
     * Tests the constructor and initial state.
     */
    public void testConstructor() {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }
    
    /**
     * Tests the size method.
     */
    public void testSize() {
        assertEquals(0, list.size());
        
        // Add some coordinates
        list.add(10, 20);
        assertEquals(1, list.size());
        
        list.add(30, 40);
        assertEquals(2, list.size());
        
        // Try adding a duplicate - size shouldn't change
        boolean result = list.add(10, 20);
        assertFalse(result);
        assertEquals(2, list.size());
    }
    
    /**
     * Tests the isEmpty method.
     */
    public void testIsEmpty() {
        // Initially empty
        assertTrue(list.isEmpty());
        
        // Not empty after adding
        list.add(5, 5);
        assertFalse(list.isEmpty());
    }
    
    /**
     * Tests the add method with Coordinate objects.
     */
    public void testAddCoordinate() {
        Coordinate coord1 = new Coordinate(15, 25);
        Coordinate coord2 = new Coordinate(35, 45);
        Coordinate coord3 = new Coordinate(15, 25); // Same as coord1
        
        // Add first coordinate
        boolean result1 = list.add(coord1);
        assertTrue(result1);
        assertEquals(1, list.size());
        
        // Add second coordinate
        boolean result2 = list.add(coord2);
        assertTrue(result2);
        assertEquals(2, list.size());
        
        // Try adding duplicate coordinate
        boolean result3 = list.add(coord3);
        assertFalse(result3); // Should not add duplicate
        assertEquals(2, list.size());
    }
    
    /**
     * Tests the add method with x,y values.
     */
    public void testAddXY() {
        // Add first coordinate
        boolean result1 = list.add(15, 25);
        assertTrue(result1);
        assertEquals(1, list.size());
        
        // Add second coordinate
        boolean result2 = list.add(35, 45);
        assertTrue(result2);
        assertEquals(2, list.size());
        
        // Try adding duplicate coordinate
        boolean result3 = list.add(15, 25);
        assertFalse(result3); // Should not add duplicate
        assertEquals(2, list.size());
    }
    
    /**
     * Tests the get method.
     */
    public void testGet() {
        list.add(10, 20);
        list.add(30, 40);
        
        Coordinate coord1 = list.get(0);
        assertEquals(10, coord1.getX());
        assertEquals(20, coord1.getY());
        
        Coordinate coord2 = list.get(1);
        assertEquals(30, coord2.getX());
        assertEquals(40, coord2.getY());
    }
    
    /**
     * Tests the get method with invalid indices.
     */
    public void testGetInvalidIndex() {
        list.add(10, 20);
        
        // Test negative index
        Exception exception1 = null;
        try {
            list.get(-1);
        }
        catch (IndexOutOfBoundsException e) {
            exception1 = e;
        }
        assertNotNull(exception1);
        
        // Test index equal to size
        Exception exception2 = null;
        try {
            list.get(1);
        }
        catch (IndexOutOfBoundsException e) {
            exception2 = e;
        }
        assertNotNull(exception2);
        
        // Test index greater than size
        Exception exception3 = null;
        try {
            list.get(2);
        }
        catch (IndexOutOfBoundsException e) {
            exception3 = e;
        }
        assertNotNull(exception3);
    }
    
    /**
     * Tests the contains method with Coordinate objects.
     */
    public void testContainsCoordinate() {
        Coordinate coord1 = new Coordinate(15, 25);
        Coordinate coord2 = new Coordinate(35, 45);
        
        // Initially, list doesn't contain any coordinates
        assertFalse(list.contains(coord1));
        assertFalse(list.contains(coord2));
        
        // Add coord1 and check again
        list.add(coord1);
        assertTrue(list.contains(coord1));
        assertFalse(list.contains(coord2));
        
        // Add coord2 and check again
        list.add(coord2);
        assertTrue(list.contains(coord1));
        assertTrue(list.contains(coord2));
        
        // Check for a similar but not identical coordinate
        Coordinate coord3 = new Coordinate(15, 25); // Same values as coord1
        assertTrue(list.contains(coord3));
    }
    
    /**
     * Tests the contains method with x,y values.
     */
    public void testContainsXY() {
        // Initially, list doesn't contain any coordinates
        assertFalse(list.contains(15, 25));
        assertFalse(list.contains(35, 45));
        
        // Add first coordinate and check again
        list.add(15, 25);
        assertTrue(list.contains(15, 25));
        assertFalse(list.contains(35, 45));
        
        // Add second coordinate and check again
        list.add(35, 45);
        assertTrue(list.contains(15, 25));
        assertTrue(list.contains(35, 45));
    }
    
    /**
     * Tests the expandCapacity method by adding more elements
     * than the default capacity.
     */
    public void testExpandCapacity() {
        // Default capacity is 10, so add 15 elements
        for (int i = 0; i < 15; i++) {
            boolean result = list.add(i, i * 2);
            assertTrue(result);
            assertEquals(i + 1, list.size());
        }
        
        // Verify all elements are still accessible
        for (int i = 0; i < 15; i++) {
            Coordinate coord = list.get(i);
            assertEquals(i, coord.getX());
            assertEquals(i * 2, coord.getY());
        }
    }
    
    /**
     * Tests boundary conditions for array expansion.
     */
    public void testExpandCapacityBoundary() {
        // Add exactly 10 elements (fill the initial capacity)
        for (int i = 0; i < 10; i++) {
            list.add(i, i);
        }
        assertEquals(10, list.size());
        
        // Add one more to trigger expansion
        list.add(10, 10);
        assertEquals(11, list.size());
        
        // Verify all elements are intact
        for (int i = 0; i < 11; i++) {
            assertTrue(list.contains(i, i));
        }
    }
    
    /**
     * Tests adding null coordinates - this specifically tests
     * for mutations that might bypass null checks.
     */
    public void testAddNull() {
        // This might throw NullPointerException depending on implementation
        // but we just want to make sure it behaves consistently
        Exception exception = null;
        try {
            list.add(null);
        }
        catch (NullPointerException e) {
            exception = e;
        }
        
        // If null is not allowed, exception should not be null
        // If null is allowed, size should increase
        if (exception == null) {
            assertEquals(1, list.size());
        }
    }
    
    /**
     * Test to ensure mutations to equality checks are caught.
     */
    public void testEqualityMutations() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 6); // Different y
        Coordinate coord3 = new Coordinate(6, 5); // Different x
        
        // Add first coordinate
        list.add(coord1);
        
        // These should be different
        assertFalse(list.contains(coord2));
        assertFalse(list.contains(coord3));
        
        // Explicitly testing boundary conditions for equality
        assertFalse(list.contains(5, 6)); // Different y
        assertFalse(list.contains(6, 5)); // Different x
        assertTrue(list.contains(5, 5));  // Same coordinates
    }
    
    /**
     * Test to catch mutations in the size increment.
     */
    public void testSizeIncrement() {
        assertEquals(0, list.size());
        
        list.add(1, 1);
        assertEquals(1, list.size());
        
        list.add(2, 2);
        assertEquals(2, list.size());
        
        // Try adding same coordinate - size shouldn't change
        list.add(1, 1);
        assertEquals(2, list.size());
    }
}