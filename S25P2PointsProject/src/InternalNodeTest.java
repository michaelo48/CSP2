import student.TestCase;

/**
 * Tests the InternalNode class using mutation testing techniques.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class InternalNodeTest extends TestCase {

    private InternalNode internalNode;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        internalNode = new InternalNode();
    }


    /**
     * Tests the constructor to ensure it properly initializes with
     * four empty children.
     */
    public void testConstructor() {
        internalNode.dump(0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("Internal"));

        int emptyCount = 0;
        int index = 0;
        while ((index = output.indexOf("Empty", index)) != -1) {
            emptyCount++;
            index++;
        }

        assertEquals(4, emptyCount);
    }


    /**
     * Tests inserting multiple points and merging when appropriate.
     */
    public void testInsertAndMerge() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 700, 100);
        Point p3 = new Point("P3", 100, 700);

        internalNode.insert(p1, 0, 0, 1024);
        internalNode.insert(p2, 0, 0, 1024);
        internalNode.insert(p3, 0, 0, 1024);

        QuadNode result = internalNode;
        assertSame(internalNode, result);

        Point p4 = new Point("P4", 700, 700);
        Point p5 = new Point("P5", 150, 150);

        result = internalNode.insert(p4, 0, 0, 1024);
        assertSame(internalNode, result);

        result = internalNode.insert(p5, 0, 0, 1024);
        assertSame(internalNode, result);
    }


    /**
     * Tests the merge functionality with points at the same coordinates.
     */
    public void testMergeWithSameCoordinates() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 700, 100);
        Point p3 = new Point("P3", 100, 700);

        internalNode.insert(p1, 0, 0, 1024);
        internalNode.insert(p2, 0, 0, 1024);
        internalNode.insert(p3, 0, 0, 1024);

        Point p4 = new Point("P4", 100, 100);
        QuadNode result = internalNode.insert(p4, 0, 0, 1024);
        assertSame(internalNode, result);
    }


    /**
     * Tests the remove method with coordinates.
     */
    public void testRemoveByCoordinates() {
        Point point = new Point("ToRemove", 200, 200);
        internalNode.insert(point, 0, 0, 1024);

        KVPair<QuadNode, Point> result = internalNode.remove(200, 200, 0, 0,
            1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());
    }


    /**
     * Tests the remove method removing a point that doesn't exist.
     */
    public void testRemoveNonExistentByCoordinates() {
        KVPair<QuadNode, Point> result = internalNode.remove(200, 200, 0, 0,
            1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
    }


    /**
     * Tests the remove method with a name.
     */
    public void testRemoveByName() {
        Point point = new Point("ToRemove", 200, 200);
        internalNode.insert(point, 0, 0, 1024);

        KVPair<QuadNode, Point> result = internalNode.remove("ToRemove", 0, 0,
            1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());
    }


    /**
     * Tests the remove method removing a point by name that doesn't exist.
     */
    public void testRemoveNonExistentByName() {
        KVPair<QuadNode, Point> result = internalNode.remove("NonExistent", 0,
            0, 1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
    }


    /**
     * Tests removing a point and then merging the node.
     */
    public void testRemoveAndMerge() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 700, 100);
        Point p3 = new Point("P3", 100, 700);

        internalNode.insert(p1, 0, 0, 1024);
        internalNode.insert(p2, 0, 0, 1024);
        internalNode.insert(p3, 0, 0, 1024);

        KVPair<QuadNode, Point> result = internalNode.remove("P2", 0, 0, 1024);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("P2", result.value().getName());
    }


    /**
     * Tests the regionsearch method.
     */
    public void testRegionsearch() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 700, 100);
        Point p3 = new Point("P3", 100, 700);

        internalNode.insert(p1, 0, 0, 1024);
        internalNode.insert(p2, 0, 0, 1024);
        internalNode.insert(p3, 0, 0, 1024);

        PointList results = new PointList();
        int visited = internalNode.regionsearch(50, 50, 100, 100, 0, 0, 1024,
            results);

        assertTrue(visited >= 1);
        assertEquals(1, results.size());
        assertEquals("P1", results.get(0).getName());
    }


    /**
     * Tests the regionsearch method with no intersection.
     */
    public void testRegionsearchNoIntersection() {
        Point p1 = new Point("P1", 100, 100);
        internalNode.insert(p1, 0, 0, 1024);

        PointList results = new PointList();
        int visited = internalNode.regionsearch(500, 500, 100, 100, 0, 0, 1024,
            results);

        assertTrue(visited >= 1);
        assertEquals(0, results.size());
    }


    /**
     * Tests the findDuplicates method.
     */
    public void testFindDuplicates() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 100, 100);
        Point p3 = new Point("P3", 300, 300);

        internalNode.insert(p1, 0, 0, 1024);
        internalNode.insert(p2, 0, 0, 1024);
        internalNode.insert(p3, 0, 0, 1024);

        CoordinateList duplicates = new CoordinateList();
        int visited = internalNode.findDuplicates(0, 0, 1024, duplicates);

        assertTrue(visited >= 1);
        assertEquals(1, duplicates.size());
        assertEquals(100, duplicates.get(0).getX());
        assertEquals(100, duplicates.get(0).getY());
    }


    /**
     * Tests the dump method.
     */
    public void testDump() {
        Point p1 = new Point("P1", 100, 100);
        internalNode.insert(p1, 0, 0, 1024);

        internalNode.dump(2);
        String output = systemOut().getHistory();

        assertTrue(output.contains("    Internal"));
        assertTrue(output.contains("P1"));
    }


    /**
     * Tests the isEmpty method.
     */
    public void testIsEmpty() {
        assertFalse(internalNode.isEmpty());
    }


    /**
     * Tests the compareTo method with various node types.
     */
    public void testCompareTo() {
        EmptyNode emptyNode = EmptyNode.getInstance();
        assertEquals(1, internalNode.compareTo(emptyNode));

        LeafNode leafNode = new LeafNode();
        assertEquals(1, internalNode.compareTo(leafNode));

        InternalNode otherInternalNode = new InternalNode();
        assertEquals(0, internalNode.compareTo(otherInternalNode));
    }


    /**
     * Tests boundary conditions for the quadrant determination.
     */
    public void testQuadrantBoundaries() {
        int midX = 512;
        int midY = 512;

        Point nwCorner = new Point("NWCorner", 0, 0);
        Point neCorner = new Point("NECorner", 1023, 0);
        Point swCorner = new Point("SWCorner", 0, 1023);
        Point seCorner = new Point("SECorner", 1023, 1023);

        Point midPoint = new Point("Mid", midX, midY);

        internalNode.insert(nwCorner, 0, 0, 1024);
        internalNode.insert(neCorner, 0, 0, 1024);
        internalNode.insert(swCorner, 0, 0, 1024);
        internalNode.insert(seCorner, 0, 0, 1024);
        internalNode.insert(midPoint, 0, 0, 1024);

        internalNode.dump(0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("NWCorner"));
        assertTrue(output.contains("NECorner"));
        assertTrue(output.contains("SWCorner"));
        assertTrue(output.contains("SECorner"));
        assertTrue(output.contains("Mid"));
    }


    /**
     * Tests a complex scenario with many points and operations.
     */
    public void testComplexScenario() {
        for (int i = 0; i < 10; i++) {
            Point p = new Point("P" + i, i * 100, i * 50);
            internalNode.insert(p, 0, 0, 1024);
        }

        internalNode.remove("P3", 0, 0, 1024);
        internalNode.remove(700, 350, 0, 0, 1024);

        PointList results = new PointList();
        internalNode.regionsearch(0, 0, 500, 500, 0, 0, 1024, results);

        assertTrue(results.size() > 0);

        CoordinateList duplicates = new CoordinateList();
        internalNode.findDuplicates(0, 0, 1024, duplicates);

        internalNode.dump(0);
        String output = systemOut().getHistory();

        assertFalse(output.contains("P3"));
        assertFalse(output.contains("P7"));
    }
}
