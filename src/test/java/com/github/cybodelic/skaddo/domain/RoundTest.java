package com.github.cybodelic.skaddo.domain;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class RoundTest {

    @Test
    public void compareToTest() {
        Round r1 = new Round(new Player("Player1"),92);
        Round r2 = new Round(new Player("Player2"),19);
        r1.setIndex(0);
        r2.setIndex(1);
        assertThat(r1.compareTo(r2), lessThan(0));
        r1.setIndex(1);
        r2.setIndex(0);
        assertThat(r2.compareTo(r1), lessThan(0));
        r1.setIndex(1);
        r2.setIndex(1);
        assertThat(r2.compareTo(r1), is(0));
    }
}
