package sk.krajc.scoreboard;

import sk.krajc.scoreboard.exception.GameAlreadyInProgressException;
import sk.krajc.scoreboard.exception.NoSuchGameInProgressException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreBoardImpl implements ScoreBoard {

    public static final String NO_GAME_IN_PROGRESS_MESSAGE = "No game in progress";
    public enum Field { TOTAL_SCORE, START_TIME };

    private ArrayList<ScoreBoardRow> scoreBoardTable;
    private AtomicInteger timeOrdering; //not date type, because date not needed and might have insufficient time resolution in concurrent runs

    public ScoreBoardImpl() {
        scoreBoardTable = new ArrayList<ScoreBoardRow>();
        timeOrdering = new AtomicInteger();
    }

    private boolean isGameInProgress(String homeTeamName, String awayTeamName) {
        return scoreBoardTable.contains(new ScoreBoardRow(homeTeamName, awayTeamName));
    }

    @Override
    public void startGame(String homeTeamName, String awayTeamName) {
        if(isGameInProgress(homeTeamName, awayTeamName)){
            throw new GameAlreadyInProgressException();
        } else {
            scoreBoardTable.add(new ScoreBoardRow(homeTeamName, 0, awayTeamName, 0, timeOrdering.incrementAndGet()));
        }
    }

    @Override
    public void finishGame(String homeTeamName, String awayTeamName) {
        if(isGameInProgress(homeTeamName, awayTeamName)) {
            scoreBoardTable.removeIf(x -> x.homeTeamName.equals(homeTeamName) && x.awayTeamName.equals(awayTeamName));
        } else {
            throw new NoSuchGameInProgressException();
        }
    }

    @Override
    public void updateScore(String homeTeamName, int homeTeamScore, String awayTeamName, int awayTeamScore) {
        if(isGameInProgress(homeTeamName, awayTeamName)) {
            scoreBoardTable.stream().filter(x -> x.homeTeamName.equals(homeTeamName) && x.awayTeamName.equals(awayTeamName))
                    .findAny().ifPresent(x -> {
                x.homeTeamScore = homeTeamScore;
                x.awayTeamScore = awayTeamScore;
            });
            ensureOrdering(homeTeamName, awayTeamName);
        } else {
            throw new NoSuchGameInProgressException();
        }
    }

    private int iterateUpUntilConditionMet(int iter, Field field, int condition, int iterLimit){ // passing field because calling methods through reflection would be slow and complicated
        while(iter >= iterLimit && (field == Field.START_TIME ? scoreBoardTable.get(iter).getStartTime(): scoreBoardTable.get(iter).getTotalScore()) < condition){
            iter--;
        }
        return iter;
    }

    private int iterateUpUntilTotalScoreBigger(int iter, int score, int iterLimit){
        return iterateUpUntilConditionMet(iter, Field.TOTAL_SCORE, score, iterLimit);
    }

    private int iterateUpUntilStartTimeBigger(int iter, int time, int iterLimit){
        return iterateUpUntilConditionMet(iter, Field.START_TIME, time, iterLimit);
    }

    private int bubbleUp(int iter, int currentIndex){
        if(iter != currentIndex - 1){
            for(int i = currentIndex; i>iter+1;i--){
                Collections.swap(scoreBoardTable, i, i-1);
                currentIndex--;
            }
        }
        return currentIndex;
    }

    private void ensureOrdering(String homeTeamName, String awayTeamName) {
        //doing sort from jdk library would have poor complexity (n log n or even more for 2 field sorting), so preferring manual maintenance
        int currentIndex = scoreBoardTable.indexOf(new ScoreBoardRow(homeTeamName, awayTeamName));
        int currentTotalScore = scoreBoardTable.get(currentIndex).updateTotalScore();
        int currentStartTime = scoreBoardTable.get(currentIndex).getStartTime();
        int iter = currentIndex - 1;
        iter = iterateUpUntilTotalScoreBigger(iter, currentTotalScore, 0);
        currentIndex = bubbleUp(iter, currentIndex);
        int iterLimit = currentIndex;
        for(; iterLimit > 0 && scoreBoardTable.get(iterLimit).getTotalScore() == scoreBoardTable.get(iterLimit-1).getTotalScore(); iterLimit--){
        }
        iter = iterateUpUntilStartTimeBigger(iter, currentStartTime, iterLimit >= 0 ? iterLimit : 0);
        currentIndex = bubbleUp(iter, currentIndex);
    }

    @Override
    public String getSummary() {
        String summary = "";
        for(int i = 0; i < scoreBoardTable.size(); i++){
            ScoreBoardRow row = scoreBoardTable.get(i);
            summary += row.homeTeamName + " " + row.homeTeamScore + " - " + row.awayTeamName + " " + row.awayTeamScore + (i < scoreBoardTable.size()-1 ? "\n" : "" );

        }
        return "".equals(summary) ? NO_GAME_IN_PROGRESS_MESSAGE : summary;
    }
}

