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

/**
 * Created by derduke on 19.05.2016.
 */
public abstract class AbstractKabeltypE implements IKabeltypE {
    private final int materialNummer;
    private String typ;

    protected AbstractKabeltypE(int materialNummer) {
        this.materialNummer = materialNummer;
    }


    @Override
    public String toString() {
        return materialNummer + " - " + typ;
    }

    @Override
    public String getTyp() {
        return typ == null ? "" : typ;
    }

    @Override
    public void setTyp(String typ) {
        this.typ = typ;
    }

    @Override
    public int getMaterialNummer() {
        return materialNummer;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return getMaterialNummer() == ((IKabeltypE) obj).getMaterialNummer();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return materialNummer;
    }
}
