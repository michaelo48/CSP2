import student.TestCase;

/**
 * Test class for the Database class.
 * Provides comprehensive mutation testing coverage.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class DatabaseTest extends TestCase {

    private Database db;

    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        db = new Database();
    }


    /**
     * Tests the database constructor.
     * Ensures the database is properly initialized.
     */
    public void testConstructor() {
        db.dump();
        String output = systemOut().getHistory();
        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("QuadTree dump:"));
        assertTrue(output.contains("Node at 0 0 1024 Empty"));
        assertTrue(output.contains("1 quadtree nodes printed"));
    }


    /**
     * Tests the insert method with valid inputs.
     */
    public void testInsertValid() {
        db.insert("Point1", 100, 200);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point inserted:"));
        assertTrue(output.contains("Point1"));
        assertTrue(output.contains("100"));
        assertTrue(output.contains("200"));

        systemOut().clearHistory();
        db.insert("Point2", 300, 400);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point inserted:"));
        assertTrue(output.contains("Point2"));
    }


    /**
     * Tests the insert method with coordinates at the boundaries.
     */
    public void testInsertBoundaries() {
        db.insert("Origin", 0, 0);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point inserted:"));
        assertTrue(output.contains("Origin"));

        systemOut().clearHistory();
        db.insert("Max", 1023, 1023);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point inserted:"));
        assertTrue(output.contains("Max"));
    }


    /**
     * Tests the insert method with invalid coordinates.
     */
    public void testInsertInvalid() {
        db.insert("Invalid1", -1, 100);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("Invalid1"));
        assertTrue(output.contains("-1"));

        systemOut().clearHistory();
        db.insert("Invalid2", 100, -5);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("Invalid2"));
        assertTrue(output.contains("-5"));

        systemOut().clearHistory();
        db.insert("Invalid3", 1024, 100);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("Invalid3"));
        assertTrue(output.contains("1024"));

        systemOut().clearHistory();
        db.insert("Invalid4", 100, 1024);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("Invalid4"));
        assertTrue(output.contains("1024"));
    }


    /**
     * Tests the remove method with name parameter.
     */
    public void testRemoveByName() {
        db.insert("RemovableByName", 150, 250);
        systemOut().clearHistory();

        db.remove("RemovableByName");
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point removed:"));
        assertTrue(output.contains("RemovableByName"));

        systemOut().clearHistory();
        db.remove("RemovableByName");
        output = systemOut().getHistory();
        assertTrue(output.contains("Point not removed:"));
        assertTrue(output.contains("RemovableByName"));
    }


    /**
     * Tests the remove method with coordinate parameters.
     */
    public void testRemoveByCoordinates() {
        db.insert("RemovableByCoord", 350, 450);
        systemOut().clearHistory();

        db.remove(350, 450);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point removed:"));
        assertTrue(output.contains("RemovableByCoord"));

        systemOut().clearHistory();
        db.remove(350, 450);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point not found:"));
        assertTrue(output.contains("350"));
        assertTrue(output.contains("450"));
    }


    /**
     * Tests the remove method with invalid coordinates.
     */
    public void testRemoveInvalidCoordinates() {
        db.remove(-1, 100);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("-1"));

        systemOut().clearHistory();
        db.remove(100, -5);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("-5"));

        systemOut().clearHistory();
        db.remove(1024, 100);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("1024"));

        systemOut().clearHistory();
        db.remove(100, 1024);
        output = systemOut().getHistory();
        assertTrue(output.contains("Point rejected:"));
        assertTrue(output.contains("1024"));
    }


    /**
     * Tests the regionsearch method with valid inputs.
     */
    public void testRegionsearchValid() {
        db.insert("Point1", 100, 100);
        db.insert("Point2", 200, 200);
        db.insert("Point3", 300, 300);
        systemOut().clearHistory();

        db.regionsearch(50, 50, 300, 300);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Points intersecting region"));
        assertTrue(output.contains("50 50 300 300")); // Fixed: removed comma
        assertTrue(output.contains("quadtree nodes visited"));
    }


    /**
     * Tests the regionsearch method with invalid inputs.
     */
    public void testRegionsearchInvalid() {
        db.regionsearch(50, 50, -10, 300);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Rectangle rejected:"));
        assertTrue(output.contains("-10"));

        systemOut().clearHistory();
        db.regionsearch(50, 50, 300, -20);
        output = systemOut().getHistory();
        assertTrue(output.contains("Rectangle rejected:"));
        assertTrue(output.contains("-20"));

        systemOut().clearHistory();
        db.regionsearch(50, 50, 0, 300);
        output = systemOut().getHistory();
        assertTrue(output.contains("Rectangle rejected:"));

        systemOut().clearHistory();
        db.regionsearch(50, 50, 300, 0);
        output = systemOut().getHistory();
        assertTrue(output.contains("Rectangle rejected:"));
    }


    /**
     * Tests the duplicates method.
     */
    public void testDuplicates() {
        db.insert("Point1", 500, 500);
        db.insert("Point2", 500, 500);
        db.insert("Point3", 600, 600);
        systemOut().clearHistory();

        db.duplicates();
        String output = systemOut().getHistory();
        assertTrue(output.contains("Duplicate points:"));
        assertTrue(output.contains("500 500")); // Fixed: removed comma
    }


    /**
     * Tests the search method for existing points.
     */
    public void testSearchExisting() {
        db.insert("SearchablePoint", 700, 700);
        systemOut().clearHistory();

        db.search("SearchablePoint");
        String output = systemOut().getHistory();
        assertTrue(output.contains("Found"));
        assertTrue(output.contains("SearchablePoint"));
    }


    /**
     * Tests the search method for non-existing points.
     */
    public void testSearchNonExisting() {
        db.search("NonExistentPoint");
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point not found:"));
        assertTrue(output.contains("NonExistentPoint"));
    }


    /**
     * Tests the dump method after various operations.
     */
    public void testDump() {
        db.insert("DumpPoint1", 800, 800);
        db.insert("DumpPoint2", 900, 900);
        systemOut().clearHistory();

        db.dump();
        String output = systemOut().getHistory();
        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("QuadTree dump:"));
        assertTrue(output.contains("DumpPoint1"));
        assertTrue(output.contains("DumpPoint2"));
        assertTrue(output.contains("Node at 0 0 1024"));
        assertTrue(output.contains("quadtree nodes printed"));
    }


    /**
     * Tests a complex sequence of operations to catch potential interaction
     * bugs.
     */
    public void testComplexSequence() {
        db.insert("Complex1", 150, 150);
        db.insert("Complex2", 250, 250);
        db.insert("Complex3", 350, 350);

        db.remove("Complex2");

        db.remove(350, 350);

        systemOut().clearHistory();
        db.search("Complex2");
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point not found:"));

        systemOut().clearHistory();
        db.insert("Complex1", 450, 450);

        systemOut().clearHistory();
        db.search("Complex1");
        output = systemOut().getHistory();
        assertTrue(output.contains("Found"));
        assertTrue(output.contains("Complex1"));

        systemOut().clearHistory();
        db.regionsearch(100, 100, 400, 400);
        output = systemOut().getHistory();
        assertTrue(output.contains("Points intersecting region"));
    }


    /**
     * Tests edge cases of valid inputs to catch specific mutations.
     */
    public void testEdgeCases() {
        db.insert("Edge1", 1023, 1023);

        db.insert("Edge2", 1022, 1022);

        db.insert("Edge3", 1, 1);

        systemOut().clearHistory();
        db.regionsearch(1022, 1022, 2, 2);
        String output = systemOut().getHistory();
        assertTrue(output.contains("Points intersecting region"));
    }


    /**
     * Tests empty scenarios to catch mutations in emptiness checks.
     */
    public void testEmptyScenarios() {
        db.search("EmptySearch");
        String output = systemOut().getHistory();
        assertTrue(output.contains("Point not found:"));

        systemOut().clearHistory();
        db.remove("EmptyRemove");
        output = systemOut().getHistory();
        assertTrue(output.contains("Point not removed:"));

        systemOut().clearHistory();
        db.regionsearch(10, 10, 100, 100);
        output = systemOut().getHistory();
        assertTrue(output.contains("Points intersecting region"));
        assertFalse(output.contains("Found"));

        systemOut().clearHistory();
        db.duplicates();
        output = systemOut().getHistory();
        assertTrue(output.contains("Duplicate points:"));

        systemOut().clearHistory();
        db.dump();
        output = systemOut().getHistory();
        assertTrue(output.contains("SkipList dump:"));
        assertTrue(output.contains("QuadTree dump:"));
        assertTrue(output.contains("Node at 0 0 1024 Empty"));
        assertTrue(output.contains("1 quadtree nodes printed"));
    }


    /**
     * Tests multiple identical objects with the same name.
     */
    public void testMultipleSameNameEntries() {
        db.insert("SameName", 100, 100);
        db.insert("SameName", 200, 200);
        db.insert("SameName", 300, 300);

        systemOut().clearHistory();
        db.search("SameName");
        String output = systemOut().getHistory();

        assertTrue(output.contains("Found"));
        assertTrue(output.contains("SameName"));

        int count = 0;
        int index = output.indexOf("Found");
        while (index != -1) {
            count++;
            index = output.indexOf("Found", index + 1);
        }

        assertEquals(3, count);
    }


    /**
     * Tests removal effects on duplicates detection.
     */
    public void testRemovalEffectsOnDuplicates() {
        db.insert("DupA", 500, 500);
        db.insert("DupB", 500, 500);

        systemOut().clearHistory();
        db.duplicates();
        String output = systemOut().getHistory();
        assertTrue(output.contains("500 500")); // Fixed: removed comma

        systemOut().clearHistory();
        db.remove("DupA");

        systemOut().clearHistory();
        db.duplicates();
        output = systemOut().getHistory();
        assertFalse(output.contains("500 500")); // Fixed: removed comma
    }


    /**
     * Tests the QuadTree dump format specifically
     */
    public void testQuadTreeDumpFormat() {
        // Test empty quadtree dump
        systemOut().clearHistory();
        db.dump();
        String output = systemOut().getHistory();
        assertTrue(output.contains("Node at 0 0 1024 Empty"));
        assertTrue(output.contains("1 quadtree nodes printed"));

        // Test with some inserted points
        db.insert("Point1", 100, 100);
        db.insert("Point2", 800, 800);

        systemOut().clearHistory();
        db.dump();
        output = systemOut().getHistory();
        assertTrue(output.contains("Node at 0 0 1024"));
        // Should have more than 1 node now
        assertTrue(output.contains("quadtree nodes printed"));
    }
}
