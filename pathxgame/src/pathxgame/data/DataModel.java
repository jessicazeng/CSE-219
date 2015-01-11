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
    private ArrayList<Zombie> zombies;
    
    private boolean specialSelected;
    private String special;
    private int zombieCollisions;
    
    // Specials booleans
    private boolean freeze;
    private int incPlayerSpeed;
    
    public DataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame; // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        
        record = ((Game) miniGame).getPlayerRecord();
        specialSelected = false;
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
    
    public ArrayList<Zombie> getZombies(){
        return zombies;
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
    
    public void setSpecialSelected(){
        specialSelected = true;
    }
    
    public void setSpecial(String value){
        special = value;
    }
    
    public void setFreeze(){
        if(freeze==false)
            freeze = true;
        else
            freeze = false;
        
        money -= 10;
        specialSelected = false;
    }
    
    public void incPlayerSpeed() {
        incPlayerSpeed++;
        money -= 20;
        specialSelected = false;
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
    
    public void loadZombies(){
        zombies = new ArrayList();
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        int numZombies = record.getNumZombies(currentLevel);
        for(int i=0; i<numZombies; i++){
            Random rand = new Random();
            int node = rand.nextInt(intersections.size());
            while(node==0 || node==1)
                node = rand.nextInt(intersections.size());
            
            Intersection intersection = intersections.get(node);
            ArrayList<Intersection> path = zombiePath(intersection);
            
            int x = intersection.getX();
            int y = intersection.getY();
            
            SpriteType sT = new SpriteType("Zombie" + i);
            addSpriteType(sT);
            Zombie newZombie = new Zombie(sT, x, y, 0, 0, States.INVISIBLE_STATE.toString());
            newZombie.setPath(path);
            newZombie.setID(i);
            newZombie.setNode(node);
            zombies.add(newZombie);
        }
    }
    
    public ArrayList<Intersection> zombiePath(Intersection intersection){
        ArrayList<Intersection> path = new ArrayList<Intersection>();
        path.add(intersection);
        
        ArrayList<Intersection> adjacentIntersections = intersection.getAdjacentIntersections();
        
        Random rand = new Random();
        int node = rand.nextInt(adjacentIntersections.size());
        while(node==0 || node==1)
            node = rand.nextInt(adjacentIntersections.size());
        
        Intersection nextIntersection = adjacentIntersections.get(node);
        path.add(nextIntersection);
        
        return path;
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
                initSpeed *= (1 + (incPlayerSpeed * 0.2));
                // decrease player speed by 10% for each zombie collision
                if(zombieCollisions > 0){ 
                    initSpeed -= initSpeed * (zombieCollisions * 0.1);
                }

                // SEND THEM TO THEIR DESTINATION
                player.startMovingToTarget(initSpeed);
                player.setStartingPos(tile2x, tile2y);
                player.setNextNode(index2);
            }
        }
    }
    
    public void movePolice(int ID){
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
    
    public void moveBandit(int ID){
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
    
    public void moveZombie(int ID){
        Zombie zombieSprite = zombies.get(ID);
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        int currentNode = zombieSprite.getNode();
        Intersection intersection1 = intersections.get(currentNode);

        zombieSprite.incPathIndex();
        int nextNode = zombieSprite.getPathIndex();
        Intersection nextIntersection = (zombieSprite.getPath()).get(nextNode);
        int node = nextIntersection.getID();
        Intersection intersection2 = intersections.get(node);

        // GET THE TILE TWO LOCATION
        int x2 = intersection2.getX();
        int y2 = intersection2.getY();

        if(intersection1.isOpen()){
            // MOVE ZOMBIE
            zombieSprite.setTarget(x2, y2);

            // FIND SPEED LIMIT OF ROAD
            Road road = findRoad(intersection1, intersection2);
            int speedLimit = (road.getSpeedLimit())/10;

            // SEND TO DESTINATION
            zombieSprite.startMovingToTarget(speedLimit);
            zombieSprite.setNode(node);
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
            if(specialSelected)
                applySpecial(x, y);
            else
                pressOnGameScreen(game, x, y);
        }
    }
    
    public void applySpecial(int x, int y){
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        // Green or red light
        if(special.equals(GREENLIGHT_BUTTON_TYPE) || special.equals(REDLIGHT_BUTTON_TYPE)){
            if(money >= 5){
                for(int i=0; i<intersections.size(); i++){
                    Intersection intersection2 = intersections.get(i);

                    int screenPositionX1 = viewport.getViewportX();
                    int screenPositionY1 = viewport.getViewportY();

                    // position of intersection on node
                    int node2x = intersection2.getX() - screenPositionX1;
                    int node2y = intersection2.getY() - screenPositionY1;

                    Point point = new Point(node2x-12, node2y-12);
                    Point point2 = new Point(x, y);
                    Rectangle bounds = new Rectangle(point, new Dimension(INTERSECTION_WIDTH, INTERSECTION_WIDTH));

                    if (bounds.contains(point2)) {
                        if(special.equals(REDLIGHT_BUTTON_TYPE)){
                            intersection2.setOpen(false);
                            intersection2.setTime(System.currentTimeMillis() + 10000);
                        } else
                            intersection2.setOpen(true);
                        money -= 5;
                        specialSelected = false;
                    }
                }
            }
        } 
        // flat tire
        else if(special.equals(FLATTIRE_BUTTON_TYPE)){
            int x1 = viewport.getViewportX();
            int y1 = viewport.getViewportY();
                
            if(money >= 20){
                for(Police p: police){
                    // position of police car
                    int node2x = (int)p.getX() - x1 - SPRITE_MARGIN;
                    int node2y = (int)p.getY() - y1 - SPRITE_MARGIN;

                    Point point = new Point(node2x, node2y);
                    Point point2 = new Point(x, y);
                    Rectangle policeCar = new Rectangle(point, new Dimension(59, 42));
                    if(policeCar.contains(point2)){
                        p.setFlatTire();
                        money -= 20;
                        specialSelected = false;
                    }
                }

                for(Bandit b: bandits){
                    // position of bandit car
                    int node2x = (int)b.getX() - x1 - SPRITE_MARGIN;
                    int node2y = (int)b.getY() - y1 - SPRITE_MARGIN;

                    Point point = new Point(node2x, node2y);
                    Point point2 = new Point(x, y);
                    Rectangle banditCar = new Rectangle(point, new Dimension(49, 32));
                    if(banditCar.contains(point2)){
                        b.setFlatTire();
                        money -= 20;
                        specialSelected = false;
                    }
                }

                for(Zombie z: zombies){
                    // position of zombie car
                    int node2x = (int)z.getX() - x1 - SPRITE_MARGIN;
                    int node2y = (int)z.getY() - y1 - SPRITE_MARGIN;

                    Point point = new Point(node2x, node2y);
                    Point point2 = new Point(x, y);
                    Rectangle zombieCar = new Rectangle(point, new Dimension(49, 32));
                    if(zombieCar.contains(point2)){
                        z.setFlatTire();
                        money -= 20;
                        specialSelected = false;
                    }
                }
            }
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
     * Updates the player record when player does not pass a level
     */
    public void endGameAsLoss(){
        super.endGameAsLoss();
        
        ((Game)miniGame).openDialog();
        pause();
        
        // player loses 10% of his/her money when he/she does not pass a level
        int newBalance = (int)(record.getBalance() * 0.1);
        record.incBalance(0 - newBalance);
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
        pause();
        
        ((Game)miniGame).openDialog();
        
        record.completedLevel(currentLevel);
    }

    @Override
    public void reset(MiniGame game) {
        if(super.isPaused())
            unpause();
        
        setAdjacentIntersections();
        
        loadPolice();
        loadBandits();
        loadZombies();
        
        zombieCollisions = 0;
        incPlayerSpeed = 0;
        
        ArrayList<Road> roads = record.getRoads(currentLevel);
        for (int r = 0; r < roads.size(); r++){
            roads.get(r).setClosed(false);
        }
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        for(int i=0; i<intersections.size(); i++)
            intersections.get(i).setBlocked(false);
    }
    
    /* 
     *Checks if the player intersects with police, bandits and zombies
     */
    public void updateBots(){
        // check for collisions with police
        for(int i=0; i<police.size(); i++){
            Police policeSprite = police.get(i);
            
            // check if player and police overlap
            Point playerPoint = new Point((int)player.getX()+5, (int)player.getY()+5);
            Rectangle playerCar = new Rectangle(playerPoint, new Dimension(56, 30));
            Point policePoint = new Point((int)policeSprite.getX()+5, (int)policeSprite.getY()+5);
            Rectangle policeCar = new Rectangle(policePoint, new Dimension(59, 42));
            if(playerCar.intersects(policeCar))
                endGameAsLoss(); // got caught by police
        }
        
        // check for collision with bandit
        for(int i=0; i<bandits.size(); i++){
            Bandit banditSprite = bandits.get(i);
            
            // check if player and bandit overlap
            Point playerPoint = new Point((int)player.getX()+5, (int)player.getY()+5);
            Rectangle playerCar = new Rectangle(playerPoint, new Dimension(56, 30));
            Point banditPoint = new Point((int)banditSprite.getX()+5, (int)banditSprite.getY()+5);
            Rectangle banditCar = new Rectangle(banditPoint, new Dimension(49, 32));
            if(playerCar.intersects(banditCar)){
                if(banditSprite.hasRobbedPlayer() == false){
                    banditSprite.setRobbed(true);
                    money -= money * 0.1;
                }
            } else{
                banditSprite.setRobbed(false);
            }
        }
        
        // check for collision with zombie
        for(int i=0; i<zombies.size(); i++){
            Zombie zombieSprite = zombies.get(i);
            
            // check if player and police overlap
            Point playerPoint = new Point((int)player.getX()+5, (int)player.getY()+5);
            Rectangle playerCar = new Rectangle(playerPoint, new Dimension(56, 30));
            Point zombiePoint = new Point((int)zombieSprite.getX()+5, (int)zombieSprite.getY()+5);
            Rectangle zombieCar = new Rectangle(zombiePoint, new Dimension(49, 32));
            if(playerCar.intersects(zombieCar)){
                if(zombieSprite.hasSlowedPlayer() == false){
                    zombieSprite.setSlowed(true);
                    zombieCollisions += 1;
                }
            } else{
                zombieSprite.setSlowed(false);
            }
        }
    }
    
    @Override
    public void updateAll(MiniGame game) {
        try
        {
            // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
            game.beginUsingData();
            
            if(!freeze){ // if all cars are not frozen by the special
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

                // update zombie sprites
                for(int i=0; i<zombies.size(); i++){
                    Zombie zombieSprite = zombies.get(i);
                    zombieSprite.update(miniGame);
                }
            }
            
            //check if bots overlap with player
            updateBots();
            
            // update intersections - check red lights
            ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
            for(int i=0; i<intersections.size(); i++){
                if(System.currentTimeMillis() >= intersections.get(i).getTime()){
                    if(!(intersections.get(i).isBlocked()))
                        intersections.get(i).setOpen(true);
                }
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
