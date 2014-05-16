package pathx;

import java.awt.Color;
import java.awt.Font;

/**
 * This class stores all the constants used in pathX.
 * 
 * @author Jessica Zeng
 */
public class pathXConstants {
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    public static final String PLAY_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE = "RESET_BUTTON_TYPE";
    public static final String SETTINGS_BUTTON_TYPE = "SETTINGS_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE = "HELP_BUTTON_TYPE";
    public static final String CLOSE_BUTTON_TYPE = "CLOSE_BUTTON_TYPE";
    public static final String SCROLL_DOWN_BUTTON_TYPE = "SCROLL_DOWN_BUTTON_TYPE";
    public static final String SCROLL_UP_BUTTON_TYPE = "SCROLL_UP_BUTTON_TYPE";
    public static final String SCROLL_LEFT_BUTTON_TYPE = "SCROLL_LEFT_BUTTON_TYPE";
    public static final String SCROLL_RIGHT_BUTTON_TYPE = "SCROLL_RIGHT_BUTTON_TYPE";
    public static final String LEVEL_SCROLL_DOWN_BUTTON_TYPE = "LEVEL_SCROLL_DOWN_BUTTON_TYPE";
    public static final String LEVEL_SCROLL_UP_BUTTON_TYPE = "LEVEL_SCROLL_UP_BUTTON_TYPE";
    public static final String LEVEL_SCROLL_LEFT_BUTTON_TYPE = "LEVEL_SCROLL_LEFT_BUTTON_TYPE";
    public static final String LEVEL_SCROLL_RIGHT_BUTTON_TYPE = "LEVEL_SCROLL_RIGHT_BUTTON_TYPE";
    public static final String SOUND_BUTTON_TYPE = "SOUND_BUTTON_TYPE";
    public static final String MUSIC_BUTTON_TYPE = "MUSIC_BUTTON_TYPE";
    public static final String GAME_SPEED_BUTTON_TYPE = "GAME_SPEED_BUTTON_TYPE";
    public static final String SETTINGS_HOME_BUTTON_TYPE = "SETTINGS_HOME_SPEED_BUTTON_TYPE";
    public static final String LEVEL_SELECTION_HOME_BUTTON_TYPE = "LEVEL_SELECTION_HOME_SPEED_BUTTON_TYPE";
    public static final String LEVEL_BUTTON_TYPE = "LEVEL_BUTTON_TYPE";
    public static final String LEVEL_DIALOG_TYPE = "LEVEL_DIALOG_TYPE";
    public static final String SPECIALS_BUTTON_TYPE = "SPECIALS_BUTTON_TYPE";
    public static final String START_BUTTON_TYPE = "START_BUTTON_TYPE";
    public static final String GAME_CLOSE_BUTTON_TYPE = "GAME_CLOSE_BUTTON_TYPE";
    public static final String GAME_HOME_BUTTON_TYPE = "GAME_HOME_BUTTON_TYPE";
    public static final String PAUSE_BUTTON_TYPE = "PAUSE_BUTTON_TYPE";
    public static final String CLOSE_DIALOG_BUTTON_TYPE = "CLOSE_DIALOG_BUTTON_TYPE";
    public static final String TRY_AGAIN_BUTTON_TYPE = "TRY_AGAIN_BUTTON_TYPE";
    public static final String LEAVE_TOWN_BUTTON_TYPE = "LEAVE_TOWN_BUTTON_TYPE";
    public static final String MAKE_GREEN_LIGHT_BUTTON_TYPE = "MAKE_GREEN_LIGHT_BUTTON_TYPE";
    public static final String MAKE_RED_LIGHT_BUTTON_TYPE = "MAKE_RED_LIGHT_BUTTON_TYPE";
    public static final String DEC_SPEED_BUTTON_TYPE = "DEC_SPEED_BUTTON_TYPE";
    public static final String INC_SPEED_BUTTON_TYPE = "INC_SPEED_BUTTON_TYPE";
    public static final String INC_PLAYER_SPEED_BUTTON_TYPE = "INC_PLAYER_SPEED_BUTTON_TYPE";
    public static final String FLAT_TIRE_BUTTON_TYPE = "FLAT_TIRE_BUTTON_TYPE";
    public static final String EMPTY_GAS_BUTTON_TYPE = "EMPTY_GAS_BUTTON_TYPE";
    public static final String CLOSE_ROAD_BUTTON_TYPE = "CLOSE_ROAD_BUTTON_TYPE";
    public static final String CLOSE_INTERSECTION_BUTTON_TYPE = "CLOSE_INTERSECTION_BUTTON_TYPE";
    public static final String OPEN_INTERSECTION_BUTTON_TYPE = "OPEN_INTERSECTION_BUTTON_TYPE";
    public static final String STEAL_BUTTON_TYPE = "STEAL_BUTTON_TYPE";
    public static final String MIND_CONTROL_BUTTON_TYPE = "MIND_CONTROL_BUTTON_TYPE";
    public static final String INTANGIBILITY_BUTTON_TYPE = "INTANGIBILITY_BUTTON_TYPE";
    public static final String MINDLESS_TERROR_BUTTON_TYPE = "MINDLESS_TERROR_BUTTON_TYPE";
    public static final String FLYING_BUTTON_TYPE = "FLYING_BUTTON_TYPE";
    public static final String INVINCIBILITY_BUTTON_TYPE = "INVINCIBILITY_BUTTON_TYPE";
    
