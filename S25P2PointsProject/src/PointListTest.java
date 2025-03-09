import student.TestCase;

/**
 * Tests the PointList class by verifying all its functionality.
 * 
 * @author You
 * @version 03.09.2025
 */
public class PointListTest extends TestCase {

    private PointList list;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        list = new PointList();
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

        Point p1 = new Point("P1", 10, 20);
        list.add(p1);
        assertEquals(1, list.size());

        Point p2 = new Point("P2", 30, 40);
        list.add(p2);
        assertEquals(2, list.size());
    }

    /**
     * Tests the isEmpty method.
     */
    public void testIsEmpty() {
        assertTrue(list.isEmpty());

        Point p = new Point("P", 5, 5);
        list.add(p);
        assertFalse(list.isEmpty());
    }

    /**
     * Tests the add method.
     */
    public void testAdd() {
        Point p1 = new Point("P1", 10, 20);
        Point p2 = new Point("P2", 30, 40);
        
        list.add(p1);
        assertEquals(1, list.size());
        assertEquals("P1", list.get(0).getName());
        
        list.add(p2);
        assertEquals(2, list.size());
        assertEquals("P2", list.get(1).getName());
    }

    /**
     * Tests the expandCapacity method by adding more elements
     * than the default capacity.
     */
    public void testExpandCapacity() {
        for (int i = 0; i < 15; i++) {
            Point p = new Point("P" + i, i, i * 2);
            list.add(p);
            assertEquals(i + 1, list.size());
        }
        
        for (int i = 0; i < 15; i++) {
            Point p = list.get(i);
            assertEquals("P" + i, p.getName());
            assertEquals(i, p.getX());
            assertEquals(i * 2, p.getY());
        }
    }

    /**
     * Tests the get method.
     */
    public void testGet() {
        Point p1 = new Point("P1", 10, 20);
        Point p2 = new Point("P2", 30, 40);
        
        list.add(p1);
        list.add(p2);
        
        Point retrieved1 = list.get(0);
        Point retrieved2 = list.get(1);
        
        assertEquals(p1.getName(), retrieved1.getName());
        assertEquals(p1.getX(), retrieved1.getX());
        assertEquals(p1.getY(), retrieved1.getY());
        
        assertEquals(p2.getName(), retrieved2.getName());
        assertEquals(p2.getX(), retrieved2.getX());
        assertEquals(p2.getY(), retrieved2.getY());
    }

    /**
     * Tests the get method with invalid indices.
     */
    public void testGetInvalidIndex() {
        Point p = new Point("P", 10, 20);
        list.add(p);
        
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
            list.get(100);
        }
        catch (IndexOutOfBoundsException e) {
            exception3 = e;
        }
        assertNotNull(exception3);
    }

    /**
     * Tests the remove method.
     */
    public void testRemove() {
        Point p1 = new Point("P1", 10, 20);
        Point p2 = new Point("P2", 30, 40);
        Point p3 = new Point("P3", 50, 60);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        assertEquals(3, list.size());
        
        Point removed = list.remove(1);
        assertEquals(2, list.size());
        assertEquals(p2.getName(), removed.getName());
        assertEquals(p2.getX(), removed.getX());
        assertEquals(p2.getY(), removed.getY());
        
        assertEquals("P1", list.get(0).getName());
        assertEquals("P3", list.get(1).getName());
        
        removed = list.remove(0);
        assertEquals(1, list.size());
        assertEquals(p1.getName(), removed.getName());
        
        removed = list.remove(0);
        assertEquals(0, list.size());
        assertEquals(p3.getName(), removed.getName());
    }

    /**
     * Tests the remove method with invalid indices.
     */
    public void testRemoveInvalidIndex() {
        Point p = new Point("P", 10, 20);
        list.add(p);
        
        Exception exception1 = null;
        try {
            list.remove(-1);
        }
        catch (IndexOutOfBoundsException e) {
            exception1 = e;
        }
        assertNotNull(exception1);
        
        Exception exception2 = null;
        try {
            list.remove(1);
        }
        catch (IndexOutOfBoundsException e) {
            exception2 = e;
        }
        assertNotNull(exception2);
        
        Exception exception3 = null;
        try {
            list.remove(100);
        }
        catch (IndexOutOfBoundsException e) {
            exception3 = e;
        }
        assertNotNull(exception3);
    }

    /**
     * Tests the addAll method.
     */
    public void testAddAll() {
        PointList otherList = new PointList();
        
        Point p1 = new Point("P1", 10, 20);
        Point p2 = new Point("P2", 30, 40);
        otherList.add(p1);
        otherList.add(p2);
        
        list.addAll(otherList);
        assertEquals(2, list.size());
        assertEquals("P1", list.get(0).getName());
        assertEquals("P2", list.get(1).getName());
        
        Point p3 = new Point("P3", 50, 60);
        otherList.add(p3);
        
        list.addAll(otherList);
        assertEquals(5, list.size());
        assertEquals("P1", list.get(0).getName());
        assertEquals("P2", list.get(1).getName());
        assertEquals("P1", list.get(2).getName());
        assertEquals("P2", list.get(3).getName());
        assertEquals("P3", list.get(4).getName());
    }

    /**
     * Tests the copy method.
     */
    public void testCopy() {
        Point p1 = new Point("P1", 10, 20);
        Point p2 = new Point("P2", 30, 40);
        
        list.add(p1);
        list.add(p2);
        
        PointList copy = list.copy();
        
        assertEquals(list.size(), copy.size());
        assertEquals(list.get(0).getName(), copy.get(0).getName());
        assertEquals(list.get(1).getName(), copy.get(1).getName());
        
        Point p3 = new Point("P3", 50, 60);
        list.add(p3);
        
        assertEquals(3, list.size());
        assertEquals(2, copy.size());
    }

    /**
     * Tests the clear method.
     */
    public void testClear() {
        Point p1 = new Point("P1", 10, 20);
        Point p2 = new Point("P2", 30, 40);
        
        list.add(p1);
        list.add(p2);
        assertEquals(2, list.size());
        
        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        
        Point p3 = new Point("P3", 50, 60);
        list.add(p3);
        assertEquals(1, list.size());
        assertEquals("P3", list.get(0).getName());
    }

    /**
     * Tests the null handling capability.
     */
    public void testNullHandling() {
        try {
            list.add(null);
            Point retrieved = list.get(0);
            assertNull(retrieved);
        }
        catch (NullPointerException e) {
            // This is also acceptable behavior
        }
    }

    /**
     * Tests edge cases with large numbers of elements.
     */
    public void testLargeNumberOfElements() {
        int count = 1000;
        for (int i = 0; i < count; i++) {
            Point p = new Point("P" + i, i, i);
            list.add(p);
        }
        
        assertEquals(count, list.size());
        
        for (int i = 0; i < count; i++) {
            Point p = list.get(i);
            assertEquals("P" + i, p.getName());
            assertEquals(i, p.getX());
            assertEquals(i, p.getY());
        }
    }
}