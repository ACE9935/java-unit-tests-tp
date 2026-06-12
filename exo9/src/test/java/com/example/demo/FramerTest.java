import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrameTest {

    private IGenerateur generateur;

    @BeforeEach
    void setUp() {
        generateur = mock(IGenerateur.class);
    }

    @Test
    void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(6);

        boolean result = frame.makeRoll();

        assertTrue(result);
        assertEquals(6, frame.getScore());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(6);
        when(generateur.randomPin(4)).thenReturn(3); 

        frame.makeRoll(); 
        boolean result = frame.makeRoll(); 

        assertTrue(result);
        assertEquals(9, frame.getScore());
    }

    @Test
    void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(10); 

        frame.makeRoll();
        boolean result = frame.makeRoll(); 

        assertFalse(result);
        assertEquals(10, frame.getScore()); 
    }

    @Test
    void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(4);
        when(generateur.randomPin(6)).thenReturn(3);

        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll(); 

        assertFalse(result);
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 7);

        frame.makeRoll(); 
        boolean result = frame.makeRoll(); 

        assertTrue(result);
        assertEquals(17, frame.getScore());
    }

    @Test
    void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 5);

        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertTrue(result);
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 10, 4);

        frame.makeRoll(); 
        frame.makeRoll(); 
        boolean result = frame.makeRoll(); 

        assertTrue(result);
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 6);
        when(generateur.randomPin(4)).thenReturn(3); 

        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertTrue(result);
        assertEquals(19, frame.getScore());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(7, 8);
        when(generateur.randomPin(3)).thenReturn(3); 

        frame.makeRoll(); 
        frame.makeRoll(); 
        boolean result = frame.makeRoll(); 

        assertTrue(result);
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(5, 9);
        when(generateur.randomPin(5)).thenReturn(5); 

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertEquals(19, frame.getScore());
    }

    @Test
    void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(4);
        when(generateur.randomPin(6)).thenReturn(4); 

        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll(); 

        assertFalse(result);
    }

    @Test
    void shouldRejectFourthRollInLastFrame() {
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 10, 10);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll(); 

        assertFalse(result);
    }
}