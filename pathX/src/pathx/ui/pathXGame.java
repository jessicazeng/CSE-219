package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.JFrame;
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
import pathx.data.pathXDataModel;

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
    public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }
    
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
         // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        
        // ACTIVATE THE MENU BUTTONS
        // DEACTIVATE THE MENU BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(true);
        
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;
        
        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
    }
     
    public void switchToSettingsScreen(){
         
    }
     
    public void switchToHelpScreen(){
         
    }
     
    public void switchToLevelSelectionScreen(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCREEN_STATE);
        
        // DEACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(false);
    }
    
    @Override
    public void initGUIHandlers(){
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new pathXEventHandler(this);
        
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) 
            { eventHandler.respondToExitRequest(); }
        });
        
        // PLAY EVENT HANDLER
        guiButtons.get(PLAY_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToPlayRequest();     }
        });
    }
    
    @Override
    public void initGUIControls(){
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
        canvas = new pathXPanel(this, (pathXDataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MENU_BACKGROUND));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MAP_BACKGROUND));
        sT.addState(LEVEL_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        // load cursor?
        
        // ADD A BUTTON FOR EACH MENU OPTION
        float totalWidth = 4 * (MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN) - MENU_BUTTON_MARGIN;
        Viewport viewport = data.getViewport();
        x = (viewport.getScreenWidth() - totalWidth)/2.0f;
        
        String playButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PLAY);
        sT = new SpriteType(PLAY_BUTTON_TYPE);
        img = loadImage(imgPath + playButton);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String playMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PLAY_MOUSE_OVER);
        img = loadImage(imgPath + playMouseOverButton);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, pathXStates.VISIBLE_STATE.toString());
        guiButtons.put(PLAY_BUTTON_TYPE, s);
        x += MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN;
        
        String resetButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RESET);
        sT = new SpriteType(RESET_BUTTON_TYPE);
        img = loadImage(imgPath + resetButton);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String resetMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RESET_MOUSE_OVER);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, pathXStates.VISIBLE_STATE.toString());
        guiButtons.put(RESET_BUTTON_TYPE, s);
        x += MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN;
        
        String settingsButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SETTINGS);
        sT = new SpriteType(SETTINGS_BUTTON_TYPE);
        img = loadImage(imgPath + settingsButton);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String settingsMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SETTINGS_MOUSE_OVER);
        img = loadImage(imgPath + settingsMouseOverButton);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, pathXStates.VISIBLE_STATE.toString());
        guiButtons.put(SETTINGS_BUTTON_TYPE, s);
        x += MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN;
        
        String helpButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HELP);
        sT = new SpriteType(HELP_BUTTON_TYPE);
        img = loadImage(imgPath + helpButton);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String helpMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HELP_MOUSE_OVER);
        img = loadImage(imgPath + helpMouseOverButton);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, pathXStates.VISIBLE_STATE.toString());
        guiButtons.put(HELP_BUTTON_TYPE, s);
        
        String closeButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
        img = loadImage(imgPath + closeButton);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String closeMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = loadImage(imgPath + closeMouseOverButton);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_BUTTON_X, CLOSE_BUTTON_Y, 0, 0, pathXStates.VISIBLE_STATE.toString());
        guiButtons.put(CLOSE_BUTTON_TYPE, s);
        
        // ADD THE CONTROLS IN THE LEVEL SELECTION SCREEN
        // THEN THE DOWN BUTTON
        String downButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN);
        sT = new SpriteType(SCROLL_DOWN_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + downButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String downMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + downMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_BUTTON_X, DOWN_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_DOWN_BUTTON_TYPE, s);
        
        // THEN THE UP BUTTON
        String upButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_UP);
        sT = new SpriteType(SCROLL_UP_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + upButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String upMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_UP_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + upMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_BUTTON_X, UP_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_UP_BUTTON_TYPE, s);
        
        // THEN THE LEFT BUTTON
        String leftButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT);
        sT = new SpriteType(SCROLL_LEFT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + leftButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String leftMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + leftMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEFT_BUTTON_X, LEFT_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_LEFT_BUTTON_TYPE, s);
        
        // THEN THE RIGHT BUTTON
        String rightButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT);
        sT = new SpriteType(SCROLL_RIGHT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + rightButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String rightMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + rightMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RIGHT_BUTTON_X, RIGHT_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_RIGHT_BUTTON_TYPE, s);
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
        data = new pathXDataModel(this);
    }
    
    @Override
    public void initAudioContent(){
        
    }
    
    public void reset(){
        
    }
    
    /**
     * Updates the state of all gui controls according to the 
     * current game conditions.
     */
    @Override
    public void updateGUI(){
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(pathXStates.VISIBLE_STATE.toString()))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(pathXStates.MOUSE_OVER_STATE.toString());
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(pathXStates.MOUSE_OVER_STATE.toString()))
            {
                 if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(pathXStates.VISIBLE_STATE.toString());
                }
            }
        }
    }
}
