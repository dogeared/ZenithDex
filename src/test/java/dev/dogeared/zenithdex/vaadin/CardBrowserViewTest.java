package dev.dogeared.zenithdex.vaadin;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import dev.dogeared.zenithdex.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CardBrowserViewTest extends SpringBrowserlessTest {

    @Test
    void rendersFirstCardDetailsAndFooter() {
        navigate(CardBrowserView.class);

        assertThat(find(H3.class).withClassName("card-name").single().getText())
            .isEqualTo("4ND3RSØN");
        assertThat(indexText()).isEqualTo("1 / 100");
        assertThat(find(Div.class).withClassName("card-desc").single().getText()).isNotBlank();

        Div footer = find(Div.class).withClassName("footer").single();
        assertThat(footer.getElement().getTextRecursively())
            .isEqualTo("made with ❤️ by dogeared.");

        Anchor author = find(Anchor.class).withClassName("footer-link").single();
        assertThat(author.getHref()).isEqualTo("https://github.com/dogeared");
        assertThat(author.getText()).isEqualTo("dogeared");
    }

    @Test
    void desktopNavigationMovesForwardBackwardAndWraps() {
        navigate(CardBrowserView.class);
        List<Button> navButtons = find(Button.class).withClassName("desktop-nav-btn").all();
        Button previous = navButtons.get(0);
        Button next = navButtons.get(1);

        test(next).click();
        assertThat(indexText()).isEqualTo("2 / 100");

        test(previous).click();
        assertThat(indexText()).isEqualTo("1 / 100");

        test(previous).click();
        assertThat(indexText()).isEqualTo("100 / 100");
    }

    @Test
    void mobileNavigationMovesToNextCard() {
        navigate(CardBrowserView.class);
        Button next = find(Button.class).withClassName("mobile-nav-btn").all().get(1);

        test(next).click();

        assertThat(indexText()).isEqualTo("2 / 100");
    }

    @Test
    void alphabetBarJumpsToTheSelectedLetter() {
        navigate(CardBrowserView.class);

        test(find(Button.class).withClassName("alpha-letter").withText("B").single()).click();
        assertThat(indexText()).isEqualTo("11 / 100");

        test(find(Button.class).withClassName("alpha-letter").withText("Z").single()).click();
        assertThat(indexText()).isEqualTo("100 / 100");
    }

    @SuppressWarnings("unchecked")
    @Test
    void filtersNarrowTheDeckAndSortingChangesTheOrder() {
        navigate(CardBrowserView.class);
        ComboBox<String> planetFilter = (ComboBox<String>) find(ComboBox.class)
            .withLabel("Planet").single();
        ComboBox<String> raceFilter = (ComboBox<String>) find(ComboBox.class)
            .withLabel("Race").single();
        ComboBox<String> sortBy = (ComboBox<String>) find(ComboBox.class)
            .withLabel("Sort By").single();

        test(planetFilter).selectItem("Mars");
        assertThat(indexText()).isEqualTo("1 / 20");

        test(raceFilter).selectItem("Robot");
        assertThat(indexText()).isEqualTo("1 / 7");

        test(sortBy).selectItem("Number");
        assertThat(indexText()).isEqualTo("1 / 7");
        assertThat(find(H3.class).withClassName("card-name").single().getText())
            .isEqualTo("RØB1NSØN");
    }

    @Test
    void emptyDeckShowsMessageAndNavigationIsSafe() {
        CardBrowserView view = new CardBrowserView(new CardService(new ObjectMapper()));
        UI.getCurrent().add(view);

        assertThat(find(Span.class, view).withClassName("index-label").single().getText())
            .isEqualTo("0 / 0");
        assertThat(find(Paragraph.class, view).single().getText())
            .isEqualTo("No cards match the current filters.");
        assertThat(find(Div.class, view).withClassName("card-image").single()
            .hasClassName("card-image-empty")).isTrue();

        List<Button> navButtons = find(Button.class, view).withClassName("desktop-nav-btn").all();
        test(navButtons.get(1)).click();
        test(navButtons.get(0)).click();

        assertThat(find(Span.class, view).withClassName("index-label").single().getText())
            .isEqualTo("0 / 0");
    }

    private String indexText() {
        return find(Span.class).withClassName("index-label").single().getText();
    }
}
