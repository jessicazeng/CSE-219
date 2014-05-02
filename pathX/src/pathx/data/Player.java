/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;

/**
 *
 * @author Jessica
 */
public class Player extends Sprite{
    // CURRENT POSITION
    private int startX;
    private int startY;
    
    // CURRENT NODE
    private int currentNode;
    
    // THE TARGET COORDINATES IN WHICH IT IS CURRENTLY HEADING
    private int targetX;
    private int targetY;
    
    // THIS IS true WHEN THIS TILE IS MOVING, WHICH HELPS US FIGURE
    // OUT WHEN IT HAS REACHED A DESTINATION NODE
    private boolean movingToTarget;
    
    private boolean inPath;
    
    private ArrayList<Intersection> path;
    private int pathIndex;
    
    public Player(SpriteType initSpriteType, float initX, float initY, float initVx, float initVy, 
            String initState)
    {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        currentNode = 0;
    }
    
    // ACCESSOR METHODS
    public int getStartX(){
        return startX;
    }
    
    public int getStartY(){
        return startY;
    }
    
    public int getTargetX(){
        return targetX;
    }
    
    public int getTargetY(){
        return targetY;
    }
    
    public int getCurrentNode(){
        return currentNode;
    }
    
    public boolean isMovingToTarget(){
        return movingToTarget;
    }
    
    // MUTATOR METHODS
    public void setStartingPos(int x, int y){
        startX = x;
        startY = y;
    }
    
    public void setTarget(int x, int y){
        targetX = x;
        targetY = y;
    }
    
    public void setCurrentNode(int index){
        currentNode = index;
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
    
    public void initPath(ArrayList<Intersection> initPath){
        path = initPath;
        inPath = true;
        pathIndex = 0;
    }
    
    public void updatePath(MiniGame game){
        if (calculateDistanceToTarget() < 5)
        {
            // AND TARGET THE NEXT NODE IN THE PATH
            Intersection target = path.get(pathIndex);
            int x1 = startX;
            int y1 = startY;
            int x2 = target.getX();
            int y2 = target.getY();
            
            targetX = x2-60; 
            targetY = y2+30;
            
            // START THE TILE MOVING AGAIN AND RANDOMIZE IT'S SPEED
            startMovingToTarget(5);
            startX = targetX;
            startY = targetY;
            setCurrentNode(target.getID());
            
            // AND ON TO THE NEXT PATH FOR THE NEXT TIME WE PICK A TARGET
            pathIndex += 1;
            if(pathIndex == path.size())
                inPath = false;
        }
        // JUST A NORMAL PATHING UPDATE
        else
        {
            // THIS WILL SIMPLY UPDATE THIS TILE'S POSITION USING ITS CURRENT VELOCITY
            super.update(game);
        }  
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
        if (inPath == true)
        {
            updatePath(game);
        }
        // GO TO THE TARGET AND THEN STOP MOVING
        if (calculateDistanceToTarget() < 5)
        {
            vX = 0;
            vY = 0;
            x = targetX;
            y = targetY;
            movingToTarget = false;
        }
        // OTHERWISE, JUST DO A NORMAL UPDATE, WHICH WILL CHANGE ITS POSITION
        // USING ITS CURRENT VELOCITY.
        else
        {
            super.update(game);
        }
    }
}
