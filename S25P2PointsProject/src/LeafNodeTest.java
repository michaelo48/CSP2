import student.TestCase;

/**
 * Tests the LeafNode class using mutation testing techniques.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class LeafNodeTest extends TestCase {

    private LeafNode leafNode;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        leafNode = new LeafNode();
    }


    /**
     * Tests the constructor to ensure it properly initializes an empty leaf
     * node.
     */
    public void testConstructor() {
        assertEquals(0, leafNode.getPoints().size());
    }


    /**
     * Tests the insert method with a single point.
     */
    public void testInsertSinglePoint() {
        Point point = new Point("SinglePoint", 100, 200);
        QuadNode result = leafNode.insert(point, 0, 0, 1024);

        assertNotNull(result);
        assertSame(leafNode, result);
        assertEquals(1, leafNode.getPoints().size());
        assertEquals("SinglePoint", leafNode.getPoints().get(0).getName());
    }


    /**
     * Tests the insert method with multiple points at the same location.
     */
    public void testInsertMultiplePointsSameLocation() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 100, 200);
        Point p3 = new Point("P3", 100, 200);
        Point p4 = new Point("P4", 100, 200);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);
        leafNode.insert(p3, 0, 0, 1024);
        QuadNode result = leafNode.insert(p4, 0, 0, 1024);

        assertNotNull(result);
        assertSame(leafNode, result);
        assertEquals(4, leafNode.getPoints().size());
    }


    /**
     * Tests the insert method that causes a split.
     */
    public void testInsertCausingSplit() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);
        Point p3 = new Point("P3", 500, 600);
        Point p4 = new Point("P4", 700, 800);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);
        leafNode.insert(p3, 0, 0, 1024);
        QuadNode result = leafNode.insert(p4, 0, 0, 1024);

        assertNotNull(result);
        assertNotSame(leafNode, result);
        assertTrue(result instanceof InternalNode);
    }


    /**
     * Tests the remove method with coordinates.
     */
    public void testRemoveByCoordinates() {
        Point point = new Point("ToRemove", 100, 200);
        leafNode.insert(point, 0, 0, 1024);

        KVPair<QuadNode, Point> result = leafNode.remove(100, 200, 0, 0, 1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());
        assertTrue(result.key() instanceof EmptyNode);
        assertEquals(0, leafNode.getPoints().size());
    }


    /**
     * Tests the remove method with coordinates, removing one of multiple
     * points.
     */
    public void testRemoveByCoordinatesOneOfMany() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);

        KVPair<QuadNode, Point> result = leafNode.remove(100, 200, 0, 0, 1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("P1", result.value().getName());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
        assertEquals("P2", leafNode.getPoints().get(0).getName());
    }


    /**
     * Tests the remove method with coordinates, when point doesn't exist.
     */
    public void testRemoveByCoordinatesNonExistent() {
        Point point = new Point("Point", 100, 200);
        leafNode.insert(point, 0, 0, 1024);

        KVPair<QuadNode, Point> result = leafNode.remove(300, 400, 0, 0, 1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
    }


    /**
     * Tests the remove method with a name.
     */
    public void testRemoveByName() {
        Point point = new Point("ToRemove", 100, 200);
        leafNode.insert(point, 0, 0, 1024);

        KVPair<QuadNode, Point> result = leafNode.remove("ToRemove", 0, 0,
            1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());
        assertTrue(result.key() instanceof EmptyNode);
        assertEquals(0, leafNode.getPoints().size());
    }


    /**
     * Tests the remove method with a name, removing one of multiple points.
     */
    public void testRemoveByNameOneOfMany() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);

        KVPair<QuadNode, Point> result = leafNode.remove("P1", 0, 0, 1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("P1", result.value().getName());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
        assertEquals("P2", leafNode.getPoints().get(0).getName());
    }


    /**
     * Tests the remove method with a name, when point doesn't exist.
     */
    public void testRemoveByNameNonExistent() {
        Point point = new Point("Point", 100, 200);
        leafNode.insert(point, 0, 0, 1024);

        KVPair<QuadNode, Point> result = leafNode.remove("NonExistent", 0, 0,
            1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
    }


    /**
     * Tests the regionsearch method.
     */
    public void testRegionsearch() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);
        Point p3 = new Point("P3", 500, 600);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);
        leafNode.insert(p3, 0, 0, 1024);

        PointList results = new PointList();
        int visited = leafNode.regionsearch(90, 190, 100, 100, 0, 0, 1024,
            results);

        assertEquals(1, visited);
        assertEquals(1, results.size());
        assertEquals("P1", results.get(0).getName());
    }


    /**
     * Tests the regionsearch method with no matches.
     */
    public void testRegionsearchNoMatches() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);

        PointList results = new PointList();
        int visited = leafNode.regionsearch(900, 900, 100, 100, 0, 0, 1024,
            results);

        assertEquals(1, visited);
        assertEquals(0, results.size());
    }


    /**
     * Tests the findDuplicates method with duplicate points.
     */
    public void testFindDuplicatesWithDuplicates() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 100, 200);
        Point p3 = new Point("P3", 300, 400);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);
        leafNode.insert(p3, 0, 0, 1024);

        CoordinateList duplicates = new CoordinateList();
        int visited = leafNode.findDuplicates(0, 0, 1024, duplicates);

        assertEquals(1, visited);
        assertEquals(1, duplicates.size());
        assertEquals(100, duplicates.get(0).getX());
        assertEquals(200, duplicates.get(0).getY());
    }


    /**
     * Tests the findDuplicates method without duplicate points.
     */
    public void testFindDuplicatesWithoutDuplicates() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);
        Point p3 = new Point("P3", 500, 600);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);
        leafNode.insert(p3, 0, 0, 1024);

        CoordinateList duplicates = new CoordinateList();
        int visited = leafNode.findDuplicates(0, 0, 1024, duplicates);

        assertEquals(1, visited);
        assertEquals(0, duplicates.size());
    }


    /**
     * Tests the isEmpty method.
     */
    public void testIsEmpty() {
        assertFalse(leafNode.isEmpty());
    }


    /**
     * Tests the compareTo method with various node types.
     */
    public void testCompareTo() {
        EmptyNode emptyNode = EmptyNode.getInstance();
        assertEquals(1, leafNode.compareTo(emptyNode));

        InternalNode internalNode = new InternalNode();
        assertEquals(-1, leafNode.compareTo(internalNode));

        LeafNode otherLeafNode = new LeafNode();
        assertEquals(0, leafNode.compareTo(otherLeafNode));

        Point point = new Point("P1", 100, 200);
        leafNode.insert(point, 0, 0, 1024);

        assertEquals(1, leafNode.compareTo(otherLeafNode));
        assertEquals(-1, otherLeafNode.compareTo(leafNode));
    }


    /**
     * Tests the getPoints method.
     */
    public void testGetPoints() {
        Point p1 = new Point("P1", 100, 200);
        leafNode.insert(p1, 0, 0, 1024);

        PointList points = leafNode.getPoints();

        assertNotNull(points);
        assertEquals(1, points.size());
        assertEquals("P1", points.get(0).getName());
    }


    /**
     * Tests the shouldSplit method when the node contains more than 3 points
     * at different locations.
     */
    public void testShouldSplitWithMoreThanThreePointsDifferentLocations() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);
        Point p3 = new Point("P3", 500, 600);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);
        leafNode.insert(p3, 0, 0, 1024);

        QuadNode result = leafNode.insert(new Point("P4", 700, 800), 0, 0,
            1024);

        assertTrue(result instanceof InternalNode);
    }


    /**
     * Tests the edge case when there are 3 points at different locations.
     */
    public void testThreePointsDifferentLocations() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);
        Point p3 = new Point("P3", 500, 600);

        leafNode.insert(p1, 0, 0, 1024);
        leafNode.insert(p2, 0, 0, 1024);
        QuadNode result = leafNode.insert(p3, 0, 0, 1024);

        assertSame(leafNode, result);
        assertEquals(3, leafNode.getPoints().size());
    }
}
