package pathx.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import pathx.PathX;
import static pathx.pathXConstants.*;
import pathx.ui.pathXGame;
import pathx.ui.pathXStates;
import properties_manager.PropertiesManager;

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
    ArrayList<Police> police;
    ArrayList<Zombie> zombies;
    
    // THIS IS THE TILE THE USER IS DRAGGING
    private Intersection selectedNode;
    private int selectedNodeIndex;
    
    private Player player;
    
    // LEVEL
    private String currentLevel;
    
    private Record record;
    
    public pathXDataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        
        bandits = new ArrayList();
        police = new ArrayList();
        zombies = new ArrayList();
        
        selectedNode = null;
        
        record = ((pathXGame) miniGame).getPlayerRecord();
    }
    
    public void initPlayer(){
        SpriteType sT = new SpriteType("Player");
        addSpriteType(sT);
        
        //Record record = ((pathXGame) miniGame).getPlayerRecord();
        Intersection intersection = record.getIntersections(currentLevel).get(0);
        int x = intersection.getX() - 80;
        int y = intersection.getY() + 30;
        
        player = new Player(sT, x, y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
    }
    
    // ACCESSOR METHODS
    public String getCurrentLevel()
    {
        return currentLevel;
    }
    
    public Player getplayer(){
        return player;
    }
    
    public ArrayList<Bandit> getBandits(){
        return bandits;
    }
    
    public ArrayList<Police> getPolice(){
        return police;
    }
    
    public ArrayList<Zombie> getZombies(){
        return zombies;
    }
    
    public Intersection getSelectedNode(){
        return selectedNode;
    }
    
    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
    }
    
    public void loadBandits(){
        bandits = new ArrayList();
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        int numBandits = record.getNumBandits(currentLevel);
        for(int i=0; i<numBandits; i++){
            Random rand = new Random();
            int node = rand.nextInt(intersections.size());
            while(node==0 || node==1)
                node = rand.nextInt(intersections.size());
            
            Bandit newBandit = new Bandit(node);
            bandits.add(newBandit);
        }
    }
    
    public void loadPolice(){
        police = new ArrayList();
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        int numPolice = record.getNumPolice(currentLevel);
        for(int i=0; i<numPolice; i++){
            Random rand = new Random();
            int node = rand.nextInt(intersections.size());
            while(node==0 || node==1)
                node = rand.nextInt(intersections.size());
            
            Intersection intersection = intersections.get(node);
            int x = intersection.getX();
            int y = intersection.getY();
            SpriteType sT = new SpriteType("Police" + i);
            addSpriteType(sT);
            Police newPolice = new Police(sT, x, y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
            newPolice.setID(i);
            newPolice.setNode(node);
            police.add(newPolice);
        }
    }
    
    public void movePolice(int ID)
    {
        Police policeSprite = police.get(ID);
        int currentNode = policeSprite.getNode();
        
        Random rand = new Random();
            int node = rand.nextInt(record.getIntersections(currentLevel).size());
            while(node==0 || node==1)
                node = rand.nextInt(record.getIntersections(currentLevel).size());
            
        Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);
        Intersection intersection2 = record.getIntersections(currentLevel).get(node);
        
        Boolean isAdjacent = intersection1.isAdjacent(intersection2);
        
        if(isAdjacent == true){
            // GET THE TILE TWO LOCATION
            int x1 = policeSprite.getStartX();
            int y1 = policeSprite.getStartY();
            int tile2x = intersection2.getX() + 50;
            int tile2y = intersection2.getY() - 30;

            int differenceX = tile2x - x1 - 60;
            int differenceY = tile2y - y1 + 20;

            int newX = x1+differenceX;
            int newY = y1+differenceY;

            // THEN MOVE PLAYER
            policeSprite.setTarget(newX, newY);
            
            // FIND SPEED LIMIT OF ROAD
            Road road = findRoad(intersection1, intersection2);
            int speedLimit = (road.getSpeedLimit())/10;

            // SEND THEM TO THEIR DESTINATION
            policeSprite.startMovingToTarget(speedLimit);
            policeSprite.setNode(node);
            player.setStartingPos(x1+tile2x, y1+tile2y);
        } 
    } 
    
    public void loadZombies(){
        zombies = new ArrayList();
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        int numZombies = record.getNumZombies(currentLevel);
        for(int i=0; i<numZombies; i++){
            Random rand = new Random();
            int node = rand.nextInt(intersections.size());
            while(node==0 || node==1)
                node = rand.nextInt(intersections.size());
            Zombie newZombie = new Zombie(node);
            zombies.add(newZombie);
        }
    }
    
    public void setAdjacentIntersections(){
        ArrayList<Road> roads = record.getRoads(currentLevel);
        for (int i = 0; i < roads.size(); i++){
            Road road = roads.get(i);
            Intersection intersection1 = road.getNode1();
            Intersection intersection2 = road.getNode2();
            Boolean isOneWay = road.isOneWay();
            
            if(isOneWay == true){
                intersection1.addAdjacentIntersection(intersection2);
            } else{
                intersection1.addAdjacentIntersection(intersection2);
                intersection2.addAdjacentIntersection(intersection1);
            }
        }
    }
    
    public Road findRoad(Intersection from, Intersection to){
        Road road = null;
        ArrayList<Road> roads = record.getRoads(currentLevel);
        for (int i = 0; i < roads.size(); i++){
            road = roads.get(i);
            Intersection intersection1 = road.getNode1();
            Intersection intersection2 = road.getNode2();
            
            if(from.equals(intersection1) && to.equals(intersection2))
                return road;
        }
        return road;
    }
    
    public Road findRoad(int index1, int index2){
        Road road = null;
        Intersection intersection1 = record.getIntersections(currentLevel).get(index1);
        Intersection intersection2 = record.getIntersections(currentLevel).get(index2);
        
        ArrayList<Road> roads = record.getRoads(currentLevel);
        for (int i = 0; i < roads.size(); i++){
            road = roads.get(i);
            Intersection uno = road.getNode1();
            Intersection dos = road.getNode2();
            
            if(intersection1.equals(uno) && intersection2.equals(dos))
                return road;
        }
        
        return road;
    }
    
    public ArrayList<Intersection> findPath(Intersection from, Intersection to){
        ArrayList<Intersection> path = new ArrayList();
        Intersection startNode = from;
        
        Boolean found = false;
        while(found == false){
            if(startNode.isAdjacent(to)){
                path.add(to);
                found = true;
            } else{
                ArrayList<Intersection> intersections = startNode.getAdjacentIntersections();
                int size = intersections.size();
                Random rand = new Random();
                int random = rand.nextInt(size);
                Intersection randomIntersection = intersections.get(random);
                path.add(randomIntersection);
                startNode = randomIntersection;
            }
        }
        
        return path;
    }
    
    /**
     * Moves player
     */
    public void movePlayer(int index2)
    {
        int currentNode = player.getCurrentNode();
        Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);
        Intersection intersection2 = record.getIntersections(currentLevel).get(index2);
        
        Boolean isAdjacent = intersection1.isAdjacent(intersection2);
        
        if(isAdjacent == true){
            int screenPositionX1 = viewport.getViewportX();
            int screenPositionY1 = viewport.getViewportY();

            // GET THE TILE TWO LOCATION
            int x1 = player.getStartX();
            int y1 = player.getStartY();
            int tile2x = intersection2.getX();
            int tile2y = intersection2.getY();

            int differenceX = tile2x - x1 - 60;
            int differenceY = tile2y - y1 + 20;

            int newX = x1+differenceX;
            int newY = y1+differenceY;

            // THEN MOVE PLAYER
            player.setTarget(newX, newY);
            
            // FIND SPEED LIMIT OF ROAD
            Road road = findRoad(intersection1, intersection2);
            int speedLimit = (road.getSpeedLimit())/10;

            // SEND THEM TO THEIR DESTINATION
            player.startMovingToTarget(speedLimit);
            player.setCurrentNode(index2);
            player.setStartingPos(x1+tile2x, y1+tile2y);
        } else{
            ArrayList<Intersection> path = findPath(intersection1, intersection2);
            player.initPath(path);
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
    public void checkMousePressOnSprites(MiniGame game, int x, int y){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        //Record record = ((pathXGame) miniGame).getPlayerRecord();
        
        if(((pathXGame)miniGame).isCurrentScreenState(GAME_SCREEN_STATE)){
            if(!isPaused()){
                ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
                for(int i=0; i<intersections.size(); i++){
                    Intersection intersection2 = intersections.get(i);
                    
                    int screenPositionX1 = viewport.getViewportX();
                    int screenPositionY1 = viewport.getViewportY();
            
                    // position of intersection on node
                    int node2x = intersection2.getX() - screenPositionX1 + 170;
                    int node2y = intersection2.getY() - screenPositionY1;

                    Point point = new Point(node2x, node2y);
                    Point point2 = new Point(x, y);
                    Rectangle bounds;
                    if(i == 0 || i == 1)
                        bounds = new Rectangle(point, new Dimension(50, 50));
                    else
                        bounds = new Rectangle(point, new Dimension(30, 30));

                    if (bounds.contains(point2)) {
                        selectedNode = intersection2;
                        selectedNodeIndex = i;

                        movePlayer(i);
                    }
                }
            }
            
        }
        
        //else if(((pathXGame)miniGame).isCurrentScreenState(LEVEL_SCREEN_STATE)){
        //    ArrayList<String> levels = props.getPropertyOptionsList(PathX.pathXPropertyType.LEVEL_OPTIONS);
        //    for (int i = 0; i < levels.size(); i++){
        //        String levelName = levels.get(i);
                
        //        int screenPositionX1 = viewport.getViewportX();
        //        int screenPositionY1 = viewport.getViewportY();
                
        //        int levelX = record.getLevelPositionX(levelName) - screenPositionX1;
        //        int levelY = record.getLevelPositionY(levelName) - screenPositionY1;
                
        //        Point point = new Point(levelX, levelY);
        //        Point point2 = new Point(x, y);
        //        Rectangle bounds = new Rectangle(point, new Dimension(17, 17));
        //        
        //        if(bounds.contains(point2)){
        //            ((pathXGame)miniGame).pressedLevelButton(levelName);
        //        }
        //    }
        //}
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
        try
        {
            // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
            game.beginUsingData();
            
            // Go through each tile. If its state is visible, check if we are entering a button
            //change it to mouseover state
            
            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            player.update(miniGame);
            
            for(int i=0; i<police.size(); i++){
                Police policeSprite = police.get(i);
                //movePolice(i);
                policeSprite.update(miniGame);
            }
        } finally
        {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
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
