package de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis;

/**
 * Created by derduke on 20.05.16.
 */
public class TrommelE {
    private int id;
    private String trommelnummer;
    private int gesamtlaenge;
    private final KabeltypE kabelTyp;

    public TrommelE(KabeltypE kabelTyp, int id, String trommelnummer, int gesamtlaenge) {
        this.kabelTyp = kabelTyp;
        this.id = id;
        this.trommelnummer = trommelnummer;
        this.gesamtlaenge = gesamtlaenge;
    }


    public TrommelE(KabeltypE kabelTyp, String trommelnummer, int gesamtlaenge) {
        this.kabelTyp = kabelTyp;

        this.trommelnummer = trommelnummer;
        this.gesamtlaenge = gesamtlaenge;
    }

    public int getMaterialNummer() {
        return kabelTyp.getMaterialNummer();
    }

    public int getGesamtlaenge() {
        return gesamtlaenge;
    }

    public void setGesamtlaenge(int gesamtlaenge) {
        this.gesamtlaenge = gesamtlaenge;
    }

    public int getId() {
        return id;
    }

    public String getTrommelnummer() {
        return trommelnummer;
    }


    public void setTrommelnummer(String trommelnummer) {
        this.trommelnummer = trommelnummer;
    }

    @Override
    public String toString() {
        return "TrommelE{" +
                "id=" + id +
                ", trommelnummer='" + trommelnummer + '\'' +
                ", gesamtlaenge=" + gesamtlaenge +
                ", kabelTyp=" + kabelTyp.toString() +
                '}';
    }
}
