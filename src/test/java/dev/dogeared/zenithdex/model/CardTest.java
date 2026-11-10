package dev.dogeared.zenithdex.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

    @Test
    void gettersAndSettersRoundTripAllFields() {
        Card card = new Card();

        card.setNum(42);
        card.setCol(3);
        card.setRow(7);
        card.setGoodies(2);
        card.setPlanet(5);
        card.setCost(9);
        card.setRace(1);
        card.setRule("1,2,3");
        card.setDesc("A test card");
        card.setName("Test Subject");

        assertThat(card.getNum()).isEqualTo(42);
        assertThat(card.getCol()).isEqualTo(3);
        assertThat(card.getRow()).isEqualTo(7);
        assertThat(card.getGoodies()).isEqualTo(2);
        assertThat(card.getPlanet()).isEqualTo(5);
        assertThat(card.getCost()).isEqualTo(9);
        assertThat(card.getRace()).isEqualTo(1);
        assertThat(card.getRule()).isEqualTo("1,2,3");
        assertThat(card.getDesc()).isEqualTo("A test card");
        assertThat(card.getName()).isEqualTo("Test Subject");
    }
}
