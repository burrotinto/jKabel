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

package de.burrotinto.jKabel.dispalyAS.bearbeiten.streckenAS;

import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.*;
import de.burrotinto.usefull.list.SortedSetAnzahlDerEingefuegtenElemente;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.function.Consumer;

/**
 * Created by derduke on 22.05.16.
 */
class StreckenK {
    private final IDBWrapper db;
    private IKabeltypE typ = null;

    StreckenK(IDBWrapper db) {
        this.db = db;
    }

    public List<IStreckeE> getStreckenForTrommel(ITrommelE trommel) {
//        List<IStreckeE> strecken = db.getStreckenForTrommel(trommel);
//
//        Collections.sort(strecken, new Comparator<IStreckeE>() {
//            @Override
//            public int compare(IStreckeE o1, IStreckeE o2) {
//                return new Long(o1.getVerlegedatum()).compareTo(o2.getVerlegedatum());
//            }
//        });
//        return strecken;
        return trommel.getStrecken();
    }

    public IKabeltypE getTyp(ITrommelE trommel) {
        return trommel.getTyp();
    }

    public void setTyp(IKabeltypE typ) {
        this.typ = typ;
    }

    public void update(IStreckeE s) {
        db.update(s);
    }

    public void update(ITrommelE trommel) {
        db.update(trommel);
    }

    public void update(IKabeltypE typ) {
        db.update(typ);
    }

    public String getTimeString(long t) {
        StringBuilder sb = new StringBuilder();
        Date d = new Date(t);
        if (d.getDate() < 10) sb.append("0");
        sb.append(d.getDate()).append(".");
        if (d.getMonth() + 1 < 10) sb.append("0");
        sb.append((d.getMonth() + 1)).append(".").append(d.getYear() + 1900);
        return sb.toString();
    }

    public void remove(IStreckeE strecke) {
        db.remove(strecke);
    }

    public boolean richtigeRichtung(ITrommelE trommel, int start, int ende) {
        List<IStreckeE> list = getStreckenForTrommel(trommel);
        if (list.size() == 0) {
            return true;
        } else {
            int x = list.get(0).getStart() - list.get(0).getEnde();
            int y = start - ende;
            return (x <= 0 && y <= 0) || (x >= 0 && y >= 0);
        }

    }

    public IGeliefertE getLiefer(ITrommelE trommel) {
        return trommel.getGeliefert();
    }

    public long getLieferDate(ITrommelE trommel) {
        return getLiefer(trommel).getDatum();
    }

    public ILieferantE getLieferant(ITrommelE trommel) {
        return getLiefer(trommel).getLieferant();
    }

    public String getLieferscheinNR(ITrommelE trommel) {
        return getLiefer(trommel).getLieferscheinNr();
    }

    public Vector<ILieferantE> getLieferanten() {
        return new Vector<ILieferantE>(db.getAllLieferanten());
    }

    public void update(IGeliefertE g) {
        db.update(g);
    }

    public void eintragenStrecke(int ba, String text, long l, int start, int ende, ITrommelE trommel) {
        db.createStrecke(ba, text, l, start, ende, trommel);
    }

    public Vector<String> getTextForBA(int ba) {
        Set<String> set = new SortedSetAnzahlDerEingefuegtenElemente();
//        db.getAllKabeltypen().forEach(new Consumer<IKabeltypE>() {
//            @Override
//            public void accept(IKabeltypE iKabeltypE) {
//                iKabeltypE.getTrommeln().forEach(new Consumer<ITrommelE>() {
//                    @Override
//                    public void accept(ITrommelE iTrommelE) {
//                        iTrommelE.getStrecken().forEach(new Consumer<IStreckeE>() {
//                            @Override
//                            public void accept(IStreckeE iStreckeE) {
//                                if (new String( iStreckeE.getBa()+ "").startsWith("" + ba))
//                                    set.add(iStreckeE.getOrt() == null ? "" : iStreckeE.getOrt());
//                            }
//                        });
//                    }
//                });
//            }
//        });
        set.addAll(db.getAllTexteForBA(ba));
        return new Vector<>(set);
    }

    public boolean istAusserHaus(ITrommelE trommel) {
        for (IStreckeE s : getStreckenForTrommel(trommel)) {
            if (s.getEnde() < 0 || s.getStart() < 0) {
                return true;
            }
        }
        return false;
    }

    public int getRestMeter(ITrommelE trommel) {
        int laenge = trommel.getGesamtlaenge();
        for (IStreckeE s : trommel.getStrecken()) {
            laenge -= s.getMeter();
        }
        return laenge;
    }

    public ITrommelE getNewCopy(ITrommelE trommel) {
        return getTrommelByID(trommel.getId());
    }

    public ITrommelE getTrommelByID(Integer trommelID) {
        return trommelID == null ? null : db.getTrommelByID(trommelID);
    }


    public Vector<String> getLagerPlaetze() {
        SortedSetAnzahlDerEingefuegtenElemente<String> sort = new SortedSetAnzahlDerEingefuegtenElemente<>();
        typ.getTrommeln().forEach(new Consumer<ITrommelE>() {
            @Override
            public void accept(ITrommelE iTrommelE) {
                sort.add(iTrommelE.getLagerPlatz());
            }
        });
        return new Vector<String>(sort);
    }
}
