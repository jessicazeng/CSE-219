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
public class Intersection {
    private int ID;
    // INTERSECTION LOCATION
    private int x;
    private int y;
    
    // IS IT RED LIGHT OR GREEN LIGHT
    private boolean open;
    
    // IS INTERSECTION CLOSED
    private boolean blocked;
    private long time;
    
    // LIST OF INTERSECTIONS THAT ARE ONE AWAY FROM THIS NODE.
    // PLAYER CAN ONLY MOVE TO ONE OF THESE NODES FROM CURRENT 
    // INTERSECTION
    private ArrayList<Intersection> adjacentIntersections;
    
    private boolean visited;
    
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
    }
    
    // ACCESSOR METHODS
    public int getID()      {   return ID;      }
    public int getX()       {   return x;       }
    public int getY()       {   return y;       }
    public long getTime()   {   return time;    }
    public boolean isOpen() {   return open;    }
    public boolean isBlocked()  {   return blocked;     }
    public ArrayList<Intersection> getAdjacentIntersections() {    
        return adjacentIntersections;   
    }
    
    public Boolean isAdjacent(Intersection intersection){
        for(int i=0; i<adjacentIntersections.size(); i++){
            Intersection listIntersection = adjacentIntersections.get(i);
            if(listIntersection.equals(intersection))
                return true;
        }
        return false;
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
    public void setBlocked(boolean block){
        this.blocked = block;
    }
    public void setTime(long newTime){
        this.time = newTime;
    }
    
    public void addAdjacentIntersection(Intersection intersection){
        adjacentIntersections.add(intersection);
    }
}
