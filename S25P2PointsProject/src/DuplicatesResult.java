/**
 * Container for duplicate search results
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class DuplicatesResult {
    private CoordinateList duplicates;
    private int nodesVisited;
    
    /**
     * Creates a new result container
     * 
     * @param duplicates The duplicate coordinates found
     * @param nodesVisited The number of nodes visited during search
     */
    public DuplicatesResult(CoordinateList duplicates, int nodesVisited) {
        this.duplicates = duplicates;
        this.nodesVisited = nodesVisited;
    }
    
    /**
     * Gets the duplicate coordinates
     * 
     * @return The list of duplicate coordinates
     */
    public CoordinateList getDuplicates() {
        return duplicates;
    }
    
    /**
     * Gets the number of nodes visited
     * 
     * @return Number of nodes visited
     */
    public int getNodesVisited() {
        return nodesVisited;
    }
}