package pathxgame.ui;

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
import pathxgame.Pathx.pathXPropertyType;
import static pathxgame.Pathx.pathXPropertyType.*;
import pathxgame.data.DataModel;
import pathxgame.data.Record;
import pathxgame.file.FileManager;
import static pathxgame.pathXConstants.*;
import properties_manager.PropertiesManager;

/**
 *
 * @author Jessica
 */
public class Game extends MiniGame{
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private Record record;

    // HANDLES GAME UI EVENTS
    private EventHandler eventHandler;
    
    // HANDLES ERROR CONDITIONS
    private ErrorHandler errorHandler;
    
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private FileManager fileManager;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;

    @Override
    public void initAudioContent() {
       
    }

    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new ErrorHandler(window);
        
        // INIT OUR FILE MANAGER
        fileManager = new FileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = new Record();
        
        // INIT OUR DATA MANAGER
        data = new DataModel(this);
    }

    @Override
    public void initGUIControls() {
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
        //viewport.setGameWorldSize(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new Panel(this, (DataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MENU_BACKGROUND));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_SETTINGS_BACKGROUND));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_HELP_BACKGROUND));
        sT.addState(HELP_SCREEN_STATE, img);
        img = loadImageWithColorKey(imgPath + props.getProperty(pathXPropertyType.IMAGE_LEVELSELECTION_BACKGROUND), COLOR_KEY);
        sT.addState(LEVEL_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_GAMESCREEN_BACKGROUND));
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        String playButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PLAY);
        sT = new SpriteType(PLAY_BUTTON_TYPE);
        img = loadImage(imgPath + playButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String playMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PLAY_MOUSE_OVER);
        img = loadImage(imgPath + playMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, States.VISIBLE_STATE.toString());
        guiButtons.put(PLAY_BUTTON_TYPE, s);
        
        // -------------------ADD A BUTTON FOR EACH MENU OPTION-------------------------------
        float totalWidth = 3 * (MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN) - MENU_BUTTON_MARGIN;
        //Viewport viewport = data.getViewport();
        x = (viewport.getScreenWidth() - totalWidth)/2.0f;
        
        // Reset Button
        String resetButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RESET);
        sT = new SpriteType(RESET_BUTTON_TYPE);
        img = loadImage(imgPath + resetButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String resetMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RESET_MOUSE_OVER);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, States.VISIBLE_STATE.toString());
        guiButtons.put(RESET_BUTTON_TYPE, s);
        x += MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN;
        
        // Settings Button
        String settingsButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SETTINGS);
        sT = new SpriteType(SETTINGS_BUTTON_TYPE);
        img = loadImage(imgPath + settingsButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String settingsMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SETTINGS_MOUSE_OVER);
        img = loadImage(imgPath + settingsMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, States.VISIBLE_STATE.toString());
        guiButtons.put(SETTINGS_BUTTON_TYPE, s);
        x += MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN;
        
        // Help Button
        String helpButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HELP);
        sT = new SpriteType(HELP_BUTTON_TYPE);
        img = loadImage(imgPath + helpButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String helpMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HELP_MOUSE_OVER);
        img = loadImage(imgPath + helpMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, States.VISIBLE_STATE.toString());
        guiButtons.put(HELP_BUTTON_TYPE, s);
        
        // -----------------------LEVEL SELECTION SCREEN BUTTONS------------------------------
        String homeButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(HOME_BUTTON_TYPE);
        img = loadImage(imgPath + homeButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String homeMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = loadImage(imgPath + homeMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_BUTTON_X, HOME_BUTTON_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(HOME_BUTTON_TYPE, s);
        
        // ------------------------------GAME SCREEN BUTTONS---------------------------------
        String backButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_BACK);
        sT = new SpriteType(BACK_BUTTON_TYPE);
        img = loadImage(imgPath + backButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String backMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_BACK_MOUSE_OVER);
        img = loadImage(imgPath + backMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, BACK_BUTTON_X, BACK_BUTTON_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(BACK_BUTTON_TYPE, s);
        
        //---------------------------DIALOG BUTTONS-------------------------------------
        String leavetownButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEAVETOWN);
        sT = new SpriteType(LEAVE_TOWN_BUTTON_TYPE);
        img = loadImage(imgPath + leavetownButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String leavetownMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEAVETOWN_MOUSE_OVER);
        img = loadImage(imgPath + leavetownMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEAVETOWN_X, LEAVETOWN_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(LEAVE_TOWN_BUTTON_TYPE, s);
        
        String tryagainButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_TRYAGAIN);
        sT = new SpriteType(TRY_AGAIN_BUTTON_TYPE);
        img = loadImage(imgPath + tryagainButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String tryagainMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_TRYAGAIN_MOUSE_OVER);
        img = loadImage(imgPath + tryagainMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, TRYAGAIN_X, TRYAGAIN_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(TRY_AGAIN_BUTTON_TYPE, s);
        
        // ------------------------SPECIALS BUTTONS------------------------------
        // Green light
        String greenlightButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_GREENLIGHT);
        sT = new SpriteType(GREENLIGHT_BUTTON_TYPE);
        img = loadImage(imgPath + greenlightButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String greenlightMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_GREENLIGHT_MOUSE_OVER);
        img = loadImage(imgPath + greenlightMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, GREENLIGHT_X, GREENLIGHT_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(GREENLIGHT_BUTTON_TYPE, s);
        
        // Red light
        String redlightButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_REDLIGHT);
        sT = new SpriteType(REDLIGHT_BUTTON_TYPE);
        img = loadImage(imgPath + redlightButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String redlightMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_REDLIGHT_MOUSE_OVER);
        img = loadImage(imgPath + redlightMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, REDLIGHT_X, REDLIGHT_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(REDLIGHT_BUTTON_TYPE, s);
        
        // Freeze 
        String freezeButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FREEZE);
        sT = new SpriteType(FREEZE_BUTTON_TYPE);
        img = loadImage(imgPath + freezeButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String freezeMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FREEZE_MOUSE_OVER);
        img = loadImage(imgPath + freezeMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, FREEZE_X, FREEZE_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(FREEZE_BUTTON_TYPE, s);
        
        // Dec Speed 
        String decspeedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_DECSPEED);
        sT = new SpriteType(DECSPEED_BUTTON_TYPE);
        img = loadImage(imgPath + decspeedButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String decspeedMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_DECSPEED_MOUSE_OVER);
        img = loadImage(imgPath + decspeedMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DECSPEED_X, DECSPEED_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(DECSPEED_BUTTON_TYPE, s);
        
        // Inc Speed 
        String incspeedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INCSPEED);
        sT = new SpriteType(INCSPEED_BUTTON_TYPE);
        img = loadImage(imgPath + incspeedButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String incspeedMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INCSPEED_MOUSE_OVER);
        img = loadImage(imgPath + incspeedMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INCSPEED_X, INCSPEED_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(INCSPEED_BUTTON_TYPE, s);
        
        // Inc Player Speed 
        String incplayerspeedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INCPLAYERSPEED);
        sT = new SpriteType(INCPLAYERSPEED_BUTTON_TYPE);
        img = loadImage(imgPath + incplayerspeedButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String incplayerspeedMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INCPLAYERSPEED_MOUSE_OVER);
        img = loadImage(imgPath + incplayerspeedMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INCPLAYERSPEED_X, INCPLAYERSPEED_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(INCPLAYERSPEED_BUTTON_TYPE, s);
        
        // Flat Tire
        String flattireButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FLATTIRE);
        sT = new SpriteType(FLATTIRE_BUTTON_TYPE);
        img = loadImage(imgPath + flattireButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String flattireMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FLATTIRE_MOUSE_OVER);
        img = loadImage(imgPath + flattireMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, FLATTIRE_X, FLATTIRE_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(FLATTIRE_BUTTON_TYPE, s);
        
        // Empty Gas
        String emptygasButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_EMPTYGAS);
        sT = new SpriteType(EMPTYGAS_BUTTON_TYPE);
        img = loadImage(imgPath + emptygasButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String emptygasMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_EMPTYGAS_MOUSE_OVER);
        img = loadImage(imgPath + emptygasMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, EMPTYGAS_X, EMPTYGAS_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(EMPTYGAS_BUTTON_TYPE, s);
        
        // Close Road
        String closeroadButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSEROAD);
        sT = new SpriteType(CLOSEROAD_BUTTON_TYPE);
        img = loadImage(imgPath + closeroadButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String closeroadMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSEROAD_MOUSE_OVER);
        img = loadImage(imgPath + closeroadMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSEROAD_X, CLOSEROAD_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSEROAD_BUTTON_TYPE, s);
        
        // Close Intersection
        String closeintersectionButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSEINTERSECTION);
        sT = new SpriteType(CLOSEINTERSECTION_BUTTON_TYPE);
        img = loadImage(imgPath + closeintersectionButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String closeintersectionMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSEINTERSECTION_MOUSE_OVER);
        img = loadImage(imgPath + closeintersectionMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSEINTERSECTION_X, CLOSEINTERSECTION_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSEINTERSECTION_BUTTON_TYPE, s);
        
        // Open Intersection
        String openintersectionButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_OPENINTERSECTION);
        sT = new SpriteType(OPENINTERSECTION_BUTTON_TYPE);
        img = loadImage(imgPath + openintersectionButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String openintersectionMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_OPENINTERSECTION_MOUSE_OVER);
        img = loadImage(imgPath + openintersectionMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, OPENINTERSECTION_X, OPENINTERSECTION_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(OPENINTERSECTION_BUTTON_TYPE, s);
        
        // Steal
        String stealButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_STEAL);
        sT = new SpriteType(STEAL_BUTTON_TYPE);
        img = loadImage(imgPath + stealButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String stealMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_STEAL_MOUSE_OVER);
        img = loadImage(imgPath + stealMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, STEAL_X, STEAL_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(STEAL_BUTTON_TYPE, s);
        
        // Mind Control
        String mindcontrolButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MINDCONTROL);
        sT = new SpriteType(MINDCONTROL_BUTTON_TYPE);
        img = loadImage(imgPath + mindcontrolButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String mindcontrolMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MINDCONTROL_MOUSE_OVER);
        img = loadImage(imgPath + mindcontrolMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MINDCONTROL_X, MINDCONTROL_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(MINDCONTROL_BUTTON_TYPE, s);
        
        // intangibility
        String intangibilitylButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INTANGIBILITY);
        sT = new SpriteType(INTANGBILITY_BUTTON_TYPE);
        img = loadImage(imgPath + intangibilitylButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String intangibilityMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INTANGIBILITY_MOUSE_OVER);
        img = loadImage(imgPath + intangibilityMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INTANGIBILITY_X, INTANGIBILITY_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(INTANGBILITY_BUTTON_TYPE, s);
        
        // mindlessterror
        String mindlessterrorButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MINDLESSTERROR);
        sT = new SpriteType(MINDLESSTERROR_BUTTON_TYPE);
        img = loadImage(imgPath + mindlessterrorButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String mindlesssterrorMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MINDLESSTERROR_MOUSE_OVER);
        img = loadImage(imgPath + mindlesssterrorMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MINDLESSTERROR_X, MINDLESSTERROR_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(MINDLESSTERROR_BUTTON_TYPE, s);
        
        // invincibility
        String invincibilityButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INVINCIBILITY);
        sT = new SpriteType(INVINCIBILITY_BUTTON_TYPE);
        img = loadImage(imgPath + invincibilityButton);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        String invincibilityMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INVINCIBILITY_MOUSE_OVER);
        img = loadImage(imgPath + invincibilityMouseOverButton);
        sT.addState(States.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INVINCIBILITY_X, INVINCIBILITY_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiButtons.put(INVINCIBILITY_BUTTON_TYPE, s);
        
        // ------------------------------DIALOGS-----------------------------------
        String statsDialog = props.getProperty(pathXPropertyType.DIALOG_LEVEL);
        sT = new SpriteType(LEVEL_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
        sT.addState(States.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_DIALOG_X, LEVEL_DIALOG_Y, 0, 0, States.INVISIBLE_STATE.toString());
        guiDialogs.put(LEVEL_DIALOG_TYPE, s);
    }

    @Override
    public void initGUIHandlers() {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new EventHandler(this);
        
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) 
            { eventHandler.respondToExitRequest(); }
        });
        
        // PLAY EVENT HANDLER
        guiButtons.get(PLAY_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToLevelSelectionScreenRequest();     }
        });
        
        // SETTINGS EVENT HANDLER
        guiButtons.get(SETTINGS_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.switchToSettingsScreenRequest();     }
        });
        
        // HELP EVENT HANDLER
        guiButtons.get(HELP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.switchToHelpScreenRequest();     }
        });
        
        // HOME EVENT HANDLER
        guiButtons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.switchToSplashScreenRequest();     }
        });
        
        // BACK EVENT HANDLER
        guiButtons.get(BACK_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToLevelSelectionScreenRequest();     }
        });
        
        //---------------------DIALOG HANDLERS--------------------------
        
        // TRY AGAIN HANDLER
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToTryAgainRequest();     }
        });
        
        // TRY AGAIN HANDLER
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToLevelSelectionScreenRequest();     }
        });
        
        //------------------SPECIALS HANDLERS--------------------------------
        
        // GREEN LIGHT HANDLER
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSpecialSelcted(GREENLIGHT_BUTTON_TYPE);     }
        });
        
        // RED LIGHT HANDLER
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSpecialSelcted(REDLIGHT_BUTTON_TYPE);     }
        });
        
        // ---------------------------KEY LISTENER-----------------------------
        this.setKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke)
            {   
                eventHandler.respondToKeyPress(ke.getKeyCode());    
            }
        });
    }

    @Override
    public void reset() {
        data.reset(this);
    }

    /**
     * Updates the state of all gui controls according to the 
     * current game conditions.
     */
    @Override
    public void updateGUI() {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(States.VISIBLE_STATE.toString()))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(States.MOUSE_OVER_STATE.toString());
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(States.MOUSE_OVER_STATE.toString()))
            {
                 if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(States.VISIBLE_STATE.toString());
                }
            }
        }
    }
    
    // ----------------------Accessor Methods-----------------------------------
    public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }
    
    public Record getPlayerRecord(){
        return record;
    }
    
    /**
     * Accessor method for getting the application's error handler.
     * 
     * @return The error handler.
     */
    public ErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    /**
     * Accessor method for getting the app's file manager.
     * 
     * @return The file manager.
     */
    public FileManager getFileManager()
    {
        return fileManager;
    }
    
    /**
     * Accessor method for getting the app's event handler.
     * 
     * @return The file manager.
     */
    public EventHandler getEventHandler()
    {
        return eventHandler;
    }
    
    //------------------------SCREEN SWITCHING----------------------------------
    
    /**
     * This method switches the application to the menu screen, making
     * all the appropriate UI controls visible & invisible.
     */    
    public void switchToSplashScreen()
    {
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);

        // ACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(true);
        
        // DEACTIVATE LEVEL SELECTION & GAME SCREEN BUTTONS
        guiButtons.get(HOME_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FREEZE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FREEZE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DECSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(DECSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
    }
    
    public void switchToSettingsScreen()
    {
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = SETTINGS_SCREEN_STATE;
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);

        // ACTIVATE THE SETTINGS CONTROLS
        guiButtons.get(HOME_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        
        // DEACTIVATE LEVEL SELECTION & GAME SCREEN BUTTONS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FREEZE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FREEZE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DECSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(DECSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
    }
    
    public void switchToHelpScreen()
    {
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = HELP_SCREEN_STATE;
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);

        // ACTIVATE THE HELP CONTROLS
        guiButtons.get(HOME_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        
        // DEACTIVATE LEVEL SELECTION & GAME SCREEN BUTTONS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FREEZE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FREEZE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DECSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(DECSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
    }

    public void switchToLevelSelectionScreen() {
        currentScreenState = LEVEL_SCREEN_STATE;
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCREEN_STATE);
        
        Viewport viewport = data.getViewport();
        viewport.setGameWorldSize(LEVELSELECTION_GAMEWORLD_WIDTH, LEVELSELECTION_GAMEWORLD_HEIGHT);
        viewport.scroll(0-viewport.getViewportX(), 0-viewport.getViewportY());
        
        // DEACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        
        // specials buttons
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FREEZE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FREEZE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DECSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(DECSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
        
        //ACTIVATE LEVEL SELECTION SCREEN BUTTONS
        guiButtons.get(HOME_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        
        // DIALOG BUTTONS & DIALOG
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        
        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
    }
    
    public void switchToGameScreen(){
        currentScreenState = GAME_SCREEN_STATE;
        
        Viewport viewport = data.getViewport();
        viewport.setGameWorldSize(GAME_GAMEWORLD_WIDTH, GAME_GAMEWORLD_HEIGHT);
        viewport.scroll(0-viewport.getViewportX(), 0-viewport.getViewportY());
        
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        
        // DEACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOME_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(false);
        
        //ACTIVATE LEVEL SELECTION SCREEN BUTTONS
        guiButtons.get(BACK_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        
        // SPECIALS BUTTONS
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FREEZE_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(FREEZE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INCSPEED_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(INCSPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DECSPEED_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(DECSPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(FLATTIRE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(EMPTYGAS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(CLOSEROAD_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(CLOSEINTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(OPENINTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(INTANGBILITY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(MINDLESSTERROR_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(true);
        
        // DIALOG BUTTONS & DIALOG
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(States.INVISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        
        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.IN_PROGRESS);
        ((DataModel)data).initPlayer();
    }
    
    /**
     * 
     * @param buttontype 
     */
    public void scroll(String buttontype){
        Viewport viewport = data.getViewport();
        
        if(buttontype == SCROLL_RIGHT_BUTTON_TYPE){ // right button
            int maxWidth = viewport.getGameWorldWidth() - viewport.getViewportWidth();
            if(viewport.getViewportX() < maxWidth)
                viewport.scroll(MAP_SCROLL_INC, 0);
        } else if(buttontype == SCROLL_LEFT_BUTTON_TYPE){ // left button
            if(viewport.getViewportX() > 0)
                viewport.scroll(0-MAP_SCROLL_INC, 0);
        } else if(buttontype == SCROLL_UP_BUTTON_TYPE){ // up button
            if(viewport.getViewportY() > 0)
                viewport.scroll(0, 0-MAP_SCROLL_INC);
        } else if(buttontype == SCROLL_DOWN_BUTTON_TYPE){ // down button
            int maxHeight = viewport.getGameWorldHeight() - viewport.getViewportHeight();
            if(viewport.getViewportY() < maxHeight)
                viewport.scroll(0, MAP_SCROLL_INC);
        }
    }
    
    public void openDialog(){
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(States.VISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(true);
    }
}
