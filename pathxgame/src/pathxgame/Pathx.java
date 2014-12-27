/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame;

import static pathxgame.pathXConstants.*;
import pathxgame.ui.Game;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 *
 * @author Jessica
 */
public class Pathx {
    static Game game = new Game();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            
            // THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(pathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);

            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(pathXPropertyType.TEXT_TITLE_BAR_GAME);
            game.initMiniGame(appTitle, 30, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // GET THE PROPER WINDOW DIMENSIONS
            game.startGame();
        } catch(InvalidXMLFileFormatException ixmlffe){
            
        }
    }
    
    /**
     * pathXPropertyType represents the types of data that will need
     * to be extracted from XML files.
     */
    public enum pathXPropertyType{
        FILE_GAME_PROPERTIES, 
        TEXT_TITLE_BAR_GAME,
        
        LEVEL_OPTIONS,
        LEVEL_FILES,
        X_LOCATION,
        Y_LOCATION,
        
        PATH_IMG, 
        
        IMAGE_MENU_BACKGROUND,
        IMAGE_LEVELSELECTION_BACKGROUND,
        IMAGE_MAP,
        IMAGE_LEVELSELECTION_PANE,
        IMAGE_GAMESCREEN_BACKGROUND,
        IMAGE_GAMESCREEN_PANE,
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_PLAY_MOUSE_OVER,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_RESET_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_HOME_MOUSE_OVER,
        IMAGE_BUTTON_BACK,
        IMAGE_BUTTON_BACK_MOUSE_OVER,
        IMAGE_BUTTON_LEAVETOWN,
        IMAGE_BUTTON_LEAVETOWN_MOUSE_OVER,
        IMAGE_BUTTON_TRYAGAIN,
        IMAGE_BUTTON_TRYAGAIN_MOUSE_OVER,
        IMAGE_POLICE,
        IMAGE_PLAYER,
        DIALOG_LEVEL
    }
}
