package dev.dogeared.zenithdex.vaadin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@PageTitle("ZenithDex")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView() {
        H1 title = new H1("Welcome to ZenithDex");
        Paragraph description = new Paragraph("Your Spring Boot + Vaadin application is running.");
        RouterLink browseCards = new RouterLink("Browse Cards", CardBrowserView.class);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        add(title, description, browseCards);
    }
}
