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
public class Police extends Sprite{
    // the current node this sprite is in
    int Node;
    
    int targetX;
    int targetY;
    
    int startX;
    int startY;
    
    // THIS IS true WHEN THIS TILE IS MOVING, WHICH HELPS US FIGURE
    // OUT WHEN IT HAS REACHED A DESTINATION NODE
    private boolean movingToTarget;
    
    private boolean inPath;
    
    private ArrayList<Intersection> path;
    private int pathIndex;
    
    public Police(SpriteType initSpriteType, float initX, float initY, float initVx, float initVy, 
            String initState)
    {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        Node = 0;
    }
    
    public int getNode(){
        return Node;
    }
    
    public void setNode(int newNode){
        Node = newNode;
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
        
    }
}
