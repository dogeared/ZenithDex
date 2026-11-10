package dev.dogeared.zenithdex.service;

import dev.dogeared.zenithdex.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CardServiceTest {

    private CardService cardService;

    @BeforeEach
    void setUp() throws Exception {
        cardService = new CardService(new ObjectMapper());
        cardService.init();
    }

    @Test
    void initLoadsEveryCardFromTheReferenceFile() {
        List<Card> cards = cardService.getCards();

        assertThat(cards).hasSize(100);
        assertThat(cards).allSatisfy(card -> {
            assertThat(card.getName()).isNotBlank();
            assertThat(card.getNum()).isPositive();
            assertThat(card.getCol()).isPositive();
            assertThat(card.getRow()).isPositive();
        });
    }

    @Test
    void getCardsContainsKnownCard() {
        assertThat(cardService.getCards())
            .filteredOn(card -> card.getNum() == 201)
            .singleElement()
            .satisfies(card -> {
                assertThat(card.getName()).isEqualTo("Hiroshi Sun");
                assertThat(card.getPlanet()).isEqualTo(2);
                assertThat(card.getRace()).isEqualTo(2);
                assertThat(card.getCost()).isEqualTo(1);
            });
    }

    @Test
    void racesAndPlanetsExposeDisplayNames() {
        assertThat(cardService.getRaces())
            .containsEntry(1, "Robot")
            .containsEntry(2, "Human")
            .containsEntry(3, "Animod")
            .hasSize(3);

        assertThat(cardService.getPlanets())
            .containsEntry(1, "Mercury")
            .containsEntry(2, "Venus")
            .containsEntry(3, "Terra")
            .containsEntry(4, "Mars")
            .containsEntry(5, "Jupiter")
            .hasSize(5);
    }
}
