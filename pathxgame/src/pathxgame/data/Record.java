/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.data;

import java.util.ArrayList;
import java.util.HashMap;
import pathxgame.Pathx.pathXPropertyType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Jessica
 */
public class Record {
    // HERE ARE ALL THE RECORDS
    private HashMap<String, Level> levelRecords;
    
    // total amount of money earned for all levels won
    private int money;
    
    public Record(){
        levelRecords = new HashMap();
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        ArrayList<String> positionx = props.getPropertyOptionsList(pathXPropertyType.X_LOCATION);
        ArrayList<String> positiony = props.getPropertyOptionsList(pathXPropertyType.Y_LOCATION);
        ArrayList<String> files = props.getPropertyOptionsList(pathXPropertyType.LEVEL_FILES);
        
        for (int i = 0; i < levels.size(); i++){
            Level level = new Level();
            String levelFile = levels.get(i);
            level.levelname = levelFile;
            int x = Integer.parseInt(positionx.get(i));
            level.xposition = x;
            int y = Integer.parseInt(positiony.get(i));
            level.yposition = y;
            String file = files.get(i);
            level.fileName = file;
            
            if(i == 0){
                level.locked = false;
            } else{
                level.locked = true;
            }
            level.levelCompleted = false;
            
            levelRecords.put(levelFile, level);
        }
        
        money = 0;
    }
    
    //------------------Accessor Methods-----------------------------------
    
    public int getLevelPositionX(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.xposition;
    }
    
    public int getLevelPositionY(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.yposition;
    }
    
    public boolean isLocked(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.locked;
    }
    
    public boolean isLevelCompleted(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.levelCompleted;
    }
    
    public String getFileName(String levelname){
        Level level = levelRecords.get(levelname);
        
        return level.fileName;
    }
    
    public String getLevelImage(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.levelImage;
    }
    
    public String getCity(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.city;
    }
    
    public int getBalance(){
        return money;
    }
    
    public ArrayList<Intersection> getIntersections(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.intersections;
    }
    
    public ArrayList<Road> getRoads(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.roads;
    }
    
    public String getStartImage(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.startImage;
    }
    
    public String getEndImage(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.endImage;
    }
    
    public String getLevelName(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.levelname;
    }
    
    public int getMoney(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.money;
    }
    
    public int getNumPolice(String levelName){
        Level level = levelRecords.get(levelName);
        return level.numPolice;
    }
    
    public int getNumZombies(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.numZombies;
    }
    
    public int getNumBandits(String levelName){
        Level level = levelRecords.get(levelName);
        
        return level.numBandits;
    }
    
    //--------------------Mutator Methods-----------------------------------
    
    public void addIntersections(String levelName, ArrayList<Intersection> newIntersections){
        Level level = levelRecords.get(levelName);
        
        level.intersections = newIntersections;
    }
    
    public void addRoads(String levelName, ArrayList<Road> newRoads){
        Level level = levelRecords.get(levelName);
        
        level.roads = newRoads;
    }
    
    public void addStartImage(String levelName, String imagename){
        Level level = levelRecords.get(levelName);
        level.startImage = imagename;
    }
    
    public void addEndImage(String levelName, String imagename){
        Level level = levelRecords.get(levelName);
        level.endImage = imagename;
    }
    
    public void setLevelName(String levelName, String name){
        Level level = levelRecords.get(levelName);
        
        level.levelname = name;
    }
    
    public void setLevelImage(String levelName, String imgPath){
         Level level = levelRecords.get(levelName);
         
         level.levelImage = imgPath;
    }
    
    public void setNumZombies(String levelName, int num){
        Level level = levelRecords.get(levelName);
        level.numZombies = num;
    }
    
    public void setNumBandits(String levelName, int num){
        Level level = levelRecords.get(levelName);
        level.numBandits = num;
    }
    
    public void setMoney(String levelName, int newMoney){
        Level level = levelRecords.get(levelName);
        
        level.money = newMoney;
    }
    
    public void setCity(String levelName, String newCity){
        Level level = levelRecords.get(levelName);
        
        level.city = newCity;
    }
    
    public void setNumPolice(String levelName, int num){
        Level level = levelRecords.get(levelName);
        level.numPolice = num;
    }
    
    public void incBalance(int amt){
        money += amt;
    }
    
    /**
     * Called when a level is completed & changes the state of a level
     * @param levelName 
     */
    public void completedLevel(String levelName){
        Level level = levelRecords.get(levelName);
        
        level.levelCompleted = true;
        
        if(level.nextLevel != null){
            String next = level.nextLevel;
            Level nextLev = levelRecords.get(next);
            nextLev.locked = false;
        }
    }
    
    public void setNextLevel(String levelName, String nextLevelName){
        Level level = levelRecords.get(levelName);
        
        level.nextLevel = nextLevelName;
    }
}
