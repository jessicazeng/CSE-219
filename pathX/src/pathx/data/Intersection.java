package pathx.data;

import java.util.ArrayList;

/**
 * This class represents an intersection in a level.
 * 
 * @author Jessica
 */
public class Intersection {
    public int ID;
    // INTERSECTION LOCATION
    public int x;
    public int y;
    
    // IS IT OPEN OR NOT
    public boolean open;
    
    public boolean blocked;
    public long time;
    
    // LIST OF INTERSECTIONS THAT ARE ONE AWAY FROM THIS NODE.
    // PLAYER CAN ONLY MOVE TO ONE OF THESE NODES FROM CURRENT 
    // INTERSECTION
    ArrayList<Intersection  > adjacentIntersections;
    
    public boolean visited;
    
    /**
     * Constructor allows for a custom location, note that all
     * intersections start as open.
     */
    public Intersection(int initID, int initX, int initY)
    {
        ID = initID;
        x = initX;
        y = initY;
        open = true;
        adjacentIntersections = new ArrayList();
        visited = false;
    }
    
    // ACCESSOR METHODS
    public int getID()      {   return ID;      }
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
    {   
        this.open = open;   
    }
    
    public void addAdjacentIntersection(Intersection intersection){
        adjacentIntersections.add(intersection);
    }
    
    public Boolean isAdjacent(Intersection intersection){
        for(int i=0; i<adjacentIntersections.size(); i++){
            Intersection listIntersection = adjacentIntersections.get(i);
            if(listIntersection.equals(intersection))
                return true;
        }
        return false;
    }
}
