/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import mini_game.Sprite;
import mini_game.SpriteType;

/**
 *
 * @author Jessica
 */
public class Bandit extends Sprite{
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
    
    public Bandit(SpriteType initSpriteType, float initX, float initY, float initVx, float initVy, 
            String initState){
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        Node = 0;
    }
    
    public int getNode(){
        return Node;
    }
}
