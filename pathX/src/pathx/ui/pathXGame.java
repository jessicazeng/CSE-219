package pathx.ui;

import audio_manager.AudioManager;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JFrame;
import javax.swing.JPanel;
import mini_game.MiniGameEventRelayer;
import mini_game.MiniGameState;
import mini_game.MiniGameTimerTask;
import mini_game.Sprite;

/**
 *
 * @author Jessica
 */
public abstract class pathXGame {
    // THIS IS THE NAME OF THE CUSTOMIZED GAME
    protected String name;
    
    // WE ARE GOING TO HAVE 2 THREADS AT WORK IN THIS APPLICATION,
    // THE MAIN GUI THREAD, WHICH WILL LISTEN FOR USER INTERACTION
    // AND CALL OUR EVENT HANDLERS, AND THE TIMER THREAD, WHICH ON
    // A FIXED SCHEDULE (e.g. 30 times/second) WILL UPDATE ALL THE
    // GAME DATA AND THEN RENDER. THIS LOCK WILL MAKE SURE THAT
    // ONE THREAD DOESN'T RUIN WHAT THE OTHER IS DOING (CALLED A
    // RACE CONDITION). FOR EXAMPLE, IT WOULD BE BAD IF WE STARTED
    // RENDERING THE GAME AND 1/2 WAY THROUGH, THE GAME DATA CHANGED.
    // THAT MAY CAUSE BIG PROBLEMS. EACH THREAD WILL NEED TO LOCK
    // THE DATA BEFORE EACH USE AND THEN UNLOCK WHEN DONE WITH IT.
    protected ReentrantLock dataLock;
    
    // EVERY GAME WILL HAVE A SINGLE CANVAS INSIDE
    // OUR WINDOW. WE CAN PAINT AND HANDLE EVENTS
    // FOR BUTTONS OURSELVES
    protected JFrame window;
    protected int windowWidth;
    protected int windowHeight;
    protected JPanel canvas;
    
    // HERE ARE OUR GUI COMPONENTS. NOTE WE ARE NOT
    // USING SWING COMPONENTS (except JFrame and JPanel),
    // WE ARE MANAGING THE BUTTONS AND OTHER DISPLAY
    // ITEMS OURSLEVES. TreeMap IS SIMPLY A BINARY SEARCH
    // TREE WHERE THE String PARAMETER SPECIFIES THE ID
    // USED FOR KEEPING THE CONTROLS IN SORTED ORDER.
    // ONE HAS TO UNIQUELY NAME EACH CONTROL THAT GOES
    // IN THESE DATA STRUCTURES
    protected TreeMap<String, Sprite> guiButtons;
    protected TreeMap<String, Sprite> guiDecor;
    protected TreeMap<String, Sprite> guiDialogs;
    
    // WE WILL HAVE A TIMER RUNNING IN ANOTHER THREAD THAT
    // EVERY frameDuration AMOUNT OF TIME WILL UPDATE THE
    // GAME AND THEN RENDER. frameDuration IS THE NUMBER
    // OF MILLISECONDS IT TAKES PER FRAME, SO WE CAN
    // CALCULATE IT BY DIVIDING 1000 (MILLISECONDS IN 1 SEC.)
    // BY THE FRAME RATE, i.e. framesPerSecond
    // NOTE THAT THE GameTimerTask WILL HAVE THE CUSTOM
    // BEHAVIOUR PERFORMED EACH FRAME BY THE GAME
    protected Timer gameTimer;
    protected MiniGameTimerTask gameTimerTask;
    protected int framesPerSecond;
    protected int frameDuration;
    
    // WE'LL MANAGE THE EVENTS IN A SLIGHTLY DIFFERENT WAY
    // THIS TIME SINCE WE ARE RENDERING ALL OUR OWN
    // BUTTONS AND BECAUSE WE HAVE TO WORRY ABOUT 2 THREADS.
    // THIS OBJECT WILL RELAY BOTH MOUSE INTERACTIONS AND
    // KEYBOARD INTERACTIONS TO THE CUSTOM HANDLER, MAKING
    // SURE TO AVOID RACE CONDITIONS
    protected MiniGameEventRelayer gamh;
    
    // THIS CLASS WILL PLAY OUR AUDIO FILES
    protected AudioManager audio;
    
    // THIS ALLOWS FOR CUSTOM KEY RESPONSES
    protected KeyListener keyHandler;
    
    public void initMiniGame(   String appTitle, int initFramesPerSecond, 
                                int initWindowWidth, int initWindowHeight)
    {
        // KEEP THE TITLE AND FRAME RATE
        name = appTitle;
        framesPerSecond = initFramesPerSecond;

        // CALCULATE THE TIME EACH FRAME SHOULD TAKE
        frameDuration = 1000 / framesPerSecond;

        // CONSTRUCT OUR LOCK, WHICH WILL MAKE SURE
        // WE ARE NOT UPDATING THE GAME DATA SIMULATEOUSLY
        // IN TWO DIFFERENT THREADS
        dataLock = new ReentrantLock();

        // AND NOW SETUP THE FULL APP. NOTE THAT SOME
        // OF THESE METHODS MUST BE CUSTOMLY PROVIDED FOR
        // EACH GAME IMPLEMENTATION
        //initAudio();
        initWindow(initWindowWidth, initWindowHeight);
        initData();
        initViewport();
        initGUI();
        initHandlers();
        initTimer();

        // LET THE USER START THE GAME ON DEMAND
        //data.setGameState(MiniGameState.NOT_STARTED);
    }
}
