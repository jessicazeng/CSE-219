package pathx.ui;

import static pathx.pathXConstants.LEVEL_SCREEN_STATE;
import static pathx.pathXConstants.MENU_SCREEN_STATE;
import pathx.data.pathXDataModel;
import pathx.file.pathXFileManager;

/**
 *
 * @author Jessica
 */
public class pathXEventHandler {
    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private pathXGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public pathXEventHandler(pathXGame initGame)
    {
        game = initGame;
    }
    
    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }
    
    
}
