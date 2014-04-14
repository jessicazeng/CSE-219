package sorting_hat.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import static sorting_hat.SortingHatConstants.MILLIS_IN_AN_HOUR;
import static sorting_hat.SortingHatConstants.MILLIS_IN_A_MINUTE;
import static sorting_hat.SortingHatConstants.MILLIS_IN_A_SECOND;

/**
 * This class represents the complete playing history for the player
 * since originally starting the application. Note that it stores
 * stats separately for different levels.
 * 
 * @author Richard McKenna & ________________
 */
public class SortingHatRecord
{
    // HERE ARE ALL THE RECORDS
    private HashMap<String, SortingHatLevelRecord> levelRecords;

    /**
     * Default constructor, it simply creates the hash table for
     * storing all the records stored by level.
     */
    public SortingHatRecord()
    {
        levelRecords = new HashMap();
    }

    // GET METHODS
        // - getAlgorithm
        // - getGamesPlayed
        // - getWins
        // - getPerfectWins
        // - getFastestPerfectWinTime
        // - hasLevel

    /**
     * This method gets the algorithm for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The number of games played for the levelName level.
     */
    public String getAlgorithm(String levelName) 
    {
        SortingHatLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null)
            return null;
        // OTHERWISE RETURN THE GAMES PLAYED
        else
            return rec.algorithm; 
    }
    
    /**
     * This method gets the games played for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The number of games played for the levelName level.
     */
    public int getGamesPlayed(String levelName) 
    {
        SortingHatLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE GAMES PLAYED
        else
            return rec.gamesPlayed; 
    }

    /**
     * This method gets the wins for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The wins the player has earned for the levelName level.
     */    
    public int getWins(String levelName)
    {
        SortingHatLevelRecord rec = levelRecords.get(levelName);
        
        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0        
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE WINS
        else
            return rec.wins; 
    }
    
    public int getPerfectWins(String levelName){
        SortingHatLevelRecord rec = levelRecords.get(levelName);
        
        if(rec == null)
            return 0;
        else
            return rec.perfectWins;
    }
    
    public String getPerfectWinTime(String levelName){
        SortingHatLevelRecord rec = levelRecords.get(levelName);
            
        long timeInMillis = rec.endTimeMillis;
            
            long hours = timeInMillis / MILLIS_IN_AN_HOUR;
            timeInMillis -= hours * MILLIS_IN_AN_HOUR;
            long minutes = timeInMillis / MILLIS_IN_A_MINUTE;
            timeInMillis -= minutes * MILLIS_IN_A_MINUTE;
            long seconds = timeInMillis / MILLIS_IN_A_SECOND;

            // THEN ADD THE TIME OF GAME SUMMARIZED IN PARENTHESES
            String minutesText = "" + minutes;
            if (minutes < 10)
            {
                minutesText = "0" + minutesText;
            }
            String secondsText = "" + seconds;
            if (seconds < 10)
            {
                secondsText = "0" + secondsText;
            }
            String time = hours + ":" + minutesText + ":" + secondsText;
            if(time.matches("0:00:00"))
                return "";
            else
                return time;
    }
    
    public long getPerfectWinInMillis(String levelName){
        SortingHatLevelRecord rec = levelRecords.get(levelName);
        
        return rec.endTimeMillis;
    }
    
    /**
     * Returns true if there is already a level called levelName, false
     * otherwise.
     */
    public boolean hasLevel(String levelName)
    {
        return levelRecords.containsKey(levelName);
    } 
    
    // MUTATOR METHOD
    public void setAlgorithm(String levelName, SortingHatAlgorithm initAlgorithm)
    {
        levelRecords.get(levelName).algorithm = initAlgorithm.name;
    }    
    
    // ADD METHODS
        // -addLevel
        // -addLoss
        // -addSortingHatLevelRecord
        // -addWin
        // -addPerfectWin

    /**
     * Adds a level record, which will be used to store the
     * game results for a particular level. This is used when
     * adding a brand new level that hasn't been played yet.
     */
    public void addLevel(String levelName, String algorithmName)
    {
        // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
        // THE FIRST TIME WE'VE PLAYED IT
        SortingHatLevelRecord rec = new SortingHatLevelRecord();
        rec.algorithm = algorithmName;
        rec.gamesPlayed = 0;
        rec.wins = 0;
        levelRecords.put(levelName, rec);
    }

    /**
     * We don't count losses, so all this does is add a game without
     * adding a win.
     */
    public void addLoss(String levelName)
    {
        levelRecords.get(levelName).gamesPlayed++;
    }
    
    /**
     * Adds the record for a level. This is used when loading records full of
     * data loaded from files.
     * 
     * @param levelName
     * 
     * @param rec 
     */
    public void addSortingHatLevelRecord(String levelName, SortingHatLevelRecord rec)
    {
        levelRecords.put(levelName, rec);
    }

    /**
     * This method adds a win to the current player's record according
     * to the level being played.
     */
    public void addWin(String levelName)
    {
        // GET THE RECORD FOR levelName
        SortingHatLevelRecord rec = levelRecords.get(levelName);
        
        // UPDATE THE STATS
        rec.gamesPlayed++;
        rec.wins++;
    }
    
    public void addPerfectWin(String levelName){
        // GET THE RECORD FOR levelName
        SortingHatLevelRecord rec = levelRecords.get(levelName);
        
        rec.perfectWins++;
    }
    
    //public void addPerfectTime(String levelName, String time){
        //SortingHatLevelRecord rec = levelRecords.get(levelName);
        
        //rec.endTime = time;
    //}
    
    public void addPerfectWinInMillis(String levelName, long time){
        SortingHatLevelRecord rec = levelRecords.get(levelName);
        
        rec.endTimeMillis = time;
    }
    
    // ADDITIONAL SERVICE METHODS
        // -toByteArray

    /**
     * This method constructs and fills in a byte array with all the
     * necessary data stored by this object. We do this because writing
     * a byte array all at once to a file is fast. Certainly much faster
     * than writing to a file across many write operations.
     * 
     * @return A byte array filled in with all the data stored in this
     * object, which means all the player records in all the levels.
     * 
     * @throws IOException Note that this method uses a stream that
     * writes to an internal byte array, not a file. So this exception
     * should never happen.
     */
    public byte[] toByteArray() throws IOException
    {
        Iterator<String> keysIt = levelRecords.keySet().iterator();
        int numLevels = levelRecords.keySet().size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(numLevels);
        while(keysIt.hasNext())
        {
            // PACK IT WITH ALL THE DATA FOR THE RECORDS
            String key = keysIt.next();
            dos.writeUTF(key);
            SortingHatLevelRecord rec = levelRecords.get(key);
            dos.writeUTF(rec.algorithm.toString());
            dos.writeInt(rec.gamesPlayed);
            dos.writeInt(rec.wins);
            //dos.writeInt(rec.perfectWins);
            //dos.writeUTF(rec.endTime);
            //dos.writeLong(rec.endTimeMillis);
        }
        // AND THEN RETURN IT
        return baos.toByteArray();
    }
}    