import student.TestCase;
import student.TestableRandom;

/**
 * This class tests the methods of SkipList class using Point objects
 * 
 * @author CS Staff
 * @version 2024-01-22
 */
public class SkipListTest extends TestCase {

    private SkipList<String, Point> sl;
    private Point p1;
    private Point p2;
    private Point p3;

    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        sl = new SkipList<String, Point>();
        p1 = new Point("P1", 1, 2);
        p2 = new Point("P2", 3, 4);
        p3 = new Point("P3", 5, 6);
    }


    /***
     * Example 1: Test `randomLevel` method with
     * predetermined random values using `TestableRandom`
     */
    public void testRandomLevelOne() {
        TestableRandom.setNextBooleans(false);
        sl = new SkipList<String, Point>();
        int randomLevelValue = sl.randomLevel();

        // This returns 1 because the first preset
        // random boolean is `false` which breaks
        // the `while condition inside the `randomLevel` method
        int expectedLevelValue = 1;

        // Compare the values
        assertEquals(expectedLevelValue, randomLevelValue);
    }


    /***
     * Example 2: Test `randomLevel` method with
     * predetermined random values using `TestableRandom`
     */
    public void testRandomLevelFour() {
        TestableRandom.setNextBooleans(true, true, true, false, true, false);
        sl = new SkipList<String, Point>();
        int randomLevelValue = sl.randomLevel();

        // This returns 4 because the fourth preset
        // random boolean is `false` which breaks
        // the `while condition inside the `randomLevel` method
        int expectedLevelValue = 4;

        // Compare the values
        assertEquals(expectedLevelValue, randomLevelValue);
    }


    /**
     * Tests the search method of SkipList
     */
    public void testSearch() {
        assertTrue(sl.search("empty").isEmpty());
        assertTrue(sl.search(null).isEmpty());

        sl.insert(new KVPair<String, Point>("p1", p1));
        sl.insert(new KVPair<String, Point>("p2", p2));
        sl.insert(new KVPair<String, Point>("p2", p3));

        assertFalse(sl.search("p1").isEmpty());

        assertEquals(1, sl.search("p1").size());
        assertEquals(p1, sl.search("p1").get(0).value());
        assertEquals(2, sl.search("p2").size());
    }


    /**
     * Tests the getSize method of SkipList
     */
    public void testGetSize() {
        assertEquals(0, sl.size());

        sl.insert(new KVPair<String, Point>("p1", p1));
        assertEquals(1, sl.size());

        sl.insert(new KVPair<String, Point>("p2", p2));
        sl.insert(new KVPair<String, Point>("p2", p3));
        assertEquals(3, sl.size());

        sl.remove("p1");
        assertEquals(2, sl.size());

        sl.removeByValue(p2);
        sl.remove("p2");
        assertEquals(0, sl.size());
    }


    /**
     * Tests the adjustHead method of SkipList
     */
    public void testAdjustHead() {
        TestableRandom.setNextBooleans(false);
        sl.insert(new KVPair<String, Point>("p1", p1));
        assertEquals(p1, sl.search("p1").get(0).value());

        TestableRandom.setNextBooleans(true, true, false);
        sl.insert(new KVPair<String, Point>("p2", p2));
        assertEquals(p2, sl.search("p2").get(0).value());

        TestableRandom.setNextBooleans(true, true, true, false);
        sl.insert(new KVPair<String, Point>("p3", p3));
        assertEquals(p3, sl.search("p3").get(0).value());

        sl.remove("p2");
        assertEquals(2, sl.size());
        assertTrue(sl.search("p2").isEmpty());
    }


    /**
     * Tests the insert method of SkipList
     */
    public void testInsert() {
        sl.insert(null);
        assertEquals(0, sl.size());

        KVPair<String, Point> pair1 = new KVPair<>("A", new Point("A", 1, 1));
        sl.insert(pair1);
        assertEquals(1, sl.size());
        assertEquals(pair1.value(), sl.search("A").get(0).value());

        sl.insert(new KVPair<>("A", new Point("A2", 2, 2)));
        assertEquals(2, sl.size());
        assertEquals(2, sl.search("A").size());

        TestableRandom.setNextBooleans(true, true, false);
        sl.insert(new KVPair<>("B", new Point("B", 3, 3)));

        TestableRandom.setNextBooleans(true, true, true, false);
        sl.insert(new KVPair<>("C", new Point("C", 4, 4)));
        assertEquals(4, sl.size());
    }


    /**
     * Tests the remove method of SkipList
     */
    public void testRemove() {
        assertNull(sl.remove("test"));
        assertNull(sl.remove(null));

        sl.insert(new KVPair<>("p1", p1));
        KVPair<String, Point> removed = sl.remove("p1");
        assertNotNull(removed);
        assertEquals(p1, removed.value());
        assertEquals(0, sl.size());

        TestableRandom.setNextBooleans(true, false);
        sl.insert(new KVPair<>("p1", p1));
        TestableRandom.setNextBooleans(true, true, false);
        sl.insert(new KVPair<>("p2", p2));
        TestableRandom.setNextBooleans(false);
        sl.insert(new KVPair<>("p3", p3));

        removed = sl.remove("p2");
        assertNotNull(removed);
        assertEquals(2, sl.size());
        assertNull(sl.remove("p2"));
    }


    /**
     * Tests the removeByValue method of SkipList
     */
    public void testRemoveByValue() {
        assertNull(sl.removeByValue(p1));
        assertNull(sl.removeByValue(null));

        sl.insert(new KVPair<>("p1", p1));
        KVPair<String, Point> removed = sl.removeByValue(p1);
        assertNotNull(removed);
        assertEquals(p1, removed.value());
        assertEquals(0, sl.size());

        TestableRandom.setNextBooleans(true, false);
        sl.insert(new KVPair<>("p1", p1));
        TestableRandom.setNextBooleans(true, true, false);
        sl.insert(new KVPair<>("p2", p2));
        TestableRandom.setNextBooleans(false);
        sl.insert(new KVPair<>("p3", p1));

        removed = sl.removeByValue(p1);
        assertNotNull(removed);
        assertEquals(p1, removed.value());
        assertEquals(2, sl.size());

        Point p4 = new Point("P4", 7, 8);
        assertNull(sl.removeByValue(p4));

        assertEquals(2, sl.size());
        assertNotNull(sl.search("p2").get(0));

        removed = sl.removeByValue(p1);
        assertNotNull(removed);
        assertEquals(p1, removed.value());
        assertEquals(1, sl.size());
    }


    /**
     * Tests the dump method of SkipList
     */
    public void testDump() {
        sl.dump();
        String output = systemOut().getHistory();
        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("Node has depth 1 value null"));
        assertTrue(output.contains("SkipList size is: 0"));

        systemOut().clearHistory();

        TestableRandom.setNextBooleans(false); // Level 1
        sl.insert(new KVPair<>("p1", p1));
        sl.dump();
        output = systemOut().getHistory();
        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("Node has depth 1 value null"));
    }
}
