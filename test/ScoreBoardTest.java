import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {

    @Test
    void testDummy()
    {
        ScoreBoardImpl scoreBoard = new ScoreBoardImpl();
        assertNull(scoreBoard.getSummary());
    }
}
