package pathx.ui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import mini_game.MiniGameDataModel;
import pathx.PathX;
import pathx.data.Record;
import static pathx.pathXConstants.*;
import pathx.data.pathXDataModel;
import pathx.file.pathXFileManager;import properties_manager.PropertiesManager;
;

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
        game.getAudio().stop(PathX.pathXPropertyType.AUDIO_LOSS.toString());
        
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        game.switchToLevelSelectionScreen();
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
    
    public void respondToMusicPressRequest(String buttontype){
        game.setClickedMusicButton(buttontype);
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
        if (game.isCurrentScreenState(LEVEL_SCREEN_STATE))
        {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            pathXDataModel data = (pathXDataModel)game.getDataModel();
        
            // UPDATE THE DATA
            pathXFileManager fileManager = game.getFileManager();
            fileManager.loadLevel(levelFile);
            data.reset(game);
            
            if(data.isFrozen())
                data.freeze();
            //data.slowSpeed(false);

            // GO TO THE GAME
            game.switchToLevelScreen();
        }      
    }
    
    public void respondToTryAgainRequest(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        pathXDataModel data = (pathXDataModel)game.getDataModel();
            
        // UPDATE THE DATA
        data.reset(game);
        
        game.getAudio().stop(PathX.pathXPropertyType.AUDIO_LOSS.toString());
            
        // GO TO THE GAME
        game.switchToLevelScreen();
    }
    
    public void respondToPauseRequest(){
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
        //if(data.isPaused() == true){
            //data.unpause();
            //game.enableSpecials();
            data.freeze();
        //} else{
            //data.pause();
            //game.disableSpecials();
        //}
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
        pathXDataModel data = (pathXDataModel)game.getDataModel();
        
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
        if (keyCode == KeyEvent.VK_I){
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && !data.isPaused())
                data.incMoney(10);
        }
        if (keyCode == KeyEvent.VK_J){
            Record record = game.getPlayerRecord();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            ArrayList<String> levels = props.getPropertyOptionsList(PathX.pathXPropertyType.LEVEL_OPTIONS);
            
            int i = 0;
            while(!record.isLocked(levels.get(i)) && i<(levels.size()-1)){
                i++;
            }
            i--;
            if(i < levels.size()){
                pathXFileManager fileManager = game.getFileManager();
                fileManager.loadLevel(levels.get(i));
                record.unlockNextLevel(levels.get(i));
            }
        }
        
        // Keys for specials
        
        // green light
        if (keyCode == KeyEvent.VK_G){
            data.setSpecialSelected(true);

            data.setSpecial(MAKE_GREEN_LIGHT_BUTTON_TYPE);
        }
        
        // red light
        if (keyCode == KeyEvent.VK_R){
            data.setSpecialSelected(true);
        
            data.setSpecial(MAKE_RED_LIGHT_BUTTON_TYPE);
        }
        
        // dec speed
        if (keyCode == KeyEvent.VK_Z){
            data.setSpecialSelected(true);
        
            data.setSpecial(DEC_SPEED_BUTTON_TYPE);
        }
        
        // inc speed
        if (keyCode == KeyEvent.VK_X){
            data.setSpecialSelected(true);
        
            data.setSpecial(INC_SPEED_BUTTON_TYPE);
        }
        
        // inc player speed
        if (keyCode == KeyEvent.VK_P){
            data.incPlayerSpeed();
        }
        
        // flat tire
        if (keyCode == KeyEvent.VK_T){
            data.setSpecialSelected(true);
        
            data.setSpecial(FLAT_TIRE_BUTTON_TYPE);
        }
        
        // empty gas tank
        if (keyCode == KeyEvent.VK_E){
            data.setSpecialSelected(true);
        
            data.setSpecial(EMPTY_GAS_BUTTON_TYPE);
        }
        
        // close intersection
        if (keyCode == KeyEvent.VK_C){
            data.setSpecialSelected(true);
        
            data.setSpecial(CLOSE_INTERSECTION_BUTTON_TYPE);
        }
        
        // open intersection
        if (keyCode == KeyEvent.VK_O){
            data.setSpecialSelected(true);
        
            data.setSpecial(OPEN_INTERSECTION_BUTTON_TYPE);
        }
        
        // steal
        if (keyCode == KeyEvent.VK_Q){
            data.setSpecial(STEAL_BUTTON_TYPE);
        }
        
        // intangibility
        if (keyCode == KeyEvent.VK_B){
            data.setSpecial(INTANGIBILITY_BUTTON_TYPE);
        
            data.setIntangible();
        }
        
        // invinsible
        if (keyCode == KeyEvent.VK_V){
            data.setSpecial(INVINCIBILITY_BUTTON_TYPE);
        }
        
        // flying
        if (keyCode == KeyEvent.VK_V){
            data.setSpecialSelected(true);
        
            data.setSpecial(FLYING_BUTTON_TYPE);
        }
    }
}
