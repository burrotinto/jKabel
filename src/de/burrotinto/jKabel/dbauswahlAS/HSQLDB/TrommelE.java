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
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by derduke on 20.05.16.
 */
class TrommelE extends AbstractTrommelE {
    private static Logger log = Logger.getLogger(TrommelE.class);
    private final HSQLDBWrapper db;
    private IGeliefertE geliefert = null;
    private List<IStreckeE> strecken = null;
    private boolean updateS = true;
    private boolean updateG = true;

    TrommelE(IKabeltypE kabelTyp, int id, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start, boolean freigemeldet, HSQLDBWrapper db) {
        super(id, kabelTyp);
        super.setStart(start);
        super.setLagerPlatz(lagerPlatz);
        super.setTrommelnummer(trommelnummer);
        super.setGesamtlaenge(gesamtlaenge);
        super.setFreimeldung(freigemeldet);
        this.db = db;
    }


    @Override
    public List<IStreckeE> getStrecken() {
        if (updateS) {
            strecken = db.getStreckenForTrommel(this);
            updateS = false;
        }
        return strecken = strecken;
    }

    @Override
    public IGeliefertE getGeliefert() {
        if (updateG) {
            geliefert = db.getLiefer(this);
            updateG = false;
        }
        return geliefert;
    }

    @Override
    public void setFreimeldung(boolean freigemeldet) {
        if (freigemeldet != isFreigemeldet()) doUpdate();
        super.setFreimeldung(freigemeldet);
    }

    @Override
    public void setGesamtlaenge(int gesamtlaenge) {
        if (gesamtlaenge != getStart()) doUpdate();
        super.setGesamtlaenge(gesamtlaenge);
    }

    @Override
    public void setLagerPlatz(String lagerPlatz) {
        if (!lagerPlatz.equals(getLagerPlatz())) doUpdate();
        super.setLagerPlatz(lagerPlatz);
    }

    @Override
    public void setStart(int start) {
        if (start != getStart()) doUpdate();
        super.setStart(start);
    }

    @Override
    public void setTrommelnummer(String trommelnummer) {
        if (!trommelnummer.equals(getTrommelnummer())) doUpdate();
        super.setTrommelnummer(trommelnummer);

    }

    void doUpdate() {
        updateS = true;
        updateG = true;
    }
}
