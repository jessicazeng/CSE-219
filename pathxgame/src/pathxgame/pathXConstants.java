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
    // Specials buttons
    public static final String GREENLIGHT_BUTTON_TYPE = "GREENLIGHT_BUTTON_TYPE";
    public static final String REDLIGHT_BUTTON_TYPE = "REDLIGHT_BUTTON_TYPE";
    public static final String FREEZE_BUTTON_TYPE = "FREEZE_BUTTON_TYPE";
    public static final String DECSPEED_BUTTON_TYPE = "DECSPEED_BUTTON_TYPE";
    public static final String INCSPEED_BUTTON_TYPE = "INCSPEED_BUTTON_TYPE";
    public static final String INCPLAYERSPEED_BUTTON_TYPE = "INCPLAYERSPEED_BUTTON_TYPE";
    public static final String FLATTIRE_BUTTON_TYPE = "FLATTIRE_BUTTON_TYPE";
    public static final String EMPTYGAS_BUTTON_TYPE = "EMPTYGAS_BUTTON_TYPE";
    public static final String CLOSEROAD_BUTTON_TYPE = "CLOSEROAD_BUTTON_TYPE";
    public static final String CLOSEINTERSECTION_BUTTON_TYPE = "CLOSEINTERSECTION_BUTTON_TYPE";
    public static final String OPENINTERSECTION_BUTTON_TYPE = "OPENINTERSECTION_BUTTON_TYPE";
    public static final String STEAL_BUTTON_TYPE = "STEAL_BUTTON_TYPE";
    public static final String MINDCONTROL_BUTTON_TYPE = "MINDCONTROL_BUTTON_TYPE";
    public static final String INTANGBILITY_BUTTON_TYPE = "INTANGIBILITY_BUTTON_TYPE";
    public static final String MINDLESSTERROR_BUTTON_TYPE = "MINDLESSTERROR_BUTTON_TYPE";
    public static final String INVINCIBILITY_BUTTON_TYPE = "INVINCIBILITY_BUTTON_TYPE";
    
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
    public static final int DIALOG_TEXT_X = 230;
    public static final int DIALOG_TEXT_Y = 160;
    public static final int TOTAL_MONEY_X = 200;
    public static final int TOTAL_MONEY_Y = 45;
    public static final int CITY_X = 93;
    public static final int CITY_Y = 20;
    
    // SPECIALS POSITIONS
    public static final int GREENLIGHT_X = 9;
    public static final int GREENLIGHT_Y = 100;
    public static final int REDLIGHT_X = 45;
    public static final int REDLIGHT_Y = 100;
    public static final int FREEZE_X = 9;
    public static final int FREEZE_Y = 136;
    public static final int DECSPEED_X = 45;
    public static final int DECSPEED_Y = 136;
    public static final int INCSPEED_X = 9;
    public static final int INCSPEED_Y = 172;
    public static final int INCPLAYERSPEED_X = 45;
    public static final int INCPLAYERSPEED_Y = 172;
    public static final int FLATTIRE_X = 9;
    public static final int FLATTIRE_Y = 205;
    public static final int EMPTYGAS_X = 45;
    public static final int EMPTYGAS_Y = 205;
    public static final int CLOSEROAD_X = 9;
    public static final int CLOSEROAD_Y = 241;
    public static final int CLOSEINTERSECTION_X = 45;
    public static final int CLOSEINTERSECTION_Y = 241;
    public static final int OPENINTERSECTION_X = 9;
    public static final int OPENINTERSECTION_Y = 277;
    public static final int STEAL_X = 45;
    public static final int STEAL_Y = 277;
    public static final int MINDCONTROL_X = 9;
    public static final int MINDCONTROL_Y = 313;
    public static final int INTANGIBILITY_X = 45;
    public static final int INTANGIBILITY_Y = 313;
    public static final int MINDLESSTERROR_X = 9;
    public static final int MINDLESSTERROR_Y = 349;
    public static final int INVINCIBILITY_X = 45;
    public static final int INVINCIBILITY_Y = 349;
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SCREEN_STATE = "LEVEL_SCREEN_STATE"; 
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";  
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";  
    
    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 28);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_GAME_STATS = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    
    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_GREEN = new Color(216, 253, 193);
    public static final Color COLOR_RED = new Color(243, 92, 97);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
}
