package pathx;

import pathx.ui.pathXGame;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;
import static pathx.pathXConstants.*;

/**
 * This is the pathX game application. It starts the game.
 * 
 * @author Jessica Zeng
 */
public class PathX {
    static pathXGame game = new pathXGame();

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
    public enum pathXPropertyType
    {
        // LOADED FROM properties.xml
        
        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        
        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // pathx_properties.xml
                
        /* IMAGE FILE NAMES */
        IMAGE_MENU_BACKGROUND,
        IMAGE_MAP_BACKGROUND,
        IMAGE_HELP_BACKGROUND,
        IMAGE_SETTINGS_BACKGROUND,
        IMAGE_LEVEL_SELECTION_BACKGROUND,
        IMAGE_LEVEL_BACKGROUND,
        IMAGE_BUTTON_CLOSE,
        IMAGE_BUTTON_CLOSE_MOUSE_OVER,
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_PLAY_MOUSE_OVER,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_RESET_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_HOME_MOUSE_OVER,
        IMAGE_BUTTON_SCROLL_DOWN,
        IMAGE_BUTTON_SCROLL_DOWN_MOUSE_OVER,
        IMAGE_BUTTON_SCROLL_LEFT,
        IMAGE_BUTTON_SCROLL_LEFT_MOUSE_OVER,
        IMAGE_BUTTON_SCROLL_RIGHT,
        IMAGE_BUTTON_SCROLL_RIGHT_MOUSE_OVER,
        IMAGE_BUTTON_SCROLL_UP,
        IMAGE_BUTTON_SCROLL_UP_MOUSE_OVER,
        IMAGE_BUTTON_SOUND,
        IMAGE_BUTTON_SOUND_MOUSE_OVER,
        IMAGE_BUTTON_SOUND_CLICKED,
        IMAGE_BUTTON_MUSIC,
        IMAGE_BUTTON_MUSIC_MOUSE_OVER,
        IMAGE_BUTTON_MUSIC_CLICKED,
        IMAGE_BUTTON_GAME_SPEED_MOUSE_OVER,
        IMAGE_BUTTON_GAME_SPEED,
        IMAGE_BUTTON_LEVEL,
        IMAGE_BUTTON_LEVEL_DIALOG, 
        IMAGE_BUTTON_LEVEL_DIALOG_MOUSE_OVER,
        IMAGE_BUTTON_START,
        IMAGE_BUTTON_SPECIALS,
        IMAGE_BUTTON_PAUSE,
        IMAGE_BUTTON_PAUSE_MOUSE_OVER,
        IMAGE_BUTTON_CLOSE_DIALOG,
        IMAGE_BUTTON_CLOSE_DIALOG_MOUSE_OVER,
        IMAGE_BUTTON_LEAVE_TOWN,
        IMAGE_BUTTON_LEAVE_TOWN_MOUSE_OVER,
        IMAGE_BUTTON_TRY_AGAIN,
        IMAGE_BUTTON_TRY_AGAIN_MOUSE_OVER,
        IMAGE_BUTTON_GREEN_LIGHT,
        IMAGE_BUTTON_GREEN_LIGHT_MOUSE_OVER,
        IMAGE_BUTTON_RED_LIGHT,
        IMAGE_BUTTON_RED_LIGHT_MOUSE_OVER,
        IMAGE_BUTTON_FREEZE,
        IMAGE_BUTTON_FREEZE_MOUSE_OVER,
        IMAGE_BUTTON_DEC_SPEED,
        IMAGE_BUTTON_DEC_SPEED_MOUSE_OVER,
        IMAGE_BUTTON_INC_SPEED,
        IMAGE_BUTTON_INC_SPEED_MOUSE_OVER,
        IMAGE_BUTTON_PLAYER_SPEED,
        IMAGE_BUTTON_PLAYER_SPEED_MOUSE_OVER,
        IMAGE_BUTTON_FLAT_TIRE,
        IMAGE_BUTTON_FLAT_TIRE_MOUSE_OVER,
        IMAGE_BUTTON_EMPTY_GAS,
        IMAGE_BUTTON_EMPTY_GAS_MOUSE_OVER,
        IMAGE_BUTTON_CLOSE_ROAD,
        IMAGE_BUTTON_CLOSE_ROAD_MOUSE_OVER,
        IMAGE_BUTTON_CLOSE_INTERSECTION,
        IMAGE_BUTTON_CLOSE_INTERSECTION_MOUSE_OVER,
        IMAGE_BUTTON_OPEN_INTERSECTION,
        IMAGE_BUTTON_OPEN_INTERSECTION_MOUSE_OVER,
        IMAGE_BUTTON_STEAL,
        IMAGE_BUTTON_STEAL_MOUSE_OVER,
        IMAGE_BUTTON_MIND_CONTROL,
        IMAGE_BUTTON_MIND_CONTROL_MOUSE_OVER,
        IMAGE_BUTTON_INTANGIBILITY,
        IMAGE_BUTTON_INTANGIBILITY_MOUSE_OVER,
        IMAGE_BUTTON_MINDLESS_TERROR,
        IMAGE_BUTTON_MINDLESS_TERROR_MOUSE_OVER,
        IMAGE_BUTTON_FLYING,
        IMAGE_BUTTON_FLYING_MOUSE_OVER,
        IMAGE_GETAWAY_CAR,
        IMAGE_BANDIT,
        IMAGE_POLICE,
        IMAGE_ZOMBIE,
        
        IMAGE_DIALOG_LEVEL,
        
        TEXT_TITLE_BAR_GAME,
        
        LEVEL_OPTIONS,
        LEVEL_FILES,
        X_LOCATION,
        Y_LOCATION
    }
}
