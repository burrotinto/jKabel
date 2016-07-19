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
 * Created by derduke on 23.05.16.
 */
public abstract class AbstractGeliefertE implements IGeliefertE {
    private long datum;
    private String lieferscheinNr;
    private ITrommelE trommel;
    private ILieferantE lieferant;

    @Override
    public long getDatum() {
        return datum;
    }

    protected void setDatum(long datum) {
        this.datum = datum;
    }

    @Override
    public String getLieferscheinNr() {
        return lieferscheinNr;
    }

    protected void setLieferscheinNr(String lieferscheinNr) {
        this.lieferscheinNr = lieferscheinNr;
    }

    @Override
    public ITrommelE getTrommel() {
        return trommel;
    }

    public void setTrommel(ITrommelE trommel) {
        this.trommel = trommel;
    }

    @Override
    public void setLieferantID(ILieferantE lieferant) {
        this.lieferant = lieferant;
    }

    @Override
    public ILieferantE getLieferant() {
        return lieferant;
    }
}
