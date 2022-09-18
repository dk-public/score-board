package sk.krajc.scoreboard;

import java.util.Objects;

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

    public ScoreBoardRow(String homeTeamName, String awayTeamName) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
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
