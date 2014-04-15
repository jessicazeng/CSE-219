package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
    
    public Record getPlayerRecord(){
        return record;
    }
    
    /**
     * This method forces the file manager to save the current player record.
     */
    public void savePlayerRecord(){
        
    }
    
    public void displayDialogs(){
        
    }
    
    public void scroll(String buttontype){
        Viewport viewport = data.getViewport();
        
        if(buttontype == SCROLL_RIGHT_BUTTON_TYPE){
            int maxWidth = viewport.getGameWorldWidth() - viewport.getViewportWidth();
            if(viewport.getViewportX() < maxWidth)
                viewport.scroll(MAP_SCROLL_INC, 0);
        } else if(buttontype == SCROLL_LEFT_BUTTON_TYPE){
            if(viewport.getViewportX() > 0)
                viewport.scroll(0-MAP_SCROLL_INC, 0);
        } else if(buttontype == SCROLL_UP_BUTTON_TYPE){
            if(viewport.getViewportY() > 0)
                viewport.scroll(0, 0-MAP_SCROLL_INC);
        } else if(buttontype == SCROLL_DOWN_BUTTON_TYPE){
            int maxHeight = viewport.getGameWorldHeight() - viewport.getViewportHeight();
            if(viewport.getViewportY() < maxHeight)
                viewport.scroll(0, MAP_SCROLL_INC);
        }
    }
    
    /**
     * This method switches the application to the menu screen, making
     * all the appropriate UI controls visible & invisible.
     */    
    public void switchToMenuScreen(){
         // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;
        
        // ACTIVATE THE MENU BUTTONS
        // DEACTIVATE THE MENU BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setEnabled(false);
        
        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
    }
    
    public void switchToLevelScreen(String levelName){
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        currentScreenState = GAME_SCREEN_STATE;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(true);
        
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        
        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.IN_PROGRESS);
    }
     
    public void switchToSettingsScreen(){
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
        guiButtons.get(SOUND_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SOUND_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(MUSIC_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
         
        // DEACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
    }
     
    public void switchToHelpScreen(){
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
         
        // DEACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setEnabled(false);
    }
     
    public void switchToLevelSelectionScreen(){
        BufferedImage img;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);   
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCREEN_STATE);
        
        currentScreenState = LEVEL_SCREEN_STATE;
        
        // DEACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_BUTTON_TYPE).setEnabled(false);
        
        //ACTIVATE LEVEL SELECTION SCREEN BUTTONS
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String level : levels)
        {
            guiButtons.get(level).setState(pathXStates.VISIBLE_STATE.toString());
            guiButtons.get(level).setEnabled(true);
        }        
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
        
        // SETTINGS EVENT HANDLER
        guiButtons.get(SETTINGS_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSwitchToSettingsScreenRequest();     }
        });
        
        // SCROLL RIGHT EVENT HANDLER
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_RIGHT_BUTTON_TYPE);     }
        });
        
        // SCROLL LEFT EVENT HANDLER
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_LEFT_BUTTON_TYPE);     }
        });
        
        // SCROLL LEFT EVENT HANDLER
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_DOWN_BUTTON_TYPE);     }
        });
        
        // SCROLL LEFT EVENT HANDLER
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_UP_BUTTON_TYPE);     }
        });
        
        // SOUND EVENT HANDLER
        guiButtons.get(SOUND_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSoundPressRequest(SOUND_BUTTON_TYPE);     }
        });
        
        // MUSIC EVENT HANDLER
        guiButtons.get(MUSIC_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSoundPressRequest(MUSIC_BUTTON_TYPE);     }
        });
        
        // HOME BUTTON IN SETTINGS SCREEN EVENT HANDLER
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSwitchToHomeScreenRequest();     }
        });
        
        // CLOSE BUTTON SCREEN EVENT HANDLER
        guiButtons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToExitRequest();     }
        });
        
        // CLOSE BUTTON SCREEN EVENT HANDLER
        guiButtons.get(HELP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSwitchToHelpScreenRequest();     }
        });
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke)
            {   
                eventHandler.respondToKeyPress(ke.getKeyCode());    
            }
        });
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        for (String levelFile : levels)
        {
            Sprite levelButton = guiButtons.get(levelFile);
            levelButton.setActionCommand(PATH_DATA + levelFile);
            levelButton.setActionListener(new ActionListener(){
                Sprite s;
                public ActionListener init(Sprite initS) 
                {   s = initS; 
                    return this;    }
                public void actionPerformed(ActionEvent ae)
                {   eventHandler.respondToSelectLevelRequest(s.getActionCommand());    }
            }.init(levelButton));
        }   
    }
    
    public void setClickedSoundButton(String buttontype){
        if(guiButtons.get(buttontype).getState() == pathXStates.SELECTED_STATE.toString()){
            guiButtons.get(buttontype).setState(pathXStates.VISIBLE_STATE.toString());
        } else{
            guiButtons.get(buttontype).setState(pathXStates.SELECTED_STATE.toString());
        }
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
        
        Viewport viewport = data.getViewport();
        viewport.setViewportSize(MAP_WIDTH, MAP_HEIGHT);
        viewport.setGameWorldSize(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        
         // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new pathXPanel(this, (pathXDataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MENU_BACKGROUND));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MAP_BACKGROUND));
        //img = img.getSubimage(0, 300, 635, 500);
        sT.addState(LEVEL_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_SETTINGS_BACKGROUND));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_HELP_BACKGROUND));
        sT.addState(HELP_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_LEVEL_BACKGROUND));
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        // load cursor?
        
        // ADD A BUTTON FOR EACH MENU OPTION
        float totalWidth = 4 * (MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN) - MENU_BUTTON_MARGIN;
        //Viewport viewport = data.getViewport();
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
        img = loadImageWithColorKey(imgPath + closeButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String closeMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + closeMouseOverButton, COLOR_KEY);
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
        
        // THEN THE SOUND BUTTON
        String soundButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SOUND);
        sT = new SpriteType(SOUND_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + soundButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String soundMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SOUND_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + soundMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        String soundSelectedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SOUND_CLICKED);
        img = loadImageWithColorKey(imgPath + soundSelectedButton, COLOR_KEY);
        sT.addState(pathXStates.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, SOUND_BUTTON_X, SOUND_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(SOUND_BUTTON_TYPE, s);
        
        // THEN THE MUSIC BUTTON
        String musicButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUSIC);
        sT = new SpriteType(MUSIC_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + musicButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String musicMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUSIC_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + musicMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        String musicSelectedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUSIC_CLICKED);
        img = loadImageWithColorKey(imgPath + musicSelectedButton, COLOR_KEY);
        sT.addState(pathXStates.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, MUSIC_BUTTON_X, MUSIC_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(MUSIC_BUTTON_TYPE, s);
        
        // THEN THE GAME SPEED BUTTON
        String gameSpeedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_GAME_SPEED);
        sT = new SpriteType(GAME_SPEED_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + gameSpeedButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String gameSpeedMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_GAME_SPEED_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + gameSpeedMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, GAME_SPEED_BUTTON_X, GAME_SPEED_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(GAME_SPEED_BUTTON_TYPE, s);
        
        // THEN THE SETTINGS HOME BUTTON
        String settingsHomeButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(SETTINGS_HOME_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + settingsHomeButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String settingsHomeMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + settingsHomeMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_BUTTON_X, HOME_BUTTON_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(SETTINGS_HOME_BUTTON_TYPE, s);
        
        ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        ArrayList<String> positionx = props.getPropertyOptionsList(pathXPropertyType.X_LOCATION);
        ArrayList<String> positiony = props.getPropertyOptionsList(pathXPropertyType.Y_LOCATION);
        for (int i = 0; i < levels.size(); i++){
            sT = new SpriteType(LEVEL_BUTTON_TYPE);
            String levelName = levels.get(i);
            x = record.getLevelPositionX(levelName) - viewport.getViewportX();
            y = record.getLevelPositionY(levelName) - viewport.getViewportY();
            s = new Sprite(sT, x, y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
            guiButtons.put(levels.get(i), s);
        }
        
        // NOW ADD THE DIALOGS
        
        // AND THE STATS DISPLAY
        String statsDialog = props.getProperty(pathXPropertyType.IMAGE_DIALOG_LEVEL);
        sT = new SpriteType(LEVEL_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
        y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
        s = new Sprite(sT, x, y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiDialogs.put(LEVEL_DIALOG_TYPE, s);
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
        record = new Record();
        
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
