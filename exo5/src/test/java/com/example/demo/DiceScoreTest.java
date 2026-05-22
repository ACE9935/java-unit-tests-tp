package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DiceScoreTest {

    @Mock
    private Ide de;

    @InjectMocks
    private DiceScore diceScore;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetScore_DesIdentiques_RetourneDoublePlusDix() {
        when(de.getRoll()).thenReturn(4, 4);

        int score = diceScore.getScore();

        assertEquals(18, score);
    }

    @Test
    public void testGetScore_DesIdentiquesEtEgauxA6_RetourneTrente() {
        when(de.getRoll()).thenReturn(6, 6);

        int score = diceScore.getScore();

        assertEquals(30, score);
    }

    @Test
    public void testGetScore_DesQuelconques_RetourneLePlusGrand() {
        when(de.getRoll()).thenReturn(3, 5);
        assertEquals(5, diceScore.getScore());

        when(de.getRoll()).thenReturn(5, 2);
        assertEquals(5, diceScore.getScore());
    }
}