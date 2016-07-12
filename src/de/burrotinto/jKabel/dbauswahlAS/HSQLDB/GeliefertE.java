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

import de.burrotinto.jKabel.dbauswahlAS.enitys.IGeliefertE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ILieferantE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;

/**
 * Created by derduke on 23.05.16.
 */
class GeliefertE implements IGeliefertE {
    private ILieferantE lieferant;
    private long datum;
    private String lieferscheinNr;
    private ITrommelE trommel;

    GeliefertE(long datum, String lieferscheinNr, ILieferantE lieferant, ITrommelE trommel) {
        this.lieferant = lieferant;
        this.trommel = trommel;
        this.lieferscheinNr = lieferscheinNr;
        this.datum = datum;
    }

    @Override
    public long getDatum() {
        return datum;
    }

    @Override
    public String getLieferscheinNr() {
        return lieferscheinNr;
    }

    @Override
    public ITrommelE getTrommel() {
        return trommel;
    }

    @Override
    public ILieferantE getLieferant() {
        return lieferant;
    }

    public void setLieferantID(ILieferantE lieferant) {
        this.lieferant = lieferant;
    }
}
