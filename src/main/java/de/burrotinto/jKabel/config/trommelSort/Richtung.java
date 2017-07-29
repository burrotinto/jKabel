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

package de.burrotinto.jKabel.config.trommelSort;

import org.springframework.stereotype.Component;

/**
 * Created by Florian Klinger on 24.08.16.
 */
@Component
public class Richtung {
    private boolean aufsteigend = true;

    public boolean isAufsteigend() {
        return aufsteigend;
    }

    public void setAufsteigend(boolean aufsteigend) {
        this.aufsteigend = aufsteigend;
    }
}
