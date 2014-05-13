package sdokb.game;

import java.util.ArrayList;
import java.util.Iterator;
import sdokb.ui.KevinBaconUI;

/**
 * KevinBaconGameStateManager runs the game. Note that it does so completely
 * independent of the presentation of the game.
 *
 * @author Richard McKenna & _________________
 */
public class KevinBaconGameStateManager
{

    // THE GAME WILL ALWAYS BE IN
    // ONE OF THESE THREE STATES
    public enum KevinBaconGameState
    {

        GAME_NOT_STARTED,
        GAME_IN_PROGRESS,
        GAME_OVER
    }

    // THIS IS THE STATE FOR THE GAME IN PROGRESS
    private KevinBaconGameState currentGameState;

    // THIS IS THE GAME CURRENTLY BEING PLAYED
    private KevinBaconGameData gameInProgress;

    // WHEN THE STATE OF THE GAME CHANGES IT WILL NEED TO BE
    // REFLECTED IN THE USER INTERFACE, SO THIS CLASS NEEDS
    // A REFERENCE TO THE UI
    private KevinBaconUI ui;

    // HOLDS ALL OF THE COMPLETED GAMES. NOTE THAT THE GAME
    // IN PROGRESS IS NOT ADDED UNTIL IT IS COMPLETED
    private ArrayList<KevinBaconGameData> gamesHistory;

    // THE ACTOR/FILMS GAME GRAPH DATA STRUCTURE
    private KevinBaconGameGraphManager gameGraphManager;

    // THIS IS 
    private final String NEWLINE_DELIMITER = "\n";

    /**
     * This constructor initializes this class for use, but does not start a
     * game.
     *
     * @param initUI A reference to the graphical user interface, this game
     * state manager needs to inform it of when this state changes so that it
     * can display the appropriate changes.
     */
    public KevinBaconGameStateManager(KevinBaconUI initUI)
    {
        // STORE THIS FOR LATER
        ui = initUI;

        // WE HAVE NOT STARTED A GAME YET
        currentGameState = KevinBaconGameState.GAME_NOT_STARTED;

        // NO GAMES HAVE BEEN PLAYED YET, BUT INITIALIZE
        // THE DATA STRCUTURE FOR PLACING COMPLETED GAMES
        gamesHistory = new ArrayList();

        // THE FIRST GAME HAS NOT BEEN STARTED YET
        gameInProgress = null;

        // THIS IS THE ACTUAL GAME GRAPH THAT WE'LL
        // WALK TO FIND CONNECTIONS BETWEEN ACTORS & FILMS
        gameGraphManager = new KevinBaconGameGraphManager();
    }

    // ACCESSOR METHODS
    public KevinBaconGameGraphManager getGameGraphManager()
    {
        return gameGraphManager;
    }

    public KevinBaconGameData getGameInProgress()
    {
        return gameInProgress;
    }

    public int getNumGamesPlayed()
    {
        return gamesHistory.size();
    }

    public Iterator<KevinBaconGameData> getGamesHistoryIterator()
    {
        return gamesHistory.iterator();
    }

    public boolean isGameNotStarted()
    {
        return currentGameState == KevinBaconGameState.GAME_NOT_STARTED;
    }

    public boolean isGameOver()
    {
        return currentGameState == KevinBaconGameState.GAME_OVER;
    }

    public boolean isGameInProgress()
    {
        return currentGameState == KevinBaconGameState.GAME_IN_PROGRESS;
    }

    /**
     * Counts and returns the number of wins during this game session.
     *
     * @return The number of games in that have been completed that the player
     * won.
     */
    public int getWins()
    {
        // ITERATE THROUGH ALL THE COMPLETED GAMES
        Iterator<KevinBaconGameData> it = gamesHistory.iterator();
        int wins = 0;
        while (it.hasNext())
        {
            // GET THE NEXT GAME IN THE SEQUENCE
            KevinBaconGameData game = it.next();

            // IF IT ENDED IN A WIN, INC THE COUNTER
            if (game.isKevinBaconFound())
            {
                wins++;
            }
        }
        return wins;
    }

    /**
     * Counts the perfect wins and returns that value.
     *
     * @return
     */
    public int getPerfectWins()
    {
        int perfectWins = 0;
        for (KevinBaconGameData game : gamesHistory)
        {
            if (game.isPerfectWin())
            {
                perfectWins++;
            }
        }
        return perfectWins;
    }

    /**
     * Counts and returns the number of losses during this game session.
     *
     * @return The number of games in that have been completed that the player
     * lost.
     */
    public int getLosses()
    {
        // ITERATE THROUGH ALL THE COMPLETED GAMES
        Iterator<KevinBaconGameData> it = gamesHistory.iterator();
        int losses = 0;
        while (it.hasNext())
        {
            // GET THE NEXT GAME IN THE SEQUENCE
            KevinBaconGameData game = it.next();

            // IF IT ENDED IN A LOSS, INC THE COUNTER
            if (!game.isKevinBaconFound())
            {
                losses++;
            }
        }
        return losses;
    }

