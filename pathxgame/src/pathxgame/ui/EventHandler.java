/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.ui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import pathxgame.data.DataModel;
import pathxgame.file.FileManager;
import static pathxgame.pathXConstants.*;
import properties_manager.PropertiesManager;

/**
 * This class handles all the events and responds to them.
 * 
 * @author Jessica Zeng
 */
public class EventHandler {
    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private Game game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public EventHandler(Game initGame)
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
    public void respondToLevelSelectionScreenRequest(){
        game.switchToLevelSelectionScreen();
    }

    public void switchToSplashScreenRequest() {
        game.switchToSplashScreen();
    }
    
    public void switchToSettingsScreenRequest(){
        game.switchToSettingsScreen();
    }
    
    public void switchToHelpScreenRequest(){
        game.switchToHelpScreen();
    }
    
    public void respondToScrollRequest(String buttontype){
        game.scroll(buttontype);
    }
    
    /**
     * Called when the user clicks a button to select a level.
     */    
    public void respondToSelectLevelRequest(String levelFile){
        if (game.isCurrentScreenState(LEVEL_SCREEN_STATE))
        {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            DataModel data = (DataModel)game.getDataModel();

            // UPDATE THE DATA
            FileManager fileManager = game.getFileManager();
            fileManager.loadLevel(levelFile);
            data.reset(game);
            
            // GO TO THE GAME
            game.switchToGameScreen();
        }      
    }
    
    public void respondToTryAgainRequest(){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        DataModel data = (DataModel)game.getDataModel();
            
        // UPDATE THE DATA
        data.reset(game);
            
        // GO TO THE GAME
        game.switchToGameScreen();
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode){
        DataModel data = (DataModel)game.getDataModel();
        
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
    
    //----------------------------SPECIALS HANDERS-----------------------------
    public void respondToSpecialSelcted(String type){
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        DataModel data = (DataModel)game.getDataModel();
        
        data.setSpecialSelected();
        data.setSpecial(type);
        
        if(type.equals(FREEZE_BUTTON_TYPE))
            data.setFreeze();
        else if(type.equals(INCPLAYERSPEED_BUTTON_TYPE))
            data.incPlayerSpeed();
    }
}
