import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Main class for the PointsProject.
 * Processes commands from a file to manage a database of points.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class PointsProject {

    /**
     * Main method to run the program.
     * 
     * @param args Command line arguments (should contain the command file)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java PointsProject {commandFile}");
            return;
        }

        String commandFile = args[0];
        Database db = new Database();

        try (Scanner scanner = new Scanner(new File(commandFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                processCommand(line, db);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + commandFile);
        }
    }

    /**
     * Processes a single command.
     * 
     * @param command The command to process
     * @param db      The database to operate on
     */
    private static void processCommand(String command, Database db) {
        Scanner scanner = new Scanner(command);
        if (!scanner.hasNext()) {
            scanner.close();
            return;
        }

        String cmd = scanner.next();

        try {
            switch (cmd) {
                case "insert":
                    processInsert(scanner, db);
                    break;
                case "remove":
                    processRemove(scanner, db);
                    break;
                case "search":
                    processSearch(scanner, db);
                    break;
                case "regionsearch":
                    processRegionSearch(scanner, db);
                    break;
                case "duplicates":
                    processDuplicates(db);
                    break;
                case "dump":
                    processDump(db);
                    break;
                default:
                    System.out.println("Unknown command: " + cmd);
            }
        } catch (Exception e) {
            System.out.println("Error processing command: " + command);
            e.printStackTrace();
        }

        scanner.close();
    }

    /**
     * Processes an insert command.
     * 
     * @param scanner The scanner containing the command arguments
     * @param db      The database to operate on
     */
    private static void processInsert(Scanner scanner, Database db) {
        String name = scanner.next();
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        if (x < 0 || y < 0 || x >= 1024 || y >= 1024) {
            System.out.println("Point rejected: (" + x + ", " + y + ")");
            return;
        }

        boolean success = db.insert(name, x, y);
        if (success) {
            System.out.println("Point inserted: (" + name + ", " + x + ", " + y + ")");
        } else {
            System.out.println("Point rejected: (" + x + ", " + y + ")");
        }
    }

    /**
     * Processes a remove command.
     * 
     * @param scanner The scanner containing the command arguments
     * @param db      The database to operate on
     */
    private static void processRemove(Scanner scanner, Database db) {
        if (!scanner.hasNextInt()) {
            // Remove by name
            String name = scanner.next();
            Point removedPoint = db.removeByName(name);

            if (removedPoint != null) {
                System.out.println("Point removed: " + removedPoint.toString());
            } else {
                System.out.println("Point not removed: " + name);
            }
        } else {
            // Remove by position
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (x < 0 || y < 0) {
                System.out.println("Point rejected: (" + x + ", " + y + ")");
                return;
            }

            if (x >= 1024 || y >= 1024) {
                System.out.println("Point rejected: (" + x + ", " + y + ")");
                return;
            }

            Point removedPoint = db.removeByPosition(x, y);

            if (removedPoint != null) {
                System.out.println("Point removed: " + removedPoint.toString());
            } else {
                System.out.println("Point not found: (" + x + ", " + y + ")");
            }
        }
    }

    /**
     * Processes a search command.
     * 
     * @param scanner The scanner containing the command arguments
     * @param db      The database to operate on
     */
    private static void processSearch(Scanner scanner, Database db) {
        String name = scanner.next();
        List<Point> points = db.search(name);

        if (points.isEmpty()) {
            System.out.println("Point not found: " + name);
            return;
        }

        for (Point p : points) {
            System.out.println("Found " + p.toString());
        }
    }

    /**
     * Processes a regionsearch command.
     * 
     * @param scanner The scanner containing the command arguments
     * @param db      The database to operate on
     */
    private static void processRegionSearch(Scanner scanner, Database db) {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        if (width <= 0 || height <= 0) {
            System.out.println("Rectangle rejected: (" + x + ", " + y +
                    ", " + width + ", " + height + ")");
            return;
        }

        QuadTree.RegionSearchResult result = db.regionSearch(x, y, width, height);
        List<Point> points = result.getPoints();
        int nodesVisited = result.getNodesVisited();

        System.out.println("Points intersecting region (" + x + ", " + y +
                ", " + width + ", " + height + "):");

        for (Point p : points) {
            System.out.println(p.toString());
        }

        System.out.println(nodesVisited + " quadtree nodes visited");
    }

    /**
     * Processes a duplicates command.
     * 
     * @param db The database to operate on
     */
    private static void processDuplicates(Database db) {
        Set<Position> duplicates = db.findDuplicates();

        System.out.println("Duplicate points:");

        for (Position pos : duplicates) {
            System.out.println(pos.toString());
        }
    }

    /**
     * Processes a dump command.
     * 
     * @param db The database to operate on
     */
    private static void processDump(Database db) {
        System.out.println(db.dump());
    }
}