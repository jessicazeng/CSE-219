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
import pathx.data.Intersection;
import pathx.data.Record;
import pathx.data.Road;
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
            Record record = miniGame.getPlayerRecord();
            
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
                    
                    // MAKE ARRAY LIST TO STORE INTERSECTIONS AS WE READ THEM IN
                    ArrayList<Intersection> intersections = new ArrayList();
                    
                    // READ IN NUMBER OF INTERSECTIONS IN THIS LEVEL
                    int numIntersections = dis.readInt();
                    
                    // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
                    for (int j = 0; j < numIntersections; j++){
                        // GET THEIR DATA FROM THE DOC
                        int x = dis.readInt();
                        int y = dis.readInt();
                        boolean isOpen = dis.readBoolean();
                        
                        // NOW MAKE AND ADD THE INTERSECTION
                        Intersection newIntersection = new Intersection(x, y);
                        newIntersection.setOpen(isOpen);
                        intersections.add(newIntersection);
                    }
                    
                    // add list of intersections into the level record
                    record.addIntersections(levelFile, intersections);
                    
                    // make array list to store roads
                    ArrayList<Road> roads = new ArrayList();
                    
                    // read in number of road
                    int numRoads = dis.readInt();
                    
                    // AND THEN GO THROUGH AND ADD ALL THE LISTED ROADS
                    for (int k = 0; k < numRoads; k++)
                    {
                        // GET THEIR DATA FROM THE DOC
                        int int_id1 = dis.readInt();
                        int int_id2 = dis.readInt();
                        boolean oneWay = dis.readBoolean();
                        int speedLimit = dis.readInt();
            
                        // NOW MAKE AND ADD THE ROAD
                        Road newRoad = new Road();
                        newRoad.setNode1(intersections.get(int_id1));
                        newRoad.setNode2(intersections.get(int_id2));
                        newRoad.setOneWay(oneWay);
                        newRoad.setSpeedLimit(speedLimit);
                        roads.add(newRoad);
                    }
                    
                    record.addRoads(levelFile, roads);
                    
                    // LOAD THE START INTERSECTION
                    int startId = dis.readInt();
                    String startImageName = "./pathx/" + dis.readUTF();
                    
                    record.addStartImage(levelFile, startImageName);
                    
                    // LOAD THE DESTINATION
                    int destId = dis.readInt();
                    String destImageName = "./pathx/" + dis.readUTF();
                    
                    record.addEndImage(levelFile, destImageName);
                    
                    // LOAD THE MONEY
                    int money = dis.readInt();
                    
                    record.setMoney(levelFile, money);
                    
                    // LOAD THE NUMBER OF POLICE
                    int numPolice = dis.readInt();
                    record.setNumPolice(levelFile, numPolice);
                    
                    // LOAD THE NUMBER OF BANDITS
                    int numBandits = dis.readInt();
                    record.setNumBandits(levelFile, numBandits);
            
                    // LOAD THE NUMBER OF ZOMBIES
                    int numZombies = dis.readInt();
                    record.setNumZombies(levelFile, numZombies);
                    
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
