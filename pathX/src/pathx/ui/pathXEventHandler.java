package pathx.ui;

import java.awt.event.KeyEvent;
import static pathx.pathXConstants.*;
import pathx.data.pathXDataModel;
import pathx.file.pathXFileManager;;

/**
 * This class handles all the events and responds to them.
 * 
 * @author Jessica Zeng
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
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(MENU_SCREEN_STATE))
        {
            // GO TO THE MAP
            game.switchToLevelSelectionScreen();
        }      
    }
    
    /**
     * Called when the user clicks on the reset button in the
     * menu. Resets game and erases all player records.
     */
    public void respondToResetRequest(){
        
    }
    
    public void respondToCloseDialogRequest(){
        game.closeDialog();
    }
    
    public void respondToSoundPressRequest(String buttontype){
        game.setClickedSoundButton(buttontype);
    }
    
    public void respondToSwitchToHomeScreenRequest(){
        game.switchToMenuScreen();
    }
    
    /**
     * Called when the user clicks a button to select a level.
     */    
    public void respondToSelectLevelRequest(String levelFile){
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(LEVEL_SCREEN_STATE))
        {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            pathXDataModel data = (pathXDataModel)game.getDataModel();
        
            // UPDATE THE DATA
            pathXFileManager fileManager = game.getFileManager();
            fileManager.loadLevel(levelFile);
            data.reset(game);

            // GO TO THE GAME
            game.switchToLevelScreen();
        }      
    }
    
    /**
     * Called when user clicks on settings button.
     */
    public void respondToSwitchToSettingsScreenRequest(){
        game.switchToSettingsScreen();
    }
    
    /**
     * Called when user clicks on help button.
     */
    public void respondToSwitchToHelpScreenRequest(){
        game.switchToHelpScreen();
    }
    
    public void respondToScrollRequest(String buttontype){
        game.scroll(buttontype);
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode){
        if (keyCode == KeyEvent.VK_LEFT){
            respondToScrollRequest(SCROLL_LEFT_BUTTON_TYPE);
        }
        if (keyCode == KeyEvent.VK_DOWN){
            respondToScrollRequest(SCROLL_DOWN_BUTTON_TYPE);
        }
        if (keyCode == KeyEvent.VK_UP){
            respondToScrollRequest(SCROLL_UP_BUTTON_TYPE);
        }
        if (keyCode == KeyEvent.VK_RIGHT){
            respondToScrollRequest(SCROLL_RIGHT_BUTTON_TYPE);
        }
    }
}
