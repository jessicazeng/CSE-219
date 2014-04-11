package pathx.ui;

import mini_game.MiniGame;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.file.pathXFileManager;
import pathx.data.Record;

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
    
    @Override
    public void initGUIHandlers(){
        
    }
    
    @Override
    public void initGUIControls(){
        
    }
    
    @Override
    public void initData(){
        
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
