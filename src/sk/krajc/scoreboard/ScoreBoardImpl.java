package sk.krajc.scoreboard;

import java.util.ArrayList;

public class ScoreBoardImpl implements ScoreBoard {

    public static final String NO_GAME_IN_PROGRESS_MESSAGE = "No game in progress";

    private ArrayList<ScoreBoardRow> scoreBoardTable;

    public ScoreBoardImpl() {
        scoreBoardTable = new ArrayList<ScoreBoardRow>();
    }

    @Override
    public void startGame(String homeTeamName, String awayTeamName) {
        scoreBoardTable.add(new ScoreBoardRow(homeTeamName, 0, awayTeamName, 0));
    }

    @Override
    public void finishGame(String homeTeamName, String awayTeamName) {
        scoreBoardTable.removeIf(x -> x.homeTeamName.equals(homeTeamName) && x.awayTeamName.equals(awayTeamName));
    }

    @Override
    public void updateScore(String homeTeamName, int homeTeamScore, String awayTeamName, int awayTeamScore) {
        scoreBoardTable.stream().filter(x -> x.homeTeamName.equals(homeTeamName) && x.awayTeamName.equals(awayTeamName))
                .findAny().ifPresent(x -> {x.homeTeamScore = homeTeamScore; x.awayTeamScore = awayTeamScore;});
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

class ScoreBoardRow {
    String homeTeamName;
    int homeTeamScore;
    String awayTeamName;
    int awayTeamScore;

    public ScoreBoardRow(String homeTeamName, int homeTeamScore, String awayTeamName, int awayTeamScore) {
        this.homeTeamName = homeTeamName;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamName = awayTeamName;
        this.awayTeamScore = awayTeamScore;
    }
}
