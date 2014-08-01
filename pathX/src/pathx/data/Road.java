package pathx.data;

/**
 *
 * @author Jessica
 */
public class Road {
    // THESE ARE THE EDGE'S NODES
    Intersection node1;
    Intersection node2;
    
    // false IF IT'S TWO-WAY, true IF IT'S ONE WAY
    boolean oneWay;
    
    boolean closed;
    
    // ROAD SPEED LIMIT
    int speedLimit;

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
