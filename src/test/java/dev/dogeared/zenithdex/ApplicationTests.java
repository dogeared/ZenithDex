package dev.dogeared.zenithdex;

import dev.dogeared.zenithdex.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoadsWithAllExpectedBeans() {
        assertThat(context.getBean(CardService.class)).isNotNull();
        assertThat(context.getBean(Application.class)).isNotNull();
    }
}
