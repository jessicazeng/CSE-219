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
        // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
        //String appTitle = props.getProperty(SortingHatPropertyType.TEXT_TITLE_BAR_GAME);
        game.initMiniGame("pathX", 30, 750, 650);
            
        // GET THE PROPER WINDOW DIMENSIONS
        game.startGame();
    }
    
}
