package de.burrotinto.jKabel.config;

import de.burrotinto.jKabel.config.trommelSort.TrommelDatumSort;
import de.burrotinto.jKabel.config.trommelSort.TrommelFuellstand;
import de.burrotinto.jKabel.config.trommelSort.TrommelIntelligentSort;
import de.burrotinto.jKabel.config.trommelSort.TrommelNummerSort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by derduke on 23.08.2016.
 */
@Configuration
public class ConfigReaderK {

    @Bean
    TrommelDatumSort getTrommelDatumSort() {
        return new TrommelDatumSort();
    }

    @Bean
    TrommelFuellstand getTrommelFuellstandS() {
        return new TrommelFuellstand();
    }

    @Bean
    TrommelIntelligentSort getTrommelIntelligentSort() {
        return new TrommelIntelligentSort();
    }

    @Bean
    TrommelNummerSort getTrommelNummerSort() {
        return new TrommelNummerSort();
    }

}
