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

package de.burrotinto.jKabel.dbauswahlAS;

import de.burrotinto.jKabel.dbauswahlAS.enitys.*;

import java.util.List;

/**
 * Regelt wie eine DB zu benutzen ist. Die Methodennamen sollten selbserklärend sein
 * Created by Florian Klinger on 19.05.2016.
 */
public interface IDBWrapper {
    List<IKabeltypE> getAllKabeltypen();

    List<ILieferantE> getAllLieferanten();

    List<String> getAllTexteForBA(int ba);

    ITrommelE getTrommelByID(int id);

    boolean update(IKabeltypE kabeltyp);

    /**
     * Erstellt einen Kabeltypen
     *
     * @param name
     * @param materialnummer
     * @return
     */
    boolean createKabeltyp(String name, int materialnummer);

    boolean update(IStreckeE strecke);

    boolean createStrecke(int ba, String ort, long verlegedatum, int start, int ende, ITrommelE trommel);

    boolean update(ITrommelE trommel);

    boolean createTrommel(IKabeltypE kabelTyp, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start, ILieferantE lieferantE, long Lieferdatum, String lieferscheinNr);

    boolean remove(IStreckeE strecke);

    boolean createLieferant(String name);

    boolean update(ILieferantE lieferantE);

    boolean update(IGeliefertE geliefert);

    boolean isClosed();

    IKabeltypE getTypByMaterialnummer(int materialNummer);
}
