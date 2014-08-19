package pathx.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import pathx.PathX;
import pathx.PathX.pathXPropertyType;
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
    
    private int money;
    
    private int zombieCollisions;
    
    private boolean specialSelected;
    private String special;
    
    private boolean frozen;
    //private boolean slowed;
    //private boolean speedup;
    
    //private boolean decroadspeed;
    private int playerSpeed;
    private boolean steal;
    
    // This is used for specials that require you to select a node
    // or vehicle to apply it to
    private boolean policeSelected;
    private boolean banditSelected;
    private boolean zombieSelected;
    private int selectedSprite;
    
    public pathXDataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        
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
    
    public int getMoney(){
        return money;
    }
    
    public boolean isFrozen(){
        return frozen;
    }
    
    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
        
        money = record.getMoney(currentLevel);
    }
    
    public void setSpecialSelected(boolean value){
        specialSelected = value;
    }
    
    public void setSpecial(String string){
        special = string;
        
        if(special.equals(STEAL_BUTTON_TYPE)){
            steal = true;
            player.stealTimer = System.currentTimeMillis() + 10000;
            money -= 30;
            
            specialSelected = false;
            
            //if(((pathXGame)miniGame).isSoundDisabled() == false)
            //    miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
        } else if(special.equals(INVINCIBILITY_BUTTON_TYPE)){
            player.invinsible = true;
            player.timer = System.currentTimeMillis() + 10000;
            money -= 40;
            
            specialSelected = false;
            
            //if(((pathXGame)miniGame).isSoundDisabled() == false)
            //    miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
        } else if(special.equals(INC_PLAYER_SPEED_BUTTON_TYPE)){
            playerSpeed += 1;
            money -= 20;
            specialSelected = false;

            if(((pathXGame)miniGame).isSoundDisabled() == false)
                miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
        }
        
        if(((pathXGame)miniGame).isSoundDisabled() == false)
                miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
    }
    
    public void closeRoad(){
        money -= 25;
        
        if(((pathXGame)miniGame).isSoundDisabled() == false)
            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
    }
    
    public void closeIntersection(){
        if(((pathXGame)miniGame).isSoundDisabled() == false)
            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
    }
    
    public void openIntersection(){
        if(((pathXGame)miniGame).isSoundDisabled() == false)
            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
    }
    
    public void freeze(){
        if(frozen == false){
            frozen = true;
            money -= 15;
            
            if(((pathXGame)miniGame).isSoundDisabled() == false)
                miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
        } else{
            frozen = false;
        }
        
        specialSelected = false;
    }
    
    public void setIntangible(){
        player.setTagible(true);
        player.timer = System.currentTimeMillis() + 10000;
        money -= 30;
        specialSelected = false;
        
        if(((pathXGame)miniGame).isSoundDisabled() == false)
            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
    }
    
    /**
     * Decrements the amount of money stolen for the level.
     */
    public void decMoney(){
        int dec = (int) (money * 0.1);
        money -= dec;
    }
    
    public void incMoney(int amt){
        money += amt;
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
            
            Intersection intersection = intersections.get(node);
            int x = intersection.getX();
            int y = intersection.getY();
            
            SpriteType sT = new SpriteType("Bandit" + i);
            addSpriteType(sT);
            Bandit newBandit = new Bandit(sT, x, y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
            newBandit.setID(i);
            newBandit.setNode(node);
            bandits.add(newBandit);
        }
    }
    
    public void moveBandit(int ID)
    {
        Bandit banditSprite = bandits.get(ID);
        
        if(banditSprite.stopped == false){
            int currentNode = banditSprite.getNode();
            Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);

            //if(intersection1.isOpen()){
                Random rand = new Random();
                int node = rand.nextInt(record.getIntersections(currentLevel).size());
                Intersection intersection2 = record.getIntersections(currentLevel).get(node);
                while(node==0 || node==1 || intersection2.blocked==true){
                    node = rand.nextInt(record.getIntersections(currentLevel).size());
                    intersection2 = record.getIntersections(currentLevel).get(node);
                }

                Boolean isAdjacent = intersection1.isAdjacent(intersection2);

                if(isAdjacent == true){
                    // GET THE TILE TWO LOCATION
                    int x1 = banditSprite.getStartX();
                    int y1 = banditSprite.getStartY();
                    int tile2x = intersection2.getX() + 50;
                    int tile2y = intersection2.getY() - 30;

                    int differenceX = tile2x - x1 - 60;
                    int differenceY = tile2y - y1 + 20;

                    int newX = x1+differenceX;
                    int newY = y1+differenceY;

                    if(intersection1.isOpen()){
                        // THEN MOVE PLAYER
                        banditSprite.setTarget(newX, newY);

                        // FIND SPEED LIMIT OF ROAD
                        Road road = findRoad(intersection1, intersection2);
                        int speedLimit = (road.getSpeedLimit())/10;

                        // SEND THEM TO THEIR DESTINATION
                        banditSprite.startMovingToTarget(speedLimit);
                        banditSprite.setNode(node);
                    } else{
                        if(banditSprite.mindlessTerror == true){
                            // THEN MOVE PLAYER
                            banditSprite.setTarget(newX, newY);

                            // FIND SPEED LIMIT OF ROAD
                            Road road = findRoad(intersection1, intersection2);
                            int speedLimit = (road.getSpeedLimit())/5;

                            // SEND THEM TO THEIR DESTINATION
                            banditSprite.startMovingToTarget(speedLimit);
                            banditSprite.setNode(node);
                        }
                    }
                } 
            //} 
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
        
        if(policeSprite.stopped == false){
            int currentNode = policeSprite.getNode();    
            Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);

           //if(intersection1.isOpen()){
                Random rand = new Random();
                int node = rand.nextInt(record.getIntersections(currentLevel).size());
                Intersection intersection2 = record.getIntersections(currentLevel).get(node);
                while(node==0 || node==1 || intersection2.blocked==true){
                    node = rand.nextInt(record.getIntersections(currentLevel).size());
                    intersection2 = record.getIntersections(currentLevel).get(node);
                }

                Boolean isAdjacent = intersection1.isAdjacent(intersection2);

                if(isAdjacent == true){
                    // GET THE TILE TWO LOCATION
                    int x1 = policeSprite.getStartX();
                    int y1 = policeSprite.getStartY();
                    int node2x = intersection2.getX() + 50;
                    int node2y = intersection2.getY() - 30;

                    int differenceX = node2x - x1 - 60;
                    int differenceY = node2y - y1 + 20;

                    int newX = x1+differenceX;
                    int newY = y1+differenceY;

                    if(intersection1.isOpen()){
                        // THEN MOVE POLICE CAR
                        policeSprite.setTarget(newX, newY);

                        // FIND SPEED LIMIT OF ROAD
                        Road road = findRoad(intersection1, intersection2);
                        int speedLimit = (road.getSpeedLimit())/10;

                        //if(slowed == true)
                        //    speedLimit = speedLimit / 2;
                        //if(speedup == true)
                        //    speedLimit = speedLimit * 2;

                        // SEND THEM TO THEIR DESTINATION
                        policeSprite.startMovingToTarget(speedLimit);
                        policeSprite.setNode(node);
                    } 
                }
            //}
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
            int x = intersection.getX();
            int y = intersection.getY();
            SpriteType sT = new SpriteType("Zombie" + i);
            addSpriteType(sT);
            Zombie newZombie = new Zombie(sT, x, y, 0, 0, pathXStates.INVISIBLE_STATE.toString());
            newZombie.setID(i);
            newZombie.setNode(node);
            //ArrayList<Intersection> path = generateZombiePath(newZombie);
            zombies.add(newZombie);
        }
    }
    
    public void moveZombie(int ID)
    {
        Zombie zombieSprite = zombies.get(ID);
        
        if(zombieSprite.stopped == false){
            int currentNode = zombieSprite.getNode();
            Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);

            if(intersection1.isOpen()){
                Random rand = new Random();
                int node = rand.nextInt(record.getIntersections(currentLevel).size());
                Intersection intersection2 = record.getIntersections(currentLevel).get(node);
                while(node==0 || node==1 || intersection2.blocked==true){
                    node = rand.nextInt(record.getIntersections(currentLevel).size());
                    intersection2 = record.getIntersections(currentLevel).get(node);
                }

                Boolean isAdjacent = intersection1.isAdjacent(intersection2);

                if(isAdjacent == true){
                    // GET THE TILE TWO LOCATION
                    int x1 = zombieSprite.getStartX();
                    int y1 = zombieSprite.getStartY();
                    int tile2x = intersection2.getX() + 50;
                    int tile2y = intersection2.getY() - 30;

                    int differenceX = tile2x - x1 - 60;
                    int differenceY = tile2y - y1 + 20;

                    int newX = x1+differenceX;
                    int newY = y1+differenceY;

                    // THEN MOVE PLAYER
                    zombieSprite.setTarget(newX, newY);

                    // FIND SPEED LIMIT OF ROAD
                    Road road = findRoad(intersection1, intersection2);
                    int speedLimit = (road.getSpeedLimit())/10;

                    //if(slowed == true)
                    //    speedLimit = speedLimit / 2;
                    //if(speedup == true)
                    //    speedLimit = speedLimit * 2;

                    // SEND THEM TO THEIR DESTINATION
                    zombieSprite.startMovingToTarget(speedLimit);
                    zombieSprite.setNode(node);
                } 
            }
        }
    } 
    
    public void controlZombie(Zombie zom, Intersection intersection2, int node){
        int currentNode = zom.getNode();
            Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);

             // GET THE TILE TWO LOCATION
                    int x1 = zom.getStartX();
                    int y1 = zom.getStartY();
                    int tile2x = intersection2.getX() + 50;
                    int tile2y = intersection2.getY() - 30;

                    int differenceX = tile2x - x1 - 60;
                    int differenceY = tile2y - y1 + 20;

                    int newX = x1+differenceX;
                    int newY = y1+differenceY;

                    // THEN MOVE PLAYER
                    zom.setTarget(newX, newY);

                    // SEND THEM TO THEIR DESTINATION
                    zom.startMovingToTarget(3);
                    zom.setNode(node);
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
                
                Intersection add = null;
                for(int i=0; i<intersections.size(); i++){
                    Intersection next = intersections.get(i);
                    if(next.isAdjacent(to)){
                        add = next;
                    }
                }
                if(add != null){
                    path.add(add);
                    startNode = add;
                } else{
                    Random rand = new Random();
                    int random = rand.nextInt(size);
                    Intersection randomIntersection = intersections.get(random);
                    
                    path.add(randomIntersection);
                    startNode = randomIntersection;
                }
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
        
        if(intersection2.blocked == false){
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
                int initSpeed = road.getSpeedLimit();
                int speedLimit = (int) (initSpeed - (zombieCollisions * (0.1*initSpeed)))/10;

                // if inc player speed special was applied, increase player speed
                // by 20% for each time player applied special
                if(playerSpeed > 0){
                    float inc = (int) (playerSpeed * 0.2);
                    float percentInc = 1 + inc;
                    speedLimit = (int) (speedLimit * percentInc);
                }

                if(zombieCollisions == 10)
                    speedLimit = 0;

                // SEND THEM TO THEIR DESTINATION
                player.startMovingToTarget(speedLimit);
                player.setCurrentNode(index2);
                player.setStartingPos(x1+tile2x, y1+tile2y);
            } else{
                ArrayList<Intersection> path = findPath(intersection1, intersection2);
                player.initPath(path);
            }
        }
    } 
    
    /**
     * Moves player
     */
    public void fly(int index2)
    {
        int currentNode = player.getCurrentNode();
        Intersection intersection1 = record.getIntersections(currentLevel).get(currentNode);
        Intersection intersection2 = record.getIntersections(currentLevel).get(index2);
        
        if(intersection2.blocked == false){
            Boolean isAdjacent = intersection1.isAdjacent(intersection2);
        
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

                // SEND PLAYEr TO DESTINATION
                player.startMovingToTarget(3);
                player.setCurrentNode(index2);
                player.setStartingPos(x1+tile2x, y1+tile2y);
        }
    } 
    
    public boolean reachedDestination(){
        int x = player.getStartX();
        int y = player.getStartY();
        
        Intersection destination = record.getIntersections(currentLevel).get(1);
        int screenPositionX1 = viewport.getViewportX();
        int screenPositionY1 = viewport.getViewportY();
        
        Point point = new Point(destination.getX() - screenPositionX1 + 170, destination.getY() - screenPositionY1);
        Point point2 = new Point(x, y);
        Rectangle bounds = new Rectangle(point, new Dimension(50, 50));
        if ((player.getCurrentNode()==1) && !(player.isMovingToTarget())) {
            return true;
        }
        
        return false;
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
        
        // mouse press in game screen
        if(((pathXGame)miniGame).isCurrentScreenState(GAME_SCREEN_STATE)){
            if(inProgress()){
                ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
                
                if(specialSelected == true){
                    applySpecial(x, y);
                } else{
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
                            if(policeSelected == true){
                                if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                                
                                policeSelected = false;
                                
                                Police policeSprite = police.get(selectedSprite);
                                int x1 = policeSprite.getStartX();
                                int y1 = policeSprite.getStartY();
                                node2x = intersection2.getX() + 50;
                                node2y = intersection2.getY() - 30;

                                int differenceX = node2x - x1 - 60;
                                int differenceY = node2y - y1 + 20;

                                int newX = x1+differenceX;
                                int newY = y1+differenceY;

                                // THEN MOVE POLICE CAR
                                policeSprite.setTarget(newX, newY);

                                // SEND THEM TO THEIR DESTINATION
                                policeSprite.startMovingToTarget(2);
                                policeSprite.setNode(i);
                                
                                policeSprite.stopped = true;
                                policeSprite.timer = System.currentTimeMillis() + 20000;
                                
                                money -= 30;
                            } else if(zombieSelected == true){
                                zombieSelected = false;
                                
                                if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                                
                                Zombie zombieSprite = zombies.get(selectedSprite);
                                int x1 = zombieSprite.getStartX();
                                int y1 = zombieSprite.getStartY();
                                node2x = intersection2.getX() + 50;
                                node2y = intersection2.getY() - 30;

                                int differenceX = node2x - x1 - 60;
                                int differenceY = node2y - y1 + 20;

                                int newX = x1+differenceX;
                                int newY = y1+differenceY;

                                // THEN MOVE POLICE CAR
                                zombieSprite.setTarget(newX, newY);

                                // SEND THEM TO THEIR DESTINATION
                                zombieSprite.startMovingToTarget(2);
                                zombieSprite.setNode(i);
                                
                                zombieSprite.stopped = true;
                                zombieSprite.timer = System.currentTimeMillis() + 20000;
                                
                                money -= 30;
                            } else if(banditSelected == true){
                                banditSelected = false;
                                
                                // play sound when player selects node to move bandit to
                                if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                                
                                Bandit banditSprite = bandits.get(selectedSprite);
                                int x1 = banditSprite.getStartX();
                                int y1 = banditSprite.getStartY();
                                node2x = intersection2.getX() + 50;
                                node2y = intersection2.getY() - 30;

                                int differenceX = node2x - x1 - 60;
                                int differenceY = node2y - y1 + 20;

                                int newX = x1+differenceX;
                                int newY = y1+differenceY;

                                // THEN MOVE POLICE CAR
                                banditSprite.setTarget(newX, newY);

                                // SEND THEM TO THEIR DESTINATION
                                banditSprite.startMovingToTarget(2);
                                banditSprite.setNode(i);
                                
                                banditSprite.stopped = true;
                                banditSprite.timer = System.currentTimeMillis() + 20000;
                                
                                money -= 30;
                            } else{
                                selectedNode = intersection2;
                                selectedNodeIndex = i;

                                if(i != player.getCurrentNode())
                                    movePlayer(i);
                            }
                        }
                    }
                }
            }
        }
        
        else if(((pathXGame)miniGame).isCurrentScreenState(LEVEL_SCREEN_STATE)){
            ArrayList<String> levels = props.getPropertyOptionsList(PathX.pathXPropertyType.LEVEL_OPTIONS);
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
                    if(!record.isLocked(levelName))
                        ((pathXGame)miniGame).pressedLevelButton(levelName);
                }
            }
        } 
    }
    
    public void applySpecial(int x, int y){
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        
        if(special.equals(MAKE_RED_LIGHT_BUTTON_TYPE) || special.equals(MAKE_GREEN_LIGHT_BUTTON_TYPE)){
            for(int i=0; i<intersections.size(); i++){
                Intersection intersection2 = intersections.get(i);

                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                // position of intersection on node
                int node2x = intersection2.getX() - screenPositionX1 + 170;
                int node2y = intersection2.getY() - screenPositionY1;

                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(30, 30));

                if (bounds.contains(point2)) {
                    if(special.equals(MAKE_RED_LIGHT_BUTTON_TYPE)){
                        intersection2.setOpen(false);
                        intersection2.time = System.currentTimeMillis() + 10000;
                    } else
                        intersection2.setOpen(true);
                    specialSelected = false;
                    money -= 5;
                    
                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                        miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                }
            }
        } else if(special.equals(FLAT_TIRE_BUTTON_TYPE) || special.equals(EMPTY_GAS_BUTTON_TYPE)){
            for(int i=0; i<police.size(); i++){
                Police policeSprite = police.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (policeSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (policeSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){
                    if(special.equals(FLAT_TIRE_BUTTON_TYPE) && policeSprite.stopped==false){
                        policeSprite.stopped = true;
                        policeSprite.timer = System.currentTimeMillis() + 10000;
                        money -= 20;

                        if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    } else if(special.equals(EMPTY_GAS_BUTTON_TYPE) && policeSprite.noGas==false){
                        policeSprite.noGas = true;
                        policeSprite.timer = System.currentTimeMillis() + 20000;
                        money -= 20;

                        if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    }
                    
                    specialSelected = false;
                }
            }
            
            for(int i=0; i<bandits.size(); i++){
                Bandit banditSprite = bandits.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (banditSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (banditSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){
                   if(special.equals(FLAT_TIRE_BUTTON_TYPE) && banditSprite.stopped==false){
                        banditSprite.stopped = true;
                        banditSprite.timer = System.currentTimeMillis() + 10000;
                        money -= 20;

                        if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    } else if(special.equals(EMPTY_GAS_BUTTON_TYPE) && banditSprite.noGas==false){
                        banditSprite.noGas = true;
                        banditSprite.timer = System.currentTimeMillis() + 20000;
                        money -= 20;

                        if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    }
                    
                    specialSelected = false;
                }
            }
            
            for(int i=0; i<zombies.size(); i++){
                Zombie zombieSprite = zombies.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (zombieSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (zombieSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){
                    if(special.equals(FLAT_TIRE_BUTTON_TYPE) && zombieSprite.stopped==false){
                        zombieSprite.stopped = true;
                        zombieSprite.timer = System.currentTimeMillis() + 10000;
                        money -= 20;

                        if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    } else if(special.equals(EMPTY_GAS_BUTTON_TYPE) && zombieSprite.noGas==false){
                        zombieSprite.noGas = true;
                        zombieSprite.timer = System.currentTimeMillis() + 20000;
                        money -= 20;

                        if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    }
                    
                    specialSelected = false;
                }
            }
        } else if(special.equals(FLYING_BUTTON_TYPE)){
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
                    
                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    
                    if(i != player.getCurrentNode())
                        fly(i);
                }
            }
        } else if(special.equals(MIND_CONTROL_BUTTON_TYPE)){
            for(int i=0; i<police.size(); i++){
                Police policeSprite = police.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (policeSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (policeSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){ //if player chooses to control police
                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    
                    specialSelected = false;
                    
                    policeSelected = true;
                    selectedSprite = i;
                }
            }
            
            for(int i=0; i<bandits.size(); i++){
                Bandit banditSprite = bandits.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (banditSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (banditSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){
                   if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    
                   specialSelected = false;
                   
                   banditSelected = true;
                   selectedSprite = i;
                }
            }
            
            for(int i=0; i<zombies.size(); i++){
                Zombie zombieSprite = zombies.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (zombieSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (zombieSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){
                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    
                    specialSelected = false;
                    
                    zombieSelected = true;
                    selectedSprite = i;
                }
            }
        } else if(special.equals(DEC_SPEED_BUTTON_TYPE) || special.equals(INC_SPEED_BUTTON_TYPE)){
            specialSelected = false;
        } else if(special.equals(CLOSE_ROAD_BUTTON_TYPE)){
            specialSelected = false;
        } else if(special.equals(CLOSE_INTERSECTION_BUTTON_TYPE) || special.equals(OPEN_INTERSECTION_BUTTON_TYPE)){
            for(int i=0; i<intersections.size(); i++){
                Intersection intersection2 = intersections.get(i);

                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                // position of intersection
                int node2x = intersection2.getX() - screenPositionX1 + 170;
                int node2y = intersection2.getY() - screenPositionY1;

                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(30, 30));

                if (bounds.contains(point2)) { //check if pressed on this intersection
                    if(special.equals(CLOSE_INTERSECTION_BUTTON_TYPE)){
                        intersection2.setBlocked(true);
                        
                        // CLOSE ALL ROADS LEADING TO INTERSECTION
                        
                        //get roads
                        ArrayList<Road> roads = record.getRoads(currentLevel);
                        
                        // loop through all roads & check if its starting/ending intersection
                        // is the closed intersection
                        for (int r = 0; r < roads.size(); r++){
                            Road road = roads.get(r);
                            
                            //check starting node
                            if((road.getNode1()).equals(intersection2))
                                road.setClosed(true);
                            
                            if((road.getNode2()).equals(intersection2))
                                road.setClosed(true);
                        }
                    } else{
                        intersection2.setBlocked(false);
                        
                        ArrayList<Road> roads = record.getRoads(currentLevel);
                        
                        // loop through all roads & check if its starting/ending intersection
                        // is the open intersection
                        for (int r = 0; r < roads.size(); r++){
                            Road road = roads.get(r);
                            
                            //check starting node
                            if((road.getNode1()).equals(intersection2))
                                road.setClosed(false);
                            
                            if((road.getNode2()).equals(intersection2))
                                road.setClosed(false);
                        }
                    }
                        
                    specialSelected = false;
                    money -= 25;
                    
                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                        miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                }
            }
        } else if(special.equals(MINDLESS_TERROR_BUTTON_TYPE)){
            for(int i=0; i<police.size(); i++){
                Police policeSprite = police.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (policeSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (policeSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){ //if player chooses to control police
                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    
                    specialSelected = false;
                    
                    policeSprite.mindlessTerror = true;
                }
            }
            
            for(int i=0; i<bandits.size(); i++){
                Bandit banditSprite = bandits.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (banditSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (banditSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){
                   if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    
                   specialSelected = false;
                   
                   banditSprite.mindlessTerror = true;
                }
            }
            
            for(int i=0; i<zombies.size(); i++){
                Zombie zombieSprite = zombies.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();

                int node2x = (int) (zombieSprite.getX() - screenPositionX1 + 170);
                int node2y = (int) (zombieSprite.getY() - screenPositionY1);
                
                Point point = new Point(node2x, node2y);
                Point point2 = new Point(x, y);
                Rectangle bounds = new Rectangle(point, new Dimension(50, 25));
                if(bounds.contains(point2)){
                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                            miniGame.getAudio().play(pathXPropertyType.AUDIO_SPECIALS.toString(), false);
                    
                    money -= 30;
                    
                    specialSelected = false;
                    
                    zombieSprite.mindlessTerror = true;
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
        
        ((pathXGame)miniGame).openDialog();
        
        record.completedLevel(currentLevel);
        
        playerSpeed = 0;
        specialSelected = false;
        player.setTagible(false);
        steal = false;
        
        if(!((pathXGame)miniGame).isMusicDisabled())
            miniGame.getAudio().play(pathXPropertyType.AUDIO_WIN.toString(), false);
    }
    
    /**
     * Updates the player record, adding a game without a win.
     */
    public void endGameAsLoss(){
        super.endGameAsLoss();
        
        ((pathXGame)miniGame).openDialog();
        pause();
        
        int newBalance = (int) (record.getBalance() * 0.1);
        
        record.incBalance(0 - newBalance);
        
        playerSpeed = 0;
        specialSelected = false;
        
        if(((pathXGame)miniGame).isSoundDisabled() == false){
            miniGame.getAudio().stop(pathXPropertyType.AUDIO_GAME.toString());
            miniGame.getAudio().play(pathXPropertyType.AUDIO_LOSS.toString(), false);
        }
    }
    
     /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
    @Override
    public void reset(MiniGame game){
        // CLEAR ANY WIN OR LOSS DISPLAY
        miniGame.getGUIDialogs().get(LEVEL_DIALOG_TYPE).setState(pathXStates.INVISIBLE_STATE.toString());
        
        setCurrentLevel(currentLevel);
        
        loadBandits();
        loadPolice();
        loadZombies();
        
        zombieCollisions = 0;
        
        playerSpeed = 0;
        specialSelected = false;
        
        policeSelected = false;
        banditSelected = false;
        zombieSelected = false;
        
        ArrayList<Road> roads = record.getRoads(currentLevel);
        for (int r = 0; r < roads.size(); r++){
            roads.get(r).setClosed(false);
        }
        
        ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
        for(int i=0; i<intersections.size(); i++)
            intersections.get(i).setBlocked(false);
    }
    
    public void updateBots(MiniGame game){
        for(int i=0; i<police.size(); i++){
                Police policeSprite = police.get(i);
                
                if(policeSprite.noGas == false && policeSprite.destroyed==false)
                    policeSprite.update(miniGame);
                
                if(System.currentTimeMillis() >= policeSprite.timer){
                    policeSprite.noGas = false;
                    policeSprite.stopped = false;
                }
                
                if(!player.isIntangible()){
                    // check if player and police overlap
                    Point playerPoint = new Point((int)player.getX(), (int)player.getY());
                    Rectangle playa = new Rectangle(playerPoint, new Dimension(50, 25));
                    Point policePoint = new Point((int)policeSprite.getX()-50, (int)policeSprite.getY()+20);
                    Rectangle policeCar = new Rectangle(policePoint, new Dimension(50, 25));
                    if(playa.intersects(policeCar)){
                        if(player.invinsible == true && policeSprite.destroyed==false){
                            policeSprite.destroyed = true;
                            
                            if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_CRASH.toString(), false);
                        } else if(player.invinsible != true){
                            if(steal==true && policeSprite.steal==false){
                                money += 10;
                                policeSprite.steal = true;
                            } else if(steal == false){
                                endGameAsLoss();
                            }
                        }
                    } else{
                        policeSprite.steal = false;
                    }
                }
            }
            
            for(int i=0; i<bandits.size(); i++){
                Bandit banditSprite = bandits.get(i);
                
                if(banditSprite.noGas == false && banditSprite.destroyed==false)
                    banditSprite.update(miniGame);
                
                if(System.currentTimeMillis() >= banditSprite.timer){
                    banditSprite.noGas = false;
                    banditSprite.stopped = false;
                }
                
                if(!player.isIntangible()){
                    // check if player and bandit overlap
                    Point playerPoint = new Point((int)player.getX(), (int)player.getY());
                    Rectangle playa = new Rectangle(playerPoint, new Dimension(50, 25));
                    Point banditPoint = new Point((int)banditSprite.getX()-50, (int)banditSprite.getY()+20);
                    Rectangle banditCar = new Rectangle(banditPoint, new Dimension(50, 25));
                    if(playa.intersects(banditCar)){
                        if(player.invinsible == true && banditSprite.destroyed==false){
                            banditSprite.destroyed = true;
                            
                            if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_CRASH.toString(), false);
                        } else if(player.invinsible != true){
                            if(steal == false){
                                if(banditSprite.robbed() == false){
                                    int stolen = (int) (money * 0.1);
                                    int banditMoney = banditSprite.getMoneyStolen() + stolen;
                                    banditSprite.setMoneyStolen(banditMoney);
                                    decMoney();
                                    banditSprite.setRobbed(true);

                                    if(((pathXGame)miniGame).isSoundDisabled() == false)
                                        miniGame.getAudio().play(pathXPropertyType.AUDIO_BANDIT.toString(), false);
                                }
                            } else{
                                int banditMoney = banditSprite.getMoneyStolen();
                                money += banditMoney;
                                banditSprite.setMoneyStolen(0);

                                if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_BANDIT.toString(), false);
                            }
                        }
                    } else{
                        banditSprite.setRobbed(false);
                    }
                }
            }
            
            for(int i=0; i<zombies.size(); i++){
                Zombie zombieSprite = zombies.get(i);
                
                if(zombieSprite.noGas == false && zombieSprite.destroyed==false && zombieSprite.controlled==false)
                    zombieSprite.update(miniGame);
                
                if(System.currentTimeMillis() >= zombieSprite.timer){
                    zombieSprite.noGas = false;
                    zombieSprite.stopped = false;
                }
                
                if(!player.isIntangible()){
                    // check if player and bandit overlap
                    Point playerPoint = new Point((int)player.getX(), (int)player.getY());
                    Rectangle playa = new Rectangle(playerPoint, new Dimension(50, 25));
                    Point zombiePoint = new Point((int)zombieSprite.getX()-50, (int)zombieSprite.getY()+20);
                    Rectangle banditCar = new Rectangle(zombiePoint, new Dimension(50, 25));
                    if(playa.intersects(banditCar)){
                        if(player.invinsible == true && zombieSprite.destroyed==false){
                            zombieSprite.destroyed = true;
                            
                            if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_CRASH.toString(), false);
                        } else if(player.invinsible != true){
                            if(zombieSprite.slowed() == false){
                                zombieCollisions += 1;
                                zombieSprite.setSlowed(true);

                                if(steal==true && zombieSprite.steal==false){
                                    money += 10;
                                    zombieSprite.steal = true;
                                }

                                if(((pathXGame)miniGame).isSoundDisabled() == false)
                                    miniGame.getAudio().play(pathXPropertyType.AUDIO_ZOMBIE.toString(), false);
                            }
                        }
                    } else{
                        zombieSprite.setSlowed(false);
                        zombieSprite.steal = false;
                    }
                }
            }
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
            
            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            player.update(miniGame);
            
            if(System.currentTimeMillis() >= player.timer){
                player.setTagible(false);
                player.invinsible = false;
            }
            
            if(System.currentTimeMillis() >= player.stealTimer)
                steal = false;
            
            if(frozen == false)
                updateBots(game);
            
            ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
            for(int i=0; i<intersections.size(); i++){
                if(System.currentTimeMillis() >= intersections.get(i).time){
                    intersections.get(i).open = true;
                }
            }
            
            // IF PLAYER REACHES DESTINATION
            if(reachedDestination() == true){
                if(!won())
                    endGameAsWin();
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
