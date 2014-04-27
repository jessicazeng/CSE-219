package pathx.data;

import java.util.ArrayList;

/**
 * This class represents a level in pathX. This holds the information for each 
 * level.
 * 
 * @author Jessica Zeng
 */
public class Level {
    public String levelname;
    public int xposition;
    public int yposition;
    public boolean locked;
    public boolean levelCompleted;
    public String levelImage;
    public ArrayList<Intersection> intersections;
    public ArrayList<Road> roads;
    public String startImage;
    public String endImage;
    public int money;
    public int numPolice;
    public int numZombies;
    public int numBandits;
}
