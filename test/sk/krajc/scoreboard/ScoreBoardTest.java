package sk.krajc.scoreboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sk.krajc.scoreboard.exception.BadTeamNameException;
import sk.krajc.scoreboard.exception.GameAlreadyInProgressException;
import sk.krajc.scoreboard.exception.NoSuchGameInProgressException;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {

    @Test
    void testHappyPath()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();
        scoreBoard.startGame("England", "Russia");
        scoreBoard.updateScore("England", 1, "Russia", 0);
        assertEquals("England 1 - Russia 0", scoreBoard.getSummary());
        scoreBoard.finishGame("England", "Russia");
        assertEquals(scoreBoard.NO_GAME_IN_PROGRESS_MESSAGE, scoreBoard.getSummary());
    }

    @Test
    void testExceptionOfStartingExistingGame()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();
        scoreBoard.startGame("England", "Russia");
        Assertions.assertThrows(GameAlreadyInProgressException.class, () -> {
            scoreBoard.startGame("England", "Russia");
        }, "GameAlreadyInProgress was expected");
    }

    @Test
    void testExceptionOfFinishingNonExistingGame()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();
        scoreBoard.startGame("England", "Russia");
        Assertions.assertThrows(NoSuchGameInProgressException.class, () -> {
            scoreBoard.finishGame("Russia", "Iran");
        }, "NoSuchGameInProgressException was expected");
    }

    @Test
    void testExceptionOfUpdatingNonExistingGame()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();
        scoreBoard.startGame("England", "Iran");
        Assertions.assertThrows(NoSuchGameInProgressException.class, () -> {
            scoreBoard.updateScore("England", 1, "Russia", 0);
        }, "NoSuchGameInProgressException was expected");
    }

    @Test
    void testBasicSummaryOrdering()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();

        scoreBoard.startGame("England", "Russia");
        scoreBoard.updateScore("England", 1, "Russia", 0);

        scoreBoard.startGame("Iran", "Ukraine");
        scoreBoard.updateScore("Iran", 0, "Ukraine", 8);

        assertEquals("Iran 0 - Ukraine 8\n"
                            + "England 1 - Russia 0", scoreBoard.getSummary());

    }

    @Test
    void testSummaryOrderingFromExcercise()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();

        scoreBoard.startGame("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", 0, "Canada", 5);

        scoreBoard.startGame("Spain", "Brazil");
        scoreBoard.updateScore("Spain", 10, "Brazil", 2);

        scoreBoard.startGame("Germany", "France");
        scoreBoard.updateScore("Germany", 2, "France", 2);

        scoreBoard.startGame("Uruguay", "Italy");
        scoreBoard.updateScore("Uruguay", 6, "Italy", 6);

        scoreBoard.startGame("Argentina", "Australia");
        scoreBoard.updateScore("Argentina", 3, "Australia", 1);

        assertEquals(
                "Uruguay 6 - Italy 6\n" +
                        "Spain 10 - Brazil 2\n" +
                        "Mexico 0 - Canada 5\n" +
                        "Argentina 3 - Australia 1\n" +
                        "Germany 2 - France 2", scoreBoard.getSummary());
    }

    @Test
    void testSummaryOrderingFromExcerciseExtended()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();

        scoreBoard.startGame("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", 0, "Canada", 5);

        scoreBoard.startGame("Spain", "Brazil");
        scoreBoard.updateScore("Spain", 10, "Brazil", 2);

        scoreBoard.startGame("Germany", "France");
        scoreBoard.updateScore("Germany", 2, "France", 2);

        scoreBoard.startGame("Uruguay", "Italy");
        scoreBoard.updateScore("Uruguay", 6, "Italy", 6);

        scoreBoard.startGame("Argentina", "Australia");
        scoreBoard.updateScore("Argentina", 3, "Australia", 1);

        scoreBoard.updateScore("Spain", 11, "Brazil", 2);
        scoreBoard.startGame("India", "Switzerland");
        scoreBoard.updateScore("India", 1, "Switzerland", 0);

        assertEquals(
                "Spain 11 - Brazil 2\n" +
                        "Uruguay 6 - Italy 6\n" +
                        "Mexico 0 - Canada 5\n" +
                        "Argentina 3 - Australia 1\n" +
                        "Germany 2 - France 2\n" +
                        "India 1 - Switzerland 0", scoreBoard.getSummary());
    }

    @Test
    void testExceptionOfNullTeamName()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();

        Assertions.assertThrows(BadTeamNameException.class, () -> {
            scoreBoard.startGame("England", null);
        }, "BadTeamNameException was expected");
    }

    @Test
    void testArithmeticException()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();

        Assertions.assertThrows(ArithmeticException.class, () -> {
            scoreBoard.startGame("Mexico", "Canada");
            scoreBoard.updateScore("Mexico", 0, "Canada", 5);
            scoreBoard.updateScore("Mexico", 0, "Canada", 0);
        }, "ArithmeticException was expected");
    }

}
