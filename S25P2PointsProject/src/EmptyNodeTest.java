import student.TestCase;

/**
 * Tests the EmptyNode class using mutation testing techniques.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class EmptyNodeTest extends TestCase {

    private EmptyNode emptyNode;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        emptyNode = EmptyNode.getInstance();
    }


    /**
     * Tests the singleton pattern implementation.
     */
    public void testGetInstance() {
        EmptyNode instance1 = EmptyNode.getInstance();
        EmptyNode instance2 = EmptyNode.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }


    /**
     * Tests the insert method.
     */
    public void testInsert() {
        Point point = new Point("TestPoint", 100, 200);
        QuadNode result = emptyNode.insert(point, 0, 0, 1024);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }


    /**
     * Tests the remove method with x,y coordinates.
     */
    public void testRemoveByCoordinates() {
        KVPair<QuadNode, Point> result = emptyNode.remove(100, 200, 0, 0, 1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
        assertTrue(result.key().isEmpty());
        assertSame(emptyNode, result.key());
    }


    /**
     * Tests the remove method with a name.
     */
    public void testRemoveByName() {
        KVPair<QuadNode, Point> result = emptyNode.remove("TestPoint", 0, 0,
            1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
        assertTrue(result.key().isEmpty());
        assertSame(emptyNode, result.key());
    }


    /**
     * Tests the regionsearch method.
     */
    public void testRegionsearch() {
        PointList results = new PointList();
        int visited = emptyNode.regionsearch(50, 50, 100, 100, 0, 0, 1024,
            results);

        assertEquals(1, visited);
        assertEquals(0, results.size());
    }


    /**
     * Tests the findDuplicates method.
     */
    public void testFindDuplicates() {
        CoordinateList duplicates = new CoordinateList();
        int visited = emptyNode.findDuplicates(0, 0, 1024, duplicates);

        assertEquals(1, visited);
        assertEquals(0, duplicates.size());
    }


    /**
     * Tests the dump method.
     */
    public void testDump() {
        int nodesPrinted = emptyNode.dump(0, 0, 1024, 2);
        String output = systemOut().getHistory();

        assertEquals(1, nodesPrinted);
        assertTrue(output.contains("    Node at 0 0 1024 Empty"));
    }


    /**
     * Tests the isEmpty method.
     */
    public void testIsEmpty() {
        assertTrue(emptyNode.isEmpty());
    }


    /**
     * Tests the compareTo method with various node types.
     */
    public void testCompareTo() {
        assertEquals(0, emptyNode.compareTo(emptyNode));

        LeafNode leafNode = new LeafNode();
        leafNode.insert(new Point("TestPoint", 100, 200), 0, 0, 1024);

        assertEquals(-1, emptyNode.compareTo(leafNode));

        assertTrue(leafNode.compareTo(emptyNode) > 0);
    }


    /**
     * Tests edge cases of parameters.
     */
    public void testParameterEdgeCases() {
        PointList results = new PointList();
        int visited = emptyNode.regionsearch(Integer.MAX_VALUE,
            Integer.MAX_VALUE, 100, 100, 0, 0, 1024, results);

        assertEquals(1, visited);
        assertEquals(0, results.size());

        visited = emptyNode.regionsearch(50, 50, 100, 100, 0, 0, 1, results);
        assertEquals(1, visited);
        assertEquals(0, results.size());
    }


    /**
     * Tests boundary values for the quadtree structure.
     */
    public void testBoundaryValues() {
        PointList results = new PointList();
        int visited = emptyNode.regionsearch(50, 50, 100, 100, 0, 0, 0,
            results);
        assertEquals(1, visited);

        visited = emptyNode.regionsearch(50, 50, 100, 100, 0, 0, -1, results);
        assertEquals(1, visited);
    }
}