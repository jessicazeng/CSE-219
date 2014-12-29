/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import pathxgame.Pathx;
import pathxgame.data.DataModel;
import pathxgame.data.Intersection;
import pathxgame.data.Record;
import pathxgame.data.Road;
import static pathxgame.pathXConstants.*;
import pathxgame.ui.Game;
import properties_manager.PropertiesManager;

/**
 *
 * @author Jessica
 */
public class FileManager {
     private Game miniGame;
    
    /**
     * Constructor for initializing this file manager.
     * 
     * @param initMiniGame The game for which this class loads data.
     */
    public FileManager(Game initMiniGame)
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
            ArrayList<String> levels = props.getPropertyOptionsList(Pathx.pathXPropertyType.LEVEL_OPTIONS);
            Record record = miniGame.getPlayerRecord();
            
            //find the level
            for (int i = 0; i < levels.size(); i++){ 
                if((levels.get(i)).matches(levelFile)){
                    String file = record.getFileName(levels.get(i));
                    String levelFileName = PATH_DATA + "./pathx/" + file + ".bin";
                    
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
                    
                    // FIRST READ THE LEVEL NAME TO USE FOR THE LEVEL
                    String levelName = dis.readUTF();
                    
                    // THEN GET THE BACKGROUND IMAGE NAME
                    String bgImageName = "./" + dis.readUTF();
                    
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
                        Intersection newIntersection = new Intersection(j, x, y);
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
                    String startImageName = "./" + dis.readUTF();
                    
                    record.addStartImage(levelFile, startImageName);
                    
                    // LOAD THE DESTINATION
                    int destId = dis.readInt();
                    String destImageName = "./" + dis.readUTF();
                    
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
                    
                    record.setLevelName(levelFile, levelName);
                    
                    record.setCity(levelFile, levels.get(i));
                    
                    if(i != levels.size()-1)
                        record.setNextLevel(levelFile, levels.get(i+1));
                    
                    DataModel dataModel = (DataModel)miniGame.getDataModel();
                    dataModel.setCurrentLevel(levelFile);
                    
                    dis.close();
                }
            }
        } catch(IOException e){
            
        }
    }
}
