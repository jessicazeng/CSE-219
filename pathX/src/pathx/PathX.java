package pathx;

import pathx.ui.pathXGame;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;
import static pathx.pathXConstants.*;

/**
 *
 * @author Jessica
 */
public class PathX {
    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
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
            game.initMiniGame(appTitle, 30, 750, 650);
            
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
        
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // sorting_hat_properties.xml
                
        /* IMAGE FILE NAMES */
        IMAGE_MENU_BACKGROUND,
        IMAGE_MAP_BACKGROUND,
        IMAGE_BUTTON_CLOSE,
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_PLAY_MOUSE_OVER,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_RESET_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_SCROLL_DOWN,
        IMAGE_BUTTON_SCROLL_LEFT,
        IMAGE_BUTTON_SCROLL_RIGHT,
        IMAGE_BUTTON_SCROLL_UP,
        
        TEXT_TITLE_BAR_GAME
    }
}
