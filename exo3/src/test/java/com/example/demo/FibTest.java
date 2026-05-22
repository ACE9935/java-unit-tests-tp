package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FibTest {

    @Test
    @DisplayName("Quand range est 1 : le résultat n'est pas vide et contient uniquement {0}")
    void testGetFibSeries_WithRangeOne_ShouldReturnListWithZero() {
        Fib fib = new Fib(1);

        List<Integer> result = fib.getFibSeries();

        assertThat(result).isNotEmpty();
        assertThat(result).containsExactly(0);
    }

    @Test
    @DisplayName("Quand range est 6 : le résultat doit respecter toutes les propriétés de la suite")
    void testGetFibSeries_WithRangeSix_ShouldRespectAllConditions() {
        Fib fib = new Fib(6);

        List<Integer> result = fib.getFibSeries();
        assertThat(result).hasSize(6);

        assertThat(result).contains(3);

        assertThat(result).doesNotContain(4);

        assertThat(result).containsExactly(0, 1, 1, 2, 3, 5);

        assertThat(result).isSorted();
    }
}
