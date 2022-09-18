package sk.krajc.scoreboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
}
