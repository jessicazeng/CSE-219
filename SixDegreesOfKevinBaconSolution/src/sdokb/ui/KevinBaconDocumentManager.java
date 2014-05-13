package sdokb.ui;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import sdokb.SixDegreesOfKevinBacon.KevinBaconPropertyType;
import sdokb.game.KevinBaconGameData;
import sdokb.game.KevinBaconGameStateManager;
import properties_manager.PropertiesManager;
import sdokb.game.Actor;
import sdokb.game.Connection;
import sdokb.game.Film;
import sdokb.game.KevinBaconGameGraphManager;

/**
 * Generates HTML content for display inside the game application, including \
 * the in-game GUI and the stats page. Note that we maintain both of these \
 * pages inside Documents, which store trees containing all the HTML. We 
 * will make use of HTML.Tag constants to update these DOMs
 * (Document Object Models).
 *
 * @author Richard McKenna & ____________________
 */
public class KevinBaconDocumentManager
{
    // THE GAME'S UI HAS ACCESS TO ALL COMPONENTS, SO
    // IT'S USEFUL TO HAVE IT WHEN WE NEED IT
    private KevinBaconUI ui;

    // THESE ARE THE DOCUMENTS WE'LL BE UPDATING HERE
    private HTMLDocument gameDoc;
    private HTMLDocument statsDoc;

    // WE'LL USE THESE TO BUILD OUR HTML
    private final String DASHES = "---";
    private final String START_TAG = "<";
    private final String END_TAG = ">";
    private final String SLASH = "/";
    private final String SPACE = " ";
    private final String EMPTY_TEXT = "";
    private final String NL = "\n";
    private final String QUOTE = "\"";
    private final String OPEN_PAREN = "(";
    private final String CLOSE_PAREN = ")";
    private final String COLON = ":";
    private final String EQUAL = "=";
    private final String COMMA = ",";
    private final String RGB = "rgb";

    // THESE ARE IDs IN THE GAME DISPLAY HTML FILE SO THAT WE 
    // CAN GRAB THE NECESSARY ELEMENTS AND UPDATE THEM
    private final String SUBHEADER_TEXT_ID = "subheader_text";
    private final String GUESSES_LIST_ID = "guesses_list";
    private final String WIN_DISPLAY_ID = "win_display";

    // THESE ARE IDs IN THE STATS HTML FILE SO THAT WE CAN
    // GRAB THE NECESSARY ELEMENTS AND UPDATE THEM
    private final String GAMES_PLAYED_ID = "games_played";
    private final String WINS_ID = "wins";
    private final String PERFECT_WINS_ID = "perfect_wins";
    private final String LOSSES_ID = "losses";
    private final String GAME_RESULTS_HEADER_ID = "game_results_header";
    private final String GAME_RESULTS_LIST_ID = "game_results_list";

    /**
     * This constructor just keeps the UI for later. Note that once constructed,
     * the docs will need to be set before this class can be used.
     *
     * @param initUI
     */
    public KevinBaconDocumentManager(KevinBaconUI initUI)
    {
        // KEEP THE UI FOR LATER
        ui = initUI;
    }

    /**
     * Accessor method for initializing the game doc, which displays while the
     * game is being played and displays the guesses. Note that this must be
     * done before this object can be used.
     *
     * @param initGameDoc The game document to be displayed while the game is
     * being played.
     */
    public void setGameDoc(HTMLDocument initGameDoc)
    {
        gameDoc = initGameDoc;
    }

    /**
     * Mutator method for initializing the stats doc, which displays past game
     * results and statistics. Note that this must be done before this object
     * can be used.
     *
     * @param initStatsDoc The stats document to be displayed on the stats
     * screen.
     */
    public void setStatsDoc(HTMLDocument initStatsDoc)
    {
        statsDoc = initStatsDoc;
    }