    public static final String BALANCE = "Balance: ";
    public static final String GOAL = "Goal: ";
    public static final String WIN = "Congratulations!";
    public static final String LOSS = "Oh shucks!";
    
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final int GAME_VIEWPORT_WIDTH = 466;
    public static final int GAME_VIEWPORT_HEIGHT = 421;
    public static final int LEVEL_GAMEWORLD_WIDTH = 984;
    public static final int LEVEL_GAMEWORLD_HEIGHT = 551;
    public static final int MENU_BUTTON_WIDTH = 100;
    public static final int MENU_BUTTON_MARGIN = 15;
    public static final int MENU_BUTTON_Y = 385;
    public static final int CLOSE_BUTTON_X = 585;
    public static final int CLOSE_BUTTON_Y = 8;
    public static final int DOWN_BUTTON_X = 55;
    public static final int DOWN_BUTTON_Y = 405;
    public static final int UP_BUTTON_X = 55;
    public static final int UP_BUTTON_Y = 375;
    public static final int LEFT_BUTTON_X = 25;
    public static final int LEFT_BUTTON_Y = 390;
    public static final int RIGHT_BUTTON_X = 90;
    public static final int RIGHT_BUTTON_Y = 390;
    public static final int MAP_WIDTH = 635;
    public static final int MAP_HEIGHT = 500;
    public static final int MAP_X = 0;
    public static final int MAP_Y = 330;
    public static final int MAP_SCROLL_INC = 20;
    public static final int GAME_WORLD_WIDTH = 1494;
    public static final int GAME_WORLD_HEIGHT = 1129;
    public static final int SOUND_BUTTON_X = 220;
    public static final int SOUND_BUTTON_Y = 212;
    public static final int MUSIC_BUTTON_X = 220;
    public static final int MUSIC_BUTTON_Y = 248;
    public static final int GAME_SPEED_BUTTON_X = 260;
    public static final int GAME_SPEED_BUTTON_Y = 335;
    public static final int HOME_BUTTON_X = 535;
    public static final int HOME_BUTTON_Y = 8;
    public static final int LEVEL_DIALOG_X = 120;
    public static final int LEVEL_DIALOG_Y = 30;
    public static final int GAME_STATS_X = 150;
    public static final int GAME_STATS_Y = 100;
    public static final int ONE_WAY_TRIANGLE_HEIGHT = 10;
    public static final int ONE_WAY_TRIANGLE_WIDTH = 30;
    public static final int NORMAL_VELOCITY = 10;
    public static final int SPECIAL_BUTTON_WIDTH = 32;
    public static final int SPECIAL_BUTTON_HEIGHT = 37;
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SCREEN_STATE = "LEVEL_SCREEN_STATE"; 
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";  
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";  
    
    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_KEY2 = new Color(239, 228, 176);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    
    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 28);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_GAME_STATS = new Font(Font.SANS_SERIF, Font.BOLD, 28);
}
