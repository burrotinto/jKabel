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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.trommelAuswahlAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IStreckeE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by derduke on 22.05.16.
 */
class TommelAuswahlK {
    private final IDBWrapper db;
    private Comparator<ITrommelE> comperator = null;


    public TommelAuswahlK(IDBWrapper db) {
        this.db = db;
        comperator = new Comparator<ITrommelE>() {
            @Override
            public int compare(ITrommelE o1, ITrommelE o2) {
                return ((Long) o1.getGeliefert().getDatum()).compareTo(o2.getGeliefert().getDatum());
            }
        };
    }

    public List<ITrommelE> getAllTrommelForTyp(IKabeltypE typ) {
        List<ITrommelE> list = typ.getTrommeln();
        Collections.sort(list, comperator);
        return list;
    }

    public int getRestMeter(ITrommelE trommel) {
        int laenge = trommel.getGesamtlaenge();
        for (IStreckeE s : trommel.getStrecken()) {
            laenge -= s.getMeter();
        }
        return laenge;
    }

    public boolean isAusserHaus(ITrommelE t) {
        return getBaustelle(t) != null;
    }

    public String getBaustelle(ITrommelE t) {
        if (t != null)
            for (IStreckeE s : t.getStrecken()) {
                if (s.getEnde() < 0 || s.getStart() < 0) {
                    return s.getOrt();
                }
            }
        return null;
    }

    public void setSortierReihenfolge(Comparator<ITrommelE> comperator) {
        if (comperator != null) {
            this.comperator = comperator;
        }
    }
}
