/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.util.ArrayList;
import java.util.HashMap;
import properties_manager.PropertiesManager;
import pathx.PathX.pathXPropertyType;

/**
 *
 * @author Jessica
 */
public class Record {
    // HERE ARE ALL THE RECORDS
    private HashMap<String, Level> levelRecords;
    
    public Record(){
        levelRecords = new HashMap();
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        ArrayList<String> positionx = props.getPropertyOptionsList(pathXPropertyType.X_LOCATION);
        ArrayList<String> positiony = props.getPropertyOptionsList(pathXPropertyType.Y_LOCATION);
        for (int i = 0; i < levels.size(); i++){
            Level level = new Level();
            String levelFile = levels.get(i);
            level.levelname = levelFile;
            int x = Integer.parseInt(positionx.get(i));
            level.xposition = x;
            int y = Integer.parseInt(positiony.get(i));
            level.yposition = y;
            
            if(i == 0){
                level.locked = false;
            } else{
                level.locked = true;
            }
            level.levelCompleted = false;
        }
    }
}
