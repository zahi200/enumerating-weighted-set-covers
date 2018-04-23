// This interface represents
import java.util.*;
public interface World {

    //returns the cost of the best solution in the world represented by this World
    public double getNextCost();

    //returns the best (or approximately best) solution in the world represented by this World
    public LinkedHashSet<Expert> getNextSolution();
}
