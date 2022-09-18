public interface ScoreBoard {
    void startGame(String homeTeamName, String awayTeamName);
    void finishGame(String homeTeamName, String awayTeamName);
    void updateScore(String homeTeamName, int homeTeamScore, String awayTeamName, int awayTeamScore);
    String getSummary();
}
