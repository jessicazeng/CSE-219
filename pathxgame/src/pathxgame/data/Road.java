/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.data;

/**
 *
 * @author Jessica
 */
public class Road {
    // THESE ARE THE EDGE'S NODES
    private Intersection node1;
    private Intersection node2;
    
    // false IF IT'S TWO-WAY, true IF IT'S ONE WAY
    private boolean oneWay;
    
    private boolean closed;
    
    // ROAD SPEED LIMIT
    private int speedLimit;

    // ACCESSOR METHODS
    public Intersection getNode1()  {   return node1;       }
    public Intersection getNode2()  {   return node2;       }
    public boolean isOneWay()       {   return oneWay;      }
    public boolean isClosed()       {   return closed;      }
    public int getSpeedLimit()      {   return speedLimit;  }
    
    // MUTATOR METHODS
    public void setNode1(Intersection node1)    {   this.node1 = node1;             }
    public void setNode2(Intersection node2)    {   this.node2 = node2;             }
    public void setOneWay(boolean oneWay)       {   this.oneWay = oneWay;           }
    public void setSpeedLimit(int speedLimit)   {   this.speedLimit = speedLimit;   }
    public void setClosed(boolean value)        {   this.closed = value;            }
}
