package pathx.ui;

import pathx.data.pathXDataModel;
import static pathx.pathXConstants.*;

/**
 *
 * @author Jessica
 */
public class SpecialsHandler {
    private pathXGame game;
    
    public SpecialsHandler(pathXGame initGame)
    {
        game = initGame;
    }
    public void makeLightRed(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(MAKE_RED_LIGHT_BUTTON_TYPE);
    }
    
    public void makeLightGreen(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(MAKE_GREEN_LIGHT_BUTTON_TYPE);
    }
    
    /**
     * Decreases speed on a particular road.
     */
    public void decSpeed(){
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        data.setSpecial(DEC_SPEED_BUTTON_TYPE);
        
        //data.slowSpeed();
    }
    
    public void incSpeed(){
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        data.setSpecial(INC_SPEED_BUTTON_TYPE);
        
        //data.increaseSpeed();
    }
    
    public void incPlayerSpeed(){
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(INC_PLAYER_SPEED_BUTTON_TYPE);
    }
    
    public void freeze(){
        // GET THE GAME'S DATA MODEL
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.freeze();
        data.setSpecial(PAUSE_BUTTON_TYPE);
    }
    
    public void closeRoad(){
        // GET THE GAME'S DATA MODEL
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(CLOSE_ROAD_BUTTON_TYPE);
        
        data.closeRoad();
    }
    
    public void closeIntersection(){
        // GET THE GAME'S DATA MODEL
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(CLOSE_INTERSECTION_BUTTON_TYPE);
        
        //data.closeIntersection();
    }
    
    public void openIntersection(){
        // GET THE GAME'S DATA MODEL
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(OPEN_INTERSECTION_BUTTON_TYPE);
        
        //data.openIntersection();
    }
    
    public void flatTire(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(FLAT_TIRE_BUTTON_TYPE);
    }
    
    public void emptyGas(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(EMPTY_GAS_BUTTON_TYPE);
    }
    
    public void intangible(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecial(INTANGIBILITY_BUTTON_TYPE);
        
        data.setIntangible();
    }
    
    public void steal(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecial(STEAL_BUTTON_TYPE);
    }
    
    public void invincible(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(INVINCIBILITY_BUTTON_TYPE);
    }
    
    public void flying(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(FLYING_BUTTON_TYPE);
    }
    
    public void mindControl(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(MIND_CONTROL_BUTTON_TYPE);
    }
    
    public void mindlessTerror(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.setSpecialSelected(true);
        
        data.setSpecial(MINDLESS_TERROR_BUTTON_TYPE);
    }
}