    /**
     * Called when a new game starts, it updates the starting actor display.
     */
    public void updateActorInGamePage()
    {
        try
        {
            // NOW FILL IN THE STARTING ACTOR
            Element sH = gameDoc.getElement(SUBHEADER_TEXT_ID);
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String subheadText = props.getProperty(KevinBaconPropertyType.GAME_SUBHEADER_TEXT);
            Actor startingActor = ui.getGSM().getGameInProgress().getStartingActor();
            gameDoc.setInnerHTML(sH, subheadText + startingActor.toString());
        } catch (BadLocationException | IOException e)
        {
            KevinBaconErrorHandler errorHandler = ui.getErrorHandler();
            errorHandler.processError(KevinBaconPropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

    /**
     * This method lets us add a guess to the game page display without having
     * to rebuild the entire page. We just rebuild the list item each time guess
     * is made.
     */
    public void updateGuessesList()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        KevinBaconGameStateManager gsm = ui.getGSM();
        KevinBaconGameData gameInProgress = gsm.getGameInProgress();
        KevinBaconGameGraphManager graph = gsm.getGameGraphManager();
        Connection lastConnection = gameInProgress.getLastConnection();
        if (lastConnection == null)
        {
            return;
        }

        try
        {
            // THERE ARE 2 SITUATIONS, 
            // 1) THE USER JUST SELECTED A FILM,
            //      WHICH MEANS WE HAVE A NEW LIST ITEM WHICH WOULD SAY
            //      ACTOR WAS IN FILM
            // 2) THE USER JUST SELECTED AN ACTOR,
            //      WHICH MEANS WE ARE UPDATING THE EXISTING LIST ITEM
            Actor actor1 = graph.getActor(lastConnection.getActor1Id());
            Film film = graph.getFilm(lastConnection.getFilmId());
            Element ol = gameDoc.getElement(GUESSES_LIST_ID);

            if (lastConnection.hasTwoActors())
            {
                Actor actor2 = graph.getActor(lastConnection.getActor2Id());

            // FOR THIS CASE WE NEED TO GET THE EXISING LIST ITEM
                // FOR ACTOR 1 AND UPDATE THE FILM
                // IF THE SECOND ACTOR IS KEVIN BACON THE GAME IS OVER
                // AND WE'LL HAVER TO ADD THIS LAST NODE WHOLE, SO
                // THIS li ELEMENT WILL BE NULL
                Element li = gameDoc.getElement(actor1.getId());
                if (li == null)
                {
                    String liText = START_TAG + HTML.Tag.LI + SPACE + HTML.Attribute.ID + EQUAL + QUOTE + actor1.getId() + QUOTE + END_TAG
                            + actor1.toString() + DASHES + film.getTitle()
                            + DASHES + actor2.toString() + SPACE + OPEN_PAREN + film.getYear() + CLOSE_PAREN
                            + START_TAG + SLASH + HTML.Tag.LI + END_TAG;
                    gameDoc.insertBeforeEnd(ol, liText);
                } else
                {
                    String liText = START_TAG + HTML.Tag.LI + SPACE + HTML.Attribute.ID + EQUAL + QUOTE + actor1.toString() + QUOTE + END_TAG
                            + actor1.toString()
                            + DASHES + QUOTE + film.getTitle() + QUOTE
                            + DASHES + actor2.toString() + SPACE + OPEN_PAREN + film.getYear() + CLOSE_PAREN
                            + START_TAG + SLASH + HTML.Tag.LI + END_TAG;
                    gameDoc.removeElement(li);
                    gameDoc.insertBeforeEnd(ol, liText);
                }
            } else
            {
                String liText = START_TAG + HTML.Tag.LI + SPACE + HTML.Attribute.ID + EQUAL + QUOTE + actor1.getId() + QUOTE + END_TAG
                        + actor1.toString() + DASHES + film.getTitle() + START_TAG + SLASH + HTML.Tag.LI + END_TAG;
                gameDoc.insertBeforeEnd(ol, liText);
            }

            // AND NOW ADD THE GAME OVER TEXT IF NEEDED
            Element winH2 = gameDoc.getElement(WIN_DISPLAY_ID);
            if (gameInProgress.isKevinBaconFound())
            {
                // PLAYER HAS WON
                String winText = props.getProperty(KevinBaconPropertyType.WIN_DISPLAY_TEXT);
                gameDoc.setInnerHTML(winH2, winText);
            }
            else if (!gsm.isGameInProgress())
            {
                // PLAYER HAS LOST
                String lossText = props.getProperty(KevinBaconPropertyType.LOSS_DISPLAY_TEXT);
                gameDoc.setInnerHTML(winH2, lossText);
            }
            else
            {
                // GAME IS STILL IN PROGRESS
                gameDoc.setInnerHTML(winH2, EMPTY_TEXT);
            }

           // this.printDoc(gameDoc);
        } // THE ERROR HANDLER WILL DEAL WITH ERRORS ASSOCIATED WITH BUILDING
        // THE HTML FOR THE PAGE, WHICH WOULD LIKELY BE DUE TO BAD DATA FROM
        // AN XML SETUP FILE
        catch (BadLocationException | IOException e)
        {
            KevinBaconErrorHandler errorHandler = ui.getErrorHandler();
            errorHandler.processError(KevinBaconPropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

    /**
     * When a new game starts the game page should not have a sub-header or
     * display guesses or a win state, so all of that has to be cleared out of
     * the DOM at that time. This method does the work of clearing out these
     * nodes.
     */
    public void clearGamePage()
    {
        try
        {
            // CLEAR THE GUESS LIST
            Element ol = gameDoc.getElement(GUESSES_LIST_ID);
            gameDoc.setInnerHTML(ol, START_TAG + HTML.Tag.BR + END_TAG);
            
            // CLEAR THE WIN DISPLAY
            Element winH2 = gameDoc.getElement(WIN_DISPLAY_ID);
            gameDoc.setInnerHTML(winH2, START_TAG + HTML.Tag.BR + END_TAG);
            this.printDoc(gameDoc);
        } 
        // THE ERROR HANDLER WILL DEAL WITH ERRORS ASSOCIATED WITH BUILDING
        // THE HTML FOR THE PAGE, WHICH WOULD LIKELY BE DUE TO BAD DATA FROM
        // AN XML SETUP FILE
        catch (BadLocationException | IOException ex)
        {
            KevinBaconErrorHandler errorHandler = ui.getErrorHandler();
            errorHandler.processError(KevinBaconPropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

    /**
     * This method adds the data from the completedGame argument to the stats
     * page, as well as loading all the newly computed stats for all the games
     * played.
     *
     * @param completedGame Game whose summary will be added to the stats page.
     */
    public void addGameResultToStatsPage(KevinBaconGameData completedGame)
    {
        // GET THE GAME STATS
        KevinBaconGameStateManager gsm = ui.getGSM();
        KevinBaconGameGraphManager graph = gsm.getGameGraphManager();
        int gamesPlayed = gsm.getNumGamesPlayed();
        int wins = gsm.getWins();
        int perfectWins = gsm.getPerfectWins();
        int losses = gsm.getLosses();

        try
        {
            // USE THE STATS TO UPDATE THE TABLE AT THE TOP OF THE PAGE
            Element gamePlayedElement = statsDoc.getElement(GAMES_PLAYED_ID);
            statsDoc.setInnerHTML(gamePlayedElement, EMPTY_TEXT + gamesPlayed);

            Element winsElement = statsDoc.getElement(WINS_ID);
            statsDoc.setInnerHTML(winsElement, EMPTY_TEXT + wins);
            
            Element perfectWinsElement = statsDoc.getElement(PERFECT_WINS_ID);
            statsDoc.setInnerHTML(perfectWinsElement, EMPTY_TEXT + perfectWins);

            Element lossesElement = statsDoc.getElement(LOSSES_ID);
            statsDoc.setInnerHTML(lossesElement, EMPTY_TEXT + losses);

            // ADD THE SUBHEADER
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String gameResultsText = props.getProperty(KevinBaconPropertyType.GAME_RESULTS_TEXT);
            Element h2 = statsDoc.getElement(GAME_RESULTS_HEADER_ID);
            statsDoc.setInnerHTML(h2, gameResultsText);

            // AND NOW ADD THE LATEST GAME TO THE LIST
            Element ol = statsDoc.getElement(GAME_RESULTS_LIST_ID);
            String gameSummary =    
                      completedGame.getGameTimeDescription() + SLASH
                    + completedGame.getDegrees()
                    + DASHES + SPACE;
            gameSummary += completedGame.getStartingActor().toString();
            
            Iterator<Connection> it = completedGame.gamePathIterator();
            while (it.hasNext())
            {
                Connection node = it.next();
                gameSummary += DASHES + graph.getFilm(node.getFilmId()).toString();
                if (node.hasTwoActors())
                {
                    gameSummary += DASHES + graph.getActor(node.getActor2Id()).toString();
                }
            }
            String color = "rgb(0,0,0)";
            if (completedGame.isPerfectWin())
                color = "rgb(255,0,0)";
            else if (completedGame.isKevinBaconFound())
                color = "rgb(0,255,0)";
            String htmlText = START_TAG + HTML.Tag.LI + SPACE
                    + HTML.Attribute.STYLE + EQUAL + QUOTE + HTML.Attribute.COLOR + COLON + color + QUOTE 
                    + END_TAG + gameSummary + START_TAG + SLASH + HTML.Tag.LI + END_TAG + NL;
            
            System.out.println(htmlText);
            
            
            
            statsDoc.insertBeforeEnd(ol, htmlText);
        }
        // WE'LL LET THE ERROR HANDLER TAKE CARE OF ANY ERRORS,
        // WHICH COULD HAPPEN IF XML SETUP FILES ARE IMPROPERLY
        // FORMATTED
        catch (BadLocationException | IOException e)
        {
            KevinBaconErrorHandler errorHandler = ui.getErrorHandler();
            errorHandler.processError(KevinBaconPropertyType.INVALID_DOC_ERROR_TEXT);
        }
    }

    /**
     * This helper method lets you print the contents of a DOM (i.e. a doc) to
     * the console, which can help with error checking during testing.
     */
    public void printDoc(HTMLDocument doc) throws BadLocationException, IOException
    {
        StringWriter writer = new StringWriter();
        HTMLEditorKit kit = new HTMLEditorKit();
        kit.write(writer, doc, 0, doc.getLength());
        String htmlText = writer.toString();
        System.out.println(htmlText);
    }
}