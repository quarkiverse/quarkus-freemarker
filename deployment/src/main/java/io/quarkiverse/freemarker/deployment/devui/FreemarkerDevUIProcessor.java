package io.quarkiverse.freemarker.deployment.devui;

import freemarker.template.Configuration;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.devui.spi.page.PageBuilder;

/**
 * Dev UI card for displaying important details such as the Freemarker library version.
 */
public class FreemarkerDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    void createVersion(BuildProducer<CardPageBuildItem> cardPageBuildItemBuildProducer) {
        final CardPageBuildItem card = new CardPageBuildItem();

        final PageBuilder versionPage = Page.externalPageBuilder("Version")
                .icon("font-awesome-solid:code")
                .url("https://freemarker.apache.org/")
                .doNotEmbed()
                .staticLabel(Configuration.class.getPackage().getImplementationVersion());
        card.addPage(versionPage);

        card.setCustomCard("qwc-freemarker-card.js");

        cardPageBuildItemBuildProducer.produce(card);
    }
}
