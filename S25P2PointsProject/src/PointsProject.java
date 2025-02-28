import java.io.File;
import java.io.IOException;

/**
 * Main for CS3114 Quadtree/SkipList Point project (CS3114 Spring 2016 Project
 * 2). Usage: java PointsProject <command-file>
 *
 * @author CS Staff
 * @version February, 2016
 */
public class PointsProject {
    /**
     * Main: Process input parameters and invoke command file processor
     *
     * @param args
     *            The command line parameters
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: PointsProject <command-file>");
            return;
        }

        String commandFile = args[0].trim();
        // System.out.println("Working on file " + commandFile);
        File theFile = new File(commandFile);
        if (!theFile.exists()) {
            System.out.println("There is no such input file as |" + commandFile
                + "|");
            return;
        }

// Database myWorld = new Database();

// CommandProcessor processor = new CommandProcessor(myWorld);
// processor.readCmdFile(theFile);
    }
}
