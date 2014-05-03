package pathx.data;

import java.util.Random;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import pathx.ui.pathXGame;

/**
 *
 * @author Jessica
 */
public class Zombie extends Sprite{
    int ID;
    
    // the current node this sprite is in
    int Node;
    
    int targetX;
    int targetY;
    
    int startX;
    int startY;
    
    // THIS IS true WHEN THIS TILE IS MOVING, WHICH HELPS US FIGURE
    // OUT WHEN IT HAS REACHED A DESTINATION NODE
    private boolean movingToTarget;
    
    public Zombie(SpriteType initSpriteType, float initX, float initY, float initVx, float initVy, 
            String initState){
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        Node = 0;
    }
    
    public int getNode(){
        return Node;
    }
    
    public int getStartX(){
        return startX;
    }
    
    public int getStartY(){
        return startY;
    }
    
    // MUTATOR METHODS
    public void setID(int initID){
        ID = initID;
    }
    
    public void setStartingPos(int x, int y){
        startX = x;
        startY = y;
    }
    
    public void setTarget(int x, int y){
        targetX = x;
        targetY = y;
    }
    
    public void setNode(int newNode){
        Node = newNode;
    }
    
    /**
     * This method calculates the distance from the player's current location
     * to the target coordinates on a direct line.
     * 
     * @return The total distance on a direct line from where the tile is
     * currently, to where its target is.
     */
    public float calculateDistanceToTarget()
    {
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = targetX - x;
        
        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = targetY - y;
        
        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
        
        // AND RETURN THE DISTANCE
        return distance;
    }
    
    /**
     * Allows the tile to start moving by initializing its properly
     * scaled velocity vector pointed towards it target coordinates.
     * 
     * @param maxVelocity The maximum velocity of this tile, which
     * we'll then compute the x and y axis components for taking into
     * account the trajectory angle.
     */
    public void startMovingToTarget(int maxVelocity)
    {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;
        
        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetX - x;
        float diffY = targetY - y;
        float tanResult = diffY/diffX;
        float angleInRadians = (float)Math.atan(tanResult);
        
        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float)(maxVelocity * Math.cos(angleInRadians));
        
        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) vX *= -1;
        if ((diffX > 0) && (vX < 0)) vX *= -1;
        
        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float)(maxVelocity * Math.sin(angleInRadians));        
        
        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) vY *= -1;
        if ((diffY > 0) && (vY < 0)) vY *= -1;
    }
    
    /**
     * Called each frame, this method ensures that this tile is updated
     * according to the path it is on.
     * 
     * @param game The Sorting Hat game this tile is part of.
     */
    @Override
    public void update(MiniGame game)
    {
        if(movingToTarget == false){
            Record record = ((pathXGame)game).getPlayerRecord();
            String currentLevel = ((pathXDataModel)(game.getDataModel())).getCurrentLevel();
            Random rand = new Random();
            int node = rand.nextInt(record.getIntersections(currentLevel).size());
            while(node==0 || node==1)
                node = rand.nextInt(record.getIntersections(currentLevel).size());
            
            ((pathXDataModel)(game.getDataModel())).moveZombie(ID);
        }
        // GO TO THE TARGET AND THEN STOP MOVING
        else if (calculateDistanceToTarget() < 5)
        {
            vX = 0;
            vY = 0;
            x = targetX;
            y = targetY;
            movingToTarget = false;
            //((pathXDataModel)(game.getDataModel())).movePolice(ID);
        }
        // OTHERWISE, JUST DO A NORMAL UPDATE, WHICH WILL CHANGE ITS POSITION
        // USING ITS CURRENT VELOCITY.
        else
        {
            super.update(game);
        }
    }
}
