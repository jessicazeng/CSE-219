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
    
    public void decSpeed(){
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.slowSpeed(true);
    }
    
    public void incSpeed(){
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.increaseSpeed(true);
    }
    
    public void incPlayerSpeed(){
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.incPlayerSpeed();
    }
    
    public void freeze(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        data.freeze();
        data.setSpecial(FREEZE_UNFREEZE_BUTTON_TYPE);
    }
}
