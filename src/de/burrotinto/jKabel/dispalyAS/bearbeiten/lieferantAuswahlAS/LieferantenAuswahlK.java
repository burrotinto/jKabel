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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.lieferantAuswahlAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ILieferantE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import de.burrotinto.usefull.list.SortedSetAnzahlDerEingefuegtenElemente;

import java.util.Vector;
import java.util.function.Consumer;

/**
 * Created by Florian Klinger on 24.05.16.
 */
class LieferantenAuswahlK {

    private final IDBWrapper db;

    public LieferantenAuswahlK(IDBWrapper db) {
        this.db = db;
    }

    public Vector<ILieferantE> getLieferanten() {
        return new Vector<ILieferantE>(db.getAllLieferanten());
    }

    public Vector<ILieferantE> getLieferantenSorted() {
        SortedSetAnzahlDerEingefuegtenElemente<ILieferantE> sort = new SortedSetAnzahlDerEingefuegtenElemente<>();
        db.getAllKabeltypen().forEach(new Consumer<IKabeltypE>() {
            @Override
            public void accept(IKabeltypE iKabeltypE) {
                iKabeltypE.getTrommeln().forEach(new Consumer<ITrommelE>() {
                    @Override
                    public void accept(ITrommelE iTrommelE) {
                        sort.add(iTrommelE.getGeliefert().getLieferant());
                    }
                });
            }
        });
        return new Vector<ILieferantE>(sort);
    }
}
