/*
 * jKabel - Ein hochperfomantes, extremstanpassungsfähiges Mehrbenutzersystem zur erfassung von Kabelstrecken
 *
 * Copyright (C) 2016 Florian Klinger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.burrotinto.jKabel.config;

import de.burrotinto.jKabel.config.trommelSort.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derduke on 23.08.2016.
 */
@Configuration
public class ConfigReaderK {

    @Bean
    Richtung getSortierrichtungTrommel() {
        return new Richtung();
    }

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

    @Bean
    List<AbstractTrommelSort> getAllTrommelSort() {
        List<AbstractTrommelSort> list = new ArrayList<>();
        list.add(getTrommelDatumSort());
        list.add(getTrommelFuellstandS());
        list.add(getTrommelIntelligentSort());
        list.add(getTrommelNummerSort());
        return list;
    }

}
