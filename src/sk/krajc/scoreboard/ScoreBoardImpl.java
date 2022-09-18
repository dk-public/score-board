package sk.krajc.scoreboard;

import sk.krajc.scoreboard.exception.GameAlreadyInProgressException;
import sk.krajc.scoreboard.exception.NoSuchGameInProgressException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreBoardImpl implements ScoreBoard {

    public static final String NO_GAME_IN_PROGRESS_MESSAGE = "No game in progress";

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

    private void ensureOrdering(String homeTeamName, String awayTeamName) {
        //doing sort from jdk library would have poor complexity (n log n or even more for 2 field sorting), so preferring manual maintenance
        int currentIndex = scoreBoardTable.indexOf(new ScoreBoardRow(homeTeamName, awayTeamName));
        int currentTotalScore = scoreBoardTable.get(currentIndex).updateTotalScore();
        int iter = currentIndex - 1;
        while(iter >= 0 && scoreBoardTable.get(iter).getTotalScore() < currentTotalScore){
            iter--;
        }
        int currentStartTime = scoreBoardTable.get(currentIndex).getStartTime();
        while(iter >= 0 && scoreBoardTable.get(iter).getStartTime() < currentStartTime){
            iter--;
        }
        if(iter != currentIndex - 1){
            Collections.swap(scoreBoardTable, currentIndex, iter + 1);
        }
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

