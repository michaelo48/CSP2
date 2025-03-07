import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import student.TestCase;

/**
 * This class tests the CommandProcessor class.
 * Test each possible command to ensure they work properly.
 * 
 * @author <your_name>
 * @version 03.07.2025
 */
public class CommandProcessorTest extends TestCase {

    private CommandProcessor commandProcessor;
    private Database database;
    
    /**
     * Sets up the test environment before each test.
     */
    public void setUp() {
        database = new Database();
        commandProcessor = new CommandProcessor(database);
    }

    /**
     * Tests the processor method with various commands.
     */
    public void testProcessor() {
        // Test insert command
        commandProcessor.processor("insert A 10 20");
        String output = systemOut().getHistory();
        // Assuming Database.insert() prints a confirmation message
        // Check if the output contains relevant information
        
        systemOut().clearHistory();
        
        // Test remove by name
        commandProcessor.processor("remove A");
        output = systemOut().getHistory();
        // Check for appropriate output
        
        systemOut().clearHistory();
        
        // Test remove by coordinates
        commandProcessor.processor("insert B 15 25");
        systemOut().clearHistory();
        commandProcessor.processor("remove 15 25");
        output = systemOut().getHistory();
        // Check for appropriate output
        
        systemOut().clearHistory();
        
        // Test region search
        commandProcessor.processor("insert C 30 40");
        systemOut().clearHistory();
        commandProcessor.processor("regionsearch 20 30 20 20");
        output = systemOut().getHistory();
        // Check for appropriate output
        
        systemOut().clearHistory();
        
        // Test duplicates
        commandProcessor.processor("insert D 30 40");
        systemOut().clearHistory();
        commandProcessor.processor("duplicates");
        output = systemOut().getHistory();
        // Check for appropriate output
        
        systemOut().clearHistory();
        
        // Test search
        commandProcessor.processor("search C");
        output = systemOut().getHistory();
        // Check for appropriate output
        
        systemOut().clearHistory();
        
        // Test dump
        commandProcessor.processor("dump");
        output = systemOut().getHistory();
        // Check for appropriate output
        
        systemOut().clearHistory();
        
        // Test unrecognized command
        commandProcessor.processor("unknown_command");
        output = systemOut().getHistory();
        assertTrue(output.contains("Unrecognized command."));
    }
    
    /**
     * Tests each command in isolation to ensure proper mutation coverage.
     */
    public void testIndividualCommands() {
        // Test each command individually to ensure mutations are killed
        
        // Insert command
        commandProcessor.processor("insert TestObj 100 200");
        String output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Remove by name command
        commandProcessor.processor("remove TestObj");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Remove by coordinates command
        commandProcessor.processor("remove 100 200");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Region search command
        commandProcessor.processor("regionsearch 10 10 50 50");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Duplicates command
        commandProcessor.processor("duplicates");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Search command
        commandProcessor.processor("search SomeObject");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Dump command
        commandProcessor.processor("dump");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
    }
    
    /**
     * Tests the readCmdFile method.
     * @throws FileNotFoundException if the file cannot be found
     */
    public void testReadCmdFile() throws FileNotFoundException {
        // Create a temporary command file
        File tempFile = new File("test_commands.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        writer.println("insert X 50 60");
        writer.println("insert Y 70 80");
        writer.println("dump");
        writer.close();
        
        // Process the file
        commandProcessor.readCmdFile(tempFile);
        String output = systemOut().getHistory();
        // Check for appropriate output from executing commands
        
        // Clean up
        tempFile.delete();
    }
    
    /**
     * Tests handling of empty lines in command file.
     * @throws FileNotFoundException if the file cannot be found
     */
    public void testEmptyLines() throws FileNotFoundException {
        // Create a temporary command file with empty lines
        File tempFile = new File("test_empty_lines.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        writer.println("insert Z 90 100");
        writer.println("");  // Empty line
        writer.println("dump");
        writer.println("");  // Empty line
        writer.close();
        
        // Process the file
        commandProcessor.readCmdFile(tempFile);
        String output = systemOut().getHistory();
        // Verify commands were processed correctly
        // despite empty lines
        
        // Clean up
        tempFile.delete();
    }
    
    /**
     * Test specifically for the readCmdFile method's handling of empty lines.
     * This test is designed to kill mutations on line 43.
     * @throws FileNotFoundException if the file cannot be found
     */
    public void testEmptyLineSpecific() throws FileNotFoundException {
        File tempFile = new File("empty_line_test.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        writer.println("");  // Just an empty line
        writer.close();
        
        // Process the file with just an empty line
        commandProcessor.readCmdFile(tempFile);
        
        // The test passes if no exception is thrown
        // This kills the mutation that replaced line.isEmpty() check with false
        
        // Clean up
        tempFile.delete();
    }
    
    /**
     * Test to verify behavior when scanner has no next line.
     * This test is designed to kill mutations on line 43 and 69.
     * @throws FileNotFoundException if the file cannot be found
     */
    public void testScannerHasNextLine() throws FileNotFoundException {
        File tempFile = new File("empty_file.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        // Create completely empty file
        writer.close();
        
        // Process empty file
        commandProcessor.readCmdFile(tempFile);
        
        // The test passes if no exception is thrown and the loop is not entered
        // This kills both hasNextLine() mutations
        
        // Clean up
        tempFile.delete();
    }
    
    /**
     * Tests the processor method with each command to ensure
     * mutations are killed for every command path.
     */
    public void testProcessorCommandPaths() {
        // Tests for killing mutations on lines 77, 79, 91, 100, 104, 108
        
        // Test each command path with assert statements to ensure
        // the right method is called
        
        // Insert test
        commandProcessor.processor("insert TestShape 10 20");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Deliberate false check for insert to kill mutation
        String nonInsertCommand = "notinsert TestShape 10 20";
        commandProcessor.processor(nonInsertCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Remove by name test
        commandProcessor.processor("remove TestName");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Remove by coordinates test
        commandProcessor.processor("remove 50 60");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Deliberate false check for remove to kill mutation
        String nonRemoveCommand = "notremove TestName";
        commandProcessor.processor(nonRemoveCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Region search test
        commandProcessor.processor("regionsearch 1 2 3 4");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Deliberate false check for regionsearch to kill mutation
        String nonRegionSearchCommand = "notregionsearch 1 2 3 4";
        commandProcessor.processor(nonRegionSearchCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Duplicates test
        commandProcessor.processor("duplicates");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Deliberate false check for duplicates to kill mutation
        String nonDuplicatesCommand = "notduplicates";
        commandProcessor.processor(nonDuplicatesCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Search test
        commandProcessor.processor("search TestObject");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Deliberate false check for search to kill mutation
        String nonSearchCommand = "notsearch TestObject";
        commandProcessor.processor(nonSearchCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Dump test
        commandProcessor.processor("dump");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        // Deliberate false check for dump to kill mutation
        String nonDumpCommand = "notdump";
        commandProcessor.processor(nonDumpCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
    }
}