/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.MiniGameState;
import mini_game.SpriteType;
import pathxgame.Pathx;
import static pathxgame.pathXConstants.*;
import pathxgame.ui.EventHandler;
import pathxgame.ui.Game;
import pathxgame.ui.States;
import properties_manager.PropertiesManager;

/**
 *
 * @author Jessica
 */
public class DataModel extends MiniGameDataModel {
    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;
    
    // LEVEL
    private String currentLevel;
    private Record record;
    
    // keeps track of money stolen during a level
    private int money;
    
    private Player player;
    
    // Lists of Sprites
    private ArrayList<Police> police;
    private ArrayList<Bandit> bandits;
    
    public DataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame; // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        
        record = ((Game) miniGame).getPlayerRecord();
    }
    
    //-------------------------ACCESSOR METHODS-------------------------------
    
    public String getCurrentLevel()
    {
        return currentLevel;
    }
    
    public int getMoney(){
        return money;
    }
    
    public Player getplayer(){
        return player;
    }
    
    public ArrayList<Police> getPolice(){
        return police;
    }
    
    public ArrayList<Bandit> getBandits(){
        return bandits;
    }
    
    //------------------------MUTATOR METHODS----------------------------------
    
    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
        
        money = record.getMoney(currentLevel);
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
    
    // -----------------------LOAD SPRITES------------------------------------
    
    public void initPlayer(){
        SpriteType sT = new SpriteType("Player");
        addSpriteType(sT);
        
        Intersection intersection = record.getIntersections(currentLevel).get(0);
        int x = intersection.getX();
        int y = intersection.getY();
        
        player = new Player(sT, x, y, 0, 0, States.INVISIBLE_STATE.toString());
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
            Police newPolice = new Police(sT, x, y, 0, 0, States.INVISIBLE_STATE.toString());
            newPolice.setID(i);
            newPolice.setNode(node);
            police.add(newPolice);
        }
    }
    
    public void loadBandits(){
        bandits = new ArrayList();
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        int numBandits = record.getNumBandits(currentLevel);
        for(int i=0; i<numBandits; i++){
            Random rand = new Random();
            int node = rand.nextInt((intersections.size())/2);
            while(node==0 || node==1)
                node = rand.nextInt((intersections.size())/2);
            
            Intersection intersection = intersections.get(node);
            int x = intersection.getX();
            int y = intersection.getY();
            
            SpriteType sT = new SpriteType("Bandit" + i);
            addSpriteType(sT);
            Bandit newBandit = new Bandit(sT, x, y, 0, 0, States.INVISIBLE_STATE.toString());
            newBandit.setID(i);
            newBandit.setNode(node);
            bandits.add(newBandit);
        }
    }
    
    // ----------------------MOVE SPRITES--------------------------------------
    
    /**
     * Moves player to a specified node
     */
    public void movePlayer(int index2)
    {
        int currentNode = player.getCurrentNode();
        Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);
        Intersection intersection2 = record.getIntersections(currentLevel).get(index2);
        Boolean isAdjacent = intersection1.isAdjacent(intersection2);
        
        if(intersection2.isBlocked() == false){
            if(isAdjacent == true){ // if destination node is one edge away
                // get location of destination node
                int tile2x = intersection2.getX();
                int tile2y = intersection2.getY();

                // THEN MOVE PLAYER
                player.setTarget(tile2x, tile2y);

                // FIND SPEED LIMIT OF ROAD
                Road road = findRoad(intersection1, intersection2);
                int initSpeed = road.getSpeedLimit()/10;

                // SEND THEM TO THEIR DESTINATION
                player.startMovingToTarget(initSpeed);
                player.setCurrentNode(index2);
                player.setStartingPos(tile2x, tile2y);
            }
        }
    }
    
    public void movePolice(int ID)
    {
        Police policeSprite = police.get(ID);
        
        int currentNode = policeSprite.getNode();    
        Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);

        Random rand = new Random();
        int node = rand.nextInt(record.getIntersections(currentLevel).size());
        Intersection intersection2 = record.getIntersections(currentLevel).get(node);
        while(node==0 || node==1){
            node = rand.nextInt(record.getIntersections(currentLevel).size());
            intersection2 = record.getIntersections(currentLevel).get(node);
        }

        Boolean isAdjacent = intersection1.isAdjacent(intersection2);

        if(isAdjacent == true){
            // GET THE TILE TWO LOCATION
            int x2 = intersection2.getX();
            int y2 = intersection2.getY();
            
            if(intersection1.isOpen()){
                // THEN MOVE POLICE CAR
                policeSprite.setTarget(x2, y2);

                // FIND SPEED LIMIT OF ROAD
                Road road = findRoad(intersection1, intersection2);
                int speedLimit = (road.getSpeedLimit())/10;

                // SEND THEM TO THEIR DESTINATION
                policeSprite.startMovingToTarget(speedLimit);
                policeSprite.setNode(node);
            } 
        }
    }
    
    public void moveBandit(int ID)
    {
        Bandit banditSprite = bandits.get(ID);
        
        int currentNode = banditSprite.getNode();
        Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);

        Random rand = new Random();
        int node = rand.nextInt((record.getIntersections(currentLevel).size())/2);
        Intersection intersection2 = record.getIntersections(currentLevel).get(node);
        while(node==0 || node==1){
            node = rand.nextInt((record.getIntersections(currentLevel).size())/2);
            intersection2 = record.getIntersections(currentLevel).get(node);
        }

        Boolean isAdjacent = intersection1.isAdjacent(intersection2);
        if(isAdjacent == true){
            // GET THE TILE TWO LOCATION
            int x2 = intersection2.getX();
            int y2 = intersection2.getY();

            if(intersection1.isOpen()){
                // THEN MOVE PLAYER
                banditSprite.setTarget(x2, y2);

                // FIND SPEED LIMIT OF ROAD
                Road road = findRoad(intersection1, intersection2);
                int speedLimit = (road.getSpeedLimit())/10;

                // SEND THEM TO THEIR DESTINATION
                banditSprite.startMovingToTarget(speedLimit);
                banditSprite.setNode(node);
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

    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        if(((Game)miniGame).isCurrentScreenState(LEVEL_SCREEN_STATE)){
            pressOnLevelScreen(game, x, y);
        } else if(((Game)miniGame).isCurrentScreenState(GAME_SCREEN_STATE)){
            pressOnGameScreen(game, x, y);
        }
    }
    
    /**
     * Check for mouse press on level selection screen
     * @param game
     * @param x
     * @param y 
     */
    public void pressOnLevelScreen(MiniGame game, int x, int y){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(Pathx.pathXPropertyType.LEVEL_OPTIONS);
        
        for (int i = 0; i < levels.size(); i++){
            String levelName = levels.get(i);

            int screenPositionX1 = viewport.getViewportX();
            int screenPositionY1 = viewport.getViewportY();

            int levelX = record.getLevelPositionX(levelName) - screenPositionX1;
            int levelY = record.getLevelPositionY(levelName) - screenPositionY1;

            Point point = new Point(levelX, levelY);
            Point point2 = new Point(x, y);
            Rectangle bounds = new Rectangle(point, new Dimension(17, 17));

            if(bounds.contains(point2)){
                if(!record.isLocked(levelName)){
                    EventHandler eventHandler = ((Game)miniGame).getEventHandler();
                    eventHandler.respondToSelectLevelRequest(levelName);
                }
             }
        }
    }
    
    /**
     * Checks for mouse press on Game screen.
     * @param game
     * @param x
     * @param y 
     */
    public void pressOnGameScreen(MiniGame game, int x, int y){
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        for(int i=0; i<intersections.size(); i++){
            if(i != player.getCurrentNode()){
                Intersection intersection = intersections.get(i);

                int x1 = viewport.getViewportX();
                int y1 = viewport.getViewportY();

                // position of intersection on node
                int node2x = intersection.getX() - x1 - SPRITE_MARGIN;
                int node2y = intersection.getY() - y1 - SPRITE_MARGIN;

                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds;
                if(i == 0 || i == 1)
                    bounds = new Rectangle(point, new Dimension(50, 50));
                else
                    bounds = new Rectangle(point, new Dimension(30, 30));

                if (bounds.contains(point2)) {
                    movePlayer(i);
                }
            }
        }
    }
    
    /**
     * Called when the game is won, it will record the ending game time, update
     * the player record, display the win dialog, and play the win animation.
     */
    @Override
    public void endGameAsWin(){
        record.incBalance(money);
        
        // UPDATE THE GAME STATE USING THE INHERITED FUNCTIONALITY
        super.endGameAsWin();
        
        super.setGameState(MiniGameState.WIN);
        
        ((Game)miniGame).openDialog();
        
        record.completedLevel(currentLevel);
    }

    @Override
    public void reset(MiniGame game) {
        loadPolice();
        loadBandits();
        
        ArrayList<Road> roads = record.getRoads(currentLevel);
        for (int r = 0; r < roads.size(); r++){
            roads.get(r).setClosed(false);
        }
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        for(int i=0; i<intersections.size(); i++)
            intersections.get(i).setBlocked(false);
    }

    @Override
    public void updateAll(MiniGame game) {
        try
        {
            // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
            game.beginUsingData();
            
            // update player
            player.update(miniGame);
            
            // update police sprites
            for(int i=0; i<police.size(); i++){
                Police policeSprite = police.get(i);
                
                policeSprite.update(miniGame);
            }
            
            // update bandit sprites
            for(int i=0; i<bandits.size(); i++){
                Bandit banditSprite = bandits.get(i);
                banditSprite.update(miniGame);
            }
            
            // check if player reached destination
            if(player.getCurrentNode()==1)
                if(!won())
                    endGameAsWin();
        } finally
        {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
    }

    @Override
    public void updateDebugText(MiniGame game) {
        
    }
    
}
