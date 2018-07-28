package com.github.cybodelic.skaddo.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    @Test
    public void getComposedName() {
        Player p = new Player();
        p.setNickName("nickName");
        assertThat(p.getComposedName()).isEqualTo("nickName");
        p.setFirstName("firstName");
        assertThat(p.getComposedName()).isEqualTo("firstName nickName");
        p.setLastName("lastName");
        assertThat(p.getComposedName()).isEqualTo("firstName nickName lastName");
    }

}