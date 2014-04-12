package pathx.ui;

import static pathx.pathXConstants.LEVEL_SCREEN_STATE;
import static pathx.pathXConstants.MENU_SCREEN_STATE;
import pathx.data.pathXDataModel;
import pathx.file.pathXFileManager;

/**
 *
 * @author Jessica
 */
public class pathXEventHandler {
    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private pathXGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public pathXEventHandler(pathXGame initGame)
    {
        game = initGame;
    }
    
    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }
    
    /**
     * Called when the user clicks the play button in the menu
     */
    public void respondToPlayRequest(){
        
    }
    
    /**
     * Called when the user clicks on the reset button in the
     * menu. Resets game and erases all player records.
     */
    public void respondToResetRequest(){
        
    }
    
    /**
     * Called when the user clicks a button to select a level.
     */    
    public void respondToSelectLevelRequest(String levelFile){
        
    }
    
    /**
     * Called when user clicks on settings button.
     */
    public void respondToSwitchToSettingsScreenRequest(){
        
    }
    
    /**
     * Called when user clicks on help button.
     */
    public void respondToSwitchToHelpScreenRequest(){
        
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode){
        
    }
}
