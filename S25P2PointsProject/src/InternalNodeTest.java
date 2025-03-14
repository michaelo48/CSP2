import student.TestCase;

/**
 * Tests the InternalNode class with enhanced test cases for mutation testing.
 * Optimized to remove redundant test conditions.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class InternalNodeTest extends TestCase {

    private InternalNode internalNode;
    private static final int WORLD_SIZE = 1024;
    private static final int DEFAULT_CHILD_SIZE = 512;
    private static final int DEFAULT_CHILD_X = 0;
    private static final int DEFAULT_CHILD_Y = 0;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        internalNode = new InternalNode();
    }


    /**
     * Tests the constructor and basic properties.
     */
    public void testConstructor() {
        int nodeCount = internalNode.dump(0, 0, 1024, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("Node at 0 0 1024 Internal"));
        assertEquals(5, nodeCount);
        assertFalse(internalNode.isEmpty());

        int emptyCount = countSubstrings(output, "Empty");
        assertEquals(4, emptyCount);
    }


    private int countSubstrings(String str, String subStr) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index++;
        }
        return count;
    }


    /**
     * Tests the insert method with points in all quadrants.
     */
    public void testInsert() {
        insertPointInAllQuadrants();

        systemOut().clearHistory();
        int nodeCount = internalNode.dump(0, 0, 1024, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("NW"));
        assertTrue(output.contains("NE"));
        assertTrue(output.contains("SW"));
        assertTrue(output.contains("SE"));
        assertEquals(5, nodeCount);

        internalNode = new InternalNode();
        for (int i = 0; i < 4; i++) {
            Point p = new Point("P" + i, 100 + i * 30, 100 + i * 30);
            internalNode.insert(p, 0, 0, 1024);
        }

        systemOut().clearHistory();
        nodeCount = internalNode.dump(0, 0, 1024, 0);
        assertTrue(nodeCount > 5);

        internalNode = new InternalNode();
        for (int i = 0; i < 4; i++) {
            Point p = new Point("SamePos" + i, 100, 100);
            internalNode.insert(p, 0, 0, 1024);
        }

        systemOut().clearHistory();
        nodeCount = internalNode.dump(0, 0, 1024, 0);
        output = systemOut().getHistory();

        assertEquals(5, nodeCount);
        for (int i = 0; i < 4; i++) {
            assertTrue(output.contains("SamePos" + i));
        }
    }


    private void insertPointInAllQuadrants() {
        internalNode.insert(new Point("NW", 100, 100), 0, 0, 1024);
        internalNode.insert(new Point("NE", 800, 100), 0, 0, 1024);
        internalNode.insert(new Point("SW", 100, 800), 0, 0, 1024);
        internalNode.insert(new Point("SE", 800, 800), 0, 0, 1024);
    }


    /**
     * Tests the insertIntoChild method with comprehensive scenarios.
     */
    public void testInsertIntoChild() {
        Point point = new Point("EmptyTest", 100, 100);
        internalNode.insertIntoChild(point, 0, DEFAULT_CHILD_X, DEFAULT_CHILD_Y,
            DEFAULT_CHILD_SIZE);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("EmptyTest"));
        assertFalse(output.contains("Node at 0 0 512 Internal"));

        internalNode = new InternalNode();
        for (int i = 0; i < 4; i++) {
            internalNode.insertIntoChild(new Point("SamePos" + i, 100, 100), 0,
                DEFAULT_CHILD_X, DEFAULT_CHILD_Y, DEFAULT_CHILD_SIZE);
        }

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        output = systemOut().getHistory();

        for (int i = 0; i < 4; i++) {
            assertTrue(output.contains("SamePos" + i));
        }

        internalNode = new InternalNode();
        for (int i = 0; i < 4; i++) {
            internalNode.insertIntoChild(new Point("DiffPos" + i, 100 + i * 10,
                100 + i * 10), 0, DEFAULT_CHILD_X, DEFAULT_CHILD_Y,
                DEFAULT_CHILD_SIZE);
        }

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        output = systemOut().getHistory();

        for (int i = 0; i < 4; i++) {
            assertTrue(output.contains("DiffPos" + i));
        }
        assertTrue(output.contains("Node at 0 0 512 Internal"));

        internalNode = new InternalNode();
        Point nw = new Point("SubNW", 100, 100);
        Point ne = new Point("SubNE", 220, 100);
        Point sw = new Point("SubSW", 100, 220);
        Point se = new Point("SubSE", 220, 220);

        internalNode.insertIntoChild(nw, 0, DEFAULT_CHILD_X, DEFAULT_CHILD_Y,
            DEFAULT_CHILD_SIZE);
        internalNode.insertIntoChild(ne, 0, DEFAULT_CHILD_X, DEFAULT_CHILD_Y,
            DEFAULT_CHILD_SIZE);
        internalNode.insertIntoChild(sw, 0, DEFAULT_CHILD_X, DEFAULT_CHILD_Y,
            DEFAULT_CHILD_SIZE);
        internalNode.insertIntoChild(se, 0, DEFAULT_CHILD_X, DEFAULT_CHILD_Y,
            DEFAULT_CHILD_SIZE);

        PointList results = new PointList();

        results.clear();
        internalNode.regionsearch(0, 0, 256, 256, 0, 0, WORLD_SIZE, results);
        assertEquals(4, results.size());
        assertTrue(results.get(0).getName().startsWith("Sub"));

        internalNode = new InternalNode();
        Point q0 = new Point("Q0", 100, 100);
        Point q1 = new Point("Q1", 900, 100);
        Point q2 = new Point("Q2", 100, 900);
        Point q3 = new Point("Q3", 900, 900);

        internalNode.insertIntoChild(q0, 0, 0, 0, 512);
        internalNode.insertIntoChild(q1, 1, 512, 0, 512);
        internalNode.insertIntoChild(q2, 2, 0, 512, 512);
        internalNode.insertIntoChild(q3, 3, 512, 512, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        output = systemOut().getHistory();

        assertTrue(output.contains("Q0"));
        assertTrue(output.contains("Q1"));
        assertTrue(output.contains("Q2"));
        assertTrue(output.contains("Q3"));

        results.clear();
        internalNode.regionsearch(0, 0, 512, 512, 0, 0, WORLD_SIZE, results);
        assertEquals(1, results.size());
        assertEquals("Q0", results.get(0).getName());

        results.clear();
        internalNode.regionsearch(512, 0, 512, 512, 0, 0, WORLD_SIZE, results);
        assertEquals(1, results.size());
        assertEquals("Q1", results.get(0).getName());
    }


    /**
     * Tests different scenarios for point location checks and splitting.
     */
    public void testSameLocationAndSplitting() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 100, 100);
        Point p3 = new Point("P3", 100, 100);
        Point p4 = new Point("P4", 100, 100);

        internalNode.insertIntoChild(p1, 0, 0, 0, 512);
        internalNode.insertIntoChild(p2, 0, 0, 0, 512);
        internalNode.insertIntoChild(p3, 0, 0, 0, 512);
        internalNode.insertIntoChild(p4, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("P1"));
        assertTrue(output.contains("P2"));
        assertTrue(output.contains("P3"));
        assertTrue(output.contains("P4"));

        testSameLocationScenario(new Point("A1", 100, 100), new Point("A2", 100,
            100), new Point("A3", 100, 100), new Point("A4", 100, 100), false);

        testSameLocationScenario(new Point("B1", 100, 100), new Point("B2", 101,
            100), new Point("B3", 100, 100), new Point("B4", 100, 100), true);

        internalNode = new InternalNode();
        Point sp1 = new Point("SP1", 100, 100);
        Point sp2 = new Point("SP2", 110, 110);
        Point sp3 = new Point("SP3", 120, 120);
        Point sp4 = new Point("SP4", 130, 130);

        internalNode.insertIntoChild(sp1, 0, 0, 0, 512);
        internalNode.insertIntoChild(sp2, 0, 0, 0, 512);
        internalNode.insertIntoChild(sp3, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String beforeSplit = systemOut().getHistory();

        internalNode.insertIntoChild(sp4, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String afterSplit = systemOut().getHistory();

        assertTrue(afterSplit.contains("Internal"));
        assertTrue(afterSplit.contains("SP1"));
        assertTrue(afterSplit.contains("SP2"));
        assertTrue(afterSplit.contains("SP3"));
        assertTrue(afterSplit.contains("SP4"));
    }


    private void testSameLocationScenario(
        Point p1,
        Point p2,
        Point p3,
        Point p4,
        boolean shouldSplit) {
        InternalNode node = new InternalNode();

        node.insertIntoChild(p1, 0, 0, 0, 512);
        node.insertIntoChild(p2, 0, 0, 0, 512);
        node.insertIntoChild(p3, 0, 0, 0, 512);
        node.insertIntoChild(p4, 0, 0, 0, 512);

        systemOut().clearHistory();
        node.dump(0, 0, WORLD_SIZE, 0);
        String output = systemOut().getHistory();

        if (shouldSplit) {
            assertTrue("Should have split to create internal nodes", output
                .contains("Internal"));
        }
        else {
            assertFalse("Should not have split to create internal nodes", output
                .contains("Node at 0 0 1024 Internal") && output.contains(
                    "Node at 0 0 512 Internal"));
        }
    }


    /**
     * Tests the remove methods (by coordinates and name).
     */
    public void testRemove() {
        Point point = new Point("ToRemove", 200, 200);
        internalNode.insert(point, 0, 0, 1024);

        KVPair<QuadNode, Point> result = internalNode.remove(200, 200, 0, 0,
            1024);
        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());

        internalNode.insert(point, 0, 0, 1024);
        result = internalNode.remove(999, 999, 0, 0, 1024);
        assertNotNull(result);
        assertNotNull(result.key());
        assertNull(result.value());

        result = internalNode.remove("ToRemove", 0, 0, 1024);
        assertNotNull(result);
        assertNotNull(result.key());
        assertNotNull(result.value());
        assertEquals("ToRemove", result.value().getName());

        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 700, 100);
        Point p3 = new Point("P3", 100, 700);

        internalNode.insert(p1, 0, 0, 1024);
        internalNode.insert(p2, 0, 0, 1024);
        internalNode.insert(p3, 0, 0, 1024);

        result = internalNode.remove("P2", 0, 0, 1024);
        assertNotNull(result);

        assertTrue(result.key() instanceof LeafNode);
        LeafNode leaf = (LeafNode)result.key();
        assertEquals(2, leaf.getPoints().size());
    }


    /**
     * Tests the regionsearch method with various scenarios.
     */
    public void testRegionsearch() {
        insertPointInAllQuadrants();

        PointList results = new PointList();
        int visited = internalNode.regionsearch(0, 0, 512, 512, 0, 0, 1024,
            results);
        assertTrue(visited >= 2);
        assertEquals(1, results.size());
        assertEquals("NW", results.get(0).getName());

        results = new PointList();
        visited = internalNode.regionsearch(400, 400, 400, 400, 0, 0, 1024,
            results);
        assertTrue(visited >= 5);
        assertEquals(1, results.size());
        assertEquals("SE", results.get(0).getName());

        results = new PointList();
        visited = internalNode.regionsearch(900, 900, 100, 100, 0, 0, 1024,
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
        Point p3 = new Point("P3", 700, 700);
        Point p4 = new Point("P4", 700, 700);

        internalNode.insert(p1, 0, 0, 1024);
        internalNode.insert(p2, 0, 0, 1024);
        internalNode.insert(p3, 0, 0, 1024);
        internalNode.insert(p4, 0, 0, 1024);

        CoordinateList duplicates = new CoordinateList();
        int visited = internalNode.findDuplicates(0, 0, 1024, duplicates);

        assertTrue(visited >= 5);
        assertEquals(2, duplicates.size());

        boolean found100 = false;
        boolean found700 = false;

        for (int i = 0; i < duplicates.size(); i++) {
            Coordinate coord = duplicates.get(i);
            if (coord.getX() == 100 && coord.getY() == 100)
                found100 = true;
            else if (coord.getX() == 700 && coord.getY() == 700)
                found700 = true;
        }

        assertTrue(found100);
        assertTrue(found700);
    }


    /**
     * Tests quadrant determination with different world sizes.
     */
    public void testQuadrantDetermination() {
        testQuadrantWithSize(1024);

        internalNode = new InternalNode();
        testQuadrantWithSize(1023);

        internalNode = new InternalNode();
        testSmallWorldQuadrants();
    }


    private void testQuadrantWithSize(int size) {
        int midPoint = size / 2;

        Point nw = new Point("NW", midPoint - 1, midPoint - 1);
        Point ne = new Point("NE", midPoint, midPoint - 1);
        Point sw = new Point("SW", midPoint - 1, midPoint);
        Point se = new Point("SE", midPoint, midPoint);

        internalNode.insert(nw, 0, 0, size);
        internalNode.insert(ne, 0, 0, size);
        internalNode.insert(sw, 0, 0, size);
        internalNode.insert(se, 0, 0, size);

        PointList results = new PointList();
        internalNode.regionsearch(0, 0, midPoint - 1, midPoint - 1, 0, 0, size,
            results);
        assertEquals(1, results.size());
        assertEquals("NW", results.get(0).getName());

        results = new PointList();
        internalNode.regionsearch(midPoint, 0, midPoint, midPoint - 1, 0, 0,
            size, results);
        assertEquals(1, results.size());
        assertEquals("NE", results.get(0).getName());
    }


    private void testSmallWorldQuadrants() {
        int size = 4;

        Point nw = new Point("SmallNW", 1, 1);
        Point ne = new Point("SmallNE", 2, 1);
        Point sw = new Point("SmallSW", 1, 2);
        Point se = new Point("SmallSE", 2, 2);

        internalNode.insert(nw, 0, 0, size);
        internalNode.insert(ne, 0, 0, size);
        internalNode.insert(sw, 0, 0, size);
        internalNode.insert(se, 0, 0, size);

        PointList results = new PointList();
        internalNode.regionsearch(1, 1, 1, 1, 0, 0, size, results);
        assertEquals(1, results.size());
        assertEquals("SmallNW", results.get(0).getName());

        results = new PointList();
        internalNode.regionsearch(2, 1, 1, 1, 0, 0, size, results);
        assertEquals(1, results.size());
        assertEquals("SmallNE", results.get(0).getName());
    }


    /**
     * Tests node comparison and additional methods.
     */
    public void testMiscellaneousMethods() {
        EmptyNode emptyNode = EmptyNode.getInstance();
        assertEquals(1, internalNode.compareTo(emptyNode));

        LeafNode leafNode = new LeafNode();
        leafNode.insert(new Point("P1", 100, 200), 0, 0, 1024);
        assertEquals(1, internalNode.compareTo(leafNode));

        internalNode = new InternalNode();
        Point nwPoint = new Point("NW", 100, 100);
        Point nePoint = new Point("NE", 300, 100);
        Point swPoint = new Point("SW", 100, 300);
        Point sePoint = new Point("SE", 300, 300);

        internalNode.insertIntoChild(nwPoint, 0, 0, 0, 512);
        internalNode.insertIntoChild(nePoint, 0, 0, 0, 512);
        internalNode.insertIntoChild(swPoint, 0, 0, 0, 512);
        internalNode.insertIntoChild(sePoint, 0, 0, 0, 512);

        PointList results = new PointList();
        internalNode.regionsearch(0, 0, 256, 256, 0, 0, WORLD_SIZE, results);
        assertEquals(1, results.size());
        assertEquals("NW", results.get(0).getName());

        Point center = new Point("Center", 512, 512);
        internalNode.insert(center, 0, 0, 1024);

        systemOut().clearHistory();
        int nodeCount = internalNode.dump(0, 0, 1024, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("Center"));
        assertTrue(output.contains("Node at 0 0 1024 Internal"));
        assertTrue(nodeCount >= 5);
    }


    /**
     * Tests the region search efficiency with boundary rectangles.
     */
    public void testRegionSearchBoundaries() {
        insertPointInAllQuadrants();

        PointList results = new PointList();
        int visited = internalNode.regionsearch(0, 0, 512, 512, 0, 0, 1024,
            results);

        assertEquals(1, results.size());
        assertEquals("NW", results.get(0).getName());

        assertEquals(2, visited);

        results = new PointList();
        visited = internalNode.regionsearch(512, 0, 512, 512, 0, 0, 1024,
            results);

        assertEquals(1, results.size());
        assertEquals("NE", results.get(0).getName());

        assertEquals(2, visited);
    }


    /**
     * Tests the region search with a region that intersects multiple quadrants.
     */
    public void testRegionSearchCrossingQuadrants() {

        internalNode.insert(new Point("NW", 300, 300), 0, 0, 1024);
        internalNode.insert(new Point("NE", 600, 300), 0, 0, 1024);
        internalNode.insert(new Point("SW", 300, 600), 0, 0, 1024);
        internalNode.insert(new Point("SE", 600, 600), 0, 0, 1024);

        PointList results = new PointList();
        int visited = internalNode.regionsearch(256, 256, 512, 512, 0, 0, 1024,
            results);

        assertEquals(4, results.size());

        assertEquals(5, visited);
    }


    /**
     * Tests the region search with disjoint regions.
     */
    public void testRegionSearchDisjoint() {
        insertPointInAllQuadrants();

        PointList results = new PointList();
        int visited = internalNode.regionsearch(400, 400, 10, 10, 0, 0, 1024,
            results);

        assertEquals(0, results.size());

        assertTrue("Visited too many nodes: " + visited, visited < 5);
    }


    /**
     * Tests the specific code path for inserting into a child that is already
     * an InternalNode.
     */
    public void testInternalNodeChildInsertion() {

        InternalNode mainNode = new InternalNode();

        Point nwNW = new Point("NWNW", 100, 100);
        Point nwNE = new Point("NWNE", 250, 100);
        Point nwSW = new Point("NWSW", 100, 250);
        Point nwSE = new Point("NWSE", 250, 250);

        mainNode.insertIntoChild(nwNW, 0, 0, 0, 512);
        mainNode.insertIntoChild(nwNE, 0, 0, 0, 512);
        mainNode.insertIntoChild(nwSW, 0, 0, 0, 512);
        mainNode.insertIntoChild(nwSE, 0, 0, 0, 512);

        Point deepNWNW = new Point("DeepNWNW", 50, 50);
        mainNode.insertIntoChild(deepNWNW, 0, 0, 0, 512);

        PointList results = new PointList();
        mainNode.regionsearch(40, 40, 20, 20, 0, 0, WORLD_SIZE, results);

        assertEquals(1, results.size());
        assertEquals("DeepNWNW", results.get(0).getName());

        Point deepNWSE = new Point("DeepNWSE", 300, 300);
        mainNode.insertIntoChild(deepNWSE, 0, 0, 0, 512);

        results = new PointList();
        mainNode.regionsearch(290, 290, 20, 20, 0, 0, WORLD_SIZE, results);

        assertEquals(1, results.size());
        assertEquals("DeepNWSE", results.get(0).getName());

        results = new PointList();
        mainNode.regionsearch(0, 0, 512, 512, 0, 0, WORLD_SIZE, results);

        assertEquals(6, results.size());

        systemOut().clearHistory();
        mainNode.dump(0, 0, WORLD_SIZE, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("Node at 0 0 1024 Internal"));
        assertTrue(output.contains("DeepNWNW"));
        assertTrue(output.contains("DeepNWSE"));
    }
}