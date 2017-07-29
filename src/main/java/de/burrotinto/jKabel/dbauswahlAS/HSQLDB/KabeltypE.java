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

package de.burrotinto.jKabel.dbauswahlAS.HSQLDB;

import de.burrotinto.jKabel.dbauswahlAS.enitys.AbstractKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;

import java.util.List;

/**
 * Created by derduke on 19.05.2016.
 */
class KabeltypE extends AbstractKabeltypE {
    private final HSQLDBWrapper db;
    private List<ITrommelE> trommeln = null;

    KabeltypE(String typ, int materialNummer, HSQLDBWrapper db) {
        super(materialNummer);
        setTyp(typ);
        this.db = db;

    }

    @Override
    public List<ITrommelE> getTrommeln() {
        return trommeln = trommeln == null ? db.getTrommelnForTyp(this) : trommeln;
//        return db.getTrommelnForTyp(this);
    }
}
