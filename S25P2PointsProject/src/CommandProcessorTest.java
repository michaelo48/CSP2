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
        // Check that commands were processed correctly
        // despite empty lines
        
        // Clean up
        tempFile.delete();
    }
}