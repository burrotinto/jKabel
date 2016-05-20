package de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis;

/**
 * Created by derduke on 19.05.2016.
 */
public class KabeltypE {
    private String typ;
    private final int materialNummer;

    @Override
    public String toString() {
        return "KabeltypE{" +
                "materialNummer=" + materialNummer +
                ", typ='" + typ + '\'' +
                '}';
    }

    public String getTyp() {
        return typ == null ? "" : typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getMaterialNummer() {
        return materialNummer;
    }


    public KabeltypE(int materialNummer, String typ) {
        this(typ, materialNummer);
    }

    public KabeltypE(String typ, int materialNummer) {
        this.typ = typ;
        this.materialNummer = materialNummer;

    }
}
