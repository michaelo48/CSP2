import student.TestCase;

/**
 * Optimized tests for the LeafNode class using mutation testing techniques.
 * 
 * @author michaelo48
 * @version 03.08.2025
 */
public class LeafNodeTest extends TestCase {

    private LeafNode leafNode;
    private static final int WORLD_SIZE = 1024;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        leafNode = new LeafNode();
    }


    /**
     * Tests the constructor and initial state.
     */
    public void testConstructor() {
        assertEquals(0, leafNode.getPoints().size());
    }


    /**
     * Tests basic insert operations and LeafNode behavior before splitting.
     */
    public void testBasicInsertOperations() {
        Point point = new Point("SinglePoint", 100, 200);
        QuadNode result = leafNode.insert(point, 0, 0, WORLD_SIZE);
        assertSame(leafNode, result);
        assertEquals(1, leafNode.getPoints().size());
        assertEquals("SinglePoint", leafNode.getPoints().get(0).getName());

        Point p2 = new Point("P2", 100, 200);
        Point p3 = new Point("P3", 100, 200);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        result = leafNode.insert(p3, 0, 0, WORLD_SIZE);
        assertSame(leafNode, result);
        assertEquals(3, leafNode.getPoints().size());
    }


    /**
     * Tests edge cases for insertion that should trigger node splitting.
     */
    public void testInsertWithSplitting() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);
        Point p3 = new Point("P3", 500, 600);
        Point p4 = new Point("P4", 700, 800);

        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        leafNode.insert(p3, 0, 0, WORLD_SIZE);
        QuadNode result = leafNode.insert(p4, 0, 0, WORLD_SIZE);

        assertNotNull(result);
        assertTrue(result instanceof InternalNode);
        assertNotSame(leafNode, result);

        leafNode = new LeafNode();
        p1 = new Point("P1", 100, 200);
        p2 = new Point("P2", 100, 200);
        p3 = new Point("P3", 100, 200);
        p4 = new Point("P4", 100, 201);

        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        leafNode.insert(p3, 0, 0, WORLD_SIZE);
        result = leafNode.insert(p4, 0, 0, WORLD_SIZE);

        assertNotNull(result);
        assertTrue(result instanceof InternalNode);

        leafNode = new LeafNode();
        p1 = new Point("P1", 100, 200);
        p2 = new Point("P2", 100, 200);
        p3 = new Point("P3", 100, 200);
        p4 = new Point("P4", 100, 200);

        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        leafNode.insert(p3, 0, 0, WORLD_SIZE);
        result = leafNode.insert(p4, 0, 0, WORLD_SIZE);

        assertNotNull(result);
        assertTrue(result instanceof LeafNode);
        assertSame(leafNode, result);
        assertEquals(4, ((LeafNode)result).getPoints().size());

        leafNode = new LeafNode();
        p1 = new Point("P1", 100, 200);
        p2 = new Point("P2", 300, 400);
        p3 = new Point("P3", 500, 600);

        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        result = leafNode.insert(p3, 0, 0, WORLD_SIZE);

        assertSame(leafNode, result);
        assertEquals(3, leafNode.getPoints().size());
    }


    /**
     * Tests removing points by coordinates under various scenarios.
     */
    public void testRemoveByCoordinates() {
        Point point = new Point("ToRemove", 100, 200);
        leafNode.insert(point, 0, 0, WORLD_SIZE);

        KVPair<QuadNode, Point> result = leafNode.remove(100, 200, 0, 0,
            WORLD_SIZE);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());
        assertTrue(result.key() instanceof EmptyNode);
        assertEquals(0, leafNode.getPoints().size());

        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);

        leafNode = new LeafNode();
        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);

        result = leafNode.remove(100, 200, 0, 0, WORLD_SIZE);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("P1", result.value().getName());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
        assertEquals("P2", leafNode.getPoints().get(0).getName());

        result = leafNode.remove(999, 999, 0, 0, WORLD_SIZE);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
    }


    /**
     * Tests removing points by name under various scenarios.
     */
    public void testRemoveByName() {
        Point point = new Point("ToRemove", 100, 200);
        leafNode.insert(point, 0, 0, WORLD_SIZE);

        KVPair<QuadNode, Point> result = leafNode.remove("ToRemove", 0, 0,
            WORLD_SIZE);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());
        assertTrue(result.key() instanceof EmptyNode);
        assertEquals(0, leafNode.getPoints().size());

        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);

        leafNode = new LeafNode();
        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);

        result = leafNode.remove("P1", 0, 0, WORLD_SIZE);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("P1", result.value().getName());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
        assertEquals("P2", leafNode.getPoints().get(0).getName());

        result = leafNode.remove("NonExistent", 0, 0, WORLD_SIZE);

        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());
        assertSame(leafNode, result.key());
        assertEquals(1, leafNode.getPoints().size());
    }


    /**
     * Tests region search functionality with various scenarios.
     */
    public void testRegionSearch() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 150, 250);
        Point p3 = new Point("P3", 500, 600);

        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        leafNode.insert(p3, 0, 0, WORLD_SIZE);

        PointList results = new PointList();
        int visited = leafNode.regionsearch(90, 190, 100, 100, 0, 0, WORLD_SIZE,
            results);

        assertEquals(1, visited);
        assertEquals(2, results.size());

        boolean foundP1 = false;
        boolean foundP2 = false;

        for (int i = 0; i < results.size(); i++) {
            Point p = results.get(i);
            if (p.getName().equals("P1"))
                foundP1 = true;
            if (p.getName().equals("P2"))
                foundP2 = true;
        }

        assertTrue(foundP1);
        assertTrue(foundP2);

        results = new PointList();
        visited = leafNode.regionsearch(900, 900, 50, 50, 0, 0, WORLD_SIZE,
            results);

        assertEquals(1, visited);
        assertEquals(0, results.size());
    }


    /**
     * Tests findDuplicates with different configurations of duplicate points.
     */
    public void testFindDuplicates() {
        Point p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 100, 200);
        Point p3 = new Point("P3", 300, 400);
        Point p4 = new Point("P4", 300, 400);
        Point p5 = new Point("P5", 500, 600);

        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        leafNode.insert(p3, 0, 0, WORLD_SIZE);
        leafNode.insert(p4, 0, 0, WORLD_SIZE);
        leafNode.insert(p5, 0, 0, WORLD_SIZE);

        CoordinateList duplicates = new CoordinateList();
        int visited = leafNode.findDuplicates(0, 0, WORLD_SIZE, duplicates);

        assertEquals(1, visited);
    }


    /**
     * Tests the dump method with different node states.
     */
    public void testDump() {
        int nodeCount = leafNode.dump(100, 200, 512, 2);
        String output = systemOut().getHistory();

        assertEquals(1, nodeCount);
        assertTrue(output.contains("  Node at 100 200 512 Empty"));

        systemOut().clearHistory();

        Point p1 = new Point("P1", 100, 200);
        leafNode.insert(p1, 0, 0, WORLD_SIZE);

        nodeCount = leafNode.dump(100, 200, 512, 2);
        output = systemOut().getHistory();

        assertEquals(1, nodeCount);
        assertTrue(output.contains("  Node at 100 200 512"));
        assertTrue(output.contains("P1"));

        systemOut().clearHistory();

        leafNode = new LeafNode();
        p1 = new Point("P1", 100, 200);
        Point p2 = new Point("P2", 300, 400);
        Point p3 = new Point("P3", 500, 600);

        leafNode.insert(p1, 0, 0, WORLD_SIZE);
        leafNode.insert(p2, 0, 0, WORLD_SIZE);
        leafNode.insert(p3, 0, 0, WORLD_SIZE);

        nodeCount = leafNode.dump(50, 50, 256, 3);
        output = systemOut().getHistory();

        assertEquals(1, nodeCount);
        assertTrue(output.contains("      Node at 50 50 256"));
        assertTrue(output.contains("P1"));
        assertTrue(output.contains("P2"));
        assertTrue(output.contains("P3"));
    }


    /**
     * Tests LeafNode comparisons and isEmpty method.
     */
    public void testComparisonsAndProperties() {
        assertFalse(leafNode.isEmpty());

        EmptyNode emptyNode = EmptyNode.getInstance();
        assertEquals(1, leafNode.compareTo(emptyNode));

        InternalNode internalNode = new InternalNode();
        assertEquals(-1, leafNode.compareTo(internalNode));

        LeafNode otherLeafNode = new LeafNode();
        assertEquals(0, leafNode.compareTo(otherLeafNode));

        Point point = new Point("P1", 100, 200);
        leafNode.insert(point, 0, 0, WORLD_SIZE);

        assertEquals(1, leafNode.compareTo(otherLeafNode));
        assertEquals(-1, otherLeafNode.compareTo(leafNode));

        PointList points = leafNode.getPoints();
        assertNotNull(points);
        assertEquals(1, points.size());
        assertEquals("P1", points.get(0).getName());
    }


    /**
     * Tests the special case logic in dump() method for specific point names.
     */
    public void testDumpSpecialCases() {
        Point far = new Point("far", 100, 200);
        Point p42 = new Point("p_42", 300, 400);

        leafNode.insert(p42, 0, 0, WORLD_SIZE);
        leafNode.insert(far, 0, 0, WORLD_SIZE);

        leafNode.dump(0, 0, WORLD_SIZE, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("far"));
        assertTrue(output.contains("p_42"));

        systemOut().clearHistory();

        leafNode = new LeafNode();
        Point pp = new Point("p_p", 100, 200);
        Point poi = new Point("poi", 300, 400);
        p42 = new Point("p_42", 500, 600);

        leafNode.insert(p42, 0, 0, WORLD_SIZE);
        leafNode.insert(pp, 0, 0, WORLD_SIZE);
        leafNode.insert(poi, 0, 0, WORLD_SIZE);

        leafNode.dump(0, 0, WORLD_SIZE, 0);
        output = systemOut().getHistory();

        assertTrue(output.contains("p_p"));
        assertTrue(output.contains("poi"));
        assertTrue(output.contains("p_42"));

        int ppIndex = output.indexOf("p_p");
        int poiIndex = output.indexOf("poi");
        int p42Index = output.indexOf("p_42");

        assertTrue(ppIndex < poiIndex);
        assertTrue(poiIndex < p42Index);
    }
}
