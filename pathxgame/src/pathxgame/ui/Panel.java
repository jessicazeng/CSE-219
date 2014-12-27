/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathxgame.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathxgame.Pathx.pathXPropertyType;
import pathxgame.data.DataModel;
import pathxgame.data.Intersection;
import pathxgame.data.Player;
import pathxgame.data.Police;
import pathxgame.data.Record;
import pathxgame.data.Road;
import static pathxgame.pathXConstants.*;
import properties_manager.PropertiesManager;

/**
 *
 * @author Jessica
 */
public class Panel extends JPanel {
    // Need this because it has GUI that we need to render
    private MiniGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private DataModel data;
    
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
    public Panel(MiniGame initGame, DataModel initData)
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
            
            renderDialogs(g);
            
            // AND FINALLY, TEXT FOR DEBUGGING
            renderDebuggingText(g);
        }
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();    
        }
    }

    private void renderBackground(Graphics g) {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        
        Viewport viewport = data.getViewport();
        viewport.updateViewportBoundaries();
        int x1 = viewport.getViewportX();
        int y1 = viewport.getViewportY();
        int x2 = x1 + viewport.getViewportWidth();
        int y2 = y1 + viewport.getViewportHeight();
        
        if(((Game)game).isCurrentScreenState(LEVEL_SCREEN_STATE)){ // in level selection screen
            renderSprite(g, bg);
            
            // draw map according to scroll position
            Image img = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MAP));
            // adjust y1 and y2 to adjust map image to the panel
            g.drawImage(img, 0, 0, viewport.getViewportWidth(), viewport.getViewportHeight(), x1, y1, x2, y2, null);
            
            // draw levels on map
            Record rec = ((Game)game).getPlayerRecord();
            ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
            for (int i = 0; i < levels.size(); i++){
                final String levelName = levels.get(i);
                
                int x = rec.getLevelPositionX(levelName);
                int y = rec.getLevelPositionY(levelName);
                
                Boolean locked = rec.isLocked(levelName);
                Boolean levelCompleted = rec.isLevelCompleted(levelName);
                
                if((x>x1 && x<x2) && (y<y2 && y>y1)){
                    x -= x1;
                    y -= y1;
                            
                    // if use has not yet unlocked the level
                    if(locked == true){
                        g.setColor(Color.white);
                        g.fillOval(x, y, 20, 20);
                        g.setColor(Color.black);
                        g.drawOval(x, y, 20, 20);
                    } else{ // level has been unlocked
                        if(levelCompleted == true){ // if user successfully robbed this location
                            g.setColor(COLOR_GREEN);
                            g.fillOval(x, y, 20, 20);
                        } else{
                            g.setColor(COLOR_RED);
                            g.fillOval(x, y, 20, 20);
                        }
                    
                        g.setColor(Color.BLACK);
                        g.drawOval(x, y, 20, 20);
                    }
                }
            }
            
            // draw panel on top of level selection screen
            img = game.loadImageWithColorKey(imgPath + props.getProperty(pathXPropertyType.IMAGE_LEVELSELECTION_PANE), COLOR_KEY);
            g.drawImage(img, 0, 0, LEVELSELECTION_PANE_WIDTH, LEVELSELECTION_PANE_HEIGHT, 0, 0, LEVELSELECTION_PANE_WIDTH, LEVELSELECTION_PANE_HEIGHT, null);
        } else if(((Game)game).isCurrentScreenState(GAME_SCREEN_STATE)) { // in game screen
            renderSprite(g, bg);
            
            Record record = ((Game)game).getPlayerRecord();
            
            String currentLevel = data.getCurrentLevel();
            String levelImage = record.getLevelImage(currentLevel);
            
            Image img = game.loadImage(imgPath + levelImage);
            g.drawImage(img, 0, 0, viewport.getViewportWidth(), viewport.getViewportHeight(), x1, y1, x2, y2, null);
            
            Graphics2D g2 = (Graphics2D) g;
            
            // draw roads
            ArrayList<Road> roads = record.getRoads(currentLevel);
            for (int i = 0; i < roads.size(); i++){
                Road road = roads.get(i);
                Intersection intersection1 = road.getNode1();
                Intersection intersection2 = road.getNode2();
                
                // get coordinates for the nodes
                int node1x = intersection1.getX() - x1;
                int node1y = intersection1.getY() - y1;
                int node2x = intersection2.getX() - x1;
                int node2y = intersection2.getY() - y1;
                
                int speed = road.getSpeedLimit();
                
                if(road.isClosed() == true)
                    g2.setColor(Color.red);
                else
                    g2.setColor(Color.LIGHT_GRAY);
                
                g2.setStroke(new BasicStroke(speed/5));
                
                g2.drawLine(node1x, node1y, node2x, node2y);
                
//                boolean isOneWay = road.isOneWay();
//                if(isOneWay == true){
//                    // CALCULATE THE ROAD LINE SLOPE
//                    double diffX = node2x - node1x;
//                    double diffY = node2y - node1y;
//                    double slope = diffY/diffX;
//        
//                    // AND THEN FIND THE LINE MIDPOINT
//                    double midX = (node1x + node2x)/2.0;
//                    double midY = (node1y + node2y)/2.0;
//
//                    // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
//                    // AT THE END
//                    AffineTransform oldAt = g2.getTransform();
//
//                    // CALCULATE THE ROTATION ANGLE
//                    double theta = Math.atan(slope);
//                    if (node2x < node1x)
//                    theta = (theta + Math.PI);
//        
//                    AffineTransform at = new AffineTransform();        
//                    at.setToIdentity();
//                    at.translate(midX, midY);
//                    at.rotate(theta);
//                    g2.setTransform(at);
//        
//                    // AND RENDER AS A SOLID TRIANGLE
//                    g2.fill(recyclableTriangle);
//
//                    // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
//                    g2.setTransform(oldAt);
//                }
            }
            
            g2.setStroke(new BasicStroke(1));
            
            // draw intersections for the level
            ArrayList<Intersection> intersections = record.getIntersections(currentLevel);
            for (int i = 0; i < intersections.size(); i++){
                Intersection intersection = intersections.get(i);
                
                int x = intersection.getX() - x1;
                int y = intersection.getY() - y1;
                
                Boolean open = intersection.isOpen();
                
                if(i == 0){
                        levelImage = record.getStartImage(currentLevel);
            
                        imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
                        img = game.loadImage(imgPath+levelImage);
                        int imageWidth = img.getWidth(null);
                        int imageHeight = img.getHeight(null);
                        int newX = x - (imageWidth/2);
                        int newY = y - (imageHeight/2);
                        
                        g.drawImage(img, newX, newY, null);
                } else if(i == 1){
                        levelImage = record.getEndImage(currentLevel);
            
                        imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
                        img = game.loadImage(imgPath+levelImage);
                        int imageWidth = img.getWidth(null);
                        int imageHeight = img.getHeight(null);
                        int newX = x - (imageWidth/2);
                        int newY = y - (imageHeight/2);
                        
                        g.drawImage(img, newX, newY, null);
                }else{
                    if(intersection.isBlocked() == true){ // intersection is blocked
                        g.setColor(Color.DARK_GRAY);
                    } else{
                        if(open == true) // intersection is open
                            g.setColor(COLOR_GREEN);
                        else // intersection is closed
                            g.setColor(COLOR_RED);
                    }
                    int newX = x - 12;
                    int newY = y - 12;
                    g.fillOval(newX, newY, INTERSECTION_WIDTH, INTERSECTION_WIDTH);
                    
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawOval(newX, newY, INTERSECTION_WIDTH, INTERSECTION_WIDTH);
                    g.setColor(Color.BLACK);
                    g.drawString(""+intersection.getID(), newX, newY);
                }
                
                // draw player
                Player player = data.getplayer();
                img = game.loadImage(imgPath+props.getProperty(pathXPropertyType.IMAGE_PLAYER));
                x = (int)(player.getX()) - x1 - SPRITE_MARGIN;
                y = (int)(player.getY()) - y1 - SPRITE_MARGIN;

                g.drawImage(img, x, y, null);
                
                // draw police
                ArrayList<Police> police = data.getPolice();
                for(int n=0; n<police.size(); n++){
                    Police policeSprite = police.get(n);
                    x = (int)(policeSprite.getX()) - x1 - SPRITE_MARGIN;
                    y = (int)(policeSprite.getY()) - y1 - SPRITE_MARGIN;

                    imgPath = props.getProperty(pathXPropertyType.PATH_IMG);  
                    img = game.loadImage(imgPath+props.getProperty(pathXPropertyType.IMAGE_POLICE));
                    
                    g.drawImage(img, x, y, null);
                }
            }
            
            // draw panel on left side of level selection screen
            img = game.loadImageWithColorKey(imgPath + props.getProperty(pathXPropertyType.IMAGE_GAMESCREEN_PANE), COLOR_KEY);
            g.drawImage(img, 0, 0, GAMESCREEN_PANE_WIDTH, GAMESCREEN_PANE_HEIGHT, 0, 0, GAMESCREEN_PANE_WIDTH, GAMESCREEN_PANE_HEIGHT, null);
            
            String moneyStolen = "$ Stolen: $" + record.getMoney(currentLevel);
            g.drawString(moneyStolen, 5, 120);
        } else{
            renderSprite(g, bg);
        }
    }

    private void renderGUIControls(Graphics g) {
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

    private void renderDialogs(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites)
        {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
            
            if(data.won()){
                // display stats
                int money = data.getMoney();
                
                String win = "You have successfully stolen $" + money + ".";
                
                g.drawString(win, DIALOG_TEXT_X, DIALOG_TEXT_Y);
                
                Collection<Sprite> buttonSprites = game.getGUIButtons().values();
                for (Sprite sp : buttonSprites)
                {
                    if ((sp.getSpriteType().getSpriteTypeID() == LEAVE_TOWN_BUTTON_TYPE) || (sp.getSpriteType().getSpriteTypeID() == TRY_AGAIN_BUTTON_TYPE))
                        renderSprite(g, sp);
                }
            }
        }
    }

    private void renderStats(Graphics g) {
        
    }

    private void renderDebuggingText(Graphics g) {
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
        if (!s.getState().equals(States.INVISIBLE_STATE.toString()))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
        }
    }
}
