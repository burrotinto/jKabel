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
                if ((!isBeendet(o1) && isBeendet(o2)) || (!isBeendet(o2) && isBeendet(o1))) {
                    return isBeendet(o1) ? 1 : -1;
                } else if ((!isAusserHaus(o1) && isAusserHaus(o2)) || (!isAusserHaus(o2) && isAusserHaus(o1))) {
                    return isAusserHaus(o1) ? 1 : -1;
                } else if (isAusserHaus(o1) && isAusserHaus(o2)) {
                    return getAusleihMinuten(o2) - getAusleihMinuten(o1);
                } else {
//                return o2.getId() - o2.getId();
                    return ((Long) o1.getGeliefert().getDatum()).compareTo(o2.getGeliefert().getDatum());
                }
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

    public IKabeltypE getNewTypCopy(IKabeltypE typ) {
        return db.getTypByMaterialnummer(typ.getMaterialNummer());
    }

    public IKabeltypE getNewTypCopy(int materialnummer) {
        return db.getTypByMaterialnummer(materialnummer);
    }

    public int getMaxMeter(IKabeltypE typ) {
        int x = 0;
        for (ITrommelE t : getAllTrommelForTyp(typ)) {
            if (!isAusserHaus(t)) {
                x += getRestMeter(t);
            } else {
                int i = 0;
                for (IStreckeE s : t.getStrecken()) {
                    if (s.getEnde() >= 0) {
                        i += s.getMeter();
                    }
                }
                x += t.getGesamtlaenge() - i;
            }
        }
        return x;
    }

    public int getMinMeter(IKabeltypE typ) {
        int x = 0;
        for (ITrommelE t : getAllTrommelForTyp(typ)) {
            if (!isAusserHaus(t)) {
                x += getRestMeter(t);
            }

        }
        return x;
    }

    public int getAusleihStunden(ITrommelE t) {
        return getAusleihMinuten(t) / 60;
    }

    public int getAusleihMinuten(ITrommelE t) {
        List<IStreckeE> strecken = t.getStrecken();
        Collections.sort(strecken, new Comparator<IStreckeE>() {
            @Override
            public int compare(IStreckeE iStreckeE, IStreckeE t1) {
                return -((Long) iStreckeE.getVerlegedatum()).compareTo(t1.getVerlegedatum());
            }
        });
        if (strecken.size() == 0) {
            return 0;
        } else {
            return (int) ((System.currentTimeMillis() - strecken.get(0).getVerlegedatum()) / ((long) (1000 * 60)));
        }
    }

    public int getAusleihtage(ITrommelE t) {
        return getAusleihStunden(t) / 24;
    }

    public boolean isBeendet(ITrommelE t) {
        return t.isFreigemeldet() && getRestMeter(t) == 0;
    }
}
