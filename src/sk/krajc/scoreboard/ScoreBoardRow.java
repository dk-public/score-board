package sk.krajc.scoreboard;

import java.util.Objects;

class ScoreBoardRow {
    String homeTeamName;
    int homeTeamScore;
    String awayTeamName;
    int awayTeamScore;
    int startTime;
    int totalScore;

    public ScoreBoardRow(String homeTeamName, int homeTeamScore, String awayTeamName, int awayTeamScore, int startTime) {
        this.homeTeamName = homeTeamName;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamName = awayTeamName;
        this.awayTeamScore = awayTeamScore;
        this.startTime = startTime;
    }

    public ScoreBoardRow(String homeTeamName, String awayTeamName) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int updateTotalScore(){
        return totalScore = homeTeamScore + awayTeamScore;
    }


    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreBoardRow that = (ScoreBoardRow) o;
        return homeTeamName.equals(that.homeTeamName) &&
                awayTeamName.equals(that.awayTeamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeamName, awayTeamName);
    }
}
