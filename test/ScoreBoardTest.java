import org.junit.jupiter.api.Test;

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
        assertEquals("No game in progress", scoreBoard.getSummary());

    }
}
