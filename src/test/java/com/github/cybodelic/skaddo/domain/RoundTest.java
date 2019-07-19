package com.github.cybodelic.skaddo.domain;

import org.junit.Assert;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class RoundTest {

    @Test
    public void compareToTest() throws InterruptedException {
        Round r1 = new Round(new Player("Player1"),92);
        Thread.sleep(2);
        Round r2 = new Round(new Player("Player2"),19);
        Assert.assertThat(r1.compareTo(r2), lessThan(0));
    }

    @Test
    public void compareToNullTest() throws InterruptedException {
        Round r = new Round();
        r.setDeclarer(new Player("fred"));
        r.setScore(SkatConstants.POSSIBLE_SCORE_VALUES[5]);
        assertThatThrownBy(() -> r.compareTo(null)).isInstanceOf(NullPointerException.class);
    }
}
