/**
 * Container for region search results
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class RegionSearchResult {
    private PointList points;
    private int nodesVisited;
    
    /**
     * Creates a new result container
     * 
     * @param points The points found in the search
     * @param nodesVisited The number of nodes visited during search
     */
    public RegionSearchResult(PointList points, int nodesVisited) {
        this.points = points;
        this.nodesVisited = nodesVisited;
    }
    
    /**
     * Gets the points found in the search
     * 
     * @return The list of points
     */
    public PointList getPoints() {
        return points;
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