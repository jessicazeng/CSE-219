package pathx.data;

import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import pathx.ui.pathXGame;

/**
 * This class manages the data for the pathX game.
 * 
 * @author Jessica Zeng
 */
public class pathXDataModel extends MiniGameDataModel {
    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;
    
    ArrayList<Bandit> bandits;
    
    // LEVEL
    private String currentLevel;
    
    public pathXDataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        
        bandits = new ArrayList();
    }
    
    // ACCESSOR METHODS
    public String getCurrentLevel()
    {
        return currentLevel;
    }
    
    public ArrayList<Bandit> getBandits(){
        return bandits;
    }
    
    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
    }
    
    public void loadBandits(){
        Record record = ((pathXGame) miniGame).getPlayerRecord();
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        int numBandits = record.getNumBandits(currentLevel);
        for(int i=0; i<numBandits; i++){
            int node = (int) (2 + (Math.random() * (intersections.size() - 2)));
            Bandit newBandit = new Bandit(node);
            bandits.add(newBandit);
        }
    }
    
    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on grid cells.
     *
     * @param game The Sorting Hat game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y)
    {
        
    }
    
    /**
     * Called when the game is won, it will record the ending game time, update
     * the player record, display the win dialog, and play the win animation.
     */
    @Override
    public void endGameAsWin(){
        
    }
    
    /**
     * Updates the player record, adding a game without a win.
     */
    public void endGameAsLoss(){
        
    }
    
     /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
    @Override
    public void reset(MiniGame game){
        
    }
    
    /**
     * Called each frame, this method updates all the game objects.
     *
     * @param game The Sorting Hat game to be updated.
     */
    @Override
    public void updateAll(MiniGame game){
        
    }
    
     /**
     * This method is for updating any debug text to present to the screen. In a
     * graphical application like this it's sometimes useful to display data in
     * the GUI.
     *
     * @param game The Sorting Hat game about which to display info.
     */
    @Override
    public void updateDebugText(MiniGame game){
        
    }
}
