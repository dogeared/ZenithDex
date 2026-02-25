package dev.dogeared.zenithdex.vaadin;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
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

    private final List<Card> cards;
    private final Map<Integer, String> races;
    private final Map<Integer, String> planets;

    private int currentIndex = 0;
    private Div cardImage;
    private Div detailsPanel;
    private Span indexLabel;

    private ComboBox<String> planetFilter;
    private ComboBox<String> raceFilter;
    private List<Card> filteredCards;

    public CardBrowserView(CardService cardService) {
        this.cards = cardService.getCards().stream()
            .sorted(Comparator.comparingInt(Card::getNum))
            .toList();
        this.races = cardService.getRaces();
        this.planets = cardService.getPlanets();
        this.filteredCards = List.copyOf(cards);

        addClassName("card-browser");
        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(false);

        H2 title = new H2("ZenithDex");
        title.addClassName("title");
        add(title);

        add(buildFilterBar());
        add(buildNavigationControls());
        add(buildCardDisplay());

        updateCard();
    }

    private HorizontalLayout buildFilterBar() {
        planetFilter = new ComboBox<>("Planet");
        planetFilter.setItems("All", "Mercury", "Venus", "Terra", "Mars", "Jupiter");
        planetFilter.setValue("All");
        planetFilter.setWidthFull();
        planetFilter.addValueChangeListener(e -> applyFilters());

        raceFilter = new ComboBox<>("Race");
        raceFilter.setItems("All", "Robot", "Human", "Animod");
        raceFilter.setValue("All");
        raceFilter.setWidthFull();
        raceFilter.addValueChangeListener(e -> applyFilters());

        HorizontalLayout filterBar = new HorizontalLayout(planetFilter, raceFilter);
        filterBar.addClassName("filter-bar");
        filterBar.setWidthFull();
        filterBar.setSpacing(true);
        filterBar.setPadding(false);
        return filterBar;
    }

    private Div buildCardDisplay() {
        cardImage = new Div();
        cardImage.addClassName("card-image");

        detailsPanel = new Div();
        detailsPanel.addClassName("details-panel");

        Div display = new Div(cardImage, detailsPanel);
        display.addClassName("card-display");
        return display;
    }

    private HorizontalLayout buildNavigationControls() {
        Button prevBtn = new Button(VaadinIcon.ARROW_LEFT.create(), e -> navigate(-1));
        prevBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button nextBtn = new Button(VaadinIcon.ARROW_RIGHT.create(), e -> navigate(1));
        nextBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        indexLabel = new Span();

        HorizontalLayout nav = new HorizontalLayout(prevBtn, indexLabel, nextBtn);
        nav.addClassName("nav-bar");
        nav.setAlignItems(Alignment.CENTER);
        nav.setJustifyContentMode(JustifyContentMode.CENTER);
        nav.setWidthFull();
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

        int offsetX = (card.getCol() - 1) * CARD_WIDTH;
        int offsetY = (card.getRow() - 1) * CARD_HEIGHT;

        cardImage.getStyle()
            .set("background-position", "-" + offsetX + "px -" + offsetY + "px");

        detailsPanel.removeAll();

        H3 cardName = new H3(card.getName());
        cardName.addClassName("card-name");
        detailsPanel.add(cardName);

        String planetName = planets.getOrDefault(card.getPlanet(), "Unknown");
        String raceName = races.getOrDefault(card.getRace(), "Unknown");

        Div statsRow = new Div();
        statsRow.addClassName("stats-row");
        statsRow.add(
            new Html("<span><strong>#</strong> " + card.getNum() + "</span>"),
            new Html("<span><strong>Planet:</strong> " + planetName + "</span>"),
            new Html("<span><strong>Race:</strong> " + raceName + "</span>"),
            new Html("<span><strong>Cost:</strong> " + card.getCost() + "</span>")
        );
        detailsPanel.add(statsRow);

        if (card.getDesc() != null && !card.getDesc().isEmpty()) {
            Div descDiv = new Div();
            descDiv.addClassName("card-desc");
            descDiv.setText(card.getDesc());
            detailsPanel.add(descDiv);
        }

        indexLabel.setText((currentIndex + 1) + " / " + filteredCards.size());
    }
}
