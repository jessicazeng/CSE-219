package pathx.file;

import java.io.File;
import java.util.ArrayList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pathx.PathX;
import pathx.data.Record;
import pathx.data.pathXDataModel;
import pathx.ui.pathXGame;
import properties_manager.PropertiesManager;

/**
 *
 * @author Jessica
 */
public class pathXFileManager {
    private pathXGame miniGame;
    
    /**
     * Constructor for initializing this file manager.
     * 
     * @param initMiniGame The game for which this class loads data.
     */
    public pathXFileManager(pathXGame initMiniGame)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }
    
    /**
     * This method loads the contents of the levelFile argument so that
     * the player may then play that level. 
     * 
     * @param levelFile Level to load.
     */
    public void loadLevel(String levelFile){
        try{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            ArrayList<String> levels = props.getPropertyOptionsList(PathX.pathXPropertyType.LEVEL_OPTIONS);
            ArrayList<String> levelFiles = props.getPropertyOptionsList(PathX.pathXPropertyType.LEVEL_FILES);
            //find the level
            for (int i = 0; i < levels.size(); i++){ 
                if(levels.get(i).matches(levelFile)){
                    String gameImage = "./pathx/californiabg.png";
                    
                    Record record = miniGame.getPlayerRecord();
                    
                    // set level background image
                    record.setLevelImage(levelFile, gameImage);
                    
                    pathXDataModel dataModel = (pathXDataModel)miniGame.getDataModel();
                    dataModel.setCurrentLevel(levelFile);
                }
            }
        } catch(Exception e){
            
        }
    }
}
