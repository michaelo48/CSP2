import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import student.TestCase;

/**
 * This class tests the CommandProcessor class.
 * Test each possible command to ensure they work properly.
 * 
 * @author michaelo48
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
        commandProcessor.processor("insert A 10 20");
        String output = systemOut().getHistory();
        systemOut().clearHistory();
        
        commandProcessor.processor("remove A");
        output = systemOut().getHistory();
        
        systemOut().clearHistory();
        
        commandProcessor.processor("insert B 15 25");
        systemOut().clearHistory();
        commandProcessor.processor("remove 15 25");
        output = systemOut().getHistory();
        
        systemOut().clearHistory();
        
        commandProcessor.processor("insert C 30 40");
        systemOut().clearHistory();
        commandProcessor.processor("regionsearch 20 30 20 20");
        output = systemOut().getHistory();
        
        systemOut().clearHistory();
        
        commandProcessor.processor("insert D 30 40");
        systemOut().clearHistory();
        commandProcessor.processor("duplicates");
        output = systemOut().getHistory();
        
        systemOut().clearHistory();
        
        commandProcessor.processor("search C");
        output = systemOut().getHistory();
        
        systemOut().clearHistory();
        
        commandProcessor.processor("dump");
        output = systemOut().getHistory();
        
        systemOut().clearHistory();
        
        commandProcessor.processor("unknown_command");
        output = systemOut().getHistory();
        assertTrue(output.contains("Unrecognized command."));
    }
    
    /**
     * Tests each command in isolation
     */
    public void testIndividualCommands() {
        
        commandProcessor.processor("insert TestObj 100 200");
        String output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("remove TestObj");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("remove 100 200");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("regionsearch 10 10 50 50");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("duplicates");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("search SomeObject");
        output = systemOut().getHistory();
        assertFalse(output.contains("Unrecognized command"));
        systemOut().clearHistory();
        
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
        File tempFile = new File("test_commands.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        writer.println("insert X 50 60");
        writer.println("insert Y 70 80");
        writer.println("dump");
        writer.close();
        
        commandProcessor.readCmdFile(tempFile);
        String output = systemOut().getHistory();

        tempFile.delete();
    }
    
    /**
     * Tests handling of empty lines in command file.
     * @throws FileNotFoundException if the file cannot be found
     */
    public void testEmptyLines() throws FileNotFoundException {
        File tempFile = new File("test_empty_lines.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        writer.println("insert Z 90 100");
        writer.println("");
        writer.println("dump");
        writer.println("");
        writer.close();
        
        commandProcessor.readCmdFile(tempFile);
        String output = systemOut().getHistory();

        tempFile.delete();
    }
    
    /**
     * Test for the readCmdFile method's handling of empty lines.
     * @throws FileNotFoundException if the file cannot be found
     */
    public void testEmptyLineSpecific() throws FileNotFoundException {
        File tempFile = new File("empty_line_test.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        writer.println("");
        writer.close();
        
        commandProcessor.readCmdFile(tempFile);

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
        
        commandProcessor.readCmdFile(tempFile);
        tempFile.delete();
    }
    
    /**
     * Tests the processor method with each command to ensure
     * mutations are killed for every command path.
     */
    public void testProcessorCommandPaths() {
        commandProcessor.processor("insert TestShape 10 20");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        String nonInsertCommand = "notinsert TestShape 10 20";
        commandProcessor.processor(nonInsertCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("remove TestName");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("remove 50 60");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        String nonRemoveCommand = "notremove TestName";
        commandProcessor.processor(nonRemoveCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("regionsearch 1 2 3 4");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        String nonRegionSearchCommand = "notregionsearch 1 2 3 4";
        commandProcessor.processor(nonRegionSearchCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("duplicates");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        String nonDuplicatesCommand = "notduplicates";
        commandProcessor.processor(nonDuplicatesCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("search TestObject");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        

        String nonSearchCommand = "notsearch TestObject";
        commandProcessor.processor(nonSearchCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        commandProcessor.processor("dump");
        assertFalse(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
        
        String nonDumpCommand = "notdump";
        commandProcessor.processor(nonDumpCommand);
        assertTrue(systemOut().getHistory().contains("Unrecognized command"));
        systemOut().clearHistory();
    }
}