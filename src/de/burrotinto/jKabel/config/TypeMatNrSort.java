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

import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;

import java.util.Comparator;

/**
 * Created by Florian Klinger on 21.07.16.
 */
public class TypeMatNrSort implements Comparator<IKabeltypE> {

    @Override
    public int compare(IKabeltypE t1, IKabeltypE t2) {
        return t1.getMaterialNummer() - t2.getMaterialNummer();
    }
}