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

import de.burrotinto.jKabel.dbauswahlAS.enitys.AbstractTrommelE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IGeliefertE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IStreckeE;

import java.util.List;

/**
 * Created by derduke on 20.05.16.
 */
class TrommelE extends AbstractTrommelE {
    private final HSQLDBWrapper db;
    private IGeliefertE geliefert = null;
    private List<IStreckeE> strecken = null;

    TrommelE(IKabeltypE kabelTyp, int id, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start, boolean freigemeldet, HSQLDBWrapper db) {
        super(id, kabelTyp);
        setStart(start);
        setLagerPlatz(lagerPlatz);
        setTrommelnummer(trommelnummer);
        setGesamtlaenge(gesamtlaenge);
        setFreimeldung(freigemeldet);
        this.db = db;
    }


    @Override
    public List<IStreckeE> getStrecken() {
        return strecken = strecken == null ? db.getStreckenForTrommel(this) : strecken;
    }

    @Override
    public IGeliefertE getGeliefert() {
        return geliefert = geliefert == null ? db.getLiefer(this) : geliefert;
    }
}
