package BackEnd.GraphComponent;

public class MapEdge implements Comparable<MapEdge> {
    public MapVertex destination; //holds destination vertex (toVertex)
    public double dist;

    public MapEdge(MapVertex destination, double dist) {
        this.destination = destination;
        this.dist = dist;
    }

    @Override
    public String toString() {
        return "|-> " + destination + "d=" + dist + "|| h=" + destination.capacity;
    }

    @Override
    public int compareTo(MapEdge o) {
        return Double.compare(this.dist, o.dist);
    }
}
