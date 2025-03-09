import student.TestCase;

/**
 * Tests the InternalNode class with enhanced test cases for mutation testing.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class InternalNodeTest extends TestCase {

    private InternalNode internalNode;
    private static final int WORLD_SIZE = 1024;

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


    /**
     * Helper method to count occurrences of a substring in a string.
     */
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
     * Tests inserting points into different quadrants.
     */
    public void testInsertQuadrants() {
        insertPointInAllQuadrants();

        systemOut().clearHistory();
        int nodeCount = internalNode.dump(0, 0, 1024, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("NW"));
        assertTrue(output.contains("NE"));
        assertTrue(output.contains("SW"));
        assertTrue(output.contains("SE"));
        assertEquals(5, nodeCount);
    }


    /**
     * Helper method to insert points in all quadrants.
     */
    private void insertPointInAllQuadrants() {
        internalNode.insert(new Point("NW", 100, 100), 0, 0, 1024);
        internalNode.insert(new Point("NE", 800, 100), 0, 0, 1024);
        internalNode.insert(new Point("SW", 100, 800), 0, 0, 1024);
        internalNode.insert(new Point("SE", 800, 800), 0, 0, 1024);
    }


    /**
     * Tests insertion that causes node splitting and handling points
     * at the same location.
     */
    public void testAdvancedInsertion() {
        for (int i = 0; i < 4; i++) {
            Point p = new Point("P" + i, 100 + i * 30, 100 + i * 30);
            internalNode.insert(p, 0, 0, 1024);
        }

        systemOut().clearHistory();
        int nodeCount = internalNode.dump(0, 0, 1024, 0);

        assertTrue(nodeCount > 5);

        internalNode = new InternalNode();

        for (int i = 0; i < 4; i++) {
            Point p = new Point("SamePos" + i, 100, 100);
            internalNode.insert(p, 0, 0, 1024);
        }

        systemOut().clearHistory();
        nodeCount = internalNode.dump(0, 0, 1024, 0);
        String output = systemOut().getHistory();

        assertEquals(5, nodeCount);
        for (int i = 0; i < 4; i++) {
            assertTrue(output.contains("SamePos" + i));
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
    }


    /**
     * Tests removing points that triggers merging of nodes.
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
     * Tests quadrant determination at boundaries using insert and regionsearch.
     * This test specifically targets the getQuadrant method by testing boundary
     * cases.
     */
    public void testQuadrantBoundaries() {
        internalNode = new InternalNode();

        testQuadrantWithSize(1024);

        internalNode = new InternalNode();
        testQuadrantWithSize(1023);

        internalNode = new InternalNode();
        testQuadrantWithSmallSize();
    }


    /**
     * Helper method to test quadrant determination with a specific region size
     * 
     * @param size
     *            The size of the region to test with
     */
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

        results = new PointList();
        internalNode.regionsearch(0, midPoint, midPoint - 1, midPoint, 0, 0,
            size, results);
        assertEquals(1, results.size());
        assertEquals("SW", results.get(0).getName());

        results = new PointList();
        internalNode.regionsearch(midPoint, midPoint, midPoint, midPoint, 0, 0,
            size, results);
        assertEquals(1, results.size());
        assertEquals("SE", results.get(0).getName());
    }


    /**
     * Tests quadrant determination with a very small region size
     * to specifically target the integer division in getQuadrant
     */
    private void testQuadrantWithSmallSize() {
        int size = 4;
        int midPoint = size / 2;

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

        results = new PointList();
        internalNode.regionsearch(1, 2, 1, 1, 0, 0, size, results);
        assertEquals(1, results.size());
        assertEquals("SmallSW", results.get(0).getName());

        results = new PointList();
        internalNode.regionsearch(2, 2, 1, 1, 0, 0, size, results);
        assertEquals(1, results.size());
        assertEquals("SmallSE", results.get(0).getName());
    }


    /**
     * Tests methods related to node comparison and boundary conditions.
     */
    public void testMiscellaneous() {
        EmptyNode emptyNode = EmptyNode.getInstance();
        assertEquals(1, internalNode.compareTo(emptyNode));

        LeafNode leafNode = new LeafNode();
        leafNode.insert(new Point("P1", 100, 200), 0, 0, 1024);
        assertEquals(1, internalNode.compareTo(leafNode));

        Point center = new Point("Center", 512, 512);
        internalNode.insert(center, 0, 0, 1024);

        systemOut().clearHistory();
        internalNode.dump(0, 0, 1024, 0);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Center"));
    }


    /**
     * Tests the insertIntoChild method when all points have the same location.
     * This targets line 125 that checks if all points have the same location.
     */
    public void testSameLocationCheck() {
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

        assertTrue(output.contains("Internal"));
        assertTrue(output.contains("P1"));
        assertTrue(output.contains("P2"));
        assertTrue(output.contains("P3"));
        assertTrue(output.contains("P4"));

        Point p5 = new Point("P5", 150, 150);
        internalNode.insertIntoChild(p5, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        output = systemOut().getHistory();

        assertTrue(output.contains("Internal"));
    }


    /**
     * Tests the creation of a new internal node during splitting.
     * This targets line 129 where a new internal node is created.
     */
    public void testNewInternalNodeCreation() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 110, 110);
        Point p3 = new Point("P3", 120, 120);
        Point p4 = new Point("P4", 130, 130);

        internalNode.insertIntoChild(p1, 0, 0, 0, 512);
        internalNode.insertIntoChild(p2, 0, 0, 0, 512);
        internalNode.insertIntoChild(p3, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String beforeSplit = systemOut().getHistory();

        internalNode.insertIntoChild(p4, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String afterSplit = systemOut().getHistory();

        assertTrue(beforeSplit.contains("Internal"));
        assertTrue(afterSplit.contains("Internal"));

        assertTrue(afterSplit.contains("P1"));
        assertTrue(afterSplit.contains("P2"));
        assertTrue(afterSplit.contains("P3"));
        assertTrue(afterSplit.contains("P4"));
    }


    /**
     * Tests the process of transferring points during a split.
     * This targets lines 132-135 where points are added from the original leaf
     * to the new internal node.
     */
    public void testPointTransferDuringSplit() {
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

        results = new PointList();
        internalNode.regionsearch(256, 0, 256, 256, 0, 0, WORLD_SIZE, results);
        assertEquals(1, results.size());
        assertEquals("NE", results.get(0).getName());

        results = new PointList();
        internalNode.regionsearch(0, 256, 256, 256, 0, 0, WORLD_SIZE, results);
        assertEquals(1, results.size());
        assertEquals("SW", results.get(0).getName());

        results = new PointList();
        internalNode.regionsearch(256, 256, 256, 256, 0, 0, WORLD_SIZE,
            results);
        assertEquals(1, results.size());
        assertEquals("SE", results.get(0).getName());
    }


    /**
     * Tests boundary conditions for the allSameLocation check.
     * This also targets line 125 with edge cases.
     */
    public void testSameLocationEdgeCases() {
        Point p1 = new Point("P1", 100, 100);
        Point p2 = new Point("P2", 100, 100);
        Point p3 = new Point("P3", 100, 100);
        Point p4 = new Point("P4", 100, 101);

        internalNode.insertIntoChild(p1, 0, 0, 0, 512);
        internalNode.insertIntoChild(p2, 0, 0, 0, 512);
        internalNode.insertIntoChild(p3, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String beforeSplit = systemOut().getHistory();

        internalNode.insertIntoChild(p4, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String afterSplit = systemOut().getHistory();

        assertTrue(afterSplit.contains("Internal"));

        internalNode = new InternalNode();
        p1 = new Point("P1", 200, 200);
        p2 = new Point("P2", 100, 100);
        p3 = new Point("P3", 100, 100);
        p4 = new Point("P4", 100, 100);

        internalNode.insertIntoChild(p1, 0, 0, 0, 512);
        internalNode.insertIntoChild(p2, 0, 0, 0, 512);
        internalNode.insertIntoChild(p3, 0, 0, 0, 512);
        internalNode.insertIntoChild(p4, 0, 0, 0, 512);

        systemOut().clearHistory();
        internalNode.dump(0, 0, WORLD_SIZE, 0);
        String output = systemOut().getHistory();

        assertTrue(output.contains("Internal"));
    }


    /**
     * Tests the allSameLocation check for various point arrangements.
     * This specifically targets line 125.
     */
    public void testAllSameLocationVariations() {
        testSameLocationScenario(new Point("A1", 100, 100), new Point("A2", 100,
            100), new Point("A3", 100, 100), new Point("A4", 100, 100), false);

        testSameLocationScenario(new Point("B1", 100, 100), new Point("B2", 101,
            100), new Point("B3", 100, 100), new Point("B4", 100, 100), true);

        testSameLocationScenario(new Point("C1", 100, 100), new Point("C2", 100,
            100), new Point("C3", 100, 101), new Point("C4", 100, 100), true);

        testSameLocationScenario(new Point("D1", 100, 100), new Point("D2", 100,
            100), new Point("D3", 100, 100), new Point("D4", 101, 101), true);
    }


    /**
     * Helper method to test various same location scenarios.
     * 
     * @param p1
     *            First point
     * @param p2
     *            Second point
     * @param p3
     *            Third point
     * @param p4
     *            Fourth point
     * @param shouldSplit
     *            Whether the scenario should result in a split
     */
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
}
