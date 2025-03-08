import student.TestCase;

/**
 * This class tests the CoordinateList class.
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
        
        list.add(10, 20);
        assertEquals(1, list.size());
        
        list.add(30, 40);
        assertEquals(2, list.size());
        
        boolean result = list.add(10, 20);
        assertFalse(result);
        assertEquals(2, list.size());
    }
    
    /**
     * Tests the isEmpty method.
     */
    public void testIsEmpty() {
        assertTrue(list.isEmpty());
        
        list.add(5, 5);
        assertFalse(list.isEmpty());
    }
    
    /**
     * Tests the add method with Coordinate objects.
     */
    public void testAddCoordinate() {
        Coordinate coord1 = new Coordinate(15, 25);
        Coordinate coord2 = new Coordinate(35, 45);
        Coordinate coord3 = new Coordinate(15, 25);
        
        boolean result1 = list.add(coord1);
        assertTrue(result1);
        assertEquals(1, list.size());
        
        boolean result2 = list.add(coord2);
        assertTrue(result2);
        assertEquals(2, list.size());
        
        boolean result3 = list.add(coord3);
        assertFalse(result3);
        assertEquals(2, list.size());
    }
    
    /**
     * Tests the add method with x,y values.
     */
    public void testAddXY() {
        boolean result1 = list.add(15, 25);
        assertTrue(result1);
        assertEquals(1, list.size());
        
        boolean result2 = list.add(35, 45);
        assertTrue(result2);
        assertEquals(2, list.size());
        
        boolean result3 = list.add(15, 25);
        assertFalse(result3);
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
        
        Exception exception1 = null;
        try {
            list.get(-1);
        }
        catch (IndexOutOfBoundsException e) {
            exception1 = e;
        }
        assertNotNull(exception1);
        
        Exception exception2 = null;
        try {
            list.get(1);
        }
        catch (IndexOutOfBoundsException e) {
            exception2 = e;
        }
        assertNotNull(exception2);
        
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
        
        assertFalse(list.contains(coord1));
        assertFalse(list.contains(coord2));
        
        list.add(coord1);
        assertTrue(list.contains(coord1));
        assertFalse(list.contains(coord2));
        
        list.add(coord2);
        assertTrue(list.contains(coord1));
        assertTrue(list.contains(coord2));
        
        Coordinate coord3 = new Coordinate(15, 25); 
        assertTrue(list.contains(coord3));
    }
    
    /**
     * Tests the contains method with x,y values.
     */
    public void testContainsXY() {
        assertFalse(list.contains(15, 25));
        assertFalse(list.contains(35, 45));
        
        list.add(15, 25);
        assertTrue(list.contains(15, 25));
        assertFalse(list.contains(35, 45));
        
        list.add(35, 45);
        assertTrue(list.contains(15, 25));
        assertTrue(list.contains(35, 45));
    }
    
    /**
     * Tests the expandCapacity method by adding more elements
     * than the default capacity.
     */
    public void testExpandCapacity() {
        for (int i = 0; i < 15; i++) {
            boolean result = list.add(i, i * 2);
            assertTrue(result);
            assertEquals(i + 1, list.size());
        }
        
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
        for (int i = 0; i < 10; i++) {
            list.add(i, i);
        }
        assertEquals(10, list.size());
        
        list.add(10, 10);
        assertEquals(11, list.size());
        
        for (int i = 0; i < 11; i++) {
            assertTrue(list.contains(i, i));
        }
    }
    
    /**
     * Tests adding null coordinates - this specifically tests
     * for mutations that might bypass null checks.
     */
    public void testAddNull() {
        Exception exception = null;
        try {
            list.add(null);
        }
        catch (NullPointerException e) {
            exception = e;
        }
        
        if (exception == null) {
            assertEquals(1, list.size());
        }
    }
    
    /**
     * Test to ensure mutations to equality checks are caught.
     */
    public void testEqualityMutations() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 6); 
        Coordinate coord3 = new Coordinate(6, 5); 
        
        list.add(coord1);
        
        assertFalse(list.contains(coord2));
        assertFalse(list.contains(coord3));
        
        assertFalse(list.contains(5, 6)); 
        assertFalse(list.contains(6, 5));
        assertTrue(list.contains(5, 5)); 
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
        
        list.add(1, 1);
        assertEquals(2, list.size());
    }
}