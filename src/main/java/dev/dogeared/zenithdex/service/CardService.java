package dev.dogeared.zenithdex.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dogeared.zenithdex.model.Card;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService {

    private final List<Card> cards = new ArrayList<>();
    private final Map<Integer, String> races = Map.of(
    1, "Robot",
    2, "Human",
    3, "Animod"
    );
    private final Map<Integer, String> planets = Map.of(
        2, "Venus",
        1, "Mercury",
        3, "Terra",
        4, "Mars",
        5, "Jupiter"

    );
    private final ObjectMapper objectMapper;

    public CardService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        InputStream inputStream = new ClassPathResource("card_ref.json").getInputStream();
        JsonNode root = objectMapper.readTree(inputStream);
        JsonNode cardRefNode = root.get("card_ref");
        Map<String, Card> cardMap = objectMapper.convertValue(
            cardRefNode, new TypeReference<Map<String, Card>>() {}
        );
        cards.addAll(cardMap.values());
    }

    public List<Card> getCards() {
        return cards;
    }

    public Map<Integer, String> getRaces() {
        return races;
    }

    public Map<Integer, String> getPlanets() {
        return planets;
    }
}
