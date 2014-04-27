package pathx.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import static pathx.pathXConstants.*;
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
                if((levels.get(i)).matches(levelFile)){
                    String levelFileName = PATH_DATA + levelFiles.get(i);
                    
                    File fileToOpen = new File(levelFileName);
                    byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    FileInputStream fis = new FileInputStream(fileToOpen);
                    BufferedInputStream bis = new BufferedInputStream(fis);
            
                    // HERE IT IS, THE ONLY READY REQUEST WE NEED
                    bis.read(bytes);
                    bis.close();
                    
                    // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
                    DataInputStream dis = new DataInputStream(bais);
                    
                    // FIRST READ THE ALGORITHM NAME TO USE FOR THE LEVEL
                    String levelName = dis.readUTF();
                    
                    // THEN GET THE BACKGROUND IMAGE NAME
                    String bgImageName = "./pathx/" + dis.readUTF();
                    
                    
                    Record record = miniGame.getPlayerRecord();
                    
                    // set level background image
                    record.setLevelImage(levelFile, bgImageName);
                    
                    pathXDataModel dataModel = (pathXDataModel)miniGame.getDataModel();
                    dataModel.setCurrentLevel(levelFile);
                }
            }
        } catch(IOException e){
            
        }
    }
}
