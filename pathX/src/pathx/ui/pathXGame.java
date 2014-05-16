package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.file.pathXFileManager;
import pathx.data.Record;
import pathx.PathX.pathXPropertyType;
import pathx.data.Player;
import pathx.pathXConstants;
import properties_manager.PropertiesManager;
import static pathx.pathXConstants.*;
import pathx.data.pathXDataModel;

/**
 * This class manages all the UI for the pathX game.
 * 
 * @author Jessica Zeng
 */
public class pathXGame  extends MiniGame{
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private Record record;

    // HANDLES GAME UI EVENTS
    private pathXEventHandler eventHandler;
    
    private SpecialsHandler specialsHandler;
    
    // HANDLES ERROR CONDITIONS
    private pathXErrorHandler errorHandler;
    
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private pathXFileManager fileManager;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    
    private boolean soundDisabled;
    private boolean musicDisabled;
    
    // Accessor Methods
    public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }
    
    public Record getPlayerRecord(){
        return record;
    }
    
    /**
     * Accessor method for getting the app's file manager.
     * 
     * @return The file manager.
     */
    public pathXFileManager getFileManager()
    {
        return fileManager;
    }
    
    /**
     * This method forces the file manager to save the current player record.
     */
    public void savePlayerRecord(){
        
    }
    
    public void pressedLevelButton(String levelName){
        eventHandler.respondToSelectLevelRequest(levelName);
    }
    
    public boolean isSoundDisabled(){
        return soundDisabled;
    }
    
    public boolean isMusicDisabled(){
        return musicDisabled;
    }
    
    public void disableSpecials(){
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(false);
    }
    
    public void enableSpecials(){
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(true);
    }
    
    public void closeDialog(){
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        
        data.beginGame();
    }
    
    public void openDialog(){
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(true);
    }
    
    public void scroll(String buttontype){
        Viewport viewport = data.getViewport();
        Player player = ((pathXDataModel)data).getplayer();
        
        if(buttontype == SCROLL_RIGHT_BUTTON_TYPE){
            int maxWidth = viewport.getGameWorldWidth() - viewport.getViewportWidth();
            if(viewport.getViewportX() < maxWidth){
                viewport.scroll(MAP_SCROLL_INC, 0);
            }
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
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = MENU_SCREEN_STATE;
        
         // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        
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
        guiButtons.get(START_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLYING_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(false);
        
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        
        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
        
        if(musicDisabled == false){
            audio.stop(pathXPropertyType.AUDIO_GAME.toString()); 
            audio.play(pathXPropertyType.AUDIO_MENU.toString(), true); 
        }
    }
    
    public void switchToLevelScreen(){
        currentScreenState = GAME_SCREEN_STATE;
        
        Viewport viewport = data.getViewport();
        viewport.setViewportSize(GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT);
        viewport.setGameWorldSize(LEVEL_GAMEWORLD_WIDTH, LEVEL_GAMEWORLD_WIDTH);
        viewport.scroll(0-viewport.getViewportX(), 0-viewport.getViewportY());
        
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        
        if(data.isPaused())
            data.unpause();
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        guiButtons.get(START_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FLYING_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(true);
        
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
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        
        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
        ((pathXDataModel)data).initPlayer();
        ((pathXDataModel)data).setAdjacentIntersections();
        
        if(musicDisabled == false){
            audio.play(pathXPropertyType.AUDIO_GAME.toString(), true); 
            audio.stop(pathXPropertyType.AUDIO_MENU.toString());
        }
    }
     
    public void switchToSettingsScreen(){
        currentScreenState = SETTINGS_SCREEN_STATE;
        
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
        
        if(musicDisabled == true)
            guiButtons.get(MUSIC_BUTTON_TYPE).setState(pathXStates.SELECTED_STATE.toString());
        else
            guiButtons.get(MUSIC_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        
        if(soundDisabled == true)
            guiButtons.get(SOUND_BUTTON_TYPE).setState(pathXStates.SELECTED_STATE.toString());
        else
            guiButtons.get(SOUND_BUTTON_TYPE).setState(pathXStates.VISIBLE_STATE.toString());
        
        guiButtons.get(MUSIC_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SOUND_BUTTON_TYPE).setEnabled(true);
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
        guiButtons.get(START_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLYING_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(false);
    }
     
    public void switchToHelpScreen(){
        currentScreenState = HELP_SCREEN_STATE;
        
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
        guiButtons.get(START_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLYING_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(false);
    }
     
    public void switchToLevelSelectionScreen(){
        currentScreenState = LEVEL_SCREEN_STATE;
        
        BufferedImage img;
        SpriteType sT;
        Sprite s;
        
        Viewport viewport = data.getViewport();
        viewport.setViewportSize(MAP_WIDTH, MAP_HEIGHT);
        viewport.setGameWorldSize(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        viewport.scroll(0-viewport.getViewportX(), 0-viewport.getViewportY());
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCREEN_STATE);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
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
        guiButtons.get(START_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLYING_BUTTON_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(false);
        
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
        
        guiDialogs.get(LEVEL_DIALOG_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        
        data.setGameState(MiniGameState.NOT_STARTED);
        
        if(musicDisabled == false){
            audio.stop(pathXPropertyType.AUDIO_GAME.toString()); 
            audio.play(pathXPropertyType.AUDIO_MENU.toString(), true);
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
        
        // SCROLL DOWN EVENT HANDLER
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_DOWN_BUTTON_TYPE);     }
        });
        
        // SCROLL LEFT EVENT HANDLER
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_UP_BUTTON_TYPE);     }
        });
        
        //--------------------
        // SCROLL RIGHT EVENT HANDLER
        guiButtons.get(LEVEL_SCROLL_RIGHT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_RIGHT_BUTTON_TYPE);     }
        });
        
        // SCROLL LEFT EVENT HANDLER
        guiButtons.get(LEVEL_SCROLL_LEFT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_LEFT_BUTTON_TYPE);     }
        });
        
        // SCROLL LEFT EVENT HANDLER
        guiButtons.get(LEVEL_SCROLL_DOWN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(SCROLL_DOWN_BUTTON_TYPE);     }
        });
        
        // SCROLL LEFT EVENT HANDLER
        guiButtons.get(LEVEL_SCROLL_UP_BUTTON_TYPE).setActionListener(new ActionListener(){
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
            {   eventHandler.respondToMusicPressRequest(MUSIC_BUTTON_TYPE);     }
        });
        
        // HOME BUTTON IN SETTINGS SCREEN EVENT HANDLER
        guiButtons.get(SETTINGS_HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSwitchToHomeScreenRequest();     }
        });
        guiButtons.get(GAME_HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSwitchToHomeScreenRequest();     }
        });
        
        // CLOSE BUTTON SCREEN EVENT HANDLER
        guiButtons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToExitRequest();     }
        });
        
        guiButtons.get(GAME_CLOSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToExitRequest();     }
        });
        
        // CLOSE BUTTON SCREEN EVENT HANDLER
        guiButtons.get(HELP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSwitchToHelpScreenRequest();     }
        });
        
        //
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToCloseDialogRequest();     }
        });
        
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToPlayRequest();     }
        });
        
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                eventHandler.respondToTryAgainRequest();     
            }
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
        
        
        specialsHandler = new SpecialsHandler(this);
        
        guiButtons.get(MAKE_RED_LIGHT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                specialsHandler.makeLightRed();     
            }
        });
        
        guiButtons.get(MAKE_GREEN_LIGHT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                specialsHandler.makeLightGreen();     
            }
        });
        
        guiButtons.get(DEC_SPEED_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                specialsHandler.decSpeed();     
            }
        });
        
        guiButtons.get(INC_SPEED_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                specialsHandler.incSpeed();     
            }
        });
        
        guiButtons.get(INC_PLAYER_SPEED_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                specialsHandler.incPlayerSpeed();     
            }
        });
        
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                specialsHandler.closeRoad();     
            }
        });
        
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                specialsHandler.openRoad();     
            }
        });
        
        guiButtons.get(PAUSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.freeze();     }
        });
        
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.flatTire();     }
        });
        
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.intangible();     }
        });
        
        guiButtons.get(STEAL_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.steal();     }
        });
        
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.emptyGas();     }
        });
        
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.invincible();     }
        });
        
        guiButtons.get(FLYING_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.flying();     }
        });
        
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   specialsHandler.mindControl();     }
        });
    }
    
    public void setClickedMusicButton(String buttontype){
        if(guiButtons.get(buttontype).getState() == pathXStates.SELECTED_STATE.toString()){
            musicDisabled = false;
            guiButtons.get(buttontype).setState(pathXStates.VISIBLE_STATE.toString());
            audio.play(pathXPropertyType.AUDIO_MENU.toString(), true);
        } else{
            musicDisabled = true;
            guiButtons.get(buttontype).setState(pathXStates.SELECTED_STATE.toString()); 
            audio.stop(pathXPropertyType.AUDIO_MENU.toString()); 
        }
    }
    
    public void setClickedSoundButton(String buttontype){
        if(guiButtons.get(buttontype).getState() == pathXStates.SELECTED_STATE.toString()){
            soundDisabled = false;
            guiButtons.get(buttontype).setState(pathXStates.VISIBLE_STATE.toString());
        } else{
            soundDisabled = true;
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
        img = loadImageWithColorKey(imgPath + props.getProperty(pathXPropertyType.IMAGE_LEVEL_SELECTION_BACKGROUND), COLOR_KEY);
        sT.addState(LEVEL_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_SETTINGS_BACKGROUND));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_HELP_BACKGROUND));
        sT.addState(HELP_SCREEN_STATE, img);
        //img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_LEVEL_BACKGROUND));
        img = loadImageWithColorKey(imgPath + props.getProperty(pathXPropertyType.IMAGE_LEVEL_BACKGROUND), COLOR_KEY);
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
        
        String levelDownButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN);
        sT = new SpriteType(LEVEL_SCROLL_DOWN_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + levelDownButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String levelDownMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + levelDownMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_BUTTON_X-1, DOWN_BUTTON_Y+15, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_SCROLL_DOWN_BUTTON_TYPE, s);
        
        // THEN THE UP BUTTON
        String levelUpButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_UP);
        sT = new SpriteType(LEVEL_SCROLL_UP_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + levelUpButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String levelUpMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_UP_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + levelUpMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_BUTTON_X-1, UP_BUTTON_Y-25, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_SCROLL_UP_BUTTON_TYPE, s);
        
        // THEN THE LEFT BUTTON
        String levelLeftButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT);
        sT = new SpriteType(LEVEL_SCROLL_LEFT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + levelLeftButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String levelLeftMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + levelLeftMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEFT_BUTTON_X-3, LEFT_BUTTON_Y-10, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_SCROLL_LEFT_BUTTON_TYPE, s);
        
        // THEN THE RIGHT BUTTON
        String levelRightButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT);
        sT = new SpriteType(LEVEL_SCROLL_RIGHT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + levelRightButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String levelRightMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + levelRightMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RIGHT_BUTTON_X, RIGHT_BUTTON_Y-10, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_SCROLL_RIGHT_BUTTON_TYPE, s);
        
        String pauseButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PAUSE);
        sT = new SpriteType(PAUSE_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + pauseButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String pauseMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PAUSE_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + pauseMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_BUTTON_X-3, RIGHT_BUTTON_Y-10, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(PAUSE_BUTTON_TYPE, s);
        
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
        
        // THEN THE START BUTTON
        String startButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_START);
        sT = new SpriteType(START_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + startButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String startMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_START);
        img = loadImageWithColorKey(imgPath + startMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10, 140, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(START_BUTTON_TYPE, s);
        
        String closeButton2 = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(GAME_CLOSE_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + closeButton2, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String closeMouseOverButton2 = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + closeMouseOverButton2, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 85, 95, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(GAME_CLOSE_BUTTON_TYPE, s);
        
        String levelHomeButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(GAME_HOME_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + levelHomeButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String levelHomeMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + levelHomeMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 30, 95, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(GAME_HOME_BUTTON_TYPE, s);
        
        String closeDialogHomeButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_DIALOG);
        sT = new SpriteType(CLOSE_DIALOG_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + closeDialogHomeButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String closeDialogMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_DIALOG_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + closeDialogMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 270, 350, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_DIALOG_BUTTON_TYPE, s);
        
        String tryAgainButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_TRY_AGAIN);
        sT = new SpriteType(TRY_AGAIN_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + tryAgainButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String tryAgainMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_TRY_AGAIN_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + tryAgainMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 150, 350, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(TRY_AGAIN_BUTTON_TYPE, s);
        
        String leaveTownButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEAVE_TOWN);
        sT = new SpriteType(LEAVE_TOWN_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + leaveTownButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String leaveTownMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEAVE_TOWN_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + leaveTownMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 330, 353, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(LEAVE_TOWN_BUTTON_TYPE, s);
        
        String greenlightButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_GREEN_LIGHT);
        sT = new SpriteType(MAKE_GREEN_LIGHT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + greenlightButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String greenLightMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_GREEN_LIGHT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + greenLightMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10, 200, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(MAKE_GREEN_LIGHT_BUTTON_TYPE, s);
        
        String redlightButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RED_LIGHT);
        sT = new SpriteType(MAKE_RED_LIGHT_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + redlightButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String redLightMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_RED_LIGHT_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + redLightMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10 + SPECIAL_BUTTON_WIDTH, 200, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(MAKE_RED_LIGHT_BUTTON_TYPE, s);
        
        //invincibility
        String invincibilityButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FREEZE);
        sT = new SpriteType(INVINCIBILITY_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + invincibilityButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String invincibilityMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FREEZE_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + invincibilityMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10 + (2*SPECIAL_BUTTON_WIDTH), 200, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(INVINCIBILITY_BUTTON_TYPE, s);
        
        String decSpeedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_DEC_SPEED);
        sT = new SpriteType(DEC_SPEED_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + decSpeedButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String decSpeedMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_DEC_SPEED_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + decSpeedMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10 + (3*SPECIAL_BUTTON_WIDTH), 200, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(DEC_SPEED_BUTTON_TYPE, s);
        
        String incSpeedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INC_SPEED);
        sT = new SpriteType(INC_SPEED_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + incSpeedButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String incSpeedMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INC_SPEED_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + incSpeedMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10, 200+SPECIAL_BUTTON_HEIGHT, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(INC_SPEED_BUTTON_TYPE, s);
        
        String incPlayerSpeedButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PLAYER_SPEED);
        sT = new SpriteType(INC_PLAYER_SPEED_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + incPlayerSpeedButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String incPlayerSpeedMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_PLAYER_SPEED_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + incPlayerSpeedMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(1*SPECIAL_BUTTON_WIDTH), 200+SPECIAL_BUTTON_HEIGHT, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(INC_PLAYER_SPEED_BUTTON_TYPE, s);
        
        String flatTireButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FLAT_TIRE);
        sT = new SpriteType(FLAT_TIRE_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + flatTireButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String flatTireMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FLAT_TIRE_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + flatTireMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(2*SPECIAL_BUTTON_WIDTH), 200+SPECIAL_BUTTON_HEIGHT, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(FLAT_TIRE_BUTTON_TYPE, s);
        
        String emptyGasButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_EMPTY_GAS);
        sT = new SpriteType(EMPTY_GAS_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + emptyGasButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String emptyGasMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_EMPTY_GAS_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + emptyGasMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(3*SPECIAL_BUTTON_WIDTH), 200+SPECIAL_BUTTON_HEIGHT, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(EMPTY_GAS_BUTTON_TYPE, s);
        
        String closeRoadButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_ROAD);
        sT = new SpriteType(CLOSE_ROAD_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + closeRoadButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String closeRoadMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_ROAD_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + closeRoadMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10, 200+(2*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_ROAD_BUTTON_TYPE, s);
        
        String closeIntersectionButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_INTERSECTION);
        sT = new SpriteType(CLOSE_INTERSECTION_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + closeIntersectionButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String closeIntersectionMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_INTERSECTION_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + closeIntersectionMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(1*SPECIAL_BUTTON_WIDTH), 200+(2*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_INTERSECTION_BUTTON_TYPE, s);
        
        String openIntersectionButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_OPEN_INTERSECTION);
        sT = new SpriteType(OPEN_INTERSECTION_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + openIntersectionButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String openIntersectionMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_OPEN_INTERSECTION_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + openIntersectionMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(2*SPECIAL_BUTTON_WIDTH), 200+(2*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(OPEN_INTERSECTION_BUTTON_TYPE, s);
        
        String stealButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_STEAL);
        sT = new SpriteType(STEAL_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + stealButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String stealMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_STEAL_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + stealMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(3*SPECIAL_BUTTON_WIDTH), 200+(2*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(STEAL_BUTTON_TYPE, s);
        
        String mindControlButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MIND_CONTROL);
        sT = new SpriteType(MIND_CONTROL_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + mindControlButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String mindControlMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MIND_CONTROL_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + mindControlMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10, 200+(3*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(MIND_CONTROL_BUTTON_TYPE, s);
        
        String intangibilityButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INTANGIBILITY);
        sT = new SpriteType(INTANGIBILITY_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + intangibilityButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String intangibilityMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_INTANGIBILITY_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + intangibilityMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(1*SPECIAL_BUTTON_WIDTH), 200+(3*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(INTANGIBILITY_BUTTON_TYPE, s);
        
        String mindlessTerrorButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MINDLESS_TERROR);
        sT = new SpriteType(MINDLESS_TERROR_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + mindlessTerrorButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String mindlessTerrorMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_MINDLESS_TERROR_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + mindlessTerrorMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(2*SPECIAL_BUTTON_WIDTH), 200+(3*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(MINDLESS_TERROR_BUTTON_TYPE, s);
        
        String flyingButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FLYING);
        sT = new SpriteType(FLYING_BUTTON_TYPE);
        img = loadImageWithColorKey(imgPath + flyingButton, COLOR_KEY);
        sT.addState(pathXStates.VISIBLE_STATE.toString(), img);
        String flyingMouseOverButton = props.getProperty(pathXPropertyType.IMAGE_BUTTON_FLYING_MOUSE_OVER);
        img = loadImageWithColorKey(imgPath + flyingMouseOverButton, COLOR_KEY);
        sT.addState(pathXStates.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 10+(3*SPECIAL_BUTTON_WIDTH), 200+(3*SPECIAL_BUTTON_HEIGHT), 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiButtons.put(FLYING_BUTTON_TYPE, s);
        
        
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
        s = new Sprite(sT, LEVEL_DIALOG_X, LEVEL_DIALOG_Y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
        guiDialogs.put(LEVEL_DIALOG_TYPE, s);
    }
    
    /**
     * Initializes the game data used by the application.
     */
    @Override
    public void initData(){
        // INIT OUR ERROR HANDLER
        //errorHandler = new pathXErrorHandler(window);
        
        //INIT OUR FILE MANAGER
        fileManager = new pathXFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        //record = fileManager.loadRecord();
        record = new Record();
        
        // INIT OUR DATA MANAGER
        data = new pathXDataModel(this);
    }
    
    @Override
    public void initAudioContent(){
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(pathXPropertyType.PATH_AUDIO);

            // LOAD ALL THE AUDIO
            loadAudioCue(pathXPropertyType.AUDIO_MENU);
            loadAudioCue(pathXPropertyType.AUDIO_GAME);
            loadAudioCue(pathXPropertyType.AUDIO_LOSS);
            loadAudioCue(pathXPropertyType.AUDIO_BANDIT);
            loadAudioCue(pathXPropertyType.AUDIO_ZOMBIE);
            loadAudioCue(pathXPropertyType.AUDIO_SPECIALS);
            loadAudioCue(pathXPropertyType.AUDIO_WIN);
            loadAudioCue(pathXPropertyType.AUDIO_CRASH);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(pathXPropertyType.AUDIO_MENU.toString(), true);
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e)
        { }     
    }
    
     /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(pathXPropertyType audioCueType) 
            throws  UnsupportedAudioFileException, IOException, LineUnavailableException, 
                    InvalidMidiDataException, MidiUnavailableException
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(pathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);        
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
