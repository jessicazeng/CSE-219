package pathx.ui;

import java.awt.image.BufferedImage;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.file.pathXFileManager;
import pathx.data.Record;
import pathx.PathX.pathXPropertyType;
import pathx.pathXConstants;
import properties_manager.PropertiesManager;
import static pathx.pathXConstants.*;

/**
 *
 * @author Jessica
 */
public class pathXGame  extends MiniGame{
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private Record record;

    // HANDLES GAME UI EVENTS
    private pathXEventHandler eventHandler;
    
    // HANDLES ERROR CONDITIONS
    private pathXErrorHandler errorHandler;
    
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private pathXFileManager fileManager;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    
    // Accessor Methods
    
    /**
     * This method forces the file manager to save the current player record.
     */
    public void savePlayerRecord(){
        
    }
    
    /**
     * This method switches the application to the menu screen, making
     * all the appropriate UI controls visible & invisible.
     */    
    public void switchToMenuScreen(){
         
    }
     
    public void switchToSettingsScreen(){
         
    }
     
    public void switchToHelpScreen(){
         
    }
     
    public void switchToLevelSelectionScreen(){
         
    }
    
    @Override
    public void initGUIHandlers(){
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);        
        //String windowIconFile = props.getProperty(pathXPropertyType.IMAGE_WINDOW_ICON);
        //img = loadImage(imgPath + windowIconFile);
        //window.setIconImage(img);
        
         // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        //canvas = new SortingHatPanel(this, (SortingHatDataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MENU_BACKGROUND));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MAP_BACKGROUND));
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        // load cursor?
        
        
    }
    
    @Override
    public void initGUIControls(){
        
    }
    
    /**
     * Initializes the game data used by the application.
     */
    @Override
    public void initData(){
        // INIT OUR ERROR HANDLER
        //errorHandler = new pathXErrorHandler(window);
        
        // INIT OUR FILE MANAGER
        //fileManager = new pathXFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        //record = fileManager.loadRecord();
        
        // INIT OUR DATA MANAGER
        //data = new pathXDataModel(this);
    }
    
    @Override
    public void initAudioContent(){
        
    }
    
    public void reset(){
        
    }
    
    @Override
    public void updateGUI(){
        
    }
}
