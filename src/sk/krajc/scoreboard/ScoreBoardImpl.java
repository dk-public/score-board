package sk.krajc.scoreboard;

import sk.krajc.scoreboard.exception.GameAlreadyInProgressException;
import sk.krajc.scoreboard.exception.NoSuchGameInProgressException;

import java.util.ArrayList;

public class ScoreBoardImpl implements ScoreBoard {

    public static final String NO_GAME_IN_PROGRESS_MESSAGE = "No game in progress";

    private ArrayList<ScoreBoardRow> scoreBoardTable;

    public ScoreBoardImpl() {
        scoreBoardTable = new ArrayList<ScoreBoardRow>();
    }

    private boolean isGameInProgress(String homeTeamName, String awayTeamName) {
        return scoreBoardTable.contains(new ScoreBoardRow(homeTeamName, awayTeamName));
    }

    @Override
    public void startGame(String homeTeamName, String awayTeamName) {
        if(isGameInProgress(homeTeamName, awayTeamName)){
            throw new GameAlreadyInProgressException();
        } else {
            scoreBoardTable.add(new ScoreBoardRow(homeTeamName, 0, awayTeamName, 0));
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
        } else {
            throw new NoSuchGameInProgressException();
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

