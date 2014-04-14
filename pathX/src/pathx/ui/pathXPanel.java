/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
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
import pathx.data.Record;

/**
 *
 * @author Jessica
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
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        if(((pathXGame)game).isCurrentScreenState(LEVEL_SCREEN_STATE)){
            Viewport viewport = data.getViewport();
            viewport.updateViewportBoundaries();
            int x1 = viewport.getViewportX();
            int y1 = viewport.getViewportY();
            int x2 = x1 + viewport.getViewportWidth();
            int y2 = y1 + viewport.getViewportHeight();
            SpriteType bgST = bg.getSpriteType();
            Image img = bgST.getStateImage(bg.getState());
            g.drawImage(img, 0, 0, MAP_WIDTH, MAP_HEIGHT, x1, y1, x2, y2, null);
            
            Record rec = ((pathXGame)game).getPlayerRecord();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            ArrayList<String> levels = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
            ArrayList<String> positionx = props.getPropertyOptionsList(pathXPropertyType.X_LOCATION);
            ArrayList<String> positiony = props.getPropertyOptionsList(pathXPropertyType.Y_LOCATION);
            for (int i = 0; i < levels.size(); i++){
                String levelName = levels.get(i);
                
                int screenPositionX1 = viewport.getViewportX();
                int screenPositionY1 = viewport.getViewportY();
                int screenPositionX2 = viewport.getViewportWidth() + screenPositionX1;
                int screenPositionY2 = viewport.getViewportHeight() + screenPositionY1;
                
                int x = rec.getLevelPositionX(levelName) - screenPositionX1;
                int y = rec.getLevelPositionY(levelName) - screenPositionY1;
                
                Boolean locked = rec.isLocked(levelName);
                Boolean levelCompleted = rec.isLevelCompleted(levelName);
                
                // if use has not yet unlocked the level
                if(locked == true){
                    g.setColor(Color.white);
                    g.fillOval(x, y, 20, 20);
                    g.setColor(Color.black);
                    g.drawOval(x, y, 20, 20);
                } else{ // level has been unlocked
                    if(levelCompleted == true){ // if user successfully robbed this location
                        g.setColor(Color.green);
                        g.fillOval(x, y, 20, 20);
                    } else{
                        g.setColor(Color.red);
                        g.fillOval(x, y, 20, 20);
                    }
                    
                    g.setColor(Color.black);
                    g.drawOval(x, y, 20, 20);
                }
            }
            
            //int x = 200 - viewport.getViewportX();
            //int y = 200 - viewport.getViewportY();
            //g.setColor(Color.red);
            //g.fillOval(x, y, 20, 20);
            //g.setColor(Color.red);
            //g.drawOval(x, y, 20, 20);
        } else{
            renderSprite(g, bg);
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
