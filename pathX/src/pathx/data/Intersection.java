package pathx.data;

import java.util.ArrayList;

/**
 * This class represents an intersection in a level.
 * 
 * @author Jessica
 */
public class Intersection {
    // INTERSECTION LOCATION
    public int x;
    public int y;
    
    // IS IT OPEN OR NOT
    public boolean open;
    
    ArrayList<Intersection> adjacentIntersections;
    
    /**
     * Constructor allows for a custom location, note that all
     * intersections start as open.
     */
    public Intersection(int initX, int initY)
    {
        x = initX;
        y = initY;
        open = true;
        adjacentIntersections = new ArrayList();
    }
    
    // ACCESSOR METHODS
    public int getX()       {   return x;       }
    public int getY()       {   return y;       }
    public boolean isOpen() {   return open;    }
    
    public ArrayList<Intersection> getAdjacentIntersections(){
        return adjacentIntersections;
    }
    
    // MUTATOR METHODS
    public void setX(int x)
    {   this.x = x;         }
    public void setY(int y)
    {   this.y = y;         }
    public void setOpen(boolean open)
    {   this.open = open;   }
    
    public void addAdjacentIntersection(Intersection intersection){
        adjacentIntersections.add(intersection);
    }
}
