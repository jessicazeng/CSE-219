package pathx.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.data.pathXDataModel;
import properties_manager.PropertiesManager;
import static pathx.pathXConstants.*;
import pathx.PathX.pathXPropertyType;
import pathx.data.Bandit;
import pathx.data.Intersection;
import pathx.data.Police;
import pathx.data.Record;
import pathx.data.Road;

/**
 * This class handles all the rendering for the pathX game. 
 * 
 * @author Jessica Zeng
 */
public class pathXPanel extends JPanel {
    // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private pathXDataModel data;
    
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
    
    /**
     * This constructor stores the game and data references,
     * which we'll need for rendering.
     * 
     * @param initGame The Sorting Hat game that is using
     * this panel for rendering.
     * 
     * @param initData The Sorting Hat game data.
     */
    public pathXPanel(MiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
    }
    
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g){
        try 
        {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
        
            // CLEAR THE PANEL
            super.paintComponent(g);
        
            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
            
            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);
            
            // ONLY RENDER THIS STUFF IF WE'RE ACTUALLY IN-GAME
            if (!data.notStarted())
            {
                renderDialogs(g);
            }
            
            renderStats(g);
            
            // AND FINALLY, TEXT FOR DEBUGGING
            renderDebuggingText(g);
        }
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();    
        }
    }
    
    /**`
     * Renders the background image, which is different depending on the screen. 
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g)
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        Record record = ((pathXGame)game).getPlayerRecord();
        
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        //renderSprite(g, bg);
        
        if(((pathXGame)game).isCurrentScreenState(LEVEL_SCREEN_STATE)){
            renderSprite(g, bg);
            
            Viewport viewport = data.getViewport();
            viewport.updateViewportBoundaries();
            int x1 = viewport.getViewportX();
            int y1 = viewport.getViewportY();
            int x2 = x1 + viewport.getViewportWidth();
            int y2 = y1 + viewport.getViewportHeight();
            SpriteType bgST = bg.getSpriteType();
            String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
            Image img = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MAP_BACKGROUND));
            g.drawImage(img, 10, 90, 620, 440, x1, y1, x2, y2, null);
            g.setColor(Color.black);
            g.drawRect(10, 90, 610, 350);
            
            Record rec = ((pathXGame)game).getPlayerRecord();
            ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
            for (int i = 0; i < levels.size(); i++){
                String levelName = levels.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();
                
                int x = rec.getLevelPositionX(levelName) - screenPositionX1;
                int y = rec.getLevelPositionY(levelName) - screenPositionY1;
                
                Boolean locked = rec.isLocked(levelName);
                Boolean levelCompleted = rec.isLevelCompleted(levelName);
                
                if((x>10 && x<600) && (y<420 && y>90)){
                    // if use has not yet unlocked the level
                    if(locked == true){
                        g.setColor(Color.white);
                        g.fillOval(x, y, 17, 17);
                        g.setColor(Color.black);
                        g.drawOval(x, y, 17, 17);
                    } else{ // level has been unlocked
                        if(levelCompleted == true){ // if user successfully robbed this location
                            g.setColor(Color.green);
                            g.fillOval(x, y, 17, 17);
                        } else{
                            g.setColor(Color.red);
                            g.fillOval(x, y, 17, 17);
                        }
                    
                        g.setColor(Color.black);
                        g.drawOval(x, y, 17, 17);
                    }
                }
                
            }
            
            for (int i = 0; i < levels.size(); i++){
                final String levelName = levels.get(i);
                boolean locked = rec.isLocked(levelName);
                
                if(locked == false){
                    int screenPositionX1 = viewport.getViewportX();
                    int screenPositionY1 = viewport.getViewportY();
                    
                    int x = rec.getLevelPositionX(levelName) - screenPositionX1;
                    int y = rec.getLevelPositionY(levelName) - screenPositionY1;
                
                    final Point point = new Point(x, y);
                    addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Point me = e.getPoint();
                        Rectangle bounds = new Rectangle(point, new Dimension(17, 17));
                    
                        if (bounds.contains(me)) {
                            ((pathXGame)game).pressedLevelButton(levelName);
                        }
                    }
                     });
                }
                
            }
        } else if(((pathXGame)game).isCurrentScreenState(GAME_SCREEN_STATE)){
            renderSprite(g, bg);
            
            Viewport viewport = data.getViewport();
            viewport.setGameWorldSize(LEVEL_GAMEWORLD_WIDTH, LEVEL_GAMEWORLD_HEIGHT);
            int x1 = viewport.getViewportX();
            int y1 = viewport.getViewportY();
            int x2 = x1 + GAME_VIEWPORT_WIDTH;
            int y2 = y1 + GAME_VIEWPORT_HEIGHT;
            SpriteType bgST = bg.getSpriteType();
            String currentLevel = data.getCurrentLevel();
            String levelImage = record.getLevelImage(currentLevel);
            
            String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
            Image img = game.loadImage(imgPath + levelImage);
            g.drawImage(img, 155, 20, 620, 440, x1, y1, x2, y2, null);
            g.setColor(Color.black);
            g.drawRect(155, 20, GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT);
            
            // draw roads
            Graphics2D g2 = (Graphics2D) g;
            
            ArrayList<Road> roads = record.getRoads(currentLevel);
            for (int i = 0; i < roads.size(); i++){
                Road road = roads.get(i);
                Intersection intersection1 = road.getNode1();
                Intersection intersection2 = road.getNode2();
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();
                int screenPositionX2 = viewport.getViewportWidth() + screenPositionX1;
                int screenPositionY2 = viewport.getViewportHeight() + screenPositionY1;
                
                // get coordinates for the nodes
                int node1x = intersection1.getX() - screenPositionX1 + 170 + 15;
                int node1y = intersection1.getY() - screenPositionY1 + 15;
                int node2x = intersection2.getX() - screenPositionX1 + 170 + 15;
                int node2y = intersection2.getY() - screenPositionY1 + 15;
                
                int speed = road.getSpeedLimit();
                g2.setColor(Color.black);
                g2.setStroke(new BasicStroke(speed/5));
                
                g2.drawLine(node1x, node1y, node2x, node2y);
                
            }
            
            // draw intersections for the level
            ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < intersections.size(); i++){
                Intersection intersection = intersections.get(i);
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();
                int screenPositionX2 = viewport.getViewportWidth() + screenPositionX1;
                int screenPositionY2 = viewport.getViewportHeight() + screenPositionY1;
                
                int x = intersection.getX() - screenPositionX1 + 170;
                int y = intersection.getY() - screenPositionY1;
                
                Boolean open = intersection.isOpen();
                
                if(i == 0){
                        levelImage = record.getStartImage(currentLevel);
            
                        imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
                        img = game.loadImageWithColorKey(imgPath+levelImage, COLOR_KEY);
                        int imageHeight = img.getHeight(null);
                        int newX = x;
                        int newY = y - (imageHeight/2);
                        
                        g.drawImage(img, newX, newY, null);
                } else if(i == 1){
                        levelImage = record.getEndImage(currentLevel);
            
                        imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
                        img = game.loadImageWithColorKey(imgPath+levelImage, COLOR_KEY);
                        int imageHeight = img.getHeight(null);
                        int newX = x;
                        int newY = y - (imageHeight/2);
                        
                        g.drawImage(img, newX, newY, null);
                }else{
                    if((x>155 && x<600) && (y<(screenPositionY2+5) && y>20)){
                        if(open == true){
                            g.setColor(Color.green);
                            g.fillOval(x, y, 30, 30);
                        } else{ // level has been unlocked
                            g.setColor(Color.red);
                            g.fillOval(x, y, 30, 30);
                        }
                    
                        g.setColor(Color.black);
                        g.drawOval(x, y, 30, 30);
                }
                }
            }
            Intersection intersection = intersections.get(0);
            int screenPositionX1 = viewport.getViewportX();
            int screenPositionY1 = viewport.getViewportY();
            int x = intersection.getX() - screenPositionX1 + 170;
            int y = intersection.getY() - screenPositionY1;
            
            imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
            img = game.loadImageWithColorKey(imgPath+props.getProperty(pathXPropertyType.IMAGE_GETAWAY_CAR), COLOR_KEY);
            int imageHeight = img.getHeight(null);
            int imageWidth = img.getWidth(null);
            int newX = x + imageWidth;
            int newY = y - imageHeight;
                        
            g.drawImage(img, newX, newY, null);
            
            ArrayList<Bandit> bandits = data.getBandits();
            for(int n=0; n<bandits.size(); n++){
                int node = bandits.get(n).getNode();
                intersection = intersections.get(node);
                x = intersection.getX() - viewport.getViewportX() + 170;
                y = intersection.getY() - viewport.getViewportY();
                
                imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
                img = game.loadImageWithColorKey(imgPath+props.getProperty(pathXPropertyType.IMAGE_BANDIT), COLOR_KEY);
                imageHeight = img.getHeight(null);
                imageWidth = img.getWidth(null);
                
                g.drawImage(img, x, y, null);
            }
             
            ArrayList<Police> police = data.getPolice();
            for(int n=0; n<police.size(); n++){
                int node = police.get(n).getNode();
                intersection = intersections.get(node);
                x = intersection.getX() - viewport.getViewportX() + 170;
                y = intersection.getY() - viewport.getViewportY();
                
                imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
                img = game.loadImageWithColorKey(imgPath+props.getProperty(pathXPropertyType.IMAGE_POLICE), COLOR_KEY);
                imageHeight = img.getHeight(null);
                imageWidth = img.getWidth(null);
                
                g.drawImage(img, x, y, null);
            }
            
            renderSprite(g, bg);
        } else{
            renderSprite(g, bg);
        }
    }
    
    
    
     /**
     * Renders the game dialog boxes.
     * 
     * @param g This panel's graphics context.
     */
    public void renderDialogs(Graphics g){
        Record record = ((pathXGame)game).getPlayerRecord();
        
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites)
        {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
            
            if(((pathXGame)game).isCurrentScreenState(GAME_SCREEN_STATE) && data.isPaused()){
                // display stats
                String currentLevel = data.getCurrentLevel();
                String levelDescription = "Rob the " + record.getLevelName(currentLevel);
                String levelDescription2 = "and make your getaway to";
                int money = record.getMoney(currentLevel);
                String levelDescription3 = "to earn $" + money + ".";
                g.setFont(FONT_GAME_STATS);
                g.drawString(currentLevel, GAME_STATS_X, GAME_STATS_Y);
                g.drawString(levelDescription, GAME_STATS_X, GAME_STATS_Y+50);
                g.drawString(levelDescription2, GAME_STATS_X, GAME_STATS_Y+80);
                g.drawString(levelDescription3, GAME_STATS_X, GAME_STATS_Y+110);
                
                final Point point = new Point(273, 346);
                addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point me = e.getPoint();
                    Rectangle bounds = new Rectangle(point, new Dimension(120, 145));
                    
                    if (bounds.contains(me)) {
                        ((pathXGame)game).closeDialog();
                        data.unpause();
                    }
                }
                 });
            }
        }
    }
    
    /**
     * This method renders the on-screen stats that change as
     * the game progresses. This means things like the game time
     * and the number of tiles remaining.
     * 
     * @param g the Graphics context for this panel
     */
    public void renderStats(Graphics g){
        if (((pathXGame)game).isCurrentScreenState(LEVEL_SCREEN_STATE)){
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(Color.BLACK);
            g.drawString(BALANCE, 200, 35);
            
            g.drawString(GOAL, 200, 70);
        }
        if(((pathXGame)game).isCurrentScreenState(GAME_SCREEN_STATE) && !data.isPaused()){
            Record record = ((pathXGame)game).getPlayerRecord();
            String currentLevel = data.getCurrentLevel();
            String levelName = record.getCity(currentLevel);
            g.setFont(FONT_GAME_STATS);
            g.drawString(levelName, 165, 50);
            
            String money = "$" + record.getMoney(currentLevel);
            g.drawString(money, 165, 80);
        }
    }
    
    /**   
     * Renders all the GUI decor and buttons.
     * 
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE)
                renderSprite(g, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            renderSprite(g, s);
        }
    }
    
    /**
     * Renders the s Sprite into the Graphics context g. Note
     * that each Sprite knows its own x,y coordinate location.
     * 
     * @param g the Graphics context of this panel
     * 
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(pathXStates.INVISIBLE_STATE.toString()))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
        }
    }
    
    /**
     * Renders the debugging text to the panel. Note
     * that the rendering will only actually be done
     * if data has activated debug text rendering.
     * 
     * @param g the Graphics context for this panel
     */
    public void renderDebuggingText(Graphics g)
    {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive())
        {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);
            
            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext())
            {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }   
        } 
    }
}
