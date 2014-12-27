/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Jessica
 */
public class pathXConstants {
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    // Menu Buttons
    public static final String PLAY_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String SETTINGS_BUTTON_TYPE = "SETTINGS_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE = "HELP_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE = "RESET_BUTTON_TYPE";
    // Level Selection Buttons
    public static final String HOME_BUTTON_TYPE = "HOME_BUTTON_TYPE";
    // Game Screen Buttons
    public static final String BACK_BUTTON_TYPE = "BACK_BUTTON_TYPE";
    // Navigation buttons
    public static final String SCROLL_LEFT_BUTTON_TYPE = "SCROLL_LEFT_BUTTON_TYPE";
    public static final String SCROLL_DOWN_BUTTON_TYPE = "SCROLL_DOWN_BUTTON_TYPE";
    public static final String SCROLL_UP_BUTTON_TYPE = "SCROLL_UP_BUTTON_TYPE";
    public static final String SCROLL_RIGHT_BUTTON_TYPE = "SCROLL_RIGHT_BUTTON_TYPE";
    public static final String LEVEL_DIALOG_TYPE = "LEVEL_DIALOG_BUTTON_TYPE";
    public static final String TRY_AGAIN_BUTTON_TYPE = "TRY_AGAIN_BUTTON_TYPE";
    public static final String LEAVE_TOWN_BUTTON_TYPE = "LEAVE_TOWN_BUTTON_TYPE";
    
    
    // variables
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final int LEVELSELECTION_GAMEWORLD_WIDTH = 1830;
    public static final int LEVELSELECTION_GAMEWORLD_HEIGHT = 1150;
    public static final int MAP_WIDTH = 635;
    public static final int MAP_HEIGHT = 500;
    public static final int PLAY_BUTTON_WIDTH = 108;
    public static final int PLAY_BUTTON_X = 266;
    public static final int PLAY_BUTTON_Y = 270;
    public static final int MENU_BUTTON_WIDTH = 92;
    public static final int MENU_BUTTON_Y = 340;
    public static final int MENU_BUTTON_X = 200;
    public static final int MENU_BUTTON_MARGIN = 15;
    public static final int LEVELSELECTION_PANE_WIDTH = 634;
    public static final int LEVELSELECTION_PANE_HEIGHT = 75;
    public static final int HOME_BUTTON_X = 10;
    public static final int HOME_BUTTON_Y = 12;
    public static final int MAP_SCROLL_INC = 20;
    public static final int BACK_BUTTON_X = 14;
    public static final int BACK_BUTTON_Y = 17;
    public static final int GAMESCREEN_PANE_WIDTH = 87;
    public static final int GAMESCREEN_PANE_HEIGHT = 480;
    public static final int GAME_GAMEWORLD_WIDTH = 992;
    public static final int GAME_GAMEWORLD_HEIGHT = 550;
    public static final int INTERSECTION_WIDTH = 24;
    public static final int SPRITE_MARGIN = 25;
    public static final int LEVEL_DIALOG_X = 200;
    public static final int LEVEL_DIALOG_Y = 100;
    public static final int TRYAGAIN_X = 280;
    public static final int TRYAGAIN_Y = 210;
    public static final int LEAVETOWN_X = 275;
    public static final int LEAVETOWN_Y = 240;
    public static final int DIALOG_TEXT_X = 270;
    public static final int DIALOG_TEXT_Y = 160;
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SCREEN_STATE = "LEVEL_SCREEN_STATE"; 
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";  
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";  
    
    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    
    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_GREEN = new Color(216, 253, 193);
    public static final Color COLOR_RED = new Color(243, 92, 97);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
}
