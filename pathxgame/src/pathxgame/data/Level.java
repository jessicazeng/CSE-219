/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.data;

import java.util.ArrayList;

/**
 *
 * @author Jessica
 */
public class Level {
    public String levelname;
    public String fileName;
    public String city;
    
    // position of the level node on map
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
    public String nextLevel;
}
