package dev.dogeared.zenithdex.vaadin;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.dogeared.zenithdex.model.Card;
import dev.dogeared.zenithdex.service.CardService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Route("cards")
@PageTitle("ZenithDex - Card Browser")
@AnonymousAllowed
public class CardBrowserView extends VerticalLayout {

    private static final int CARD_WIDTH = 200;
    private static final int CARD_HEIGHT = 313;

    private final CardService cardService;
    private final List<Card> cards;
    private final Map<Integer, String> races;
    private final Map<Integer, String> planets;

    private int currentIndex = 0;
    private Div cardImage;
    private Div detailsPanel;
    private Span indexLabel;

    // Filter combos
    private ComboBox<String> planetFilter;
    private ComboBox<String> raceFilter;
    private List<Card> filteredCards;

    public CardBrowserView(CardService cardService) {
        this.cardService = cardService;
        this.cards = cardService.getCards().stream()
            .sorted(Comparator.comparingInt(Card::getNum))
            .toList();
        this.races = cardService.getRaces();
        this.planets = cardService.getPlanets();
        this.filteredCards = List.copyOf(cards);

        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("ZenithDex Card Browser");
        add(title);

        add(buildFilterBar());
        add(buildCardDisplay());
        add(buildNavigationControls());

        updateCard();
    }

    private HorizontalLayout buildFilterBar() {
        planetFilter = new ComboBox<>("Planet");
        planetFilter.setItems("All", "Mercury", "Venus", "Terra", "Mars", "Jupiter");
        planetFilter.setValue("All");
        planetFilter.addValueChangeListener(e -> applyFilters());

        raceFilter = new ComboBox<>("Race");
        raceFilter.setItems("All", "Robot", "Human", "Animod");
        raceFilter.setValue("All");
        raceFilter.addValueChangeListener(e -> applyFilters());

        HorizontalLayout filterBar = new HorizontalLayout(planetFilter, raceFilter);
        filterBar.setAlignItems(Alignment.BASELINE);
        return filterBar;
    }

    private HorizontalLayout buildCardDisplay() {
        cardImage = new Div();
        cardImage.setWidth(CARD_WIDTH + "px");
        cardImage.setHeight(CARD_HEIGHT + "px");
        cardImage.getStyle()
            .set("background-image", "url('images/cards.jpg')")
            .set("background-repeat", "no-repeat")
            .set("border-radius", "8px")
            .set("box-shadow", "0 4px 12px rgba(0,0,0,0.3)")
            .set("flex-shrink", "0");

        detailsPanel = new Div();
        detailsPanel.setWidth("300px");
        detailsPanel.getStyle()
            .set("padding", "16px")
            .set("background", "var(--lumo-contrast-5pct)")
            .set("border-radius", "8px");

        HorizontalLayout display = new HorizontalLayout(cardImage, detailsPanel);
        display.setAlignItems(FlexComponent.Alignment.START);
        display.setSpacing(true);
        return display;
    }

    private HorizontalLayout buildNavigationControls() {
        Button prevBtn = new Button("Previous", e -> navigate(-1));
        prevBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button nextBtn = new Button("Next", e -> navigate(1));
        nextBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        indexLabel = new Span();

        HorizontalLayout nav = new HorizontalLayout(prevBtn, indexLabel, nextBtn);
        nav.setAlignItems(Alignment.CENTER);
        nav.setSpacing(true);
        return nav;
    }

    private void applyFilters() {
        String selectedPlanet = planetFilter.getValue();
        String selectedRace = raceFilter.getValue();

        filteredCards = cards.stream()
            .filter(c -> "All".equals(selectedPlanet) || planets.getOrDefault(c.getPlanet(), "").equals(selectedPlanet))
            .filter(c -> "All".equals(selectedRace) || races.getOrDefault(c.getRace(), "").equals(selectedRace))
            .toList();

        currentIndex = 0;
        updateCard();
    }

    private void navigate(int direction) {
        if (filteredCards.isEmpty()) return;
        currentIndex = (currentIndex + direction + filteredCards.size()) % filteredCards.size();
        updateCard();
    }

    private void updateCard() {
        if (filteredCards.isEmpty()) {
            cardImage.getStyle().set("background-image", "none");
            detailsPanel.removeAll();
            detailsPanel.add(new Paragraph("No cards match the current filters."));
            indexLabel.setText("0 / 0");
            return;
        }

        Card card = filteredCards.get(currentIndex);

        // Sprite sheet uses 1-based row/col; convert to 0-based pixel offsets
        int offsetX = (card.getCol() - 1) * CARD_WIDTH;
        int offsetY = (card.getRow() - 1) * CARD_HEIGHT;

        cardImage.getStyle()
            .set("background-image", "url('images/cards.jpg')")
            .set("background-position", "-" + offsetX + "px -" + offsetY + "px");

        detailsPanel.removeAll();

        H3 cardName = new H3(card.getName());
        cardName.getStyle().set("margin-top", "0");
        detailsPanel.add(cardName);

        String planetName = planets.getOrDefault(card.getPlanet(), "Unknown");
        String raceName = races.getOrDefault(card.getRace(), "Unknown");

        detailsPanel.add(new Html("<div><strong>Number:</strong> " + card.getNum() + "</div>"));
        detailsPanel.add(new Html("<div><strong>Planet:</strong> " + planetName + "</div>"));
        detailsPanel.add(new Html("<div><strong>Race:</strong> " + raceName + "</div>"));
        detailsPanel.add(new Html("<div><strong>Cost:</strong> " + card.getCost() + "</div>"));

        if (card.getDesc() != null && !card.getDesc().isEmpty()) {
            Div descDiv = new Div();
            descDiv.getStyle()
                .set("margin-top", "12px")
                .set("padding", "8px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("border-radius", "4px")
                .set("font-style", "italic");
            descDiv.setText(card.getDesc());
            detailsPanel.add(descDiv);
        }

        indexLabel.setText((currentIndex + 1) + " / " + filteredCards.size());
    }
}
