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
    private Database data;

    /**
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
     * database as required. 
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
        
        if (command.equals("insert")) {
            // Format: insert name x y
            String name = arr[1];
            int x = Integer.parseInt(arr[2]);
            int y = Integer.parseInt(arr[3]);
            
            data.insert(name, x, y);
        }
        else if (command.equals("remove")) {
            if (arr.length == 2) {
                data.remove(arr[1]);
            }
            else {
                int x = Integer.parseInt(arr[1]);
                int y = Integer.parseInt(arr[2]);
                
                data.remove(x, y);
            }
        }
        else if (command.equals("regionsearch")) {
            int x = Integer.parseInt(arr[1]);
            int y = Integer.parseInt(arr[2]);
            int w = Integer.parseInt(arr[3]);
            int h = Integer.parseInt(arr[4]);
            
            data.regionsearch(x, y, w, h);
        }
        else if (command.equals("duplicates")) {
            data.duplicates();
        }
        else if (command.equals("search")) {
            data.search(arr[1]);
        }
        else if (command.equals("dump")) {
            data.dump();
        }
        else {
            System.out.println("Unrecognized command.");
        }
    }
}