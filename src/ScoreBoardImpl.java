import java.util.ArrayList;

public class ScoreBoardImpl implements ScoreBoard {

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
        for(int i = 0; i < scoreBoardTable.size(); i++){
            ScoreBoardRow row = scoreBoardTable.get(i);
            if(row.homeTeamName.equals(homeTeamName) && row.awayTeamName.equals(awayTeamName)){
                scoreBoardTable.remove(i);
                return;
            }
        }
    }

    @Override
    public void updateScore(String homeTeamName, int homeTeamScore, String awayTeamName, int awayTeamScore) {
        for(int i = 0; i < scoreBoardTable.size(); i++){
            ScoreBoardRow row = scoreBoardTable.get(i);
            if(row.homeTeamName.equals(homeTeamName) && row.awayTeamName.equals(awayTeamName)){
                row.homeTeamScore = homeTeamScore;
                row.awayTeamScore = awayTeamScore;
                return;
            }
        }
    }

    @Override
    public String getSummary() {
        String summary = "";
        for(int i = 0; i < scoreBoardTable.size(); i++){
            ScoreBoardRow row = scoreBoardTable.get(i);
            summary += row.homeTeamName + " " + row.homeTeamScore + " - " + row.awayTeamName + " " + row.awayTeamScore + (i < scoreBoardTable.size()-1 ? "\n" : "" );

        }
        return "".equals(summary) ? "No game in progress" : summary;
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
