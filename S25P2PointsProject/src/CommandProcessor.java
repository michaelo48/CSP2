import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The purpose of this class is to parse a text file into its appropriate, line
 * by line commands for the format specified in the project spec.
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class CommandProcessor {

    // the database object to manipulate the
    // commands that the command processor
    // feeds to it
    private Database data;

    /**
     * The constructor for the command processor requires a database instance to
     * exist, so the only constructor takes a database class object to feed
     * commands to.
     * 
     * @param database
     *            The database to use
     */
    public CommandProcessor(Database database) {
        data = database;
    }

    /**
     * Reads and processes commands from a file
     * 
     * @param file
     *            The file to read from
     * @throws FileNotFoundException
     *            If the file cannot be found
     */
    public void readCmdFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            
            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }
            
            // Process the command
            processor(line);
        }
        
        scanner.close();
    }

    /**
     * This method parses keywords in the line and calls methods in the
     * database as required. Each line command will be specified by one of the
     * keywords to perform the actions.
     * These actions are performed on specified objects and include insert,
     * remove, regionsearch, duplicates, search, and dump. If the command in the 
     * file line is not one of these, an appropriate message will be written in 
     * the console. This processor method is called for each line in the file. 
     * Note that the methods called will themselves write to the console, this 
     * method does not, only calling methods that do.
     * 
     * @param line
     *            a single line from the text file
     */
    public void processor(String line) {
        // converts the string of the line into an
        // array of its space (" ") delimited elements
        String[] arr = line.split("\\s{1,}");
        String command = arr[0].toLowerCase(); // the command will be the first of these
                                               // elements, converted to lowercase
        
        // calls the appropriate method based on the command
        if (command.equals("insert")) {
            // Format: insert name x y
            String name = arr[1];
            int x = Integer.parseInt(arr[2]);
            int y = Integer.parseInt(arr[3]);
            
            data.insert(name, x, y);
        }
        else if (command.equals("remove")) {
            // Format: remove name or remove x y
            if (arr.length == 2) {
                // Remove by name
                data.remove(arr[1]);
            }
            else {
                // Remove by coordinates
                int x = Integer.parseInt(arr[1]);
                int y = Integer.parseInt(arr[2]);
                
                data.remove(x, y);
            }
        }
        else if (command.equals("regionsearch")) {
            // Format: regionsearch x y w h
            int x = Integer.parseInt(arr[1]);
            int y = Integer.parseInt(arr[2]);
            int w = Integer.parseInt(arr[3]);
            int h = Integer.parseInt(arr[4]);
            
            data.regionsearch(x, y, w, h);
        }
        else if (command.equals("duplicates")) {
            // Format: duplicates
            data.duplicates();
        }
        else if (command.equals("search")) {
            // Format: search name
            data.search(arr[1]);
        }
        else if (command.equals("dump")) {
            // Format: dump
            data.dump();
        }
        else {
            // Unrecognized command
            System.out.println("Unrecognized command.");
        }
    }
}