    /**
     * This method starts a new game, initializing all the necessary data for
     * that new game as well as recording the current game (if it exists) in the
     * games history data structure. It also lets the user interface know about
     * this change of state such that it may reflect this change.
     */
    public void startNewGame()
    {
        // IS THERE A GAME ALREADY UNDERWAY?
        // YES, SO END THAT GAME AS A LOSS
        if (!isGameNotStarted() && (!gamesHistory.contains(gameInProgress)))
        {
            gamesHistory.add(gameInProgress);
        }

        // IF THERE IS A GAME IN PROGRESS AND THE PLAYER HASN'T WON, THAT MEANS
        // THE PLAYER IS QUITTING, SO WE NEED TO SAVE THE GAME TO OUR HISTORY
        // DATA STRUCTURE. NOTE THAT IF THE PLAYER WON THE GAME, IT WOULD HAVE
        // ALREADY BEEN SAVED SINCE THERE WOULD BE NO GUARANTEE THE PLAYER WOULD
        // CHOOSE TO PLAY AGAIN
        if (isGameInProgress() && !gameInProgress.isKevinBaconFound())
        {
            // QUIT THE GAME, WHICH SETS THE END TIME
            gameInProgress.endGameAsLoss();

            // MAKE SURE THE STATS PAGE KNOWS ABOUT THE COMPLETED GAME
            ui.getDocManager().addGameResultToStatsPage(gameInProgress);
        }

        // AND NOW MAKE A NEW GAME
        makeNewGame();

        // AND UPDATE THE GAME DISPLAY
        ui.resetUI();
        ui.getDocManager().updateActorInGamePage();

        // LOAD ALL THE FILMS INTO THE COMBO BOX
        ArrayList<String> startingActorFilmIds = gameInProgress.getStartingActor().getFilmIDs();
        ui.reloadComboBox(startingActorFilmIds);
    }

    /**
     * This method chooses a secret word and uses it to create a new game,
     * effectively starting it.
     */
    public void makeNewGame()
    {
        // FIRST PICK THE ACTOR
        Actor startingActor = gameGraphManager.pickRandomActor();
        ArrayList<Connection> shortestPath = gameGraphManager.findShortestPathToKevinBacon(startingActor);

        // THEN MAKE THE GAME WITH IT
        gameInProgress = new KevinBaconGameData(startingActor, shortestPath);

        // THE GAME IS OFFICIALLY UNDERWAY
        currentGameState = KevinBaconGameState.GAME_IN_PROGRESS;
    }

    /**
     * This method processes the guess, checking to make sure it's in the
     * dictionary in use and then updating the game accordingly.
     *
     * @param guess The word that the player is guessing is the secret word.
     * Note that it must be in the dictionary.
     *
     * @throws sdokb.game.DeadEndException
     */
    public void processGuess(IMDBObject guess) throws
            DeadEndException
    {
        // ONLY PROCESS GUESSES IF A GAME IS IN PROGRESS
        if (!isGameInProgress())
        {
            return;
        }

        ArrayList<String> nonCircularEdges = gameInProgress.getNonRepeatingIds(guess.getId(), gameGraphManager);

        // DEAD END, NOWHERE TO GO
        if (nonCircularEdges.isEmpty())
        {
            // END THE GAME IN A LOSS
            currentGameState = KevinBaconGameState.GAME_OVER;
            gamesHistory.add(gameInProgress);
            gameInProgress.guess(guess);
            ui.enableGuessComboBox(false);
            ui.getDocManager().updateGuessesList();
            ui.getDocManager().addGameResultToStatsPage(gameInProgress);
            throw new DeadEndException(guess.toString());
        }

        // IF WE REACH HERE THEN THE GUESS HAS NEVER BEEN MADE BEFORE
        // SO LET'S PROCESS THE GUESS
        gameInProgress.guess(guess);

        // WAS KEVIN BACON IN THE FILM JUST GUESSED?
        if (!gameInProgress.isWaitingForFilm() && gameGraphManager.wasKevinBaconInFilm(guess.getId()))
        {

            // THE PLAYER WINS
            gameInProgress.endGameAsWin(gameGraphManager.getKevinBacon());

            // CHANGE THE GAME STATE
            currentGameState = KevinBaconGameState.GAME_OVER;

            // ADD THE COMPLETED GAME TO THE HISTORY
            gamesHistory.add(gameInProgress);

            // AND MAKE SURE THE STATS PAGE IS CURRENT
            ui.getDocManager().addGameResultToStatsPage(gameInProgress);

            // DISABLE THE COMBO BOX UNTIL ANOTHER GAME STARTS
            ui.enableGuessComboBox(false);
        } else
        {
            // WE ONLY RELOAD THE COMBO BOX IF PLAY CONTINUES
            ui.reloadComboBox(nonCircularEdges);
        }
        
        // UPDATE THE GAME DISPLAY
        ui.getDocManager().updateGuessesList();        
    }
}
