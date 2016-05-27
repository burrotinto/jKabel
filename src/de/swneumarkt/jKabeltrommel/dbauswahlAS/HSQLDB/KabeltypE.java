package de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;

/**
 * Created by derduke on 19.05.2016.
 */
class KabeltypE implements IKabeltypE {
    private final int materialNummer;
    private String typ;

    KabeltypE(int materialNummer, String typ) {
        this(typ, materialNummer);
    }

    KabeltypE(String typ, int materialNummer) {
        this.typ = typ;
        this.materialNummer = materialNummer;

    }

    @Override
    public String toString() {
        return materialNummer + " - " + typ;
    }

    @Override
    public String getTyp() {
        return typ == null ? "" : typ;
    }

    @Override
    public void setTyp(String typ) {
        this.typ = typ;
    }

    @Override
    public int getMaterialNummer() {
        return materialNummer;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return getMaterialNummer() == ((IKabeltypE) obj).getMaterialNummer();
        } catch (Exception e) {
            return false;
        }
    }
}
