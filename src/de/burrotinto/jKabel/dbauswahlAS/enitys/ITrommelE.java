
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

package de.burrotinto.jKabel.dbauswahlAS.enitys;

import java.io.Serializable;

/**
 * Created by derduke on 25.05.16.
 */
public interface ITrommelE extends Serializable {
    int getStart();

    void setStart(int start);

    String getLagerPlatz();

    void setLagerPlatz(String lagerPlatz);

    int getMaterialNummer();

    int getGesamtlaenge();

    void setGesamtlaenge(int gesamtlaenge);

    int getId();

    String getTrommelnummer();

    void setTrommelnummer(String trommelnummer);

    boolean isFreigemeldet();

    @Override
    String toString();

    void setFreimeldung(boolean freigemeldet);
}
